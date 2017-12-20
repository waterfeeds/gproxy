package com.waterfeeds.gproxy.util;

import java.util.BitSet;
import java.util.List;

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

    public static Bit int2Bit(int num, int len) {
        BitSet bits = new BitSet();
        for (int i = 0; i < len; i++) {
            int offset = len - 1 - i;
            bits.set(i, ((num >>> offset) & 0x1) == 1);
        }
        return new Bit(bits, len);
    }

    public static Bit bool2Bit(boolean bool) {
        BitSet bits = new BitSet();
        bits.set(0, bool);
        return new Bit(bits, 1);
    }

    public static Bit byte2Bit(byte b) {
        int len = 8;
        BitSet bits = new BitSet();
        for (int i = 0; i < len; i++) {
            int offset = len - 1 - i;
            bits.set(i, ((b >>> offset) & 0x1) == 1);
        }
        return new Bit(bits, len);
    }

    public static Bit byte2Bit(byte b, int len) {
        BitSet bits = new BitSet();
        for (int i = 0; i < len; i++) {
            int offset = len - 1 - i;
            bits.set(i, ((b >>> offset) & 0x1) == 1);
        }
        return new Bit(bits, len);
    }

    public static Bit byte2Bit(byte b, int index, int len) {
        BitSet bits = new BitSet();
        for (int i = index; i < len; i++) {
            int offset = len - 1 - i;
            bits.set(i, ((b >>> offset) & 0x1) == 1);
        }
        return new Bit(bits, len);
    }

    public static Bit mergeBits(List<Bit> bits) {
        int len = bits.size();
        int index = 0;
        BitSet bitSet = new BitSet();
        for (Bit bit : bits) {
            int bLen = bit.getLen();
            for (int i = 0; i < bLen; i++) {
                bitSet.set(index++, bit.getBit().get(i));
            }
        }
        return new Bit(bitSet, index);
    }

    public static byte[] bit2Byte(Bit bit) {
        BitSet bits = bit.getBit();
        int len = bit.getLen();
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
       /* byte[] bytes = int2Byte(10000, 4);
        print(bytes);
        System.out.println(byte2Int(bytes));*/
        Bit bits = int2Bit(15, 4);
        System.out.println(bits.toString());
        byte[] bytes = bit2Byte(bits);
        System.out.println(bytes);
    }
}
