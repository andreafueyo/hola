package com.andreafueyo.TFCesteticien_AndreaGomezFueyo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.modelo.Credenciales;
import com.andreafueyo.TFCesteticien_AndreaGomezFueyo.repositorios.CredencialesRepository;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService{
	@Autowired
    private CredencialesRepository credencialesRepository;
	
	private Credenciales credenciales;

    public CustomUserDetailsServiceImpl(CredencialesRepository credencialesRepository) {
        this.credencialesRepository = credencialesRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        credenciales = credencialesRepository.findByUsuario(username);

        if (credenciales == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        System.out.println("🔑 Usuario encontrado: " + credenciales.getUsuario());
        System.out.println("Contraseña guardada en la BBDD: " + credenciales.getPassword());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("Comparando contraseña: " + encoder.matches("personal", credenciales.getPassword()));
        
        System.out.println("CODIFICADA");
        System.out.println(encoder.encode("admin"));

        return new User(
                credenciales.getUsuario(),
                credenciales.getPassword(), // aquí va la contraseña tal cual
                List.of(new SimpleGrantedAuthority(credenciales.getRol()))
        );
    }

	public Credenciales getCredenciales() {
		return credenciales;
	}

	public void setCredenciales(Credenciales credenciales) {
		this.credenciales = credenciales;
	}
    
    

}
