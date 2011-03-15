package me.FallingDownLib.StringUtil.random;

import java.util.Random;

/**
 * Weak char sequence generator. But Fast.
 * @author victork
 */
public class WeakRandomSequenceGenerator {

    public static String charArray="abcdefghijklmnopqrstuvwxyz"
            + "ABCDEFGHIJKLMNOPQRSTUVWXTZ0123456789";
    private Random randomGenerator;
    private String result;
    private int length;

    protected WeakRandomSequenceGenerator(int neededLength){
        randomGenerator= new Random();
        length=neededLength;
    }

    public static WeakRandomSequenceGenerator getGenerator(int neededLength){
        return new WeakRandomSequenceGenerator(neededLength);
    }

    private String constructRandomString(){
        StringBuilder random_builder = new StringBuilder();
        for(int index =0; index<length;index++ ){
            random_builder.append(charArray.charAt(randomGenerator.nextInt(32)));
        }
        return random_builder.toString();
    }

    public String getSequence(){
        result= constructRandomString();
        return result;
    }
}
