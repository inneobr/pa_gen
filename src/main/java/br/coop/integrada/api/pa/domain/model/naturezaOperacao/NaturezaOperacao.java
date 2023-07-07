package br.coop.integrada.api.pa.domain.model.naturezaOperacao;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;

import javax.persistence.Column;

import javax.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "natureza_operacao")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class NaturezaOperacao extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "Campo {codGrupo} obrigatório")
	@Column(name = "cod_grupo", unique=true)
	@ComparaObjeto(nome = "codGrupo")
	private Integer codGrupo;
	
	@NotBlank(message = "Campo {descricao} obrigatório")
	@ComparaObjeto(nome = "descricao")
	@Column(name = "descricao")
	private String descricao;
	
	@Column(name = "nat_ent_coop")
	@ComparaObjeto(nome = "natEntCoop")
	private String natEntCoop;
	
	@Column(name = "ser_ent_coop")
	@ComparaObjeto(nome = "natEntCoop")
	private String serEntCoop;
	
	@Column(name = "mod_nat_ent_coop")
	@ComparaObjeto(nome = "modNatEntCoop")
	private String modNatEntCoop;
	
	@Column(name = "nat_dev_ent_coop")
	@ComparaObjeto(nome = "natDevEntCoop")
	private String natDevEntCoop;
	
	@Column(name = "ser_dev_ent_coop")
	@ComparaObjeto(nome = "serDevEntCoop")
	private String serDevEntCoop;
	
	@Column(name = "mod_dev_ent_coop")
	@ComparaObjeto(nome = "modDevEntCoop")
	private String modDevEntCoop;
	
	@Column(name = "nat_fix_coop")
	@ComparaObjeto(nome = "natFixCoop")
	private String natFixCoop;
	
	@Column(name = "ser_fix_coop")
	@ComparaObjeto(nome = "serFixCoop")
	private String serFixCoop;
	
	@Column(name = "mod_nat_fix_coop")
	@ComparaObjeto(nome = "modNatFixCoop")
	private String modNatFixCoop;	
	
	@Column(name = "nat_fix_sem_coop")
	@ComparaObjeto(nome = "natFixSemCoop")
	private String natFixSemCoop;
	
	@Column(name = "ser_fix_sem_coop")
	@ComparaObjeto(nome = "serFixSemCoop")
	private String serFixSemCoop;
	
	@Column(name = "mod_nat_fix_sem_coop")
	@ComparaObjeto(nome = "modNatFixSemCoop")
	private String modNatFixSemCoop;
	
	@Column(name = "nat_dev_fix_coop")
	@ComparaObjeto(nome = "natDevFixCoop")
	private String natDevFixCoop;
	
	@Column(name = "ser_dev_fix_coop")
	@ComparaObjeto(nome = "serDevFixCoop")
	private String serDevFixCoop;
	
	@Column(name = "mod_dev_fix_coop")
	@ComparaObjeto(nome = "modDevFixCoop")
	private String modDevFixCoop;
	
	@Column(name = "nat_ent_terc")
	@ComparaObjeto(nome = "natEntTerc")
	private String natEntTerc;
	
	@Column(name = "ser_ent_terc")
	@ComparaObjeto(nome = "serEntTerc")
	private String serEntTerc;
	
	@Column(name = "mod_nat_ent_terc")
	@ComparaObjeto(nome = "modNatEntTerc")
	private String modNatEntTerc;
	
	@Column(name = "nat_dev_ent_terc")
	@ComparaObjeto(nome = "natDevEntTerc")
	private String natDevEntTerc;
	
	@Column(name = "ser_dev_ent_terc")
	@ComparaObjeto(nome = "serDevEntTerc")
	private String serDevEntTerc;
	
	@Column(name = "mod_dev_ent_terc")
	@ComparaObjeto(nome = "modDevEntTerc")
	private String modDevEntTerc;
	
	@Column(name = "nat_fix_terc")
	@ComparaObjeto(nome = "natFixTerc")
	private String natFixTerc;
	
	@Column(name = "ser_fix_terc")
	@ComparaObjeto(nome = "serFixTerc")
	private String serFixTerc;
	
	@Column(name = "mod_nat_fix_terc")
	@ComparaObjeto(nome = "modNatFixTerc")
	private String modNatFixTerc;
	
	@Column(name = "nat_fix_sem_terc")
	@ComparaObjeto(nome = "natFixSemTerc")
	private String natFixSemTerc;
	
	@Column(name = "ser_fix_sem_terc")
	@ComparaObjeto(nome = "serFixSemTerc")
	private String serFixSemTerc;
	
	@Column(name = "mod_nat_fix_sem_terc")
	@ComparaObjeto(nome = "modNatFixSemTerc")
	private String modNatFixSemTerc;
	
	@Column(name = "nat_dev_fix_terc")
	@ComparaObjeto(nome = "natDevFixTerc")
	private String natDevFixTerc;
	
	@Column(name = "ser_dev_fix_terc")
	@ComparaObjeto(nome = "serDevFixTerc")
	private String serDevFixTerc;
	
	@Column(name = "mod_nat_dev_fix_terc")
	@ComparaObjeto(nome = "modNatDevFixTerc")
	private String modNatDevFixTerc;
	
	@Column(name = "nat_ent_pj_cnf_coop")
	@ComparaObjeto(nome = "natEntPjCnfCoop")
	private String natEntPjCnfCoop;
	
	@Column(name = "ser_ent_pj_cnf_coop")
	@ComparaObjeto(nome = "serEntPjCnfCoop")
	private String serEntPjCnfCoop;
	
	@Column(name = "mod_nat_ent_pj_cnf_coop")
	@ComparaObjeto(nome = "modNatEntPjCnfCoop")
	private String modNatEntPjCnfCoop;
	
	@Column(name = "nat_dev_pj_cnf_coop")
	@ComparaObjeto(nome = "natDevPjCnfCoop")
	private String natDevPjCnfCoop;
	
	@Column(name = "ser_dev_pj_cnf_coop")
	@ComparaObjeto(nome = "serDevPjCnfCoop")
	private String serDevPjCnfCoop;
	
	@Column(name = "mod_dev_pj_cnf_coop")
	@ComparaObjeto(nome = "modDevPjCnfCoop")
	private String modDevPjCnfCoop;
	
	@Column(name = "nat_fix_pj_cnf_coop")
	@ComparaObjeto(nome = "natFixPjCnfCoop")
	private String natFixPjCnfCoop;
	
	@Column(name = "ser_fix_pj_cnf_coop")
	@ComparaObjeto(nome = "serFixPjCnfCoop")
	private String serFixPjCnfCoop;
	
	@Column(name = "mod_fix_pj_cnf_coop")
	@ComparaObjeto(nome = "modFixPjCnfCoop")
	private String modFixPjCnfCoop;
	
	@Column(name = "nat_fix_sem_pj_cnf_coop")
	@ComparaObjeto(nome = "natFixSemPjCnfCoop")
	private String natFixSemPjCnfCoop;
	
	@Column(name = "ser_fix_sem_pj_cnf_coop")
	@ComparaObjeto(nome = "serFixSemPjCnfCoop")
	private String serFixSemPjCnfCoop;
	
	@Column(name = "mod_fix_sem_pj_cnj_coop")
	@ComparaObjeto(nome = "modFixSemPjCnfCoop")
	private String modFixSemPjCnfCoop;
	
	@Column(name = "nat_dev_fix_pj_cnf_coop")
	@ComparaObjeto(nome = "natDevFixPjCnfCoop")
	private String natDevFixPjCnfCoop;
	
	@Column(name = "ser_dev_fix_pj_cnf_coop")
	@ComparaObjeto(nome = "serDevFixPjCnfCoop")
	private String serDevFixPjCnfCoop;
	
	@Column(name = "mod_dev_fix_pj_cnj_coop")
	@ComparaObjeto(nome = "modDevFixPjCnjCoop")
	private String modDevFixPjCnfCoop;
	
	@Column(name = "nat_ent_pj_snf_coop")
	@ComparaObjeto(nome = "natEntPjSnfCoop")
	private String natEntPjSnfCoop;
	
	@Column(name = "ser_ent_pj_snf_coop")
	@ComparaObjeto(nome = "serEntPjSnfCoop")
	private String serEntPjSnfCoop;
	
	@Column(name = "mod_nat_ent_pj_snf_coop")
	@ComparaObjeto(nome = "modNatEntPjSnfCoop")
	private String modNatEntPjSnfCoop;
	
	@Column(name = "nat_dev_pj_snf_coop")
	@ComparaObjeto(nome = "natDevPjSnfCoop")
	private String natDevPjSnfCoop;
	
	@Column(name = "ser_dev_pj_snf_coop")
	@ComparaObjeto(nome = "serDevPjSnfCoop")
	private String serDevPjSnfCoop;
	
	@Column(name = "mod_dev_pj_snf_coop")
	@ComparaObjeto(nome = "modDevPjSnfCoop")
	private String modDevPjSnfCoop;
	
	@Column(name = "nat_fix_pj_snf_coop")
	@ComparaObjeto(nome = "natFixPjSnfCoop")
	private String natFixPjSnfCoop;
	
	@Column(name = "ser_fix_pj_snf_coop")
	@ComparaObjeto(nome = "serFixPjSnfCoop")
	private String serFixPjSnfCoop;
	
	@Column(name = "mod_fix_pj_snf_coop")
	@ComparaObjeto(nome = "modFixPjSnfCoop")
	private String modFixPjSnfCoop;
	
	@Column(name = "nat_fix_sem_pj_snf_coop")
	@ComparaObjeto(nome = "natFixSemPjSnfCoop")
	private String natFixSemPjSnfCoop;
	
	@Column(name = "ser_fix_sem_pj_snf_coop") 
	@ComparaObjeto(nome = "serFixSemPjSnfCoop")
	private String serFixSemPjSnfCoop;
	
	@Column(name = "mod_fix_sem_pj_snf_coop")
	@ComparaObjeto(nome = "modFixSemPjSnfCoop")
	private String modFixSemPjSnfCoop;
	
	@Column(name = "nat_dev_fix_pj_snf_coop")
	@ComparaObjeto(nome = "natDevFixPjSnfCoop")
	private String natDevFixPjSnfCoop;
	
	@Column(name = "ser_dev_fix_pj_snf_coop")
	@ComparaObjeto(nome = "serDevFixPjSnfCoop")
	private String serDevFixPjSnfCoop;
	
	@Column(name = "mod_dev_fix_pj_snf_coop")
	@ComparaObjeto(nome = "modDevFixPjSnfCoop")
	private String modDevFixPjSnfCoop;
	
	@Column(name = "nat_ent_pj_cnf_terc")
	@ComparaObjeto(nome = "natEntPjCnfTerc")
	private String natEntPjCnfTerc;
	
	@Column(name = "ser_ent_pj_cnf_terc")
	@ComparaObjeto(nome = "serEntPjCnfTerc")
	private String serEntPjCnfTerc;
	
	@Column(name = "mod_nat_ent_pj_cnf_terc")
	@ComparaObjeto(nome = "modNatEntPjCnfTerc")
	private String modNatEntPjCnfTerc;
	
	@Column(name = "nat_dev_pj_cnf_terc")
	@ComparaObjeto(nome = "natDevPjCnfTerc")
	private String natDevPjCnfTerc;
	
	@Column(name = "ser_dev_pj_cnf_terc")
	@ComparaObjeto(nome = "serDevPjCnfTerc")
	private String serDevPjCnfTerc;
	
	@Column(name = "mod_dev_pj_cnf_terc")
	@ComparaObjeto(nome = "modDevPjCnfTerc")
	private String modDevPjCnfTerc;
	
	@Column(name = "nat_fix_pj_cnf_terc")
	@ComparaObjeto(nome = "natFixPjCnfTerc")
	private String natFixPjCnfTerc;
	
	@Column(name = "ser_fix_pj_cnf_terc")
	@ComparaObjeto(nome = "serFixPjCnfTerc")
	private String serFixPjCnfTerc;
	
	@Column(name = "mod_fix_pj_cnf_terc")
	@ComparaObjeto(nome = "modFixPjCnfTerc")
	private String modFixPjCnfTerc;
	
	@Column(name = "nat_fix_sem_pj_cnf_terc")
	@ComparaObjeto(nome = "natFixSemPjCnfTerc")
	private String natFixSemPjCnfTerc;
	
	@Column(name = "ser_fix_sem_pj_cnf_terc")
	@ComparaObjeto(nome = "serFixSemPjCnfTerc")
	private String serFixSemPjCnfTerc;
	
	@Column(name = "mod_fix_sem_pj_cnf_terc")
	@ComparaObjeto(nome = "modFixSemPjCnfTerc")
	private String modFixSemPjCnfTerc;
	
	@Column(name = "nat_dev_fix_pj_cnf_terc")
	@ComparaObjeto(nome = "natDevFixPjCnfTerc")
	private String natDevFixPjCnfTerc;
	
	@Column(name = "ser_dev_fix_pj_cnf_terc")
	@ComparaObjeto(nome = "serDevFixPjCnfTerc")
	private String serDevFixPjCnfTerc;
	
	@Column(name = "mod_dev_fix_pj_cnf_terc")
	@ComparaObjeto(nome = "modDevFixPjCnfTerc")
	private String modDevFixPjCnfTerc;
	
	@Column(name = "nat_ent_pj_snf_terc")
	@ComparaObjeto(nome = "natEntPjSnfTerc")
	private String natEntPjSnfTerc;
	
	@Column(name = "ser_ent_pj_snf_terc")
	@ComparaObjeto(nome = "serEntPjSnfTerc")
	private String serEntPjSnfTerc;
	
	@Column(name = "mod_ent_pj_snf_terc")
	@ComparaObjeto(nome = "modEntPjSnfTerc")
	private String modEntPjSnfTerc;
	
	@Column(name = "nat_dev_pj_snf_terc")
	@ComparaObjeto(nome = "natDevPjSnfTerc")
	private String natDevPjSnfTerc;
	
	@Column(name = "ser_dev_pj_snf_terc")
	@ComparaObjeto(nome = "serDevPjSnfTerc")
	private String serDevPjSnfTerc;
	
	@Column(name = "mod_dev_pj_snf_terc")
	@ComparaObjeto(nome = "modDevPjSnfTerc")
	private String modDevPjSnfTerc;
	
	@Column(name = "nat_fix_pj_snf_terc")
	@ComparaObjeto(nome = "natFixPjSnfTerc")
	private String natFixPjSnfTerc;
	
	@Column(name = "ser_fix_pj_snf_terc")
	@ComparaObjeto(nome = "serFixPjSnfTerc")
	private String serFixPjSnfTerc;
	
	@Column(name = "mod_fix_pj_snf_terc")
	@ComparaObjeto(nome = "modFixPjSnfTerc")
	private String modFixPjSnfTerc;
	
	@Column(name = "nat_fix_sem_pj_snf_tec")
	@ComparaObjeto(nome = "natFixSemPjSnfTec")
	private String natFixSemPjSnfTec;
	
	@Column(name = "ser_fix_sem_pj_snf_terc")
	@ComparaObjeto(nome = "serFixSemPjSnfTerc")
	private String serFixSemPjSnfTerc;
	
	@Column(name = "mod_fix_sem_pj_snf_terc")
	@ComparaObjeto(nome = "modFixSemPjSnfTerc")
	private String modFixSemPjSnfTerc;

	@Column(name = "nat_dev_fix_pj_snf_terc")
	@ComparaObjeto(nome = "natDevFixPjSnfTerc")
	private String natDevFixPjSnfTerc;
	
	@Column(name = "ser_dev_fix_pj_snf_terc")
	@ComparaObjeto(nome = "serDevFixPjSnfTerc")
	private String serDevFixPjSnfTerc;	
	
	@Column(name = "mod_dev_fix_pj_snf_terc")
	@ComparaObjeto(nome = "modDevFixPjSnfTerc")
	private String modDevFixPjSnfTerc;
	
	public String getCodGrupoDescricao() {
		return codGrupo.toString() + " - " + descricao;
	}
}