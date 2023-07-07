package br.coop.integrada.api.pa.domain.model.imovel;

import lombok.Setter;
import lombok.Getter;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.NoArgsConstructor;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(
        name = "imovel",
        uniqueConstraints = {
                @UniqueConstraint(name = "KEY_MATRICULA", columnNames = { "matricula" })
        }
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Imovel extends AbstractEntity{
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "O campo {matricula} não pode nulo!")
    @Column(name = "matricula")
    private Long matricula;    
    private Boolean tipo;
    
    @NotNull(message = "O campo {nome} não pode nulo!")
    private String nome;
    private String descricao;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String telefone;
    private String cep;
    private String localizacao;
    private Boolean bloqueado;
    private Long codigo;
    
    @JsonIgnore
    @OneToMany(mappedBy = "imovel", fetch = FetchType.LAZY)
    private List<ImovelProdutor> imoveisProdutores;
    
    public String getMatriculaNome() {
		return matricula + " - " + nome;
    }
}
