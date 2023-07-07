package br.coop.integrada.api.pa.domain.modelDto.classificacao;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.cadastro.TipoClassificacao;
import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoDetalhe;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoEstabelecimento;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoGrupoProduto;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoSafra;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Data
public class ClassificacaoDto implements Serializable {

    private static final long serialVersionUID = 1L;

	private Long id;

    @NotBlank(message = "O campo {descricao} é obrigatório")
    private String descricao;
    private TipoClassificacaoDto tipoClassificacao;
    private List<ClassificacaoSafraDto> safras;
    private List<ClassificacaoDetalheDto> detalhes;
    private List<ClassificacaoGrupoProdutoDto> grupoProdutos;
    private List<ClassificacaoEstabelecimentoDto> estabelecimentos;
    
    private Date dataIntegracao;
    private String descricaoStatusIntegracao;

    public static ClassificacaoDto construir(Classificacao obj) {
        ClassificacaoDto objDto = new ClassificacaoDto();
        BeanUtils.copyProperties(obj, objDto);

        TipoClassificacaoDto tipoClassificacao = TipoClassificacaoDto.construir(obj.getTipoClassificacao());
        objDto.setTipoClassificacao(tipoClassificacao);

        List<ClassificacaoSafraDto> safras = ClassificacaoSafraDto.construir(obj.getSafras());
        objDto.setSafras(safras);

        List<ClassificacaoDetalheDto> detalhes = ClassificacaoDetalheDto.construir(obj.getDetalhes());
        objDto.setDetalhes(detalhes);

        List<ClassificacaoGrupoProdutoDto> grupoPordutos = ClassificacaoGrupoProdutoDto.construir(obj.getGrupoProdutos());
        objDto.setGrupoProdutos(grupoPordutos);
        
        List<ClassificacaoEstabelecimentoDto> estabelecimentos = ClassificacaoEstabelecimentoDto.construir(obj.getEstabelecimentos());
        objDto.setEstabelecimentos(estabelecimentos);
        
        if(obj.getStatusIntegracao() != null) {
    		objDto.setDescricaoStatusIntegracao(obj.getStatusIntegracao().getDescricao());
    	}
        
        return objDto;
    }
    
    public static ClassificacaoDto construirDetalheAtivos(Classificacao obj) {
        ClassificacaoDto objDto = new ClassificacaoDto();
        BeanUtils.copyProperties(obj, objDto);

        TipoClassificacaoDto tipoClassificacao = TipoClassificacaoDto.construir(obj.getTipoClassificacao());
        objDto.setTipoClassificacao(tipoClassificacao);

        List<ClassificacaoSafraDto> safras = ClassificacaoSafraDto.construir(obj.getSafras());
        objDto.setSafras(safras);

        List<ClassificacaoDetalheDto> detalhes = ClassificacaoDetalheDto.construirAtivos(obj.getDetalhes());
        objDto.setDetalhes(detalhes);

        List<ClassificacaoGrupoProdutoDto> grupoPordutos = ClassificacaoGrupoProdutoDto.construir(obj.getGrupoProdutos());
        objDto.setGrupoProdutos(grupoPordutos);
        
        List<ClassificacaoEstabelecimentoDto> estabelecimentos = ClassificacaoEstabelecimentoDto.construir(obj.getEstabelecimentos());
        objDto.setEstabelecimentos(estabelecimentos);

        return objDto;
    }

    public static Classificacao converterDto(ClassificacaoDto objDto) {
        Classificacao obj = new Classificacao();
        BeanUtils.copyProperties(objDto, obj);

        TipoClassificacao tipoClassificacao = TipoClassificacaoDto.converterDto(objDto.getTipoClassificacao());
        obj.setTipoClassificacao(tipoClassificacao);

        List<ClassificacaoSafra> safra = ClassificacaoSafraDto.converterDto(objDto.getSafras(), obj);
        obj.setSafras(safra);

        List<ClassificacaoDetalhe> detalhes = ClassificacaoDetalheDto.converterDto(objDto.getDetalhes(), obj);
        obj.setDetalhes(detalhes);

        List<ClassificacaoGrupoProduto> grupoProdutos = ClassificacaoGrupoProdutoDto.converterDto(objDto.getGrupoProdutos(), obj);
        obj.setGrupoProdutos(grupoProdutos);
        
        List<ClassificacaoEstabelecimento> estabelecimentos = ClassificacaoEstabelecimentoDto.converterDto(objDto.getEstabelecimentos(), obj);
        obj.setEstabelecimentos(estabelecimentos);

        return obj;
    }
}
