package csu.gui;

import java.util.concurrent.CountDownLatch;

public class MySection implements Runnable {
    private int row1, col1, row2, col2;
    private int[][] sectionImg;
    private CountDownLatch latch;

    public MySection(int row1, int col1, int row2, int col2, int[][] sectionImg, CountDownLatch latch) {
        this.row1 = row1;
        this.col1 = col1;
        this.row2 = row2;
        this.col2 = col2;
        this.sectionImg = sectionImg;
        this.latch = latch;
    }

    @Override
    public void run() {
        // 在这里实现坐标的交换逻辑
        // 你可以根据需要修改这部分代码
        int temp = sectionImg[row1][col1];
        sectionImg[row1][col1] = sectionImg[row2][col2];
        sectionImg[row2][col2] = temp;

//        // 延时，单位为毫秒
//        try {
//            Thread.sleep(500);  // 500毫秒，即0.5秒
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        latch.countDown();  // 减少计数器
    }
}

