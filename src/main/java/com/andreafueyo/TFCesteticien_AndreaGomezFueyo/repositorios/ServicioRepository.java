package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Servicio;


@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long>{

	default boolean existeCodigo(Servicio sc) {
		List<Servicio> listaservicios = findAll();
		for(Servicio aux:listaservicios) {
			if(aux.getCodigo() != null && sc.getCodigo().equals(aux.getCodigo()))
					return true;
		}
		return false;
	}
	

	@Query("SELECT sc FROM Servicio sc WHERE sc.codigo = :cod")
	Servicio findByCod(@Param("cod") String cod);
	
	@Query("SELECT sc FROM Servicio sc WHERE sc.id = :id")
	Servicio findByServicioId(@Param("id") Long id);

	
	List<Servicio> findAllByOrderByCodigoAsc();
		
}