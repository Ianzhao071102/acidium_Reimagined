package org.izdevs.acidium.basic;

public class IDGenerator {
    static int counter = 0;
    public static synchronized int createId(){
        counter++;
        return counter;
    }
}
