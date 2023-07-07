package br.coop.integrada.api.pa.domain.modelDto.usuario;

import lombok.Data;

@Data
public class UsuarioDto {
	private String matricula;
    private String codUsuario;
	private String username;
	private String nome;
	private String regional;
	private String estabelecimento;
}
