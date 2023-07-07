package br.coop.integrada.api.pa.domain.model.parametros;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "historico_parametros_estabelecimento")
public class HistoricoParametroEstabelecimento extends AbstractEntity{
    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    @JoinColumn(name="id_estabelecimento")
    private Estabelecimento estabelecimento;
    
    @Column(name = "campo_alterado")
    private String campoAlterado;
    
    @Column(name = "valor_anterior")
    private String valorAnterior;
    
    @Column(name = "valor_atual")
    private String valorAtual;
    
    @Column(name = "username")
    private String userName;
    
    @Column(name = "data")
    private LocalDate data;
    
    @Column(name = "hora")
    private LocalTime hora;
    
    @Column(name = "id_unico")
    private String idUnico;
}
