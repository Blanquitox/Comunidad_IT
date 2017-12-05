package model;

public class Contacto {
	int id;
	String nombre;
	String mensaje;
	String email;
	String tema;
	

	public Contacto (int id, String n, String e, String t, String m) {
		this.id= id;
		this.nombre= n;
		this.email= e;
		this.tema= t;
		this.mensaje= m;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getMensaje() {
		return mensaje;
	}


	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getTema() {
		return tema;
	}


	public void setTema(String tema) {
		this.tema = tema;
	}

}