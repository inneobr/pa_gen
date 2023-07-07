package br.coop.integrada.api.pa.domain.repository.rependente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.model.rependente.RePendente;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteItem;

@Repository
public interface RePendenteItemRep extends JpaRepository<RePendenteItem, Long>{

	Optional<RePendenteItem> findByRePendenteAndProdutoCodItemAndReferencia(RePendente rePendente, String codigoProduto, String referencia);
}
