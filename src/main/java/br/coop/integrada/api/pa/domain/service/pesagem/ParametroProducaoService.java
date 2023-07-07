package br.coop.integrada.api.pa.domain.service.pesagem;

import br.coop.integrada.api.pa.domain.model.parametros.ParametroProducao;
import br.coop.integrada.api.pa.domain.modelDto.producao.ParametroProducaoDto;
import br.coop.integrada.api.pa.domain.repository.parametros.ParametroProducaoRep;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParametroProducaoService {

    @Autowired
    private ParametroProducaoRep parametroProducaoRep;

    public ParametroProducao salvar(ParametroProducaoDto parametroProducaoDto) {
        ParametroProducao parametroProducaoAtual = buscar();

        if(parametroProducaoAtual == null) {
            parametroProducaoAtual = new ParametroProducao();
        }

        BeanUtils.copyProperties(parametroProducaoDto, parametroProducaoAtual);

        return parametroProducaoRep.save(parametroProducaoAtual);
    }

    @SuppressWarnings("null")
    public ParametroProducao buscar() {
        List<ParametroProducao> parametros = parametroProducaoRep.findAll();
        if(parametros != null && parametros.isEmpty()) return null;
        return parametros.get(0);
    }
}
