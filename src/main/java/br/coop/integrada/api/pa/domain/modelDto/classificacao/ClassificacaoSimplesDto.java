package br.coop.integrada.api.pa.domain.modelDto.classificacao;

import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoSafra;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ClassificacaoSimplesDto implements Serializable {

    private Long id;
    private String descricao;
    private String safras;
    private String tipoClassificacao;
    private Date dataAtualizacao;
    private boolean ativo;
    
    private Date dataIntegracao;
    private String descricaoStatusIntegracao;

    public static ClassificacaoSimplesDto construir(Classificacao obj) {
        ClassificacaoSimplesDto objDto = new ClassificacaoSimplesDto();
        BeanUtils.copyProperties(obj, objDto);
        objDto.setSafras(concatenarSafra(obj.getSafras()));
        String tipoClassificacao = obj.getTipoClassificacao().getTipoClassificacao().getDescricao();
        objDto.setTipoClassificacao(tipoClassificacao);
        

        if(obj.getDataAtualizacao() == null) {
            objDto.setDataAtualizacao(obj.getDataCadastro());
        }
        if(obj.getDataIntegracao() != null) {
        	objDto.setDataIntegracao(obj.getDataIntegracao());
        }
        if(obj.getStatusIntegracao() != null) {
    		objDto.setDescricaoStatusIntegracao(obj.getStatusIntegracao().getDescricao());
    	}

        return objDto;
    }

    public static List<ClassificacaoSimplesDto> construir(List<Classificacao> objs) {
        return objs.stream().map(classificacao -> {
            return ClassificacaoSimplesDto.construir(classificacao);
        }).collect(Collectors.toList());
    }

    private static String concatenarSafra(List<ClassificacaoSafra> safraList) {
        String safras = "";

        for(ClassificacaoSafra item : safraList) {
            if(safras.isEmpty()) {
                safras += item.getSafra();
            }
            else {
                safras += ", " + item.getSafra();
            }
        }

        return safras;
    }
}
