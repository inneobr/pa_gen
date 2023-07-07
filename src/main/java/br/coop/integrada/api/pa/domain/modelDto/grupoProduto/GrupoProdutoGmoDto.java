package br.coop.integrada.api.pa.domain.modelDto.grupoProduto;

import br.coop.integrada.api.pa.domain.modelDto.produto.TipoGmoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrupoProdutoGmoDto {

	@NotNull
	private Long id;
	
	@NotNull
	private TipoGmoDto tipoGmo;

	private Boolean ativo;
}
