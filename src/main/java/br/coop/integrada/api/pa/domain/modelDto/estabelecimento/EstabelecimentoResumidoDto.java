package br.coop.integrada.api.pa.domain.modelDto.estabelecimento;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class EstabelecimentoResumidoDto {

	private Long id;
	
	@NotBlank(message = "Campo {codigo} obrigatório")
	private String codigo;

	@NotBlank(message = "Campo {nomeFantasia} obrigatório")
	private String nomeFantasia;
	
	private Integer idRegistro;
	
	private String codigoRegional;
	
    public String getCodNome() {
        return codigo + " - "+ nomeFantasia;
    }

	public static EstabelecimentoResumidoDto construir(Estabelecimento obj) {
		var objDto = new EstabelecimentoResumidoDto();
		BeanUtils.copyProperties(obj, objDto);

		return objDto;
	}

	public static List<EstabelecimentoResumidoDto> construir(List<Estabelecimento> objs) {
		if(objs == null) return new ArrayList<>();
		return objs.stream().map(estabelecimento -> {
			return EstabelecimentoResumidoDto.construir(estabelecimento);
		}).toList();
	}
}
	