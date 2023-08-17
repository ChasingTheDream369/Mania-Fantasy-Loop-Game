package test;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.Missile;
import unsw.loopmania.PathPosition;
import unsw.loopmania.PathTile;
import unsw.loopmania.Ramen;
import unsw.loopmania.Slug;

import org.javatuples.Pair;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import unsw.loopmania.Character;

public class TestDragonAndRamen {
    
    @Test
    public void TestDragon(){
        TestingWorld tw= new TestingWorld();
		LoopManiaWorld world =  tw.world();

		List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
		PathPosition pathPosition = new PathPosition(1, orderedPath);

        Character character = new Character(pathPosition);
		world.setCharacter(character);

        PathPosition pathForChicken = new PathPosition(3,orderedPath);

        Ramen ramen = new Ramen(pathForChicken.getX(), pathForChicken.getY());

        world.setRamen(ramen);

        character.moveDownPath();
        character.moveDownPath();

        world.takeRamen();

        assertEquals(true, character.isDragon());


        Slug slug = new Slug(new PathPosition(5,orderedPath));

        world.addEnemy(slug);

        character.moveDownPath();
        character.moveDownPath();        

        world.runBattles();

        assertEquals(false, character.isDragon());
        
    }

}
