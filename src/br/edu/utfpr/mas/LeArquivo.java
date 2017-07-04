/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.mas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ronaldo
 */
public class LeArquivo {

    private static final String SPACE = " ";       //#define do c
    private static final Calendar CALENDAR = Calendar.getInstance();

    /**
     * Lê o arquivo e faz a transformações necessárias, armazenado-as no
     * stringbuilder
     *
     * @param fileName Noem do arquivo a ser lido
     * @return stringbuilder já tudo OK.
     * @throws java.text.ParseException
     */
    public StringBuilder leitura(String fileName) throws ParseException {
        System.out.println(fileName);
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader buffer = new BufferedReader(new FileReader(fileName));

            String line = buffer.readLine();
            int count = 0, totalLinhas = 1;
            boolean setFirstTime = false;
            Date dateTime;
            String time;
            String strDateTime;
            int interval;

            int[] r = new int[100];
            int[] cpu = new int[100];
            int[] swp = new int[100];
            int[] free = new int[100];
            int[] bff = new int[100];
            int[] cache = new int[100];

            int space = line.indexOf(SPACE);

            String serverName = line.substring(0, space);
            dateTime = getDate(line.substring(9, 28).trim(), "yyyy-MM-dd HH:mm:ss");
            CALENDAR.setTime(dateTime);
            time = getTime(dateTime, "HH:mm:ss");
            strDateTime = getTime(dateTime, "yyyy-MM-dd HH:mm:ss");

            int hour;
            int minute;
            int second;
            Date lineDate;
            String lineTime;
            int totalSecond = 0;
            Calendar lineCalendar = Calendar.getInstance();

            interval = 900;

            while (true) {

                if (line != null) {
                    lineDate = getDate(line.substring(9, 28), "yyyy-MM-dd HH:mm:ss");
                    System.out.println("linha1: " + line);
                    lineCalendar.setTime(lineDate);
                    lineTime = getTime(lineDate, "HH:mm:ss");

                    hour = lineCalendar.get(Calendar.HOUR_OF_DAY);
                    minute = lineCalendar.get(Calendar.MINUTE);
                    second = lineCalendar.get(Calendar.SECOND);

                    totalSecond = hour * 3600 + minute * 60 + second;
                }

                if (totalSecond > interval || line == null) {
                    sb.append(serverName).append(SPACE);

                    if (!setFirstTime) {
                        setFirstTime = true;

                    } else {
                        CALENDAR.add(Calendar.MINUTE, 15);

                        strDateTime = getTime(CALENDAR.getTime(), "yyyy-MM-dd HH:mm:ss");
                    }

                    // Adiciona no buffer as informações lidas.
                    sb.append(strDateTime)
                            .append(SPACE);

                    sb.append(getDay())
                            .append(SPACE)
                            .append(getMonth())
                            .append(SPACE)
                            .append(getYear())
                            .append(SPACE);

                    sb.append(getMax(r))
                            .append(SPACE)
                            .append((int) getAvg(r))
                            .append(SPACE);

                    sb.append(formatDouble(getMax(cpu)))
                            .append(SPACE)
                            .append(formatDouble(getAvg(cpu)))
                            .append(SPACE);

                    sb.append(getMax(swp))
                            .append(SPACE)
                            .append((int) getAvg(swp))
                            .append(SPACE);

                    sb.append(getMax(free))
                            .append(SPACE)
                            .append((int) getAvg(free))
                            .append(SPACE);

                    sb.append((int) getMax(bff))
                            .append(SPACE)
                            .append((int) getAvg(bff))
                            .append(SPACE);

                    sb.append((int) getMax(cache))
                            .append(SPACE)
                            .append((int) getAvg(cache))
                            .append(SPACE)
                            .append("\n");

                    count = 0;

                    if (line == null) {
                        break;
                    }

                    // zera os vetores
                    for (int i = 0; i < 60; i++) {
                        r[i] = 0;
                        swp[i] = 0;
                        free[i] = 0;
                        bff[i] = 0;
                        cache[i] = 0;
                        cpu[i] = 0;
                    }

                    interval += 900;
                }

                // Lendo as informações de ranquue, swap, memória.
                //r[count] = Integer.parseInt(line.substring(30, 31));
                r[count] = Integer.parseInt(line.substring(30, 33).trim());

                swp[count] = Integer.parseInt(line.substring(38, 41).trim());

                System.out.println("linha2: " + line);
                System.out.println("linha: " + count);
                System.out.println(line);
                free[count] = Integer.parseInt(line.substring(42, 48).trim()) / 1024;
                System.out.println("free: " + free[count]);

                bff[count] = Integer.parseInt(line.substring(50, 55).trim()) / 1024;
                System.out.println("bff: " + bff[count]);

                cache[count] = Integer.parseInt(line.substring(57, 63).trim()) / 1024;
                System.out.println("cache: " + cache[count]);

                String idCpu = line.substring(102, 104).trim();
                System.out.println("cpu: " + idCpu);
                cpu[count] = 100 - (Integer.parseInt(idCpu));
                count++;

                // Vai para a proxima linha
                line = buffer.readLine();
            }

            System.out.println("Total de linhas lidas:" + totalLinhas);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(LeArquivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LeArquivo.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sb;
    }

    private Date getDate(String dateString, String format) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = dateFormat.parse(dateString);

        return date;
    }

    private String getTime(Date dateTime, String format) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(dateTime);
    }

    private int getDay() {
        return CALENDAR.get(Calendar.DAY_OF_MONTH);
    }

    private int getMonth() {
        return CALENDAR.get(Calendar.MONTH) + 1; // janary start with 0
    }

    private int getYear() {
        return CALENDAR.get(Calendar.YEAR);
    }

    private int getMax(int[] array) {
        return Arrays.stream(array).max().getAsInt();
    }

    private double getAvg(int[] array) {
        double total = 0;

        for (int i = 0; i < array.length; i++) {
            total += array[i];
        }

        return total / array.length;
    }

    private String formatDouble(double valor) {

        return String.format("%.2f", valor);
    }

}
