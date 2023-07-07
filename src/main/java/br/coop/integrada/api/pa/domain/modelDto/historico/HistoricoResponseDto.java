package br.coop.integrada.api.pa.domain.modelDto.historico;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.Historico;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Data
public class HistoricoResponseDto extends AbstractResponseDto {

    private Integer nroDocPesagem;
    private String codEstabelecimento;
    private Integer safra;

    public static HistoricoResponseDto construir(Historico historico, Boolean integrated, String message) {
        var objDto = new HistoricoResponseDto();
        BeanUtils.copyProperties(historico, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

    public static HistoricoResponseDto construir(HistoricoDto historicoDto, Boolean integrated, String message, String exception) {
        var objDto = new HistoricoResponseDto();
        BeanUtils.copyProperties(historicoDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception);

        return objDto;
    }

    public static HistoricoResponseDto construir(HistoricoDto historicoDto, Boolean integrated, String message, ConstraintViolationException exception) {
        var objDto = new HistoricoResponseDto();
        BeanUtils.copyProperties(historicoDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        if(exception != null) {
            objDto.setException(exception.getMessage());

            for (ConstraintViolation<?> constraint : exception.getConstraintViolations()) {
                String campo = ((PathImpl) constraint.getPropertyPath()).getLeafNode().getName();
                String mensagem = constraint.getMessage();
                FieldErrorItem fieldErrorItem = FieldErrorItem.construir(campo, mensagem);
                objDto.getFieldErrors().add(fieldErrorItem);
            }
        }

        return objDto;
    }
}
