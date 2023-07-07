package br.coop.integrada.api.pa.domain.repository.baixaCredito;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.coop.integrada.api.pa.domain.model.baixaCredito.BaixaCredito;

public interface BaixaCreditoRep  extends JpaRepository<BaixaCredito, Long> {
	BaixaCredito findByCodEstabelAndNrRe(String codEstabel, Long nrRe);
	BaixaCredito findByCodEstabelAndIdRe(String codEstabel, Long idRe);
	Page<BaixaCredito> findByNrRe(Integer nrRe, Pageable pageable);
	Page<BaixaCredito> findByCodEmitente(Integer codEmitente, Pageable pageable);
}
