package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class CarritoCompra implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Map<String, Integer> items = new HashMap<>();

    public void agregarItem(String codigo, int cantidad) {
    	if (items.containsKey(codigo)) {
    	    items.put(codigo, items.get(codigo) + cantidad);
    	} else {
    	    items.put(codigo, cantidad);
    	}
    }

    public void eliminarItem(String codigo) {
        items.remove(codigo);
    }

    public void vaciarCarrito() {
        items.clear();
    }    
    
    public Map<String, Integer> getItems() {
        return items;
    }
}