package br.coop.integrada.api.pa.domain.repository.gmo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.coop.integrada.api.pa.domain.model.produto.TipoGmo;

public interface GmoRep extends JpaRepository<TipoGmo, Long>{
	List<TipoGmo> findByDataInativacao(Date data);
}
