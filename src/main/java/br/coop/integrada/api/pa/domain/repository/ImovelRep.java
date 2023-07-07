package br.coop.integrada.api.pa.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.coop.integrada.api.pa.domain.model.imovel.Imovel;

public interface ImovelRep  extends JpaRepository<Imovel, Long>, JpaSpecificationExecutor<Imovel> {
    Page<Imovel> findByDataInativacaoNull(Pageable pageable);
    Imovel findByMatricula(Long matricula);
}
