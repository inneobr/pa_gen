package br.coop.integrada.api.pa.domain.modelDto.rependente;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import br.coop.integrada.api.pa.domain.model.produto.TipoGmo;
import br.coop.integrada.api.pa.domain.model.rependente.RePendente;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoDto;
import br.coop.integrada.api.pa.domain.modelDto.grupoProduto.GrupoProdutoDto;
import br.coop.integrada.api.pa.domain.modelDto.imovel.ImovelProdutorSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.imovel.ImovelSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.produtor.ProdutorSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.desmembramento.RePendenteDesmembramentoCompletoDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.desmembramento.RePendenteDesmembramentoDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.item.RePendenteItemCompletoDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.item.RePendenteItemDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.SementeClasseDto;
import lombok.Data;

@Data
public class RePendenteCompletoDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private EstabelecimentoDto estabelecimento;
	private String placa;
	private GrupoProdutoDto grupoProduto;
	private ProdutorSimplesDto produtor;
	private String nomeProd;
	private ImovelProdutorSimplesDto imovel;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	private Date dtEntrada;
	private String hrEntrada;
	private Long matricula;
	private Long nrNfProd;
	private Integer nrDocPes;
	private BigDecimal pesoBruto;
	private BigDecimal taraVeiculo;
	private BigDecimal taraSacaria;
	private BigDecimal pesoLiquido;
	private String motorista;
	private String tulha;
	private Integer nrOrdCampo;
	private Long nrLaudo;
	private String observacoes;
	private Integer tpRe;
	private Integer nrReOrig;
	private Integer nrRePai;
	private SementeClasseDto classe;
	private Integer codSit;
	private BigDecimal nrAutTransf;
	private String tipoRr;
	private BigDecimal descCredito;
	private BigDecimal qtDepi;
	private BigDecimal perDepi;
	private Boolean kitCobrado;
	private String entradaRr;
	private Boolean comCobranca;
	private Integer nrReOrigDfx;
	private Boolean reDisponivel;
	private String tipoRendaCfe;
	private Date dtEmissaoOri;
	private Boolean entradaManual;
	private String nrContSem;
	private String safraCompos;
	private String nomeSafraCompos;
	private TipoGmo tipoGmo;
	private Integer nrPriEnt;
	private Integer cdnRepres;
	private Boolean logReDpi;
	private Boolean logBloqDpi;
	private Integer nrReDpi;
	private BigDecimal pesoLiqSemDescDpi;
	private String serNfProd;
	private Date dtNfProd;
	private Boolean prodPadr;
	private Boolean descarUnid;
	private Boolean logTxSpRetida;
	private Integer nrReTxSpRetida;
	private String pjNroNota;
	private String pjSerie;
	private Date pjDtEmissao;
	private BigDecimal pjVlTotNota;
	private BigDecimal pjQtTotNota;
	private String pjChaveAcesso;
	private Boolean pjLogNotaPropria;
	private String pjNatOper;
	
	private List<RePendenteItemCompletoDto> itens;
	private List<RePendenteDesmembramentoCompletoDto> desmembramentos;
	
	public static RePendenteCompletoDto construir(RePendente obj) {
		var objDto = new RePendenteCompletoDto();
		BeanUtils.copyProperties(obj, objDto);
		
		if(obj.getEstabelecimento() != null) {
			EstabelecimentoDto estabelecimentoDto = EstabelecimentoDto.construir(obj.getEstabelecimento());
			objDto.setEstabelecimento(estabelecimentoDto);
		}
		
		if(obj.getGrupoProduto() != null) {
			GrupoProdutoDto grupoProdutoDto = GrupoProdutoDto.construir(obj.getGrupoProduto(), null);
			objDto.setGrupoProduto(grupoProdutoDto);
		}
		
		if(obj.getProdutor() != null) {
			ProdutorSimplesDto produtorDto = ProdutorSimplesDto.construir(obj.getProdutor());
			objDto.setProdutor(produtorDto);
		}
		
		if(obj.getImovel() != null) {
			ImovelProdutorSimplesDto imovelDto = ImovelProdutorSimplesDto.construir(obj.getImovel());
			objDto.setImovel(imovelDto);
		}
		
		if(obj.getClasse() != null) {
			SementeClasseDto classeDto = SementeClasseDto.construir(obj.getClasse());
			objDto.setClasse(classeDto);
		}
		
		if(obj.getDesmembramentos() != null) {
			List<RePendenteDesmembramentoCompletoDto> desmembramentos = RePendenteDesmembramentoCompletoDto.construir(obj.getDesmembramentos());
			objDto.setDesmembramentos(desmembramentos);
		}
		
		List<RePendenteItemCompletoDto> itens = RePendenteItemCompletoDto.construir(obj.getItens());
		objDto.setItens(itens);
		
		return objDto;
	}
}
