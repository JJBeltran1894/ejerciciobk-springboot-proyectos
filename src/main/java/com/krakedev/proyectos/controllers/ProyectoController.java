package com.krakedev.proyectos.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.services.ProyectoService;


@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {
	
	@Autowired
	private ProyectoService pService;
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> insertar(@RequestBody Proyecto proyecto) {
        try {
        	Proyecto creado = pService.insertar(proyecto);
			return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el Proyecto");
        }
	}
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> listarTodos() {
		try {
			List<Proyecto> proyectos =pService.listarTodos();
			return ResponseEntity.ok(proyectos);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener la lista de Proyectos");
		}
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
		try {
			Optional<Proyecto> proyecto =pService.buscarPorId(id);
			if (proyecto.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proyecto con id " + id + " no encontrado");
			}else {
				return ResponseEntity.ok(proyecto);
			}
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar el Proyecto");
		}
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Proyecto proyecto) {
		try {
			Proyecto actualizado = pService.actualizar(id, proyecto);
			if (actualizado ==null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proyecto " + id + " no existe");
			} else {
				return ResponseEntity.ok(actualizado);
			}
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar Proyecto: " + id);
		}
	}
	
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
		try {
			boolean eliminado = pService.eliminar(id);
			if (!eliminado) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proyecto con id " + id + " no existe");
			}
			return ResponseEntity.ok("Proyecto con id: " + id + " eliminado");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar Proyecto: " + id);
		}
	}
	
}
