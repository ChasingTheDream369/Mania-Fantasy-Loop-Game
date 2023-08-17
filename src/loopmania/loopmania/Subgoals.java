package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;

public class Subgoals implements GoalCondtions {

    String operator;
    List<GoalCondtions> goalNodes;

    public Subgoals(String goalQuantifier) {
        this.operator = goalQuantifier;
        this.goalNodes = new ArrayList<GoalCondtions>();
    }
    
    
    public void addGoal(GoalCondtions newGoal) {
        goalNodes.add(newGoal);
    }

    @Override
    public boolean evaluateOverallGoal(Character character) {
        GoalCondtions firstChild = goalNodes.get(0);
        GoalCondtions secondChild = goalNodes.get(1);
        if(operator.equals("AND")){
            return (firstChild.evaluateOverallGoal(character) && secondChild.evaluateOverallGoal(character));
        }
        else{
            return (firstChild.evaluateOverallGoal(character) || secondChild.evaluateOverallGoal(character));
        }   

    }


}
