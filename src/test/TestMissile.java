package test;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.Missile;
import unsw.loopmania.PathPosition;
import unsw.loopmania.PathTile;
import unsw.loopmania.Slug;

import org.javatuples.Pair;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import unsw.loopmania.Character;

public class TestMissile {
    

    @Test
    public void testMissile(){
        TestingWorld tw= new TestingWorld();
		LoopManiaWorld world =  tw.world();

		List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
		PathPosition pathPosition = new PathPosition(4, orderedPath);
	
		Character character = new Character(pathPosition);
		world.setCharacter(character);

        Missile missile = new Missile(new PathPosition(3, orderedPath));
        world.setMissile(missile);

        Slug slug = new Slug(new PathPosition(2, orderedPath));
        world.addEnemy(slug);

        missile.moveUpPath();

        world.runBattles();

        assertEquals(15-2,slug.getHealth());


    }

        

}
