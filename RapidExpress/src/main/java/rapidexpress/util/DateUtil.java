/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.util;

/**
 *
 * @author User
 */
public class DateUtil {
  /*Propósito: Utilidades para fechas.
Métodos estáticos:
public static Date getCurrentDate(): Retorna new Date()
public static String formatDate(Date date):
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")
Retorna sdf.format(date)
public static String formatDateTime(Date date):
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
Retorna sdf.format(date)
public static Date parseDate(String dateString):
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")
Retorna sdf.parse(dateString)
public static Date addDays(Date date, int days):
Calendar cal = Calendar.getInstance()
cal.setTime(date)
cal.add(Calendar.DAY_OF_MONTH, days)
Retorna cal.getTime()
public static boolean betweenDates(Date date, Date start, Date end):
Retorna !date.before(start) && !date.after(end)
public static long daysBetween(Date start, Date end):
Retorna (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24)*/  
}
