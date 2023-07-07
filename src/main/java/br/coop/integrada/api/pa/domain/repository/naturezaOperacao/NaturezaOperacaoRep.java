package br.coop.integrada.api.pa.domain.repository.naturezaOperacao;

import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface NaturezaOperacaoRep extends JpaRepository<NaturezaOperacao, Long>, NaturezaOperacaoQueriesRep, JpaSpecificationExecutor<NaturezaOperacao> {
	NaturezaOperacao findByCodGrupo(Integer codGrupo);
	Page<NaturezaOperacao> findByDataInativacaoNull(Pageable pageable);

	@Query(value = """
			SELECT
				NATOPER.*
			FROM NATUREZA_OPERACAO NATOPER
			INNER JOIN NATUREZA_OPERACAO_ESTABELECIMENTO NOE
				ON NOE.ID_NATUREZA_OPERACAO = NATOPER.ID
			INNER JOIN ESTABELECIMENTO E
				ON E.ID = NOE.ID_ESTABELECIMENTO
				AND E.CODIGO = :codigoEstabelecimento
			""", nativeQuery = true)
	NaturezaOperacao buscarPorEstabelecimento(String codigoEstabelecimento);
	NaturezaOperacao findByCodGrupoOrDescricao(Integer codGrupo, String descricao);
}
