package com.tkdz.tank;

import com.tkdz.game.Explode;
import com.tkdz.game.GameFrame;
import com.tkdz.game.LevelInfo;
import com.tkdz.game.bullet;
import com.tkdz.map.MapTile;
import com.tkdz.map.MapTilePool;
import com.tkdz.util.*;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//坦克类
public abstract class tank {
    public static final int ATK_MAX = 25;
    public static final int ATK_MIN = 15;
    private String name;
    //四个方向
    public static final int DIR_UP = 0;
    public static final int DIR_DOWN = 1;
    public static final int DIR_LEFT = 2;
    public static final int DIR_RIGHT = 3;
    //坦克的半径
    public static final int REDIUS = 20;
    //默认速度 每帧的速度
    public static final int DEFAULT_SPEED = 5;
    //坦克的状态
    public static final int STATE_STAND = 0;
    public static final int STATE_MOVE = 1;
    public static final int STATE_DIE = 2;
    private int x,y;
    private int hp = DEFAULT_HP;
    private int atk;//攻击力
    private int speed = DEFAULT_SPEED;
    private int dir;//坦克的方向
    private int state = STATE_STAND;
    private Color color;
    private boolean isEnemy = false;
    //坦克的初始生命
    public static final int DEFAULT_HP = 100;
    private int maxHP=DEFAULT_HP;
    //炮弹
    private List<bullet> bullets = new ArrayList();//管理炮弹的容器
    //使用容器保存当前坦克上所有的爆炸效果
    private List<Explode> explodes = new ArrayList<>();

    private BloodBar bar = new BloodBar();
    public boolean isEnemy() {
        return isEnemy;
    }

    public void setEnemy(boolean enemy) {
        isEnemy = enemy;
    }

    public tank(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        initTank();
    }
    public tank() {
        initTank();
    }
    private void initTank(){
        color = MyUtil.getRandomColor();
        name = MyUtil.getRandomName();
        atk = MyUtil.getRandomNumber(ATK_MIN,ATK_MAX);
    }

    //绘制坦克
    public void draw(Graphics g){
        logic();//希望坦克的逻辑函数每一帧都要调用
        drawImgTank(g);
        drawbullets(g);
        drawName(g);
        bar.draw(g);
    }
    private void drawName(Graphics g){
        g.setColor(color);
        g.setFont(Constant.SMALL_FONT);
        g.drawString(name,x-REDIUS,y-40);
    }
    //使用图片的方式绘制坦克
    public abstract void drawImgTank(Graphics g);/*{
        if(isEnemy)
        {
        g.drawImage(enemyImg[dir],x-REDIUS,y-REDIUS,null );
        }
        else
        g.drawImage(tankImg[dir],x-REDIUS,y-REDIUS,null );
    }*/
    //使用系统的方式绘制坦克
    private void drawTank(Graphics g)
    {
        g.setColor(color);//设置画笔的颜色
        //绘制坦克的圆
        g.fillOval(x-REDIUS,y-REDIUS,REDIUS<<1,REDIUS<<1);
        //g.drawImage(tankImg[dir],x-REDIUS,y-REDIUS,null );
        int endX = x;
        int endY = y;
        switch (dir){
            case DIR_UP:
                //g.drawImage(tankImg[dir+1],x-REDIUS,y-REDIUS,null );
                endY = y-REDIUS*2;
                g.drawLine(x-1,y,endX-1,endY);//这样处理只用变换x和y中的一项
                g.drawLine(x+1,y,endX+1,endY);
                break;
            case DIR_DOWN:
                //g.drawImage(tankImg[dir],x-REDIUS,y-REDIUS,null );
                endY = y+REDIUS*2;
                g.drawLine(x-1,y,endX-1,endY);
                g.drawLine(x+1,y,endX+1,endY);
                break;
            case DIR_LEFT:
                //g.drawImage(tankImg[dir],x-REDIUS,y-REDIUS,null );
                endX = x-REDIUS*2;
                g.drawLine(x,y-1,endX,endY-1);
                g.drawLine(x,y+1,endX,endY+1);
                break;
            case DIR_RIGHT:
                //g.drawImage(tankImg[dir],x-REDIUS,y-REDIUS,null );
                endX = x+REDIUS*2;
                g.drawLine(x,y-1,endX,endY-1);
                g.drawLine(x,y+1,endX,endY+1);
                break;
        }
        //加粗
        //g.drawLine(x,y,endX,endY);
    }
    //坦克和敌人的子弹的碰撞的方法
    public void collideBullets(List<bullet> bullets)
    {//很多个点和坦克进行碰撞,遍历所有的子弹依次和当前的坦克进行碰撞的检测
        for(bullet bullet1 : bullets){
            int bulletX = bullet1.getX();
            int bulletY = bullet1.getY();
            //子弹和坦克碰上了
            if(MyUtil.isCollide(x,y,REDIUS,bulletX,bulletY)){
                //子弹消失
                bullet1.setVisible(false);
                //坦克受到伤害
                hurt(bullet1);
                //添加爆炸效果
                addExplode(x,y+REDIUS);
            }
        }
    }
    private void addExplode(int x,int y){
        //添加爆炸效果,以当前被击中的坦克的坐标为参考
        Explode explode = ExplodesPool.get();
        //从对象池中取出一个爆炸效果
        explode.setX(x);
        explode.setY(y);
        explode.setVisible(true);//设置成可见
        explode.setIndex(0);
        explodes.add(explode);
    }
    //坦克收到伤害
    private void hurt(bullet bu){
        int atk =bu.getAtk();
        hp-=atk;
        if(hp<0) {
            hp=0;
            die();
        }
    }


    //坦克死亡需要处理的内容
    private void die(){
        if(isEnemy){
            // 敌人坦克被消灭  归还到对象池
            //而且要从敌人的坦克容器中移除掉
            GameFrame.killEnemyCount++;
            EnemyTanksPool.theReturn(this);
            //本关是否结束
            if(GameFrame.isCrossLevel()){
                if(GameFrame.isLastLevel()){
                    //通关了
                    GameFrame.setGameState(Constant.STATE_WIN);
                }
                else{
                    //todo 进入下一关
                    GameFrame.startCrossLevel();
                }
            }
        }
        else{
            //有戏结束
            delaySecondsToOver(3000);
        }
    }
    //判断当前的坦克是否死亡
    public boolean isDie(){
        return hp<=0;
    }
    //绘制当前坦克上的所有的爆炸的效果
    public void drawExplodes(Graphics g){
        for (Explode explode : explodes) {
            explode.draw(g);
        }
        //将不可见的爆炸效果删除，还回对象池
        for (int i = 0; i < explodes.size(); i++) {
            Explode explode2 = explodes.get(i);
            if(!explode2.isVisible()){
                Explode remove = explodes.remove(i);
                ExplodesPool.theReturn(remove);
                i--;
            }
        }
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List getBullets() {
        return bullets;
    }

    public void setBullets(List bullets) {
        this.bullets = bullets;
    }

    public static int getDirUp() {
        return DIR_UP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //坦克的逻辑处理
    private void logic(){
        switch(state){
            case STATE_STAND:
                break;
            case STATE_MOVE:
                move();
                break;
            case STATE_DIE:
                break;
        }
    }
    private  int oldX = -1,oldY = -1;
    //坦克的移动功能
    private void move()
    {
        oldX = x;
        oldY = y;
        switch (dir){
            case DIR_UP:
                y-=speed;//通过speed变量控制坦克的位置
                if(y < REDIUS + GameFrame.titleBarH)
                {
                    y = REDIUS + GameFrame.titleBarH;
                }
                break;
            case DIR_DOWN:
                y+=speed;
                if(y > Constant.FRAME_HEIGHT-REDIUS-6)
                    y = Constant.FRAME_HEIGHT-REDIUS-6;
                break;
            case DIR_LEFT:
                x-=speed;
                if(x < REDIUS+6)  x=REDIUS+6;
                break;
            case DIR_RIGHT:
                x+=speed;
                if(x>Constant.FRAME_WIDTH-REDIUS-6) x=Constant.FRAME_WIDTH-REDIUS-6;
                break;
        }
    }
    public String toString(){
        return "Tank{" +
                "x=" + x +
                ",y="+y+
                ",hp="+hp+
                ",atk="+atk+
                ",speed="+speed+
                ",dir="+dir+
                ",state="+state+
                '}';
    }
    //上一次开火的时间
    private long fireTime;
    //子弹发射的最小的间隔
    public static final int FIRE_INTERVAL = 500;
    //坦克的功能，坦克开火的方法
    //创建了一个子弹对象，子弹对象的属性信息通过坦克的信息获得
    //然后将创建的子弹添加搭配坦克管理的容器中
    public void fire(){
        if(System.currentTimeMillis()-fireTime > FIRE_INTERVAL){
        int bulletX = x;//子弹的坐标
        int bulletY = y;
        //坦克的方向
        switch (dir){
            case DIR_UP:
                bulletY -= REDIUS;
                break;
            case DIR_DOWN:
                bulletY +=REDIUS;
                break;
            case DIR_LEFT:
                bulletX -=REDIUS;
                break;
            case DIR_RIGHT:
                bulletX +=REDIUS;
                break;
        }
        //从对象池中获取子弹对象
        bullet b = BulletsPool.get();
        //设置子弹的属性
        b.setX(bulletX);
        b.setY(bulletY);
        b.setDir(dir);
        b.setAtk(atk);
        b.setColor(color);
        b.setVisible(true);
        bullets.add(b);
        //发射子弹之后，记录本次的发射时间
            fireTime = System.currentTimeMillis();
            MusicUtil.playBomb();//todo 可删除
        }
    }


    //将当前坦克的发射的所有子弹绘制出来
    //所有的子弹都在容器之中
    private void drawbullets(Graphics g){
        for(bullet b : bullets){
            b.draw(g);
        }
        //遍历所有的子弹，将不可见的子弹移除，并返回对象池
        for (int i = 0; i < bullets.size(); i++) {
            bullet b = bullets.get(i);
            if(!b.isVisible()){
                bullet remove = bullets.remove(i);
                i--;
                BulletsPool.theReturn(remove);//还原到对象池中
            }
        }
    }
    //坦克销毁的时候处理坦克的所有子弹
    public void bulletsReturn(){
        for(bullet b : bullets){
            BulletsPool.theReturn(b);//还原到对象池中
        }
        bullets.clear();//将arraylist清空
    }
    //内部类,来表示坦克的血条
    class BloodBar{
        public static final int BAR_LENGTH = 50;
        public static final int BAR_HEIGHT = 5;

        public void draw(Graphics g){
            //填充底色
            g.setColor(Color.YELLOW);
            g.fillRect(x-REDIUS,y-REDIUS-BAR_HEIGHT*2,BAR_LENGTH,BAR_HEIGHT);
            //红色的当前血量
            g.setColor(Color.RED);
            g.fillRect(x-REDIUS,y-REDIUS-BAR_HEIGHT*2,hp*BAR_LENGTH/maxHP,BAR_HEIGHT);
            //蓝色的框
            g.setColor(Color.white);
            g.drawRect(x-REDIUS,y-REDIUS-BAR_HEIGHT*2,BAR_LENGTH,BAR_HEIGHT);
        }
    }//正好是1000/1000

    //坦克的子弹和地图块的碰撞
    public void bulletsCollideMapTiles(List<MapTile> tiles){
        for (MapTile tile : tiles) {
            if(tile.isCollideBullet(bullets)){
                //添加爆炸效果
                addExplode(tile.getX()+MapTile.radius, tile.getY()+MapTile.tileW);
                //地图水泥块没有击毁的处理
                if(tile.getType()==MapTile.TYPE_HARD) continue;
                //设置地图块销毁
                tile.setVisible(false);
                //归还对象池
                MapTilePool.theReturn(tile);
                //当老巢被击毁之后，一秒钟切换到游戏结束的画面
                if(tile.isHouse()){
                    delaySecondsToOver(3000);
                }
            }
        }
    }
    //延迟若干秒 切换到 游戏结束
    private void delaySecondsToOver(int millisSecond){
        new Thread(){
            public void run(){
                try {
                    Thread.sleep(millisSecond);//因为一个数字是一毫秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                GameFrame.setGameState(Constant.STATE_LOSE);
            }
        }.start();
    }
    //一个地图块和当前坦克碰撞的方法
    //从tile中提取8个点判断 8个点是否有任意一个点和当前的坦克有了碰撞
    //点的顺序从左上角的点开始顺时针遍历
    public boolean isCollideTile(List<MapTile> tiles){
        final int len = 2;
        for (MapTile tile : tiles) {
            if(!tile.isVisible()||tile.getType()==MapTile.TYPE_COVER) continue;
            //点-1 左上角点
            int tileX = tile.getX();
            int tileY = tile.getY();
            boolean collide = MyUtil.isCollide(x, y, REDIUS, tileX, tileY);
            if(collide)  return true;
            //点-2 中上
            tileX +=MapTile.radius;
            collide = MyUtil.isCollide(x, y, REDIUS, tileX, tileY);
            if(collide)  return true;
            //点-3 右上角
            tileX +=MapTile.radius;
            collide = MyUtil.isCollide(x, y, REDIUS, tileX, tileY);
            if(collide)  return true;
            //点-4 右中
            tileY +=MapTile.radius;
            collide = MyUtil.isCollide(x, y, REDIUS, tileX, tileY);
            if(collide)  return true;
            //点-5 右下
            tileY +=MapTile.radius;
            collide = MyUtil.isCollide(x, y, REDIUS, tileX, tileY);
            if(collide)  return true;
            //点-6 中下
            tileX -=MapTile.radius;
            collide = MyUtil.isCollide(x, y, REDIUS, tileX, tileY);
            if(collide)  return true;
            //点-7 左下
            tileX -=MapTile.radius;
            collide = MyUtil.isCollide(x, y, REDIUS, tileX, tileY);
            if(collide)  return true;
            //点-8 左中
            tileY -=MapTile.radius;
            collide = MyUtil.isCollide(x, y, REDIUS, tileX, tileY);
            if(collide)  return true;
        }
            return false;
    }
    //坦克回退的方法
    public void back(){
        x = oldX;
        y= oldY;
        //当发生碰撞时，恢复到碰撞之前的位置
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }
}
