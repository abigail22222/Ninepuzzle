package csu.gui;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import java.util.*;

public class Gameframe extends JFrame implements KeyListener {

    //记录状态
    private Stack<int[][]> gameStateStack;
    private int undoCount; // 用于记录悔步次数  lastMoveDirection 变量记录了用户上一次的移动方向。在用户按下上下左右键时，检查当前方向与上一次方向的关系，如果不符合规定的移动序列，就不执行移动操作。这样，你就可以防止用户通过直接按上下左右键来绕过悔棋限制。
    private int lastMoveDirection = -1; // -1 表示初始值，没有上一次的移动方向

    //成员变量，储存图片的4*4棋盘
    int[][]data=new int[4][4];
    //x：行  y:列
    int x,y;

    private JLabel timeLabel;  // 用于显示时间
    private long startTime;    // 游戏开始时间


    //表示游戏主界面
    public Gameframe()
    {
        // 初始化数据（打乱）
        initdata();
        // 初始化界面 标题，宽高，位置布局，关闭方式
        initGframe();
        // 初始化菜单栏
        initGmenubar();
        // 启动计时器
        startTimer();
        // 载入图片
        initphotos();

        // 显示
        this.setVisible(true);
    }


    private void initdata() {

        //打乱
        int[]temparr={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        Random r=new Random();
        for (int i = 0; i < temparr.length; i++) {
            int index=r.nextInt(temparr.length);
            int temp=temparr[i];
            temparr[i]=temparr[index];
            temparr[index]=temp;
        }
        //放到4*4棋盘中
        for (int i = 0; i < temparr.length; i++) {
            //获取0在棋盘中的位置
            if(temparr[i]==0)
            {
                x=i/4;
                y=i%4;
            }
            data[i/4][i%4]=temparr[i];
        }

        // 初始化游戏状态栈
        gameStateStack = new Stack<>();
        gameStateStack.push(copyArray(data));

    }

    // 复制二维数组
    private int[][] copyArray(int[][] source) {
        int[][] destination = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            destination[i] = Arrays.copyOf(source[i], source[i].length);
        }
        return destination;
    }

    private void initphotos() {
        //每次移动图片时需要把原先的图片全部删除
        this.getContentPane().removeAll();
        // 更新游戏状态栈
        gameStateStack.push(copyArray(data));


        for (int k = 0; k < 4; k++) {
            for (int i = 0; i < 4; i++) {
                int n=data[k][i];
                JLabel jLabel=new JLabel(new ImageIcon("puzzle\\image\\animal\\animal1\\"+n+".jpg"));//根据棋盘位置加载对应图片
                jLabel.setBounds(105*i+93,105*k+69,105,105);
                //设置边框
                jLabel.setBorder(new BevelBorder(1));
                //先获取隐藏的容器
                this.getContentPane().add(jLabel);

            }
        }

        //添加背景图片
        JLabel background=new JLabel(new ImageIcon("puzzle\\image\\bg.png"));
        background.setBounds(-76,0,641,633);
        this.getContentPane().add(background);



        //刷新一下界面
        this.getContentPane().repaint();

    }

    private void initGmenubar() {

        //initGmenubar
        //创建大框
        JMenuBar jMenuBar=new JMenuBar();
        //创建大框中的选项
        JMenu functionmenu=new JMenu("更多功能");
        JMenu aboutmenu=new JMenu("关于我们");
        //创建选项的下拉选项
        JMenuItem replayitem = new JMenuItem("重新游戏");
        JMenuItem reloginitem=new JMenuItem("重新登陆");
        JMenuItem closegameitem=new JMenuItem("关闭游戏");
        JMenuItem selectregistrationitem = new JMenuItem("查找用户");

        JMenuItem accountitem=new JMenuItem("联系作者");
        //
        //
        // 添加
        functionmenu.add(replayitem);
        functionmenu.add(reloginitem);
        functionmenu.add(closegameitem);
        functionmenu.add(selectregistrationitem);

        aboutmenu.add(accountitem);

        jMenuBar.add(functionmenu);
        jMenuBar.add(aboutmenu);

        this.setJMenuBar(jMenuBar);
    }

    private void initGframe() {
        //设置界面标题
        this.setTitle("WJSN");
        //宽高
        this.setSize(600,585);
        //界面居中
        this.setLocationRelativeTo(null);
        //关闭方式
        this.setDefaultCloseOperation(WindowConstants. DISPOSE_ON_CLOSE);
        //取消默认布局居中,里面的图片位置才会根据自己定的坐标位置显示
        this.setLayout(null);
        //添加键盘监听
        this.addKeyListener(this);



    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateElapsedTime();
            }
        });
        timer.start();
    }
    private void updateElapsedTime() {
        long currentTime = System.currentTimeMillis();
        long elapsedTimeInSeconds = (currentTime - startTime) / 1000;
        displayElapsedTime(elapsedTimeInSeconds);
    }

    private void displayElapsedTime(long elapsedTimeInSeconds) {
        if (timeLabel == null) {
            timeLabel = new JLabel("Time: " + elapsedTimeInSeconds + "s");
            timeLabel.setBounds(5, 10, 100, 20);
            this.getContentPane().add(timeLabel);
        }

        timeLabel.setText("Time: " + elapsedTimeInSeconds + "s");

        // 将timeLabel移到顶层
        setComponentZOrder(timeLabel, 0);

        getContentPane().repaint();
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        //👈37 👆38 👉39 👇40
        int theKey=e.getKeyCode();
        System.out.println(theKey);
        if(theKey==37)
        {
            if (lastMoveDirection != 39) { // 不是上一次右移
                if(y==3)
                    return;;
                data[x][y]=data[x][y+1];
                data[x][y+1]=0;
                y++;
                initphotos();
                lastMoveDirection = 37; // 更新上一次的移动方向
            }
        } else if (theKey==38) {
            if (lastMoveDirection != 40) { // 不是上一次下移
                if(x==3)
                    return;;
                data[x][y]=data[x+1][y];
                data[x+1][y]=0;
                x++;
                initphotos();
                lastMoveDirection = 38; // 更新上一次的移动方向
            }

        } else if (theKey==39) {
            if (lastMoveDirection != 37) { // 不是上一次左移
                if (y==0)
                    return;;
                data[x][y]=data[x][y-1];
                data[x][y-1]=0;
                y--;
                initphotos();
                lastMoveDirection = 39; // 更新上一次的移动方向
            }

        } else if (theKey==40) {
            if (lastMoveDirection != 38) { // 不是上一次上移
                if (x==0)
                    return;
                data[x][y]=data[x-1][y];
                data[x-1][y]=0;
                x--;
                initphotos();
                lastMoveDirection = 40; // 更新上一次的移动方向
            }

        }else if (theKey == KeyEvent.VK_BACK_SPACE) {
            // 悔棋操作
            undoMove();
            lastMoveDirection = -1; // 重置上一次的移动方向
        } else if (theKey==65) {//A刷新界面
            initdata();
            initphotos();

        }
    }

    //<editor-fold desc="悔棋的实现">
    // 悔棋
    private void undoMove() {
        if (undoCount < 1 && gameStateStack.size() > 1) {
            // 移除当前状态
            gameStateStack.pop();
            // 获取上一个状态
            int[][] previousState = gameStateStack.peek();
            x = findZeroX(previousState);
            y = findZeroY(previousState);
            data = previousState;
            initphotos();

            // 增加悔棋步数计数
            undoCount++;
        } else {
            new Undocant2frame();
        }
    }

    // 找到数组中0的横坐标
    private int findZeroX(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == 0) {
                    return i;
                }
            }
        }
        return -1; // 未找到
    }

    // 找到数组中0的纵坐标
    private int findZeroY(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == 0) {
                    return j;
                }
            }
        }
        return -1; // 未找到
    }


    //</editor-fold>
}
