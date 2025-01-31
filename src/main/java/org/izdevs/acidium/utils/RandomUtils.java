package org.izdevs.acidium.utils;

import java.security.SecureRandom;
import java.util.Random;

public class RandomUtils {
    public static final String alphabet_num = "qwertyuiopasdfghjklzxcvbnm1234567890!@#$%^&*()_+-=";
    public static String getRandomString(byte[] seed,int length){
        SecureRandom random = new SecureRandom(seed);

        return baseGen(length,random);
    }
    public static String getRandomString(int length){
        return baseGen(length,new Random());
    }
    public static String baseGen(int length, Random random){
        StringBuilder sb = new StringBuilder();
        if(length <= 0) throw new IllegalArgumentException("length should be greater than 0");
        else{
            for(int i=0;i<=length-1;i++){
                sb.append(alphabet_num.charAt(random.nextInt(0,alphabet_num.length()-1)));
            }
            return sb.toString();
        }
    }
    public static String sub_entity_name(String owner_name,String property_type_name){
        return owner_name+"_"+property_type_name+"_"+getRandomString(5);
    }
}
