package com.krakedev.proyectos.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.security.JwtUtil;
import com.krakedev.proyectos.services.TokenBlacklistService;
import com.krakedev.proyectos.services.UsuarioService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final UsuarioService usuarioService;
	private final TokenBlacklistService blacklistService;

	public AuthController(UsuarioService usuarioService, TokenBlacklistService blacklistService) {
		this.usuarioService = usuarioService;
		this.blacklistService = blacklistService;
	}
	
	@PostMapping("/registrar")
	public ResponseEntity<?> registrar(@RequestBody Usuario usuario){
		try {
			Usuario nuevoUsuario = usuarioService.registrar(usuario);
			return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar usuario: " + e.getMessage());
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String,String> credenciales){
		try {
			String username = credenciales.get("username");
			String password = credenciales.get("password");
			
			Usuario usuarioAuth = usuarioService.login(username, password);
			
			String token = JwtUtil.generarToken(usuarioAuth.getUsername(), usuarioAuth.getRol());
			
			return ResponseEntity.ok(Map.of("token", token));
			
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrecta");
		}
	}
	
}
