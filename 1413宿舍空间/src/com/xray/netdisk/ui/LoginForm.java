package com.xray.netdisk.ui;

import com.xray.netdisk.utils.NetworkUtilsV2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btLogin;
    private JButton btExit;
    private JPanel panel1;
    private static JFrame loginFrame;

    public LoginForm() {
        //点击登录按钮
        btLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = txtPassword.getText();
                if(username.isEmpty()){
                    JOptionPane.showMessageDialog(null,"账号不能为空");
                    return;
                }
                if(password.isEmpty()){
                    JOptionPane.showMessageDialog(null,"密码不能为空");
                    return;
                }
                //服务器登录
                Long userId = NetworkUtilsV2.login(username, password);
                if(userId == null || userId == -1){
                    JOptionPane.showMessageDialog(null,"登录失败");
                    return;
                }
                //登录成功，打开客户端窗体
                showClientForm(userId);
                //关闭登录窗体
                loginFrame.dispose();
            }
        });
    }

    public void showClientForm(Long userId){
        JFrame frame = new JFrame("ClientForm");
        frame.setContentPane(new ClientForm(userId).panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(600,400);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        loginFrame = new JFrame("LoginForm");
        loginFrame.setContentPane(new LoginForm().panel1);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.pack();
        loginFrame.setSize(400,200);
        loginFrame.setVisible(true);
    }
}
