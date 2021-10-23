package com.tkdz.game;

import com.tkdz.util.MyUtil;

import javax.tools.Tool;
import java.awt.*;

//用来控制爆炸效果
public class Explode {
    public static final int EXPLODE_FRAME_COUNT = 12;//爆炸帧的个数,数字越大越快
    //导入资源
    private static Image[] img;//使用static不会导致多次导入
    //爆炸效果的图片的宽度和高度
    private static int explodeWidth;
    private static int explodeHeight;
    static{
        img = new Image[EXPLODE_FRAME_COUNT/3];//除3和12是对应配套的
        for (int i = 0; i < img.length; i++) {
            img[i] = MyUtil.clearImage("res/boom_"+i+".png");
        }
        explodeWidth = img[0].getWidth(null)/2;
        explodeHeight = img[0].getHeight(null);
    }

    public Explode() {
        index = 0;
    }

    //爆炸效果的属性
    private int x,y;
    //当前播放的帧的下标 0-3（有渐变效果）
    private int index;
    //是否可见
    private boolean visible = true;

    public void draw(Graphics g){
        //对爆炸效果图片的宽高的确定
        if(explodeHeight<=0)
        {
            explodeHeight = img[0].getHeight(null);
            explodeWidth = img[0].getWidth(null)>>1;
        }
        if(!visible) return ;
        g.drawImage(img[index/3],x-explodeWidth,y-explodeHeight,null);//和上面的爆炸帧是配套的，原来是4所以不用除
        index++;
        //播放完最后一帧设置为不可见(使用visible的变量的属性）
        if(index>=EXPLODE_FRAME_COUNT){
            visible = false;
        }
    }

    public Explode(int x, int y) {
        this.x = x;
        this.y = y;
        index = 0;
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "Explode{" +
                "x=" + x +
                ", y=" + y +
                ", index=" + index +
                ", visible=" + visible +
                '}';
    }
}
