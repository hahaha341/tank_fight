package com.tkdz.game;

import com.tkdz.util.MyUtil;

//用来管理当前关卡的信息的：单例类
// 单例设计模式 如果一个类只需要该类具有唯一的实例
public class LevelInfo {
    //构造方法私有化
    private LevelInfo(){

    }
    //定义静态的本类类型的变量，指向唯一的实例
    private static LevelInfo instance;

    //懒汉模式的单例 第一次使用该实例时创建唯一的实例
    //所有的访问该类的唯一实例都是通过该方法
    public static LevelInfo getInstance(){
        if(instance == null){
            //创建了唯一的实例
            instance = new LevelInfo();
        }
        return instance;
    }
    private int level;//关卡的编号
    //关卡的敌人的数量
    private int enemyCount;
    private int crossTime = -1 ;//通关要求的时长 -1意味不限时
    //敌人类型信息
    private int [] enemyType;
    //游戏的难度
    private int levelType;

    public int getLevelType() {
        return levelType<=0?1:levelType;//难度要求:大于等于1
    }

    public void setLevelType(int levelType) {
        this.levelType = levelType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public void setEnemyCount(int enemyCount) {
        this.enemyCount = enemyCount;
    }

    public int getCrossTime() {
        return crossTime;
    }

    public void setCrossTime(int crossTime) {
        this.crossTime = crossTime;
    }

    public int[] getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(int[] enemyType) {
        this.enemyType = enemyType;
    }
    //获得敌人类型数组中的随机元素
    //获得随机的敌人的类型
    public int getRandomEnemyType(){
        int index =MyUtil.getRandomNumber(0,enemyType.length);
        return enemyType[index];
    }
}
