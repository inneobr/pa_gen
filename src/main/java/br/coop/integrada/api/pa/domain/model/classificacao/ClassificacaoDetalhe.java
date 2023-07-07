package br.coop.integrada.api.pa.domain.model.classificacao;

import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.produto.ProdutoReferencia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "classificacao_detalhe")
public class ClassificacaoDetalhe extends AbstractEntity{ //implements Comparable<ClassificacaoDetalhe>{
	
	private static final long serialVersionUID = 1L;

	@JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_classificacao", nullable = false)
    private Classificacao classificacao;
	
	@ComparaObjeto(nome="Teor Classificação Inicial")
    @Column(name = "teor_classificacao_inicial", nullable = false)
    private BigDecimal teorClassificacaoInicial;

	@ComparaObjeto(nome="Teor Classificação Final")
    @Column(name = "teor_classificacao_final", nullable = false)
    private BigDecimal teorClassificacaoFinal;

	@ComparaObjeto(nome="PH Entrada")
    @Column(name = "ph_entrada")
    private BigInteger phEntrada;

	@ComparaObjeto(nome="Percentual de Desconto")
    @Column(name = "percentual_desconto")
    private BigDecimal percentualDesconto;

	@ComparaObjeto(nome="PH Corrigido")
    @Column(name = "ph_corrigido")
    private BigInteger phCorrigido;
	
	@ComparaObjeto(nome="Referência")
    @ManyToOne
    @JoinColumn(name = "id_produto_referencia")
    private ProdutoReferencia produtoReferencia;

    @ComparaObjeto(nome="Taxa Secagem Kg")
    @Digits(integer = 9, fraction = 9)
    @Column(name = "taxa_secagem_quilo")
    private BigDecimal taxaSecagemQuilo;

    @ComparaObjeto(nome="Taxa Secagem Valor")
    @Digits(integer = 9, fraction = 9)
    @Column(name = "taxa_secagem_valor")
    private BigDecimal taxaSecagemValor;
        
    
    public BigDecimal getTeorClassificacaoInicial() {
        if(teorClassificacaoInicial == null) return BigDecimal.ZERO;
        return teorClassificacaoInicial;
    }
    
    public BigDecimal getTeorClassificacaoFinal() {
        if(teorClassificacaoFinal == null) return BigDecimal.ZERO;
        return teorClassificacaoFinal;
    }
    
    public BigDecimal getPercentualDesconto() {
        if(percentualDesconto == null) return BigDecimal.ZERO;
        return percentualDesconto;
    }
    
    public BigDecimal getTaxaSecagemQuilo() {
        if(taxaSecagemQuilo == null) return BigDecimal.ZERO;
        return taxaSecagemQuilo;
    }
    
    public BigDecimal getTaxaSecagemValor() {
        if(taxaSecagemValor == null) return BigDecimal.ZERO;
        return taxaSecagemValor;
    }

}
