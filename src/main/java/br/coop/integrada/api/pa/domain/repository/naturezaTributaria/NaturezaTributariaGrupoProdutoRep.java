package br.coop.integrada.api.pa.domain.repository.naturezaTributaria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import br.coop.integrada.api.pa.domain.model.naturezaTributaria.NaturezaTributariaGrupoProduto;

public interface NaturezaTributariaGrupoProdutoRep extends JpaRepository<NaturezaTributariaGrupoProduto, Long>, JpaSpecificationExecutor<NaturezaTributariaGrupoProduto>{
	Page<NaturezaTributariaGrupoProduto> findByNaturezaTributariaId(Long idNatureza, Pageable pageable);
	NaturezaTributariaGrupoProduto findByNaturezaTributariaIdAndGrupoProdutoFmCodigo(Long idNatureza, String fmCodigo);
	void deleteByNaturezaTributariaId(Long idNatureza);
}
