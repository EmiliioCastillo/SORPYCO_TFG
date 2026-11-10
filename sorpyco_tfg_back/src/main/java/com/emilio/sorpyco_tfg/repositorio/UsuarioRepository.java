package com.emilio.sorpyco_tfg.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emilio.sorpyco_tfg.entidades.Usuario;
import com.emilio.sorpyco_tfg.enums.Rol;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	/*findByEmail: Busca un usuario por su correo electrónico.
	 * existsByEmail: Comprueba si ya existe un usuario con ese correo.
	 * findByRol: Obtiene los usuarios que tienen un determinado rol.
	 * 
	 * */
	
	
	  Optional<Usuario> findByEmail(String email);

	    boolean existsByEmail(String email);

	    List<Usuario> findByRol(Rol rol);
}
