package br.coop.integrada.api.pa.domain.repository.parametros;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.parametros.safra.composta.SafraComposta;
import br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.SafraCompostaComGrupoProdutoDto;

@Repository
public interface SafraCompostaRep extends JpaRepository<SafraComposta, Long>, SafraCompostaQueriesRep, JpaSpecificationExecutor<SafraComposta> {

    @Query(value = """
                SELECT DISTINCT
                    'ID Agrupamento: ' || SC.ID || ', Nome: ' || SC.NOME_SAFRA || ', Tipo Safra: ' || SC.TIPO_SAFRA || ', Estabelecimento: ' || E.NOME_FANTASIA 
                FROM SAFRA_COMPOSTA SC
                INNER JOIN SAFRA_COMPOSTA_ESTABELECIMENTO SCE
                    ON SCE.ID_SAFRA_COMPOSTA = SC.ID
                    AND SCE.DATA_INATIVACAO IS NULL
                INNER JOIN ESTABELECIMENTO E
                    ON E.ID = SCE.ID_ESTABELECIMENTO
                    AND E.CODIGO IN (:codigoEstabelecimentos)
                WHERE
                    SC.TIPO_SAFRA LIKE :tipoSafra
                    AND SC.ID_TIPO_PRODUTO = :idTipoProduto
                    AND SC.ID <> :idSafraComposta
                    AND SC.DATA_INATIVACAO IS NULL
            """, nativeQuery = true)
    List<String> getSafrasCompostasComMesmoEstabelecimentos(String tipoSafra, Long idTipoProduto, List<String> codigoEstabelecimentos, Long idSafraComposta);

    @Query(nativeQuery = true)
    List<SafraCompostaComGrupoProdutoDto> safrasCompostasComGrupoProduto();

    @Query(value = """
            SELECT
                SC.*
            FROM SAFRA_COMPOSTA SC
            INNER JOIN SAFRA_COMPOSTA_ESTABELECIMENTO SCE
            	ON SCE.ID_SAFRA_COMPOSTA  = SC.ID
            	AND SCE.DATA_INATIVACAO IS NULL
            INNER JOIN ESTABELECIMENTO E
                ON E.ID = SCE.ID_ESTABELECIMENTO
                AND E.CODIGO LIKE :codigoEstabelecimento
            WHERE
                SC.DATA_INATIVACAO IS NULL
                AND SC.ID_TIPO_PRODUTO = :idTipoProduto
            """, nativeQuery = true)
    List<SafraComposta> buscarPorEstabelecimentoETipoProduto(String codigoEstabelecimento, Long idTipoProduto);
    
    List<SafraComposta> findByStatusIntegracao(StatusIntegracao status);
}
