package modelo;

import java.io.Serializable;
import java.util.Map;

public class DatosReporte implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DatosEndpoint datosEndpoint;
	private String rutaReporte;
	private String formatoSalida;
	private Map<?, ?> parametros;

	public DatosEndpoint getDatosEndpoint() {
		return datosEndpoint;
	}

	public void setDatosEndpoint(DatosEndpoint datosEndpoint) {
		this.datosEndpoint = datosEndpoint;
	}

	public String getRutaReporte() {
		return rutaReporte;
	}

	public void setRutaReporte(String rutaReporte) {
		this.rutaReporte = rutaReporte;
	}

	public String getFormatoSalida() {
		return formatoSalida;
	}

	public void setFormatoSalida(String formatoSalida) {
		this.formatoSalida = formatoSalida;
	}

	public Map<?, ?> getParametros() {
		return parametros;
	}

	public void setParametros(Map<?, ?> parametros) {
		this.parametros = parametros;
	}

	@Override
	public String toString() {
		return "DatosReporte [datosEndpoint=" + datosEndpoint + ", rutaReporte=" + rutaReporte + ", formatoSalida="
				+ formatoSalida + ", parametros=" + parametros + "]";
	}

}
