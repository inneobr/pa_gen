package br.coop.integrada.api.pa.domain.model.parametros;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;
import br.coop.integrada.api.pa.domain.enums.ParametroEstabelecimentoSituacaoEnum;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(
        name = "parametros_estabelecimento",
        uniqueConstraints = {
                @UniqueConstraint(name = "KEY_ID_ESTABELECIMENTO", columnNames = { "id_estabelecimento" })
        }
        )
public class ParametroEstabelecimento extends AbstractEntity{
    private static final long serialVersionUID = 1L;

    @ComparaObjeto(nome = "estabelecimento:", verifica = true)
    @ManyToOne
    @JoinColumn(name="id_estabelecimento")
    private Estabelecimento estabelecimento;

    @ComparaObjeto(nome = "nrUltBcqs:", verifica = true)
    @Column(name = "nr_ult_bcqs")
    private Integer nrUltBcqs;

    @ComparaObjeto(nome = "txFam:", verifica = true)
    @Digits(integer = 6, fraction = 2)
    @Column(name = "tx_fam")
    private BigDecimal txFam;

    @ComparaObjeto(nome = "famTerc:", verifica = true)
    @Column(name = "fam_terc")
    private Boolean famTerc;

    @ComparaObjeto(nome = "famCoop:", verifica = true)
    @Column(name = "fam_coop")
    private Boolean famCoop;

    @ComparaObjeto(nome = "famPc:", verifica = true)
    @Column(name = "fam_pc")
    private Boolean famPc;

    @ComparaObjeto(nome = "credencial:", verifica = true)
    @Column(name = "credencial")
    private String credencial;

    @ComparaObjeto(nome = "nrUltTransf:", verifica = true)
    @Column(name = "nr_ult_transf")
    private Long nrUltTransf;

    @Enumerated(EnumType.STRING)
    @ComparaObjeto(nome = "Situacao:", verifica = true)
    @Column(name = "situacao")
    private ParametroEstabelecimentoSituacaoEnum situacao;

    @ComparaObjeto(nome = "nomeEst:", verifica = true)
    @Column(name = "nome_est")
    private String nomeEst;

    @ComparaObjeto(nome = "codEmitente:", verifica = true)
    @Column(name = "cod_emitente ")
    private String codEmitente;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @ComparaObjeto(nome = "dtMovtoAberto:", verifica = true)
    @Column(name = " dtMovtoAberto")
    private Date dtMovtoAberto;

    @ComparaObjeto(nome = "Estado:", verifica = true)
    @Column(name = "estado")
    private String estado;

    @ComparaObjeto(nome = "entSafraAnt:", verifica = true)
    @Column(name = "ent_safra_ant")
    private Boolean entSafraAnt;

    @ComparaObjeto(nome = "renasem:", verifica = true)
    @Column(name = "renasem")
    private String renasem;

    @ComparaObjeto(nome = "logRecebeTransgenico:", verifica = true)
    @Column(name = "log_recebe_transgenico")
    private Boolean logRecebeTransgenico;

    @ComparaObjeto(nome = "sigla", verifica = true)
    @Column(name = "sigla")
    private String sigla;

    @ComparaObjeto(nome = "renasemComer:", verifica = true)
    @Column(name = "renasem_comer")
    private String renasemComer;

    @ComparaObjeto(nome = "logMaqCafe:", verifica = true)
    @Column(name = "log_maq_cafe")
    private Boolean logMaqCafe;

    @ComparaObjeto(nome = "logSilo:", verifica = true)
    @Column(name = "log_silo")
    private Boolean logSilo;

    @ComparaObjeto(nome = "provador:", verifica = true)
    @Column(name = "provador")
    private String provador;    

    @ComparaObjeto(nome = "demoVlAgregados:", verifica = true)
    @Column(name = "demo_vl_agregados")
    private Boolean demoVlAgregados;

    @ComparaObjeto(nome = "vlDolarDecom:", verifica = true)
    @Digits(integer = 6, fraction = 3)
    @Column(name = "vl_dolar_decom")
    private BigDecimal vlDolarDecom;

    @ComparaObjeto(nome = "vlKgFornecFut", verifica = true)
    @Digits(integer = 6, fraction = 9)
    @Column(name = "vl_kg_fornec_fut")
    private BigDecimal vlKgFornecFut;

    @ComparaObjeto(nome = "integraRe:", verifica = true)
    @Column(name = "integra_re")
    private Boolean integraRe;

    @ComparaObjeto(nome = "prazoCancNf:", verifica = true)
    @Column(name = "prazo_canc_nf")
    private Integer prazoCancNf;

    @ComparaObjeto(nome = "codImovel:", verifica = true)
    @Column(name = "cod_imovel")
    private Long codImovel;

    @ComparaObjeto(nome = "logDesfazerFixacao:", verifica = true)
    @Column(name = "logDesfazerFixacao")
    private Boolean logDesfazerFixacao;

    @ComparaObjeto(nome = "desMotCancFixacao:", verifica = true)
    @Column(name = "des_mot_canc_fixacao")
    private String desMotCancFixacao;

    @ComparaObjeto(nome = "hrAgendaManhaIni:", verifica = true)
    @Column(name = "hr_agenda_manha_ini")
    private String hrAgendaManhaIni;

    @ComparaObjeto(nome = "hrAgendaManhaFim:", verifica = true)
    @Column(name = "hr_agenda_manha_fim")
    private String hrAgendaManhaFim;

    @ComparaObjeto(nome = "hrAgendaTardeIni:", verifica = true)
    @Column(name = "hr_agenda_tarde_ini")
    private String hrAgendaTardeIni;

    @ComparaObjeto(nome = "hrAgendaTardeFim:", verifica = true)
    @Column(name = "hr_agenda_tarde_fim")
    private String hrAgendaTardeFim;

    @ComparaObjeto(nome = "logEntradaSemTik:", verifica = true)
    @Column(name = "log_entrada_sem_tik")
    private Boolean logEntradaSemTik;

    @ComparaObjeto(nome = "Permitir entrada em nome da cooperativa", verifica = true)
    @Column(name = "permitir_entrada_cooperativa")
    private Boolean permitirEntradaCooperativa;

    @ComparaObjeto(nome = "Obriga nota fiscal do produtor", verifica = true)
    @Column(name = "obriga_nf_produtor")
    private Boolean obrigaNfProdutor;

    @ComparaObjeto(nome = "geraReGenesis:", verifica = true)
    @Column(name = "gera_re_genesis")
    private Boolean geraReGenesis;

    @ComparaObjeto(nome = "logUbs:", verifica = true)
    @Column(name = "log_ubs")
    private Boolean logUbs;
    
    
    public Boolean getFamTerc() {
    	if(famTerc == null) return false;
    	return famTerc;
    }
    
    public Boolean getFamCoop() {
    	if(famCoop == null) return false;
    	return famCoop;
    }

    public Boolean getFamPc() {
    	if(famPc == null) return false;
    	return famPc;
    }
    
    public Boolean getEntSafraAnt() {
    	if(entSafraAnt == null) return false;
    	return entSafraAnt;
    }
    
    public Boolean getLogRecebeTransgenico() {
    	if(logRecebeTransgenico == null) return false;
    	return logRecebeTransgenico;
    }
    
    public Boolean getLogMaqCafe() {
    	if(logMaqCafe == null) return false;
    	return logMaqCafe;
    }
    
    public Boolean getLogSilo() {
    	if(logSilo == null) return false;
    	return logSilo;
    }
    
    public Boolean getLogUbs() {
    	if(logUbs == null) return false;
    	return logUbs;
    }
    
    public Boolean getDemoVlAgregados() {
    	if(demoVlAgregados == null) return false;
    	return demoVlAgregados;
    }
    
    public Boolean getIntegraRe() {
    	if(integraRe == null) return false;
    	return integraRe;
    }
    
    public Boolean getLogDesfazerFixacao() {
    	if(logDesfazerFixacao == null) return false;
    	return logDesfazerFixacao;
    }
    
    public Boolean getLogEntradaSemTik() {
    	if(logEntradaSemTik == null) return false;
    	return logEntradaSemTik;
    }

    public Boolean getPermitirEntradaCooperativa() {
    	if(permitirEntradaCooperativa == null) return false;
    	return permitirEntradaCooperativa;
    }

    public Boolean getObrigaNfProdutor() {
    	if(obrigaNfProdutor == null) return false;
    	return obrigaNfProdutor;
    }
}
