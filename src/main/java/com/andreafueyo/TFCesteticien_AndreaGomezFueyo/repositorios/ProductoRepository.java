package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Producto;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{
	
	default boolean existeCodigo(Producto p) {
		List<Producto> listaproductos = findAll();
		for(Producto aux:listaproductos) {
			if(aux.getCodigo() != null && p.getCodigo().equals(aux.getCodigo()))
					return true;
		}
		return false;
	}

	@Query("SELECT p FROM Producto p WHERE p.codigo = :cod")
	Producto findByCod(@Param("cod") String cod);
	
	List<Producto> findAllByOrderByCodigoAsc();
	
	@Query("SELECT p FROM Producto p WHERE p.id = :id")
	Producto findByProductoId(@Param("id") Long id);

}
