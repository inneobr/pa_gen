package br.coop.integrada.api.pa.domain.modelDto.produtor;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProdutorDto implements Serializable{ 
    
    private static final long serialVersionUID = 1L;
       
    @NotNull(message = "Campo {codProdutor} obrigatório")
    private String codProdutor;
    
    @NotNull(message = "Campo {nome} obrigatório")
    @NotBlank(message = "Campo {nome} obrigatório")
    private String nome;  
    private String nomeAbreviado;
    private String cpfCnpj;
    private String RgIe;
    private String natureza;
    private String tipoProdutor;
    private String telefone;
    private String email;
    private Boolean emiteNota;
    private String codRegional;
    private String codRepres;
    private Integer tipoRetencao;
    private String classificacaoFinanceira;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataDemissao;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataNascimento;
    private String numPessoaFisica;
    private String sexo;
    
    private Boolean ativo;
    private Boolean bloqueado;
    private String codTransp;
    private String nomeTransp;
    private Boolean participanteBayer;
    private Boolean produtorDap;
    private Boolean cooperativa;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String estadoCivil;
    private String nacionalidade;
	private IntegrationOperacaoEnum operacao;

}
