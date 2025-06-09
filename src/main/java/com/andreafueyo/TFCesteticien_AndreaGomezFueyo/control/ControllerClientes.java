package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Cliente;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios.ServiciosCliente;

@Controller
public class ControllerClientes {

    @Autowired
    private ServiciosCliente servCliente;

	@GetMapping("/verclientes")
	public String verClientes(Model model) {
		List<Cliente> listaClientes = servCliente.findAll();
		model.addAttribute("clientes", listaClientes);
		return "verclientes";  
	}

}

