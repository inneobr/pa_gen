package br.coop.integrada.api.pa.domain.modelDto.classificacao;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import org.springframework.beans.BeanUtils;
import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoEstabelecimento;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import lombok.Data;

@Data
public class ClassificacaoEstabelecimentoDto  implements Serializable {

    private static final long serialVersionUID = 1L;

	private Long id;

    @NotNull(message = "O campo {idEstabelecimento} é obrigatório")
    private Long idEstabelecimento;
    private String codigo;
    private String nomeFantasia;
    
    public static ClassificacaoEstabelecimentoDto construir(ClassificacaoEstabelecimento obj) {
    	ClassificacaoEstabelecimentoDto objDto = new ClassificacaoEstabelecimentoDto();

        objDto.setId(obj.getId());

        if(obj.getEstabelecimento() != null) {
            objDto.setIdEstabelecimento(obj.getEstabelecimento().getId());
            objDto.setCodigo(obj.getEstabelecimento().getCodigo());
            objDto.setNomeFantasia(obj.getEstabelecimento().getNomeFantasia());
        }

        return objDto;
    }

    public static List<ClassificacaoEstabelecimentoDto> construir(List<ClassificacaoEstabelecimento> objs) {
        return objs.stream().map(classificacaoEstabelecimento -> {
            return ClassificacaoEstabelecimentoDto.construir(classificacaoEstabelecimento);
        }).collect(Collectors.toList());
    }

    public static ClassificacaoEstabelecimento converterDto(ClassificacaoEstabelecimentoDto objDto, Classificacao classificacao) {
        ClassificacaoEstabelecimento obj = new ClassificacaoEstabelecimento();
        BeanUtils.copyProperties(objDto, obj);

        Estabelecimento estabelecimento = new Estabelecimento();
        estabelecimento.setId(objDto.getIdEstabelecimento());
        estabelecimento.setCodigo(objDto.getCodigo());
        estabelecimento.setNomeFantasia(objDto.getNomeFantasia());

        obj.setClassificacao(classificacao);
        obj.setEstabelecimento(estabelecimento);
        
        return obj;
    }

    public static List<ClassificacaoEstabelecimento> converterDto(List<ClassificacaoEstabelecimentoDto> objDtos, Classificacao classificacao) {
        return objDtos.stream().map(classificacaoEstabelecimentoDto -> {
            return ClassificacaoEstabelecimentoDto.converterDto(classificacaoEstabelecimentoDto, classificacao);
        }).collect(Collectors.toList());
    }


}
