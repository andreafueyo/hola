package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.control;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Producto;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios.ServiciosProducto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ControllerProductos {

	
    @Autowired
    private ServiciosProducto servProducto;
    
    @Autowired
    private MainController maincontroller;
    

    @GetMapping("/gestionproductos")
    public String gestionEjemplares(Model model) {
        model.addAttribute("origen", maincontroller.getMenuLogin());  
        return "gestionproductos"; 
    }

    /*Registrar productos*/
    @GetMapping("/registrarproducto")
    public String mostrarRegistroProducto(Model model) {
        model.addAttribute("productos", servProducto.verProductos());
        model.addAttribute("origen", maincontroller.getMenuLogin());
        return "registrarproducto"; 
    }

    @PostMapping("/registrarproducto")
    public String registrarProducto(@ModelAttribute Producto producto, Model model) {
    	
    	if(producto.getCodigo() == null || producto.getCodigo().contains(" ") || producto.getCodigo().isEmpty()) {
            model.addAttribute("error", "El código del producto está vacío o contiene espacios.");
    	}
    	else {
        	if (servProducto.validarProducto(producto)) {
        		servProducto.insertarProducto(producto);
                model.addAttribute("exito", "Producto registrada con éxito.");
            } else {
                model.addAttribute("error", "El código del producto ya existe o es inválido.");
            }
    	}
    
    return "registrarproducto";
    }  
     
    
    @GetMapping("/gestionstock")
    public String mostrarFormularioGestionStock(@RequestParam(required = false) String codProducto, Model model) {

        List<Producto> listaProductos = servProducto.findAll();
        model.addAttribute("listaProductos", listaProductos);
        model.addAttribute("origen", maincontroller.getMenuLogin());
        return "gestionstock";  
    }

    @PostMapping("/gestionstock")
    public String modificarPlanta(
            @RequestParam String codProducto, 
            @RequestParam int cantidad, 
            RedirectAttributes redirectAttrs) {
        
        Producto producto = servProducto.findByCod(codProducto);
        if (producto == null) {
            redirectAttrs.addFlashAttribute("error", "Producto no encontrado.");
            return "redirect:/gestionstock"; 
        }

        try {
            producto.setCantidad(cantidad);
            servProducto.modificar(producto);
            redirectAttrs.addFlashAttribute("mensajeExito", "Producto modificado correctamente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al modificar: " + e.getMessage());
        }

        return "redirect:/gestionstock"; 
    }
    
}
