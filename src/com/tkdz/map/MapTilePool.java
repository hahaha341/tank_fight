package com.tkdz.map;

import com.tkdz.game.bullet;

import java.util.ArrayList;
import java.util.List;

public class MapTilePool {
    public static final int DEFAULT_POOL_SIZE = 50;
    public static final int POOL_MAX_SIZE = 70;
    private static List<MapTile> pool = new ArrayList<>();//定义一个Maptile的List对象
    static{
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new MapTile());//向pool中添加一个新地对象
        }
    }
    public static MapTile get(){//定义池中地get方法
        MapTile b1=null;
        if(pool.size()== 0){
            b1 = new MapTile();//无元素，就添加一个元素
        }
        else
        {
            b1 = pool.remove(0);//否则把list中地index为0的元素移除
        }
        return b1;
    }
    public static void theReturn(MapTile t){
        if(pool.size() == POOL_MAX_SIZE) return;
        else
            pool.add(t);//归还元素：如果超过了上限制，就不要归还
    }
}
//list中自带的方法：add方法，参数是maptile remove方法 参数是index
