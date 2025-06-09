package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.PedidoProducto;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Pedido;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Producto;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios.PedidoProductoRepository;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios.PedidoRepository;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios.ProductoRepository;


@Service
public class ServiciosPedidoProducto {
	
	@Autowired
	private PedidoRepository pedidorepo;
	@Autowired
	private ProductoRepository productorepo;
	@Autowired
	private PedidoProductoRepository pedidoproductorepo;
	
	public void guardarDetalle(Long pedidoId, String codProducto, int cantidad) {
	    Pedido pedido = pedidorepo.findByPedidoId(pedidoId);
	    Producto producto = productorepo.findByCod(codProducto);

	    PedidoProducto detalle = new PedidoProducto();
	    
	    detalle.setPedido(pedido);
	    detalle.setProducto(producto);
	    detalle.setCantidad(cantidad);

	    pedidoproductorepo.save(detalle);
	}

}

