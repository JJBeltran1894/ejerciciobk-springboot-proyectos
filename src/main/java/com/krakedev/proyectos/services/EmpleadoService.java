package com.krakedev.proyectos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Empleado;

import com.krakedev.proyectos.repositories.EmpleadoRepository;

@Service
public class EmpleadoService {

	
private EmpleadoRepository repositorio;
	
	public EmpleadoService(EmpleadoRepository repositorio) {
		this.repositorio = repositorio;
	}
	
	public List<Empleado> listarTodos(){
		return repositorio.findAll();
	}
	
	public Empleado insertar(Empleado empleado) {
		return repositorio.save(empleado);
	}
	
	public Optional<Empleado> buscarPorId(int id) {
		return repositorio.findById(id);
	}
	
	public Empleado actualizar(int id, Empleado empleado) {
        Empleado empleadoExistente = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el empleado con id: " + id));
 
        empleadoExistente.setNombre(empleado.getNombre());
        empleadoExistente.setCargo(empleado.getCargo());
        empleadoExistente.setTareas(empleado.getTareas());
     
        return repositorio.save(empleadoExistente);
	}
	
	public boolean eliminar(int id) {
		if (repositorio.existsById(id)) {
			repositorio.deleteById(id);
            return true;
        }
        return false;
	}
}
