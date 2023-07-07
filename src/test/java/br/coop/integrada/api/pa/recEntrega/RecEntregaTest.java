package br.coop.integrada.api.pa.recEntrega;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.model.produto.TipoGmo;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntregaItem;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteItem;
import br.coop.integrada.api.pa.domain.service.recEntrega.RecEntregaService;

//@SpringBootTest
public class RecEntregaTest {

	@Autowired
	RecEntregaService recEntregaService;	

	//@Test
	public void gerarReCobrancaTaxa() throws CloneNotSupportedException{
		List<RecEntrega> recEntregaList = new ArrayList<>();
		
		Estabelecimento estabelecimento = new Estabelecimento();
		estabelecimento.setId(1L);
		estabelecimento.setCodigoRegional("001");
		
		GrupoProduto fmCodigo = new GrupoProduto();
		fmCodigo.setId(1L);
		
		TipoGmo tipoGmo = new TipoGmo();
		tipoGmo.setId(1L);
		
		Produto produto = new Produto();
		produto.setId(1L);
		
		RecEntrega recEntrega = new RecEntrega();
		recEntrega.setCodEstabel("001");
		recEntrega.setNrRe(04042023L);
		recEntrega.setLogTemSaldo(false);
		recEntrega.setDtEmissao(new Date());
		recEntrega.setLogNfe(false);
		recEntrega.setNatEntArmaz(null);
		recEntrega.setSerie(null);
		recEntrega.setNrNotaFis(null);
		recEntrega.setNfPj(false);
		recEntrega.setNrReOpcao(null);
		recEntrega.setCodUnidParc(null);
		recEntrega.setCodEstOffLine("001");		
		recEntrega.setPlaca("ABC1026");
		recEntrega.setFmCodigo("20101");
		recEntrega.setSafra(2023);
		recEntrega.setCodEmitente(null);
		recEntrega.setNomeProd(null);
		recEntrega.setDtEntrada(new Date());
		recEntrega.setHrEntrada("17:17:35");
		recEntrega.setMatricula(33L);
		recEntrega.setNrDocPes(04042023);
		recEntrega.setMotorista(null);
		recEntrega.setTulha(null);
		recEntrega.setImpresso(false);
		recEntrega.setNrOrdCampo(04042023);
		recEntrega.setNrLaudo(04042023L);
		recEntrega.setNrContSem(null);
		recEntrega.setClasse(null);
		recEntrega.setObservacoes(null);
		recEntrega.setPesoBruto(new BigDecimal("19350"));
		recEntrega.setTaraVeiculo(new BigDecimal("4000"));
		recEntrega.setTaraVeiculo(BigDecimal.ZERO);
		recEntrega.setPesoLiquido(new BigDecimal("15350"));
		recEntrega.setTipoRr(null);
		recEntrega.setTipoGmo("");
		recEntrega.setCdnRepres(null);
		recEntrega.setProdPadr(null);
		recEntrega.setDescarUnid(false);
		recEntrega.setSafraCompos("2023/2023");
		recEntrega.setNomeSafraCompos("2022-2023");
		recEntrega.setNrCadPro(null);
		recEntrega.setNrNfProd(null);
		recEntrega.setDtNfProd(null);
		recEntrega.setNrAutTransf(null);
		recEntrega.setUbsLogLiberado(true);
		recEntrega.setUbsLogRequisitado(false);
		recEntrega.setUbsDataRequisicao(null);
		recEntrega.setUbsHoraRequisicao(null);
		recEntrega.setNrBcqs(null);
		recEntrega.setCodRegional("001");
		recEntrega.setNatDocum(null);
		recEntrega.setSerieDocum(null);
		recEntrega.setNroDocum(null);
		recEntrega.setReDevolvido(false);
		recEntrega.setNrOrdProd(04042023L);
		recEntrega.setEntradaManual(false);
		recEntrega.setHrSaida("17:37:22");
		recEntrega.setCodSit(null);
		recEntrega.setNrReOrig(04042023L);
		recEntrega.setMotivoBloqueio(null);
		recEntrega.setBeneficiado(false);
		recEntrega.setTipoRendaCafe(null);
		recEntrega.setNotifPortal(false);
		recEntrega.setEntradaRr(null);
		recEntrega.setKitCobrado(false);
		recEntrega.setComCobranca(null);
		recEntrega.setLogTxSpRetida(false);
		recEntrega.setReBloqueado(false);
		recEntrega.setReDisponivel(true);
		recEntrega.setNrPriEnt(null);
		recEntrega.setNrReDpi(null);
		recEntrega.setLogBloqDpi(false);
		recEntrega.setNrReDpi(null);
		recEntrega.setPesoLiqSemDescDpi(new BigDecimal("19350"));
		recEntrega.setNrReParcialDpi(null);
		recEntrega.setQtdDpi(null);
		recEntrega.setPerDpi(null);
		recEntrega.setNrReOrig(null);
		recEntrega.setNrRePai(null);
		recEntrega.setPjNroNota(null);
		recEntrega.setPjSerie(null);
		recEntrega.setPjDtEmissao(null);
		recEntrega.setPjVlTotNota(null);
		recEntrega.setPjQtEntrada(null);
		recEntrega.setPjChaveAcesso(null);
		recEntrega.setPjLogNotaPropria(false);
		recEntrega.setPjNatOper(null);
		recEntrega.setNatureza(null);		
		recEntrega.setDataCadastro(new Date());	
		recEntrega.setItens(new ArrayList<>());	
			

		RePendenteItem item = new RePendenteItem();
		item.setPesoLiquido(new BigDecimal("15350"));
		item.setPercentualDescontoImpureza(new BigDecimal("2"));
		item.setPercentualDescontoUmidade(new BigDecimal("2"));
		item.setPercentualDescontoChuvadoAvariado(new BigDecimal("1"));
		item.setQtdTbm(new BigDecimal("2"));
		item.setTabelaUmidadeTaxaSecagemKg(new BigDecimal("2"));
		
		RecEntregaItem recEntregaItem = new RecEntregaItem();
		recEntregaItem.setRecEntrega(recEntrega);
		recEntregaItem.setCodEstabel(estabelecimento.getCodigo());
		recEntregaItem.setNrRe(04042023L);
		recEntregaItem.setCodProduto(produto.getCodItem());
		recEntregaItem.setItCodigo("04042023");
		recEntregaItem.setCodRefer("REF-001");
		recEntregaItem.setPesoLiquido(new BigDecimal("19530"));
		recEntregaItem.setPhEntrada(new BigInteger("7"));
		recEntregaItem.setPhCorrigido(new BigInteger("7.5"));
		recEntregaItem.setImpureza(new BigDecimal("8"));
		recEntregaItem.setPerDescImpur(new BigDecimal("8"));
		recEntregaItem.setQtDescImpur(new BigDecimal("8"));
		recEntregaItem.setQtDescImpur(new BigDecimal("8"));
		recEntregaItem.setUmidade(new BigDecimal("23"));
		recEntregaItem.setPerDescUmid(new BigDecimal("23"));
		recEntregaItem.setChuvAvar(new BigDecimal("12"));
		recEntregaItem.setPerDescChuv(new BigDecimal("12"));
		recEntregaItem.setQtDescChuv(new BigDecimal("12"));
		recEntregaItem.setTbm(new BigDecimal("8"));
		recEntregaItem.setQtTbm(new BigDecimal("8"));
		recEntregaItem.setTipo(null);
		recEntregaItem.setBebida(null);
		recEntregaItem.setCafeEscolha(BigDecimal.ZERO);
		recEntregaItem.setRendaLiquida(new BigDecimal("15230"));
		recEntregaItem.setDefeitos(null);
		recEntregaItem.setNormal(true);
		recEntregaItem.setSementeira(false);
		recEntregaItem.setTerra(false);
		recEntregaItem.setVagem(false);
		recEntregaItem.setLote("040423");
		recEntregaItem.setRendaLiquidaAtu(new BigDecimal("15250"));
		recEntregaItem.setPesoLiquidoAtu(new BigDecimal("15250"));
		recEntregaItem.setVlFiscal(null);
		recEntregaItem.setTabTxSecKg(new BigDecimal("2"));
		recEntregaItem.setTabTxSecVl(new BigDecimal("2"));
		recEntregaItem.setCaferrTipo(null);
		recEntregaItem.setCafermBebida(null);
		recEntregaItem.setCafermCafeEscolha(BigDecimal.ZERO);
		recEntregaItem.setCafermRendaLiquida(BigDecimal.ZERO);
		recEntregaItem.setCaferrDefeitos(null);
		recEntregaItem.setCafermRendaLiquida(null);
		recEntregaItem.setCafermBebida(null);
		recEntregaItem.setCafermCafeEscolha(null);
		recEntregaItem.setCafermRendaLiquida(null);
		recEntregaItem.setCafermDefeitos(null);
		recEntregaItem.setCaferrDefeitos(null);
		recEntregaItem.setCafermCodRefer(null);
		recEntregaItem.setTabVlRecepcao(new BigDecimal("1500"));
		recEntregaItem.setVlRecepcao(new BigDecimal("1500"));
		recEntregaItem.setQtRecepcao(new BigDecimal("1500"));
		recEntregaItem.setVlSecagem(new BigDecimal("15"));
		recEntregaItem.setQtSecagem(new BigDecimal("1500"));
		recEntregaItem.setQtSecagem(new BigDecimal("2"));
		recEntregaItem.setVlPonto(new BigDecimal("2"));
		recEntregaList.add(recEntrega);	
		
		recEntrega.getItens().add(recEntregaItem);
		
		recEntregaService.gerarReCobrancaTaxa(recEntregaList, item);
	}
}
