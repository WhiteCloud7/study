package chatMax;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;


// 定义一个 UDP 客户端类，用于与 UDP 服务器进行通信
public class UDPClient {
    // 存储用户输入的服务器地址的文本框
    private JTextPane clientAdd;
    // 存储用户输入的服务器端口号的文本框
    private JTextPane port;
    // 客户端的主窗口
    private JFrame frame;
    // 发送消息的按钮
    private JButton send;
    // 用于输入要发送消息的文本区域
    private JTextArea message;
    // 显示聊天记录的文本区域
    private JTextArea chatPanel;
    // 服务器的端口号
    private int serverPort;
    // 服务器的 IP 地址
    InetAddress serverAdd;
    // UDP 套接字，用于发送和接收数据包
    DatagramSocket clientSocket;
    
    private JLabel wordCountLabel;
    private JScrollPane scrollPane_1;

    // 程序入口方法，创建并显示客户端窗口
    public static void main(String[] args) {
        // 使用 EventQueue.invokeLater 确保 GUI 在事件调度线程中创建和显示
        EventQueue.invokeLater(() -> {
            try {
                // 创建 UDPClient 实例
                UDPClient window = new UDPClient();
                // 显示客户端窗口
                window.frame.setVisible(true);
            } catch (Exception e) {
                // 打印异常堆栈信息
                e.printStackTrace();
            }
        });
    }

    // 构造函数，调用初始化方法
    public UDPClient() {
        initialize();
    }

    // 初始化客户端界面，此部分省略 UI 相关注释
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 700, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setTitle("客户端");

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
        link.setFont(new Font("宋体", Font.PLAIN, 16));
        link.setBounds(545, 13, 93, 23);
        frame.getContentPane().add(link);
        // 为连接按钮添加点击事件监听器，点击时调用 reciveMessage 方法
        link.addActionListener(e -> {
            reciveMessage();
        });

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
        chatPanel.setText("输入服务器端口号及地址以连接服务器！\n");

        wordCountLabel = new JLabel("字数: 0");
        wordCountLabel.setBounds(556, 365, 100, 20);
        frame.getContentPane().add(wordCountLabel);
        
        scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(23, 338, 522, 24);
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

    // 接收服务器消息的方法
    private void reciveMessage() {
        try {
            // 获取用户输入的服务器地址
            String keyAdd = clientAdd.getText();
            // 将用户输入的端口号转换为整数
            serverPort = Integer.parseInt(port.getText());
            // 根据输入的地址获取服务器的 InetAddress 对象
            serverAdd = InetAddress.getByName(keyAdd);
            // 创建 UDP 套接字
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            // 套接字创建失败时打印异常信息
            e.printStackTrace();
        } catch (UnknownHostException e) {
            // 无法解析服务器地址时打印异常信息
            e.printStackTrace();
        }
        // 启动一个新线程来接收服务器消息
        new Thread(() -> {
            // 检查端口号是否为空且是否为 8080
            if (port.getText() != "" && serverPort == 8080) {
                // 在聊天记录中显示连接成功信息
                chatPanel.setText("连接到服务器！\n");
                // 创建一个字节数组用于接收数据,最大可接受约674个汉字（utf-8下），则我们可以设置最多发送500字
            	byte[] buffer = new byte[2024];
                // 创建一个 DatagramPacket 对象用于接收数据包
                DatagramPacket recivePocket = new DatagramPacket(buffer, buffer.length);
                // 进入一个无限循环持续接收服务器消息
                while (true) {
                    try {
                        // 接收服务器发送的数据包
                        clientSocket.receive(recivePocket);
                        // 将接受的消息转换为字符串，并处理换行符,注意sendPocket每次从messageContext开始暂存消息，但如果前一次的消息比后一次长，
                        //那么后一次可能会包含前一次的消息，故我们接受sendPocket的真实长度而不是接受整个sendPocket
                        String recivedMessage = new String(recivePocket.getData(), 0, recivePocket.getLength(), "utf-8").replaceAll("\n", "\n               ");
                        // 在聊天记录中添加服务器发送的消息
                        chatPanel.append("服务器：" + recivedMessage + "\n");
                        Thread.sleep(100); // 由于只是两个人的聊天器，故不需要那么频繁接收
                    } catch (IOException e) {
                        // 接收消息出错时打印异常信息
                        e.printStackTrace();
                    } catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
                }
            } else {
                // 端口号为空或不是 8080 时，在聊天记录中显示错误信息
                chatPanel.setText("地址或端口错误！\n");
            }
        }).start();
    }

    // 发送消息到服务器的方法
    private void sendMessage() {
        try {
            // 将用户输入的消息转换为字节数组
            byte[] senrMessage = message.getText().getBytes();
            // 创建一个 DatagramPacket 对象，用于发送消息到服务器
            DatagramPacket sendPacket = new DatagramPacket(senrMessage, senrMessage.length, serverAdd, serverPort);
            // 将发送的消息转换为字符串，并处理换行符,注意sendPocket每次从messageContext开始暂存消息，但如果前一次的消息比后一次长，
            //那么后一次可能会包含前一次的消息，故我们接受sendPocket的真实长度而不是接受整个sendPocket
            String messaageShowPanel = new String(sendPacket.getData(), 0, sendPacket.getLength(), "utf-8").replaceAll("\n", "\n               ");
            if(messaageShowPanel.length() <= 500) {
            	// 在聊天记录中添加客户端发送的消息
                chatPanel.append("客户端：" + messaageShowPanel + "\n");
                // 发送数据包到服务器
                clientSocket.send(sendPacket);
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