package com.tkdz.util;

import com.sun.xml.internal.ws.encoding.MtomCodec;

import java.awt.*;

//工具类
public class MyUtil {
    private MyUtil(){}
    //得到指定区间的随机数
    public static final int getRandomNumber(int min,int max){
        return (int) (Math.random()*(max-min)+min);
    }
    //得到随机的颜色
    public static final Color getRandomColor(){
        int red = getRandomNumber(0,256);
        int blue = getRandomNumber(0,256);
        int green = getRandomNumber(0,256);
        return new Color(red,green,blue);
    }
    //判断是否会碰撞(一个点和一个正方形）
    //参数为正方向地中心点的横纵坐标，点的坐标和正方形一边的一半
    //如果点在正方形内部，返回true，否则返回false
    public static final  boolean isCollide(int rectX,int rectY,int radius,int  pointX,int pointY){
        int disX = Math.abs(rectX-pointX);
        int disY= Math.abs(rectY-pointY);
        if(disX<radius&&disY<radius) return true;
        return false;
    }
    //根据图片的资源路径创建加载图片对象
    public static final Image clearImage(String path){
        return Toolkit.getDefaultToolkit().createImage(path);
    }
    private static final String[] NAMES = {
            "行人","乐园","花草","树木","行人","乐园","花草","树木",
            "画家","沙子","人才","水珠","路灯","绿豆","狐狸","狗熊",
            "大象","老虎","斑马","考拉","袋鼠","野牛","山羊","刺猬",
            "海牛","蝙蝠","黄牛"
    };
    public static final String[] MODIFY = {
            "可爱","聪明","美丽","淘气","懂事","乖巧","狡猾","呆呆","天真"
    };
    //得到一个随机名字
    public static final String getRandomName(){
        return MODIFY[getRandomNumber(0,MODIFY.length)]+"的"+
                NAMES[getRandomNumber(0,NAMES.length)];
    }

}
