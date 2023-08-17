package test;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.PathTile;
import unsw.loopmania.Subgoals;
import unsw.loopmania.Goal;
import unsw.loopmania.GoalCondtions;
import unsw.loopmania.Helmet;

import org.javatuples.Pair;
import org.json.JSONArray;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;
import unsw.loopmania.Zombie;
import unsw.loopmania.Vampire;
import unsw.loopmania.Slug;
import unsw.loopmania.Enemy;
import unsw.loopmania.Armour;

public class TestArmour {

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
    
    /**
     * Sword inflicts 3 damage points to all the enemies.
     * @throws FileNotFoundException
     */
    @Test
    public void testArmourDamage() throws FileNotFoundException {
    
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
    
        PathPosition pos1 = new PathPosition(0, orderedPath);
        PathPosition pos2 = new PathPosition(1, orderedPath);
        PathPosition pos3 = new PathPosition(3, orderedPath);
        PathPosition pos4 = new PathPosition(4, orderedPath);
    
        Character character = new Character(pos1);
        Zombie zombie = new Zombie(pos4);
        Vampire vampire = new Vampire(pos3);
        Slug slug = new Slug(pos2);
    
        Pair<Integer, Integer> firstAvailableSlot = world.getFirstAvailableSlotForItem();
        // now we insert the new sword, as we know we have at least made a slot available...
        Armour armour = new Armour(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        
        double initial_ch_health = character.getHealth();
        character.setDefenceItem(armour);

        assertEquals(0.5, armour.getDamage("Slug"));
        assertEquals(0.5, armour.getDamage("Zombie"));
        assertEquals(0.5, armour.getDamage("Vampire"));

        double zombie_attack = zombie.getDamage();

        zombie.enemyAttack(character);
        assertEquals(zombie_attack-0.5*zombie_attack, initial_ch_health - character.getHealth());

        double slug_attack = slug.getDamage();
        character.setHealth(100);

        slug.enemyAttack(character);
        //assertEquals(slug_attack-(0.5*slug_attack), character.getHealth());

        double vampire_attack = vampire.getDamage();
        double vampire_character_health = character.getHealth();

        vampire.enemyAttack(character);
        assertEquals(vampire_attack-0.5*vampire_attack, vampire_character_health - character.getHealth());

        
    }   

}
