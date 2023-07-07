package br.coop.integrada.api.pa.domain.model.naturezaTributaria;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.OneToMany;
import org.hibernate.annotations.Where;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "natureza_tributaria")
public class NaturezaTributaria extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "Campo { codigo } é obrigatório")
	@ComparaObjeto(nome = "codigo:", verifica = true)
	@Column(name = "codigo",  unique=true, nullable = false)
	private Integer codigo;
	
	@NotNull(message = "Campo { descricao } é obrigatório")
	@ComparaObjeto(nome = "descricao:", verifica = true)
	@Column(name = "descricao",  unique=true, nullable = false)
	private String descricao;
	
	@ComparaObjeto(nome = "idRegistro:", verifica = true)
	@Column(name="id_registro")
	private String idRegistro;
	
	@ComparaObjeto(nome = "natFixCoop:", verifica = true)
	@Column(name="nat_fix_coop")
	private String natFixCoop;
	
	@ComparaObjeto(nome = "serFixCoop:", verifica = true)
	@Column(name="ser_fix_coop")
	private String serFixCoop;
	
	@ComparaObjeto(nome = "modNatFixCoop:", verifica = true)
	@Column(name="mod_nat_fix_coop")
	private String modNatFixCoop;
	
	@ComparaObjeto(nome = "natFixTerc:", verifica = true)
	@Column(name="nat_fix_terc")
	private String natFixTerc;
	
	@ComparaObjeto(nome = "serFixTerc:", verifica = true)
	@Column(name="ser_fix_terc")
	private String serFixTerc;
	
	@ComparaObjeto(nome = "modNatFixTerc:", verifica = true)
	@Column(name="mod_nat_fix_terc")
	private String modNatFixTerc;
	
	@ComparaObjeto(nome = "natFixPjCnfCoop:", verifica = true)
	@Column(name="nat_fix_pj_cnf_coop")
	private String natFixPjCnfCoop;
	
	@ComparaObjeto(nome = "serFixPjCnfCoop:", verifica = true)
	@Column(name="ser_fix_pj_cnf_coop")
	private String serFixPjCnfCoop;
	
	@ComparaObjeto(nome = "modFixPjCnfCoop:", verifica = true)
	@Column(name="mod_fix_pj_cnf_coop")
	private String modFixPjCnfCoop;
	
	@ComparaObjeto(nome = "natFixPjCnfTerc:", verifica = true)
	@Column(name="nat_fix_pj_cnf_terc")
	private String natFixPjCnfTerc;
	
	@ComparaObjeto(nome = "serFixPjCnfTerc:", verifica = true)
	@Column(name="ser_fix_pj_cnf_terc")
	private String serFixPjCnfTerc;
	
	@ComparaObjeto(nome = "modFixPjCnfTerc:", verifica = true)
	@Column(name="mod_fix_pj_cnf_terc")
	private String modFixPjCnfTerc;
	
	@ComparaObjeto(nome = "Nat. fixação PJ sem NF cooperado:", verifica = true)
	@Column(name="nat_fix_pj_snf_coop")
	private String natFixPjSnfCoop;
	
	@ComparaObjeto(nome = "Serv. fixação sem PJ NF cooperado:", verifica = true)
	@Column(name="ser_fix_pj_snf_coop")
	private String serFixPjSnfCoop;
	
	@ComparaObjeto(nome = "modFixPjSnfCoop:", verifica = true)
	@Column(name="mod_fix_pj_snf_coop")
	private String modFixPjSnfCoop;
	
	@ComparaObjeto(nome = "Nat. fixação PJ sem NF terceiro:", verifica = true)
	@Column(name="nat_fix_pj_snf_terc")
	private String natFixPjSnfTerc;
	
	@ComparaObjeto(nome = "Serv. fixação sem PJ NF terceiro:", verifica = true)
	@Column(name="ser_fix_pj_snf_terc")
	private String serFixPjSnfTerc;
	
	@ComparaObjeto(nome = "modFixPjSnfTerc:", verifica = true)
	@Column(name="mod_fix_pj_snf_terc")
	private String modFixPjSnfTerc;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "naturezaTributaria", orphanRemoval = true, cascade = CascadeType.ALL)
	@Where(clause = "data_inativacao is null")
	private List<NaturezaTributariaGrupoProduto> grupoProduto;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "naturezaTributaria", orphanRemoval = true, cascade = CascadeType.ALL)
	@Where(clause = "data_inativacao is null")
	private List<NaturezaTributariaEstabelecimento> estabelecimentos;
}
