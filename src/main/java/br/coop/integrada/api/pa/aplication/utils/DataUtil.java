package br.coop.integrada.api.pa.aplication.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import br.coop.integrada.api.pa.domain.model.movimentoDiario.MovimentoDiarioResponse;

public class DataUtil {
	
	public static Date ultimoDiaMesAtual() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return setarHorarioNaData(c.getTime(), 23, 59, 59);
    }
    
    public static Date ultimoDiaDoMes(Date data) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return setarHorarioNaData(c.getTime(), 23, 59, 59);
    }

    public static Date primeiroDiaMesAtual() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.DAY_OF_MONTH, 1);
        return setarHorarioNaData(c.getTime(), 0, 0, 0);
    }

    public static Date primeiroDiaAnoAtual() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.DAY_OF_YEAR, 1);
        return setarHorarioNaData(c.getTime(), 0, 0, 0);
    }

    public static Date primeiroDiaDoMes(Date data) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return setarHorarioNaData(c.getTime(), 0, 0, 0);
    }

    /**
     * Adiciona ou remove anos
     *
     * @param data
     * @param anos
     * @return Date
     */
    public static Date adicionarOuRemoverAnos(Date data, int anos) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(Calendar.YEAR, anos);
        return c.getTime();
    }

    /**
     *
     * @param data
     * @param mes
     * @return Date
     */
    public static Date adicionarOuRemoverMes(Date data, int mes) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(Calendar.MONTH, mes);
        return c.getTime();
    }

    /**
     * Adiciona ou remove dias em uma data
     *
     * @param data
     * @param dias
     */
    public static Date adicionarOuRemoverDias(Date data, int dias) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(Calendar.DATE, dias);
        return c.getTime();
    }

    /**
     * Adiciona ou remove horas na data.
     *
     * @param data
     * @param horas
     * @return
     */
    public static Date adicionarOuRemoverHorarioNaData(Date data, int horas) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(Calendar.HOUR_OF_DAY, horas);
        return c.getTime();
    }

    /**
     * Seta as horas e os minutos na data
     *
     * @param data
     * @param horas
     * @param minutos
     * @return
     */
    public static Date setarHorarioNaData(Date data, int horas, int minutos, int segundos) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.set(Calendar.HOUR_OF_DAY, horas);
        c.set(Calendar.MINUTE, minutos);
        c.set(Calendar.SECOND, segundos);
        return c.getTime();
    }

    public static Date setarDiaNaData(Date data, int dia) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.set(Calendar.DAY_OF_MONTH, dia);
        return c.getTime();
    }

    /**
     * Converte a data por externso
     *
     * @param data
     * @return
     */
    public static String retornarDataPorExtenso(Date data) {
        if (data != null) {
            DateFormat dateFormat = new SimpleDateFormat("EEEE, d' de 'MMMM' de 'yyyy", new Locale("pt", "BR"));
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(data);
            return dateFormat.format(calendar.getTime());
        }
        return "";
    }

    public static String formatarDataDDMMYYYY(Date data) {
        if (data != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(data);
            return dateFormat.format(calendar.getTime());
        }
        return "";
    }
    
    public static String formatarDataMMYY(Date data) {
        if (data != null) {
            DateFormat dateFormat = new SimpleDateFormat("MM/yy", new Locale("pt", "BR"));
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(data);
            return dateFormat.format(calendar.getTime());
        }
        return "";
    }

    public static String formatarDataDDMMYYYYHHMM(Date data) {
        if (data != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("pt", "BR"));
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(data);
            return dateFormat.format(calendar.getTime());
        }
        return "";
    }

    public static String nomeArquivoFormatoDDMMYYYYHHMMSS(String nome) {
        if (nome != null) {
            DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss", new Locale("pt", "BR"));
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(new Date());
            return dateFormat.format(calendar.getTime()) +"_"+ nome;
        }
        return "";
    }

    public static String formatarDataComHifenYYYYMMDDHHMMSS(Date data) {
        if (data != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pt", "BR"));
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(data);
            return dateFormat.format(calendar.getTime());
        }
        return "";
    }
    
    public static Date converterDataDDMMYYYY(String data) throws ParseException {
        if (data != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
            return dateFormat.parse(data);
        }
        return null;
    }
    
    public static Date converterDataYYYYMMDD(String data) throws ParseException {
        if (data != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("pt", "BR"));
            return dateFormat.parse(data);
        }
        return null;
    }
    
    public static String converterYYYYMMMDD_Para_DDMMYYYY(String data) throws ParseException {
        if (data != null) {
            DateFormat dateFormatYYYYMMDD = new SimpleDateFormat("yyyy/MM/dd", new Locale("pt", "BR"));
            Date dataGerada = dateFormatYYYYMMDD.parse(data);
            
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(dataGerada);
            return dateFormat.format(calendar.getTime());
        }
        return null;
    }
    
    public static Date converterDataDDMMYYYYHHMM(String data) throws ParseException {
        if (data != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("pt", "BR"));
            TimeZone brazilianTimeZone = TimeZone.getTimeZone ("Brazil/East");
            dateFormat.setTimeZone(brazilianTimeZone);
            return dateFormat.parse(data);
        }
        return null;
    }
    
    public static Date converterDataDDMMYYYYHHMMSS(String data) throws ParseException {
        if (data != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("pt", "BR"));
            return dateFormat.parse(data);
        }
        return null;
    }

    public static String formatarDataDDMMYY(Date data) {
        if (data != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", new Locale("pt", "BR"));
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(data);
            return dateFormat.format(calendar.getTime());
        }
        return "";
    }

    public static String formatarDataYYYYMMDD(Date data) {
        if (data != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", new Locale("pt", "BR"));
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(data);
            return dateFormat.format(calendar.getTime());
        }
        return "";
    }
    
    public static String formatarDataComHifenYYYYMMDD(Date data) {
        if (data != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("pt", "BR"));
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(data);
            return dateFormat.format(calendar.getTime());
        }
        return "";
    }

    public static String formatarHoraHHMM(Date data) {
        if (data != null) {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm", new Locale("pt", "BR"));
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(data);
            return dateFormat.format(calendar.getTime());
        }
        return "";
    }
    
    public static String horaString(Date data) {
        if (data != null) {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", new Locale("pt", "BR"));
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(data);
            return dateFormat.format(calendar.getTime());
        }
        return "";
    }

    public static String formatarDataHora(Date data) {
        if (data != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("pt", "BR"));
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(data);
            return dateFormat.format(calendar.getTime());
        }
        return "";
    }

    /**
     * Remove as horas, minutos e segundos da data e retorna somente data
     *
     * @param data
     * @return
     */
    public static Date extrairData(Date data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Formata a data utilizando o SimpleDateFormat
     *
     * @param data
     * @param padrao (dd/MM/yyy, hh:mm:ss, etc)
     * @return
     */
    public static String formatarData(Date data, String padrao) {
        Locale BRAZIL = new Locale("pt", "BR");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(padrao, BRAZIL);
        return simpleDateFormat.format(data);
    }

    public static int DIFERENCA_ENTRE_DATAS(Date data1, Date data2) {

        GregorianCalendar ini = new GregorianCalendar();
        GregorianCalendar fim = new GregorianCalendar();

        ini.setTime(data1);
        fim.setTime(data2);

        long dt1 = ini.getTimeInMillis();
        long dt2 = fim.getTimeInMillis();

        return (int) (((dt2 - dt1) / 86400000) + 1);
    }

    public static int DIFERENCA_ENTRE_DATAS_SEM_CONTAR_DIA_INICIAL(Date data1, Date data2) {

        GregorianCalendar ini = new GregorianCalendar();
        GregorianCalendar fim = new GregorianCalendar();
        
        data1 = setarHorarioNaData(data1, 0, 0, 0);
        data2 = adicionarOuRemoverHorarioNaData(data2, 2);

        ini.setTime(data1);
        fim.setTime(data2);

        long dt1 = ini.getTimeInMillis();
        long dt2 = fim.getTimeInMillis();

        return (int) (((dt2 - dt1) / 86400000));
    }

    public static long DIFERENCA_ENTRE_DATAS_MINUTOS(Date data1, Date data2) {

        GregorianCalendar ini = new GregorianCalendar();
        GregorianCalendar fim = new GregorianCalendar();

        ini.setTime(data1);
        fim.setTime(data2);

        long millis = fim.getTimeInMillis() - ini.getTimeInMillis();
        long segundos = millis / 1000;
        long minutos = segundos / 60;
        //long horas = minutos / 60;

        return minutos;
    }

    public static Integer getMes(Date data) {
        if (data != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(data);
            return c.get(Calendar.MONTH);
        } else {
            return null;
        }
    }

    public static Integer getAno(Date data) {
        if (data != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(data);
            return c.get(Calendar.YEAR);
        } else {
            return null;
        }
    }

    public static int calculaIdade(Date dataNascimento) {

        // Data de hoje. 
        GregorianCalendar gc = new GregorianCalendar();
        int diah = gc.get(Calendar.DAY_OF_MONTH);
        int mesh = gc.get(Calendar.MONTH) + 1;
        int anoh = gc.get(Calendar.YEAR);

        // Data do nascimento. 
        gc.setTime(dataNascimento);
        int dian = gc.get(Calendar.DAY_OF_MONTH);
        int mesn = gc.get(Calendar.MONTH) + 1;
        int anon = gc.get(Calendar.YEAR);
        // Idade. 
        int idade;

        if (mesn < mesh || (mesn == mesh && dian <= diah)) {
            idade = anoh - anon;
        } else {
            idade = (anoh - anon) - 1;
        }

        return (idade);
    }
    
    public static Boolean diaUtil(Date date){
    	Calendar data = Calendar.getInstance(); data.setTime(date); 
    	Boolean diaUtil = true;
    	if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){ //#Sábado
    		return diaUtil = false;           
    	}    	
    	else if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){ //#Domingo
    		return diaUtil = false;            
        }
		return diaUtil; 
    }
    
    public static String extrairDia(Date date) {
	    Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Integer dia = calendar.get(Calendar.DAY_OF_MONTH);
		return dia.toString();
    }
    
    public static String extrairMes(Date date) {
	    Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Integer mes = calendar.get(Calendar.MONTH);
		return mes.toString();
    }
    
    public static String extrairAno(Date date) {
	    Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Integer ano = calendar.get(Calendar.YEAR);
		return ano.toString();
    }
    
    public static String extrairHora(Date date) {
	    Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Integer hora = calendar.get(Calendar.HOUR);
		return hora.toString();
    }
}
