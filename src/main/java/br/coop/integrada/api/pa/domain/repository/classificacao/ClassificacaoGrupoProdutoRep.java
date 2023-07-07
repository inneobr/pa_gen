package br.coop.integrada.api.pa.domain.repository.classificacao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoGrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;

public interface ClassificacaoGrupoProdutoRep extends JpaRepository<ClassificacaoGrupoProduto, Long> {
	
	@Query(value = """
			select o.* from v_classificacao_grupo_produto o 
			  where o.id_classificacao = :idClassificacao 
			""", nativeQuery=true)
	public List<ClassificacaoGrupoProduto> findByClassificacao(Long idClassificacao);
	
	public ClassificacaoGrupoProduto findByClassificacaoAndGrupoProduto(Classificacao c, GrupoProduto g);

}
