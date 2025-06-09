package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{

	@Query("SELECT cl FROM Cliente cl WHERE cl.email = :email")
	Cliente findByEmail(@Param("email") String email);
	
	@Query("SELECT cl FROM Cliente cl WHERE cl.id = :id")
	Cliente findByClienteId(@Param("id") Long id);
	
	@Query("SELECT cl FROM Cliente cl WHERE cl.NIF = :NIF")
	Cliente findByNIF(@Param("NIF") String NIF);
	
	Cliente findByTelefono(String telefono);
	
	
	default boolean validarCliente(Cliente cl) { 
		
		if(this.findByEmail(cl.getEmail()) == null) {
			return true;
		}
		else {
			return false;
		}
	}

	default List<Cliente> todosEjemplaresDescendiente() {
		return findAll((Sort.by(Sort.Direction.DESC, "id")));
	}
	
}

