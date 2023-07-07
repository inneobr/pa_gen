package br.coop.integrada.api.pa.domain.modelDto.parametros.taxa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaGrupoProduto;
import lombok.Data;

@Data
public class TaxaGrupoProdutoDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

    @NotNull(message = "O campo {idGrupoProduto} é obrigatório")
    private Long idGrupoProduto;

    @ComparaObjeto(nome="FM Código")
    private String fmCodigo;

    @ComparaObjeto(nome="Descrição")
    private String descricao;

    /*@NotNull(message = "{estabelecimentos} obrigatório")
    @NotEmpty(message = "Necessário informar ao menos um {estabelecimentos}")
    @ComparaObjeto(nome="Estabelecimentos")
    private List<TaxaEstabelecimentoDto> estabelecimentos;*/
    
    @ComparaObjeto(nome="tipoProdutoNome")
    private String tipoProdutoNome;

    @Override
    public String toString() {StringBuilder ret = new StringBuilder();
        ret.append("ID: ");
        ret.append(idGrupoProduto);
        ret.append(" | FM Código: ");
        ret.append(fmCodigo);
        ret.append(" | Descrição: ");
        ret.append(descricao);
        ret.append(" | Tipo Produto Nome: ");
        ret.append(tipoProdutoNome);

        /*if(estabelecimentos != null) {
            ret.append(" | Estabelecimentos: [");

            for(Integer indice = 0; indice < estabelecimentos.size(); indice++) {
                TaxaEstabelecimentoDto estabelecimento = estabelecimentos.get(indice);
                ret.append(estabelecimento.toString());

                if(indice < estabelecimentos.size() - 1) ret.append(", ");
            }

            ret.append("]");
        }*/

        return ret.toString();
    }

    public static TaxaGrupoProdutoDto construir(TaxaGrupoProduto obj) {
        TaxaGrupoProdutoDto objDto = new TaxaGrupoProdutoDto();

        objDto.setId(obj.getId());

        if(obj.getGrupoProduto() != null) {
            objDto.setIdGrupoProduto(obj.getGrupoProduto().getId());
            objDto.setDescricao(obj.getGrupoProduto().getDescricao());
            objDto.setFmCodigo(obj.getGrupoProduto().getFmCodigo());
            objDto.setTipoProdutoNome(obj.getGrupoProduto().getTipoProduto().getNome());
        }

        //List<TaxaEstabelecimentoDto> estabelecimentos = TaxaEstabelecimentoDto.construir(obj.getEstabelecimentos());
        //objDto.setEstabelecimentos(estabelecimentos);

        return objDto;
    }

    public static List<TaxaGrupoProdutoDto> construir(List<TaxaGrupoProduto> objs) {
        if(objs == null) return new ArrayList<>();
        return objs.stream().map(classificacaoGrupoProduto -> {
            return TaxaGrupoProdutoDto.construir(classificacaoGrupoProduto);
        }).collect(Collectors.toList());
    }
}
