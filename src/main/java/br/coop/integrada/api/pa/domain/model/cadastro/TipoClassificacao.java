package br.coop.integrada.api.pa.domain.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;
import br.coop.integrada.api.pa.domain.enums.TipoClassificacaoEnum;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "tipo_classificacao")
public class TipoClassificacao extends AbstractEntity{
	private static final long serialVersionUID = 1L;	

	@Enumerated(EnumType.STRING)
	@Column(name="tipo_classificacao", unique = true, nullable = false)
	private TipoClassificacaoEnum tipoClassificacao;
	
	@ComparaObjeto(nome="Resultado do Desconto")
	@Column(name="resultado_desconto")
	private Boolean resultadoDesconto;
	
	@ComparaObjeto(nome="Resultado Taxa Secagem Kg")
	@Column(name="resultado_taxa_secagem_kg")
	private Boolean resultadoTaxaSecagemKg;
	
	@ComparaObjeto(nome="Resultado Taxa Secagem Valor")
	@Column(name="resultado_taxa_secagem_valor")
	private Boolean resultadoTaxaSecagemValor;
	
	@ComparaObjeto(nome="Teor por Faixa")
	@Column(name="teor_por_faixa")
	private Boolean teorPorFaixa;
	
	@ComparaObjeto(nome="Controle Por Safra")
	@Column(name="controle_safra")
	private Boolean controlePorSafra;
	
	@ComparaObjeto(nome="PH")
	@Column(name="ph")
	private Boolean ph;
	
	@ComparaObjeto(nome="PH Corrigido")
	@Column(name="ph_corrigido")
	private Boolean phCorrigido;
	
	@ComparaObjeto(nome="Referência")
	@Column(name="referencia")
	private Boolean referencia;
	
	@Transient
	public String getTipo()
	{
		if(this.tipoClassificacao != null)
			return this.tipoClassificacao.getDescricao();
		
		return null;
	}
	
	/*@Transient
	public String getDescricaoStatusIntegracao() {
		if(this.getStatusIntegracao() != null)
			return this.getStatusIntegracao().getDescricao();
		
		return null;
	}*/
	
		
}
