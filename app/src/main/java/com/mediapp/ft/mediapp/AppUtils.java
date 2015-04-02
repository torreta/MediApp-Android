package com.mediapp.ft.mediapp;
import java.text.SimpleDateFormat;
import java.text.ParseException;
/**
 * Created by Frank on 4/1/2015.
 */
public final class AppUtils {

    private AppUtils() {}

    /*
     *Added argument: format
     *From http://www.javadb.com/check-if-a-string-is-a-valid-date/
    **/

    public static boolean isValidDate(String inDate, String format) {

        if (inDate == null)
            return false;

        //set the format to use as a constructor argument
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        if (inDate.trim().length() != dateFormat.toPattern().length())
            return false;

        dateFormat.setLenient(false);

        try {
            //parse the inDate parameter
            dateFormat.parse(inDate.trim());
        }
        catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public static boolean isValidNumber(String str){

        try{
            int i = Integer.parseInt(str);
        }catch(Exception e){
            return false;
        }
        return true;
    }

}
