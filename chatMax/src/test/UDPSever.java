package test;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

// 定义一个 UDP 服务器类，用于处理 UDP 通信
public class UDPSever {
    // 服务器的主窗口
    private JFrame frame;
    // 显示聊天记录的文本区域
    private JTextArea chatPanel;
    // 用于输入要发送消息的文本区域
    private JTextArea message;
    // 发送消息的按钮
    private JButton send;
    // UDP 套接字，用于接收和发送数据包
    private DatagramSocket serverSocket;
    // 客户端的 IP 地址
    private InetAddress clientAdd;
    // 客户端的端口号
    private int clientPort;

    // 程序入口方法，创建并显示服务器窗口
    public static void main(String[] args) {
        // 使用 EventQueue.invokeLater 确保 GUI 在事件调度线程中创建和显示
        EventQueue.invokeLater(() -> {
            try {
                // 创建 UDPSever 实例
                UDPSever window = new UDPSever();
                // 显示服务器窗口
                window.frame.setVisible(true);
            } catch (Exception e) {
                // 打印异常堆栈信息
                e.printStackTrace();
            }
        });
    }

    // 构造函数，调用初始化方法并开始接收消息
    public UDPSever() {
        initialize();
        reciveMessage();
    }

    // 初始化服务器界面，此部分省略 UI 相关注释
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 700, 420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setTitle("服务器");

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
        // 为发送消息按钮添加点击事件监听器，点击时调用 sendMessage 方法
        send.addActionListener(e -> {
            sendMessage();
        });
    }

    // 接收客户端消息的方法
    private void reciveMessage() {
        try {
            // 在聊天记录中显示服务器已就绪信息
            chatPanel.setText("服务器已就绪，等待客户端连接......\n");
            // 创建一个 UDP 套接字并绑定到端口 8080
            serverSocket = new DatagramSocket(8080);
            // 启动一个新线程来持续接收客户端消息
            new Thread(() -> {
                // 创建一个字节数组用于接收数据
                byte[] recivedMessage = new byte[1024];
                // 创建一个 DatagramPacket 对象用于接收数据包
                DatagramPacket recivePocket = new DatagramPacket(recivedMessage, recivedMessage.length);
                // 进入一个无限循环持续接收客户端消息
                while (true) {
                    try {
                        // 接收客户端发送的数据包
                        serverSocket.receive(recivePocket);
                        // 获取客户端的 IP 地址
                        clientAdd = recivePocket.getAddress();
                        // 获取客户端的端口号,这样只有服务器先发送一次消息才不会报错
                        clientPort = recivePocket.getPort();
                        // 将接收的消息转换为字符串，并处理换行符,注意sendPocket每次从messageContext开始暂存消息，但如果前一次的消息比后一次长，
                        //那么后一次可能会包含前一次的消息，故我们接受sendPocket的真实长度而不是接受整个sendPocket
                        String reciveMessage = new String(recivePocket.getData(), 0, recivePocket.getLength(), "utf-8").replaceAll("\n", "\n               ");
                        // 在聊天记录中添加客户端发送的消息
                        chatPanel.append("客户端:" + reciveMessage + "\n");
                    } catch (IOException e) {
                        // 接收消息出错时打印异常信息
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (SocketException e) {
            // 套接字创建失败时打印异常信息
            e.printStackTrace();
        }
    }

    // 发送消息到客户端的方法
    private void sendMessage() {
        try {
            // 将用户输入的消息转换为字节数组
            byte[] messageContext = message.getText().getBytes();
            // 创建一个 DatagramPacket 对象，用于发送消息到客户端
            DatagramPacket sendPocket = new DatagramPacket(messageContext, messageContext.length, clientAdd, clientPort);
            // 将发送的消息转换为字符串，并处理换行符,注意sendPocket每次从messageContext开始暂存消息，但如果前一次的消息比后一次长，
            //那么后一次可能会包含前一次的消息，故我们接受sendPocket的真实长度而不是接受整个sendPocket
            String sendMessage = new String(sendPocket.getData(), 0, sendPocket.getLength(), "utf-8").replaceAll("\n", "\n               ");
            // 在聊天记录中添加服务器发送的消息
            chatPanel.append("服务器:" + sendMessage + "\n");
            // 发送数据包到客户端
            serverSocket.send(sendPocket);
            // 清空输入框
            message.setText("");
        } catch (IOException e) {
            // 发送消息出错时打印异常信息
            e.printStackTrace();
        }
    }
}