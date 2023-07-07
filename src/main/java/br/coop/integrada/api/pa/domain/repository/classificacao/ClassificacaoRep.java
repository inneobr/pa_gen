package br.coop.integrada.api.pa.domain.repository.classificacao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.TipoClassificacaoEnum;
import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
 
public interface ClassificacaoRep extends JpaRepository<Classificacao, Long>, ClassificacaoQueriesRep, JpaSpecificationExecutor<Classificacao> {
 
    @Query(value = """
    		SELECT distinct 'Classificação: '|| c.id ||', Safra: '|| cs.SAFRA ||', FM: '|| gp.fm_codigo||', Estabelecimento: '|| e.codigo ||', Tipo Classificação: ' || tc.tipo_classificacao ||')'   
            FROM CLASSIFICACAO c
            INNER JOIN CLASSIFICACAO_SAFRA cs
                ON cs.ID_CLASSIFICACAO = c.ID
                AND cs.SAFRA IN (:safras)
            INNER JOIN V_CLASSIFICACAO_GRUPO_PRODUTO vcgp
                ON vcgp.ID_CLASSIFICACAO = c.ID
                AND vcgp.ID_GRUPO_PRODUTO IN (:grupoProdutoIds)
            INNER JOIN GRUPO_PRODUTO gp
                ON gp.ID = vcgp.ID_GRUPO_PRODUTO
            INNER JOIN V_CLASSIFICACAO_ESTABELECIMENTO vce 
                ON vce.ID_CLASSIFICACAO  = c.ID
                AND vce.ID_ESTABELECIMENTO  IN (:estabelecimentoIds)
            INNER JOIN ESTABELECIMENTO e
                ON e.ID = vce.ID_ESTABELECIMENTO
            INNER JOIN TIPO_CLASSIFICACAO tc 
    		    ON tc.id = c.id_tipo_classificacao
    		    AND tc.tipo_classificacao = :tipoClassificacao
            WHERE
                c.ID <> :id
                AND c.DATA_INATIVACAO IS NULL
    		""", nativeQuery=true)
    public List<String> buscarPorSafraGrupoProdutoEstabelecimento(Long id, List<Integer> safras, List<Long> grupoProdutoIds, List<Long> estabelecimentoIds, String tipoClassificacao);

    @Query(value = """
            SELECT distinct 'Classificação: '|| c.id ||', FM: '|| gp.fm_codigo||', Estabelecimento: '|| e.codigo ||', Tipo Classificação: ' || tc.tipo_classificacao ||')'   
            FROM CLASSIFICACAO c
            INNER JOIN V_CLASSIFICACAO_GRUPO_PRODUTO vcgp
                ON vcgp.ID_CLASSIFICACAO = c.ID
                AND vcgp.ID_GRUPO_PRODUTO IN (:grupoProdutoIds)
            INNER JOIN GRUPO_PRODUTO gp
                ON gp.ID = vcgp.ID_GRUPO_PRODUTO
            INNER JOIN V_CLASSIFICACAO_ESTABELECIMENTO vce 
                ON vce.ID_CLASSIFICACAO  = c.ID
                AND vce.ID_ESTABELECIMENTO  IN (:estabelecimentoIds)
            INNER JOIN ESTABELECIMENTO e
                ON e.ID = vce.ID_ESTABELECIMENTO
            INNER JOIN TIPO_CLASSIFICACAO tc 
    		    ON tc.id = c.id_tipo_classificacao
    		    AND tc.tipo_classificacao = :tipoClassificacao
            WHERE
                c.ID <> :id
                AND c.DATA_INATIVACAO IS NULL
            """, nativeQuery=true)
    List<String> buscarPorGrupoProdutoEstabelecimento(Long id, List<Long> grupoProdutoIds, List<Long> estabelecimentoIds, String tipoClassificacao);

    @Query(value = """
            SELECT DISTINCT SAFRA
            FROM CLASSIFICACAO_SAFRA cs
            ORDER BY SAFRA desc
            """, nativeQuery=true)
    public List<Integer> buscarSafras();
    
    
    public List<Classificacao> findByStatusIntegracao(StatusIntegracao statusIntegracao);

    @Query(value = """
            SELECT * \s -- cs.safra, c.id, tc.TIPO_CLASSIFICACAO, cd.PH_ENTRADA, cd.TEOR_CLASSIFICACAO_INICIAL, cd.TEOR_CLASSIFICACAO_FINAL , vcgpe.ID_ESTABELECIMENTO, VCGP.ID_GRUPO_PRODUTO, cd.*, c.* 
			FROM CLASSIFICACAO c \s
			JOIN TIPO_CLASSIFICACAO tc ON c.ID_TIPO_CLASSIFICACAO = tc.ID AND tc.DATA_INATIVACAO IS null \s
			JOIN CLASSIFICACAO_DETALHE cd ON cd.ID_CLASSIFICACAO  = c.ID AND cd.DATA_INATIVACAO  IS NULL \s
			JOIN V_CLASSIFICACAO_GRUPO_PRODUTO vcgp  ON c.ID = VCGP.ID_CLASSIFICACAO AND VCGP.DATA_INATIVACAO IS NULL \s
			JOIN V_CLASSIFICACAO_GRUPO_PRODUTO_ESTAB vcgpe ON vcgpe.ID_CLASSIFICACAO_GRUPO_PRODUTO = vcgp.ID \s
			JOIN ESTABELECIMENTO e ON e.ID = vcgpe.ID_ESTABELECIMENTO \s
			LEFT JOIN CLASSIFICACAO_SAFRA cs ON cs.ID_CLASSIFICACAO  = c.ID AND cs.DATA_INATIVACAO  IS NULL \s
			WHERE c.DATA_INATIVACAO  IS NULL \s
			AND tc.TIPO_CLASSIFICACAO = :tipoClassificacao \s
			AND e.CODIGO = :codEstabelecimento \s
			AND VCGP.ID_GRUPO_PRODUTO = :grupoProduto \s
			AND cs.SAFRA = :safra \s
            """, nativeQuery=true)
	public List<Classificacao> buscarTabelaClassificacao(String  tipoClassificacao, String codEstabelecimento, int grupoProduto, Integer safra);
    
    
    @Query(value = """
            SELECT * \s -- cs.safra, c.id, tc.TIPO_CLASSIFICACAO, cd.PH_ENTRADA, cd.TEOR_CLASSIFICACAO_INICIAL, cd.TEOR_CLASSIFICACAO_FINAL , vcgpe.ID_ESTABELECIMENTO, VCGP.ID_GRUPO_PRODUTO, cd.*, c.* 
			FROM CLASSIFICACAO c \s
			JOIN TIPO_CLASSIFICACAO tc ON c.ID_TIPO_CLASSIFICACAO = tc.ID AND tc.DATA_INATIVACAO IS null \s
			JOIN CLASSIFICACAO_DETALHE cd ON cd.ID_CLASSIFICACAO  = c.ID AND cd.DATA_INATIVACAO  IS NULL \s
			JOIN V_CLASSIFICACAO_GRUPO_PRODUTO vcgp  ON c.ID = VCGP.ID_CLASSIFICACAO AND VCGP.DATA_INATIVACAO IS NULL \s
			JOIN V_CLASSIFICACAO_GRUPO_PRODUTO_ESTAB vcgpe ON vcgpe.ID_CLASSIFICACAO_GRUPO_PRODUTO = vcgp.ID \s
			JOIN ESTABELECIMENTO e ON e.ID = vcgpe.ID_ESTABELECIMENTO \s
			LEFT JOIN CLASSIFICACAO_SAFRA cs ON cs.ID_CLASSIFICACAO  = c.ID AND cs.DATA_INATIVACAO  IS NULL \s
			WHERE c.DATA_INATIVACAO  IS NULL \s
			AND tc.TIPO_CLASSIFICACAO = :tipoClassificacao \s
			AND e.CODIGO = :codEstabelecimento \s
			AND VCGP.ID_GRUPO_PRODUTO = :grupoProduto \s
            """, nativeQuery=true)
	public List<Classificacao> buscarTabelaClassificacao(String  tipoClassificacao, String codEstabelecimento, int grupoProduto);
    
    @Query(value = """
			update classificacao set 
				data_integracao = :dataIntegracao
			  where id = :idClassificacao 
			""", nativeQuery=true)
    public void updateStatusIntegracao(Date dataIntegracao, Long idClassificacao);
    
    
    
    
}
