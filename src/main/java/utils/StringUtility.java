package utils;

public class StringUtility {

    public static boolean isStringNullOrBlank(String pValue){
        return pValue == null || pValue.isBlank();
    }
}