package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.*;
import unsw.loopmania.Character;

public class TestSpawnExtensions {
    
    @Test
    public void testMissileSpawn() {
        TestingWorld tw = new TestingWorld();
        LoopManiaWorld world = tw.world();

        Character c = new Character(new PathPosition(1, world.getOrderedPath()));
        world.setCharacter(c);

        assertEquals(world.spawnMissile(), null);

        c.setExperience(9.0);

        assertNotEquals(world.spawnMissile(), null);
    }

    @Test
    public void testMissileDamage() {
        TestingWorld tw = new TestingWorld();
        LoopManiaWorld world = tw.world();

        Character c = new Character(new PathPosition(4, world.getOrderedPath()));
        world.setCharacter(c);

        Enemy vamp = new Vampire (new PathPosition(4, world.getOrderedPath()));
        world.addEnemy(vamp);

        c.setExperience(9.0); 

        Missile m = world.spawnMissile();

        world.attackWithMissile(vamp);

        assertEquals(vamp.getHealth(), 28.0);

        Missile m1 = world.spawnMissile();

        world.runTickMoves();
        
        assertEquals(m.getX(), 4);
        assertEquals(m.getY(), 1);

    }

    @Test
    public void testChickenWingsSpawn() {
        TestingWorld tw = new TestingWorld();
        LoopManiaWorld world = tw.world();

        Character c = new Character(new PathPosition(4, world.getOrderedPath()));
        world.setCharacter(c);

        assertEquals(world.spawnChickenWings(0), null);
        assertNotEquals(world.spawnChickenWings(6), null);
    }

    @Test
    public void testTakeChickenWings() {
        TestingWorld tw = new TestingWorld();
        LoopManiaWorld world = tw.world();

        Character c = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(c);

        ChickenWings cw = new ChickenWings(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.setChickenWings(cw);

        assertEquals(world.getChickenWings(), cw);

        world.takeChickenWings();

        assertEquals(world.getChickenWings(), null);
    }
}
