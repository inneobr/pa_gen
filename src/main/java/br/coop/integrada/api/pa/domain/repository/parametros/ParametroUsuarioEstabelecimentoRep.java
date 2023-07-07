package br.coop.integrada.api.pa.domain.repository.parametros;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.ParametrosUsuarioEstabelecimento;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;

public interface ParametroUsuarioEstabelecimentoRep extends JpaRepository<ParametrosUsuarioEstabelecimento, Long>, JpaSpecificationExecutor<ParametrosUsuarioEstabelecimento> {
	Page<ParametrosUsuarioEstabelecimento> findByUsuarioOrderByEstabelecimentoIdAsc(Usuario usuario, Pageable pageable);
	List<ParametrosUsuarioEstabelecimento> findByUsuarioOrderByEstabelecimentoCodigoAsc(Usuario usuario);
	ParametrosUsuarioEstabelecimento findByUsuarioAndEstabelecimento(Usuario usuario, Estabelecimento estabelecimento);
	List<ParametrosUsuarioEstabelecimento> findByUsuario(Usuario usuario);
	
	@Query(value = """
			SELECT
				*
			FROM PARAMETROS_USUARIO_ESTABELECIMENTO PUE
			INNER JOIN ESTABELECIMENTO E
				ON E.ID = PUE.ID_ESTABELECIMENTO
				AND E.CODIGO = :codigoEstabelecimento
			INNER JOIN USUARIO U
				ON U.ID = PUE.ID_USUARIO
				AND UPPER(U.COD_USUARIO) = UPPER(:codigoUsuario)
			""",
			nativeQuery = true
	)
	ParametrosUsuarioEstabelecimento findByCodigoUsuarioAndCodigoEstabelecimento(String codigoUsuario, String codigoEstabelecimento);
	ParametrosUsuarioEstabelecimento findByUsuarioCodUsuarioContainingIgnoreCaseAndEstabelecimentoCodigo(String codUsuario, String codEstabelecimento);
}
