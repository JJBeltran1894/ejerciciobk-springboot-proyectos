package com.krakedev.proyectos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.repositories.ProyectoRepository;


@Service
public class ProyectoService {
	
	private ProyectoRepository repositorio;
	
	public ProyectoService(ProyectoRepository repositorio) {
		this.repositorio = repositorio;
	}
	
	public List<Proyecto> listarTodos(){
		return repositorio.findAll();
	}
	
	public Proyecto insertar(Proyecto proyecto) {
		return repositorio.save(proyecto);
	}
	
	public Optional<Proyecto> buscarPorId(Integer id) {
		return repositorio.findById(id);
	}
	
	public Proyecto actualizar(int id, Proyecto proyecto) {
        Proyecto proyectoExistente = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el proyecto con id: " + id));
 
        proyectoExistente.setNombre(proyecto.getNombre());
        proyectoExistente.setDescripcion(proyecto.getDescripcion());
        proyectoExistente.setFechaInicio(proyecto.getFechaInicio());
 
        return repositorio.save(proyectoExistente);
    }

	
	public boolean eliminar(int id) {
		if (repositorio.existsById(id)) {
			repositorio.deleteById(id);
            return true;
        }
        return false;
	}
	
	
	
	

}
