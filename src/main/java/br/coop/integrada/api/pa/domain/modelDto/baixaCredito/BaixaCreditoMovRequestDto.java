package br.coop.integrada.api.pa.domain.modelDto.baixaCredito;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class BaixaCreditoMovRequestDto implements Serializable {
	private static final long serialVersionUID = 1L;

    private String codEstabel;
    private Long nrRe;
    private Long idRe;
    private String idMovtoBxaCred;
    private Date dtMovto;
    private String hrMovto;
    private String codUsuario;
    private String transacao;
    private String observacao;
    private BigDecimal idTransacao;
    private String codAutenticTransacao;
    private BigDecimal qtdade;
	private Boolean logIntegrado;
}
