package br.coop.integrada.api.pa.domain.repository.gmo;

import br.coop.integrada.api.pa.domain.model.produto.TipoGmo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TipoGmoRep extends JpaRepository<TipoGmo, Long>, TipoGmoQueriesRep, JpaSpecificationExecutor<TipoGmo>{
	List<TipoGmo> findByDataInativacaoNull();
	Page<TipoGmo> findByDataInativacaoNull(Pageable pageable);

	List<TipoGmo> findByTipoGmoContainingIgnoreCase(String nome, Pageable pageable);
	List<TipoGmo> findByTipoGmoContainingIgnoreCaseAndDataInativacaoNull(String nome, Pageable pageable);

	TipoGmo findByTipoGmoIgnoreCase(String tipoGmo);	
	TipoGmo findByIdUnicoIgnoreCase(String idUnico);

}
