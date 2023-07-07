package br.coop.integrada.api.pa.domain.repository.parametros;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaGrupoProduto;

@Repository
public interface TaxaGrupoProdutoRep extends JpaRepository<TaxaGrupoProduto, Long> {

	TaxaGrupoProduto findByTaxaIdAndGrupoProdutoFmCodigo(Long idTaxa, String codigoGrupoProduto);
	List<TaxaGrupoProduto> findByStatusIntegracao(StatusIntegracao status);
	Page<TaxaGrupoProduto> findByTaxaIdAndDataInativacaoIsNull(Long idTaxa, Pageable pageable);
	List<TaxaGrupoProduto> findByTaxaIdAndDataInativacaoIsNull(Long idTaxa);
	List<TaxaGrupoProduto> findByTaxaIdAndStatusIntegracao(Long idTaxa, StatusIntegracao status);
}
