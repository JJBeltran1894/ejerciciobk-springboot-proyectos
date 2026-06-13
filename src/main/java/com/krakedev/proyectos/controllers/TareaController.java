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

import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.services.TareaService;


@RestController
@RequestMapping("/api/tareas")
public class TareaController {
	
	@Autowired
	private TareaService tService;
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> insertar(@RequestBody Tarea tarea) {
        try {
        	Tarea creado = tService.insertar(tarea);
			return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la Tarea");
        }
	}
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> listarTodos() {
		try {
			List<Tarea> Tareas =tService.listarTodos();
			return ResponseEntity.ok(Tareas);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener la lista de Tareas");
		}
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
		try {
			Optional<Tarea> tarea =tService.buscarPorId(id);
			if (tarea.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarea con id " + id + " no encontrada");
			}else {
				return ResponseEntity.ok(tarea);
			}
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar la Tarea");
		}
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Tarea tarea) {
		try {
			Tarea actualizado = tService.actualizar(id, tarea);
			if (actualizado ==null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarea " + id + " no existe");
			} else {
				return ResponseEntity.ok(actualizado);
			}
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar tarea: " + id);
		}
	}
	
	@DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
		try {
			boolean eliminado = tService.eliminar(id);
			if (!eliminado) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarea con id " + id + " no existe");
			}
			return ResponseEntity.ok("Tarea con id: " + id + " eliminado");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar tarea: " + id);
		}
	}

}
