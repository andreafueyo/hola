package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.PedidoProducto;

@Repository
public interface PedidoProductoRepository extends JpaRepository<PedidoProducto, Long>{
	
}

