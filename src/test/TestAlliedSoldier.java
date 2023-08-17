package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.*;
import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import unsw.loopmania.Character;

public class TestAlliedSoldier {
    

    private static final double CHARACTERHEALTH = 100;
    private static final double CHARACTERDAMAGE = 5;
    private static final double INTIALEXPERIANCE = 0;
    private static final double ALLIEDSOLDIERHEALTH = 2;
    private static final double ALLIEDSOLDIERSPEED = 20;
    private static final double ALLIEDSOLDIERDAMAGE = 3;
    private static final double SLUGHEALTH = 15;
    private static final double VAMPIREHEALTH = 30;
    private static final double ZOMBIEHEALTH = 17;
    private static final double ZOMBIEDAMAGE = 7;
    private static final double VAMPIREDAMAGE = 9.0;
    private static final double SLUGDAMAGE = 5;

    public List<Pair<Integer, Integer>> loadPathTiles(String[] path, int width, int height) {
        
        PathTile starting = new PathTile(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        if (starting.getY() >= height || starting.getY() < 0 || starting.getX() >= width || starting.getX() < 0) {
            throw new IllegalArgumentException("Starting point of path is out of bounds");
        }
        // load connected path tiles
        List<PathTile.Direction> connections = new ArrayList<>();
        for (Object dir: path){
            connections.add(Enum.valueOf(PathTile.Direction.class, dir.toString()));
        }

        if (connections.size() == 0) {
            throw new IllegalArgumentException(
                "This path just consists of a single tile, it needs to consist of multiple to form a loop.");
        }

        // load the first position into the orderedPath
        PathTile.Direction first = connections.get(0);
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(Pair.with(starting.getX(), starting.getY()));

        int x = starting.getX() + first.getXOffset();
        int y = starting.getY() + first.getYOffset();

        // add all coordinates of the path into the orderedPath
        for (int i = 1; i < connections.size(); i++) {
            orderedPath.add(Pair.with(x, y));
            
            if (y >= height || y < 0 || x >= width || x < 0) {
                throw new IllegalArgumentException("Path goes out of bounds at direction index " + (i - 1) + " (" + connections.get(i - 1) + ")");
            }
            
            PathTile.Direction dir = connections.get(i);
            PathTile tile = new PathTile(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
            x += dir.getXOffset();
            y += dir.getYOffset();
            if (orderedPath.contains(Pair.with(x, y)) && !(x == starting.getX() && y == starting.getY())) {
                throw new IllegalArgumentException("Path crosses itself at direction index " + i + " (" + dir + ")");
            }
        }
        // we should connect back to the starting point
        if (x != starting.getX() || y != starting.getY()) {
            throw new IllegalArgumentException(String.format(
                    "Path must loop back around on itself, this path doesn't finish where it began, it finishes at %d, %d.",
                    x, y));
        }
        return orderedPath;
    }

    @Test
    public void testBasicFunctionality() throws FileNotFoundException{
        
        
        String[] Path = {"RIGHT", "RIGHT", "DOWN", "DOWN", "LEFT", "LEFT", "UP", "UP"};
        List<String> rareItems = new ArrayList<String>();
        rareItems.add("TheOneRing");
        
        Subgoals characterGoals= new Subgoals("AND");
        Goal firstGoal = new Goal("cycles", 100);
        characterGoals.addGoal(firstGoal);

        Subgoals childGoal = new Subgoals("OR");
        characterGoals.addGoal(childGoal);

        Goal secondGoal = new Goal("experience", 123456);
        Goal thirdGoal = new Goal("gold", 90000);
        childGoal.addGoal(secondGoal);
        childGoal.addGoal(thirdGoal);
        
        List<Pair<Integer, Integer>> orderedPath = loadPathTiles(Path, 8, 14);
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath, characterGoals, rareItems);
        
        Pair<Integer, Integer> pair1 = new Pair<Integer, Integer>(0,0); 
        Pair<Integer, Integer> pair2 = new Pair<Integer, Integer>(0,1);
        Pair<Integer, Integer> pair3 = new Pair<Integer, Integer>(1,1);
        Pair<Integer, Integer> pair4 = new Pair<Integer, Integer>(2,1);
        Pair<Integer, Integer> pair5 = new Pair<Integer, Integer>(2,2);

        List<Pair<Integer, Integer>> newOrderedPath = new ArrayList<Pair<Integer, Integer>>();

        newOrderedPath.add(pair1);
        newOrderedPath.add(pair2);
        newOrderedPath.add(pair3);
        newOrderedPath.add(pair4);
        newOrderedPath.add(pair5);

        PathPosition pos1 = new PathPosition(0, newOrderedPath);
        PathPosition pos2 = new PathPosition(1, newOrderedPath);
        PathPosition pos3 = new PathPosition(2, newOrderedPath);
        PathPosition pos4 = new PathPosition(3, newOrderedPath);
        PathPosition pos5 = new PathPosition(4, newOrderedPath);

        AlliedSoldier alliedSoldier = new AlliedSoldier(pos1);
        AlliedSoldier alliedSoldier2 = new AlliedSoldier(pos2);

        world.addAlliedSoldier(alliedSoldier);
        world.addAlliedSoldier(alliedSoldier2);

        List<AlliedSoldier> soldiers = world.getAlliedSoldiers(); 
        assertEquals(2, soldiers.size());

        assertEquals(ALLIEDSOLDIERHEALTH, alliedSoldier.getHealth());
        assertEquals(ALLIEDSOLDIERDAMAGE, alliedSoldier.getDamage());
        assertEquals(ALLIEDSOLDIERSPEED, alliedSoldier.getSpeed());

        Vampire vampire = new Vampire(pos5);
        Zombie zombie = new Zombie(pos2);
        Slug slug = new Slug(pos3);

        alliedSoldier.attack(vampire);

        assertEquals(VAMPIREHEALTH-3, vampire.getHealth());

        alliedSoldier.attack(zombie);

        assertEquals(ZOMBIEHEALTH-3, zombie.getHealth());

        alliedSoldier.attack(slug);

        assertEquals(SLUGHEALTH-3, slug.getHealth());

        vampire.attack(alliedSoldier);

        final double newAlliedSoldierHealth = ALLIEDSOLDIERHEALTH-VAMPIREDAMAGE;

        assertEquals(newAlliedSoldierHealth, alliedSoldier.getHealth());

        assertEquals(2, world.getAlliedSoldiers().size());

        assertEquals(14, zombie.getHealth());

        final double newAlliedSoldierHealth2 = newAlliedSoldierHealth-ZOMBIEDAMAGE;

        //assertEquals(newAlliedSoldierHealth2, alliedSoldier.getHealth());

        //assertEquals(1, world.getAlliedSoldiers().size());
        slug.attack(alliedSoldier);

        final double newAlliedSoldierHealth3 = newAlliedSoldierHealth2-SLUGDAMAGE;

        //assertEquals(newAlliedSoldierHealth3, alliedSoldier.getHealth());

    }


}
