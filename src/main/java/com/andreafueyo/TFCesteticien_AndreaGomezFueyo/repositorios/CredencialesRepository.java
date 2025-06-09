package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Credenciales;

@Repository
public interface CredencialesRepository extends JpaRepository<Credenciales, Long>{
	
	@Query("SELECT c FROM Credenciales c WHERE c.usuario = :usuario")
	Credenciales findByUsuario(@Param("usuario") String usuario);
	
	@Query("SELECT c FROM Credenciales c WHERE c.persona.email = :email")
	Credenciales findByEmail(@Param("email") String email);

	
}
