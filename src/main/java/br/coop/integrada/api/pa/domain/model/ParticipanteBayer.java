package br.coop.integrada.api.pa.domain.model;

import static javax.persistence.EnumType.STRING;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.OrigemEnum;
import lombok.Data;

@Data
@Entity
@Table(name = "participante_bayer")
public class ParticipanteBayer extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "cnpj", nullable = false, unique = true)
    @NotNull(message = "O campo {cnpj} é obrigatório")
    @NotBlank(message = "O campo {cnpj} não pode ser vazio")
    private String cnpj;

    @Column(name = "nome", nullable = false)
    @NotNull(message = "O campo {nome} é obrigatório")
    @NotBlank(message = "O campo {nome} não pode ser vazio")
    private String nome;

    @Column(name = "usuario", nullable = false)
    @NotNull(message = "O campo {usuario} é obrigatório")
    @NotBlank(message = "O campo {usuario} não pode ser vazio")
    private String usuario;

    @JsonProperty(access = Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy H:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
    private Date dataInclusao;

    @Enumerated(STRING)
    @Column(name = "origem", nullable = false)
    @NotNull(message = "O campo {origem} é obrigatório")
    private OrigemEnum origem;
}
