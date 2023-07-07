package br.coop.integrada.api.pa.domain.repository.parametros;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.parametros.safra.composta.SafraCompostaEstabelecimento;

@Repository
public interface SafraCompostaEstabelecimentoRep extends JpaRepository<SafraCompostaEstabelecimento, Long> {

	List<SafraCompostaEstabelecimento> findBySafraCompostaId(Long safraCompostaId);
	SafraCompostaEstabelecimento findBySafraCompostaIdAndEstabelecimentoCodigo(Long safraCompostaId, String codigoEstabelecimento);
	List<SafraCompostaEstabelecimento> findBySafraCompostaIdAndStatusIntegracao(Long idSafraComposta, StatusIntegracao status);
	List<SafraCompostaEstabelecimento> findBySafraCompostaIdAndDataInativacaoIsNull(Long safraCompostaId);
}
