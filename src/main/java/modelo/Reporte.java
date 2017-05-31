package modelo;

import java.io.Serializable;

public class Reporte implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String tipoMime;
	private String reporteBase64;

	public String getTipoMime() {
		return tipoMime;
	}

	public void setTipoMime(String tipoMime) {
		this.tipoMime = tipoMime;
	}

	public String getReporteBase64() {
		return reporteBase64;
	}

	public void setReporteBase64(String reporteBase64) {
		this.reporteBase64 = reporteBase64;
	}

	@Override
	public String toString() {
		return "Reporte [tipoMime=" + tipoMime + ", reporteBase64=" + reporteBase64 + "]";
	}

}
