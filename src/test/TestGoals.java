package test;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.PathTile;
import unsw.loopmania.Subgoals;
import unsw.loopmania.Goal;
import unsw.loopmania.GoalCondtions;
import org.javatuples.Pair;
import org.json.JSONArray;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import jdk.jshell.Snippet.SubKind;
import unsw.loopmania.Character;
import unsw.loopmania.Zombie;
import unsw.loopmania.Vampire;
import unsw.loopmania.Slug;
import unsw.loopmania.Enemy;
import unsw.loopmania.Sword;

public class TestGoals {

    public List<Pair<Integer, Integer>> loadPathTiles(String[] path, int width, int height) {

        PathTile starting = new PathTile(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        if (starting.getY() >= height || starting.getY() < 0 || starting.getX() >= width || starting.getX() < 0) {
            throw new IllegalArgumentException("Starting point of path is out of bounds");
        }
        // load connected path tiles
        List<PathTile.Direction> connections = new ArrayList<>();
        for (Object dir : path) {
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
                throw new IllegalArgumentException(
                        "Path goes out of bounds at direction index " + (i - 1) + " (" + connections.get(i - 1) + ")");
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

    /**
     * Sword inflicts 3 damage points to all the enemies.
     * 
     * @throws FileNotFoundException
     */
    @Test
    public void testGoals() throws FileNotFoundException {
        String[] Path = { "RIGHT", "RIGHT", "DOWN", "DOWN", "LEFT", "LEFT", "UP", "UP" };
        JSONArray rareItems = new JSONArray();
        rareItems.put("TheOneRing");

        Subgoals characterGoals = new Subgoals("AND");
        Goal firstGoal = new Goal("cycles", 5);
        characterGoals.addGoal(firstGoal);

        Subgoals childGoal = new Subgoals("OR");
        Goal secondGoal = new Goal("experience", 14);
        Goal thirdGoal = new Goal("gold", 27);
        childGoal.addGoal(secondGoal);
        childGoal.addGoal(thirdGoal);
        characterGoals.addGoal(childGoal);

        List<Pair<Integer, Integer>> orderedPath = loadPathTiles(Path, 8, 14);

        PathPosition pos1 = new PathPosition(0, orderedPath);
        Character character = new Character(pos1);

        character.setExperience((double) 14);
        character.ReceiveGoldItems(12);
        character.setNumOfCycles(5);

        assertTrue(characterGoals.evaluateOverallGoal(character));

        Subgoals characterGoals2 = new Subgoals("OR");
        Goal firstGoal2 = new Goal("experience", 200);
        characterGoals2.addGoal(firstGoal2);

        Subgoals childGoal2 = new Subgoals("OR");
        characterGoals2.addGoal(childGoal2);

        Goal secondGoal2 = new Goal("cycles", 12);
        Goal thirdGoal2 = new Goal("gold", 28);
        childGoal.addGoal(secondGoal2);
        childGoal.addGoal(thirdGoal2);

        character.setExperience((double) 200);
        character.setNumOfCycles(12);

        assertTrue(characterGoals2.evaluateOverallGoal(character));

        Subgoals characterGoals3 = new Subgoals("AND");
        Goal firstGoal3 = new Goal("bosses",0);
        characterGoals3.addGoal(firstGoal3);

        Subgoals childGoal3 = new Subgoals("AND");
        characterGoals3.addGoal(childGoal3);

        Goal secondGoal3 = new Goal("cycles", 42);
        Goal thirdGoal3 = new Goal("gold", 200);
        childGoal3.addGoal(secondGoal3);
        childGoal3.addGoal(thirdGoal3);

        character.setKilledBosses();

        assertEquals(false, characterGoals3.evaluateOverallGoal(character));

        character.ReceiveGoldItems(200);
        character.setNumOfCycles(42);

        assertEquals(true, characterGoals3.evaluateOverallGoal(character));

        PathPosition pos = new PathPosition(3, orderedPath);
        Character character1 = new Character(pos);

        character1.ReceiveGoldItems(200);
        character1.setNumOfCycles(42);

        Subgoals characterGoals4 = new Subgoals("AND");
        Goal firstGoal4 = new Goal("bosses",0);
        characterGoals4.addGoal(firstGoal4);

        Subgoals childGoal4 = new Subgoals("AND");
        characterGoals4.addGoal(childGoal4);

        Goal secondGoal4 = new Goal("cycles", 42);
        Goal thirdGoal4 = new Goal("gold", 200);
        childGoal4.addGoal(secondGoal4);
        childGoal4.addGoal(thirdGoal4);


        assertEquals(false, characterGoals4.evaluateOverallGoal(character1));

        character.ReceiveGoldItems(200);
        character.setNumOfCycles(42);

        assertEquals(true, characterGoals4.evaluateOverallGoal(character));

    }

}
