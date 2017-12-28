package com.waterfeeds.gproxy.protocol.base;

import com.waterfeeds.gproxy.message.Const;
import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.GproxyHeader;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;

public class GproxySafe {
    public static boolean isProtocolSafe(GproxyProtocol protocol) {
        GproxyHeader header = protocol.getHeader();
        GproxyBody body = protocol.getBody();
        if (header.getSafe() == 1 && !body.getSafeSign().equals(Const.SAFE_SIGN)) {
            return false;
        }
        return true;
    }
}
