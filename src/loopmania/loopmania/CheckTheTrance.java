package unsw.loopmania;

import java.util.HashMap;
import java.util.List;

public class CheckTheTrance extends Thread{

    private AlliedSoldier ally;
    private List<AlliedSoldier> allyList; 
    private HashMap<AlliedSoldier,Enemy> map;

    public CheckTheTrance(AlliedSoldier ally,List<AlliedSoldier> defeatAlly,HashMap<AlliedSoldier,Enemy> map){
        this.ally = ally;
        this.allyList = defeatAlly;
        this.map = map;
    }
    
    /**
     * Makes the allied soldier fall under trance for 10 seconds
     */
    public void run(){
        try {
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("===================Trance END===========================");
        allyList.remove(ally);
        Enemy e = map.get(ally);
        if(e != null){
            e.setPathPosition(ally.getPathPosition());
            e.changeToEnemy();
        }

    }
}
