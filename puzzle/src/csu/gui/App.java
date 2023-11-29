package csu.gui;

import csu.gui.ui.Gameframe;
import csu.gui.ui.Loginframe;

import javax.swing.*;

public class App extends JFrame {
    public static void main(String[] args)
    {
        new App();
    }

    public App()
    {
        initframe();
        this.setVisible(true);
    }

    private void initframe()
    {
        this.setTitle("九宫格拼图");
        this.setSize(400,200);
        this.setLocationRelativeTo(null);//居中
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);//取消内部默认布局

        JButton starbutton=new JButton("开始游戏(游客)");
        JButton loginbutton=new JButton("登陆或注册");

        starbutton.setBounds(30,40,150,80);
        loginbutton.setBounds(220,40,150,80);

        this.getContentPane().add(starbutton);
        this.getContentPane().add(loginbutton);

        starbutton.addActionListener(e->opengameframe());
        loginbutton.addActionListener(e->openloginframe());
    }

    public void opengameframe()
    {
        this.setVisible(false);
        new Gameframe(null);
    }
    public void openloginframe()
    {
        this.setVisible(false);
        new Loginframe();
    }


}
