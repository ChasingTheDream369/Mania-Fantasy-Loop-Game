package test;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Character;
import unsw.loopmania.ChickenWings;

import org.javatuples.Pair;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Time;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestChickenWings {
 
    
    @Test
    public void testChickenWings(){

        TestingWorld tw= new TestingWorld();
        LoopManiaWorld world =  tw.world();

        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        PathPosition pathPosition = new PathPosition(1, orderedPath);

        Character character = new Character(pathPosition);
        world.setCharacter(character);

        PathPosition pathForChicken = new PathPosition(2,orderedPath);
        
        ChickenWings chicken = new ChickenWings(pathForChicken.getX(), pathForChicken.getY());
        
        world.setChickenWings(chicken);

        character.moveDownPath();

        world.takeChickenWings();

        assertEquals(true, character.getInvisible());
        
        try {
            TimeUnit.SECONDS.sleep(11);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(false, character.getInvisible());
        

    }

}
