package br.coop.integrada.api.pa.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.model.HistoricoGenerico;

@Repository
public interface HistoricoGenericoRep  extends JpaRepository<HistoricoGenerico, Long> {

    public Page<HistoricoGenerico> findByRegistroAndPaginaEnumOrderByDataCadastroDesc(Long idRegistro, PaginaEnum paginaEnum, Pageable pageable);
}
