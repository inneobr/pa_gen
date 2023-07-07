package br.coop.integrada.api.pa.domain.modelDto.semente.produtor;

import java.io.Serializable;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

@Data
public class GrupoSementeProdutorResponseDto extends AbstractResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String idUnico;
    private Integer safra;
    private Integer ordemCampo;
    private String codigoEstabelecimento;
    private String codigoFamilia;
    private Long classeCodigo;

    public static GrupoSementeProdutorResponseDto construir(GrupoSementeProdutorDto grupoSementeProdutorDto, Boolean integrated, String message) {
        var sementeCampoResponseDto = new GrupoSementeProdutorResponseDto();
        BeanUtils.copyProperties(grupoSementeProdutorDto, sementeCampoResponseDto);

        sementeCampoResponseDto.setIntegrated(integrated);
        sementeCampoResponseDto.setMessage(message);

        return sementeCampoResponseDto;
    }

    public static GrupoSementeProdutorResponseDto construir(GrupoSementeProdutorDto grupoSementeProdutorDto, Boolean integrated, String message, Exception exception) {
        var sementeCampoResponseDto = new GrupoSementeProdutorResponseDto();
        BeanUtils.copyProperties(grupoSementeProdutorDto, sementeCampoResponseDto);

        sementeCampoResponseDto.setIntegrated(integrated);
        sementeCampoResponseDto.setMessage(message);
        sementeCampoResponseDto.setException(exception.getMessage());

        return sementeCampoResponseDto;
    }

    public static GrupoSementeProdutorResponseDto construir(GrupoSementeProdutorDto grupoSementeProdutorDto, Boolean integrated, String message, ConstraintViolationException exception) {
        var objDto = new GrupoSementeProdutorResponseDto();
        BeanUtils.copyProperties(grupoSementeProdutorDto, objDto);

        objDto.setIntegrated(integrated);
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
