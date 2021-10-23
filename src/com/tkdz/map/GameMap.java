package com.tkdz.map;

import com.tkdz.game.GameFrame;
import com.tkdz.game.LevelInfo;
import com.tkdz.tank.tank;
import com.tkdz.util.Constant;
import com.tkdz.util.MyUtil;
import com.tkdz.map.MapTile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import java.awt.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.function.DoubleUnaryOperator;
import java.lang.String;

//游戏地图类
public class GameMap {
    public static final int MAP_X = tank.REDIUS*3;
    public static final int MAP_Y = tank.REDIUS*3+ GameFrame.titleBarH;
    public static final int MAP_WIDTH = Constant.FRAME_WIDTH-tank.REDIUS*6;
    public static final int MAP_HEIGHT = Constant.FRAME_HEIGHT-tank.REDIUS*8-GameFrame.titleBarH;

    //地图元素块的容器
    private ArrayList<MapTile> tiles = new ArrayList<>();
    //大本营
    private TankHouse house;
    public GameMap() {

    }
    //初始化地图元素块 level代表第几关
    public void initMap(int level){
        tiles.clear();
        try {
            loadLevel(level);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //初始化大本营
        house = new TankHouse();
        addHouse();
    }
    //加载关卡信息
    private void loadLevel(int level) throws Exception {
        //获得关卡信息类的唯一实例对象
        LevelInfo levelInfo = LevelInfo.getInstance();
        levelInfo.setLevel(level);
        Properties prop = new Properties();
        prop.load(new FileInputStream("level/lv_"+level));
        //将所有的地图信息加载出来
        int enemyCount = Integer.parseInt(prop.getProperty("enemyCount"));
        //设置敌人数量
        levelInfo.setEnemyCount(enemyCount);
        //0,1 对敌人类型解析
        String[] enemyType = prop.getProperty("enemyType").split(",");
        int[]type = new int [enemyType.length];
        for (int i = 0; i < type.length; i++) {
            type[i]= Integer.parseInt(enemyType[i]);
        }
        //设置敌人类型
        levelInfo.setEnemyType(type);
        //关卡难度
        //如果没有涉及游戏难度，就添加一个默认值
        String x = prop.getProperty("levelType");
        levelInfo.setLevelType(Integer.parseInt(x==null?"1":x));

        String methodName = prop.getProperty("method");
        int invokeCount = Integer.parseInt(prop.getProperty("invokeCount"));
        //把实参都读取到数组中
        String[] params = new String[invokeCount];
        for (int i = 1; i <=invokeCount ; i++) {
            params[i-1]=prop.getProperty("param"+i);
        }
        //使用读取到的参数调用对应的方法
        invokeMethod(methodName,params);
    }
    //根据方法的名字和参数调用对应的方法
    private void invokeMethod(String name,String[] params){//传参没什么问题
        for(String param:params){
            //获得每一行的方法的参数，解析
            String[] split = param.split(",");
            //使用一个int数组保存解析后的内容
            int []arr = new int [split.length];
            int i;
            for (i = 0; i < split.length-1; i++) {
                arr[i] = Integer.parseInt(split[i]);
            }
            //块之间的间隔是地图块的倍数
            final int DIS = MapTile.tileW;
            //解析最后一个double值
            int dis = (int)(Double.parseDouble(split[i])*DIS);
            switch (name){
                case "addRow":
                    addRow(MAP_X+arr[0]*DIS,MAP_Y+arr[1]*DIS,
                            MAP_X+MAP_WIDTH-arr[2]*DIS
                            ,arr[3],dis);
                    break;
                case "addCol":
                    addCol(MAP_X+arr[0]*DIS,MAP_Y+arr[1]*DIS,
                            MAP_Y+MAP_HEIGHT-arr[2]*DIS,arr[3],dis);
                    break;
                case "addRect":
                    addRect(MAP_X+arr[0]*DIS,MAP_Y+arr[1]*DIS,
                            MAP_X+MAP_WIDTH-arr[2]*DIS,MAP_Y+MAP_HEIGHT-arr[3]*DIS,
                            arr[4],dis);
                    break;
            }
        }
    }
    //将老巢的所有的元素块添加到地图的容器中
    private void addHouse(){
        tiles.addAll(house.getTiles());
    }
    //某一个点确定的地图块是否和tiles集合中的所有的块有重叠
    //有重叠返回true否则false
    private boolean isCollide(List<MapTile> tiles,int x,int y){
        for (MapTile tile : tiles) {
            int tileX = tile.getX();
            int tileY = tile.getY();
            if(Math.abs(tileX-x)<MapTile.tileW&&Math.abs(tileY-y)<MapTile.tileW)
            {
                return true;
            }
        }
        return false;
    }
    //只对没有遮挡效果的块进行绘制
    public void drawBk(Graphics g){
        for (MapTile tile : tiles) {
            if(tile.getType()!=MapTile.TYPE_COVER)
            tile.draw(g);
        }
    }
    //只绘制有遮挡效果的块
    public void drawCover(Graphics g){
        for (MapTile tile : tiles) {
            if(tile.getType()==MapTile.TYPE_COVER)
                tile.draw(g);
        }
    }
    public List<MapTile> getTiles(){
        return tiles;
    }
    //将所有不可见的地图块从容器中移除
    public void clearDestoryTile(){
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if(!tile.isVisible())
                tiles.remove(i);
        }
    }
    //往地图块容器中添加一行指定类型的地图块
    //stx 添加地图块的起始x坐标 dis 地图块之间的中心点的间隔 如果是块的宽度，意味着是连续的
    //如果大于块的宽度，则不连续
    public void addRow(int startX,int startY,int endX,int type,final int DIST){
            int count = (endX-startX)/(MapTile.tileW+DIST);
        for (int i = 0; i < count; i++) {
            MapTile tile = MapTilePool.get();
            tile.setType(type);
            tile.setX(startX+i*(MapTile.tileW+DIST));
            tile.setY(startY);
            tiles.add(tile);
            }
    }
    //往地图元素块容器中添加一列元素
    public void addCol(int startX,int startY,int endY,int type,final int DIST){
        int count = (endY-startY)/(MapTile.tileW+DIST);
        for (int i = 0; i < count; i++) {
            MapTile tile = MapTilePool.get();
            tile.setType(type);
            tile.setY(startX);
            tile.setX(startY+i*(MapTile.tileW+DIST));
            tiles.add(tile);
        }
    }
    //对指定的矩形区域添加元素块
    public void addRect(int startX,int startY,int endX,int endY,int type,final int DIST){
        int rows = (endY-startY)/(MapTile.tileW+DIST);
        for (int i = 0; i < rows; i++) {
            addRow(startX,startY+i*(MapTile.tileW+DIST),endX,type,DIST);
        }
        //int cols = (endX-startX)/(MapTile.tileW+DIST);
    }
}
