package unsw.loopmania;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GameOverController {

    private MenuSwitcher gameSwitcher;

    @FXML
    Button restartGame;

    @FXML
    Button exitGame;

    @FXML
    Label gameOver;

    public GameOverController() {
        this.restartGame = new Button();
        this.exitGame = new Button();
        this.gameOver = new Label();
    }

    public void setGameSwitcher(MenuSwitcher gameSwitcher) {
        this.gameSwitcher = gameSwitcher;
    }

    /**
     * facilitates switching to main game upon button click
     * @throws IOException
     */
    @FXML
    private void switchToGame() throws IOException {
        gameSwitcher.switchMenu();
    }


    /**
     * close the window.
     * @param event
     */

    @FXML
    public void endGame(ActionEvent event) {
        Stage stage = (Stage) exitGame.getScene().getWindow();
        stage.close();
    }

    
    

}
