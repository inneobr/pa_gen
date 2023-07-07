package br.coop.integrada.api.pa.domain.repository.tratamentoTransacaoRe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.enums.tratamentoTransacaoRe.MovimentoReEnum;
import br.coop.integrada.api.pa.domain.model.tratamentoTransacaoRe.TransacaoReMov;


@Repository
public interface TransacaoReMovRep extends JpaRepository<TransacaoReMov, Long> {

	TransacaoReMov getByCodEstabelAndIdReAndNrReAndMovimento(String codEstabel, Long idRe, Long nrRe, MovimentoReEnum movimento);

	TransacaoReMov getByCodEstabelAndIdReAndMovimento(String codEstabel, Long idRe, MovimentoReEnum movimento);

	Page<TransacaoReMov> findByIdRe(Long idRe, Pageable pageable);

}