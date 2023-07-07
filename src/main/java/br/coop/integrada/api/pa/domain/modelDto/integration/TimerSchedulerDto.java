package br.coop.integrada.api.pa.domain.modelDto.integration;

import java.io.Serializable;

import org.apache.logging.log4j.util.Strings;

import lombok.Data;

@Data
public class TimerSchedulerDto implements Serializable{
    private static final long serialVersionUID = 1L;
      
    private String second;
    private String minute;
    private String hour;
    private String dayMonth;
    private String month;
    private String dayWeek;
    
    public String getExpressions() {
        StringBuilder expressions = new StringBuilder();
        if(!Strings.isEmpty(second)) {
            expressions.append(second.trim()).append(" ");
        }else {
            expressions.append("0 ");
        }
        if(!Strings.isEmpty(minute)) {
            expressions.append(minute.trim()).append(" ");
        }else {
            expressions.append("*/1 ");
        }
        if(!Strings.isEmpty(hour)) {
            expressions.append(hour.trim()).append(" ");
        }else {
            expressions.append("* ");
        }
        if(!Strings.isEmpty(dayMonth)) {
            expressions.append(dayMonth.trim()).append(" ");
        }else {
            expressions.append("* ");
        }
        if(!Strings.isEmpty(month)) {
            expressions.append(month.trim()).append(" ");
        }else {
            expressions.append("* ");
        }
        if(!Strings.isEmpty(dayWeek)) {
            expressions.append(dayWeek.trim());
        }else {
            expressions.append("*");
        }
        return expressions.toString();
    }
    
    public static TimerSchedulerDto construir(String expressions) {
    	TimerSchedulerDto dto = new TimerSchedulerDto();
    	
    	if(!Strings.isEmpty(expressions)) {
    		String[] array = expressions.trim().split("\\s+");
    		dto.setSecond(array.length > 0 ? array[0] : null);
    		dto.setMinute(array.length > 1 ? array[1] : null);
    		dto.setHour(array.length > 2 ? array[2] : null);
    		dto.setDayMonth(array.length > 3 ? array[3] : null);
    		dto.setMonth(array.length > 4 ? array[4] : null);
    		dto.setDayWeek(array.length > 5 ? array[5] : null);
    	}
    	
    	return dto;
    	
    }

}
