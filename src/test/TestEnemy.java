package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;

import org.javatuples.Pair;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Slug;
import unsw.loopmania.Sword;
import unsw.loopmania.Vampire;
import unsw.loopmania.VampireCastleBuilding;
import unsw.loopmania.Zombie;
import unsw.loopmania.Character;
import unsw.loopmania.Enemy;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.BasicEnemy;
import unsw.loopmania.Buildings;

public class TestEnemy {
    
    private static final double VAMPIREHEALTH = 30;
    private static final double ZOMBIEHEALTH = 17;
    private static final double SLUGHEALTH = 15;
    private static final double ZOMBIEDAMAGE = 7;
    private static final double VAMPIREDAMAGE = 9;
    private static final double SLUGDAMAGE = 1.5;

    // @BeforeClass
    // public void initializeWorld(){

    //     LoopManiaWorld world = new LoopManiaWorld(100, 100, new ArrayList<>());

    //     Pair<Integer, Integer> pair1 = new Pair<Integer, Integer>(0,0); 
    //     Pair<Integer, Integer> pair2 = new Pair<Integer, Integer>(0,1);
    //     Pair<Integer, Integer> pair3 = new Pair<Integer, Integer>(1,1);
    //     Pair<Integer, Integer> pair4 = new Pair<Integer, Integer>(2,1);
    //     Pair<Integer, Integer> pair5 = new Pair<Integer, Integer>(2,2);

    //     List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();

    //     orderedPath.add(pair1);
    //     orderedPath.add(pair2);
    //     orderedPath.add(pair3);
    //     orderedPath.add(pair4);
    //     orderedPath.add(pair5);

    //     PathPosition pos1 = new PathPosition(0, orderedPath);
    //     PathPosition pos2 = new PathPosition(1, orderedPath);
    //     PathPosition pos3 = new PathPosition(2, orderedPath);
    //     PathPosition pos4 = new PathPosition(3, orderedPath);
    //     PathPosition pos5 = new PathPosition(4, orderedPath);        

    // }



    @Test
    public void test1(){

        Pair<Integer, Integer> pair1 = new Pair<Integer, Integer>(0,0); 
        Pair<Integer, Integer> pair2 = new Pair<Integer, Integer>(0,1);
        Pair<Integer, Integer> pair3 = new Pair<Integer, Integer>(1,1);
        Pair<Integer, Integer> pair4 = new Pair<Integer, Integer>(2,1);
        Pair<Integer, Integer> pair5 = new Pair<Integer, Integer>(2,2);

        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();

        orderedPath.add(pair1);
        orderedPath.add(pair2);
        orderedPath.add(pair3);
        orderedPath.add(pair4);
        orderedPath.add(pair5);

        PathPosition pos1 = new PathPosition(0, orderedPath);
        PathPosition pos2 = new PathPosition(1, orderedPath);
        PathPosition pos3 = new PathPosition(2, orderedPath);
        PathPosition pos4 = new PathPosition(3, orderedPath);
        PathPosition pos5 = new PathPosition(4, orderedPath);


        //Character character = new Character(pos1);
        Vampire vampire = new Vampire(pos5);
        Zombie zombie = new Zombie(pos2);
        Slug slug = new Slug(pos3);

        assertEquals(VAMPIREHEALTH, vampire.getHealth());
        assertEquals(ZOMBIEHEALTH, zombie.getHealth());
        assertEquals(SLUGHEALTH, slug.getHealth());

        assertEquals(VAMPIREDAMAGE, vampire.getDamage());
        assertEquals(ZOMBIEDAMAGE, zombie.getDamage());
        assertEquals(SLUGDAMAGE, slug.getDamage());

    }

    @Test
    public void testMovement() {
        TestingWorld tw = new TestingWorld();
        LoopManiaWorld world =  tw.world();

        Enemy vampire = new Vampire(new PathPosition(1, world.getOrderedPath()));
        Enemy zombie = new Vampire(new PathPosition(2, world.getOrderedPath()));
        Character c = new Character(new PathPosition(3, world.getOrderedPath()));

        world.addEnemy(vampire);
        world.addEnemy(zombie);
        world.setCharacter(c);

        world.runTickMoves();

        assertEquals(vampire.getPathPosition().getX().getValue(), new PathPosition(0, world.getOrderedPath()).getX().getValue());
        assertEquals(vampire.getPathPosition().getY().getValue(), new PathPosition(0, world.getOrderedPath()).getY().getValue());
        
        assertEquals(zombie.getPathPosition().getX().getValue(), new PathPosition(1, world.getOrderedPath()).getX().getValue());
        assertEquals(zombie.getPathPosition().getY().getValue(), new PathPosition(1, world.getOrderedPath()).getY().getValue());

        assertEquals(c.getPathPosition().getX().getValue(), new PathPosition(4, world.getOrderedPath()).getX().getValue());
        assertEquals(c.getPathPosition().getY().getValue(), new PathPosition(4, world.getOrderedPath()).getY().getValue());
    }

    @Test
    public void testSupportRadius() {
        TestingWorld tw = new TestingWorld();
        LoopManiaWorld world = tw.world();

        Character c = new Character(new PathPosition(0, world.getOrderedPath()));
        world.setCharacter(c);

        Enemy e1 = new Slug(new PathPosition(0, world.getOrderedPath()));
        Enemy e2 = new Vampire(new PathPosition(1, world.getOrderedPath()));
        world.addEnemy(e1);
        world.addEnemy(e2);

        List<Enemy> e1Suppport = world.checkSupportRadi(e1);
        assertTrue(e1Suppport.contains(e2));

        List<Enemy> e2Suppport = world.checkSupportRadi(e2);
        assertTrue(e2Suppport.contains(e1));
    }


}
