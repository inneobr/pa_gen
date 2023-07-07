package br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

@Data
public class ParametrosUsuarioEstabelecimentoResponseDto extends AbstractResponseDto {
    private String codigoEstabelecimento;
    private String codigoUsuario;
	private String idUnico;
	private IntegrationOperacaoEnum operacao;

    public static ParametrosUsuarioEstabelecimentoResponseDto construir(String codigoEstabelecimento, String codigoUsuario, Boolean integrated, String message, IntegrationOperacaoEnum operacao) {
        var objDto = new ParametrosUsuarioEstabelecimentoResponseDto();
        objDto.setCodigoUsuario(codigoUsuario);
        objDto.setCodigoEstabelecimento(codigoEstabelecimento);
        objDto.setIntegrated(integrated);
        objDto.setOperacao(operacao);
        objDto.setMessage(message);

        return objDto;
    }

    public static ParametrosUsuarioEstabelecimentoResponseDto construir(String codigoEstabelecimento, String codigoUsuario, Boolean integrated, String message, Exception exception, IntegrationOperacaoEnum operacao) {
        var objDto = new ParametrosUsuarioEstabelecimentoResponseDto();
        objDto.setCodigoUsuario(codigoUsuario);
        objDto.setCodigoEstabelecimento(codigoEstabelecimento);
        objDto.setIntegrated(integrated);
        objDto.setOperacao(operacao);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }

    public static ParametrosUsuarioEstabelecimentoResponseDto construir(String codigoEstabelecimento, String codigoUsuario, Boolean integrated, String message, ConstraintViolationException exception, IntegrationOperacaoEnum operacao) {
        var objDto = new ParametrosUsuarioEstabelecimentoResponseDto();
        objDto.setCodigoUsuario(codigoUsuario);
        objDto.setCodigoEstabelecimento(codigoEstabelecimento);
        objDto.setIntegrated(integrated);
        objDto.setOperacao(operacao);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        if(exception instanceof ConstraintViolationException) {
            for (ConstraintViolation<?> constraint : exception.getConstraintViolations()) {
                FieldErrorItem fieldErrorItem = FieldErrorItem.construir(
                        ((PathImpl) constraint.getPropertyPath()).getLeafNode().getName(),
                        constraint.getMessage()
                );

                objDto.getFieldErrors().add(fieldErrorItem);
            }
        }

        return objDto;
    }
}
