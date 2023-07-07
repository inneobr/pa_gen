package br.coop.integrada.api.pa.domain.repository.participante.bayer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.coop.integrada.api.pa.domain.model.ParticipanteBayer;

public interface ParticipanteBayerQueriesRep {

    Page<ParticipanteBayer> findAll(Pageable pageable, String filtro);
    public void insertList(StringBuilder sql);
}
