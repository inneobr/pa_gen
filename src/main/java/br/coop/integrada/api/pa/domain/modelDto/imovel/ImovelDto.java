package br.coop.integrada.api.pa.domain.modelDto.imovel;

import java.io.Serializable;

import lombok.Data;

@Data
public class ImovelDto implements Serializable {
    private static final long serialVersionUID = 1L;
	private Long matricula;    
    private Boolean tipo;
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
    private Boolean ativo;
    
    
}
