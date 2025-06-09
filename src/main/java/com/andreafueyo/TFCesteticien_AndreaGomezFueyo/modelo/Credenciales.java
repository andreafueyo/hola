package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="credenciales")
	public class Credenciales implements Serializable {

		private static final long serialVersionUID = 1L;
		
		@Id
		/*autoincrement*/
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;

		@Column (unique = true)
		private String usuario;
		
		@Column 
		private String password;
		
		@Column 
		private String rol;
		
		@OneToOne(cascade = CascadeType.ALL)
		@JoinColumn(name="idcliente", referencedColumnName = "id", nullable = true)
		private Cliente cliente;

		@OneToOne(cascade = CascadeType.ALL)
		@JoinColumn(name="idpersona", referencedColumnName = "id", nullable = true)
		private Persona persona;

		public Credenciales() {
		}
		
		public Long getId() {
			return id;
		}
		
		public void setId(Long id) {
			this.id = id;
		}
		
		public String getUsuario() {
			return usuario;
		}
		
		public void setUsuario(String usuario) {
			this.usuario = usuario;
		}
		
		public String getPassword() {
			return password;
		}
		
		public void setPassword(String password) {
			this.password = password;
		}
		
		public Persona getPersona() {
			return persona;
		}

		public void setPersona(Persona persona) {
			this.persona = persona;
		}
		
		public Cliente getCliente() {
			return cliente;
		}

		public void setCliente(Cliente cliente) {
			this.cliente = cliente;
		}
		
		public String getRol() {
			return rol;
		}

		public void setRol(String rol) {
			this.rol = rol;
		}

		@Override
		public String toString() {
		    String ret = "CREDENCIAL";
		    ret += "\tID: " + this.id;
		    ret += "\tUsuario: " + this.usuario;
		    ret += "\tPassword: " + this.password;
		    ret += "\tfk_persona: " + (this.persona != null ? this.persona.getId() : "null");
		    ret += "\tfk_cliente: " + (this.cliente != null ? this.cliente.getId() : "null");
		    return ret;
		}

}
