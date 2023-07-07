package br.coop.integrada.api.pa.domain.repository.produtor;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import br.coop.integrada.api.pa.domain.model.Produtor;

public interface ProdutorRep extends JpaRepository<Produtor, Long>, ProdutorQueriesRep, JpaSpecificationExecutor<Produtor>{
    List<Produtor> findByDataInativacaoNull();
    
    Produtor findByCodProdutor(String codigo);
    Produtor findByCpfCnpj(String cpfCnpj);
    List<Produtor> findByCodProdutorContainingIgnoreCaseOrderByNomeAsc(String codProdutor, Pageable pageable);
    List<Produtor> findByCodProdutorContainingIgnoreCaseOrNomeContainingIgnoreCaseOrderByNomeAsc(String nome, String codigo, Pageable pageable);
    List<Produtor> findByNomeContainingIgnoreCaseOrCpfCnpjContainingIgnoreCaseOrNomeAbreviadoContainingIgnoreCaseOrderByNomeAsc(String nome, String cpf, String abreviacao, Pageable pageable);
    Produtor findByCpfCnpjIgnoreCaseAndDataInativacaoIsNull(String cpfCnpj);  
    
    //Irá listar todos produtores que estão setados como participanteBayer e não possuem registro na tabela ParticipanteBayer
    @Transactional
    @Modifying
    @Query(value = """
    		SELECT p.*  
	    	FROM PRODUTOR p 
	    	LEFT JOIN PARTICIPANTE_BAYER pb 
	    	ON p.CPF_CPJ = pb.CNPJ 
	    	WHERE pb.CNPJ is NULL
	    	AND p.PARTICIPANTE_BAYER = 1
    		""", nativeQuery = true)
    public List<Produtor> listarProdutorParticipanteBayerSemRegistroParticipanteBayer();
    
    //Produtor tem o cnpj na tabela ParticipanteBayer e está como false o campo participante bayer
    @Transactional
    @Modifying
    @Query(value = """
    		SELECT p.*  
	    	FROM PRODUTOR p 
	    	INNER JOIN PARTICIPANTE_BAYER pb 
	    	ON p.CPF_CPJ = pb.CNPJ 
	    	WHERE p.PARTICIPANTE_BAYER = 0
    		""", nativeQuery = true)
    public List<Produtor> listarProdutorParticipanteBayerComRegistroParticipanteBayer();
    
    
}
