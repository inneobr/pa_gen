package br.coop.integrada.api.pa.domain.repository.parametros;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.Taxa;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaEstabelecimento;


@Repository
public interface TaxaEstabelecimentoRep extends JpaRepository<TaxaEstabelecimento, Long> {
	
	
	//TaxaEstabelecimento findByTaxaGrupoProdutoIdAndEstabelecimentoCodigo(Long idTaxaGrupoProduto, String codigoEstabelecimento);
	//TaxaEstabelecimento findByTaxaAndEstabelecimentoCodigo(Long idTaxaGrupoProduto, String codigoEstabelecimento);
	
	List<TaxaEstabelecimento> findByStatusIntegracao(StatusIntegracao status);
	List<TaxaEstabelecimento> findByTaxaId(Long taxaId);
	List<TaxaEstabelecimento> findByTaxaIdAndDataInativacaoIsNull(Long taxaId);
	List<TaxaEstabelecimento> findByTaxaIdAndStatusIntegracao(Long idTaxa, StatusIntegracao status);
	TaxaEstabelecimento findByTaxaIdAndEstabelecimentoCodigo(Long idTaxaGrupoProduto, String codigoEstabelecimento);
	Page<TaxaEstabelecimento> findByTaxaIdAndDataInativacaoIsNullOrderByEstabelecimentoCodigo(Long idTaxa, Pageable pageable);
}
