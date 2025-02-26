package test;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class Client {
    private JTextPane clientAdd;
    private JTextPane port;
    private JFrame frame;
    private JButton send;
    private JTextArea message;
    private JTextArea chatPanel;
    private Socket socket;
    private int serverPort = 8080;
    private String ClientAdd = "127.0.0.1";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Client window = new Client();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Client() {
        initialize();
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

        JLabel lblNewLabel = new JLabel("服务器地址");
        lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 16));
        lblNewLabel.setBounds(23, 10, 80, 26);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("端口号");
        lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 16));
        lblNewLabel_1.setBounds(303, 11, 80, 26);
        frame.getContentPane().add(lblNewLabel_1);

        clientAdd = new JTextPane();
        clientAdd.setBounds(137, 11, 131, 21);
        frame.getContentPane().add(clientAdd);

        port = new JTextPane();
        port.setBounds(369, 15, 125, 21);
        frame.getContentPane().add(port);

        JButton link = new JButton("连接");
        link.addActionListener(e -> connect());
        link.setFont(new Font("宋体", Font.PLAIN, 16));
        link.setBounds(545, 13, 93, 23);
        frame.getContentPane().add(link);

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(23, 46, 622, 284);
        frame.getContentPane().add(panel_1);
        panel_1.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 0, 622, 284);
        panel_1.add(scrollPane);

        chatPanel = new JTextArea();
        chatPanel.setEditable(false);
        scrollPane.setViewportView(chatPanel);

        send = new JButton("发送消息");
        send.setBounds(556, 340, 89, 23);
        frame.getContentPane().add(send);
        send.addActionListener(e -> sendMessage());

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(23, 338, 522, 24);
        frame.getContentPane().add(scrollPane_1);

        message = new JTextArea();
        scrollPane_1.setViewportView(message);
    }

    private void sendMessage() {
        if (socket != null && socket.isConnected()) {
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                String sentMessage = message.getText();
                chatPanel.append("客户端：" + sentMessage + "\n");
                bw.write(sentMessage);
                bw.newLine();
                bw.flush();
                message.setText("");
            } catch (IOException e) {
                e.printStackTrace();
                chatPanel.append("\n发送消息时发生错误：" + e.getMessage());
            }
        } else {
            chatPanel.append("\n未连接到服务器，请先连接！");
        }
    }

    private void connect() {
        try {
            int clientPort = Integer.parseInt(port.getText());
            chatPanel.setText("准备连接服务器......");
            if (clientPort == serverPort) {
                socket = new Socket(ClientAdd, clientPort);
                chatPanel.append("\n连接服务器成功！\n");
                new Thread(() -> {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String serverMessage;
                        while (true) {
                        	if((serverMessage = br.readLine()) != null) {
                        		chatPanel.append("服务器：" + serverMessage + "\n");
                        	}
                            
                        }
                    } catch (IOException e) {
                        if (socket != null &&!socket.isClosed()) {
                            chatPanel.append("\n接收服务器消息时发生错误：" + e.getMessage());
                        }
                    }
                }).start();
            } else {
                chatPanel.append("\n连接失败，请检查端口或地址是否正确！");
            }
        } catch (NumberFormatException ex) {
            chatPanel.append("\n端口号输入无效，请输入有效的端口号！");
        } catch (IOException ex) {
            chatPanel.append("\n连接服务器时发生错误：" + ex.getMessage());
        }
    }

    public JTextPane getClientAdd() {
        return clientAdd;
    }
}