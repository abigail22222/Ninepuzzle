package csu.gui;

import javax.swing.*;

public class Manualframe extends JFrame {
    public Manualframe()
    {
        initFrame();
        initmanualphoto();
        this.setVisible(true);
    }
    private void initFrame() {
        //对自己的界面做一些设置。
        //设置宽高
        this.setSize(600,585);
        //设置标题
        this.setTitle("拼图游戏 V1.0游戏规则");
        //取消内部默认布局
        this.setLayout(null);
    }

    private void initmanualphoto()
    {
        JLabel jLabel=new JLabel(new ImageIcon("puzzle\\image\\manual.png"));
        jLabel.setBounds(-76,0,641,633);
        this.getContentPane().add(jLabel);
    }
}
