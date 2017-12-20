package com.waterfeeds.gproxy.util;

import java.util.BitSet;

public class Bit {
    private BitSet bit;
    private int len;

    public Bit(BitSet bit, int len) {
        this.bit = bit;
        this.len = len;
    }

    public BitSet getBit() {
        return bit;
    }

    public void setBit(BitSet bit) {
        this.bit = bit;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }
}
