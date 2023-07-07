package br.coop.integrada.api.pa.domain.model.produto;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.modelDto.produto.RelacionamentosDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "tipo_produto",
uniqueConstraints = {
		@UniqueConstraint(name = "KEY_UN_NOME_TIPO_PROUTO" , columnNames = { "nome"}),
		@UniqueConstraint(name = "KEY_UN_ID_UNICO_TIPO_PRODUTO" , columnNames = { "id_unico"}), 
})

@NamedNativeQuery(
        name = "TipoProduto.verificarRelacionamentos",
        query = """
            SELECT *
			FROM ( 
					SELECT 'GRUPO PRODUTO' AS tabela, 
					COUNT(gp.ID) AS quantidade
					FROM GRUPO_PRODUTO gp 
					WHERE gp.DATA_INATIVACAO IS NULL 
					AND gp.TIPO_PRODUTO = :idTipoProduto  
				) 
				UNION 
				( 
					SELECT 'SAFRA COMPOSTA' as tabela,
					COUNT(sc.ID) AS quantidade
					FROM SAFRA_COMPOSTA sc 
					WHERE sc.DATA_INATIVACAO IS NULL 
					AND sc.ID_TIPO_PRODUTO = :idTipoProduto 
				)
        """,
        resultSetMapping = "Mapping.VerificarRelacionamentos"
)
@SqlResultSetMapping(
        name = "Mapping.VerificarRelacionamentos",
        classes = {
                @ConstructorResult(
                        targetClass = RelacionamentosDto.class,
                        columns = {
                                @ColumnResult(name = "tabela", type = String.class),
                                @ColumnResult(name = "quantidade", type = Integer.class)
                        }
                )
        }
)
public class TipoProduto extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "Campo {idUnico} obrigatório")
	@Column(name = "id_unico", unique = true)
	private String idUnico;
	

    @NotBlank(message = "Campo {nome} obrigatório")
	@Column(nullable = false, unique = true)
	private String nome;
}
