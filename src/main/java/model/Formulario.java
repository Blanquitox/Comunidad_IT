package model;

public class Formulario {
	int id;
	String usuario;
	String password;
	String email;
	

	public Formulario (int id, String u, String p, String e) {
		this.id= id;
		this.usuario= u;
		this.email= e;
		this.password= p;
		
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getUsuario() {
		return usuario;
	}


	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
}