package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class usuariosController {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@GetMapping("/logout")
	public static String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String marquita = (String) session.getAttribute("marquita");
		if (marquita != null && marquita.equals("AUTORIZADO")) {

		}
		return "home";
	}

	@GetMapping("/login")
	public static String login(HttpServletRequest request) {
		return "login";
	}

	public static String controlarMarquita(HttpServletRequest request) throws SQLException {
		HttpSession session = request.getSession();
		String marquita = (String) session.getAttribute("marquita");	

		if (marquita != null) {

			Connection connection;
			connection = DriverManager.getConnection(setting.db_url, setting.db_user, setting.db_password);
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM usuarios WHERE marquita=?;");

			ps.setString(1, marquita);

			ResultSet resultado = ps.executeQuery();

			if (resultado.next()) {
				return resultado.getString("Username");
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@GetMapping("/verificarLogin")
	public static String procesarlogin(@RequestParam String Usuario, @RequestParam String Password,
			HttpServletRequest request) throws SQLException {

		Connection connection;
		connection = DriverManager.getConnection(setting.db_url, setting.db_user, setting.db_password);
		PreparedStatement as = connection.prepareStatement("SELECT * FROM usuarios WHERE usuario =? AND password=?;");

		as.setString(1, Usuario);
		as.setString(2, Password);

		ResultSet resultado = as.executeQuery();

		if (resultado.next()) {
			// ACEPTADO
			String marquitaNueva = UUID.randomUUID().toString();

			HttpSession session = request.getSession();
			session.setAttribute("autorizado", marquitaNueva);

			as = connection.prepareStatement("UPDATE usuarios SET marquita=? WHERE usuario=?");
			as.setString(1, marquitaNueva);
			as.setString(2, Usuario);
			as.executeUpdate();
			
			System.out.println("paso por aca");
			
			return "redirect:/servicios";
			
		} else {
			System.out.println("paso por else");

			return "mensajeError";

		}

	}
}
