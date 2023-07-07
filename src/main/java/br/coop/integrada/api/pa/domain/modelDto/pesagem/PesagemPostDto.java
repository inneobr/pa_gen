package br.coop.integrada.api.pa.domain.modelDto.pesagem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.coop.integrada.api.pa.domain.model.Pesagem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class PesagemPostDto implements Serializable{
	
	private static final long serialVersionUID = 1L;

    private Integer nroDocPesagem;
    private String placa;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date data;
    private String hora;
    private String codProduto;
    private String nomeProduto;
    private String codigoMoega;
    private String codEstabelecimento;
    private BigDecimal pesoEntrada;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataEntrada;
    private String horaEntrada;
    private BigDecimal pesoSaida;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataSaida;
    private String horaSaida;
    private BigDecimal pesoLiquido;
    private String codUsuarioEnt;
    private String codUsuarioSai;
    private String nfProdutor;
    private String motorista;
    private String observacao;
    private Integer safra;
    private Integer codProdutor;
    private String nomeProdutor;
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
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataIntegracao;
    
    private Boolean integrado;
    private String tipoPesagem;
    private Boolean desmembrar;
    private Boolean enviado;
    private Long matricula;
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
    private String tipoGmo;
    private Boolean prodPadr;
    private Boolean descarUnid;
    private BigDecimal precoPonto;
    private BigDecimal qtTxSecagemSp;
    private BigDecimal qtTxRecepcaoSp;
    private Boolean naoListado;
    private Boolean logReJava;
    private String codLivre1;
    private Boolean logLivre1;
    private Long intLivre1;
    private BigDecimal decLivre1;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datLivre1;
    private String codLivre2;
	private Boolean logLivre2;
	private Long intLivre2;
    private BigDecimal decLivre2;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datLivre2;

    public static PesagemPostDto construir(PesagemPutDto pesagemPutDto) {
        var pesagemPostDto = new PesagemPostDto();
        BeanUtils.copyProperties(pesagemPutDto, pesagemPostDto);

        return pesagemPostDto;
    }

	public static PesagemPostDto construir(Pesagem obj) {
        var objDto = new PesagemPostDto();
        BeanUtils.copyProperties(obj, objDto);
        return objDto;
	}
}
