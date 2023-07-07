package br.coop.integrada.api.pa.domain.repository.movimentoPesagem;

import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.coop.integrada.api.pa.domain.model.movtoPesagem.MovtoPesagem;

public interface MovtoPesagemRep extends JpaRepository<MovtoPesagem, Long> {
	MovtoPesagem findByIdMovtoPesagem(String idMovtoPesagem);
	Page<MovtoPesagem> findByNroDocPesagem(Integer nroDocPesagem, Pageable pageable);
	Page<MovtoPesagem> findByNroMovto(Integer nroMovto, Pageable pageable);	
	Page<MovtoPesagem> findByDtMovto (Date dtMovto, Pageable pageable);
	Page<MovtoPesagem> findByNroDocto (String nroDocto, Pageable pageable);
	Page<MovtoPesagem> findByCodEmitente(Integer codEmitente, Pageable pageable);
	Page<MovtoPesagem> findByNrRe(Integer nrRe, Pageable pageable);
}
