package br.coop.integrada.api.pa.domain.model.rependente;


import java.math.BigDecimal;
import lombok.Data;

@Data
public class RePendenteParametrosEstabelecimento {
	private Long id;
    private Integer nrUltBcqs;
    private BigDecimal txFam;
    private Boolean famTerc;
    private Boolean famCoop;
    private Boolean famPc;
    private String credencial;
    private Long nrUltTransf;
    private String situacao;
    private String nomeEst;
    private Integer codEmitente;
    private Boolean ativo;
    private String estado;
    private Boolean entSafraAnt;
    private String renasem;
    private Boolean logRecebeTransgenico;
    private String sigla;
    private String renasemComer;
    private Boolean logMaqCafe;
    private Boolean logSilo;
    private String provador;
    private Boolean demoVlAgregados;
    private BigDecimal vlDolarDecom;
    private BigDecimal vlKgFornecFut;
    private Boolean integraRe;
    private Integer prazoCancNf;
    private Long codImovel;
    private Boolean logDesfazerFixacao;
    private String desMotCancFixacao;
    private String hrAgendaManhaIni;
    private String hrAgendaManhaFim;
    private String hrAgendaTardeIni;
    private String hrAgendaTardeFim;
    private Boolean logEntradaSemTik;    
}
