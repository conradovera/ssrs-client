package consumidor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.axis2.Constants;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.CredentialsNotAvailableException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.params.DefaultHttpParams;
import org.apache.commons.io.IOUtils;

import arquitectura.JCIFS_NTLMScheme;
import modelo.DatosEndpoint;
import modelo.DatosReporte;
import modelo.Reporte;
import wsdl.ReportExecutionServiceStub;
import wsdl.ReportExecutionServiceStub.ArrayOfParameterValue;
import wsdl.ReportExecutionServiceStub.ExecutionHeader;
import wsdl.ReportExecutionServiceStub.ExecutionHeaderE;
import wsdl.ReportExecutionServiceStub.LoadReport;
import wsdl.ReportExecutionServiceStub.LoadReportResponse;
import wsdl.ReportExecutionServiceStub.Render;
import wsdl.ReportExecutionServiceStub.RenderResponse;
import wsdl.ReportExecutionServiceStub.SetExecutionParameters;

public class ServicioReportes {
	
	
	public Reporte obtenerReporte(DatosReporte datosReporte){
		if(datosReporte==null){
			throw new RuntimeException("Los datos del reporte son requeridos");
		}
		return obtenerReporte(datosReporte.getDatosEndpoint(), datosReporte.getRutaReporte(), datosReporte.getFormatoSalida(), datosReporte.getParametros());
	}
	
	/**
	 * @param rutaReporte ruta del reporte de Reporting Service
	 * @param formatoSalida formato Reporting Service de salida del reporte. Si viene nulo toma por defecto pdf
	 * @param parametros parametros del reporte. Puede ser nulo o venir vacío
	 * @param datosEndpoint objeto con propiedades del contrato WSDL. Si viene nulo toma la configuración default
	 * */
	public Reporte obtenerReporte(DatosEndpoint datosEndpoint, String rutaReporte,String formatoSalida,Map<?, ?> parametros){
		
		if(rutaReporte==null){
			throw new RuntimeException("La ruta del reporte es requerida");
		}
		
		if(datosEndpoint==null){
			throw new RuntimeException("Son requeridos los datos del endpoint");
		}
		
		if(formatoSalida==null){
			formatoSalida="pdf";
		}
		
		try {
			prepararCredenciales(datosEndpoint);
			
			ReportExecutionServiceStub reportExecutionServiceStub = new ReportExecutionServiceStub(datosEndpoint.getUrlEndpoint());
			
			aplicarOpciones(reportExecutionServiceStub._getServiceClient());
			
			LoadReport loadReport = new LoadReport();
			
			loadReport.setReport(rutaReporte);
			
			LoadReportResponse responseLoad = reportExecutionServiceStub.loadReport(loadReport, null);
			
			//Pongo el identificador de la sesion
			ExecutionHeaderE executionHeaderE = new ExecutionHeaderE();
			
			ExecutionHeader executionHeader = new ExecutionHeader();
			
			executionHeader.setExecutionID(responseLoad.getExecutionInfo().getExecutionID());
			
			executionHeaderE.setExecutionHeader(executionHeader);
			
			//Pongo los parametros
			SetExecutionParameters executionParameters = new SetExecutionParameters();
			
			ArrayOfParameterValue parametrosReporte = ParameterConverter.convertirParametros(parametros);
			
			executionParameters.setParameters(parametrosReporte);
			
			reportExecutionServiceStub.setExecutionParameters(executionParameters, executionHeaderE, null);
			
			Render render = new Render();
			
			render.setFormat(formatoSalida);
			
			RenderResponse responseRender = reportExecutionServiceStub.render(render, executionHeaderE, null);
			
			InputStream flujo = responseRender.getResult().getInputStream();
			
			byte[] reporteBytes = IOUtils.toByteArray(flujo);
			
			flujo.close();
			
			String reporteBase64 = Base64.encodeBase64String(reporteBytes);
			
			Reporte reporte = new Reporte();
			
			reporte.setTipoMime(responseRender.getMimeType());
			
			reporte.setReporteBase64(reporteBase64);
			
			return reporte;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Error al obtener el reporte",e);
			/*
			if(e instanceof OMBuilderException){
				OMBuilderException excepcionRS = (OMBuilderException) e;
				throw new RuntimeException("Error al obtener el reporte", excepcionRS);
			}else{
				throw new RuntimeException("Error al obtener el reporte", e);
			}*/
			
		}
	}

	private void aplicarOpciones(ServiceClient serviceClient) {
		// TODO Auto-generated method stub
		Options options = serviceClient.getOptions();
		//Muy importante decirle que esta es la version de soap de los servicios para los errores
		options.setSoapVersionURI(Constants.URI_SOAP11_ENV);
		
		//options.setTimeOutInMilliSeconds(2000);
		
		options.setProperty(HTTPConstants.SO_TIMEOUT, 3600000);
		
		options.setProperty(HTTPConstants.CONNECTION_TIMEOUT, 3600000);
		
		/*HttpState httpState = new HttpState();
		
		Cookie cookie = new Cookie();
		
		cookie.setName("SessionID");
		
		cookie.setValue("45");
		
		httpState.addCookie(cookie);
		
		options.setProperty(HTTPConstants.CACHED_HTTP_STATE, httpState);*/
	}

	/**
	 * http://robaustin.wikidot.com/axis
	 * http://devsac.blogspot.mx/2010/10/supoprt-for-ntlmv2-with-apache.html
	 * */
	private void prepararCredenciales(DatosEndpoint datosEndpoint) throws UnknownHostException {
		// TODO Auto-generated method stub
		String host = "";
		
		try {
			host = InetAddress.getLocalHost().getHostName();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final NTCredentials credenciales = new NTCredentials(datosEndpoint.getUsuario(), 
				datosEndpoint.getPass(), host, datosEndpoint.getDominio());
		
		CredentialsProvider proveedorCredenciales = new CredentialsProvider() {

			public Credentials getCredentials(AuthScheme scheme, String host, int port, boolean proxy)
					throws CredentialsNotAvailableException {
				// TODO Auto-generated method stub
				return credenciales;
			}
		};
		
		DefaultHttpParams.getDefaultParams().setParameter("http.authentication.credential-provider", proveedorCredenciales);
		AuthPolicy.registerAuthScheme(AuthPolicy.NTLM, JCIFS_NTLMScheme.class);
	}
	
	public static void main(String[] args) throws Exception{
		ServicioReportes servicioReportes = new ServicioReportes();
		
		HashMap<Object, Object> parametros = new HashMap<Object,Object>();
		
		DatosReporte datosReporte = new DatosReporte();//Llenar los datos
		
		DatosEndpoint datosEndpoint = new DatosEndpoint();
		
		datosEndpoint.setUsuario("");
		
		datosEndpoint.setPass("");
		
		datosEndpoint.setDominio("");
		
		datosEndpoint.setUrlEndpoint("");
		
		datosReporte.setDatosEndpoint(datosEndpoint);
		
		datosReporte.setRutaReporte("");
		
		datosReporte.setFormatoSalida("");
		
		datosReporte.setParametros(parametros);
		
		Reporte reporte = servicioReportes.obtenerReporte(datosReporte);
		
		File file = new File(System.getProperty("user.home")+"/reportingservice.pdf");
		if(file.exists()){
			file.delete();
		}
		file.createNewFile();
		System.out.println("Guardando archivo en: "+file.getAbsolutePath());
		OutputStream flujo = new FileOutputStream(file);
		flujo.write(Base64.decodeBase64(reporte.getReporteBase64()));
		flujo.close();
	}
}
