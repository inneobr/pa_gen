package br.coop.integrada.api.pa.domain.modelDto.classificacao;

import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoDetalhe;
import br.coop.integrada.api.pa.domain.model.produto.ProdutoReferencia;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ClassificacaoDetalheDto implements Serializable {

    private static final long serialVersionUID = 1L;

	private Long id;

    @ComparaObjeto(nome="Teor Inicial")
    @NotNull(message = "O campo {teorClassificacaoInicial} é obrigatório")
    private BigDecimal teorClassificacaoInicial;

    @ComparaObjeto(nome="Teor Final")
    @NotNull(message = "O campo {teorClassificacaoFinal} é obrigatório")
    private BigDecimal teorClassificacaoFinal;

    @ComparaObjeto(nome="PH Entrada")
    private BigInteger phEntrada;

    @ComparaObjeto(nome="Percentual Desconto")
    private BigDecimal percentualDesconto;

    @ComparaObjeto(nome="PH Corrigido")
    private BigInteger phCorrigido;

    @ComparaObjeto(nome="Código Referência")
    private String codigoReferencia;

    @ComparaObjeto(nome="Taxa Secagem Quilo")
    private BigDecimal taxaSecagemQuilo;

    @ComparaObjeto(nome="Taxa Secagem Valor")
    private BigDecimal taxaSecagemValor;
    
    private Date dataCadastro;
    private StatusIntegracao statusIntegracao;
        

    public static ClassificacaoDetalheDto construir(ClassificacaoDetalhe obj) {
        ClassificacaoDetalheDto objDto = new ClassificacaoDetalheDto();
        BeanUtils.copyProperties(obj, objDto);

        if(obj.getProdutoReferencia() != null) {
            objDto.setCodigoReferencia(obj.getProdutoReferencia().getCodRef());
        }
        
        
        
        return objDto;
    }

    public static List<ClassificacaoDetalheDto> construir(List<ClassificacaoDetalhe> objs) {
        return objs.stream().map(classificacaoDetalhe -> {
            return ClassificacaoDetalheDto.construir(classificacaoDetalhe);
        }).collect(Collectors.toList());
    }
    
    public static List<ClassificacaoDetalheDto> construirAtivos(List<ClassificacaoDetalhe> objs) {
        return objs.stream().filter(p -> p.getDataInativacao() == null).map(classificacaoDetalhe -> {
        	return ClassificacaoDetalheDto.construir(classificacaoDetalhe);
        }).collect(Collectors.toList());
    }

    public static ClassificacaoDetalhe converterDto(ClassificacaoDetalheDto objDto, Classificacao classificacao) {
        ClassificacaoDetalhe obj = new ClassificacaoDetalhe();
        BeanUtils.copyProperties(objDto, obj);

        obj.setClassificacao(classificacao);

        if(objDto.getCodigoReferencia() != null && !objDto.codigoReferencia.isEmpty()) {
            ProdutoReferencia produtoReferencia = new ProdutoReferencia();
            produtoReferencia.setCodRef(objDto.getCodigoReferencia());
            obj.setProdutoReferencia(produtoReferencia);
        }
             

        return obj;
    }

    public static List<ClassificacaoDetalhe> converterDto(List<ClassificacaoDetalheDto> objDtos, Classificacao classificacao) {
        return objDtos.stream().map(classificacaoDetalheDto -> {
            return ClassificacaoDetalheDto.converterDto(classificacaoDetalheDto, classificacao);
        }).collect(Collectors.toList());
    }
        
}
