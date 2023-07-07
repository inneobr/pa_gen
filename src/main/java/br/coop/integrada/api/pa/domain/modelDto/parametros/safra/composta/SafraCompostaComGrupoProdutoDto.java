package br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta;

import br.coop.integrada.api.pa.domain.enums.TipoSafraOperacaoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class SafraCompostaComGrupoProdutoDto implements Serializable {
    private String fmCodigo;
    private String descricao;
    private Long id;
    private String nomeSafra;
    private Integer diaInicial;
    private Integer mesInicial;
    private TipoSafraOperacaoEnum anoInicialOperacao;
    private Integer anoInicialQuantidade;
    private Integer diaFinal;
    private Integer mesFinal;
    private TipoSafraOperacaoEnum anoFinalOperacao;
    private Integer anoFinalQuantidade;
    private TipoSafraOperacaoEnum safraPlantioOperacao;
    private Integer safraPlantioQuantidade;
    private TipoSafraOperacaoEnum safraColheitaOperacao;
    private Integer safraColheitaQuantidade;

    public SafraCompostaComGrupoProdutoDto(String fmCodigo, String descricao, Long id, String nomeSafra, Integer diaInicial, Integer mesInicial, String anoInicialOperacao, Integer anoInicialQuantidade, Integer diaFinal, Integer mesFinal, String anoFinalOperacao, Integer anoFinalQuantidade, String safraPlantioOperacao, Integer safraPlantioQuantidade, String safraColheitaOperacao, Integer safraColheitaQuantidade) {
        this.fmCodigo = fmCodigo;
        this.descricao = descricao;
        this.id = id;
        this.nomeSafra = nomeSafra;
        this.diaInicial = diaInicial;
        this.mesInicial = mesInicial;
        this.anoInicialOperacao = TipoSafraOperacaoEnum.valueOf(anoInicialOperacao);
        this.anoInicialQuantidade = anoInicialQuantidade;
        this.diaFinal = diaFinal;
        this.mesFinal = mesFinal;
        this.anoFinalOperacao = TipoSafraOperacaoEnum.valueOf(anoFinalOperacao);
        this.anoFinalQuantidade = anoFinalQuantidade;
        this.safraPlantioOperacao = TipoSafraOperacaoEnum.valueOf(safraPlantioOperacao);
        this.safraPlantioQuantidade = safraPlantioQuantidade;
        this.safraColheitaOperacao = TipoSafraOperacaoEnum.valueOf(safraColheitaOperacao);
        this.safraColheitaQuantidade = safraColheitaQuantidade;
    }
}
