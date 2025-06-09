package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Cliente;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios.ClienteRepository;

@Service
public class ServiciosCliente {
	@Autowired
	ClienteRepository clienterepo;
	
	public boolean validarCliente(Cliente p) { 
		return clienterepo.validarCliente(p);
	}
	
	public Cliente insertar(Cliente p) {
		return clienterepo.saveAndFlush(p);
	}

	public Cliente findByEmail(String email) {
		return clienterepo.findByEmail(email);
	}
	
	public Cliente findById(Long id) {
		return clienterepo.findByClienteId(id);
	}
	
	public Cliente findByNIF(String NIF) {
		return clienterepo.findByNIF(NIF);
	}
	
	public List<Cliente> findAll(){
		return clienterepo.findAll();
	}

	public Cliente registrarCliente(String nombre,LocalDate fechaNac, String NIF,String direccion, String email, String telefono) {
		Cliente cl = new Cliente();
		cl.setNombre(nombre);
		cl.setFecha_nac(fechaNac);
		cl.setNIF(NIF);
		cl.setDireccion(direccion);
		cl.setEmail(email);
		cl.setTelefono(telefono);
		return this.insertar(cl);
	}

	public Cliente findByTelefono(String telCliente) {
		return clienterepo.findByTelefono(telCliente);
	}

}
