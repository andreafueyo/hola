package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

	@Entity
	@Table(name = "pedido_producto")
	public class PedidoProducto implements Serializable {

	    @EmbeddedId
	    private PedidoProductoId id = new PedidoProductoId();

	    @ManyToOne
	    @MapsId("pedidoId")
	    @JoinColumn(name = "pedido_id")
	    private Pedido pedido;

	    @ManyToOne
	    @MapsId("productoId")
	    @JoinColumn(name = "producto_id")
	    private Producto producto;

	    @Column
	    private int cantidad;

	    // Getters y setters
	    public Pedido getPedido() { return pedido; }
	    public void setPedido(Pedido pedido) { this.pedido = pedido; }

	    public Producto getProducto() { return producto; }
	    public void setProducto(Producto producto) { this.producto = producto; }

	    public int getCantidad() { return cantidad; }
	    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

	    public PedidoProductoId getId() { return id; }
	    public void setId(PedidoProductoId id) { this.id = id; }
	    
	    @Embeddable
	    public static class PedidoProductoId implements Serializable {

			private static final long serialVersionUID = 1L;
			private Long pedidoId;
	        private Long productoId;

	        public PedidoProductoId() {}

	        public PedidoProductoId(Long pedidoId, Long productoId) {
	            this.pedidoId = pedidoId;
	            this.productoId = productoId;
	        }

	        public Long getPedidoId() { return pedidoId; }
	        public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

	        public Long getProductoId() { return productoId; }
	        public void setProductoId(Long productoId) { this.productoId = productoId; }

	    }
	}
