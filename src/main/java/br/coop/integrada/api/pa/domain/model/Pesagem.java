package br.coop.integrada.api.pa.domain.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.StatusPesagem;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.PesagemPostDto;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.PesagemPutDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "pesagem", 
uniqueConstraints = {@UniqueConstraint(name = "key_un_pesagem" , columnNames = { "nro_doc_pesagem", "cod_estabelecimento", "safra" }) })
public class Pesagem extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "Campo {nroDocPesagem} obrigatório")
	@Column(name = "nro_doc_pesagem", nullable = false)
    private Integer nroDocPesagem;

	@Column(name = "placa")
    private String placa;

	@Temporal(TemporalType.DATE)
	@Column(name = "data")
    private Date data;

	@Column(name = "hora")
    private String hora;
	
	@Column(name = "cod_produto")
    private String codProduto;

	@Column(name = "nome_produto")
    private String nomeProduto;
	
	@Column(name = "codigo_moega")
    private String codigoMoega;

    @NotNull(message = "Campo {codEstabelecimento} obrigatório")
	@Column(name = "cod_estabelecimento", nullable = false)
    private String codEstabelecimento;

	@Column(name = "peso_entrada")
    @Digits(integer = 8, fraction = 3)
    private BigDecimal pesoEntrada;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_entrada")
    private Date dataEntrada;

	@Column(name = "hora_entrada")
    private String horaEntrada;

	@Column(name = "peso_saida")
    @Digits(integer = 8, fraction = 3)
    private BigDecimal pesoSaida;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_saida")
    private Date dataSaida;

	@Column(name = "hora_saida")
    private String horaSaida;

	@Column(name = "peso_liquido")
    @Digits(integer = 8, fraction = 3)
    private BigDecimal pesoLiquido;
	
	@Column(name = "cod_usuario_ent")
    private String codUsuarioEnt;
	
	@Column(name = "cod_usuario_sai")
    private String codUsuarioSai;
	
	@Column(name = "nf_produtor")
    private String nfProdutor;
	
	@Column(name = "motorista")
    private String motorista;
	
	@Column(name = "observacao")
    private String observacao;

    @NotNull(message = "Campo {safra} obrigatório")
    @Column(name = "safra", nullable = false)
    private Integer safra;

	@Column(name = "cod_produtor")
    private Integer codProdutor;

	@Column(name = "nome_produtor")
    private String nomeProdutor;
	
	@Column(name = "cl_umidade")
	@Digits(integer = 3, fraction = 2)
    private BigDecimal clUmidade;
	
	@Column(name = "cl_impureza")
	@Digits(integer = 3, fraction = 2)
    private BigDecimal clImpureza;
	
	@Column(name = "cl_chu_avar")
	@Digits(integer = 3, fraction = 2)
    private BigDecimal clChuAvar;	
	
	@Column(name = "cl_ph")
	@Digits(integer = 3, fraction = 2)
    private BigDecimal clPh;
	
	@Column(name = "cl_out")
	@Digits(integer = 3, fraction = 2)
    private BigDecimal clOut;
	
	@Column(name = "cl_trinca")
	@Digits(integer = 3, fraction = 2)
    private BigDecimal clTrinca;
	
	@Column(name = "cl_aflatoxina")
	@Digits(integer = 3, fraction = 2)
    private BigDecimal clAflatoxina;
	
	@Column(name = "cl_esverdeado")
	@Digits(integer = 3, fraction = 2)
    private BigDecimal clEsverdeado;
    
    @Column(name = "cl_tbm")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal clTbm;
    
    @Column(name = "cl_pen")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal clPen;
    
    @Column(name = "cl_soja")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal clSoja;
    
    @Column(name = "cl_ph_corrigido")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal clPhCorrigido;
    
    @Column(name = "cl_bandinha")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal clBandinha;
    
    @Column(name = "cl_picado")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal clPicado;
    
    @Column(name = "cl_enrugado")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal clEnrugado;
    
    @Column(name = "cl_chocho")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal clChocho;
    
    @Column(name = "cl_imaturo")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal clImaturo;
    
    @Column(name = "cl_verdes")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal clVerdes;
    
    @Column(name = "variedade")
    private String variedade;
    
    @Column(name = "cod_cidade")
    private Integer codCidade;
    
    @Column(name = "nome_cidade")
    private String nomeCidade;
    
    @Column(name = "situacao")
    private String situacao;
    
    @Column(name = "pes_automatica")
    private Boolean pesAutomatica;
    
    @Column(name = "cod_balanca")
    private Integer codBalanca;
    
    @Column(name = "integrado")
    private Boolean integrado;
    
    @Column(name = "tipo_pesagem")
    private String tipoPesagem;
    
    @Column(name = "desmembrar")
    private Boolean desmembrar;
    
    @Column(name = "enviado")
    private Boolean enviado;
    
    @Column(name = "matricula")
    private Long matricula;
    
    @Column(name = "cod_imovel")
    private Long codImovel;
	
	@Column(name = "nome_imovel")
    private String nomeImovel;
    
    @Column(name = "impresso")
    private Boolean impresso;
    
    @Column(name = "cod_unid_parc")
    private Integer codUnidParc;
    
    @Column(name = "cod_clas_cam")
    private Integer codClasCam;
    
    @Column(name = "peso_amostra")
    @Digits(integer = 8, fraction = 3)
    private BigDecimal pesoAmostra;
    
    @Column(name = "fruto_verde")
    @Digits(integer = 8, fraction = 3)
    private BigDecimal frutoVerde;
    
    @Column(name = "fruto_pequeno")
    @Digits(integer = 8, fraction = 3)
    private BigDecimal frutoPequeno;
    
    @Column(name = "fruto_podre")
    @Digits(integer = 8, fraction = 3)
    private BigDecimal frutoPodre;
    
    @Column(name = "fruto_bolao")
    @Digits(integer = 8, fraction = 3)
    private BigDecimal frutoBolao;
    
    @Column(name = "industria")
    @Digits(integer = 8, fraction = 3)
    private BigDecimal industria;
    
    @Column(name = "impureza")
    @Digits(integer = 8, fraction = 3)
    private BigDecimal impureza;
    
    @Column(name = "pes_automatica_2")
    private Boolean pesAutomatica2;
    
    @Column(name = "uf_caminhao")
    private String ufCaminhao;
    
    @Column(name = "ordem_campo")
    private String ordemCampo;
    
    @Column(name = "controle_semente")
    private Integer controleSemente;
    
    @Column(name = "categoria")
    private String categoria;
    
    @Column(name = "laudo_vistoria")
    private String laudoVistoria;
    
    @Column(name = "syseed")
    private String syseed;
    
    @Column(name = "leitura_umidade")
    private String leituraUmidade;
    
    @Column(name = "dickey_john")
    private String dickeyJohn;
    
    @Column(name = "tipo_gmo")
    private String tipoGmo;
    
    @Column(name = "prod_padr")
    private Boolean prodPadr;
    
    @Column(name = "descar_unid")
    private Boolean descarUnid;
    
    @Column(name = "preco_ponto")
    @Digits(integer = 6, fraction = 9)
    private BigDecimal precoPonto;
    
    @Column(name = "qt_tx_secagem_sp")
    @Digits(integer = 7, fraction = 4)
    private BigDecimal qtTxSecagemSp;
    
    @Column(name = "qt_tx_recepcao_sp")
    @Digits(integer = 7, fraction = 4)
    private BigDecimal qtTxRecepcaoSp;
    
    @Column(name = "nao_listado")
    private Boolean naoListado;    
    
    @Column(name = "log_re_java", columnDefinition = "NUMBER(1,0) default 0")
    private Boolean logReJava;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusPesagem status;
    
    @Column(name = "cod_livre_1")
    private String codLivre1;
    
    @Column(name = "log_livre_1")
    private Boolean logLivre1;
    
    @Column(name = "int_livre_1")
    private Long intLivre1;
    
    @Digits(integer = 8, fraction = 5)
    @Column(name = "dec_livre_1")
    private BigDecimal decLivre1;
    
    @Column(name = "dat_livre_1")
    private Date datLivre1;
    
    @Column(name = "cod_livre_2")
    private String codLivre2;
	
    @Column(name = "log_livre_2")
    private Boolean logLivre2;
	
    @Column(name = "int_livre_2")
    private Long intLivre2;
    
    @Digits(integer = 8, fraction = 5)
    @Column(name = "dec_livre_2")
	private BigDecimal decLivre2;
    
    @Column(name = "dat_livre_2")
	private Date datLivre2;
    
    @Version
    private Integer versao; 	
    
    @Column(name = "id_re")
    private Long idRe;
    
    public static Pesagem construir (PesagemPostDto pesagemPostDto) {
        Pesagem pesagem = new Pesagem();
        BeanUtils.copyProperties(pesagemPostDto, pesagem);
        pesagem.setStatusIntegracao(StatusIntegracao.INTEGRADO);
        pesagem.setStatus(StatusPesagem.AGUARDANDO_RE);
        pesagem.setDataIntegracao(new Date());      
        return pesagem;
    }

    public static Pesagem construir (PesagemPutDto pesagemPutDto) {
        Pesagem pesagem = new Pesagem();
        BeanUtils.copyProperties(pesagemPutDto, pesagem);
        return pesagem;
    }
}
