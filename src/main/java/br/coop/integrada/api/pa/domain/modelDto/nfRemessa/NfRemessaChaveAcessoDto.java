package br.coop.integrada.api.pa.domain.modelDto.nfRemessa;

import java.io.Serializable;

import lombok.Data;

@Data
public class NfRemessaChaveAcessoDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private String uf;
    private String cnpj;
    private String mod;
    private String pjNatOper;
}
