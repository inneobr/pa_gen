package br.coop.integrada.api.pa.domain.repository.rependente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteDesmembramento;


@Repository
public interface RePendenteDesmembramentoRep extends JpaRepository<RePendenteDesmembramento, Long> {
	
	
	

}
