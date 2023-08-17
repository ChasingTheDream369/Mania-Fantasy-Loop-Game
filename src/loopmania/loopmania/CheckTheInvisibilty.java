package unsw.loopmania;

public class CheckTheInvisibilty extends Thread{
    
    private Character character;

    public CheckTheInvisibilty(Character character){
        this.character = character;
    }

    /**
     * Makes the charcater invisible for 10 seconds
     */
    public void run(){
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("==================== Remove Invisible==============");
        character.removeInvisible();

    }

}
