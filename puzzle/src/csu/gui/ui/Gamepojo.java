package csu.gui.ui;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Gamepojo implements Runnable {
    private static JLabel[][] sectionImg;
    private static int[][] map;


    public static int[][] getMap() {
        return map;
    }

    private int [] disrupt;//打乱数组

    public int[] getDisrupt() {
        return disrupt;
    }

    public void setDisrupt(int[] disrupt) {
        this.disrupt = disrupt;
    }

    public  JLabel[][] getSectionImg() {
        return sectionImg;
    }

    public  void setSectionImg(JLabel[][] sectionImg) {
        Gamepojo.sectionImg = sectionImg;
    }

    public Gamepojo(int length)//length就是等级
    {
        //sectionImg初始化
        sectionImg=new JLabel[length][length];
        //map初始化
        map=new int[length][length];

        //disrupt初始化
        disrupt=new int[length*length];
        for(int i=0;i<length;i++){
            for(int j=0;j<length;j++){
                map[i][j]=i*length+j;
                disrupt[i*length+j]=i*length+j;
            }
        }

    }




    //获取移动前坐标和移动后坐标
    public static void gain(int[] arr,int length) throws InterruptedException {
        CountDownLatch latch=new CountDownLatch(arr.length);
        for(int i=0;i< arr.length;i++){
            //移动前二维数组索引位
            int row1=arr[i]/length;
            int col1=arr[i]%length;
            //移动后二维数组索引位
            int row2=i/length;
            int col2=i%length;
            //开启切片动态移动线程
            MySection mySection=new MySection(row1,col1,col2,row2,sectionImg,latch);
            new Thread(mySection).start();
            //一维映射二维
            map[row2][col2]=arr[i];
            System.out.print(map[row2][col2]+"|");
        }
        System.out.println(" ");
        latch.await();

        //交换有问题
        JLabel[][]sectiontem=new JLabel[map.length][map.length];//暂存数组
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map.length;j++){
                int row=map[i][j]/map.length;
                int col=map[i][j]%map.length;
                //图片数组交换
                sectiontem[i][j]= sectionImg[row][col];
            }
        }
        sectionImg=sectiontem;//换回来
        //text();
    }





    @Override
    public void run() {
        //先等会
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int length= map.length;
        try {
            gain(disrupt,length);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
