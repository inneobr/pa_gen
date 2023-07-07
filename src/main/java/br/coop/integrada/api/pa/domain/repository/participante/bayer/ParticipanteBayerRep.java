package br.coop.integrada.api.pa.domain.repository.participante.bayer;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import br.coop.integrada.api.pa.domain.enums.OrigemEnum;
import br.coop.integrada.api.pa.domain.model.ParticipanteBayer;


@Repository
public interface ParticipanteBayerRep extends JpaRepository<ParticipanteBayer, Long>, ParticipanteBayerQueriesRep, JpaSpecificationExecutor<ParticipanteBayer> {

    List<ParticipanteBayer> findByOrigem(OrigemEnum origem);
    List<ParticipanteBayer>  findByCnpjIgnoreCase(String cnpj);
    
    @Transactional
    @Modifying
    @Query(value = "UPDATE Produtor SET participanteBayer = false WHERE participanteBayer = true")
	public void inativarTodosParticipantesBayer();

    @Query(value = """
    		 SELECT 
    		 	ID 
    		 FROM PRODUTOR 
    		 WHERE PARTICIPANTE_BAYER = 1
    		""", nativeQuery = true)
    public List<Long> recuperarIdProdutorParticipanteBayer();
    
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM ParticipanteBayer")
	void excluirTodosParticipantesBayer();
    
    @Transactional
    @Modifying
    @Query(value = """
    		INSERT INTO HISTORICO_GENERICO (DATA_ATUALIZACAO, DATA_CADASTRO, MOVIMENTO, OBSERVACAO, TIPO, ID_REGISTRO, USERNAME) 
    			SELECT 
    				CURRENT_TIMESTAMP AS DATA_ATUALIZACAO, 
    				CURRENT_TIMESTAMP AS DATA_CADASTRO, 
    				'Participante Bayer', 
    				'Excluir importação Bayer', 
    				'PRODUTOR', 
    				ID, 
    				:userName
    			FROM PRODUTOR 
    			WHERE PARTICIPANTE_BAYER = 1
    		""", nativeQuery = true)
    public void gerarHistoricoGenericoExcluirTudo(String userName); 
    
    @Transactional
    @Modifying
    @Query(value = """
    		INSERT INTO HISTORICO_GENERICO (DATA_ATUALIZACAO, DATA_CADASTRO, MOVIMENTO, OBSERVACAO, TIPO, ID_REGISTRO, USERNAME) 
    			SELECT 
    				CURRENT_TIMESTAMP AS DATA_ATUALIZACAO, 
    				CURRENT_TIMESTAMP AS DATA_CADASTRO, 
    				'Participante Bayer', 
    				'Excluir importação Bayer', 
    				'PRODUTOR', 
    				p.ID, 
    				:userName
    			FROM PRODUTOR p 
    			INNER JOIN PARTICIPANTE_BAYER pb
    			ON p.CPF_CPJ = pb.CNPJ 
    			WHERE PARTICIPANTE_BAYER = 1
    			AND pb.ORIGEM = :origem
    		""", nativeQuery = true)
    public void gerarHistoricoGenericoExcluirTudoOrigem(String userName, String origem);
    
    
    //Possui CNPJ na tabela Bayer e não possui registro correspondente do produtor na tabela Produtor.
    @Transactional
    @Modifying
    @Query(value = """
    		SELECT pb.*  
	    	FROM PRODUTOR p 
	    	RIGHT JOIN PARTICIPANTE_BAYER pb 
	    	ON p.CPF_CPJ = pb.CNPJ 
	    	WHERE p.CPF_CPJ  is NULL
    		""", nativeQuery = true)
    public List<ParticipanteBayer> listarParticipanteBayerSemProdutor();
	
    @Transactional
    @Modifying
    @Query(value = """
    		UPDATE Produtor SET PARTICIPANTE_BAYER  = 0
			WHERE PARTICIPANTE_BAYER = 1
			AND CPF_CPJ IN (SELECT pb.CNPJ from PARTICIPANTE_BAYER pb where pb.ORIGEM = :origem) 
    		""", nativeQuery = true)
    public void inativarTodosParticipantesBayerPorOrigem(String origem);
	
    @Transactional
    @Modifying
    @Query(value = """
    		DELETE FROM ParticipanteBayer WHERE ORIGEM = :origem
    		""")
    void excluirTodosParticipantesBayerPorOrigem(String origem);
    
    //Modifica o campo participanteBayer como true na tabela produtor que esteja na tabela participanteBayer
    @Transactional
    @Modifying
    @Query(value = """
    			UPDATE PRODUTOR  p 
    				SET p.PARTICIPANTE_BAYER = 1  
               	WHERE p.CPF_CPJ IN (
                	SELECT pb.CNPJ FROM PARTICIPANTE_BAYER pb WHERE pb.origem = :origem
            	)
            	AND p.DATA_INATIVACAO is null
    		""", nativeQuery = true)
	public void definirProdutorComoParticipanteBayer(String origem);
    	
	@Transactional
    @Modifying
    @Query(value = """
    		INSERT INTO HISTORICO_GENERICO (DATA_ATUALIZACAO, DATA_CADASTRO, MOVIMENTO, OBSERVACAO, TIPO, ID_REGISTRO, USERNAME) 
			SELECT 
				CURRENT_TIMESTAMP AS DATA_ATUALIZACAO, 
				CURRENT_TIMESTAMP AS DATA_CADASTRO, 
				'Participante Bayer', 
				:motivo, 
				'PRODUTOR', 
				p.ID, 
				:userName
			FROM PRODUTOR p 
			INNER JOIN PARTICIPANTE_BAYER pb
			ON p.CPF_CPJ  = pb.CNPJ  
			WHERE pb.DATA_CADASTRO > :dataCadastro
    		""", nativeQuery = true)
    public void gerarHistoricoGenericoEmLote(String userName, Date dataCadastro, String motivo);
	
	@Transactional
    @Modifying
    @Query(value = """
    		INSERT INTO HISTORICO_GENERICO (DATA_ATUALIZACAO, DATA_CADASTRO, MOVIMENTO, OBSERVACAO, TIPO, ID_REGISTRO, USERNAME) 
			SELECT 
				CURRENT_TIMESTAMP AS DATA_ATUALIZACAO, 
				CURRENT_TIMESTAMP AS DATA_CADASTRO, 
				'Participante Bayer', 
				:motivo, 
				'PRODUTOR', 
				p.ID, 
				:userName
			FROM PRODUTOR p 
			INNER JOIN PARTICIPANTE_BAYER pb
			ON p.CPF_CPJ  = pb.CNPJ  
			WHERE pb.ORIGEM = :origem
    		""", nativeQuery = true)
    public void gerarHistoricoGenericoEmLote(String userName, String origem, String motivo);
	
    
    
    
}
