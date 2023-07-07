package br.coop.integrada.api.pa.domain.modelDto.rependente.validation;

import java.util.List;

import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import lombok.Data;

@Data
public class EntradaReValidation {
	
	private List<NotaFiscalPjValidation> notasFiscaisPj;
	private List<DocumentoPesagemValidation> documentosDePesagem;
	private List<ProdutoPadraoValidation> produtos;
	private List<LoteValidation> lotes;
		
	public boolean isSucessFull(boolean validarNotas, boolean validarPesagens, boolean validarProdutos, boolean validarLotes) {
		boolean sucesso = false;
		
		if(validarNotas) {
			
			if(CollectionUtils.isEmpty(notasFiscaisPj)) {
				throw new ObjectDefaultException("Erro: A API do ERP não retornou a validação da nota fiscal de PJ.");
			}else {
				for(NotaFiscalPjValidation nf : notasFiscaisPj) {
					if(nf.getExisteErp()) {
						throw new ObjectDefaultException(nf.getMensagem());
					}
				}
				sucesso = true;
			}
			
		}
		
		if(validarPesagens) {
			
			if(CollectionUtils.isEmpty(documentosDePesagem)) {
				throw new ObjectDefaultException("Erro: A API do ERP não retornou a validação do documento de pesagem.");
			}else {
				for(DocumentoPesagemValidation dp : documentosDePesagem) {
					if(!dp.getLiberado()) {
						throw new ObjectDefaultException(dp.getMensagem());
					}
				}
				sucesso = true;
			}
			
		}
		
		if(validarProdutos) {

			if(CollectionUtils.isEmpty(produtos)) {
				throw new ObjectDefaultException("Erro: A API do ERP não retornou a validação de produto padronizado.");
			}else {
				for(ProdutoPadraoValidation p : produtos) {
					if(!p.getPadronizado()) {
						throw new ObjectDefaultException(p.getMensagem());
					}
				}
				sucesso = true;
			}
			
		}
		
		if(validarLotes) {
			
			if(CollectionUtils.isEmpty(lotes)) {
				throw new ObjectDefaultException("Erro: A API do ERP não retornou a validação do lote.");
			}else {
				for(LoteValidation l : lotes) {
					if(!l.getLiberado()) {
						throw new ObjectDefaultException(l.getMensagem());
					}
				}
				sucesso = true;
			}
			
		}
		
		return sucesso;
	}

}
