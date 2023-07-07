package br.coop.integrada.api.pa.domain.repository.integration;

import org.springframework.data.jpa.repository.JpaRepository;

import br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;

public interface IntegracaoPaginaRep extends JpaRepository<IntegracaoPagina, Long>{
	void deleteByPaginaEnum(PaginaEnum paginaEnum);
	IntegracaoPagina findByPaginaEnum(PaginaEnum paginaEnum);
	IntegracaoPagina findByPaginaEnumAndFuncionalidadesFuncionalidade(PaginaEnum paginaEnum, FuncionalidadePaginaEnum funcionalidade);
}
