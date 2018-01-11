package com.waterfeeds.gproxy.client.listener;

import com.alibaba.fastjson.JSONObject;
import com.waterfeeds.gproxy.client.Chat;
import com.waterfeeds.gproxy.network.ChannelManager;
import com.waterfeeds.gproxy.protocol.GproxyBody;
import com.waterfeeds.gproxy.protocol.GproxyHeader;
import com.waterfeeds.gproxy.protocol.GproxyProtocol;
import com.waterfeeds.gproxy.protocol.base.GproxyCommand;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ButtonListener implements ActionListener {
    private Chat chat;
    private String contextUser = "";

    public ButtonListener(Chat chat) {
        this.chat = chat;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chat.getButton()) {
            String message = chat.getInputArea().getText().trim();
            JSONObject object = new JSONObject();
            object.put("user", chat.getUser());
            object.put("msg", message);
            String content = object.toString();
            if (!StringUtils.isBlank(message)) {
                ChannelManager manager = chat.getManager();
                if (manager.isAvailable()) {
                    GproxyBody body = new GproxyBody(content);
                    GproxyHeader header = new GproxyHeader(GproxyCommand.CLIENT_EVENT, 0, body.getContentLen());
                    GproxyProtocol protocol = new GproxyProtocol(header, body);
                    manager.getChannel().writeAndFlush(protocol);
                    chat.getInputArea().setText("");
                }
            }
        }
    }

    public void receiveMessage(GproxyProtocol protocol) {
        String content = protocol.getBody().getContent();
        JSONObject object = JSONObject.parseObject(content);
        String user = object.getString("user");
        String message = object.getString("msg");
        StringBuilder showText = new StringBuilder(chat.getShowArea().getText());
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String dateTime = df.format(new Date());
        if (!user.equals(contextUser)) {
            showText.append(user + " " + dateTime + "\r\n");
            contextUser = user;
        }
        showText.append(message + "\r\n");
        chat.getShowArea().setText(showText.toString());
    }
}
