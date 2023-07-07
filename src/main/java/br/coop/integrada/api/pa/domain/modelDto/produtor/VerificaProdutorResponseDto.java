package br.coop.integrada.api.pa.domain.modelDto.produtor;

import br.coop.integrada.api.pa.domain.model.Produtor;
import lombok.Data;

@Data
public class VerificaProdutorResponseDto {
	
	private String mensagem;
	private String natureza;
	private String tipoProdutor;
	private Boolean produtorDap;
	private Boolean cooperativa;
	private Boolean participanteBayer;
	private Boolean ativo;
    private Boolean bloqueado;
    private String codRegional;
    private Boolean emiteNotaPropria;
    private String codRepres;

	public VerificaProdutorResponseDto(String mensagem) {
		this.mensagem = mensagem;
	}

	public VerificaProdutorResponseDto(Produtor produtor, String mensagem) {
		this.mensagem = mensagem;
		
		this.natureza = produtor.getNatureza();
		this.tipoProdutor = produtor.getTipoProdutor();
		this.produtorDap = produtor.getProdutorDap();
		this.cooperativa = produtor.getCooperativa();
		this.participanteBayer = produtor.getParticipanteBayer();
		this.ativo = produtor.getAtivo();
		this.bloqueado = produtor.getBloqueado();
		this.codRegional = produtor.getCodRegional();
		
		if(produtor.getNatureza().equals("PJ"))
			this.emiteNotaPropria = produtor.getEmiteNota();
		else
			this.emiteNotaPropria = false;
		
		this.codRepres = produtor.getCodRepres();
	}
}
