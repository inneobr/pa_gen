package br.coop.integrada.api.pa.domain.modelDto.nfRemessa;

import java.util.List;

import br.coop.integrada.api.pa.domain.model.nfRemessa.NfRemessa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NfRemessaIntegrationDto {
	
	private static final long serialVersionUID = 1L;
	private List<NfRemessa> nfRemessa;
	
}
