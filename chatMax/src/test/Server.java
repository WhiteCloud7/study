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

// 定义一个服务器类，用于处理与客户端的通信
public class Server {
    // 服务器监听的端口号，这里设置为 8080
    private int monitorPort = 8080;
    // 服务器的主窗口
    private JFrame frame;
    // 显示聊天记录的文本区域
    private JTextArea chatPanel;
    // 用于输入要发送消息的文本区域
    private JTextArea message;
    // 发送消息的按钮
    private JButton send;
    // 与客户端建立连接的套接字
    private Socket client;

    // 程序的入口方法，使用 EventQueue.invokeLater 确保 GUI 在事件调度线程中创建和显示
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                // 创建 Server 类的实例
                Server window = new Server();
                // 显示服务器主窗口
                window.frame.setVisible(true);
            } catch (Exception e) {
                // 若出现异常，打印异常堆栈信息
                e.printStackTrace();
            }
        });
    }

    // 构造函数，调用初始化方法并启动服务器
    public Server() {
        initialize();
        startServer();
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
        send.addActionListener(e -> sendMessage());
    }

    // 启动服务器的方法，在一个新线程中运行
    private void startServer() {
        new Thread(() -> {
            try (
                    // 创建一个 ServerSocket 并绑定到指定的端口号
                    ServerSocket server = new ServerSocket(monitorPort)
            ) {
                // 在聊天面板中显示服务器已启动，等待客户端连接的信息
                chatPanel.append("服务器已启动，等待客户端连接...\n");
                // 等待客户端连接，返回一个与客户端通信的 Socket 对象
                client = server.accept();
                // 在聊天面板中显示客户端已连接的信息
                chatPanel.append("客户端已连接！\n");
                try (
                        // 创建一个 BufferedReader 用于读取客户端发送的消息
                        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()))
                ) {
                    String clientMessage;
                    // 进入一个无限循环，持续接收客户端消息
                    while (true) {
                        // 读取客户端发送的一行消息
                        if ((clientMessage = br.readLine()) != null) {
                            // 将消息中的特定字符串 &@# 替换为换行符
                            clientMessage = clientMessage.replaceAll("&@#", "\n");
                            // 将消息中的换行符替换为特定格式，用于在聊天面板中显示
                            clientMessage = clientMessage.replaceAll("\n", "\n               ");
                            // 在聊天面板中添加客户端发送的消息
                            chatPanel.append("客户端：" + clientMessage + "\n");
                        }
                    }
                } catch (IOException e) {
                    // 若接收客户端消息时出现异常，打印异常信息
                    e.printStackTrace();
                }
            } catch (IOException e) {
                // 若创建 ServerSocket 或等待客户端连接时出现异常，打印异常信息
                e.printStackTrace();
            }
        }).start();
    }

    // 发送消息到客户端的方法
    private void sendMessage() {
        // 检查客户端套接字是否不为空且已连接到客户端
        if (client != null && client.isConnected()) {
            try {
                // 获取用户输入的原始消息
                String originSentMessage = message.getText();
                // 将原始消息中的换行符替换为特定字符串 &@#，方便在网络传输中处理
                String sentMessage = originSentMessage.replaceAll("\n", "&@#");
                // 将原始消息中的换行符替换为特定格式，用于在聊天面板中显示
                String sentMessageShowPanel = originSentMessage.replaceAll("\n", "\n               ");
                // 在聊天面板中添加服务器发送的消息
                chatPanel.append("服务器：" + sentMessageShowPanel + "\n");
                // 创建一个 BufferedWriter 用于向客户端发送消息
                BufferedWriter os = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                // 向客户端发送处理后的消息
                os.write(sentMessage);
                // 写入换行符，表示消息结束
                os.newLine();
                // 刷新缓冲区，确保消息发送出去
                os.flush();
                // 清空输入框
                message.setText("");
            } catch (IOException e) {
                // 若发送消息时出现异常，打印异常信息并在聊天面板中提示
                e.printStackTrace();
                chatPanel.append("\n发送消息时发生错误：" + e.getMessage());
            }
        } else {
            // 若客户端套接字未连接到客户端，在聊天面板中提示用户未连接到客户端
            chatPanel.append("\n未连接到客户端！");
        }
    }
}