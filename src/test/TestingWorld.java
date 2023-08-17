package test;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.*;

public class TestingWorld {

    List<String> rareItems;

    public LoopManiaWorld world() {
        
        int width = 18;
        int height = 14;

        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        JSONObject path = createMap(0, 0);
        orderedPath = loadPathTiles(path, width, height);
        
        Subgoals characterGoals= new Subgoals("AND");
        Goal firstGoal = new Goal("cycles", 100);
        characterGoals.addGoal(firstGoal);

        Subgoals childGoal = new Subgoals("OR");
        characterGoals.addGoal(childGoal);

        Goal secondGoal = new Goal("experience", 123456);
        Goal thirdGoal = new Goal("gold", 90000);
        childGoal.addGoal(secondGoal);
        childGoal.addGoal(thirdGoal);

        List<String> rareItems = new ArrayList<String>();

        rareItems.add("TheOneRing");
        rareItems.add("TheOneRing");
        rareItems.add("AndurilFlameOfTheWest");
        rareItems.add("TreeStump");
        rareItems.add("TreeStump");
        rareItems.add("TreeStump");

        // Create a path position where the character is at the first part of the path(Character goals)
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, characterGoals, rareItems);
        this.rareItems = rareItems;

        return world;
    }

    public List<String> getLoadedRareItems() {
        return rareItems;
    }
    
    

    private List<Pair<Integer, Integer>> loadPathTiles(JSONObject path, int width, int height) {
        if (!path.getString("type").equals("path_tile")) {
            // ... possible extension
            throw new RuntimeException(
                    "Path object requires path_tile type.  No other path types supported at this moment.");
        }
        PathTile starting = new PathTile(new SimpleIntegerProperty(path.getInt("x")), new SimpleIntegerProperty(path.getInt("y")));
        if (starting.getY() >= height || starting.getY() < 0 || starting.getX() >= width || starting.getX() < 0) {
            throw new IllegalArgumentException("Starting point of path is out of bounds");
        }
        // load connected path tiles
        List<PathTile.Direction> connections = new ArrayList<>();
        for (Object dir: path.getJSONArray("path").toList()){
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
            //PathTile tile = new PathTile(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
            x += dir.getXOffset();
            y += dir.getYOffset();
            if (orderedPath.contains(Pair.with(x, y)) && !(x == starting.getX() && y == starting.getY())) {
                throw new IllegalArgumentException("Path crosses itself at direction index " + i + " (" + dir + ")");
            }
            //onLoad(tile, connections.get(i - 1), dir);
        }
        // we should connect back to the starting point
        if (x != starting.getX() || y != starting.getY()) {
            throw new IllegalArgumentException(String.format(
                    "Path must loop back around on itself, this path doesn't finish where it began, it finishes at %d, %d.",
                    x, y));
        }
        //onLoad(starting, connections.get(connections.size() - 1), connections.get(0));
        return orderedPath;
    }

    public CampfireCard createCampfireCard(LoopManiaWorld world) {
        CampfireCard tc = new CampfireCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addUnequippedCard(tc);
        return tc;
    }

    public Buildings createCampfireBuilding(LoopManiaWorld world, int x, int y) {
        CampfireCard cd = createCampfireCard(world);
        return world.convertCardToBuildingByCoordinates("CampfireCard", 0, 0, x, y);
    }

    public VampireCastleCard createVampireCastleCard(LoopManiaWorld world) {
        VampireCastleCard c = new VampireCastleCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addUnequippedCard(c);
        return c;
    }

    public Buildings createVampireCastleBuilding(LoopManiaWorld world, int x, int y) {
        VampireCastleCard cd = createVampireCastleCard(world);
        return world.convertCardToBuildingByCoordinates("VampireCastleCard", 0, 0, x, y);
    }

    public ZombiePitCard createZombiePitCard(LoopManiaWorld world) {
        ZombiePitCard tc = new ZombiePitCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addUnequippedCard(tc);
        return tc;
    }

    public Buildings createZombiePitBuilding(LoopManiaWorld world, int x, int y) {
        ZombiePitCard cd = createZombiePitCard(world);
        return world.convertCardToBuildingByCoordinates("ZombiePitCard", 0, 0, x, y);
    }

    public VillageCard createVillageCard(LoopManiaWorld world) {
        VillageCard vc = new VillageCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));        
        world.addUnequippedCard(vc);
        return vc;
    }

    public Buildings createVillageBuilding(LoopManiaWorld world, Integer x, Integer y) {
        VillageCard vc = createVillageCard(world);
        return world.convertCardToBuildingByCoordinates("VillageCard", 0, 0, x, y);
    }

    public BarracksCard createBarracksCard(LoopManiaWorld world) {
        BarracksCard bc = new BarracksCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addUnequippedCard(bc);
        return bc;
    }

    public Buildings createBarrackBuilding(LoopManiaWorld world, int x, int y) {
        BarracksCard bc = createBarracksCard(world);
        return world.convertCardToBuildingByCoordinates("BarracksCard", 0, 0, x, y);
    }

    public TrapCard createTrapCard(LoopManiaWorld world) {
        TrapCard tc = new TrapCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addUnequippedCard(tc);
        return tc;
    }
    public Buildings createTrapBuilding(LoopManiaWorld world, Integer x, Integer y) {
        TrapCard trapCard = createTrapCard(world);
        return world.convertCardToBuildingByCoordinates("TrapCard", 0, 0, x, y);
    }

    public TowerCard createTowerCard(LoopManiaWorld world) {
        TowerCard tc = new TowerCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addUnequippedCard(tc);
        return tc;
    }
    public Buildings createTowerBuilding(LoopManiaWorld world, Integer x, Integer y) {
        TowerCard towerCard = createTowerCard(world);
        return world.convertCardToBuildingByCoordinates("TrapCard", 0, 0, x, y);
    }


    private JSONObject createMap(int i, int j) {
        JSONObject world = new JSONObject(); 
        world.put("type", "path_tile"); 
        world.put("x", 0); world.put("y", 0); 

        String[] path = {"RIGHT", "RIGHT", "DOWN", "RIGHT", "RIGHT", "UP", "RIGHT", "RIGHT", "RIGHT",
            "DOWN", "DOWN", "DOWN", "DOWN", "DOWN", "DOWN", "DOWN", "DOWN", "DOWN",
            "LEFT", "LEFT", "LEFT", "UP", "UP", "RIGHT",
            "UP", "UP", "UP", "UP", "LEFT", "LEFT", "LEFT",
            "DOWN", "DOWN", "DOWN", "DOWN", "DOWN", "DOWN", "DOWN", "DOWN",
            "RIGHT", "RIGHT", "RIGHT", "RIGHT", "RIGHT", "DOWN", "DOWN",
            "LEFT", "LEFT", "LEFT", "LEFT", "LEFT", "LEFT", "LEFT",
            "UP", "UP", "UP", "UP", "UP", "UP", "UP", "UP", "UP", "UP", "UP", "UP", "UP"};
        
        JSONArray jsonPath = new JSONArray();
        for (String direction: path) {
            jsonPath.put(direction);
        }

        world.put("path", jsonPath);
        return world;
    }
    
}
    
