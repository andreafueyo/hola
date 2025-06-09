package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Pedido;


@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>{
	
	@Query("SELECT p FROM Pedido p WHERE p.id = :id")
	Pedido findByPedidoId(@Param("id") Long id);
	
}