package br.coop.integrada.api.pa.domain.model.cadpro;

import lombok.Data;

@Data
public class CadPro{    
    private String uf;
    private String cad;
    private String cpfCnpj;
    private String nome;   
    private Boolean baixado; 
    private String descricao;
}
