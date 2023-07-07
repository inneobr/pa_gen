package br.coop.integrada.api.pa.domain.model;

import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "historico_generico", indexes = {
        @Index(name = "historico_index", columnList = "tipo, id_registro")
})
public class HistoricoGenerico extends AbstractEntity {

    private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private PaginaEnum paginaEnum;

    @Column(name = "id_registro", nullable = false)
    private Long registro;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "movimento", nullable = false)
    private String movimento;

    @Column(name = "observacao", nullable = false, columnDefinition = "CLOB")
    private String observacao;

}
