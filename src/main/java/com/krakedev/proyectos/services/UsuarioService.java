package com.krakedev.proyectos.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.krakedev.proyectos.repositories.UsuarioRepository;
import com.krakedev.proyectos.entidades.Usuario;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepositorio;
	
	public UsuarioService(UsuarioRepository usuarioRepositorio) {
		
		this.usuarioRepositorio = usuarioRepositorio;
	}
	
	public Usuario registrar(Usuario usuario) {
		Optional<Usuario> existeUsuario = usuarioRepositorio.findByUsername(usuario.getUsername());
        if (existeUsuario.isPresent()) {
            throw new IllegalArgumentException("El username '" + usuario.getUsername() + "' ya está en uso.");
        }
		String passwordEncriptada = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());
		usuario.setPassword(passwordEncriptada);
		return usuarioRepositorio.save(usuario);
	}
	
	public Usuario login(String username, String password) {
		Optional<Usuario> usuario = usuarioRepositorio.findByUsername(username);
		
		if(usuario.isPresent()) {
			Usuario u = usuario.get();
			Boolean passValida = BCrypt.checkpw(password,u.getPassword());
			if(passValida) {
				return u;
			}
		}
		return null;
	}
}
