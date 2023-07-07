package br.coop.integrada.api.pa.domain.model.rependente;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.domain.model.produto.TipoGmo;
import lombok.Data;

@Data
public class RePendenteTipoGmo {
	private Long id;
	private String idUnico;
	private String tipoGmo;
	private String obsRomaneio;
	private BigDecimal perDeclarada;
	private BigDecimal perTestada;
	private BigDecimal vlKit;
	
	public static RePendenteTipoGmo construir(TipoGmo tipoGmo) {
		RePendenteTipoGmo objDto = new RePendenteTipoGmo();
        BeanUtils.copyProperties(tipoGmo, objDto);
		return objDto;
	}
	
	public static List<RePendenteTipoGmo> construir(List<TipoGmo> tiposGmo) {
		if(CollectionUtils.isEmpty(tiposGmo)) return Collections.emptyList();
		return tiposGmo.stream().map(tipoGmo -> {
			return construir(tipoGmo);
		}).toList();
	}
}
