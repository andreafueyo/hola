package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Persona;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios.ServiciosPersona;

@Controller
public class ControllerPersonal {

    @Autowired
    private ServiciosPersona servPersona;

	@GetMapping("/verpersonal")
	public String verClientes(Model model) {
		List<Persona> listaPersonas = servPersona.findAll();
		model.addAttribute("personal", listaPersonas);
		return "verpersonal";  
	}

}