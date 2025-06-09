package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios.ClienteRepository;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios.ProductoRepository;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios.CitaRepository;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios.PersonaRepository;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Cita;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Persona;


@Service
public class ServiciosCita {
	
	@Autowired
	CitaRepository citarepo;
	@Autowired
	PersonaRepository personarepo;
	@Autowired
	ProductoRepository productorepo;
	@Autowired
	ClienteRepository clienterepo;	

	public Cita insertar(Cita c) {
		return citarepo.saveAndFlush(c);
	}
	
	public List<Persona> obtenerPersonasDisponibles(LocalDateTime inicio, int duracionMinutos) {
	    LocalDateTime fin = inicio.plusMinutes(duracionMinutos);
	    List<Persona> lPersonas = personarepo.findAllExceptAdmin();

	    return lPersonas.stream()
	        .filter(p -> p.getCitas().stream().noneMatch(c -> {
	            LocalDateTime citaInicio = c.getFechahora();
	            LocalDateTime citaFin = citaInicio.plusMinutes(c.getServicio().getDuracion());
	            return inicio.isBefore(citaFin) && fin.isAfter(citaInicio);
	        }))
	        .toList();
	}
	
	public List<Cita> obtenerCitasCoincidanPersona(LocalDateTime fechaConsulta, int duracionMinutos, Long idPersona) {
	    List<Cita> citas = citarepo.findByPersonaId(idPersona);

	    LocalDateTime nuevaFin = fechaConsulta.plusMinutes(duracionMinutos);

	    return citas.stream()
	        .filter(c -> {
	            LocalDateTime existenteInicio = c.getFechahora();
	            LocalDateTime existenteFin = existenteInicio.plusMinutes(c.getServicio().getDuracion());

	            return fechaConsulta.isBefore(existenteFin) && nuevaFin.isAfter(existenteInicio);
	        })
	        .collect(Collectors.toList());
	}
	
	public List<Cita> obtenerCitasCoincidanCliente(LocalDateTime fechaConsulta, int duracionMinutos, Long idCliente) {
	    List<Cita> citas = citarepo.findByClienteId(idCliente);

	    LocalDateTime nuevaFin = fechaConsulta.plusMinutes(duracionMinutos);

	    return citas.stream()
	        .filter(c -> {
	            LocalDateTime existenteInicio = c.getFechahora();
	            LocalDateTime existenteFin = existenteInicio.plusMinutes(c.getServicio().getDuracion());

	            return fechaConsulta.isBefore(existenteFin) && nuevaFin.isAfter(existenteInicio);
	        })
	        .collect(Collectors.toList());
	}


	public List<Cita> findAll() { 
		return citarepo.findAll();
	}
	
	public List<Cita> findByFechahoraBetween(LocalDateTime atStartOfDay, LocalDateTime atStartOfDay2) {
		return citarepo.findByFechahoraBetween(atStartOfDay, atStartOfDay2);
	}

	public List<Cita> findByFechahoraBetweenPersona(LocalDateTime atStartOfDay, LocalDateTime atStartOfDay2, Long idPersona) {
		return citarepo.findByPersonaIdAndFechahoraBetween(idPersona, atStartOfDay, atStartOfDay2);
	}

	public Cita findById(Long idCita) {
		return citarepo.findByCitaId(idCita);
	}

	public List<Cita> findAllByPersonaId(Long idPersona) {
		return citarepo.findByPersonaId(idPersona);
	}

	public void borrar(Cita cita) {
		citarepo.delete(cita);
	}

	public List<Cita> findByNombrePersona(String persona) {
		return citarepo.findByNombrePersona(persona);
	}
	
	public List<Cita> findByCodigoServicio(String servicio) {
		return citarepo.findByCodigoServicio(servicio);
	}

	public List<Cita> findByFechahoraBetweenCliente(LocalDateTime atStartOfDay, LocalDateTime atStartOfDay2, Long idCliente) {
		return citarepo.findByClienteIdAndFechahoraBetween(idCliente, atStartOfDay, atStartOfDay2);
	}
}
