package br.coop.integrada.api.pa.domain.repository.baixaCredito;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import br.coop.integrada.api.pa.domain.model.baixaCredito.BaixaCreditoMov;

public interface BaixaCreditoMovRep extends JpaRepository<BaixaCreditoMov, Long>{
	BaixaCreditoMov findByCodEstabelAndNrRe(String codEstabel, Long nrRe);
	BaixaCreditoMov findByCodEstabelAndIdRe(String codEstabel, Long idRe);
	Page<BaixaCreditoMov> findByCodEstabel(String codEstabel, Pageable pageable);
	Page<BaixaCreditoMov> findByNrRe(Integer nrRe, Pageable pageable);	
	Page<BaixaCreditoMov> findByCodUsuario(String codUsuario, Pageable pageable);
	BaixaCreditoMov findByIdMovtoBxaCredAndCodEstabel(String idMovtoBxaCred, String codEstabel);
}
