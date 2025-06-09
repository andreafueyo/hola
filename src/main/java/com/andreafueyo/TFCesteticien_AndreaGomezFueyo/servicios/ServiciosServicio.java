package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Servicio;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios.ServicioRepository;
 
@Service
public class ServiciosServicio {

	@Autowired
	private ServicioRepository serviciorepo;
	
	public boolean validarServicio(Servicio p) {
		if (serviciorepo.existeCodigo(p)) {
			return false;
		}
		return true;
	}
	

	public void insertarServicio(Servicio p) {
		Servicio servicio =  serviciorepo.saveAndFlush(p);
	}
	
	public List<Servicio> findAll(){
		return serviciorepo.findAll();
	}

	public Servicio modificar(Servicio p) {
		if (!serviciorepo.existeCodigo(p)) {
			return null;
		}
		return serviciorepo.saveAndFlush(p);
	}

	public Servicio findByCod(String cod) {
		return serviciorepo.findByCod(cod);
	}
	
	public Servicio findById(Long id) {
		return serviciorepo.findByServicioId(id);
	}
	
	public List<Servicio> verServicios() {
		return serviciorepo.findAllByOrderByCodigoAsc();
	}


	public String verServicioDetalle(Servicio sc) {
		String ret = "";
		ret += sc.toString();
	
		return ret;		
	}
}
