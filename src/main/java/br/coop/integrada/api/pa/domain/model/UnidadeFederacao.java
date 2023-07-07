package br.coop.integrada.api.pa.domain.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(
		name = "unidade_federacao"
		/*uniqueConstraints = {
				@UniqueConstraint(
						name = "KEY_UnidadeFederacao" ,
						columnNames = { "estado", "codigo_ibge" }
				)
		}*/
	  )
public class UnidadeFederacao extends AbstractEntity {

    @NotNull(message = "O campo {estado} é obrigatório!")
    @NotBlank(message = "O campo {estado} não pode ser vazio!")
    @Column(name = "estado", unique = true)
    private String estado;

    @NotNull(message = "O campo {estadoNome} é obrigatório!")
    @NotBlank(message = "O campo {estadoNome} não pode ser vazio!")
    @Column(name = "estado_nome", unique = true)
    private String estadoNome;

    @NotNull(message = "O campo {codigoIbge} é obrigatório!")
    @NotBlank(message = "O campo {codigoIbge} não pode ser vazio!")
    @Column(name = "codigo_ibge", unique = true)
    private String codigoIbge;
}
