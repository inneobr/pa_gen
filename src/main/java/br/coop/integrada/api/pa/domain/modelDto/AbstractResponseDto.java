package br.coop.integrada.api.pa.domain.modelDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AbstractResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean integrated;
    private String message;
    private String exception;
    private List<FieldErrorItem> fieldErrors = new ArrayList<>();
}
