package br.coop.integrada.api.pa.domain.repository.semente;

import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import br.coop.integrada.api.pa.domain.modelDto.semente.VerificaTotleranciaRecebimentoDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SementeClasseRep extends JpaRepository<SementeClasse, Long>, SementeClasseQueriesRep, JpaSpecificationExecutor<SementeClasse> {

    SementeClasse findByDescricaoIgnoreCaseAndDataInativacaoIsNull(String descricao);

    SementeClasse findByCodigo(Long codigo);

    List<SementeClasse> findByCodigoAndDataInativacaoIsNullOrDescricaoIgnoreCaseAndDataInativacaoIsNull(Long codigo, String descricao);

    List<SementeClasse> findByDescricaoContainingIgnoreCaseAndDataInativacaoIsNull(String codigoOuDescricao, Pageable pageable);

	List<SementeClasse> findByCodigoOrDescricaoIgnoreCase(Long codigo, String descricao);
	
	/*@Query(value = """
				SELECT sli.QT_PROD_ESPERADA 
				FROM SEMENTE_CAMPO sc
				INNER JOIN SEMENTE_LAUDO_INSPECAO sli 
				ON sc.ID = sli.ID_CLASSE
				WHERE sc.ID_ESTABELECIMENTO = :codEstabelecimento
				AND sc.SAFRA = :safra
				AND sc.ID_GRUPO_PRODUTO = :grupoProduto
				AND sc.NR_ORD_CAMPO = :numeroOrdemCampo
				AND sli.NR_LAUDO = :numeroLaudo
			""", nativeQuery = true)
	BigDecimal buscarQtdEsperada(VerificaTotleranciaRecebimentoDto input);*/
}
