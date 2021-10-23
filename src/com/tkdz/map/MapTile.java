package com.tkdz.map;

import com.sun.org.apache.bcel.internal.Const;
import com.tkdz.game.bullet;
import com.tkdz.util.BulletsPool;
import com.tkdz.util.Constant;
import com.tkdz.util.MyUtil;
import java.util.List;

import java.awt.*;

//地图元素块
public class MapTile {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_HOUSE = 1;
    public static final int TYPE_COVER = 2;
    public static final int TYPE_HARD = 3;
    public static int tileW = 40;
    public static int radius = tileW >> 1;
    private int type = TYPE_NORMAL;
    private static Image[] tileImg;
    static {
        tileImg = new Image[4];
        tileImg[TYPE_NORMAL] = MyUtil.clearImage("res/tile.png");
        tileImg[TYPE_HOUSE] = MyUtil.clearImage("res/house.png");
        tileImg[TYPE_COVER] = MyUtil.clearImage("res/cover.png");
        tileImg[TYPE_HARD] = MyUtil.clearImage("res/hard.png");
        if (tileW <= 0) {
            tileW = tileImg[TYPE_NORMAL].getWidth(null);
        }
    }

    //图片资源的左上角坐标
    private int x, y;
    private boolean visible = true;

    public boolean isVisible(){
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public MapTile(int x, int y) {
        this.x = x;
        this.y = y;

    }

    public void draw(Graphics g) {
        if(!visible)
            return;
        if (tileW <= 0) {
            tileW = tileImg[TYPE_NORMAL].getWidth(null);
        }
        //根据不同的类型绘制不同的图片
        g.drawImage(tileImg[type], x, y, null);

    }

    public MapTile() {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    //地图块和若干个子弹是否有碰撞
    public boolean isCollideBullet(List<bullet> bullets) {
        if(!visible||type == TYPE_COVER){//不可见或者块为遮挡类型
            return false;
        }
        for (bullet b : bullets) {
            int bulletX = b.getX();
            int bulletY = b.getY();//这些get方法是对象的，不是类的
            boolean collide= MyUtil.isCollide(x + radius, y + radius, radius, bulletX, bulletY);
            if(collide){
                //子弹销毁
                b.setVisible(false);
                BulletsPool.theReturn(b);
                return true;
            }
        }
        return false;
    }
    //判断当前的地图块是否是老巢
    public boolean isHouse(){
        return type==TYPE_HOUSE;
    }
}
