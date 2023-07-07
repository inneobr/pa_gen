package br.coop.integrada.api.pa.domain.modelDto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResultsDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String message;
	private Boolean sucess;

}
