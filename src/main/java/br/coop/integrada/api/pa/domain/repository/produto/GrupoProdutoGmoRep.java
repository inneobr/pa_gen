package br.coop.integrada.api.pa.domain.repository.produto;

import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProdutoGmo;
import br.coop.integrada.api.pa.domain.model.produto.TipoGmo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrupoProdutoGmoRep extends JpaRepository<GrupoProdutoGmo, Long> {

	Page<GrupoProdutoGmo> findByDataInativacaoNull(Pageable pageable);

	List<GrupoProdutoGmo> findByGrupoProduto(GrupoProduto grupoProduto);

	GrupoProdutoGmo findByTipoGmoAndGrupoProduto(TipoGmo tipoGmo, GrupoProduto grupoProduto);

    GrupoProdutoGmo findByGrupoProdutoAndTipoGmo(GrupoProduto grupoProduto, TipoGmo tipoGmo);
}
