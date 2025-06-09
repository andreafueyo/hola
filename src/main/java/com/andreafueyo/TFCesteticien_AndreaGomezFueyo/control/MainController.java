package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.control;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Cliente;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Credenciales;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Persona;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Servicio;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios.ServiciosCliente;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios.ServiciosCredenciales;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios.ServiciosPersona;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios.ServiciosServicio;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class MainController {

	@Autowired
	private ServiciosServicio servServicio;
	@Autowired
	private ServiciosCredenciales servCredenciales;
	@Autowired
	private ServiciosPersona servPersona;
	@Autowired
	private ServiciosCliente servCliente;

	
	private String menuLogin;
	
	public MainController() {
		this.menuLogin = "menucliente";
	}
		
    public String getMenuLogin() {
		return menuLogin;
	}

	public void setMenuLogin(String menuLogin) {
		this.menuLogin = menuLogin;
	}

	/*Ver servicios*/
	@GetMapping("/verservicios")
	public String verServicios(Model model) {
		List<Servicio> listaServicios = servServicio.verServicios();
		model.addAttribute("servicios", listaServicios);
		return "verservicios";  
	}
	
	/*Quienes somos*/
	@GetMapping("/quienessomos")
	public String quienesSomos(Model model) {

		return "quienessomos";  
	}

	/*Iniciar sesión*/
	@GetMapping("/iniciarsesion")
	public String inicioSesion() {
		return "iniciarsesion";
	}

	/*Sesión ADMIN*/
	@GetMapping("/menuadmin")
	public String menuAdmin(Model model) {
		return "menuadmin"; 
	}

	/*Sesión USUARIO*/
	@GetMapping("/menupersonal")
	public String menuPersonal(Model model) {
		return "menupersonal"; 
	}
	
	/*Sesión CLIENTE*/
	@GetMapping("/menucliente")
	public String menuCliente(Model model) {
		return "menucliente"; 
	}

	/*Registrar persona*/
	@GetMapping("/registrarpersona")
	public String registrarPersona(Model model) {
		Credenciales credenciales = new Credenciales();
		credenciales.setPersona(new Persona());
		model.addAttribute("credenciales", new Credenciales());
		return "registrarpersona";
	}

	@PostMapping("/registrarpersona")
	public String procesarRegistroPersona(@ModelAttribute Credenciales credenciales, Model model) {
	    
	    if (credenciales.getPersona() == null) {
	        model.addAttribute("error", "Información de persona incompleta.");
	        return "registrarpersona";
	    }
	    
	    
	    String nombre = credenciales.getPersona().getNombre();
	    String telefono = credenciales.getPersona().getTelefono();
	    String nif = credenciales.getPersona().getNif();
	    LocalDate fechaNac = credenciales.getPersona().getFecha_nac();
	    String email = credenciales.getPersona().getEmail();
	    String password = credenciales.getPassword();

	   
	    if (nombre == null || nombre.trim().isEmpty()) {
	        model.addAttribute("error", "El nombre no puede estar vacío.");
	        return "registrarpersona";
	    }

	    
	    if (password.contains(" ") || email.contains(" ")) {
	        model.addAttribute("error", "Contraseña o email no válidos. No deben contener espacios.");
	        return "registrarpersona";
	    }

	 
	    if (telefono == null || !telefono.matches("^[0-9]{9}$")) {
	        model.addAttribute("error", "El número de teléfono debe tener exactamente 9 dígitos numéricos.");
	        return "registrarpersona";
	    }

	    
	    if (fechaNac == null) {
	        model.addAttribute("error", "La fecha de nacimiento no puede estar vacía.");
	        return "registrarpersona";
	    }

	    if (fechaNac.isAfter(LocalDate.now())) {
	        model.addAttribute("error", "La fecha de nacimiento no puede ser futura.");
	        return "registrarpersona";
	    }

	    
	    if (nif == null || !nif.matches("^[0-9]{8}[a-zA-Z]$")) {
	        model.addAttribute("error", "El NIF debe tener 8 números seguidos de una letra.");
	        return "registrarpersona";
	    }

	    
	    String usuario = nombre.trim().replaceAll("\\s+", "").toLowerCase() + ".esteticien";
	    credenciales.setUsuario(usuario);

	   
	    if (!servCredenciales.validarNuevaCredencial(credenciales) ||
	        servPersona.findByEmail(email) != null) {
	        model.addAttribute("error", "El usuario o el email ya existen.");
	        return "registrarpersona";
	    }
	    
	   
	    try {
	        credenciales.setRol("ROLE_PERSONAL");
	        servCredenciales.insertar(credenciales);
	        model.addAttribute("exito", "Usuario creado con éxito: " + usuario);
	        model.addAttribute("credenciales", new Credenciales());
	    } catch (DataIntegrityViolationException e) {
	        String errorMessage = "Error al registrar. ";
	        
	        if (e.getRootCause() != null && e.getRootCause().getMessage().contains("Duplicate entry")) {
	            if (e.getRootCause().getMessage().contains("nif") || e.getRootCause().getMessage().contains("UKhipdfpt5i2o1h8n3q78povtta")) {
	                errorMessage += "El DNI ya está registrado en el sistema.";
	            } else if (e.getRootCause().getMessage().contains("email")) {
	                errorMessage += "El email ya está registrado.";
	            } else {
	                errorMessage += "Datos duplicados (usuario, email o NIF).";
	            }
	        } else {
	            errorMessage += "Error en la base de datos.";
	        }
	        
	        model.addAttribute("error", errorMessage);
	        return "registrarpersona";
	    } catch (Exception e) {
	        model.addAttribute("error", "Error inesperado al registrar: " + e.getMessage());
	        return "registrarpersona";
	    }

	    return "registrarpersona";
	}


	
	/*Registrar cliente*/
	@GetMapping("/registrarcliente")
	public String registrarCliente(Model model) {
		Credenciales credenciales = new Credenciales();
		credenciales.setCliente(new Cliente());
		model.addAttribute("credenciales", new Credenciales());
		return "registrarcliente";
	}


	@PostMapping("/registrarcliente")
	public String procesarRegistroCliente(@ModelAttribute Credenciales credenciales, Model model) {
	    String telefono = credenciales.getCliente().getTelefono();
	    String nif = credenciales.getCliente().getNIF();

	    if (credenciales.getUsuario().contains(" ") || credenciales.getPassword().contains(" ")
	            || credenciales.getCliente().getEmail().contains(" ")) {
	        model.addAttribute("error", "Usuario, contraseña o nombre no válidos. Introduzca de nuevo los datos sin espacios.");
	        return "registrarcliente";
	    }

	    if (telefono == null || !telefono.matches("^[0-9]{9}$")) {
	        model.addAttribute("error", "El número de teléfono debe tener exactamente 9 dígitos numéricos.");
	        return "registrarcliente";
	    }

	    LocalDate fechaNac = credenciales.getCliente().getFecha_nac();
	    if (fechaNac == null) {
	        model.addAttribute("error", "La fecha de nacimiento no puede estar vacía.");
	        return "registrarcliente";
	    }
	    
	    LocalDate hoy = LocalDate.now();
	    if (fechaNac.isAfter(hoy)) {
	        model.addAttribute("error", "La fecha de nacimiento aún no pasó.");
	        return "registrarcliente";
	    }
	    
	    
	    
	    
	    if (nif == null || !nif.matches("^[0-9]{8}[a-zA-Z]$")) {
	        model.addAttribute("error", "El DNI debe tener 8 números seguidos de una letra.");
	        return "registrarcliente";
	    }

	    if (servCredenciales.validarNuevaCredencial(credenciales) 
	    		&& servCliente.findByNIF(credenciales.getCliente().getNIF()) == null) {
	        credenciales.setRol("ROLE_CLIENTE");
	        servCredenciales.insertar(credenciales);
	        model.addAttribute("exito", "Usuario creado con éxito en nuestra base de datos.");
	        model.addAttribute("credenciales", new Credenciales());
	        return "registrarcliente";
	    } else {
	        model.addAttribute("error", "El usuario ya existe.");
	        return "registrarcliente";
	    }
	}



	/*Cerrar sesión*/
	@GetMapping("/cerrarsesion")
	public String cerrarSesion(SessionStatus status) {
		status.setComplete(); 
		return "redirect:/"; 
	}
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return "redirect:/iniciarsesion?logout";
    }

	/*Raíz*/
	@GetMapping("/")
	public String index(Model model) {
		return "index";
	}

}