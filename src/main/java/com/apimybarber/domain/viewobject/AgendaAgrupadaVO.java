package com.apimybarber.domain.viewobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class AgendaAgrupadaVO {

    private LocalDate data;
    private BigDecimal total;
}
