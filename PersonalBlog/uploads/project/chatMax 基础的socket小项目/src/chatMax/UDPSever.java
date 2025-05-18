package chatMax;

import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
    
    private JLabel wordCountLabel;
    private JScrollPane scrollPane_1;

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
        frame.setBounds(100, 100, 700, 480);
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

        wordCountLabel = new JLabel("字数: 0");
        wordCountLabel.setBounds(556, 365, 100, 20);
        frame.getContentPane().add(wordCountLabel);
        
        scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(23, 338, 522, 20);
        frame.getContentPane().add(scrollPane_1);

        message = new JTextArea();
        scrollPane_1.setViewportView(message);
        message.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateWordCountAndSize();
            }
        });

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
                // 创建一个字节数组用于接收数据,最大可接受约674个汉字（utf-8下），则我们可以设置最多发送500个字
            	byte[] buffer = new byte[2024];
                // 创建一个 DatagramPacket 对象用于接收数据包
                DatagramPacket recivePocket = new DatagramPacket(buffer, buffer.length);
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
                        Thread.sleep(100); // 由于只是两个人的聊天器，故不需要那么频繁接收
                    } catch (IOException e) {
                        // 接收消息出错时打印异常信息
                        e.printStackTrace();
                    } catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
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
            if(sendMessage.length() <= 500) {
            	// 在聊天记录中添加服务器发送的消息
                chatPanel.append("服务器:" + sendMessage + "\n");
                // 发送数据包到客户端
                serverSocket.send(sendPocket);
            }else {
            	JOptionPane.showMessageDialog(null, "输入字数不能超过500，请减少字数或分次发送！","提示",JOptionPane.ERROR_MESSAGE);
            }
            // 清空输入框
            message.setText("");
        } catch (IOException e) {
            // 发送消息出错时打印异常信息
            e.printStackTrace();
        }
    }
    
    private void updateWordCountAndSize() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    // 获取输入的文本
                    String text = message.getText();
                    // 更新字数显示
                    wordCountLabel.setText("字数: " + text.length());

                    // 强制重新绘制以确保实时刷新
                    wordCountLabel.repaint();

                    // 计算输入的行数
                    int lineCount = message.getLineCount();
                    int initialHeight = 20;
                    int maxHeight = initialHeight * 4;

                    int newHeight = initialHeight * lineCount;
                    if (newHeight > maxHeight) {
                        newHeight = maxHeight;
                    }
                    java.awt.Rectangle bounds = scrollPane_1.getBounds();
                    if (bounds.height != newHeight) {
                        bounds.height = newHeight;
                        scrollPane_1.setBounds(bounds);
                        frame.revalidate();
                        frame.repaint();
                    }
                    Thread.sleep(100); // 设置线程休眠100毫秒，防止CPU过高
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}