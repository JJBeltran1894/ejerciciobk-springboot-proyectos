package com.krakedev.proyectos.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;
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
	
	@GetMapping("/perfil")
	public ResponseEntity<?> perfil(@RequestHeader(value="Authorization", required = false) String authHeader){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			String usuario = auth.getName();
			String rol = auth.getAuthorities().iterator().next().getAuthority();
			
			if ("ADMIN".equals(rol)) {
				return ResponseEntity.ok(Map.of(
						"Mensaje", "Bienvenido al sistema del refugio de mascotas - Eres Administrador", 
						"Usuario", usuario,
						"Rol", rol,
						"Estatus", "Autenticado Existosamente"
						));	
			}else if("USER".equals(rol)){
				return ResponseEntity.ok(Map.of(
						"Mensaje", "Bienvenido al sistema del refugio de mascotas - Eres Usuario con permisos basicos", 
						"Usuario", usuario,
						"Rol", rol,
						"Estatus", "Autenticado Existosamente"
						));	
			}else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Rol no autorizado a acceder a este modulo");
			}

	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader(value="Authorization", required = false) String authHeader){
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			DecodedJWT datosToken = JwtUtil.validarToken(token);
			if(datosToken == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalido o expirado");
			}
			blacklistService.invalidarToken(token);
			return ResponseEntity.ok(Map.of("Mensaje", "Sesion cerrada exitosamete: Token invalidado"));
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token no proporcionado");
		}
		
	}
	
}
