package br.coop.integrada.api.pa.domain.model.imovel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "imovel_produtor")
public class ImovelProdutor extends AbstractEntity{
    private static final long serialVersionUID = 1L;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_imovel")
    private Imovel imovel;
    
    @Column(name = "cod_produtor")
    private String codProdutor;
    
    @Column(name = "id_unico")
    private String idUnico;

    @Column(name = "cadpro")
    private Long cadpro;

    @Column(name = "cpf_cnpj")
    private String cpfCnpj;

    private Boolean transferencia; 
    private Boolean baixado;

    public Boolean getTransferencia() {
    	if(transferencia == null) return false;
    	return transferencia;
    }
    
    public Boolean getBaixado() {
    	if(baixado == null) return false;
    	return baixado;
    }
    
}
