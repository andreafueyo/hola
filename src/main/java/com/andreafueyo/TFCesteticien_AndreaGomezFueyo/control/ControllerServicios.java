package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Servicio;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios.ServiciosServicio;


@Controller
public class ControllerServicios {

    @Autowired
    private ServiciosServicio servServicio;

    /* Gestión de servicios */
    @GetMapping("/gestionservicios")
    public String gestionServicios(Model model) {
        List<Servicio> listaServicios = servServicio.verServicios();
        model.addAttribute("listaServicios", listaServicios); 
        return "gestionservicios"; 
    }

    /* Registrar servicio */
    @GetMapping("/registrarservicio")
    public String mostrarFormularioRegistroServicio(Model model) {
    	model.addAttribute("servicio", new Servicio());
    	return "registrarservicio"; 
    }

    @PostMapping("/registrarservicio")
    public String registrarServicio(@ModelAttribute Servicio servicio, Model model) {
    	
    	if(servicio.getCodigo() == null || servicio.getCodigo().contains(" ") || servicio.getCodigo().isEmpty()) {
            model.addAttribute("error", "El código del servicio está vacío o contiene espacios.");
    	}
    	else {
        	if (servServicio.validarServicio(servicio)) {
        		servServicio.insertarServicio(servicio);
                model.addAttribute("exito", "Servicio registrada con éxito.");
            } else {
                model.addAttribute("error", "El código del servicio ya existe o es inválido.");
            }
    	}
    
    return "registrarservicio";
    }  
    

    /*Modificar servicio*/
    @GetMapping("/modificarservicio")
    public String mostrarFormularioModificarServicio(@RequestParam(required = false) String codServicio, Model model) {

        List<Servicio> listaServicios = servServicio.verServicios();
        model.addAttribute("listaServicios", listaServicios);

        return "modificarservicio";  
    }

    @PostMapping("/modificarservicio")
    public String modificarServicio(@RequestParam String codServicio, 
    							  @RequestParam String nombre,
                                  @RequestParam int duracion, 
                                  @RequestParam double precio, 
                                  Model model) {

        if (nombre.trim().isEmpty()) {
            model.addAttribute("error", "Todos los campos son obligatorios.");
            return "modificarservicio";
        }

        Servicio servicio = servServicio.findByCod(codServicio);
        if (servicio == null) {
            model.addAttribute("error", "Servicio no encontrada.");
            return "modificarservicio"; 
        }

        servicio.setNombre(nombre);
        servicio.setDuracion(duracion);
        servicio.setPrecio(precio);
        
        try {
            servServicio.modificar(servicio);
            model.addAttribute("success", "Servicio modificada correctamente.");
            List<Servicio> listaServicios = servServicio.verServicios();
            model.addAttribute("listaServicios", listaServicios);
        } catch (Exception e) {
            model.addAttribute("error", "Error al modificar la servicio.");
        }


        return "modificarservicio";  
    }


}
