package com.tkdz.game;

import com.tkdz.tank.MyTank;
import com.tkdz.tank.tank;
import com.tkdz.util.Constant;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.awt.*;

//子弹类
public class bullet {
    //子弹的默认速度为坦克的2被
    public static final int DEFAULT_SPEED = tank.DEFAULT_SPEED<<1;
    //坦克的炮弹半径
    public static final int REDIUS = 4;
    private int x,y;
    private int speed = DEFAULT_SPEED;
    private int dir;
    private int atk;
    private Color color;
    //子弹是否可见
    private boolean visible = true;//控制子弹是否飞出屏幕
    public boolean isVisible(){
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public bullet(int x, int y, int dir, int atk, Color color) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.atk = atk;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    //给对象池使用，所有的数据都是默认值
    public bullet() {
    }

    //炮弹的自身的绘制方法
    public void draw(Graphics g)
    {
        if(!visible) return;
        logic();
        g.setColor(color);
        g.fillOval(x-REDIUS,y-REDIUS,REDIUS<<1,REDIUS<<1);
    }
    //子弹的逻辑
    private void logic(){
        move();
    }

    private void move()
    {
        switch (dir){
            case tank.DIR_UP:
                y -= speed;
                if(y <=0) visible = false;
                break;
            case tank.DIR_DOWN:
                y+=speed;
                if(y> Constant.FRAME_HEIGHT)
                {
                    visible = false;
                }
                break;
            case tank.DIR_LEFT:
                x-=speed;
                if(x<0)
                {
                    visible = false;
                }
                break;
            case tank.DIR_RIGHT:
                x+=speed;
                if(x>Constant.FRAME_WIDTH) visible = false;
                break;
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

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

}
