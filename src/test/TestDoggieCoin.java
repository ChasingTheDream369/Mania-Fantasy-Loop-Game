package test;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.PathTile;
import unsw.loopmania.Subgoals;
import unsw.loopmania.Character;
import unsw.loopmania.Doggie;
import unsw.loopmania.DoggieCoin;
import unsw.loopmania.ElanMuske;
import unsw.loopmania.Enemy;
import unsw.loopmania.Goal;
import unsw.loopmania.GoalCondtions;
import unsw.loopmania.Helmet;

import org.javatuples.Pair;
import org.json.JSONArray;
import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class TestDoggieCoin{

	/**
	 * Unit test for character getting the DoggieCoin 
	 * after defeating Doggie.
	 */
		//330 and 410
	// @Test
	// public void testCharacterGettingDoggieCoin(){
    //     	TestingWorld tw= new TestingWorld();
    //     	LoopManiaWorld world =  tw.world();
	
    //    	 	List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
    //    	 	PathPosition pathPosition = new PathPosition(1, orderedPath);
	
	// 		Character character = new Character(pathPosition);
    //     	world.setCharacter(character);

    //     	Doggie doge = new Doggie(new PathPosition(3, orderedPath));

    //     	world.addEnemy(doge);

    //     	character.moveDownPath();
    //     	character.moveDownPath();

    //     	world.runBattles();

    //     	assertTrue(character.getDoggieCoin() != null);


	// }

	/**
	 * Testing for DoggieCoin value fluctuate when Elan spwan
	 * in the world and the value goes down when Elan die.
	 */

	@Test
	public void testDoggieCoinFluctuatesValue(){
	
		TestingWorld tw= new TestingWorld();
		LoopManiaWorld world =  tw.world();

		List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
		PathPosition pathPosition = new PathPosition(1, orderedPath);
	
		Character character = new Character(pathPosition);
		world.setCharacter(character);

		DoggieCoin dc = DoggieCoin.getInstance(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
		character.setDoggieCoin(dc);

		Double value = dc.getPrice().getValue();
	
		ElanMuske elan = new ElanMuske(new PathPosition(3, orderedPath));

		world.addEnemy(elan);

		Double value2 = dc.getPrice().getValue();

		assertTrue(value < value2);

		character.moveDownPath();
		character.moveDownPath();

		world.runBattles();

		assertTrue(value2 > dc.getPrice().getValue());
		
 
	}

	/**
	 * Integration test for DoggieCoin.
	 */

    @Test
    public void testDoggieCoin(){
        TestingWorld tw= new TestingWorld();
        LoopManiaWorld world =  tw.world();

        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        PathPosition pathPosition = new PathPosition(3, orderedPath);

        Character character = new Character(pathPosition);
        world.setCharacter(character);

		assertEquals(null, character.getDoggieCoin());

        Doggie doge = new Doggie(new PathPosition(3, orderedPath));

        world.addEnemy(doge);

        // character.moveDownPath();
        // character.moveDownPath();

        world.runBattles();

        assertTrue(character.getDoggieCoin() != null);

		DoggieCoin dc = DoggieCoin.getDoggieCoin();

        Double value = dc.getPrice().getValue();

		character.setHealth(100);

        ElanMuske elan = new ElanMuske(new PathPosition(4, orderedPath));

        world.addEnemy(elan);

        Double value2 = dc.getPrice().getValue();

        assertTrue(value < value2);

		character.moveDownPath();

		world.runBattles();

        assertTrue(value2 > dc.getPrice().getValue());
         
    }   
}
