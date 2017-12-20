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
            int offset = (len - 1 - i);
            bits.set(i, ((num >>> offset) & 0x1) == 1);
        }
        return bits;
    }

    public static BitSet mergeBits(BitSet ...bits) {
        int len = bits.length;
        BitSet bitSets = new BitSet(len);
        int index = 0;
        for (BitSet bit: bits) {
            int bLen = bit.length();
            for (int i = 0; i < bLen; i++) {
                bitSets.set(index ++, bit.get(i));
            }
        }
        return bitSets;
    }

    public static byte[] bit2Byte(BitSet bits) {
        int len = bits.length();
        if (len % 8 != 0)
            return new byte[0];
        int bytesLen = len / 8;
        byte[] bytes = new byte[bytesLen];
        for (int i = 0; i < len; i = i + 8) {
            for (int j = 0; j < 8; j++) {
                int offset = 7 - j;
                bytes[i / 8] |= ((bits.get(i + j) ? 0x1 : 0x0) & 0xFF) << offset;
            }
        }
        return bytes;
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
