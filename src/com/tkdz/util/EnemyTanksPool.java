package com.tkdz.util;

import com.tkdz.game.bullet;
import com.tkdz.tank.EnemyTank;
import com.tkdz.tank.tank;

import java.util.ArrayList;
import java.util.List;
//敌人坦克对象池
public class EnemyTanksPool {
    public static final int DEFAULT_POOL_SIZE = 20;
    public static final int POOL_MAX_SIZE = 20;
    private static List<tank> pool = new ArrayList<>();
    static{
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new EnemyTank());
        }
    }
    //从池塘中获得一个子弹对象
    public static tank get(){
        tank t1=null;
        //池塘被掏空了
        if(pool.size()== 0){
            t1 = new EnemyTank();
        }
        else//池塘中还有子弹对象,拿走第一个位置的子弹对象
        {
            t1 = pool.remove(0);
        }
        return t1;
    }
    //子弹被销毁的时候，归还到池塘中
    public static void theReturn(tank b2){
        //池塘中子弹的个数已经到达了最大值，不再归还
        if(pool.size() == POOL_MAX_SIZE) return;
        else//否则就归还
            pool.add(b2);
    }
}
