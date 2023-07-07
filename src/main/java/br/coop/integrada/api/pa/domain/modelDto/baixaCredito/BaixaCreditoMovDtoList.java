package br.coop.integrada.api.pa.domain.modelDto.baixaCredito;

import java.util.List;

import lombok.Data;

@Data
public class BaixaCreditoMovDtoList {
    private List<BaixaCreditoMovRequestDto> baixaCreditoMov;
}
