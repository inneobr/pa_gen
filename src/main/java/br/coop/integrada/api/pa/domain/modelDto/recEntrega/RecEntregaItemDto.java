package br.coop.integrada.api.pa.domain.modelDto.recEntrega;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.validation.constraints.Digits;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.pedidoIntegracao.OperacaoEnum;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntregaItem;
import lombok.Data;

@Data
public class RecEntregaItemDto {
	   
	private String codEstabel;

	private Long nrRe;
	
	private String idRe;
	
	private String idItRe;
	
	private String codProduto;

	private String itCodigo;

	private String codRefer;

	@Digits(integer = 7, fraction = 4)
	private BigDecimal pesoLiquido = new BigDecimal(0);

	private BigInteger phEntrada = new BigInteger("0");

	private BigInteger phCorrigido = new BigInteger("0");

	@Digits(integer = 3, fraction = 2)
	private BigDecimal impureza = new BigDecimal(0);

	@Digits(integer = 7, fraction = 4)
	private BigDecimal perDescImpur = new BigDecimal(0);

	@Digits(integer = 7, fraction = 4)
	private BigDecimal qtDescImpur = new BigDecimal(0);

	@Digits(integer = 3, fraction = 2)
	private BigDecimal umidade = new BigDecimal(0);

	@Digits(integer = 3, fraction = 2)
	private BigDecimal perDescUmid = new BigDecimal(0);

	@Digits(integer = 7, fraction = 4)
	private BigDecimal qtDescUmid = new BigDecimal(0);

	@Digits(integer = 3, fraction = 2)
	private BigDecimal chuvAvar = new BigDecimal(0);

	@Digits(integer = 3, fraction = 2)
	private BigDecimal perDescChuv = new BigDecimal(0);

	@Digits(integer = 7, fraction = 4)
	private BigDecimal qtDescChuv = new BigDecimal(0);

	private BigDecimal tbm = new BigDecimal(0);

	@Digits(integer = 7, fraction = 4)
	private BigDecimal qtTbm = new BigDecimal(0);

	private Integer tipo = 0;

	private String bebida;

	@Digits(integer = 6, fraction = 2)
	private BigDecimal cafeEscolha = new BigDecimal(0);

	@Digits(integer = 7, fraction = 4)
	private BigDecimal rendaLiquida = new BigDecimal(0);

	private Integer defeitos = 0; 

	private Boolean normal;

	private Boolean sementeira;

	private Boolean terra;

	private Boolean vagem;

	private String lote;

	@Digits(integer = 7, fraction = 4)
	private BigDecimal rendaLiquidaAtu = new BigDecimal(0);

	@Digits(integer = 7, fraction = 4)
	private BigDecimal pesoLiquidoAtu = new BigDecimal(0);

	@Digits(integer = 6, fraction = 9)
	private BigDecimal vlFiscal = new BigDecimal(0);

	@Digits(integer = 3, fraction = 2)
	private BigDecimal tabTxSecKg = new BigDecimal(0);

	@Digits(integer = 6, fraction = 9)
	private BigDecimal tabTxSecVl;

	private Integer caferrTipo = 0;

	private String caferrBebida;

	@Digits(integer = 6, fraction = 2)
	private BigDecimal caferrCafeEscolha = new BigDecimal(0);

	@Digits(integer = 7, fraction = 4)
	private BigDecimal caferrRendaLiquida = new BigDecimal(0);

	private Integer cafermTipo = 0;

	private String cafermBebida;

	@Digits(integer = 6, fraction = 2)
	private BigDecimal cafermCafeEscolha = new BigDecimal(0);

	@Digits(integer = 7, fraction = 4)
	private BigDecimal cafermRendaLiquida = new BigDecimal(0);

	private Integer cafermDefeitos = 0;

	private Integer caferrDefeitos = 0;

	private String cafermCodRefer;

	private String caferrCodRefer;

	private BigDecimal cafermRendascLiq = new BigDecimal(0);

	@Digits(integer = 3, fraction = 3)
	private BigDecimal cafermRendascEsc = new BigDecimal(0);

	@Digits(integer = 3, fraction = 3)
	private BigDecimal cafermRendascTot = new BigDecimal(0);

	@Digits(integer = 5, fraction = 2)
	private BigDecimal perPen14Acima = new BigDecimal(0);

	private Integer seqNotaEnc;

	@Digits(integer = 3, fraction = 2)
	private BigDecimal percBandinha = new BigDecimal(0);

	@Digits(integer = 12, fraction = 4)
	private BigDecimal rendaSemDescDpi = new BigDecimal(0);

	@Digits(integer = 12, fraction = 4)
	private BigDecimal qtAlocada = new BigDecimal(0);

	@Digits(integer = 8, fraction = 9)
	private BigDecimal tabVlRecepcao = new BigDecimal(0);

	@Digits(integer = 8, fraction = 9)
	private BigDecimal vlRecepcao = new BigDecimal(0);

	@Digits(integer = 8, fraction = 9)
	private BigDecimal qtRecepcao = new BigDecimal(0);

	@Digits(integer = 8, fraction = 9)
	private BigDecimal vlSecagem = new BigDecimal(0);

	@Digits(integer = 7, fraction = 9)
	private BigDecimal qtSecagem = new BigDecimal(0);

	@Digits(integer = 8, fraction = 9)
	private BigDecimal vlPonto = new BigDecimal(0);

	@Digits(integer = 4, fraction = 2)
	private BigDecimal fnt = new BigDecimal(0);
	
	private Integer densidade;
	private Integer seqItemDocum;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dtUltInt;

	private String hrUltInt;
	
	private Boolean logIntegrado;
    
	private OperacaoEnum operacao;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String message;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean integrated;

	public String getCodEstabel() {
	    if(codEstabel == null) return "";
	    return codEstabel;
	}

	public String getIdRe() {
	    if(idRe == null) return "";
	    return idRe;
	}

	public String getIdItRe() {
	    if(idItRe == null) return "";
	    return idItRe;
	}

	public String getCodProduto() {
	    if(codProduto == null) return "";
	    return codProduto;
	}

	public String getItCodigo() {
	    if(itCodigo == null) return "";
	    return itCodigo;
	}

	public String getCodRefer() {
	    if(codRefer == null) return "";
	    return codRefer;
	}

	public String getBebida() {
	    if(bebida == null) return "";
	    return bebida;
	}

	public String getLote() {
	    if(lote == null) return "";
	    return lote;
	}

	public String getCaferrBebida() {
	    if(caferrBebida == null) return "";
	    return caferrBebida;
	}

	public String getCafermBebida() {
	    if(cafermBebida == null) return "";
	    return cafermBebida;
	}

	public String getCafermCodRefer() {
	    if(cafermCodRefer == null) return "";
	    return cafermCodRefer;
	}

	public String getCaferrCodRefer() {
	    if(caferrCodRefer == null) return "";
	    return caferrCodRefer;
	}

	public String getHrUltInt() {
	    if(hrUltInt == null) return "";
	    return hrUltInt;
	}
	
	public Long getNrRe() {
		if(nrRe == null) return 0L;
		return nrRe;
	}
	
	public Integer getTipo() {
	    if(tipo == null) return 0;
	    return tipo;
	}

	public Integer getDefeitos() {
	    if(defeitos == null) return 0;
	    return defeitos;
	}

	public Integer getCaferrTipo() {
	    if(caferrTipo == null) return 0;
	    return caferrTipo;
	}

	public Integer getCafermTipo() {
	    if(cafermTipo == null) return 0;
	    return cafermTipo;
	}

	public Integer getCafermDefeitos() {
	    if(cafermDefeitos == null) return 0;
	    return cafermDefeitos;
	}

	public Integer getCaferrDefeitos() {
	    if(caferrDefeitos == null) return 0;
	    return caferrDefeitos;
	}

	public Integer getSeqNotaEnc() {
	    if(seqNotaEnc == null) return 0;
	    return seqNotaEnc;
	}

	public Integer getSeqItemDocum() {
	    if(seqItemDocum == null) return 0;
	    return seqItemDocum;
	}
	
	public BigInteger getPhEntrada() {
	    if(phEntrada == null) return BigInteger.ZERO;
	    return phEntrada;
	}

	public BigInteger getPhCorrigido() {
	    if(phCorrigido == null) return BigInteger.ZERO;
	    return phCorrigido;
	}
	
	public BigDecimal getPesoLiquido() {
	    if(pesoLiquido == null) return BigDecimal.ZERO;
	    return pesoLiquido;
	}

	public BigDecimal getImpureza() {
	    if(impureza == null) return BigDecimal.ZERO;
	    return impureza;
	}

	public BigDecimal getPerDescImpur() {
	    if(perDescImpur == null) return BigDecimal.ZERO;
	    return perDescImpur;
	}

	public BigDecimal getQtDescImpur() {
	    if(qtDescImpur == null) return BigDecimal.ZERO;
	    return qtDescImpur;
	}

	public BigDecimal getUmidade() {
	    if(umidade == null) return BigDecimal.ZERO;
	    return umidade;
	}

	public BigDecimal getPerDescUmid() {
	    if(perDescUmid == null) return BigDecimal.ZERO;
	    return perDescUmid;
	}

	public BigDecimal getQtDescUmid() {
	    if(qtDescUmid == null) return BigDecimal.ZERO;
	    return qtDescUmid;
	}

	public BigDecimal getChuvAvar() {
	    if(chuvAvar == null) return BigDecimal.ZERO;
	    return chuvAvar;
	}

	public BigDecimal getPerDescChuv() {
	    if(perDescChuv == null) return BigDecimal.ZERO;
	    return perDescChuv;
	}

	public BigDecimal getQtDescChuv() {
	    if(qtDescChuv == null) return BigDecimal.ZERO;
	    return qtDescChuv;
	}

	public BigDecimal getTbm() {
	    if(tbm == null) return BigDecimal.ZERO;
	    return tbm;
	}

	public BigDecimal getQtTbm() {
	    if(qtTbm == null) return BigDecimal.ZERO;
	    return qtTbm;
	}

	public BigDecimal getCafeEscolha() {
	    if(cafeEscolha == null) return BigDecimal.ZERO;
	    return cafeEscolha;
	}

	public BigDecimal getRendaLiquida() {
	    if(rendaLiquida == null) return BigDecimal.ZERO;
	    return rendaLiquida;
	}

	public BigDecimal getRendaLiquidaAtu() {
	    if(rendaLiquidaAtu == null) return BigDecimal.ZERO;
	    return rendaLiquidaAtu;
	}

	public BigDecimal getPesoLiquidoAtu() {
	    if(pesoLiquidoAtu == null) return BigDecimal.ZERO;
	    return pesoLiquidoAtu;
	}

	public BigDecimal getVlFiscal() {
	    if(vlFiscal == null) return BigDecimal.ZERO;
	    return vlFiscal;
	}

	public BigDecimal getTabTxSecKg() {
	    if(tabTxSecKg == null) return BigDecimal.ZERO;
	    return tabTxSecKg;
	}

	public BigDecimal getTabTxSecVl() {
	    if(tabTxSecVl == null) return BigDecimal.ZERO;
	    return tabTxSecVl;
	}

	public BigDecimal getCaferrCafeEscolha() {
	    if(caferrCafeEscolha == null) return BigDecimal.ZERO;
	    return caferrCafeEscolha;
	}

	public BigDecimal getCaferrRendaLiquida() {
	    if(caferrRendaLiquida == null) return BigDecimal.ZERO;
	    return caferrRendaLiquida;
	}

	public BigDecimal getCafermCafeEscolha() {
	    if(cafermCafeEscolha == null) return BigDecimal.ZERO;
	    return cafermCafeEscolha;
	}

	public BigDecimal getCafermRendaLiquida() {
	    if(cafermRendaLiquida == null) return BigDecimal.ZERO;
	    return cafermRendaLiquida;
	}

	public BigDecimal getCafermRendascLiq() {
	    if(cafermRendascLiq == null) return BigDecimal.ZERO;
	    return cafermRendascLiq;
	}

	public BigDecimal getCafermRendascEsc() {
	    if(cafermRendascEsc == null) return BigDecimal.ZERO;
	    return cafermRendascEsc;
	}

	public BigDecimal getCafermRendascTot() {
	    if(cafermRendascTot == null) return BigDecimal.ZERO;
	    return cafermRendascTot;
	}

	public BigDecimal getPerPen14Acima() {
	    if(perPen14Acima == null) return BigDecimal.ZERO;
	    return perPen14Acima;
	}

	public BigDecimal getPercBandinha() {
	    if(percBandinha == null) return BigDecimal.ZERO;
	    return percBandinha;
	}

	public BigDecimal getRendaSemDescDpi() {
	    if(rendaSemDescDpi == null) return BigDecimal.ZERO;
	    return rendaSemDescDpi;
	}

	public BigDecimal getQtAlocada() {
	    if(qtAlocada == null) return BigDecimal.ZERO;
	    return qtAlocada;
	}

	public BigDecimal getTabVlRecepcao() {
	    if(tabVlRecepcao == null) return BigDecimal.ZERO;
	    return tabVlRecepcao;
	}

	public BigDecimal getVlRecepcao() {
	    if(vlRecepcao == null) return BigDecimal.ZERO;
	    return vlRecepcao;
	}

	public BigDecimal getQtRecepcao() {
	    if(qtRecepcao == null) return BigDecimal.ZERO;
	    return qtRecepcao;
	}

	public BigDecimal getVlSecagem() {
	    if(vlSecagem == null) return BigDecimal.ZERO;
	    return vlSecagem;
	}

	public BigDecimal getQtSecagem() {
	    if(qtSecagem == null) return BigDecimal.ZERO;
	    return qtSecagem;
	}

	public BigDecimal getVlPonto() {
	    if(vlPonto == null) return BigDecimal.ZERO;
	    return vlPonto;
	}

	public BigDecimal getFnt() {
		if(fnt == null) return BigDecimal.ZERO;
		return fnt;
	}

	public Boolean getNormal() {
	    if(normal == null) return false;
	    return normal;
	}

	public Boolean getSementeira() {
	    if(sementeira == null) return false;
	    return sementeira;
	}

	public Boolean getTerra() {
	    if(terra == null) return false;
	    return terra;
	}

	public Boolean getVagem() {
	    if(vagem == null) return false;
	    return vagem;
	}

	public Boolean getLogIntegrado() {
	    if(logIntegrado == null) return false;
	    return logIntegrado;
	}

	public static RecEntregaItemDto construir(RecEntregaItem item) {
		var objDto = new RecEntregaItemDto();
		BeanUtils.copyProperties(item, objDto);
		
		if(item.getRecEntrega() != null) {
			objDto.setIdRe(item.getRecEntrega().getId().toString());
			objDto.setIdItRe(item.getRecEntrega().getId().toString());
		}
		
		objDto.setOperacao(OperacaoEnum.WRITE);
		
		return objDto;
	}
	
	public static List<RecEntregaItemDto> construir(List<RecEntregaItem> itens) {
		if(CollectionUtils.isEmpty(itens)) return Collections.emptyList();
		return itens.stream().map(item -> {
			return construir(item);
		}).toList();
	}
}
