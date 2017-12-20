package com.waterfeeds.gproxy.util;

import java.util.BitSet;

public class ByteUtil {
    public static byte[] int2Byte(int num, int len) {
        byte[] targets = new byte[len];
        for (int i = 0; i < len; i++) {
            int offset = (len - 1 - i) * 8;
            targets[i] = (byte) ((num >>> offset) & 0xFF);
        }
        return targets;
    }

    public static int byte2Int(byte[] bytes) {
        int len = bytes.length;
        int value = 0;
        for (int i = 0; i < len; i++) {
            int offset = (len - 1 - i) * 8;
            value = (int) (value | ((bytes[i] & 0xFF) << offset));
        }
        return (int) value;
    }

    public static BitSet int2Bit(int num, int len) {
        BitSet bits = new BitSet(len);
        for (int i = 0; i < len; i++) {
            //int offset = (len - 1 - i)
        }
        return bits;
    }

    public static int bit2Int(BitSet bits) {
        return 0;
    }

    public static void print(byte[] bytes) {
        for (byte b : bytes) {
            System.out.println(Integer.toHexString(b));
        }
    }

    public static void main(String[] args) {
        byte[] bytes = int2Byte(10000, 4);
        print(bytes);
        System.out.println(byte2Int(bytes));
    }
}
