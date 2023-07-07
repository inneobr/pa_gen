package br.coop.integrada.api.pa.domain.repository.semente.produtor;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampo;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeLaudoInspecao;

@Repository
public interface SementeLaudoInspecaoRep extends JpaRepository<SementeLaudoInspecao, Long>, JpaSpecificationExecutor<SementeLaudoInspecao>{
    Optional<SementeLaudoInspecao> findBySafraAndEstabelecimentoAndNumeroLaudoAndOrdemCampoAndGrupoProduto(Integer safra, Estabelecimento estabelecimento, Long numeroLaudo, Integer ordemCampo, GrupoProduto grupoProduto);
    Optional<SementeLaudoInspecao> findBySafraAndEstabelecimentoAndNumeroLaudoAndOrdemCampoAndGrupoProdutoAndClasse(Integer safra, Estabelecimento estabelecimento, Long numeroLaudo, Integer ordemCampo, GrupoProduto grupoProduto, SementeClasse classe);
    List<SementeLaudoInspecao> findBySafraAndEstabelecimentoAndOrdemCampoAndGrupoProduto(Integer safra, Estabelecimento estabelecimento, Integer ordemCampo, GrupoProduto grupoProduto);
    List<SementeLaudoInspecao> findByEstabelecimentoAndSafraAndGrupoProdutoAndClasseAndNumeroLaudo(Estabelecimento estabelecimento, Integer safra, GrupoProduto grupoProduto, SementeClasse classe, Long numeroLaudo);
    Optional<SementeLaudoInspecao> findBySafraAndNumeroLaudoAndOrdemCampoAndGrupoProdutoFmCodigoAndEstabelecimentoCodigo(Integer safra, Long nroLaudo, Integer ordemCampo, String codigoGrupoProduto, String codigoEstabelecimento);
    SementeLaudoInspecao findByIdUnico(String idUnico); 
}
