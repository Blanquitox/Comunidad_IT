package model;

public class Comentarios {
	int id;
	String nombre;
	String tema;
	String email;
	String mensaje;
	
	public Comentarios (int id, String n, String t, String e, String m) {
		this.id= id;
		this.nombre= n;
		this.tema= t;
		this.email= e;
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

	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}