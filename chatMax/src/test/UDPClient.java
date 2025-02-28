package test;

import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class UDPClient {
    private JTextPane clientAdd;
    private JTextPane port;
    private JFrame frame;
    private JButton send;
    private JTextArea message;
    private JTextArea chatPanel;
    private int serverPort = 8080;
    private String ClientAdd = "127.0.0.1";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UDPClient window = new UDPClient();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public UDPClient() {
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
        chatPanel.setText("输入端口号及地址以连接服务器！");

        send = new JButton("发送消息");
        send.setBounds(556, 340, 89, 23);
        frame.getContentPane().add(send);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(23, 338, 522, 24);
        frame.getContentPane().add(scrollPane_1);

        message = new JTextArea();
        scrollPane_1.setViewportView(message);
    }
}