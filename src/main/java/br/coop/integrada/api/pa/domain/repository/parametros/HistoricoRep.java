package br.coop.integrada.api.pa.domain.repository.parametros;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.coop.integrada.api.pa.domain.model.Historico;

public interface HistoricoRep extends JpaRepository<Historico, Long>{
	Page<Historico> findByDataCadastroAfterAndDataCadastroBeforeOrderByDataCadastroDesc(Date dataInicio, Date dataFim,  Pageable pageable);
	Page<Historico> findByCodEstabelecimentoAndSafraAndNroDocPesagemOrderByDataCadastroDesc(String codEstabelecimento, Integer safra, Integer nroDocPesagem, Pageable pageable);
}
