package br.coop.integrada.api.pa.domain.repository.tratamentoTransacaoRe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import br.coop.integrada.api.pa.domain.model.tratamentoTransacaoRe.TransacaoRe;



public interface TransacaoReRep extends JpaRepository<TransacaoRe, Long>, JpaSpecificationExecutor<TransacaoRe>{

	public TransacaoRe findByCodEstabelAndIdRe(String codEstabel, Long idRe);

	
	
	

}
