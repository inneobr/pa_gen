package br.coop.integrada.api.pa.domain.repository.produto;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.model.produto.VinculoProdutoReferencia;

@Repository
public interface VinculoProdutoReferenciaRep extends JpaRepository<VinculoProdutoReferencia, Long>{
	List<VinculoProdutoReferencia> findByProduto(Produto produto);
}
