package br.coop.integrada.api.pa.domain.repository.movimentoRe;

import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.coop.integrada.api.pa.domain.model.movimentoRe.MovimentoRe;

public interface MovimentoReRep extends JpaRepository<MovimentoRe, Long>{

	MovimentoRe findByCodEstabelAndIdRe(String codEstabel, Long idRe);
	MovimentoRe findByCodEstabelAndNrRe(String codEstabel, String nrRe);
	MovimentoRe findByCodEstabelAndIdReAndIdMovRe(String codEstabel, Long idRe, String idMovRe);
	Page<MovimentoRe> findByCodEstabel(String codEstabel, Pageable pageble);
	Page<MovimentoRe> findByCodEstabelAndDataBetween(String codEstabelecimento, Date dataInicio, Date dataFim, Pageable pageble);
	MovimentoRe findByIdMovRe(String idMovRe);
	Page<MovimentoRe> findByNrRe(Long nrRe, Pageable pageble);

}
