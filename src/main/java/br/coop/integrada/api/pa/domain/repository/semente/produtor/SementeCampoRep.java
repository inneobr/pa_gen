package br.coop.integrada.api.pa.domain.repository.semente.produtor;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampo;
import br.coop.integrada.api.pa.domain.modelDto.semente.ZoomCampoSementeDto;

@Repository
public interface SementeCampoRep extends JpaRepository<SementeCampo, Long>, JpaSpecificationExecutor<SementeCampo>{

	SementeCampo findByIdUnico(String idUnico);	
    Optional<SementeCampo> findBySafraAndOrdemCampoAndEstabelecimentoAndGrupoProdutoAndClasse(Integer safra, Integer ordemCampo, Estabelecimento estabelecimento, GrupoProduto grupoProduto, SementeClasse classe);

    @Query(nativeQuery = true)
    ZoomCampoSementeDto findBySafraAndCodigoEstabelecimentoAndCodigoGrupoProduto(Integer safra, String codigoEstabelecimento, String codigoGrupoProduto);
}
