package com.apimybarber.domain.controllers;


import com.apimybarber.domain.enums.TipoPeriodo;
import com.apimybarber.domain.services.AgendaService;
import com.apimybarber.domain.utils.LocalDateUtils;
import com.apimybarber.domain.viewobject.AgendaAgrupadaVO;
import com.apimybarber.domain.viewobject.GraficoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/grafico")
@CrossOrigin
public class GraficoController {

    private Logger logger = LoggerFactory.getLogger(GraficoController.class);

    @Autowired
    private AgendaService agendaService;

    @GetMapping(value = "/grafico-por-periodo")
    public ResponseEntity<List<GraficoVO>> graficoPorPeriodo(@RequestParam String userId,
                                                             @RequestParam String dataInicial,
                                                             @RequestParam String dataFinal,
                                                             @RequestParam TipoPeriodo tipoPeriodo) {
        try {
            LocalDate localDateInicial = LocalDateUtils.getLocalDateIso(dataInicial);
            LocalDate localDateFinal = LocalDateUtils.getLocalDateIso(dataFinal);
            List<AgendaAgrupadaVO> agendas = agendaService.findAllAgrupadaByUserIdAndPeriodo(userId, localDateInicial, localDateFinal, tipoPeriodo);
            return ResponseEntity.ok(montarGrafico(agendas, tipoPeriodo, localDateInicial));
        } catch (Exception e) {
            logger.error("Erro: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public List<GraficoVO> montarGrafico(List<AgendaAgrupadaVO> agendas, TipoPeriodo tipoPeriodo, LocalDate localDateInicial) {
        List<GraficoVO> retorno = new ArrayList<>();
        if (TipoPeriodo.SEMANA.equals(tipoPeriodo)) {
            List<String> semanas = montarDiasSemana(localDateInicial);
            Map<String, BigDecimal> totaisPorDiaDaSemana = montarGraficoSemanal(agendas, semanas);
            for (String descricao : semanas) {
                BigDecimal total = totaisPorDiaDaSemana.getOrDefault(descricao, BigDecimal.ZERO);
                retorno.add(new GraficoVO(total.doubleValue(), descricao));
            }

        } else if (TipoPeriodo.SEMESTRE1.equals(tipoPeriodo)) {
            List<String> meses = new ArrayList<>(Arrays.asList("Jan", "Fev", "Mar", "Abr", "Mai", "Jun"));
            montarGraficoSemestral(agendas, retorno, meses);

        } else {
            List<String> meses = new ArrayList<>(Arrays.asList("Jul", "Ago", "Set", "Out", "Nov", "Dez"));
            montarGraficoSemestral(agendas, retorno, meses);
        }

        return retorno;
    }

    private Map<String, BigDecimal> montarGraficoSemanal(List<AgendaAgrupadaVO> agendas, List<String> semanas) {
        Map<String, BigDecimal> totaisPorDiaDaSemana = new HashMap<>();
        for (AgendaAgrupadaVO agenda : agendas) {
            DayOfWeek semana = agenda.getData().getDayOfWeek();
            String semanaString = semanas.get(semana.getValue() - 1);
            if (totaisPorDiaDaSemana.containsKey(semanaString)) {
                totaisPorDiaDaSemana.get(semanaString).add(agenda.getTotal());
            } else {
                totaisPorDiaDaSemana.put(semanaString, agenda.getTotal());
            }
        }
        return totaisPorDiaDaSemana;
    }

    private void montarGraficoSemestral(List<AgendaAgrupadaVO> agendas, List<GraficoVO> retorno, List<String> meses) {
        Map<String, BigDecimal> totaisPorSemestre = new HashMap<>();
        for (AgendaAgrupadaVO agenda : agendas) {
            Month mes = agenda.getData().getMonth();
            String mesString = meses.get(mes.getValue() - 1);
            if (totaisPorSemestre.containsKey(mesString)) {
                totaisPorSemestre.get(mesString).add(agenda.getTotal());
            } else {
                totaisPorSemestre.put(mesString, agenda.getTotal());
            }
        }
        for (String descricao : meses) {
            BigDecimal total = totaisPorSemestre.getOrDefault(descricao, BigDecimal.ZERO);
            retorno.add(new GraficoVO(total.doubleValue(), descricao));
        }
    }

    private List<String> montarDiasSemana(LocalDate localDateInicial) {
        List<String> semanas = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE");
        for (int i = 0; i < 7; i++) {
            semanas.add(localDateInicial.format(formatter));
            localDateInicial = localDateInicial.plusDays(1);
        }
        return semanas;
    }

}
