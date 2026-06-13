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

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.services.EmpleadoService;





@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
	
	@Autowired
	private EmpleadoService eService;
	
	@PostMapping
	public ResponseEntity<?> insertar(@RequestBody Empleado empleado) {
        try {
        	Empleado creado = eService.insertar(empleado);
			return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el empleado");
        }
	}
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> listarTodos() {
		try {
			List<Empleado> empleados =eService.listarTodos();
			return ResponseEntity.ok(empleados);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener la lista de empleados");
		}
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
		try {
			Optional<Empleado> empleado =eService.buscarPorId(id);
			if (empleado.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empleado con id " + id + " no encontrado");
			}else {
				return ResponseEntity.ok(empleado);
			}
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar el empleado");
		}
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Empleado empleado) {
		try {
			Empleado actualizado = eService.actualizar(id, empleado);
			if (actualizado ==null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empleado " + id + " no existe");
			} else {
				return ResponseEntity.ok(actualizado);
			}
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar empleado: " + id);
		}
	}
	
	@DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
		try {
			boolean eliminado = eService.eliminar(id);
			if (!eliminado) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empleado con id " + id + " no existe");
			}
			return ResponseEntity.ok("Empleado con id: " + id + " eliminado");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar Empleado: " + id);
		}
	}

	
}
