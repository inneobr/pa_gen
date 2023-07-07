package br.coop.integrada.api.pa.domain.repository.nfRemessa;



import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.coop.integrada.api.pa.domain.enums.StatusNotaFiscalEnum;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.nfRemessa.NfRemessa;


public interface NfRemessaRep extends JpaRepository<NfRemessa, Long>, JpaSpecificationExecutor<NfRemessa> {

	List<NfRemessa> findByStatusOrderById(StatusNotaFiscalEnum status);
	List<NfRemessa> findByStatusAndPendenciasFiscaisOrderById(StatusNotaFiscalEnum status, Boolean pendenciasFiscais); 
	Page<NfRemessa> findByCodEstabelAndDtCriacaoBetween(String codEstabelecimento, Date dataInicio, Date dataFim, Pageable pageble);
	NfRemessa findByCodEstabelAndIdRecEntrega(String codEstabel, Long idRecEntrega);
	List<NfRemessa> findByCodEstabelAndStatusAndDtCriacaoBetween(String codEstabelecimento, StatusNotaFiscalEnum status, Date dataInicio, Date dataFim);
	List<NfRemessa> findByCodEstabelAndStatusAndPendenciasFiscaisAndDtCriacaoBetween(String codEstabelecimento, StatusNotaFiscalEnum status, Boolean pendenciasFiscais, Date dataInicio, Date dataFim);
	
	@Query(value = """
    		SELECT 
				SUM(AGUARDANDO_INTEGRACAO) as AGUARDANDO_INTEGRACAO,
				SUM(EM_PROCESSAMENTO) as EM_PROCESSAMENTO,
				SUM(AGUARDANDO_TOTVS) as AGUARDANDO_TOTVS,
				SUM(NFE_GERADA) as NFE_GERADA,
				SUM(FUNCAO_NOTA_GERA) as FUNCAO_NOTA_GERA,
				SUM(FUNCAO_NOTA_ESCRITURA) as FUNCAO_NOTA_ESCRITURA,
				(SUM(FUNCAO_NOTA_GERA) + SUM(FUNCAO_NOTA_ESCRITURA)) as TOTAL
			FROM(
				SELECT  
				   CASE STATUS WHEN 'AGUARDANDO_INTEGRACAO' THEN COUNT(1) ELSE 0 END AS AGUARDANDO_INTEGRACAO,
				   CASE STATUS WHEN 'EM_PROCESSAMENTO' THEN COUNT(1) ELSE 0 END AS EM_PROCESSAMENTO,
				   CASE STATUS WHEN 'AGUARDANDO_TOTVS' THEN COUNT(1) ELSE 0 END AS AGUARDANDO_TOTVS,
				   CASE STATUS WHEN 'NFE_GERADA' THEN COUNT(1) ELSE 0 END AS NFE_GERADA,
				   CASE UPPER(FUNCAO_NOTA) WHEN 'GERA' THEN COUNT(1) ELSE 0 END AS FUNCAO_NOTA_GERA,
				   CASE UPPER(FUNCAO_NOTA) WHEN 'ESCRITURA' THEN COUNT(1) ELSE 0 END AS FUNCAO_NOTA_ESCRITURA
				FROM NF_REMESSA nr 
				  WHERE COD_ESTABEL = :codEstabelecimento
				    AND TRUNC(DT_CRIACAO) BETWEEN TRUNC(:dataInicio) AND TRUNC(:dataFim)
				    AND PENDENCIAS_FISCAIS = :pendenciasFiscais
				GROUP BY STATUS, FUNCAO_NOTA
			)R HAVING (SUM(FUNCAO_NOTA_GERA) + SUM(FUNCAO_NOTA_ESCRITURA)) > 0
            """,
            nativeQuery = true)
	Map<String, BigDecimal> buscarIndicadores(String codEstabelecimento, Date dataInicio, Date dataFim, Boolean pendenciasFiscais);
	
}
