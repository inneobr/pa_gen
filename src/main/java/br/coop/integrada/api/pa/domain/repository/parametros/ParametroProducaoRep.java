package br.coop.integrada.api.pa.domain.repository.parametros;

import org.springframework.data.jpa.repository.JpaRepository;

import br.coop.integrada.api.pa.domain.model.parametros.ParametroProducao;

public interface ParametroProducaoRep extends JpaRepository<ParametroProducao, Long> {

}
