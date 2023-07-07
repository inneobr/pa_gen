package br.coop.integrada.api.pa.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import br.coop.integrada.api.pa.domain.model.imovel.ImovelProdutor;

public interface ImovelProdutorRep extends JpaRepository<ImovelProdutor, Long>, JpaSpecificationExecutor<ImovelProdutor>{	
    Page<ImovelProdutor> findByDataInativacaoNullAndTransferenciaIsFalseOrderByDataCadastroDesc(Pageable pageable);
    List<ImovelProdutor> findByCodProdutorAndDataInativacaoNullAndTransferenciaIsFalseOrderByImovel(String codProdutor);
    Page<ImovelProdutor> findByCodProdutorAndDataInativacaoNullAndTransferenciaIsFalse(String codProdutor, Pageable pageable);
    Page<ImovelProdutor> findByCodProdutorAndDataInativacaoNullAndTransferenciaIsFalseAndImovelMatriculaContainingIgnoreCase(String codProdutor, Long matricula, Pageable pageable);

    List<ImovelProdutor> findByImovel(Imovel imovel);
    void deleteByImovel(Imovel imovel);
    ImovelProdutor findByImovelAndCodProdutorAndDataInativacaoNullAndTransferenciaIsFalse(Imovel imovel, String codProdutor);
    ImovelProdutor findByImovelMatriculaAndCodProdutorAndDataInativacaoNullAndTransferenciaIsFalse(Long matricula, String codProdutor);

    @Query(value = """
            SELECT
                *
            FROM IMOVEL_PRODUTOR IP
            INNER JOIN PRODUTOR P
                ON P.COD_PRODUTOR = IP.COD_PRODUTOR
                AND P.COD_PRODUTOR = :codigoProdutor
            INNER JOIN IMOVEL I
                ON I.ID = IP.ID_IMOVEL
                AND I.BLOQUEADO = 0 /* FALSE */
                AND I.DATA_INATIVACAO IS NULL
            WHERE
                IP.TRANSFERENCIA = 0 /* FALSE */
                AND IP.DATA_INATIVACAO IS NULL
            """,
            nativeQuery = true
    )
    List<ImovelProdutor> buscarPorProdutor(String codigoProdutor);
	ImovelProdutor findByImovelMatriculaAndCodProdutorAndDataInativacaoIsNull(Long matriculaImovel, String codigoProdutor);
	List<ImovelProdutor> findByImovelMatricula(Long matricula);
}
