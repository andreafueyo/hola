package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios.CredencialesRepository;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios.PersonaRepository;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Credenciales;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Persona;

@Service
public class ServiciosCredenciales {
	
	@Autowired
	CredencialesRepository credencialesrepo;
	@Autowired
	PersonaRepository personarepo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public boolean validarNuevaCredencial(Credenciales c) { //Si ya existe, devuelve false
		
		if(this.findByUsuario(c.getUsuario()) == null) {
			return true;
		}
		else {
			return false;
		}
		
	}	
	
	public boolean validarCredencialContraseña(Credenciales c) { //Si coincide, devuelve true
		if(this.findByUsuario(c.getUsuario()) == null) {
			return false;
		}
		else if(this.findByUsuario(c.getUsuario()).getPassword().equals(c.getPassword())){
			return true;
		}else {
			return false;
		}
		
		
	}
	
	public String validarTipoUsuario(Credenciales c) { 
		if((c.getUsuario()).equals("admin") && (c.getPassword()).equals("admin")) {
			return "admin";
		} else {
				return "personal";
		}
	}
	
	public Credenciales insertar(Credenciales c) {
        String passwordEncriptada = passwordEncoder.encode(c.getPassword());
        c.setPassword(passwordEncriptada);
		return credencialesrepo.saveAndFlush(c);
	}

	public Credenciales findByUsuario(String usuario) {
		return credencialesrepo.findByUsuario(usuario);
	}

	public List<Credenciales> findAll(){
		return credencialesrepo.findAll();
	}

	public Credenciales registrarCredencial(String usuario, String password, String email) {

		
		Persona persona = personarepo.findByEmail(email);
		if (persona == null) {
			return null; 
		}
		
		Credenciales c = new Credenciales();
		c.setUsuario(usuario);
		c.setPassword(password);
		c.setPersona(persona);
		
		return this.insertar(c);
	}
	
    public void registrarUsuario(Credenciales credenciales) {
        credencialesrepo.save(credenciales);
    }
}
