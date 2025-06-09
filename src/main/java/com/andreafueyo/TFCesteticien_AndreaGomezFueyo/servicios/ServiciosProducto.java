package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Producto;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios.ProductoRepository;


@Service
public class ServiciosProducto {
	
	@Autowired
	private ProductoRepository productorepo;
	
	public boolean validarProducto(Producto p) {
		if (productorepo.existeCodigo(p)) {
			return false;
		}
		return true;
	}
	

	public void insertarProducto(Producto p) {
		Producto Producto =  productorepo.saveAndFlush(p);
	}

	public Producto modificar(Producto p) {
		if (!productorepo.existeCodigo(p)) {
			return null;
		}
		return productorepo.saveAndFlush(p);
	}

	public Producto findByCod(String cod) {
		return productorepo.findByCod(cod);
	}
	
	public List<Producto> verProductos() {
		return productorepo.findAllByOrderByCodigoAsc();
	}


	public List<Producto> findAll() {
		return productorepo.findAll();
	}


	public Producto findById(Long idProducto) {
		return productorepo.findByProductoId(idProducto);
	}

	public void borrar(Producto producto) {
		productorepo.delete(producto);
	}

}