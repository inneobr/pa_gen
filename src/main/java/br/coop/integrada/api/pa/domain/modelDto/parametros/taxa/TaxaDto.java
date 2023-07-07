package br.coop.integrada.api.pa.domain.modelDto.parametros.taxa;

import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;
import br.coop.integrada.api.pa.domain.enums.taxa.TipoCobrancaEnum;
import br.coop.integrada.api.pa.domain.enums.taxa.TipoCobrancaSecagemEnum;
import br.coop.integrada.api.pa.domain.enums.taxa.TipoDescontoEnum;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.Taxa;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaCarenciaArmazenagem;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaGrupoProduto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Data
public class TaxaDto implements Serializable {

    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String codigoERP;

    @NotNull(message = "Campo {descricao} obrigatório")
    @NotBlank(message = "Campo {descricao} obrigatório")
    @ComparaObjeto(nome="Descrição")
    private String descricao;

    @NotNull(message = "Campo {safra} obrigatório")
    @ComparaObjeto(nome="Safra")
    private Integer safra;

    @ComparaObjeto(nome="Manutenção carência")
    private BigDecimal manutencaoCarencia;

    @ComparaObjeto(nome="Manutenção valor por saca")
    private BigDecimal manutencaoValorPorSaca;

    @NotNull(message = "Campo {quebraTecnicaCobrancaPor} obrigatório")
    @ComparaObjeto(nome="Quebra técnica cobrança por")
    private TipoDescontoEnum quebraTecnicaCobrancaPor;

    @ComparaObjeto(nome="Quebra técnica carência")
    private BigDecimal quebraTecnicaCarencia;

    @ComparaObjeto(nome="Quebra técnica dias máximo")
    private BigDecimal quebraTecnicaDiasMaximo;

    @ComparaObjeto(nome="Quebra técnica percentual ao dia")
    private BigDecimal quebraTecnicaPercentualAoDia;

    @ComparaObjeto(nome="Taxa seguro carência")
    private BigDecimal taxaSeguroCarencia;

    @ComparaObjeto(nome="Taxa seguro percentual por saca")
    private BigDecimal taxaSeguroPercentualPorSaca;

    @ComparaObjeto(nome="Taxa valor operacional")
    private BigDecimal taxaValorOperacional;

    @ComparaObjeto(nome="Taxa receita regional")
    private BigDecimal taxaReceitaRegional;

    @ComparaObjeto(nome="Taxa retenção especial")
    private BigDecimal taxaRetencaoEspecial;

    @ComparaObjeto(nome="Outras taxas")
    private BigDecimal taxaOutras;

    @ComparaObjeto(nome="Cobra recepção")
    private Boolean cobraRecepcao;

    @NotNull(message = "Campo {tipoCobraRecepcaoPor} obrigatório")
    @ComparaObjeto(nome="Cobra recepção por")
    private TipoCobrancaEnum tipoCobraRecepcaoPor;

    @ComparaObjeto(nome="Valor Taxa Saca 60Kg Recepção")
    private BigDecimal valorTaxaSaca60Recepcao;
    
    @ComparaObjeto(nome="Valor taxa recepção")
    private BigDecimal valorTaxaRecepcao;

    @ComparaObjeto(nome="Cobra secagem")
    private Boolean cobraSecagem;

    @NotNull(message = "Campo {tipoCobraSecagemNa} obrigatório")
    @ComparaObjeto(nome="Cobra secagem na")
    private TipoCobrancaEnum tipoCobraSecagemNa;

    @NotNull(message = "Campo {tipoCobrancaSecagemPor} obrigatório")
    @ComparaObjeto(nome="Cobrança de secagem por")
    private TipoCobrancaSecagemEnum tipoCobrancaSecagemPor;

    @ComparaObjeto(nome="Cobrança de armazenagem")
    private Boolean cobrancaArmazenagem;

    @ComparaObjeto(nome="Valor de armazenagem")
    private BigDecimal valorArmazenagem;
    
    @ComparaObjeto(nome="Valor Taxa Saca 60Kg")
    private BigDecimal valorTaxaSaca60;
    
    @ComparaObjeto(nome="Número de dias de carência")
    private Integer numeroDiasCarencia;

    @ComparaObjeto(nome="Data cobrança de armazenagem")
    private Date dataCobrancaArmazenagem;

    @ComparaObjeto(nome="Data cobrança de armazenagem 2")
    private Date dataCobrancaArmazenagem2;

    @NotNull(message = "{grupoProdutos} obrigatório")
    @NotEmpty(message = "Necessário informar ao menos um {grupoProdutos}")
    private List<TaxaGrupoProdutoDto> grupoProdutos;
    
    @NotNull(message = "{estabelecimentos} obrigatório")
    @NotEmpty(message = "Necessário informar ao menos um {estabelecimentos}")
    @ComparaObjeto(nome="Estabelecimentos")
    private List<TaxaEstabelecimentoDto> estabelecimentos;

    //@NotNull(message = "{carenciaArmazenagens} obrigatório")
    //@NotEmpty(message = "@Necessário informar ao menos uma {carenciaArmazenagens}")
    private List<TaxaCarenciaArmazenagemDto> carenciaArmazenagens;
    private Date dataCadastro;
    private Date dataAtualizacao;
    private Date dataIntegracao;
    private Boolean ativo;

    public static TaxaDto construir(Taxa obj) {
    	
    	if(obj == null) {
    		return null;
    	}
    	
        var objDto = new TaxaDto();       
        
        BeanUtils.copyProperties(obj, objDto);

        List<TaxaGrupoProdutoDto> grupoProdutos = TaxaGrupoProdutoDto.construir(obj.getGrupoProdutos());
        objDto.setGrupoProdutos(grupoProdutos);
        
        List<TaxaEstabelecimentoDto> estabelecimento = TaxaEstabelecimentoDto.construir(obj.getEstabelecimentos());
        objDto.setEstabelecimentos(estabelecimento);

        List<TaxaCarenciaArmazenagemDto> carenciaArmazenagens = TaxaCarenciaArmazenagemDto.construir(obj.getCarenciaArmazenagens());
        objDto.setCarenciaArmazenagens(carenciaArmazenagens);

        return objDto;
    }
}
