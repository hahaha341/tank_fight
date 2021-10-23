package com.tkdz.tank;

import com.tkdz.util.MyUtil;

import java.awt.*;
//自己的坦克类
public class MyTank extends tank{
    public MyTank(int x, int y, int dir) {
        super(x, y, dir);
    }

    private static Image[] tankImg;
    static{
        tankImg = new Image[4];
        tankImg[0] = MyUtil.clearImage("res/u.png");
        tankImg[1] = MyUtil.clearImage("res/d.png");
        tankImg[2] = MyUtil.clearImage("res/l.png");
        tankImg[3] = MyUtil.clearImage("res/r.png");
    }
    public void drawImgTank(Graphics g){
        g.drawImage(tankImg[getDir()],getX()-REDIUS,getY()-REDIUS,null );
    }
}
