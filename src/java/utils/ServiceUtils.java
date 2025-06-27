/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author Admin
 */
public class ServiceUtils {
    public static boolean checkStatus(String statusInput, String... statusList) {
        for (String item : statusList) {
            if(statusInput.equalsIgnoreCase(item)){
                return true;
            }
        }
        return false;
    }
    
    public static boolean isNullOrEmptyString(String str){
        return str == null || str.isEmpty();
    }
}
