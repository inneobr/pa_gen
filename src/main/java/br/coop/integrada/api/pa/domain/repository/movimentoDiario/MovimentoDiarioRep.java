package br.coop.integrada.api.pa.domain.repository.movimentoDiario;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.coop.integrada.api.pa.domain.model.movimentoDiario.MovimentoDiario;

public interface MovimentoDiarioRep extends JpaRepository<MovimentoDiario, Long>, JpaSpecificationExecutor<MovimentoDiario> {
	MovimentoDiario findByIdUnico(String idUnico);	
	MovimentoDiario findByCodEstabelAndDtMovto(String codEstabel, Date dtMovto);	
	Page<MovimentoDiario> findByCodEstabel(String codEstabel, Pageable pageble); 
	Page<MovimentoDiario> findByCodEstabelAndDtMovtoBetween(String codEstabelecimento, Date dataInicio, Date dataFim, Pageable pageble); 
	Page<MovimentoDiario> findByCodEstabelInAndDtMovtoBetween(List<String> codEstabelecimentos, Date dataInicio, Date dataFim, Pageable pageble); 

}
