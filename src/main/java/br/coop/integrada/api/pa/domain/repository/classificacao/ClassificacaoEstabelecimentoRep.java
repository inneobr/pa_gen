package br.coop.integrada.api.pa.domain.repository.classificacao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoEstabelecimento;


public interface ClassificacaoEstabelecimentoRep extends JpaRepository<ClassificacaoEstabelecimento, Long> {
	Page<ClassificacaoEstabelecimento> findByClassificacaoOrderByIdDesc(Classificacao classificacao, Pageable pageable);
	Page<ClassificacaoEstabelecimento> findByClassificacaoOrderById(Classificacao classificacao, Pageable pageable);
}
