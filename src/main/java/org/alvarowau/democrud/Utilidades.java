package org.alvarowau.democrud;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilidades {

    public static boolean isNumero(String numero){
        try{
            Integer.parseInt(numero);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    public static boolean isPositive(String experiencia) {
        return Integer.parseInt(experiencia) >= 0;
    }

    public static boolean isFormatoFechaCorrecto(String fechaNacimiento) {
        try {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            formato.setLenient(false);  // Esto asegura que el formato sea estricto
            formato.parse(fechaNacimiento);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }


    public static Date fechaFormateada(String fechaNacimiento) {
        try {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            formato.setLenient(false);  // Esto asegura que el formato sea estricto
            return formato.parse(fechaNacimiento);
        } catch (ParseException e) {
            return null;
        }
    }
}
