package br.coop.integrada.api.pa.domain.repository.parametros;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.HistoricoParametroEstabelecimento;

public interface HistoricoParametroEstabelecimentoRep extends JpaRepository<HistoricoParametroEstabelecimento, Long>{
    List<HistoricoParametroEstabelecimento> findByEstabelecimento(Estabelecimento estabelecimento);
    Page<HistoricoParametroEstabelecimento> findByEstabelecimentoAndDataInativacaoNull(Estabelecimento estabelecimento, Pageable pageable);
}
