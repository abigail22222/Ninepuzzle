package csu.gui.ui;

import csu.gui.domain.Gameinfo;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class SaveFileReader {


    public void createAndShowUI() {
        JFrame frame = new JFrame("排行榜");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JButton loadButton = new JButton("点击查看");
        loadButton.addActionListener(e -> loadData(textArea));

        frame.getContentPane().add(loadButton, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void loadData(JTextArea textArea) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("puzzle\\save\\save.txt"))) {
            // 从文件中读取ArrayList<Gameinfo>对象
            ArrayList<Gameinfo> gameinfoList = (ArrayList<Gameinfo>) ois.readObject();

            // 在UI中显示读取到的数据
            StringBuilder stringBuilder = new StringBuilder();
            for (Gameinfo gameinfo : gameinfoList) {
                stringBuilder.append(gameinfo.toString()).append("\n");
            }
            textArea.setText(stringBuilder.toString());

        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            textArea.setText("Error loading data.");
        }
    }


}
