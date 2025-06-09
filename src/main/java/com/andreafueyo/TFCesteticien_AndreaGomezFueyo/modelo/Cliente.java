package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedList;
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
	@Table(name="clientes")

	public class Cliente implements Serializable {

		private static final long serialVersionUID = 1L;
		
		@Id
		/*autoincrement*/
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;

		@Column
		private String nombre;
		
		@Column 
		private LocalDate fecha_nac;
		
		@Column (unique = true)
		private String NIF;
		
		@Column
		private String direccion;
		
		@Column
		private String email;
		
		@Column
		private String telefono;
		
		@OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL)
	    private Credenciales credenciales;	
		
		@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
		private List<Pedido> pedidos = new LinkedList<Pedido>();
		
		public Cliente() {}
		
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

		public String getNIF() {
			return NIF;
		}

		public void setNIF(String nIF) {
			NIF = nIF;
		}

		public String getDireccion() {
			return direccion;
		}

		public void setDireccion(String direccion) {
			this.direccion = direccion;
		}

		public String getTelefono() {
			return telefono;
		}

		public void setTelefono(String telefono) {
			this.telefono = telefono;
		}

		public Credenciales getCredenciales() { return credenciales; }
	    public void setCredenciales(Credenciales credenciales) { this.credenciales = credenciales; }
	
		@Override
		public String toString() {
			String ret ="";
			ret ="CLIENTE";
			ret += "\tID: " + this.id;
			ret += "\tNombre: " + this.nombre;
			ret += "\tCorreo electrónico " + this.email;
			return ret;
		}
}
