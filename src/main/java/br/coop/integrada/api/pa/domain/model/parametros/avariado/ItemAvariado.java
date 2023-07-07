package br.coop.integrada.api.pa.domain.model.parametros.avariado;

import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;
import br.coop.integrada.api.pa.domain.enums.ItemAvariadoValidacaoEnum;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import java.util.List;

@Data
@Entity
@Table(name = "item_avariado")
public class ItemAvariado extends AbstractEntity {

	@ComparaObjeto(nome = "Descrição")
    @Column(name = "descricao", nullable = false)
    private String descricao;

	@ComparaObjeto(nome = "Campo de validação")
    @Enumerated(EnumType.STRING)
    @Column(name = "campo_validacao")
    private ItemAvariadoValidacaoEnum campoValidacao;

    @OneToMany(mappedBy = "itemAvariado", orphanRemoval = true)
    @Where(clause = "data_inativacao is null")
    private List<ItemAvariadoDetalhe> detalhes;

    @OneToMany(mappedBy = "itemAvariado", orphanRemoval = true)
    @Where(clause = "data_inativacao is null")
    private List<ItemAvariadoEstabelecimento> estabelecimentos;
}
