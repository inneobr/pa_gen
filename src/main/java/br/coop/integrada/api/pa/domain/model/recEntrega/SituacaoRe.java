package br.coop.integrada.api.pa.domain.model.recEntrega;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.SituacaoReDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "situacao_re",
    uniqueConstraints = {
        @UniqueConstraint(name = "KEY_CODIGO", columnNames = { "codigo" })
    })
public class SituacaoRe extends AbstractEntity {
	
	
	//@NotBlank(message = "Campo {codigo} obrigatório")
	@Column(name = "codigo", unique=true)
	private Long codigo;
	
	//@NotBlank(message = "Campo {descricao} obrigatório")
	@Column(name = "descricao")
	private String descricao;
	
	public static SituacaoRe contruir(SituacaoReDto situacaoReDto) {
		SituacaoRe situacaoRe = new SituacaoRe();
		BeanUtils.copyProperties(situacaoReDto, situacaoRe);
		return situacaoRe;
	}
	
}
