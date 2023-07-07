package br.coop.integrada.api.pa.domain.repository.parametros;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemAvariadoRep  extends JpaRepository<ItemAvariado, Long>, ItemAvariadoQueriesRep, JpaSpecificationExecutor<ItemAvariado> {

    @Query(value = """
            SELECT DISTINCT 'ITEM AVARIADO: ' || ia.DESCRICAO || ',TIPO VALIDACAO: ' || ia.CAMPO_VALIDACAO  || ', FM: ' || gp.FM_CODIGO || ', PRODUTO: ' || p.COD_ITEM || ', ESTABELECIMENTO: ' || e.CODIGO AS INFORMACOES
			FROM ITEM_AVARIADO ia
			INNER JOIN ITEM_AVARIADO_DETALHE iad
			    ON iad.ID_ITEM_AVARIADO = ia.ID
			INNER JOIN PRODUTO p
			    ON p.ID = iad.ID_PRODUTO
			    AND p.ID_GRUPO_PRODUTO IN (:grupoProdutoIds)
			INNER JOIN GRUPO_PRODUTO gp
			    ON gp.ID = p.ID_GRUPO_PRODUTO
			INNER JOIN ITEM_AVARIADO_ESTABELECIMENTO iae
			    ON iae.ID_ITEM_AVARIADO = ia.ID
			    AND iae.ID_ESTABELECIMENTO IN (:estabelecimetnoIds)
			INNER JOIN ESTABELECIMENTO e
			    ON e.ID = iae.ID_ESTABELECIMENTO
			WHERE
			    ia.CAMPO_VALIDACAO = :tipoValidacao
			    AND ia.ID <> :id
			    AND ia.DATA_INATIVACAO IS NULL
            """, nativeQuery=true)
    public List<String> findByTipoGrupoEstabelecimento(Long id, String tipoValidacao, List<Long> grupoProdutoIds, List<Long> estabelecimetnoIds);

    public List<ItemAvariado> findByStatusIntegracao(StatusIntegracao statusIntegracao);
}