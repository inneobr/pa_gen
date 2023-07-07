package br.coop.integrada.api.pa.domain.modelDto.parametros;

import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariado;
import br.coop.integrada.api.pa.domain.model.parametros.avariado.ItemAvariadoDetalhe;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.model.produto.ProdutoReferencia;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoReferenciaDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.ProdutoSimplesDto;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.validation.constraints.Digits;

@Data
public class ItemAvariadoDetalheDto implements Serializable {

    private Long id;
    private GrupoProdutoSimplesDto grupoProduto;

    @ComparaObjeto(nome="Produto")
    private ProdutoSimplesDto produto;

    @ComparaObjeto(nome="Produto Referência")
    private ProdutoReferenciaDto produtoReferencia;

    @ComparaObjeto(nome="Percentual Inicial")
    private BigDecimal percentualInicial;

    @ComparaObjeto(nome="Percentual Final")
    private BigDecimal percentualFinal;
    
    @ComparaObjeto(nome="Ph Inicial")
    private BigDecimal phInicial;
    
    @ComparaObjeto(nome="Ph Final")
    private BigDecimal phFinal;
    
    @ComparaObjeto(nome="FNT Inicial")
    private BigDecimal fntInicial;

    @ComparaObjeto(nome="FNT Final")
    private BigDecimal fntFinal;
    
    private Boolean ativo;
    
    public BigDecimal getPercentualInicial() {
    	if(percentualInicial == null) return BigDecimal.ZERO;
    	return percentualInicial;
    }
    
    public BigDecimal getPercentualFinal() {
    	if(percentualFinal == null) return BigDecimal.ZERO;
    	return percentualFinal;
    }
    
    public BigDecimal getPhInicial() {
    	if(phInicial == null) return BigDecimal.ZERO;
    	return phInicial;
    }
    
    public BigDecimal getPhFinal() {
    	if(phFinal == null) return BigDecimal.ZERO;
    	return phFinal;
    }
    
    public BigDecimal getFntInicial() {
    	if(fntInicial == null) return BigDecimal.ZERO;
    	return fntInicial;
    }
    
    public BigDecimal getFntFinal() {
    	if(fntFinal == null) return BigDecimal.ZERO;
    	return fntFinal;
    }

    public static ItemAvariadoDetalheDto construir(ItemAvariadoDetalhe obj) {
        var objDto = new ItemAvariadoDetalheDto();
        BeanUtils.copyProperties(obj, objDto);

        if(obj.getProduto() != null) {
            GrupoProdutoSimplesDto grupoProduto = GrupoProdutoSimplesDto.construir(obj.getProduto().getGrupoProduto());
            objDto.setGrupoProduto(grupoProduto);

            ProdutoSimplesDto produto = ProdutoSimplesDto.construir(obj.getProduto());
            objDto.setProduto(produto);
        }

        if(obj.getProdutoReferencia() != null) {
            ProdutoReferenciaDto produtoReferencia = ProdutoReferenciaDto.construir(obj.getProdutoReferencia());
            objDto.setProdutoReferencia(produtoReferencia);
        }

        return objDto;
    }

    public static List<ItemAvariadoDetalheDto> construir(List<ItemAvariadoDetalhe> objs) {
        if(CollectionUtils.isEmpty(objs)) return new ArrayList<>();

        return objs.stream().map(itemAvariadoDetalhe -> {
            return ItemAvariadoDetalheDto.construir(itemAvariadoDetalhe);
        }).collect(Collectors.toList());
    }

    public static ItemAvariadoDetalhe converterDto(ItemAvariadoDetalheDto objDto, ItemAvariado itemAvariado) {
        var obj = new ItemAvariadoDetalhe();
        BeanUtils.copyProperties(objDto, obj);

        obj.setItemAvariado(itemAvariado);

        Produto produto = ProdutoSimplesDto.converterDto(objDto.getProduto());
        obj.setProduto(produto);

        ProdutoReferencia produtoReferencia = ProdutoReferenciaDto.converterDto(objDto.getProdutoReferencia());
        obj.setProdutoReferencia(produtoReferencia);

        return obj;
    }

    public static List<ItemAvariadoDetalhe> converterDto(List<ItemAvariadoDetalheDto> objDtos, ItemAvariado itemAvariado) {
        if(CollectionUtils.isEmpty(objDtos)) return new ArrayList<>();
        return objDtos.stream().map(itemAvariadoDetalheDto -> {
            return ItemAvariadoDetalheDto.converterDto(itemAvariadoDetalheDto, itemAvariado);
        }).collect(Collectors.toList());
    }
}
