package com.tkdz.util;
import com.tkdz.game.bullet;

import java.util.ArrayList;
import java.util.List;

//子弹对象池类
public class BulletsPool {
    public static final int DEFAULT_POOL_SIZE = 200;
    public static final int POOL_MAX_SIZE = 300;
    //用于保存所有的子弹的容器
    private static List<bullet> pool = new ArrayList<>();
    //在类加载的时候创建200个子弹对象添加到容器中
    static{
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new bullet());
        }
    }
    //从池塘中获得一个子弹对象
    public static bullet get(){
        bullet b1=null;
        //池塘被掏空了
        if(pool.size()== 0){
            b1 = new bullet();
        }
        else//池塘中还有子弹对象,拿走第一个位置的子弹对象
        {
            b1 = pool.remove(0);
        }
        return b1;
    }
     //子弹被销毁的时候，归还到池塘中
    public static void theReturn(bullet b2){
        //池塘中子弹的个数已经到达了最大值，不再归还
        if(pool.size() == POOL_MAX_SIZE) return;
        else//否则就归还
        pool.add(b2);
    }
}
