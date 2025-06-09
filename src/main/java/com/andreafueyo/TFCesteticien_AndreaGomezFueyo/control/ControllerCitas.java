package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.control;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.CustomUserDetailsServiceImpl;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Cita;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Cliente;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Persona;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Servicio;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios.ServiciosCita;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios.ServiciosCliente;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios.ServiciosPersona;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios.ServiciosServicio;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ControllerCitas {

	@Autowired
	private ServiciosCita servCita;

	@Autowired
	private ServiciosCliente servCliente;

	@Autowired
	private ServiciosPersona servPersona;

	@Autowired
	private ServiciosServicio servServicio;

	@Autowired
	private MainController maincontroller;

	@Autowired
	private CustomUserDetailsServiceImpl userDetails;

	/*Gestión citas*/
	@GetMapping("/gestioncitas")
	public String gestionCitasMensajes(Model model) {
		model.addAttribute("origen", maincontroller.getMenuLogin());   
		return "gestioncitas"; 
	}

	/*Registrar cita*/
	@GetMapping("/registrarcita")
	public String mostrarListaCitasCliente(Model model) {
		model.addAttribute("servicios", servServicio.findAll());
		model.addAttribute("origen", maincontroller.getMenuLogin());
		return "registrarcita"; 
	}

	@PostMapping("/registrarcita")
	public String registrarCitaCliente(@RequestParam Long servicioId,
			@RequestParam String fechahora,
			@RequestParam(value = "origen", required = false, defaultValue = "menuadmin") 
	String origen,
	Model model) {

		LocalDateTime fechaHoraParsed = LocalDateTime.parse(fechahora);
		Servicio servicio = servServicio.findById(servicioId);

		int duracionMinutos = servicio.getDuracion();

		LocalDateTime finCita = fechaHoraParsed.plusMinutes(duracionMinutos);

		boolean horarioValido = false;
		DayOfWeek diaSemana = fechaHoraParsed.getDayOfWeek();

		LocalDateTime apertura;
		LocalDateTime cierre;

		if (fechaHoraParsed.isBefore(LocalDateTime.now())) {
			model.addAttribute("error", "No puedes reservar una cita en el pasado.");
			model.addAttribute("servicios", servServicio.findAll());
			return "registrarcita";
		}

		if (diaSemana.getValue() >= 1 && diaSemana.getValue() <= 5) {
			// Lunes a viernes: 10:00 - 19:00
			apertura = fechaHoraParsed.toLocalDate().atTime(10, 0);
			cierre = fechaHoraParsed.toLocalDate().atTime(19, 0);
		} else if (diaSemana == DayOfWeek.SATURDAY) {
			// Sábado: 10:00 - 13:00
			apertura = fechaHoraParsed.toLocalDate().atTime(10, 0);
			cierre = fechaHoraParsed.toLocalDate().atTime(13, 0);
		} else {
			// Domingo: cerrado
			horarioValido = false;
			apertura = null;
			cierre = null;
		}

		if (apertura != null && cierre != null) {
			horarioValido = !fechaHoraParsed.isBefore(apertura) && !finCita.isAfter(cierre);
		}

		if (!horarioValido) {
			model.addAttribute("error", "La cita excede el horario permitido. Horario: L-V 10:00–19:00, Sáb 10:00–13:00.");
			model.addAttribute("servicios", servServicio.findAll());
			return "registrarcita";
		}
		Long idCliente = userDetails.getCredenciales().getCliente().getId();
		List<Persona> lPersonas = servCita.obtenerPersonasDisponibles(fechaHoraParsed, servicio.getDuracion());
		List<Cita> listaCitas = servCita.obtenerCitasCoincidanCliente(fechaHoraParsed, servicio.getDuracion(), idCliente);
		System.out.print(listaCitas.size());
		if (!lPersonas.isEmpty() && listaCitas.isEmpty()) {
			Random random = new Random();
			Persona personaAsignada = lPersonas.get(random.nextInt(lPersonas.size()));
			Cita nuevaCita = new Cita();
			nuevaCita.setCliente(servCliente.findById(idCliente));
			nuevaCita.setPersona(personaAsignada);
			nuevaCita.setFechahora(fechaHoraParsed);
			nuevaCita.setServicio(servServicio.findById(servicioId));

			servCita.insertar(nuevaCita);
			model.addAttribute("mensajeExito", "¡Cita creada correctamente!");
		} else if(!listaCitas.isEmpty()){
			model.addAttribute("origen", origen); 
			model.addAttribute("error", "Ya tienes una cita a esa hora");

		} else{
			model.addAttribute("origen", origen); 
			model.addAttribute("error", "No hay personas disponibles en ese horario. Intente otro horario");

		}
		return "registrarcita";

	}

	@GetMapping("/registrarcitapersonal")
	public String mostrarListaCitasPersonal(Model model) {
		model.addAttribute("servicios", servServicio.findAll());
		model.addAttribute("origen", maincontroller.getMenuLogin());
		return "registrarcitapersonal"; 
	}

	@PostMapping("/registrarcitapersonal")
	public String registrarCitaPersonal(@RequestParam Long servicioId,
			@RequestParam String fechahora,
			@RequestParam String telCliente,
			@RequestParam(value = "origen", required = false, defaultValue = "menuadmin") 
	String origen,
	Model model) {

		LocalDateTime fechaHoraParsed = LocalDateTime.parse(fechahora);
		Servicio servicio = servServicio.findById(servicioId);
		Cliente cliente = servCliente.findByTelefono(telCliente);

		Long idPersona = userDetails.getCredenciales().getPersona().getId();
		Persona persona = servPersona.findById(idPersona);

		if (fechaHoraParsed.isBefore(LocalDateTime.now())) {
			model.addAttribute("error", "No puedes reservar una cita en el pasado.");
			model.addAttribute("servicios", servServicio.findAll());
			return "registrarcitapersonal";
		}

		if(cliente == null) {
			model.addAttribute("error", "No hay clientes registrados con ese teléfono, introduce uno existente");
			return "registrarcitapersonal";
		}

		int duracionMinutos = servicio.getDuracion();

		LocalDateTime finCita = fechaHoraParsed.plusMinutes(duracionMinutos);

		boolean horarioValido = false;
		DayOfWeek diaSemana = fechaHoraParsed.getDayOfWeek();

		LocalDateTime apertura;
		LocalDateTime cierre;

		if (diaSemana.getValue() >= 1 && diaSemana.getValue() <= 5) {
			// Lunes a viernes: 10:00 - 19:00
			apertura = fechaHoraParsed.toLocalDate().atTime(10, 0);
			cierre = fechaHoraParsed.toLocalDate().atTime(19, 0);
		} else if (diaSemana == DayOfWeek.SATURDAY) {
			// Sábado: 10:00 - 13:00
			apertura = fechaHoraParsed.toLocalDate().atTime(10, 0);
			cierre = fechaHoraParsed.toLocalDate().atTime(13, 0);
		} else {
			// Domingo: cerrado
			horarioValido = false;
			apertura = null;
			cierre = null;
		}

		if (apertura != null && cierre != null) {
			horarioValido = !fechaHoraParsed.isBefore(apertura) && !finCita.isAfter(cierre);
		}

		if (!horarioValido) {
			model.addAttribute("error", "La cita excede el horario permitido. Horario: L-V 10:00–19:00, Sáb 10:00–13:00.");
			model.addAttribute("servicios", servServicio.findAll());
			return "registrarcitapersonal";
		}

		List<Cita > listaCitas = servCita.obtenerCitasCoincidanPersona(fechaHoraParsed, servicio.getDuracion(),idPersona);

		if (listaCitas.isEmpty()) {
			Cita nuevaCita = new Cita();
			nuevaCita.setCliente(cliente);
			nuevaCita.setPersona(persona);
			nuevaCita.setFechahora(fechaHoraParsed);
			nuevaCita.setServicio(servicio);
			servCita.insertar(nuevaCita);
			model.addAttribute("mensajeExito", "¡Cita creada correctamente!");
			model.addAttribute("servicios", servServicio.findAll());

		} else {
			model.addAttribute("origen", origen); 
			model.addAttribute("error", "Ya tienes una cita a esa hora. Escoge otra hora.");
			model.addAttribute("servicios", servServicio.findAll());
		}
		return "registrarcitapersonal";

	}

	/*Gestión citas*/
	@GetMapping("/vermiscitaspersonal")
	public String mostrarVerMisCitasPersonal(Model model) {
		model.addAttribute("origen", maincontroller.getMenuLogin());   
		return "vermiscitaspersonal"; 
	}
	@GetMapping("/filtrarcitasfechaspersonal")
	public String filtrarCitasFechasPersonal(@RequestParam String filtro, Model model) {
		List<Cita> citas;
		Long idPersona = userDetails.getCredenciales().getPersona().getId();
		LocalDateTime ahora = LocalDateTime.now();

		switch (filtro) {
		case "dia":
			LocalDate hoyInicio = ahora.toLocalDate();
			LocalDate hoyFin = hoyInicio.plusDays(1);
			citas = servCita.findByFechahoraBetweenPersona(hoyInicio.atStartOfDay(), hoyFin.atStartOfDay(), idPersona);
			break;
		case "semana":
			LocalDate lunes = ahora.toLocalDate().with(DayOfWeek.MONDAY);
			LocalDate domingo = lunes.plusDays(7);
			citas = servCita.findByFechahoraBetweenPersona(lunes.atStartOfDay(), domingo.atStartOfDay(), idPersona);
			break;
		case "mes":
			LocalDate mesInicio = ahora.toLocalDate().withDayOfMonth(1);
			LocalDate mesFin = mesInicio.plusMonths(1);
			citas = servCita.findByFechahoraBetweenPersona(mesInicio.atStartOfDay(), mesFin.atStartOfDay(), idPersona);
			break;
		default:
			citas = servCita.findAll();
		}

		if(citas.size()>0) {
			model.addAttribute("citas", citas);
		}
		else {
			model.addAttribute("citas", null);
		}
		model.addAttribute("filtroSeleccionado", filtro);
		return "vermiscitaspersonal";
	}
	
	/*Gestión citas*/
	@GetMapping("/vermiscitascliente")
	public String mostrarVerMisCitasCliente(Model model) {
		model.addAttribute("origen", maincontroller.getMenuLogin());   
		return "vermiscitascliente"; 
	}
	@GetMapping("/filtrarcitasfechascliente")
	public String filtrarCitasFechasCliente(@RequestParam String filtro, Model model) {
		List<Cita> citas;
		Long idCliente = userDetails.getCredenciales().getCliente().getId();
		LocalDateTime ahora = LocalDateTime.now();

		switch (filtro) {
		case "dia":
			LocalDate hoyInicio = ahora.toLocalDate();
			LocalDate hoyFin = hoyInicio.plusDays(1);
			citas = servCita.findByFechahoraBetweenCliente(hoyInicio.atStartOfDay(), hoyFin.atStartOfDay(), idCliente);
			break;
		case "semana":
			LocalDate lunes = ahora.toLocalDate().with(DayOfWeek.MONDAY);
			LocalDate domingo = lunes.plusDays(7);
			citas = servCita.findByFechahoraBetweenCliente(lunes.atStartOfDay(), domingo.atStartOfDay(), idCliente);
			break;
		case "mes":
			LocalDate mesInicio = ahora.toLocalDate().withDayOfMonth(1);
			LocalDate mesFin = mesInicio.plusMonths(1);
			citas = servCita.findByFechahoraBetweenCliente(mesInicio.atStartOfDay(), mesFin.atStartOfDay(), idCliente);
			break;
		default:
			citas = servCita.findAll();
		}

		if(citas.size()>0) {
			model.addAttribute("citas", citas);
		}
		else {
			model.addAttribute("citas", null);
		}
		model.addAttribute("filtroSeleccionado", filtro);
		return "vermiscitascliente";
	}
	
	

	/*ELiminar cita*/
	@GetMapping("/eliminarcita")
	public String mostrarEliminarCita(Model model) {
	    model.addAttribute("citas", servCita.findAllByPersonaId(userDetails.getCredenciales().getPersona().getId()));
	    model.addAttribute("origen", maincontroller.getMenuLogin());
	    
	    return "eliminarcita"; 
	}

	@PostMapping("/eliminarcita")
	public String eliminarCita(
	        @RequestParam("idCita") Long idCita,
	        RedirectAttributes redirectAttrs) {  
	    
	    try {
	        Cita cita = servCita.findById(idCita);
	        servCita.borrar(cita);
	        redirectAttrs.addFlashAttribute("mensajeExito", "¡Cita borrada correctamente!");
	    } catch (Exception e) {
	        redirectAttrs.addFlashAttribute("error", "Error al borrar la cita: " + e.getMessage());
	    }
	    
	    return "redirect:/eliminarcita";  
	}

	/*Filtrar citas*/
	@GetMapping("/filtrarcitas")
	public String mostrarFiltrarCitas(Model model) {
		model.addAttribute("origen", maincontroller.getMenuLogin());
		List<Persona> listaPersonas = servPersona.findAllExceptAdmin();
		model.addAttribute("personalFiltrarPersona", listaPersonas);
		List<Servicio> listaServicios = servServicio.findAll();
		model.addAttribute("serviciosFiltrarServicio", listaServicios);
		return "filtrarcitas";
	}

	@PostMapping("/citaFiltrarPersona")
	public String citaFiltrarPersona(@RequestParam("persona") String persona,
			Model model) {
		
		List<Cita> listaCitas = servCita.findByNombrePersona(persona);
		if(listaCitas == null || listaCitas.isEmpty()) {
			model.addAttribute("error", "No existen citas para esta persona.");
			List<Persona> listaPersonas = servPersona.findAllExceptAdmin();
			model.addAttribute("personalFiltrarPersona", listaPersonas);
			List<Servicio> listaServicios = servServicio.findAll();
			model.addAttribute("serviciosFiltrarServicio", listaServicios);
			return "filtrarcitas"; 
		}

		model.addAttribute("citasFiltrarPersona", listaCitas);
		model.addAttribute("citasFiltrarFechas", null);
		model.addAttribute("citasFiltrarServicio", null);
		List<Persona> listaPersonas = servPersona.findAllExceptAdmin();
		model.addAttribute("personalFiltrarPersona", listaPersonas);
		List<Servicio> listaServicios = servServicio.findAll();
		model.addAttribute("serviciosFiltrarServicio", listaServicios);

		return "filtrarcitas"; 
	}

	@PostMapping("/citaFiltrarServicio")
	public String citaFiltrarServicio(@RequestParam("servicio") String servicio,
			Model model) {
		

		List<Cita> listaCitas = servCita.findByCodigoServicio(servicio);
		if(listaCitas == null || listaCitas.isEmpty()) {
			model.addAttribute("error", "No existen citas para este servicio.");
			List<Servicio> listaServicios = servServicio.findAll();
			model.addAttribute("serviciosFiltrarServicio", listaServicios);
			List<Persona> listaPersonas = servPersona.findAllExceptAdmin();
			model.addAttribute("personalFiltrarPersona", listaPersonas);
			return "filtrarcitas"; 
		}

		model.addAttribute("citasFiltrarPersona", null);
		model.addAttribute("citasFiltrarFechas", null);
		model.addAttribute("citasFiltrarServicio", listaCitas);
		List<Servicio> listaServicios = servServicio.findAll();
		model.addAttribute("serviciosFiltrarServicio", listaServicios);
		List<Persona> listaPersonas = servPersona.findAllExceptAdmin();
		model.addAttribute("personalFiltrarPersona", listaPersonas);

		return "filtrarcitas"; 
	}

	@GetMapping("/filtrarcitasfechasadmin")
	public String filtrarCitasFechasAdmin(@RequestParam String filtro, Model model) {
		List<Cita> listaCitas;
		LocalDateTime ahora = LocalDateTime.now();

		switch (filtro) {
		case "dia":
			LocalDate hoyInicio = ahora.toLocalDate();
			LocalDate hoyFin = hoyInicio.plusDays(1);
			listaCitas = servCita.findByFechahoraBetween(hoyInicio.atStartOfDay(), hoyFin.atStartOfDay());
			break;
		case "semana":
			LocalDate lunes = ahora.toLocalDate().with(DayOfWeek.MONDAY);
			LocalDate domingo = lunes.plusDays(7);
			listaCitas = servCita.findByFechahoraBetween(lunes.atStartOfDay(), domingo.atStartOfDay());
			break;
		case "mes":
			LocalDate mesInicio = ahora.toLocalDate().withDayOfMonth(1);
			LocalDate mesFin = mesInicio.plusMonths(1);
			listaCitas = servCita.findByFechahoraBetween(mesInicio.atStartOfDay(), mesFin.atStartOfDay());
			break;
		default:
			listaCitas = servCita.findAll();
		}

		model.addAttribute("citasFiltrarPersona", null);
		model.addAttribute("citasFiltrarFechas", listaCitas);
		model.addAttribute("citasFiltrarServicio", null);
		model.addAttribute("filtroSeleccionado", filtro);
		List<Servicio> listaServicios = servServicio.findAll();
		model.addAttribute("serviciosFiltrarServicio", listaServicios);
		List<Persona> listaPersonas = servPersona.findAllExceptAdmin();
		model.addAttribute("personalFiltrarPersona", listaPersonas);
		return "filtrarcitas";
	}


}