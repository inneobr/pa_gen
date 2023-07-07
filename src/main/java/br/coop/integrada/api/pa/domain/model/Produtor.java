package br.coop.integrada.api.pa.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "produtor")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Produtor extends AbstractEntity {
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "Campo {codProdutor} obrigatório")
    @Column(name = "cod_produtor", nullable = false, unique = true)
    private String codProdutor;
    
    @NotNull(message = "Campo {nome} obrigatório")
    @NotBlank(message = "Campo {nome} obrigatório")
    @Column(name = "nome", nullable = false)
    private String nome;
    
    @Column(name = "abreviacao")
    private String nomeAbreviado;
    
    @Column(name = "cpf_cpj")
    private String cpfCnpj;
    
    @Column(name = "rg_ie")
    private String RgIe;
    
    @Column(name = "natureza")
    private String natureza;
    
    @Column(name = "tipo_produtor")
    private String tipoProdutor;
    
    @Column(name = "telefone")
    private String telefone;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "emite_nota")
    private Boolean emiteNota;
    
    @Column(name = "cod_regional")
    private String codRegional;
    
    @Column(name = "cod_repres")
    private String codRepres;
    
    @Column(name = "tipo_retencao")
    private Integer tipoRetencao;
    
    @Column(name = "classificacao_financeira")
    private String classificacaoFinanceira;
    
    @Column(name = "data_demissao")
    private Date dataDemissao;    

    @Column(name = "data_nascimento")
    private Date dataNascimento;
    
    @Column(name = "num_pessoa_fisica")
    private String numPessoaFisica;
    
    @Column(name = "sexo")
    private String sexo;
    
    @Column(name = "ativo")
    private Boolean ativo;
    
    @Column(name = "bloqueado")
    private Boolean bloqueado;
    
    @Column(name = "cod_transp")
    private String codTransp;
    
    @Column(name = "nome_transp")
    private String nomeTransp;
    
    @Column(name = "participante_bayer")
    private Boolean participanteBayer;
    
    @Column(name = "produtor_dap")
    private Boolean produtorDap;
    
    @Column(name = "cooperativa")
    private Boolean cooperativa;
    
    @Column(name = "endereco")
    private String endereco;
    
    @Column(name = "bairro")
    private String bairro;
    
    @Column(name = "cidade")
    private String cidade;
    
    @Column(name = "estado")
    private String estado;
    
    @Column(name = "estado_civil")
    private String estadoCivil;
    
    @Column(name = "nacionalidade")
    private String nacionalidade;
        
    public Boolean getEmiteNota() {
        if(emiteNota == null) return false;
        return emiteNota;
    }

    public Boolean getAtivo() {
        if(ativo == null) return false;
        return ativo;
    }

    public Boolean getBloqueado() {
        if(bloqueado == null) return false;
        return bloqueado;
    }

    public Boolean getParticipanteBayer() {
        if(participanteBayer == null) return false;
        return participanteBayer;
    }

    public Boolean getProdutorDap() {
        if(produtorDap == null) return false;
        return produtorDap;
    }

    public Boolean getCooperativa() {
        if(cooperativa == null) return false;
        return cooperativa;
    }
    
    public String getCodNome() {
		return codProdutor + " - " + nome;    	
    }
}
