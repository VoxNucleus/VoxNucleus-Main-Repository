package me.FallingDownLib.StringUtil.random;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author victork
 */
public class StrongRandomSequenceGenerator {

    private static SecureRandom random=null;
    private int length;
    private String result;

    protected StrongRandomSequenceGenerator(int neededLength) {
        if (random == null) {
            random = new SecureRandom();
        }
        length=neededLength;
    }

    public static StrongRandomSequenceGenerator getGenerator(int neededLength){
        return new StrongRandomSequenceGenerator(neededLength);
    }

    private String constructRandomString(){
        BigInteger bigInteger =new BigInteger(5*length,random);
        return bigInteger.toString(32);
    }

    public String getSequence(){
        result= constructRandomString();
        return result;
    }

}
