package br.coop.integrada.api.pa.domain.modelDto.parametros.taxa;

import br.coop.integrada.api.pa.domain.enums.taxa.TipoCobrancaEnum;
import br.coop.integrada.api.pa.domain.enums.taxa.TipoCobrancaSecagemEnum;
import br.coop.integrada.api.pa.domain.enums.taxa.TipoDescontoEnum;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.Taxa;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TaxaSimplesDto implements Serializable {

    private Long id;
    private String descricao;
    private Integer safra;
    private Boolean cobraRecepcao;
    private TipoCobrancaEnum tipoCobraRecepcaoPor;
    private BigDecimal valorTaxaRecepcao;
    private Boolean cobraSecagem;
    private TipoCobrancaEnum tipoCobraSecagemNa;
    private TipoCobrancaSecagemEnum tipoCobrancaSecagemPor;
    private Boolean cobrancaArmazenagem;
    private BigDecimal valorArmazenagem;
    private Date dataCobrancaArmazenagem;
    private Date dataCobrancaArmazenagem2;
    private BigDecimal manutencaoCarencia;
    private BigDecimal manutencaoValorPorSaca;
    private BigDecimal taxaSeguroCarencia;
    private BigDecimal taxaSeguroPercentualPorSaca;
    private TipoDescontoEnum quebraTecnicaCobrancaPor;
    private BigDecimal quebraTecnicaCarencia;
    private BigDecimal quebraTecnicaDiasMaximo;
    private BigDecimal quebraTecnicaPercentualAoDia;
    private BigDecimal taxaValorOperacional;
    private BigDecimal taxaReceitaRegional;
    private BigDecimal taxaRetencaoEspecial;
    private BigDecimal taxaOutras;
    private Date dataCadastro;
    private Date dataAtualizacao;
    private Date dataIntegracao;
    private Boolean ativo;

    public static TaxaSimplesDto construir(Taxa obj) {
        var objDto = new TaxaSimplesDto();
        BeanUtils.copyProperties(obj, objDto);
        return objDto;
    }

    public static List<TaxaSimplesDto> construir(List<Taxa> objs) {
        if(objs == null) return new ArrayList<>();
        return objs.stream().map(taxa -> {
            return TaxaSimplesDto.construir(taxa);
        }).collect(Collectors.toList());
    }

    public static Taxa converterDto(TaxaSimplesDto objDto) {
        var obj = new Taxa();
        BeanUtils.copyProperties(objDto, obj);
        return obj;
    }

    public static List<Taxa> converterDto(List<TaxaSimplesDto> objDtos) {
        if(objDtos == null) return new ArrayList<>();
        return objDtos.stream().map(taxaSimplesDto -> {
            return TaxaSimplesDto.converterDto(taxaSimplesDto);
        }).collect(Collectors.toList());
    }
}
