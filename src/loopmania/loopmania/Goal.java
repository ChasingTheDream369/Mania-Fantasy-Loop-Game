package unsw.loopmania;

public class Goal implements GoalCondtions {
    
    String goalType;
    int quantity;

    /**
     * Goal for the character to win the game.
     * @param type
     * @param quantity
     */

    public Goal(String type, int quantity) {
        this.goalType = type;
        this.quantity = quantity;
    }

    /**
     * checking if the goals are satisfied by the characters.
     */

    @Override
    public boolean evaluateOverallGoal(Character character) {
        if(goalType.equals("cycles")){
            return (character.getNumOfCycles() >= quantity);    
        }
        else if(goalType.equals("experience")){
            return ((int)character.getExperience() >= quantity);
        }
        else if(goalType.equals("bosses")){
            return character.killedBosses();
        }
        else{
            return (character.getGold() >= quantity);
        }
    }

    

}
