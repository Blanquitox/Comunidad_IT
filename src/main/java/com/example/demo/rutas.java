package com.example.demo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import model.Contacto;
import model.Formulario;

@Controller
public class rutas {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@GetMapping("/")
	public static String index() {
		return "index";
	}

	@GetMapping("/home")
	public static String home() {
		return "home";
	}
	
	@GetMapping("/home2")
	public static String home2() {
		return "home2";
	}

	@GetMapping("/nosotros")
	public static String About() {
		return "nosotros";
	}

	@GetMapping("/servicios")
	public static String Services() {
		return "servicios";
	}

	@GetMapping("/contacto")
	public static String Contact() {
		return "contacto";
	}

	@GetMapping("/mensajeError")
	public static String mensajeError() {
		return "mensajeError";
	}

	@PostMapping("/contacto")
	public static String procesarInfoContacto() {
		return "contacto";
	}

	@PostMapping("/formularioDeRegistro")
	public static String procesarInfoUsuario() {
		return "formularioDeRegistro";
	}

	@GetMapping("/lista-contacto")
	public static String listaContacto(Model template, String nombre, String email, String tema, String mensaje)
			throws SQLException {
		Connection connection;
		connection = DriverManager.getConnection(setting.db_url, setting.db_user, setting.db_password);

		PreparedStatement ws = connection.prepareStatement("SELECT * FROM listaContacto;");
		ws.setString(1, nombre);
		ws.setString(2, email);
		ws.setString(3, tema);
		ws.setString(4, mensaje);

		ResultSet resultado = ws.executeQuery();

		ArrayList<Contacto> listacontacto;
		listacontacto = new ArrayList<Contacto>();

		// template.addAttribute("listaContacto", resultado.getString("nombre"));
		while (resultado.next()) {
			Contacto miContacto = new Contacto(resultado.getInt("id"), resultado.getString("nombre"),
					resultado.getString("email"), resultado.getString("tema"), resultado.getString("mensaje"));

			listacontacto.add(miContacto);
		}

		template.addAttribute("listaContacto", listacontacto);
		return "listaContacto";

	}

	@PostMapping("/recibirContacto")
	public static String procesarInfoContacto(@RequestParam String nombre, @RequestParam String tema,
			@RequestParam String email, @RequestParam String mensaje, Model template) throws SQLException {
		if (nombre.equals("") || tema.equals("") || email.equals("") || mensaje.equals("")) {// si hubo algun error
			// cargar formulario de vuelta
			template.addAttribute("mensajeError", "No puede haber campos vacios");
			template.addAttribute("nombreAnterior", nombre);
			template.addAttribute("comentarioAnterior", tema);
			template.addAttribute("mailAnterior", email);
			template.addAttribute("mailAnterior", mensaje);

			return "contacto";
		} else {

			enviarCorreo(email, "Su_Ju@hotmail.com.ar", "Mensaje de contacto de:" + nombre,
					"nombre:" + nombre + "  email:" + email + "  mensaje:" + mensaje);
			enviarCorreo("Su_Ju@hotmail.com.ar", email, " Gracias por contactarte!",
					" Hemos recibido tu mail en brebe seras respondido");

			Connection connection;
			connection = DriverManager.getConnection(setting.db_url, setting.db_user, setting.db_password);
			PreparedStatement ss = connection
					.prepareStatement("INSERT INTO comentarios(nombre,email,tema,mensaje)  VALUES(?,?,?,?);");
			ss.setString(1, nombre);
			ss.setString(2, email);
			ss.setString(3, tema);
			ss.setString(4, mensaje);
			ss.executeUpdate();

			return "graciasContacto";
		}
	}

	public static void enviarCorreo(String de, String para, String tema, String mensaje) {
		Email from = new Email(de);
		String subject = tema;
		Email to = new Email(para);
		Content content = new Content("text/plain", mensaje);
		Mail mail = new Mail(from, subject, to, content);

		SendGrid sg = new SendGrid("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");
		Request request = new Request();
		try {
			request.method = Method.POST;
			request.endpoint = "mail/send";
			request.body = mail.build();
			Response response = sg.api(request);
			System.out.println(response.statusCode);
			System.out.println(response.body);
			System.out.println(response.headers);
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			;
		}
	}

	@GetMapping("/components")
	public static String Components() {
		return "components";
	}

	@GetMapping("/sabores-y-rellenos")
	public static String SaboresyRellenos() {
		return "sabores-y-rellenos";
	}

	@GetMapping("/accesorios")
	public static String accesorios() {
		return "accesorios";
	}

	@GetMapping("/decoracion")
	public static String decoracion() {
		return "decoracion";
	}

	@GetMapping("/registrarse")
	public static String registro() {
		return "login";
	}

	@GetMapping("/admin/edit")
	public static String paginaAdmin(HttpServletRequest request) {
		if (controlarMarquita(request)) {
			return "admin";
		} else {
			return "redirect:/admin/login";
		}
	}

	@GetMapping("/admin/login")
	public static String adminLogin(HttpServletRequest request) {
		return "adminLogin";
	}

	public static boolean controlarMarquita(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String marquita = (String) session.getAttribute("marquita");
		if (marquita != null && marquita.equals("AUTORIZADO")) {
			return true;
		} else {
			return false;
		}

	}

	@GetMapping("/admin/logout")
	public static String adminLogout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String marquita = (String) session.getAttribute("marquita");
		if (marquita != null && marquita.equals("AUTORIZADO")) {

		}
		return "home";
	}

	@PostMapping("/procesarLogin")
	public static String procesarlogin(@RequestParam String Usuario, @RequestParam String Password,
			HttpServletRequest request) {
		if (Usuario.equals("admin") && Password.equals("comunidad_it")) {
			// ACEPTADO
			HttpSession session = request.getSession();
			session.setAttribute("autorizado", "AUTORIZADO");

			return "redirect:/admin/edit";
		} else {
			// RECHAZADO
			// TODO: agregar mensaje de error: usuario y/o contrasenia invalida.
			return "adminLogin";

		}

	}

	@GetMapping("/formularioDeRegistro")
	public static String usuario(HttpServletRequest request) {
		return "formularioDeRegistro";
	}

	@GetMapping("/lista-usuarios")
	public static String listaUsuario(Model template, String usuario, String email, String password)
			throws SQLException {
		Connection connection;
		connection = DriverManager.getConnection(setting.db_url, setting.db_user, setting.db_password);

		PreparedStatement ps = connection.prepareStatement("SELECT * FROM listaUsuario;");
		ps.setString(1, usuario);
		ps.setString(2, password);
		ps.setString(3, email);

		ResultSet resultado = ps.executeQuery();

		ArrayList<Formulario> listausuario;
		listausuario = new ArrayList<Formulario>();

		// template.addAttribute("listaUsuario", resultado.getString("Usuario"));
		while (resultado.next()) {
			Formulario miUsuario = new Formulario(resultado.getInt("id"), resultado.getString("usuario"),
					resultado.getString("email"), resultado.getString("password"));

			listausuario.add(miUsuario);
		}

		template.addAttribute("listaUsuario", listausuario);
		return "listaUsuario";
	}

	@PostMapping("/recibirusuario")
	public static String procesarInfoUsuario(@RequestParam String usuario, @RequestParam String password,
			@RequestParam String email, Model template) throws SQLException {
		if (usuario.equals("") || password.equals("") || email.equals("")) {// si hubo algun error
			// cargar formulario de vuelta
			template.addAttribute("mensajeError", "No puede haber campos vacios");
			template.addAttribute("usuarioAnterior", usuario);
			template.addAttribute("passwordAnterior", password);
			template.addAttribute("emailAnterior", email);

			return "home2";
		} else {

			enviarCorreo(email, "Su_Ju@hotmail.com.ar", "Mensaje de contacto de:" + usuario,
					"usuario:" + usuario + "  email:" + email);
			enviarCorreo("Su_Ju@hotmail.com.ar", email, " Gracias por contactarte!", " GRACIAS POR REGISTRARTE");

			Connection connection;
			connection = DriverManager.getConnection(setting.db_url, setting.db_user, setting.db_password);
			PreparedStatement qs = connection
					.prepareStatement("INSERT INTO usuarios(usuario,email,password)  VALUES(?,?,?);");
			qs.setString(1, usuario);
			qs.setString(2, email);
			qs.setString(3, password);

			qs.executeUpdate();

			return "graciasPorElRegistro";
		}
	}

}
