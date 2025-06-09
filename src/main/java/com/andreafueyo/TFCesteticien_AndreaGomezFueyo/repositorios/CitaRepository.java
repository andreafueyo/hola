package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Cita;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long>{
	
	List<Cita> findByPersonaIdAndFechahoraBetween(Long personaId, LocalDateTime inicio, LocalDateTime fin);
	
	List<Cita> findByFechahoraBetween(LocalDateTime inicio, LocalDateTime fin);

	@Query("SELECT c FROM Cita c WHERE c.id = :id")
	Cita findByCitaId(@Param("id") Long id);

	List<Cita> findByPersonaId(Long idPersona);
	
	List<Cita> findByClienteId(Long idCliente);
	
	@Query("SELECT c FROM Cita c JOIN c.persona p WHERE p.nombre = :nombre")
	List<Cita> findByNombrePersona(@Param("nombre") String nombre);

	@Query("SELECT c FROM Cita c JOIN c.servicio s WHERE s.codigo = :codigo")
	List<Cita> findByCodigoServicio(@Param("codigo") String codigo);

	List<Cita> findByClienteIdAndFechahoraBetween(Long cliente, LocalDateTime inicio, LocalDateTime fin);

	
}



