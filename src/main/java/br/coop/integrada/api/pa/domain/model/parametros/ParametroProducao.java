package br.coop.integrada.api.pa.domain.model.parametros;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parametro_producao")
public class ParametroProducao extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Length(max = 3, message = "Campo {codEspeciePermuta} possui o limite de 3 caracteres")
    @Column(name = "cod_especie_permuta", length = 3)
    private String codEspeciePermuta;

    @Length(max = 5, message = "Campo {tabelaFixar} possui o limite de 5 caracteres")
    @Column(name = "tabela_fixar", length = 5)
    private String tabelaFixar;

    @Length(max = 20, message = "Campo {contaTransitoria} possui o limite de 20 caracteres")
    @Column(name = "conta_transitoria", length = 20)
    private String contaTransitoria;

    @Length(max = 8, message = "Campo {contaDevFechar} possui o limite de 8 caracteres")
    @Column(name = "conta_dev_fechar", length = 8)
    private String contaDevFechar;

    @Length(max = 8, message = "Campo {centroCustoAva} possui o limite de 8 caracteres")
    @Column(name = "centro_custo_ava", length = 8)
    private String centroCustoAva;

    @Column(name = "taxa_administracao")
    private BigDecimal taxaAdministracao;

    @Column(name = "taxa_retencao")
    private BigDecimal taxaRetencao;

    @Column(name = "taxa_inss_pj")
    private BigDecimal taxaInssPj;

    @Column(name = "taxa_inss_pf")
    private BigDecimal taxaInssPf;

    @Column(name = "taxa_senar_pf")
    private BigDecimal taxaSenarPf;

    @Length(max = 2, message = "Campo {codEspecieQuebra} possui o limite de 2 caracteres")
    @Column(name = "cod_especie_quebra", length = 2)
    private String codEspecieQuebra;

    @Length(max = 2, message = "Campo {codEspecieSeguro} possui o limite de 2 caracteres")
    @Column(name = "cod_especie_seguro", length = 2)
    private String codEspecieSeguro;

    @Length(max = 2, message = "Campo {codEspecieManutencao} possui o limite de 2 caracteres")
    @Column(name = "cod_especie_manutencao", length = 2)
    private String codEspecieManutencao;

    @Length(max = 2, message = "Campo {codEspecieSecagem} possui o limite de 2 caracteres")
    @Column(name = "cod_especie_secagem", length = 2)
    private String codEspecieSecagem;

    @Length(max = 2, message = "Campo {codEspecieAdmin} possui o limite de 2 caracteres")
    @Column(name = "cod_especie_admin", length = 2)
    private String codEspecieAdmin;

    @Length(max = 2, message = "Campo {codEspecieCapital} possui o limite de 2 caracteres")
    @Column(name = "cod_especie_capital", length = 2)
    private String codEspecieCapital;

    @Length(max = 2, message = "Campo {codEspecieSenar} possui o limite de 2 caracteres")
    @Column(name = "cod_especie_senar", length = 2)
    private String codEspecieSenar;

    @Length(max = 2, message = "Campo {codEspecieFam} possui o limite de 2 caracteres")
    @Column(name = "cod_especie_fam", length = 2)
    private String codEspecieFam;

    @Length(max = 2, message = "Campo {codEspecieFechamento} possui o limite de 2 caracteres")
    @Column(name = "cod_especie_fechamento", length = 2)
    private String codEspecieFechamento;

    @Column(name = "dia_vencimento_inss")
    private Integer diaVencimentoInss;

    @Column(name = "dia_vencimento_senar")
    private Integer diaVencimentoSenar;

    @Column(name = "taxa_senar_pj")
    private BigDecimal taxaSenarPj;

    @Column(name = "taxa_extra")
    private BigDecimal taxaExtra;

    @Length(max = 2, message = "Campo {codEspecieCapitalEspecial} possui o limite de 2 caracteres")
    @Column(name = "cod_especie_capital_especial", length = 2)
    private String codEspecieCapitalEspecial;

    @Length(max = 2, message = "Campo {codEspecieOutrasTaxas} possui o limite de 2 caracteres")
    @Column(name = "cod_especie_outras_taxas", length = 2)
    private String codEspecieOutrasTaxas;

    @Length(max = 2, message = "Campo {codEspecieTaxasExtra} possui o limite de 2 caracteres")
    @Column(name = "cod_especie_taxas_extra", length = 2)
    private String codEspecieTaxasExtra;

    @Length(max = 3, message = "Campo {codEspecieKit} possui o limite de 3 caracteres")
    @Column(name = "cod_especie_kit", length = 3)
    private String codEspecieKit;

    @Length(max = 3, message = "Campo {codPaisInss} possui o limite de 3 caracteres")
    @Column(name = "cod_pais_inss", length = 3)
    private String codPaisInss;

    @Length(max = 3, message = "Campo {codUfInss} possui o limite de 3 caracteres")
    @Column(name = "cod_uf_inss", length = 3)
    private String codUfInss;

    @Length(max = 5, message = "Campo {codImpostoInss} possui o limite de 3 caracteres")
    @Column(name = "cod_imposto_inss", length = 5)
    private String codImpostoInss;

    @Length(max = 5, message = "Campo {codClassificacaoInss} possui o limite de 5 caracteres")
    @Column(name = "cod_classificacao_inss", length = 5)
    private String codClassificacaoInss;

    @Length(max = 3, message = "Campo {codPaisSenar} possui o limite de 3 caracteres")
    @Column(name = "cod_pais_senar", length = 3)
    private String codPaisSenar;

    @Length(max = 3, message = "Campo {codUfSenar} possui o limite de 3 caracteres")
    @Column(name = "cod_uf_senar", length = 3)
    private String codUfSenar;

    @Length(max = 5, message = "Campo {codImpostoSenar} possui o limite de 5 caracteres")
    @Column(name = "cod_imposto_senar", length = 5)
    private String codImpostoSenar;

    @Length(max = 5, message = "Campo {codClassificacaoSenar} possui o limite de 5 caracteres")
    @Column(name = "cod_classificacao_senar", length = 5)
    private String codClassificacaoSenar;

    @Length(max = 5, message = "Campo {codClassificacaoSoSenar} possui o limite de 5 caracteres")
    @Column(name = "cod_classificacao_so_senar", length = 5)
    private String codClassificacaoSoSenar;

    @Length(max = 2, message = "Campo {codEspecieArmazen} possui o limite de 2 caracteres")
    @Column(name = "cod_especie_armazen", length = 2)
    private String codEspecieArmazen;

    @Length(max = 2, message = "Campo {codEspecieRecepcao} possui o limite de 2 caracteres")
    @Column(name = "cod_especie_recepcao", length = 2)
    private String codEspecieRecepcao;

    @Length(max = 10, message = "Campo {sementeContaTratadaEntrada} possui o limite de 10 caracteres")
    @Column(name = "semente_conta_tratada_entrada", length = 10)
    private String sementeContaTratadaEntrada;

    @Length(max = 10, message = "Campo {sementeContaTratadaSaida} possui o limite de 10 caracteres")
    @Column(name = "semente_conta_tratada_saida", length = 10)
    private String sementeContaTratadaSaida;

    @Length(max = 8, message = "Campo {sementeSerieTratadaEntrada} possui o limite de 8 caracteres")
    @Column(name = "semente_serie_tratada_entrada", length = 8)
    private String sementeSerieTratadaEntrada;

    @Length(max = 8, message = "Campo {sementeSerieTratadaSaida} possui o limite de 8 caracteres")
    @Column(name = "semente_serie_tratada_saida", length = 8)
    private String sementeSerieTratadaSaida;

    @Length(max = 8, message = "Campo {sementeDepositoTratadaEntrada} possui o limite de 8 caracteres")
    @Column(name = "semente_deposito_tratada_entrada", length = 8)
    private String sementeDepositoTratadaEntrada;

    @Length(max = 8, message = "Campo {sementeDepositoTratadaSainda} possui o limite de 8 caracteres")
    @Column(name = "semente_deposito_tratada_sainda", length = 8)
    private String sementeDepositoTratadaSainda;

    @Length(max = 8, message = "Campo {sementeCentroCustoTratadaEntrada} possui o limite de 8 caracteres")
    @Column(name = "semente_centro_custo_tratada_entrada", length = 8)
    private String sementeCentroCustoTratadaEntrada;

    @Length(max = 8, message = "Campo {sementeCentroCustoTratadaSaida} possui o limite de 8 caracteres")
    @Column(name = "semente_centro_custo_tratada_saida", length = 8)
    private String sementeCentroCustoTratadaSaida;

    @Length(max = 3, message = "Campo {codPaisGilrat} possui o limite de 3 caracteres")
    @Column(name = "cod_pais_gilrat", length = 3)
    private String codPaisGilrat;

    @Length(max = 3, message = "Campo {codUfGilrat} possui o limite de 3 caracteres")
    @Column(name = "cod_uf_gilrat", length = 3)
    private String codUfGilrat;

    @Length(max = 5, message = "Campo {codImpostoGilrat} possui o limite de 5 caracteres")
    @Column(name = "cod_imposto_gilrat", length = 5)
    private String codImpostoGilrat;

    @Length(max = 5, message = "Campo {codClassificacaoGilrat} possui o limite de 5 caracteres")
    @Column(name = "cod_classificacao_gilrat", length = 5)
    private String codClassificacaoGilrat;

    @Column(name = "qtd_anos_fixacao")
    private Integer qtdAnosFixacao;

    @Length(max = 30, message = "Campo {bayerUsuario} possui o limite de 30 caracteres")
    @Column(name = "bayer_usuario", length = 30)
    private String bayerUsuario;

    @Length(max = 30, message = "Campo {bayerSenha} possui o limite de 30 caracteres")
    @Column(name = "bayer_senha", length = 30)
    private String bayerSenha;

    @Length(max = 3, message = "Campo {codEspecieEpc} possui o limite de 3 caracteres")
    @Column(name = "cod_especie_epc", length = 3)
    private String codEspecieEpc;

    @Length(max = 12, message = "Campo {tipoFluxoCooperado} possui o limite de 12 caracteres")
    @Column(name = "tipo_fluxo_cooperado", length = 12)
    private String tipoFluxoCooperado;
    
    @Column(name = "tipo_fluxo_cooperado_descricao")
    private String tipoFluxoCooperadoDescricao;

    @Length(max = 20, message = "Campo {planoContaContabilCooperado} possui o limite de 20 caracteres")
    @Column(name = "plano_conta_contabil_cooperado", length = 20)
    private String planoContaContabilCooperado;
    
    @Column(name = "plano_conta_contabil_cooperado_descricao")
    private String planoContaContabilCooperadoDescricao;

    @Length(max = 20, message = "Campo {contaContabilCooperado} possui o limite de 20 caracteres")
    @Column(name = "conta_contabil_cooperado", length = 20)
    private String contaContabilCooperado;
    
    @Column(name = "conta_contabil_cooperado_descricao")
    private String contaContabilCooperadoDescricao;

    @Length(max = 12, message = "Campo {tipoFluxoTerceiro} possui o limite de 12 caracteres")
    @Column(name = "tipo_fluxo_terceiro", length = 12)
    private String tipoFluxoTerceiro;
    
    @Column(name = "tipo_fluxo_terceiro_descricao")
    private String tipoFluxoTerceiroDescricao;

    @Length(max = 20, message = "Campo {planoContaContabilTerceiro} possui o limite de 20 caracteres")
    @Column(name = "plano_conta_contabil_terceiro", length = 20)
    private String planoContaContabilTerceiro;
    
    @Column(name = "plano_conta_contabil_terceiro_descricao")
    private String planoContaContabilTerceiroDescricao;

    @Length(max = 20, message = "Campo {contaContabilTerceiro} possui o limite de 20 caracteres")
    @Column(name = "conta_contabil_terceiro", length = 20)
    private String contaContabilTerceiro;
    
    @Column(name = "conta_contabil_terceiro_descricao")
    private String contaContabilTerceiroDescricao;

    @Length(max = 20, message = "Campo {contaContabilTransitoria} possui o limite de 20 caracteres")
    @Column(name = "conta_contabil_transitoria", length = 20)
    private String contaContabilTransitoria;
    
    @Column(name = "conta_contabil_transitoria_descricao")
    private String contaContabilTransitoriaDescricao;

    @Column(name = "qtd_max_bxa_massa")
    private Integer qtdMaxBxaMassa;

    @Length(max = 2, message = "Campo {codEspecieSenarSemente} possui o limite de 2 caracteres")
    @Column(name = "cod_especie_senar_semente", length = 2)
    private String codEspecieSenarSemente;

    @Length(max = 2, message = "Campo {codEspecieInssSemente} possui o limite de 2 caracteres")
    @Column(name = "cod_especie_inss_semente", length = 2)
    private String codEspecieInssSemente;

    @Column(name = "taxa_gilrat_pj")
    private BigDecimal taxaGilratPj;

    @Column(name = "taxa_gilrat_pf")
    private BigDecimal taxaGilratPf;

    @Length(max = 2, message = "Campo {codEspecieGilrat} possui o limite de 2 caracteres")
    @Column(name = "cod_especie_gilrat", length = 2)
    private String codEspecieGilrat;

    @Length(max = 2, message = "Campo {codEspecieInss} possui o limite de 2 caracteres")
    @Column(name = "cod_especie_inss")
    private String codEspecieInss;
}
