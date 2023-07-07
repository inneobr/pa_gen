package br.coop.integrada.api.pa.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.coop.integrada.api.pa.domain.model.usuario.Usuario;

public interface UsuarioRep extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario>{
	Usuario findByCodUsuarioIgnoreCase(String codUsuario);
	Usuario findByUsernameIgnoreCase(String username);	
	Usuario findByMatricula(String matricula);
	Usuario findByIdOrCodUsuario(Long id, String codUsuario);
	List<Usuario> findByCpf(String cpf);

}
