package csu.gui.ui;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CountDownLatch;

public class MySection implements Runnable{
    //移动前索引
    private int row1;
    private int col1;
    //移动后索引
    private int col2;
    private int row2;
    //切片的二维数组
    private JLabel[][] sectionimg;
    //坐标点位
    private Point end;
    private Point now;
    //控制线程
    private CountDownLatch latch;
    //判断线程睡眠时间
    private int judge;
    public MySection(int row1, int col1, int col2, int row2, JLabel[][] sectionimg, CountDownLatch latch) {
        this.row1 = row1;
        this.col1 = col1;
        this.col2 = col2;
        this.row2 = row2;
        this.sectionimg = sectionimg;
        end=new Point(sectionimg[row2][col2].getLocation());
        now=new Point(sectionimg[row1][col1].getLocation());
        this.latch=latch;
        judge=0;
    }

    public void setJudge(int judge) {
        this.judge = judge;
    }
    //动态过程
    @Override
    public void run() {
        while (now.x != end.x || now.y != end.y) {
            if (now.x > end.x) {
                now.x--;
            } else if (now.x < end.x) {
                now.x++;
            }
            if (now.y > end.y) {
                now.y--;
            } else if (now.y < end.y) {
                now.y++;
            }
            sectionimg[row1][col1].setLocation(now);
            // 显示过程
            if (judge==0) {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                 //System.out.println(now.x+" "+now.y);//测试代码
            }
        }
        latch.countDown();
    }
}

