package br.coop.integrada.api.pa.domain.repository.classificacao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoDetalhe;

public interface ClassificacaoDetalheRep  extends JpaRepository<ClassificacaoDetalhe, Long>, JpaSpecificationExecutor<ClassificacaoDetalhe> {
	
	@Query(value = """
            SELECT cd.*
			FROM CLASSIFICACAO c
			INNER JOIN TIPO_CLASSIFICACAO tc ON c.ID_TIPO_CLASSIFICACAO = tc.ID AND tc.DATA_INATIVACAO IS null
			INNER JOIN CLASSIFICACAO_DETALHE cd ON cd.ID_CLASSIFICACAO  = c.ID AND cd.DATA_INATIVACAO  IS NULL
			INNER JOIN V_CLASSIFICACAO_GRUPO_PRODUTO vcgp  ON c.ID = VCGP.ID_CLASSIFICACAO AND VCGP.DATA_INATIVACAO IS NULL
			INNER JOIN V_CLASSIFICACAO_ESTABELECIMENTO vce ON vce.ID_CLASSIFICACAO = c.ID AND vce.DATA_INATIVACAO IS NULL 
			INNER JOIN ESTABELECIMENTO e ON e.ID = vce.ID_ESTABELECIMENTO AND e.DATA_INATIVACAO IS NULL 
			INNER JOIN CLASSIFICACAO_SAFRA cs ON cs.ID_CLASSIFICACAO  = c.ID AND cs.DATA_INATIVACAO  IS NULL
			WHERE c.DATA_INATIVACAO  IS NULL
			AND tc.TIPO_CLASSIFICACAO = :tipoClassificacao
			AND e.CODIGO = :codEstabelecimento
			AND VCGP.ID_GRUPO_PRODUTO = :grupoProduto
			AND cs.SAFRA = :safra
            """, nativeQuery=true)
	public List<ClassificacaoDetalhe> buscarTabelaClassificacao(String  tipoClassificacao, String codEstabelecimento, Long grupoProduto, Integer safra);
    
    
    @Query(value = """
            SELECT cd.*
			FROM CLASSIFICACAO c
			JOIN TIPO_CLASSIFICACAO tc ON c.ID_TIPO_CLASSIFICACAO = tc.ID AND tc.DATA_INATIVACAO IS null
			JOIN CLASSIFICACAO_DETALHE cd ON cd.ID_CLASSIFICACAO  = c.ID AND cd.DATA_INATIVACAO  IS NULL
			JOIN V_CLASSIFICACAO_GRUPO_PRODUTO vcgp  ON c.ID = VCGP.ID_CLASSIFICACAO AND VCGP.DATA_INATIVACAO IS NULL
			INNER JOIN V_CLASSIFICACAO_ESTABELECIMENTO vce ON vce.ID_CLASSIFICACAO = c.ID AND vce.DATA_INATIVACAO IS NULL 
			INNER JOIN ESTABELECIMENTO e ON e.ID = vce.ID_ESTABELECIMENTO AND e.DATA_INATIVACAO IS NULL
			WHERE c.DATA_INATIVACAO  IS NULL
			AND tc.TIPO_CLASSIFICACAO = :tipoClassificacao
			AND e.CODIGO = :codEstabelecimento
			AND VCGP.ID_GRUPO_PRODUTO = :grupoProduto
            """, nativeQuery=true)
	public List<ClassificacaoDetalhe> buscarTabelaClassificacao(String tipoClassificacao, String codEstabelecimento, Long grupoProduto);
    
    @Query(value = """
			select o.* from classificacao_detalhe o 
			  where o.id_classificacao = :idClassificacao 
			""", nativeQuery=true)
	public List<ClassificacaoDetalhe> findByClassificacao(Long idClassificacao);	
	
}
