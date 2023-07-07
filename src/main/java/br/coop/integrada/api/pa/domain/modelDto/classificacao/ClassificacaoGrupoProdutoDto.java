package br.coop.integrada.api.pa.domain.modelDto.classificacao;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import org.springframework.beans.BeanUtils;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoGrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class ClassificacaoGrupoProdutoDto implements Serializable {

    private static final long serialVersionUID = 1L;

	private Long id;

    @NotNull(message = "O campo {idGrupoProduto} é obrigatório")
    private Long idGrupoProduto;
    private String fmCodigo;
    private String descricao;
    //private List<EstabelecimentoSimplesDto> estabelecimentos;

    public static ClassificacaoGrupoProdutoDto construir(ClassificacaoGrupoProduto obj) {
        ClassificacaoGrupoProdutoDto objDto = new ClassificacaoGrupoProdutoDto();

        objDto.setId(obj.getId());

        if(obj.getGrupoProduto() != null) {
            objDto.setIdGrupoProduto(obj.getGrupoProduto().getId());
            objDto.setDescricao(obj.getGrupoProduto().getDescricao());
            objDto.setFmCodigo(obj.getGrupoProduto().getFmCodigo());
        }

        //List<EstabelecimentoSimplesDto> estabelecimentos = EstabelecimentoSimplesDto.construir(obj.getEstabelecimentos());
        //objDto.setEstabelecimentos(estabelecimentos);

        return objDto;
    }

    public static List<ClassificacaoGrupoProdutoDto> construir(List<ClassificacaoGrupoProduto> objs) {
        return objs.stream().map(classificacaoGrupoProduto -> {
            return ClassificacaoGrupoProdutoDto.construir(classificacaoGrupoProduto);
        }).collect(Collectors.toList());
    }

    public static ClassificacaoGrupoProduto converterDto(ClassificacaoGrupoProdutoDto objDto, Classificacao classificacao) {
        ClassificacaoGrupoProduto obj = new ClassificacaoGrupoProduto();
        BeanUtils.copyProperties(objDto, obj);

        GrupoProduto grupoProduto = new GrupoProduto();
        grupoProduto.setId(objDto.getIdGrupoProduto());
        grupoProduto.setFmCodigo(objDto.getFmCodigo());
        grupoProduto.setDescricao(objDto.getDescricao());

        //List<Estabelecimento> estabelecimentos = EstabelecimentoSimplesDto.converterDto(objDto.getEstabelecimentos());

        obj.setClassificacao(classificacao);
        obj.setGrupoProduto(grupoProduto);
        //obj.setEstabelecimentos(estabelecimentos);

        return obj;
    }

    public static List<ClassificacaoGrupoProduto> converterDto(List<ClassificacaoGrupoProdutoDto> objDtos, Classificacao classificacao) {
        return objDtos.stream().map(classificacaoGrupoProdutoDto -> {
            return ClassificacaoGrupoProdutoDto.converterDto(classificacaoGrupoProdutoDto, classificacao);
        }).collect(Collectors.toList());
    }
}
