package csu.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameVictoryScreen {


    private long finalScore;

    public GameVictoryScreen(long finalScore)
    {
        this.finalScore=finalScore;
    }

    public void settleScore() {
        SwingUtilities.invokeLater(() -> {
            GameVictoryScreen game = new GameVictoryScreen(finalScore);
            game.showVictoryScreen();
        });
    }

    private void showVictoryScreen() {

        // 创建显示分数的 UI 界面
        JFrame frame = new JFrame("游戏胜利 ✌");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        // 添加一个标签显示分数
        JLabel scoreLabel = new JLabel("WIN！！Your Final Score: " + finalScore);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.getContentPane().add(scoreLabel, BorderLayout.CENTER);

        // 添加一个按钮用于关闭界面
        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // 关闭窗口
            }
        });
        frame.getContentPane().add(closeButton, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


}
