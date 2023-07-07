package br.coop.integrada.api.pa.domain.model.integration;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

import br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.integration.SituacaoIntegracaoLogEnum;
import br.coop.integrada.api.pa.domain.enums.integration.TipoIntegracaoLogEnum;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "integracao_log")
public class IntegracaoLog extends AbstractEntity {

    private static final long serialVersionUID = 1L;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "pagina", nullable = false)
    private PaginaEnum paginaEnum;
	
	@Enumerated(EnumType.STRING)
	@Column(name="funcionalidade", nullable = false)
	private FuncionalidadePaginaEnum funcionalidade;
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo", nullable = false)
	private TipoIntegracaoLogEnum tipo;
	

	@Enumerated(EnumType.STRING)
	@Column(name="situacao", nullable = false)
	private SituacaoIntegracaoLogEnum situacao;

	@Column(name="total_registros", nullable = false)	
	private Integer totalRegistros;
	
	@Lob
	private String payload;
	
	@Lob
	private String observacao;
	
	public static IntegracaoLog construirLogRequest(PaginaEnum pagina, FuncionalidadePaginaEnum funcionalidade) {
		IntegracaoLog integracaoLog = new IntegracaoLog();
		integracaoLog.setPaginaEnum(pagina);
		integracaoLog.setTotalRegistros(0);
		integracaoLog.setFuncionalidade(funcionalidade);
		integracaoLog.setTipo(TipoIntegracaoLogEnum.REQUEST);
		integracaoLog.setSituacao(SituacaoIntegracaoLogEnum.SUCESSO);
		integracaoLog.setObservacao("Integração solicitada com sucesso.");
		return integracaoLog;
	}
	
	public static IntegracaoLog construirLogResponse(
			PaginaEnum pagina, FuncionalidadePaginaEnum funcionalidade, SituacaoIntegracaoLogEnum situacao, Integer registros, String observacao) {
		IntegracaoLog integracaoLog = new IntegracaoLog();
		integracaoLog.setPaginaEnum(pagina);
		integracaoLog.setObservacao(observacao);
		integracaoLog.setTotalRegistros(registros);
		integracaoLog.setFuncionalidade(funcionalidade);
		integracaoLog.setTipo(TipoIntegracaoLogEnum.RESPONSE);
		integracaoLog.setSituacao(situacao);
		return integracaoLog;
	}
	
	public void registrarFalha(String mensagem) {
		this.setSituacao(SituacaoIntegracaoLogEnum.FALHA);
		this.setObservacao(mensagem);
	}
	
}
