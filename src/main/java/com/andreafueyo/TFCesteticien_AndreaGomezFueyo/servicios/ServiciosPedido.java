package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Cliente;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Pedido;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios.ClienteRepository;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios.PedidoRepository;


@Service
public class ServiciosPedido {
	
	@Autowired
	private PedidoRepository pedidorepo;
	@Autowired
	private ClienteRepository clienterepo;
	

	public void actualizar(Pedido p) {
		pedidorepo.saveAndFlush(p);
	}
	
	public Pedido insertar(Pedido p) {
		return pedidorepo.save(p);
	}
		
	public Long registrarPedido(Cliente cl) {
		
	    
	    Cliente clientePersistente = clienterepo.findById(cl.getId())
	        .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

	    Pedido p = new Pedido();
	    p.setCliente(clientePersistente);
	    p.setFecha(LocalDate.now());

	    return this.insertar(p).getId();

	}
	
	public Pedido findById(Long id) {
		Optional<Pedido> optPedido = pedidorepo.findById(id);
		if(optPedido.isEmpty()) {
			return null;
		}
		else {
			return optPedido.get();
		}
	}
}
