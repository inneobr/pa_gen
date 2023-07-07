package br.coop.integrada.api.pa.domain.modelDto.rependente.desmembramento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.domain.enums.re.pendente.TipoDesmembramentoEnum;
import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteDesmembramento;
import lombok.Data;

@Data
public class RePendenteDesmembramentoDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String produtorFavorecidoCodigo;
	private String produtorFavorecidoNome;
	private BigDecimal percentual;
	private BigDecimal quantidadeQuilos;
	private BigDecimal quantidadeSacas;
	private Boolean cobraKit;
	private Long imovelMatricula;
	private TipoDesmembramentoEnum transferirPela;
	private Long nfProdutor;
	private String serieNfProdutor;
	private Date dataNfProdutor;

	public BigDecimal getPercentual() {
		if(percentual == null) return BigDecimal.ZERO;
		return percentual;
	}
	
	public BigDecimal getQuantidadeQuilos() {
		if(quantidadeQuilos == null) return BigDecimal.ZERO;
		return quantidadeQuilos;
	}
	
	public BigDecimal getQuantidadeSacas() {
		if(quantidadeSacas == null) return BigDecimal.ZERO;
		return quantidadeSacas;
	}
	
	public Boolean getCobraKit() {
		if(cobraKit == null) return false;
		return cobraKit;
	}

	public static RePendenteDesmembramentoDto construir(RePendenteDesmembramento obj) {
		var objDto = new RePendenteDesmembramentoDto();
		BeanUtils.copyProperties(obj, objDto);
		
		if(obj.getProdutorFavorecido() != null) {
			objDto.setProdutorFavorecidoCodigo(obj.getProdutorFavorecido().getCodProdutor());
		}
		
		if(obj.getImovel() != null) {
			Imovel imovel = obj.getImovel();
			objDto.setImovelMatricula(imovel.getMatricula());
		}
		
		return objDto;
	}
	
	public static List<RePendenteDesmembramentoDto> construir(List<RePendenteDesmembramento> objs) {
		if(CollectionUtils.isEmpty(objs)) return Collections.emptyList();
		return objs.stream().map(rePendenteDesmembramento -> {
			return construir(rePendenteDesmembramento);
		}).toList();
	}
}
