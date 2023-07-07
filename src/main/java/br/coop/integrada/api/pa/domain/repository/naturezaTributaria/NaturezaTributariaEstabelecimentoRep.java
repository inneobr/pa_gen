package br.coop.integrada.api.pa.domain.repository.naturezaTributaria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import br.coop.integrada.api.pa.domain.model.naturezaTributaria.NaturezaTributariaEstabelecimento;

public interface NaturezaTributariaEstabelecimentoRep extends JpaRepository<NaturezaTributariaEstabelecimento, Long>, JpaSpecificationExecutor<NaturezaTributariaEstabelecimento>{
	NaturezaTributariaEstabelecimento findByNaturezaTributariaCodigoAndEstabelecimentoCodigo(Integer codigoNatureza, String codigoEstabelecimento);
	Page<NaturezaTributariaEstabelecimento> findByNaturezaTributariaId(Long idNatureza, Pageable pageable);
	void deleteByNaturezaTributariaId(Long idNatureza);
}
