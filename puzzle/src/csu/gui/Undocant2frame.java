package csu.gui;

import javax.swing.*;

public class Undocant2frame extends JFrame {
    public Undocant2frame()
    {
        initframe();
        inittext();
        //显示
        this.setVisible(true);
    }
    private void initframe()
    {
        this.setTitle("警告！");
        this.setSize(500,100);
        //界面居中
        this.setLocationRelativeTo(null);
        //关闭方式
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }
    private void inittext()
    {
        JLabel jLabel=new JLabel("无法悔棋，已经回到初始状态或已经悔棋两步！");
        //先获取隐藏的容器
        this.getContentPane().add(jLabel);
    }
}
