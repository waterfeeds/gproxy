package com.waterfeeds.gproxy.client;

import com.waterfeeds.gproxy.client.listener.ButtonListener;
import com.waterfeeds.gproxy.network.ChannelManager;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Chat {
    private ChannelManager manager;
    private JScrollPane showPane;
    private JTextArea showArea;
    private JScrollPane inputPane;
    private JTextArea inputArea;
    private JButton button;
    private ButtonListener buttonListener;
    private String user;
    private String contextUser = "";

    public Chat() {
        buttonListener = new ButtonListener(this);
    }

    public ChannelManager getManager() {
        return manager;
    }

    public void setManager(ChannelManager manager) {
        this.manager = manager;
    }

    public ButtonListener getButtonListener() {
        return buttonListener;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContextUser() {
        return contextUser;
    }

    public void setContextUser(String contextUser) {
        this.contextUser = contextUser;
    }

    public void showChatUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("聊天室");
        frame.setBounds(400, 200, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);
        frame.setVisible(true);
        user = JOptionPane.showInputDialog(null, "用户名", "输入用户名", JOptionPane.INFORMATION_MESSAGE);
        frame.setTitle(user);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                inputArea.requestFocus();
            }
        });
    }

    public void placeComponents(JPanel panel) {
        panel.setLayout(null);
        showPane = new JScrollPane();
        showPane.setBounds(5, 5, 780, 400);
        showArea = new JTextArea();
        showArea.setEditable(false);
        showPane.setViewportView(showArea);
        panel.add(showPane);
        inputPane = new JScrollPane();
        inputPane.setBounds(5, 410, 780, 100);
        inputArea = new JTextArea();
        inputPane.setViewportView(inputArea);
        panel.add(inputPane);
        button = new JButton("发送");
        button.setBounds(720, 520, 64, 35);
        button.addActionListener(buttonListener);
        panel.add(button);
    }

    public JTextArea getShowArea() {
        return showArea;
    }

    public JTextArea getInputArea() {
        return inputArea;
    }

    public JButton getButton() {
        return button;
    }
}
