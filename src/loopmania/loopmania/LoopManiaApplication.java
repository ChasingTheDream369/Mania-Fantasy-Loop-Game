package unsw.loopmania;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * the main application
 * run main method from this class
 */
public class LoopManiaApplication extends Application {

    /**
     * the controller for the game. Stored as a field so can terminate it when click exit button
     */
    private LoopManiaWorldController mainController;
    MediaPlayer player;

    public void startGame(Stage primaryStage) throws IOException {

        // set title on top of window bar
        //playMusic();
        primaryStage.setTitle("Loop Mania");

        // prevent human player resizing game window (since otherwise would see white space)
        // alternatively, you could allow rescaling of the game (you'd have to program resizing of the JavaFX nodes)
        primaryStage.setResizable(false);

        // load the main game
        LoopManiaWorldControllerLoader loopManiaLoader = new LoopManiaWorldControllerLoader("world_with_twists_and_turns.json");
        mainController = loopManiaLoader.loadController();
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("LoopManiaView.fxml"));
        gameLoader.setController(mainController);
        Parent gameRoot = gameLoader.load();

        // load the main menu
        MainMenuController mainMenuController = new MainMenuController(mainController);
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("MainMenuView.fxml"));
        menuLoader.setController(mainMenuController);
        Parent mainMenuRoot = menuLoader.load();

        // load the shopping menu
        ShopMenuController shoppingMenuController = new StandardMenuController(mainController.getCharacter(), mainController);
        FXMLLoader shoppingMenuLoader = new FXMLLoader(getClass().getResource("ShoppingMenuView.fxml"));
        shoppingMenuLoader.setController(shoppingMenuController);
        Parent shoppingMenuRoot = shoppingMenuLoader.load();

        // load the berserker menu
        ShopMenuController berserkerMenuController = new BerserkerMenuController(mainController.getCharacter(), mainController);
        FXMLLoader berserkerMenuLoader = new FXMLLoader(getClass().getResource("ShoppingMenuView.fxml"));
        berserkerMenuLoader.setController(berserkerMenuController);
        Parent berserkerMenuRoot = berserkerMenuLoader.load();

        // load the survival menu
        ShopMenuController survivalMenuController = new SurvivalMenuController(mainController.getCharacter(), mainController);
        FXMLLoader survivalMenuLoader = new FXMLLoader(getClass().getResource("ShoppingMenuView.fxml"));
        survivalMenuLoader.setController(survivalMenuController);
        Parent survivalMenuRoot = survivalMenuLoader.load();

        // load the Game Main Menu
        GameMainMenuController gameMainMenuController = new GameMainMenuController(mainController);
        FXMLLoader gameMainMenuLoader = new FXMLLoader(getClass().getResource("GameMainMenuView.fxml"));
        gameMainMenuLoader.setController(gameMainMenuController);
        Parent gameMainMenuRoot = gameMainMenuLoader.load();

        // load the Game Main Menu
        GameOverController gameOverController = new GameOverController();
        FXMLLoader gameOverLoader = new FXMLLoader(getClass().getResource("GameOver.fxml"));
        gameOverLoader.setController(gameOverController);
        Parent gameOverRoot = gameOverLoader.load();
        

        // create new scene with the main menu (so we start with the main menu)
        Scene scene = new Scene(mainMenuRoot);
        
        // set functions which are activated when button click to switch menu is pressed
        // e.g. from main menu to start the game, or from the game to return to main menu
        mainController.setMainMenuSwitcher(() -> {switchToRoot(scene, mainMenuRoot, primaryStage);});
        mainController.setShoppingMenuSwitcher(() -> {switchToRoot(scene, shoppingMenuRoot, primaryStage);});
        mainController.setBerserkerMenuSwitcher(() -> {switchToRoot(scene, berserkerMenuRoot, primaryStage);});
        mainController.setSurvivalMenuSwitcher(() -> {switchToRoot(scene, survivalMenuRoot, primaryStage);});
        mainController.setGameMainMenuSwitcher(() -> {switchToRoot(scene, gameMainMenuRoot, primaryStage);});
        mainController.setGameOverSwticher(() -> {switchToRoot(scene, gameOverRoot, primaryStage);});

        mainMenuController.setGameSwitcher(() -> {
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.startTimer();
        });

        shoppingMenuController.setGameSwitcher(() -> {
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.startTimer();
        });

        berserkerMenuController.setGameSwitcher(() -> {
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.startTimer();
        });

        survivalMenuController.setGameSwitcher(() -> {
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.startTimer();
        });

        gameMainMenuController.setGameSwitcher(() -> {
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.startTimer();
        });

        gameMainMenuController.setRestartSwitcher(() -> {
            start(primaryStage);
        });

        gameOverController.setGameSwitcher(() -> {
            //switchToRoot(scene, mainMenuRoot, primaryStage);
            start(primaryStage);
        });
        
        // deploy the main onto the stage
        gameRoot.requestFocus();
        primaryStage.setScene(scene);
        primaryStage.show();
    

    }

    @Override
    public void start(Stage primaryStage)  {
        try {
            //  Block of code to try
            startGame(primaryStage);

        }
        catch(Exception e) {
            //  Block of code to handle errors
        }
    }

    @Override
    public void stop(){
        // wrap up activities when exit program
        mainController.terminate();
    }

    /**
     * switch to a different Root
     */
    private void switchToRoot(Scene scene, Parent root, Stage stage){
        scene.setRoot(root);
        root.requestFocus();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
