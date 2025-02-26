package test;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server {
    private int monitorPort = 8080;
    private JFrame frame;
    private JTextArea chatPanel;
    private JTextArea message;
    private JButton send;
    private Socket client;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Server window = new Server();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Server() {
        initialize();
        startServer();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 700, 420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 686, 1);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(23, 11, 622, 319);
        frame.getContentPane().add(panel_1);
        panel_1.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 0, 622, 319);
        panel_1.add(scrollPane);

        chatPanel = new JTextArea();
        chatPanel.setEditable(false);
        scrollPane.setViewportView(chatPanel);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(23, 338, 522, 24);
        frame.getContentPane().add(scrollPane_1);

        message = new JTextArea();
        scrollPane_1.setViewportView(message);

        send = new JButton("发送消息");
        send.setBounds(556, 340, 89, 23);
        frame.getContentPane().add(send);
        send.addActionListener(e -> sendMessage());
    }

    private void startServer() {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(monitorPort)) {
                chatPanel.append("服务器已启动，等待客户端连接...\n");
                client = server.accept();
                chatPanel.append("客户端已连接！\n");
                try (BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                    String clientMessage;
                    while (true) {
                    	if((clientMessage = br.readLine()) != null) {
                    		chatPanel.append("客户端：" + clientMessage + "\n");
                    	}
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendMessage() {
        if (client != null && client.isConnected()) {
            try {
                String sentMessage = message.getText();
                chatPanel.append("服务器：" + sentMessage + "\n");
                BufferedWriter os = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                os.write(sentMessage);
                os.newLine();
                os.flush();
                message.setText("");
            } catch (IOException e) {
                e.printStackTrace();
                chatPanel.append("\n发送消息时发生错误：" + e.getMessage());
            }
        } else {
            chatPanel.append("\n未连接到客户端！");
        }
    }
}