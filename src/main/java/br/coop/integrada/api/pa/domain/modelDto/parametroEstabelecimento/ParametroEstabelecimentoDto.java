package br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.coop.integrada.api.pa.domain.enums.ParametroEstabelecimentoSituacaoEnum;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.parametros.ParametroEstabelecimento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ParametroEstabelecimentoDto {
    private Long id;
    private String codigo;
    private Integer nrUltBcqs;
    private BigDecimal txFam;
    private Boolean famTerc;
    private Boolean famCoop;
    private Boolean famPc;
    private String credencial;
    private Long nrUltTransf;
    private ParametroEstabelecimentoSituacaoEnum situacao;
    private String nomeEst;
    private String codEmitente;
    private Boolean ativo;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dtMovtoAberto;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dataIntegracao;
    
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
    private Boolean permitirEntradaCooperativa;
    private Boolean obrigaNfProdutor;
    private Boolean geraReGenesis;
    private Boolean logUbs;
	private IntegrationOperacaoEnum operacao;	
	
	@Transient
	private String codNomeEstab;

    public static ParametroEstabelecimentoDto construir(ParametroEstabelecimento obj) {
        var objDto = new ParametroEstabelecimentoDto();
        BeanUtils.copyProperties(obj, objDto);
        
        objDto.setOperacao(IntegrationOperacaoEnum.toEnum(obj.getAtivo()));
        
        objDto.setCodigo(obj.getEstabelecimento().getCodigo());

        objDto.setCodNomeEstab(obj.getEstabelecimento().getCodigo() + " - " + obj.getEstabelecimento().getNomeFantasia());
        return objDto;
    }

    public static List<ParametroEstabelecimentoDto> construir(List<ParametroEstabelecimento> objs) {
        if(objs == null) new ArrayList<>();

        return objs.stream().map(parametroEstabelecimento -> {
            return construir(parametroEstabelecimento);
        }).toList();
    }
}
