package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.control;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.CarritoCompra;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Cliente;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.CustomUserDetailsServiceImpl;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Producto;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios.ServiciosPedido;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios.ServiciosPedidoProducto;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios.ServiciosProducto;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ControllerPedido {


	@Autowired
	private ServiciosProducto servProducto;

	@Autowired
	private ServiciosPedidoProducto servPedidoProducto;

	@Autowired
	private ServiciosPedido servPedido;

	@Autowired
	private MainController maincontroller;

	@Autowired
	private CarritoCompra carritoCompra;

	@Autowired
	private CustomUserDetailsServiceImpl userDetails;

	@GetMapping("/realizarpedido")
	public String mostrarRealizarPedido(Model model) {
		model.addAttribute("productos", servProducto.findAll());
		model.addAttribute("origen", maincontroller.getMenuLogin());  
		return "realizarpedido"; 
	}
	
	@GetMapping("/verejemplaresplanta")
	public String mostrarVerEjemplaresPlanta(Model model) {
		return "gestionstock"; 
	}
	
	@PostMapping("/anadirpedido")
	public String anadirAlCarrito(@RequestParam String codProducto,
	                              @RequestParam int cantidadSeleccionada,
	                              Model model) {

	    if (cantidadSeleccionada <= 0) {
	        model.addAttribute("error", "La cantidad debe ser mayor que cero.");
	        return "realizarpedido";
	    }

	    Producto producto = servProducto.findByCod(codProducto);

	    if (producto == null) {
	        model.addAttribute("error", "Producto no encontrado.");
	        return "realizarpedido";
	    }

	    if (producto.getCantidad() < cantidadSeleccionada) {
	        model.addAttribute("error", "No hay suficiente stock disponible.");
	        return "realizarpedido";
	    }

	    carritoCompra.agregarItem(codProducto, cantidadSeleccionada);
		return "redirect:/vercarrito";
	}


	@GetMapping("/vercarrito")
	public String verCarrito(Model model) {
		model.addAttribute("carrito", carritoCompra.getItems());
		return "vercarrito";
	}

	@PostMapping("/eliminarpedido")
	public String eliminarPedido(@RequestParam("codigo") String codigo) {
		carritoCompra.eliminarItem(codigo);
		return "redirect:/vercarrito";
	}

	@PostMapping("/vaciarcarrito")
	public String vaciarCarrito() {
		carritoCompra.vaciarCarrito();
		return "redirect:/vercarrito";
	}

	@GetMapping("/finalizarpedido")
	public String mostrarFinalizarPedido(Model model) {
		//comprobar cada item del carrito si sigue estando disponible esa cantidad
		Map<String, Integer> items = carritoCompra.getItems();

		if (items.isEmpty()) {
			model.addAttribute("error", "El carrito está vacío.");
			return "finalizarpedido";
		}
		model.addAttribute("carrito", items);
		model.addAttribute("cliente", userDetails.getCredenciales().getCliente());
		model.addAttribute("origen", maincontroller.getMenuLogin());  
		return "finalizarpedido"; 
	}

	@PostMapping("/finalizarpedido")
	public String finalizarPedido(Model model, RedirectAttributes redirectAttributes) {

	    Map<String, Integer> items = carritoCompra.getItems();

	    if (items.isEmpty()) {
	    	redirectAttributes.addFlashAttribute("error", "El carrito está vacío.");
			return "redirect:/vercarrito";
	    }

	    for (Map.Entry<String, Integer> item : items.entrySet()) {
	        String codProducto = item.getKey();
	        int cantidad = item.getValue();

	        Producto producto = servProducto.findByCod(codProducto);

	        if (producto == null) {
	        	redirectAttributes.addFlashAttribute("error", "Un producto del carrito ya no existe.");
	            carritoCompra.eliminarItem(codProducto);
	    		return "redirect:/vercarrito";
	        }

	        if (producto.getCantidad() < cantidad) {
	        	redirectAttributes.addFlashAttribute("error", "No hay stock suficiente para el producto: " + codProducto);
	            carritoCompra.eliminarItem(codProducto);
	    		return "redirect:/vercarrito";
	        }
	    }

	    Cliente cliente = userDetails.getCredenciales().getCliente();
	    Long pedidoId = servPedido.registrarPedido(cliente);

	    for (Map.Entry<String, Integer> item : items.entrySet()) {
	        String codProducto = item.getKey();
	        int cantidad = item.getValue();

	        Producto producto = servProducto.findByCod(codProducto);

	        producto.setCantidad(producto.getCantidad() - cantidad);
	        servProducto.modificar(producto);
	        
			servPedidoProducto.guardarDetalle(pedidoId, codProducto, cantidad);
	    }

	    carritoCompra.vaciarCarrito();
	    model.addAttribute("mensajeExito", "Pedido registrado con éxito.");
	    model.addAttribute("cliente", cliente);

	    return "finalizarpedido";
	}
	
    /*ELiminar producto*/
    @GetMapping("/eliminarproducto")
    public String mostrarEliminarProducto(Model model) {
        model.addAttribute("productos", servProducto.findAll());
        model.addAttribute("origen", maincontroller.getMenuLogin());
        return "eliminarproducto"; 
    }

    @PostMapping("/eliminarproducto")
    public String eliminarProducto(@RequestParam("codigo") String codigo,
    		RedirectAttributes redirectAttributes) {
		
    	carritoCompra.eliminarItem(codigo);
    	redirectAttributes.addFlashAttribute("mensajeExito", "¡Producto borrado correctamente!");
    	redirectAttributes.addFlashAttribute("origen", maincontroller.getMenuLogin());
		 
		return "redirect:/vercarrito";
    }  

 
}