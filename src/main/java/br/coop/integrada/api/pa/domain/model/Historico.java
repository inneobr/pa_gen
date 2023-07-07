package br.coop.integrada.api.pa.domain.model;

import br.coop.integrada.api.pa.domain.modelDto.historico.HistoricoDto;
import lombok.Setter;
import lombok.Getter;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(
		name = "historico",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "KEY_HISTORICO" ,
						columnNames = { "nro_doc_pesagem", "cod_estabelecimento", "cod_usuario", "data", "hora" }
				)
		}
)
public class Historico extends AbstractEntity{
	private static final long serialVersionUID = 1L;

	@NotNull(message = "Campo {nroDocPesagem} obrigatório")
	@Column(name = "nro_doc_pesagem", nullable = false)
    private Integer nroDocPesagem;

	@NotNull(message = "Campo {codEstabelecimento} obrigatório")
	@NotBlank(message = "Campo {codEstabelecimento} obrigatório")
	@Column(name = "cod_estabelecimento", nullable = false)
	private String codEstabelecimento;

	@NotNull(message = "Campo {codUsuario} obrigatório")
	@NotBlank(message = "Campo {codUsuario} obrigatório")
	@Column(name = "cod_usuario", nullable = false)
    private String codUsuario;

	@NotNull(message = "Campo {data} obrigatório")
	@Column(name = "data", nullable = false)
    private Date data;

	@NotNull(message = "Campo {hora} obrigatório")
	@NotBlank(message = "Campo {hora} obrigatório")
	@Column(name = "hora", nullable = false)
    private String hora;
	
	@Column(name = "transacao")
    private String transacao;
	
	@Column(name = "observacao", columnDefinition = "CLOB")
    private String observacao;

	@NotNull(message = "Campo {safra} obrigatório")
	@Column(name = "safra", nullable = false)
    private Integer safra;

	public static Historico construir(HistoricoDto historicoDto) {
		var obj = new Historico();
		BeanUtils.copyProperties(historicoDto, obj);

		return obj;
	}
}
