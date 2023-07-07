package br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta;

import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;
import br.coop.integrada.api.pa.domain.enums.TipoSafraEnum;
import br.coop.integrada.api.pa.domain.enums.TipoSafraOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.parametros.safra.composta.SafraComposta;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoSimplesDto;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SafraCompostaDto implements Serializable {

    private static final long serialVersionUID = 1L;
	private Long id;
    private Long tipoProdutoId;
    private String tipoProdutoNome;
    private TipoSafraEnum tipoSafra;

    @NotNull(message = "Campo {nomeSafra} obrigatório")
    @NotBlank(message = "Campo {nomeSafra} obrigatório")
    @ComparaObjeto(nome="Nome safra")
    private String nomeSafra;

    @NotNull(message = "Campo {diaInicial} obrigatório")
    @ComparaObjeto(nome="Dia inicial")
    private Integer diaInicial;

    @NotNull(message = "Campo {mesInicial} obrigatório")
    @ComparaObjeto(nome="Mês inicial")
    private Integer mesInicial;

    @NotNull(message = "Campo {anoInicialOperacao} obrigatório")
    @ComparaObjeto(nome="Ano inicial operação")
    private TipoSafraOperacaoEnum anoInicialOperacao;

    @NotNull(message = "Campo {anoInicialQuantidade} obrigatório")
    @ComparaObjeto(nome="Ano inicial quantidade")
    private Integer anoInicialQuantidade;

    @NotNull(message = "Campo {diaFinal} obrigatório")
    @ComparaObjeto(nome="Dia final")
    private Integer diaFinal;

    @NotNull(message = "Campo {mesFinal} obrigatório")
    @ComparaObjeto(nome="Mês final")
    private Integer mesFinal;

    @NotNull(message = "Campo {anoFinalOperacao} obrigatório")
    @ComparaObjeto(nome="Ano final operação")
    private TipoSafraOperacaoEnum anoFinalOperacao;

    @NotNull(message = "Campo {anoFinalQuantidade} obrigatório")
    @ComparaObjeto(nome="Ano final quantidade")
    private Integer anoFinalQuantidade;

    @NotNull(message = "Campo {safraPlantioOperacao} obrigatório")
    @ComparaObjeto(nome="Safra plantio operação")
    private TipoSafraOperacaoEnum safraPlantioOperacao;

    @NotNull(message = "Campo {safraPlantioQuantidade} obrigatório")
    @ComparaObjeto(nome="Safra plantio quantidade")
    private Integer safraPlantioQuantidade;

    @NotNull(message = "Campo {safraColheitaOperacao} obrigatório")
    @ComparaObjeto(nome="Safra colheita operação")
    private TipoSafraOperacaoEnum safraColheitaOperacao;

    @NotNull(message = "Campo {safraColheitaQuantidade} obrigatório")
    @ComparaObjeto(nome="Safra colheita quantidade")
    private Integer safraColheitaQuantidade;
    private List<EstabelecimentoSimplesDto> estabelecimentos;
    private Date dataAtualizacao;
    private Date dataInativacao;
    private Boolean ativo;

    public static SafraCompostaDto construir(SafraComposta obj) {
        var objDto = new SafraCompostaDto();
        BeanUtils.copyProperties(obj, objDto);

        if(obj.getTipoProduto() != null) {
            objDto.setTipoProdutoId(obj.getTipoProduto().getId());
            objDto.setTipoProdutoNome(obj.getTipoProduto().getNome());
        }

        if(obj.getDataAtualizacao() == null) {
            objDto.setDataAtualizacao(obj.getDataCadastro());
        }

        List<EstabelecimentoSimplesDto> estabelecimentoSimplesDtos = obj.getEstabelecimentos()
                .stream().map(safraCompostaEstabelecimento -> {
                    return EstabelecimentoSimplesDto.construir(safraCompostaEstabelecimento.getEstabelecimento());
                }).toList();
        objDto.setEstabelecimentos(estabelecimentoSimplesDtos);

        return objDto;
    }

    public static List<SafraCompostaDto> construir(List<SafraComposta> objs) {
        if(objs == null) new ArrayList<>();

        return objs.stream().map(safraComposta -> {
            return SafraCompostaDto.construir(safraComposta);
        }).toList();
    }
}
