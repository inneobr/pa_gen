package br.coop.integrada.api.pa.domain.model.parametros.taxa;

import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

@Data
@Entity
@Table(name = "taxa_carencia_armazenagem")
public class TaxaCarenciaArmazenagem extends AbstractEntity {
	
	@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_taxa", nullable = false)
    private Taxa taxa;

    @Column(name = "data_inicial")
    @ComparaObjeto(nome = "Data Inicial")
    private Date dataInicial;

    @Column(name = "data_final")
    @ComparaObjeto(nome = "Data final")
    private Date dataFinal;

    @Column(name = "quantidade_dias_carencia")
    @ComparaObjeto(nome = "Quantidade dias carencia")
    private Integer quantidadeDiasCarencia;
}
