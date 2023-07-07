package br.coop.integrada.api.pa.domain.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.coop.integrada.api.pa.domain.model.ParticipanteBayer;
import br.coop.integrada.api.pa.domain.repository.participante.bayer.ParticipanteBayerQueriesRep;
import br.coop.integrada.api.pa.domain.repository.participante.bayer.ParticipanteBayerRep;
import br.coop.integrada.api.pa.domain.spec.ParticipanteBayerSpecs;

@Repository
public class ParticipanteBayerRepImpl implements ParticipanteBayerQueriesRep {

    @Lazy
    @Autowired
    private ParticipanteBayerRep participanteBayerRep;
    
    @PersistenceContext
	private EntityManager entityManager;

    @Override
    public Page<ParticipanteBayer> findAll(Pageable pageable, String filtro) {
        return participanteBayerRep.findAll(
                ParticipanteBayerSpecs.doNome(filtro)
                .or(ParticipanteBayerSpecs.doCnpj(filtro)),
                pageable);
    }
    
    @Override
    public void insertList(StringBuilder sql) {
    	entityManager.createNativeQuery(sql.toString()).executeUpdate();
    	//entityManager.createQuery(sql.toString()).executeUpdate();
    }
}
