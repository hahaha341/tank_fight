package com.tkdz.tank;

import com.tkdz.game.GameFrame;
import com.tkdz.game.LevelInfo;
import com.tkdz.util.Constant;
import com.tkdz.util.EnemyTanksPool;
import com.tkdz.util.MyUtil;
import org.omg.CORBA.PRIVATE_MEMBER;


import java.awt.*;
//敌人的坦克类
public class EnemyTank extends tank{
        public static final int TYPE_GREEN=0;
        public static final int TYPE_YELLOW=1;
        private int type = TYPE_GREEN;
    public EnemyTank(int x, int y, int dir) {

        super(x, y, dir);
        //敌人一旦创建就计时
        aiTime = System.currentTimeMillis();
        type = MyUtil.getRandomNumber(0,2);
    }
    public EnemyTank(){
        type = MyUtil.getRandomNumber(0,2);
        aiTime = System.currentTimeMillis();
    }

    private static Image[] greenImg;
    private static Image[] yellowImg;
    //记录5秒开始的时间
    private long aiTime;
    static{
        greenImg = new Image[4];
        greenImg[0] = MyUtil.clearImage("res/ul.png");
        greenImg[1] = MyUtil.clearImage("res/dl.png");
        greenImg[2] = MyUtil.clearImage("res/ll.png");
        greenImg[3] = MyUtil.clearImage("res/rl.png");
        yellowImg = new Image[4];
        yellowImg[0] = MyUtil.clearImage("res/u.png");
        yellowImg[1] = MyUtil.clearImage("res/d.png");
        yellowImg[2] = MyUtil.clearImage("res/l.png");
        yellowImg[3] = MyUtil.clearImage("res/r.png");
    }
    public void drawImgTank(Graphics g)
    {
        g.drawImage(type == TYPE_GREEN?greenImg[getDir()]:yellowImg[getDir()],
                getX()-REDIUS, getY()-REDIUS,null );
        ai();
    }
    public static tank createEnemy(){
        int x = MyUtil.getRandomNumber(0,2)==0?REDIUS+6:
                Constant.FRAME_WIDTH-REDIUS-6;
        int y = GameFrame.titleBarH+REDIUS;
        int dir = DIR_DOWN;
//        tank enemy = new EnemyTank(x,y,dir);,因为使用了坦克池,之前在这个位置上标记了todo所以能够很快找到
        tank enemy = (EnemyTank)EnemyTanksPool.get();
        enemy.setX(x);
        enemy.setY(y);
        enemy.setDir(dir);
        enemy.setEnemy(true) ;//因为要创造敌人，所以敌人项要对应设置
        enemy.setState(STATE_MOVE);
        //根据游戏的难度设置敌人血量
        int maxHp=tank.DEFAULT_HP*LevelInfo.getInstance().getLevelType();
        enemy.setHp(maxHp);//从对象池中取出一个，要设置为初始值
        enemy.setMaxHP(maxHp);
        //通过关卡信息中的敌人类型，设置当前出生的敌人的类型
        int enemyType = LevelInfo.getInstance().getRandomEnemyType();
        ((EnemyTank) enemy).setType(enemyType);//todo
        return enemy;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    //敌人的ai
    private void ai(){
        if(System.currentTimeMillis() - aiTime > Constant.ENEMY_AI_INTERVAL){
            //间隔5秒随机状态,0站立，2move
            setDir(MyUtil.getRandomNumber(DIR_UP,DIR_RIGHT+1));
            setState(MyUtil.getRandomNumber(0,2)==0?STATE_STAND:STATE_MOVE);
            aiTime = System.currentTimeMillis();
        }
        //比较小的概率去开火
        if(Math.random()<Constant.ENEMY_FIRE_PERCENT){
            fire();
        }
    }
}