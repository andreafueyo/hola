package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Persona;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios.PersonaRepository;

@Service
public class ServiciosPersona {
	@Autowired
	PersonaRepository personarepo;
	
	public boolean validarPersona(Persona p) { 
		return personarepo.validarPersona(p);
	}
	
	public Persona insertar(Persona p) {
		return personarepo.saveAndFlush(p);
	}

	public Persona findByEmail(String email) {
		return personarepo.findByEmail(email);
	}
	
	public Persona findById(Long id) {
		return personarepo.findByPersonaId(id);
	}

	public Persona registrarPersona(String nombre,String email) {
		Persona p = new Persona();
		p.setNombre(nombre);
		p.setEmail(email);
		return this.insertar(p);
	}
	public List<Persona> findAll(){
		return personarepo.findAll();
	}
	
	public List<Persona> findAllExceptAdmin(){
		return personarepo.findAllExceptAdmin();
	}

}
