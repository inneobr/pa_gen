package br.coop.integrada.api.pa.domain.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.enums.ItemAvariadoValidacaoEnum;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;

@Repository
public interface EstabelecimentoRep extends JpaRepository<Estabelecimento, Long>, EstabelecimentoQueriesRep, JpaSpecificationExecutor<Estabelecimento> {
    List<Estabelecimento> findByCodigoContainingIgnoreCaseOrNomeFantasiaContainingIgnoreCaseOrderByNomeFantasiaAsc(String codigo, String nome, Pageable pageable);  
    List<Estabelecimento> findByCodigoOrNomeFantasiaContainingIgnoreCaseAndCodigoRegionalOrderByNomeFantasiaAsc(String codigo, String nome, String regional, Pageable pageable); 
	Page<Estabelecimento> findByCodigoRegional(String codigoRegional, Pageable pageable);	
	List<Estabelecimento> findByCodigoRegionalOrderByCodigoAsc(String codigoRegional);
	List<Estabelecimento> findByDataInativacaoNullOrderByCodigoAsc();	
	Page<Estabelecimento> findByDataInativacaoNull(Pageable pageable);
	Estabelecimento findByIdOrderByCodigoAsc(Long id);
	Estabelecimento findByIdOrCodigo(Long id, String codigo);
	Estabelecimento findByCnpj(String cnpj);
	
	@Query("SELECT e FROM Estabelecimento e WHERE e.codigo = e.codigoRegional")
	List<Estabelecimento> listarRegionaisAndDataInativacaoNull();
	
	Estabelecimento findByCodigo(String codigo);

	Estabelecimento findByCodigoIgnoreCase(String codigo);
	
	@Query("SELECT e FROM Estabelecimento e WHERE e.codigo = e.codigoRegional AND e.codigo = ?1")
	List<Estabelecimento> listarRegionaisPorCodigo(String codigo);

	@Query(value = """
				SELECT DISTINCT
					E.*
				FROM ESTABELECIMENTO E
				LEFT JOIN (
					SELECT
						SCE.ID_SAFRA_COMPOSTA,
						SCE.ID_ESTABELECIMENTO
					FROM SAFRA_COMPOSTA_ESTABELECIMENTO SCE
					INNER JOIN SAFRA_COMPOSTA SC
						ON SC.ID = SCE.ID_SAFRA_COMPOSTA
						AND SC.TIPO_SAFRA LIKE :tipoSafra
						AND SC.ID_TIPO_PRODUTO = :idTipoProduto
						AND SC.DATA_INATIVACAO IS NULL
					WHERE
						SCE.DATA_INATIVACAO IS NULL
				) TEMP_SC
					ON TEMP_SC.ID_ESTABELECIMENTO = E.ID
				INNER JOIN PARAMETROS_ESTABELECIMENTO PE
					ON PE.ID_ESTABELECIMENTO = E.ID
					AND PE.LOG_SILO = 1
					AND PE.DATA_INATIVACAO IS NULL
				WHERE
					E.DATA_INATIVACAO IS NULL
					AND TEMP_SC.ID_SAFRA_COMPOSTA IS NULL
				ORDER BY
					E.NOME_FANTASIA
			""", nativeQuery = true)
	List<Estabelecimento> buscarAtivoSemVinculoComSafraComposta(String tipoSafra, Long idTipoProduto);

	@Query(value = """
				SELECT
					E.*
				FROM SAFRA_COMPOSTA_ESTABELECIMENTO SCE
				INNER JOIN ESTABELECIMENTO E
					ON E.ID = SCE.ID_ESTABELECIMENTO
				WHERE
					SCE.ID_SAFRA_COMPOSTA = :idSafraComposta
					AND SCE.DATA_INATIVACAO IS NULL
            """,
			countQuery = """
				SELECT
					COUNT(E.ID)
				FROM SAFRA_COMPOSTA_ESTABELECIMENTO SCE
				INNER JOIN ESTABELECIMENTO E
					ON E.ID = SCE.ID_ESTABELECIMENTO
				WHERE
					SCE.ID_SAFRA_COMPOSTA = :idSafraComposta
					AND SCE.DATA_INATIVACAO IS NULL
            """,
			nativeQuery = true)
	Page<Estabelecimento> buscarEstabelecimentosPorIdSafraComposta(Long idSafraComposta, Pageable pageable);
	
	@Query(value = """
			SELECT E.*FROM ESTABELECIMENTO E 
			JOIN PARAMETROS_ESTABELECIMENTO P ON E.ID = P.ID_ESTABELECIMENTO 
			WHERE P.LOG_SILO = 1 
			AND P.DATA_INATIVACAO IS NULL 
			AND E.DATA_INATIVACAO IS NULL 
			AND E.ID NOT IN ( 
				SELECT N.ID_ESTABELECIMENTO 
				FROM NATUREZA_OPERACAO_ESTABELECIMENTO N 
				WHERE DATA_INATIVACAO IS NULL)
			ORDER BY E.CODIGO
        """,
		nativeQuery = true)
	List<Estabelecimento> buscarEstabelecimentoPorParametroEstabelecimentoAndNaturezaOperacaoAndSilo();
	
	@Query(value = """
			SELECT E.*
			FROM ESTABELECIMENTO E 
			JOIN PARAMETROS_ESTABELECIMENTO P 
			ON E.ID = P.ID_ESTABELECIMENTO 
			WHERE P.LOG_SILO = 1 
			AND P.DATA_INATIVACAO IS NULL 
			AND E.DATA_INATIVACAO IS NULL 
			ORDER BY E.CODIGO
        """,
		nativeQuery = true)
	List<Estabelecimento> buscarEstabelecimentoPorParametroEstabelecimentoAndSilo();
	
	@Query(value = """
			SELECT DISTINCT E.* FROM ESTABELECIMENTO E 
			JOIN PARAMETROS_ESTABELECIMENTO P ON E.ID = P.ID_ESTABELECIMENTO 
			WHERE P.LOG_SILO = 1 
			AND P.DATA_INATIVACAO IS NULL 
			AND E.DATA_INATIVACAO IS NULL 
			AND E.ID NOT IN ( 
				SELECT PUE.ID_ESTABELECIMENTO 
				FROM PARAMETROS_USUARIO_ESTABELECIMENTO PUE 
				WHERE PUE.ID_USUARIO = :idUsuario
				AND DATA_INATIVACAO IS NULL)
			ORDER BY E.CODIGO""",
					nativeQuery = true
		)	
		List<Estabelecimento> buscarEstabelecimentosDisponiveisSilodoUsuario(Long idUsuario);
	
		@Query(value = """
				SELECT E.* FROM ESTABELECIMENTO E
				JOIN PARAMETROS_ESTABELECIMENTO PE
				ON E.ID = PE.ID_ESTABELECIMENTO 
				WHERE PE.LOG_SILO = 1
				AND PE.DATA_INATIVACAO IS NULL
				AND E.DATA_INATIVACAO IS NULL 				
				ORDER BY E.CODIGO""",
						nativeQuery = true
		)	
		List<Estabelecimento> findBySiloNotNaturezaOperacao();
		
		
		@Query(value = """
				SELECT E.* FROM ESTABELECIMENTO E
				JOIN PARAMETROS_ESTABELECIMENTO PE
				ON E.ID = PE.ID_ESTABELECIMENTO 
				WHERE PE.LOG_SILO = 1
				AND PE.DATA_INATIVACAO IS NULL
				AND E.DATA_INATIVACAO IS NULL 
				AND E.ID NOT IN ( 
					SELECT N.ID_ESTABELECIMENTO 
					FROM NATUREZA_TRIBUTARIA_ESTABELECIMENTO N
					WHERE DATA_INATIVACAO IS NULL 
				)
				ORDER BY E.CODIGO""",
						nativeQuery = true
		)	
		List<Estabelecimento> findByNaturezaTributariaSilo();
		
		
		@Query(value = """
			SELECT E.* FROM ESTABELECIMENTO E
			JOIN PARAMETROS_ESTABELECIMENTO PE
			ON E.ID = PE.ID_ESTABELECIMENTO
			WHERE PE.LOG_SILO = 1
			AND PE.DATA_INATIVACAO IS NULL
			AND E.DATA_INATIVACAO IS NULL
			AND E.ID NOT IN (
			    SELECT DISTINCT IAE.ID_ESTABELECIMENTO
			    FROM ITEM_AVARIADO IA
			     JOIN ITEM_AVARIADO_ESTABELECIMENTO IAE ON IA.ID = IAE.ID_ITEM_AVARIADO
			     JOIN ITEM_AVARIADO_DETALHE IAD ON IAD.ID_ITEM_AVARIADO = IA.ID
			     JOIN PRODUTO P ON P.ID = IAD.ID_PRODUTO
			    WHERE IAE.DATA_INATIVACAO IS NULL
			      AND IA.DATA_INATIVACAO IS NULL
			      AND IA.CAMPO_VALIDACAO = :tipoValidacao
			      AND P.ID_GRUPO_PRODUTO = :grupoProduto
			)
			ORDER BY E.CODIGO""",
				nativeQuery = true
		)	
		List<Estabelecimento> findBySiloNotItemAvariado(String tipoValidacao, Long grupoProduto);
}
