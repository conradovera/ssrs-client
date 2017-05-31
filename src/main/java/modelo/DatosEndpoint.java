package modelo;

import java.io.Serializable;

public class DatosEndpoint implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String urlEndpoint;
	private String usuario;
	private String pass;
	private String dominio;

	public String getUrlEndpoint() {
		return urlEndpoint;
	}

	public void setUrlEndpoint(String urlEndpoint) {
		this.urlEndpoint = urlEndpoint;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getDominio() {
		return dominio;
	}

	public void setDominio(String dominio) {
		this.dominio = dominio;
	}

	@Override
	public String toString() {
		return "DatosEndpoint [urlEndpoint=" + urlEndpoint + ", usuario=" + usuario + ", pass=" + pass + ", dominio="
				+ dominio + "]";
	}

}
