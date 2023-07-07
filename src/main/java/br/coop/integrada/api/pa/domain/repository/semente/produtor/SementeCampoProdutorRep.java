package br.coop.integrada.api.pa.domain.repository.semente.produtor;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampo;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampoProdutor;

@Repository
public interface SementeCampoProdutorRep extends JpaRepository<SementeCampoProdutor, Long>, JpaSpecificationExecutor<SementeCampoProdutor>{
	
	SementeCampoProdutor findByIdUnico(String idUnico);	

    Optional<SementeCampoProdutor> findBySafraAndOrdemCampoAndProdutorAndEstabelecimentoAndGrupoProdutoAndClasse(Integer safra, Integer ordemCampo, Produtor produtor, Estabelecimento estabelecimento, GrupoProduto grupoProduto, SementeClasse classe);

    List<SementeCampoProdutor> findBySafraAndOrdemCampoAndEstabelecimentoAndGrupoProdutoAndClasse(Integer safra, Integer ordemCampo, Estabelecimento estabelecimento, GrupoProduto grupoProduto, SementeClasse classe);
}
