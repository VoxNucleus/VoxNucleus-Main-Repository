/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.FallingDownLib.CommonClasses.util;

import java.nio.LongBuffer;
import java.nio.ByteBuffer;

public class LongToByteValue {

    public static byte[] longToByteArray(long l) {
        byte[] bArray = new byte[8];
        ByteBuffer bBuffer = ByteBuffer.wrap(bArray);
        LongBuffer lBuffer = bBuffer.asLongBuffer();
        lBuffer.put(0, l);
        return bArray;
    }

    public static long byteArrayToLong(byte[] b) {
        long l=0;
        for (int i = 0; i < 8; i++) {
            l <<= 8;
            l ^= (long) b[i] & 0xFF;
        }
        return l;
    }
}
