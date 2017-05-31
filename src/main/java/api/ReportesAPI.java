package api;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import consumidor.ServicioReportes;
import modelo.DatosReporte;
import modelo.Reporte;

@Path("/reporte")
public class ReportesAPI {

	@Inject
	private ServicioReportes servicioReportes;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response consumirReporte(DatosReporte datosReporte){

		
		try{
			Reporte reporte = servicioReportes.obtenerReporte(datosReporte);
			return Response.ok(reporte).build();
		}catch (RuntimeException ex) {
			// TODO: handle exception
			ex.printStackTrace();
			return Response.serverError().entity("{\"mensaje\":\"Reporte inaccesible. Verifique la informacion enviada y de persistir favor de contactar al administrador\"}").build();
		}
		
	}
	
}
