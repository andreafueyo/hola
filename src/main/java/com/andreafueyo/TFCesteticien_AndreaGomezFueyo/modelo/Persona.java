package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

	@Entity
	@Table(name="personas")

	public class Persona implements Serializable {

		private static final long serialVersionUID = 1L;
		
		@Id
		/*autoincrement*/
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;

		@Column
		private String nombre;
		
		@Column
		private String apellidos;
		
		@Column (unique = true)
		private String nif;
		
		@Column (unique = true)
		private String telefono;
		
		@Column (unique = true)
		private String email;
		
		@Column 
		private LocalDate fecha_nac;
		
		@OneToOne(mappedBy = "persona", cascade = CascadeType.ALL)
	    private Credenciales credenciales;
		
		@OneToMany(mappedBy = "persona")
		private List<Cita> citas = new ArrayList<>();

		public List<Cita> getCitas() {
		    return citas;
		}

		public void setCitas(List<Cita> citas) {
		    this.citas = citas;
		}
		
		public Persona() {}
		
		public Long getId() {
			return id;
		}
		
		public void setId(Long id) {
			this.id = id;
		}
		
		public String getNombre() {
			return nombre;
		}
		
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		
		public String getApellidos() {
			return apellidos;
		}
		
		public void setApellidos(String apellidos) {
			this.apellidos = apellidos;
		}
		
		public String getNif() {
			return nif;
		}
		
		public void setNif(String nif) {
			this.nif = nif;
		}
		
		public String getTelefono() {
			return telefono;
		}
		
		public void setTelefono(String telefono) {
			this.telefono = telefono;
		}
		
		
		public String getEmail() {
			return email;
		}
		
		public void setEmail(String email) {
			this.email = email;
		}
		
		public LocalDate getFecha_nac() { 
		    return fecha_nac; 
		}

		public void setFecha_nac(LocalDate fecha_nac) { 
		    this.fecha_nac = fecha_nac; 
		}
		
		public Credenciales getCredenciales() { return credenciales; }
	    public void setCredenciales(Credenciales credenciales) { this.credenciales = credenciales; }
	
		@Override
		public String toString() {
			String ret ="";
			ret ="PERSONA";
			ret += "\tID: " + this.id;
			ret += "\tNombre: " + this.nombre;
			ret += "\tCorreo electrónico " + this.email;
			return ret;
		}
}
