package com.emilio.sorpyco_tfg.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emilio.sorpyco_tfg.entidades.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	Optional<Cliente> findByRazonSocialIgnoreCase(String razonSocial);
	
}
