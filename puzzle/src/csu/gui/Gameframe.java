package csu.gui;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import java.util.*;

public class Gameframe extends JFrame implements KeyListener,ActionListener,Level {

    //记录状态
    private Stack<int[][]> gameStateStack;
    private int undoCount; // 用于记录悔步次数  lastMoveDirection 变量记录了用户上一次的移动方向。在用户按下上下左右键时，检查当前方向与上一次方向的关系，如果不符合规定的移动序列，就不执行移动操作。这样，你就可以防止用户通过直接按上下左右键来绕过悔棋限制。
    private int lastMoveDirection = -1; // -1 表示初始值，没有上一次的移动方向

    //成员变量，储存图片的棋盘
    //默认是简单=3
    private int difficultyLevel=Level.easy;  // 表示拼图的难易程度，例如 3 表示 3x3 的拼图

    int[][]data=new int[difficultyLevel][difficultyLevel];
    //x：行  y:列
    int x,y;

    private JLabel timeLabel;  // 用于显示时间
    private long startTime;    // 游戏开始时间

    //定义一个变量，记录当前展示图片的路径
    //默认最开始的图片是animal里简单的第1张
    String inpath="animal";
    int photoindex=1;
    String path = "puzzle\\image\\"+inpath+"\\"+difficultyLevel+"\\"+inpath+photoindex+"\\";


    //创建选项的下拉选项
    JMenuItem level1=new JMenuItem("简单");//3*
    JMenuItem level2=new JMenuItem("普通");//5*
    JMenuItem level3=new JMenuItem("困难");//6*


    JMenuItem animal = new JMenuItem("动物");
    JMenuItem sport = new JMenuItem("运动");

    JMenuItem replayitem = new JMenuItem("重新游戏");
    JMenuItem escitem=new JMenuItem("退出到开始界面");
    JMenuItem stopgameitem=new JMenuItem("结束游戏");
    JMenuItem rangeitem = new JMenuItem("排行榜");

    JMenuItem manualitem=new JMenuItem("游戏说明💻");


    //表示游戏主界面
    public Gameframe()
    {
        // 初始化数据（打乱）
        initdata();

        if(isSolvable(data))
        {
            // 初始化界面 标题，宽高，位置布局，关闭方式
            initGframe();
            // 初始化菜单栏
            initGmenubar();
            // 启动计时器
            startTimer();
            // 载入图片
            initphotos();
        }else{
            while(!isSolvable(data))
            {
                showDialog("此局无解，已为您重新开新的一局，此弹框3秒后关闭");
                initdata();
                this.setVisible(false);
                // 初始化界面 标题，宽高，位置布局，关闭方式
                initGframe();
                // 初始化菜单栏
                initGmenubar();
                // 启动计时器
                startTimer();
                // 载入图片
                initphotos();
            }

        }



        // 显示
        this.setVisible(true);
    }


    private void initdata() {
        //打乱
        if(difficultyLevel==3)
        {
            int[]temparr= new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
            upset(temparr,difficultyLevel);
        } else if (difficultyLevel==5) {
            int[]temparr=new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24};
            upset(temparr,difficultyLevel);

        } else if (difficultyLevel==6) {
            int[]temparr=new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35};
            upset(temparr,difficultyLevel);

        }

        // 初始化游戏状态栈
        gameStateStack = new Stack<>();
        gameStateStack.push(copyArray(data));

    }

    //打乱
    private void upset(int[] temparr, int difficultyLevel) {
        //data数组必须手动改一次，不然只会是最原来的3*3
        data = new int[difficultyLevel][difficultyLevel];

        Random r = new Random();
        for (int i = 0; i < difficultyLevel * difficultyLevel; i++) {
            int index = r.nextInt(difficultyLevel * difficultyLevel);
            int temp = temparr[i];
            temparr[i] = temparr[index];
            temparr[index] = temp;
        }

        for (int i = 0; i < difficultyLevel * difficultyLevel; i++) {
            // 获取0在棋盘中的位置
            if (temparr[i] == 0) {
                x = i / difficultyLevel;
                y = i % difficultyLevel;
            }
            data[i / difficultyLevel][i % difficultyLevel] = temparr[i];
        }
    }


    // 复制二维数组
    private int[][] copyArray(int[][] source) {
        int[][] destination = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            destination[i] = Arrays.copyOf(source[i], source[i].length);
        }
        return destination;
    }


    //<editor-fold desc="判断拼图是否有解">
    /*逆序数是指在一个数组中，某个数字前面的比它大的数字的个数。如果逆序数的总数是偶数，那么这个初始状态是可解的；如果是奇数，那么这个初始状态是不可解的。
    *
逆序数是拼图问题可解性的一个数学性质，其原理可以通过奇偶性的变化来理解。在拼图问题中，我们可以通过数字的交换来改变逆序数的数量。

拼图问题的逆序数定义如下：对于一个 n x n 的拼图，逆序数是指在展开的一维数组中，当前数字前面比它大的数字的个数。

如果我们考虑拼图中的每一次移动，会发现以下规律：

移动空白格，逆序数不变。
移动与空白格交换的数字，逆序数的奇偶性发生改变。
拼图问题可解的条件是初始状态的逆序数为偶数。这是因为每一次移动不改变逆序数的奇偶性，而目标状态的逆序数为零（是一个偶数），因此初始状态逆序数为偶数时，可以通过有限步数的移动达到目标状态。

如果初始状态的逆序数为奇数，那么就无法通过有限步数的移动达到目标状态，因此拼图问题不可解。

这是拼图问题可解性与逆序数之间的关联，利用逆序数的奇偶性可以快速判断一个拼图问题是否有解。
    *
    * */
    // 计算逆序数
    private static int countInversions(int[] puzzle) {
        int inversions = 0;
        for (int i = 0; i < puzzle.length - 1; i++) {
            for (int j = i + 1; j < puzzle.length; j++) {
                if (puzzle[i] > puzzle[j] && puzzle[i] != 0 && puzzle[j] != 0) {
                    inversions++;
                }
            }
        }
        return inversions;
    }
    // 判断是否可解
    public static boolean isSolvable(int[][] puzzle) {
        // 将二维数组转化为一维数组
        int[] flatPuzzle = new int[puzzle.length * puzzle[0].length];
        int k = 0;
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                flatPuzzle[k++] = puzzle[i][j];
            }
        }

        int inversions = countInversions(flatPuzzle);
        // 如果逆序数为偶数，则可解；为奇数，则不可解
        return inversions % 2 == 0;
    }
    //</editor-fold>


    //判断胜利------在每次拼图移动的时候都要用到这个方法
    private boolean isPuzzleSolved() {
        int value = 1;
        for (int i = 0; i < difficultyLevel; i++) {
            for (int j = 0; j < difficultyLevel; j++) {
                if (data[i][j] != value) {
                    return false;  // 如果有任意一块不在正确的位置，返回 false
                }
                value = (value + 1) % (difficultyLevel * difficultyLevel);  // 下一块的期望值
            }
        }
        return true;  // 所有块都在正确的位置，返回 true
    }


    private void initphotos() {
        //每次移动图片时需要把原先的图片全部删除
        this.getContentPane().removeAll();
        // 更新游戏状态栈
        gameStateStack.push(copyArray(data));

        //每当initphoto()时，会new一个Jable显示时间，因为此类构造函数中调用了StartTimer()：startTime = System.currentTimeMillis();：获取当前时间，并将其作为计时的起始时间。
        //timer = new Timer(1000, new ActionListener() {...});：创建计时器，指定触发间隔为1000毫秒，即1秒。使用匿名内部类实现 ActionListener 接口，重写 actionPerformed 方法，以在计时器触发时执行特定的操作。
        //timer.start();：启动计时器，使其开始计时并触发事件。
        //在这个例子中，每次计时器触发时，它会调用 updateElapsedTime() 方法，该方法会计算经过的时间并更新显示在界面上。这样就实现了实时显示经过的时间的效果。

        //startTimer可以持续显示时间，每次移动拼图时又会initphoto，出现新的时间显示，所以StartTimer的时间是利用的这个容器，不会被遮盖

        //显示时间 在最上层
        //System.out.println("bbb");
        long currentTime = System.currentTimeMillis();
        long elapsedTimeInSeconds = (currentTime - startTime) / 1000;
        timeLabel = new JLabel("Time: " + elapsedTimeInSeconds + "s");
        timeLabel.setBounds(100, 30, 100, 20);
        this.getContentPane().add(timeLabel);


        for (int k = 0; k < difficultyLevel; k++) {
            for (int i = 0; i < difficultyLevel; i++) {
                int n=data[k][i];
                JLabel jLabel=new JLabel(new ImageIcon(path+n+".png"));//根据棋盘位置加载对应图片
                jLabel.setBounds((420/difficultyLevel)*i+93,(420/difficultyLevel)*k+69,(420/difficultyLevel),(420/difficultyLevel));
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

    //<editor-fold desc="游戏界面和菜单初始化">
    private void initGmenubar() {

        //initGmenubar
        //创建大框
        JMenuBar jMenuBar=new JMenuBar();
        //创建大框中的选项
        JMenu functionmenu=new JMenu("更多功能🛠️");
        JMenu chooselevel = new JMenu("选择难度");
        JMenu changeImage = new JMenu("更换图片");
        JMenu helpmenu=new JMenu("帮助📚");



        // 添加
        functionmenu.add(chooselevel);//选择难度
        chooselevel.add(level1);
        chooselevel.add(level2);
        chooselevel.add(level3);

        functionmenu.add(changeImage);//更换图片
        changeImage.add(animal);
        changeImage.add(sport);

        functionmenu.add(replayitem);//重新开始
        functionmenu.add(escitem);//退到登录界面
        functionmenu.add(stopgameitem);//关闭虚拟机
        functionmenu.add(rangeitem);//查看排名

        helpmenu.add(manualitem);//查看游戏说明

        jMenuBar.add(functionmenu);
        jMenuBar.add(helpmenu);

        this.setJMenuBar(jMenuBar);



        //给条目绑定事件
        replayitem.addActionListener(this);
        escitem.addActionListener(this);
        stopgameitem.addActionListener(this);
        rangeitem.addActionListener(this);
        manualitem.addActionListener(this);

        level1.addActionListener(this);
        level2.addActionListener(this);
        level3.addActionListener(this);


        animal.addActionListener(this);
        sport.addActionListener(this);




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
    //</editor-fold>

    //<editor-fold desc="计时功能">
    private void startTimer() {
        startTime = System.currentTimeMillis();//开始计时
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

//        if (timeLabel == null) {
//            timeLabel = new JLabel("Time: " + elapsedTimeInSeconds + "s");
//            timeLabel.setBounds(100, 30, 100, 20);
//            this.getContentPane().add(timeLabel);
//        }

        timeLabel.setText("Time: " + elapsedTimeInSeconds + "s");

    }



    //</editor-fold>


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    //移动拼图的逻辑
    @Override
    public void keyReleased(KeyEvent e) {
        //👈37 👆38 👉39 👇40
        int theKey=e.getKeyCode();
        //System.out.println(theKey);
        if(theKey==37)
        {
            if (lastMoveDirection != 39) { // 不是上一次右移
                if(y==difficultyLevel-1)//空白拼图在边界的情况
                    return;

                data[x][y]=data[x][y+1];
                data[x][y+1]=0;
                y++;
                initphotos();
                lastMoveDirection = 37; // 更新上一次的移动方向
            }else{
                showDialog("悔棋请按backspace，此弹框3秒后自动关闭");
            }
        } else if (theKey==38) {
            if (lastMoveDirection != 40) { // 不是上一次下移
                if(x==difficultyLevel-1)//空白拼图在边界的情况
                    return;;
                data[x][y]=data[x+1][y];
                data[x+1][y]=0;
                x++;
                initphotos();
                lastMoveDirection = 38; // 更新上一次的移动方向
            }else{
                showDialog("悔棋请按backspace，此弹框3秒后自动关闭");
            }

        } else if (theKey==39) {
            if (lastMoveDirection != 37) { // 不是上一次左移
                if (y==0)//空白拼图在边界的情况
                    return;;
                data[x][y]=data[x][y-1];
                data[x][y-1]=0;
                y--;
                initphotos();
                lastMoveDirection = 39; // 更新上一次的移动方向
            }else{
                showDialog("悔棋请按backspace，此弹框3秒后自动关闭");
            }

        } else if (theKey==40) {
            if (lastMoveDirection != 38) { // 不是上一次上移
                if (x==0)//空白拼图在边界的情况
                    return;
                data[x][y]=data[x-1][y];
                data[x-1][y]=0;
                x--;
                initphotos();
                lastMoveDirection = 40; // 更新上一次的移动方向
            }else{
                showDialog("悔棋请按backspace，此弹框3秒后自动关闭");
            }

        }else if (theKey == KeyEvent.VK_BACK_SPACE) {
            // 悔棋操作
            undoMove();
            lastMoveDirection = -1; // 重置上一次的移动方向
        } else if (theKey==65) {//A刷新界面
            initdata();
            while (!isSolvable(data))
            {
                showDialog("此局无解，已为你重新刷新，此弹框3秒后自动关闭");
                initdata();
            }
            initphotos();
        }

        if (isPuzzleSolved()) {
            // 处理玩家胜利的处理逻辑...
            showDialog("恭喜！您获得了胜利，此弹框3秒后自动关闭");
            //记录分数，填进文件。。。文件里进行用户排序
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
    @Override
    public void actionPerformed(ActionEvent e) {
        //获取事件的事件源
        Object obj=e.getSource();
        if(obj==replayitem)
        {
            //replay
            replay();
        } else if (obj==escitem) {
            //esc
            //判断是否胜利，如果胜利则需结算分数，添加记录

            //关闭当前游戏界面
            this.setVisible(false);
            //打开开始界面
            new Loginframe();
        } else if (obj==stopgameitem) {
            //stop
            //关闭虚拟机

            System.exit(0);
        } else if (obj==rangeitem) {
            //range
            //打开记录面板
        } else if (obj==manualitem) {
            //manual
            //打开说明面板
        } else if (obj==animal) {
            //随机选择图片,修改图片路径
            Random random=new Random();
            photoindex= random.nextInt(9)+1;//[1,9]
            path = "puzzle\\image\\"+inpath+"\\"+difficultyLevel+"\\"+inpath+photoindex+"\\";
            //replay
            replay();
        }else if (obj==sport) {
            //随机选择图片,修改图片路径
            Random random=new Random();
            photoindex= random.nextInt(10)+1;//[1,10]
            inpath="sport";
            path = "puzzle\\image\\"+inpath+"\\"+difficultyLevel+"\\"+inpath+photoindex+"\\";
            //replay
            replay();
        }
        else if (obj==level1) {
            difficultyLevel=Level.easy;
            //修改路径
            path = "puzzle\\image\\"+inpath+"\\"+difficultyLevel+"\\"+inpath+photoindex+"\\";
            //replay
            replay();
        } else if (obj==level2) {
            difficultyLevel=Level.normal;
            //修改路径
            path = "puzzle\\image\\"+inpath+"\\"+difficultyLevel+"\\"+inpath+photoindex+"\\";
            //replay
            replay();
        } else if (obj==level3) {
            difficultyLevel=Level.difficult;
            //修改路径
            path = "puzzle\\image\\"+inpath+"\\"+difficultyLevel+"\\"+inpath+photoindex+"\\";
            replay();
        }


    }

    private void replay() {
        //replay
        //重新计算悔棋步数
        undoCount=0;
        //重新打乱图品顺序
        initdata();
        while (!isSolvable(data))
        {
            showDialog("此局无解,已为你重新生成新的一局，此弹框3秒后自动关闭");
            initdata();
        }
        //重新开始计时
        startTimer();
        //重新加载图片
        initphotos();
    }


    //只创建一个弹框对象
    JDialog jDialog = new JDialog();
    // 将 Timer 声明为类级别的变量
    private Timer timer;

    //因为展示弹框的代码，会被运行多次
    //所以，我们把展示弹框的代码，抽取到一个方法中。以后用到的时候，就不需要写了
    //直接调用就可以了。
    public void showDialog(String content){
        if(!jDialog.isVisible()){
            //把弹框中原来的文字给清空掉。
            jDialog.getContentPane().removeAll();
            JLabel jLabel = new JLabel(content);
            jLabel.setBounds(0,0,200,150);
            jDialog.add(jLabel);
            //给弹框设置大小
            jDialog.setSize(200, 150);
            //要把弹框在设置为顶层 -- 置顶效果
            jDialog.setAlwaysOnTop(true);
            //要让jDialog居中
            jDialog.setLocationRelativeTo(null);
            //让弹框
            jDialog.setModal(false);
            //让jDialog显示出来
            jDialog.setVisible(true);
            // 如果定时器尚未运行，则设置定时器
            if (timer == null || !timer.isRunning()) {
                // 创建一个新的 Timer
                timer = new Timer(3000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                //System.out.println("定时器到期");
                                jDialog.setVisible(false);
                            }
                        });
                    }
                });
                timer.setRepeats(false);  // 设置为不重复触发
                timer.start();
            }
        }
    }
}