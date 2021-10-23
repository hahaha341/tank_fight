package com.tkdz.util;

import com.tkdz.game.Explode;
import com.tkdz.game.bullet;

import java.util.ArrayList;
import java.util.List;
//爆炸池
public class ExplodesPool {

    public static final int DEFAULT_POOL_SIZE = 10;//最多最多20个爆炸效果
    public static final int POOL_MAX_SIZE = 20;
    //用于保存所有的爆炸效果的容器
    private static List<Explode> pool = new ArrayList<>();
    //在类加载的时候创建200个子弹对象添加到容器中
    static{
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new Explode());
        }
    }

    //从池塘中获得一个爆炸对象
    public static Explode get(){
        Explode explode=null;
        //池塘被掏空了
        if(pool.size()== 0){
            explode = new Explode();
        }
        else//池塘中还有子弹对象,拿走第一个位置的子弹对象
        {
            explode = pool.remove(0);
        }
        return explode;
    }
    public static void theReturn(Explode explode){
        if(pool.size() == POOL_MAX_SIZE) return;
        else//否则就归还
            pool.add(explode);
    }
}
