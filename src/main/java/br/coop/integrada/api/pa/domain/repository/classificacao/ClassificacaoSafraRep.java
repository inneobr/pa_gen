package br.coop.integrada.api.pa.domain.repository.classificacao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoSafra;

public interface ClassificacaoSafraRep extends JpaRepository<ClassificacaoSafra, Long> {
	
	@Query(value = """
			select o.* from classificacao_safra o 
			  where o.id_classificacao = :idClassificacao 
			""", nativeQuery=true)
	public List<ClassificacaoSafra> findByClassificacao(Long idClassificacao);	

}
