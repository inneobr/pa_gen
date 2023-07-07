package br.coop.integrada.api.pa.domain.model.rependente;

import java.math.BigDecimal;
import java.util.Date;
import br.coop.integrada.api.pa.domain.enums.StatusPesagem;
import lombok.Data;

@Data
public class RependentePesagemDto {
	private Long id;
	private Integer nroDocPesagem;
    private String codProduto;
    private String nomeProduto;
    private Integer codProdutor;
    private String nomeProdutor;
    private String codEstabelecimento;    
    private Date data;
    private String hora;
    
    private String placa;
    private String motorista;
    
    private Date dataEntrada;
    private String horaEntrada;
    private String codigoMoega;
    
    private BigDecimal pesoEntrada;
    private Integer pesoSaida;
    private Integer pesoLiquido;
    
    private Date dataSaida;
    private String horaSaida;
    
    private String codUsuarioEnt;
    private String codUsuarioSai;
    
    private Long nfProdutor;
    
    private Integer safra;
    private String observacao;
    
    private BigDecimal clUmidade;
    private BigDecimal clImpureza;
    private BigDecimal clChuAvar;
    private BigDecimal clPh;
    private BigDecimal clOut;
    private BigDecimal clTrinca;
    private BigDecimal clAflatoxina;
    private BigDecimal clEsverdeado;
    private BigDecimal clTbm;
    private BigDecimal clPen;
    private BigDecimal clSoja;
    private BigDecimal clPhCorrigido;
    private BigDecimal clBandinha;
    private BigDecimal clPicado;
    private BigDecimal clEnrugado;
    private BigDecimal clChocho;
    private BigDecimal clImaturo;
    private BigDecimal clVerdes;
    private String variedade;
    private Integer codCidade;
    private String nomeCidade;
    private String situacao;
    private Boolean pesAutomatica;
    private Integer codBalanca;
    private Boolean integrado;
    private String tipoPesagem;
    private Boolean desmembrar;
    private Boolean enviado;
    private Long matricula;
    private Long codImovel;
    private String nomeImovel;
    private Boolean impresso;
    private Integer codUnidParc;
    private Integer codClasCam;
    private BigDecimal pesoAmostra;
    private BigDecimal frutoVerde;
    private BigDecimal frutoPequeno;
    private BigDecimal frutoPodre;
    private BigDecimal frutoBolao;
    private BigDecimal industria;
    private BigDecimal impureza;
    private Boolean pesAutomatica2;
    private String ufCaminhao;
    private String ordemCampo;
    private Integer controleSemente;
    private String categoria;
    private String laudoVistoria;
    private String syseed;
    private String leituraUmidade;
    private String dickeyJohn;
    private String tipoGmo;
    private Boolean prodPadr;
    private Boolean descarUnid;
    private BigDecimal precoPonto;
    private BigDecimal qtTxSecagemSp;
    private BigDecimal qtTxRecepcaoSp;
    private Boolean naoListado; 
    private String logReJava;
    private StatusPesagem status;
}
