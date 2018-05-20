package BombermanInda;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class CharacterMovement {

    private Stage primaryStage;
    private Render render;
    private Game game;
    private Character playerOne;
    private Character playerTwo;
    private Character playerThree;
    private Character playerFour;

    public CharacterMovement (Game game, Stage primaryStage, Render render, Character playerOne){
        this.primaryStage = primaryStage;
        this.render = render;
        this.game = game;
        this.playerOne = playerOne;
    }

    public CharacterMovement (Game game, Stage primaryStage, Render render, Character playerOne, Character playerTwo){
        this.primaryStage = primaryStage;
        this.render = render;
        this.game = game;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        TwoPlayerControl();
    }

    public CharacterMovement (Game game, Stage primaryStage, Render render, Character playerOne, Character playerTwo, Character playerThree){
        this.primaryStage = primaryStage;
        this.render = render;
        this.game = game;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.playerThree = playerThree;
    }

    public CharacterMovement (Game game, Stage primaryStage, Render render, Character playerOne, Character playerTwo, Character playerThree,
                              Character playerFour){
        this.primaryStage = primaryStage;
        this.render = render;
        this.game = game;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.playerThree = playerThree;
        this.playerFour = playerFour;
    }

    public enum CharacterObject {
        MAINCHARACTER, SECONDCHARACTER;
    }

    public void TwoPlayerControl(){


        //random keylistener for no reason
        primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {

                if(playerOne.getIsInvulnerable()) {
                    if (ke.getCode() == KeyCode.UP) {
                        playerOne.setSpeedYDirection(-1);
                        render.drawInvulnerableCharacterBack(playerOne.getNode());
                    } else if (ke.getCode() == KeyCode.DOWN) {
                        playerOne.setSpeedYDirection(1);
                        render.drawInvulnerableCharacterFront(playerOne.getNode());
                    } else if (ke.getCode() == KeyCode.LEFT) {
                        playerOne.setSpeedXDirection(-1);
                        render.drawInvulnerableCharacterLeft(playerOne.getNode());
                    } else if (ke.getCode() == KeyCode.RIGHT) {
                        playerOne.setSpeedXDirection(1);
                        render.drawInvulnerableCharacterRight(playerOne.getNode());
                    }
                }

                else if(playerTwo.getIsInvulnerable()){
                    if (ke.getCode() == KeyCode.W) {
                        playerTwo.setSpeedYDirection(-1);
                        render.drawInvulnerableCharacterBack(playerTwo.getNode());
                    } else if (ke.getCode() == KeyCode.S) {
                        playerTwo.setSpeedYDirection(1);
                        render.drawInvulnerableCharacterFront(playerTwo.getNode());
                    } else if (ke.getCode() == KeyCode.A) {
                        playerTwo.setSpeedXDirection(-1);
                        render.drawInvulnerableCharacterLeft(playerTwo.getNode());
                    } else if (ke.getCode() == KeyCode.D) {
                        playerTwo.setSpeedXDirection(1);
                        render.drawInvulnerableCharacterRight(playerTwo.getNode());
                    }
                }
                else {
                    if (ke.getCode() == KeyCode.UP) {
                        playerOne.setSpeedYDirection(-1);
                        render.drawMainCharacterBack(playerOne.getNode());
                    } else if (ke.getCode() == KeyCode.DOWN) {
                        playerOne.setSpeedYDirection(1);
                        render.drawMainCharacterFront(playerOne.getNode());
                    } else if (ke.getCode() == KeyCode.LEFT) {
                        playerOne.setSpeedXDirection(-1);
                        render.drawMainCharacterLeft(playerOne.getNode());
                    } else if (ke.getCode() == KeyCode.RIGHT) {
                        playerOne.setSpeedXDirection(1);
                        render.drawMainCharacterRight(playerOne.getNode());
                    } else if (ke.getCode() == KeyCode.W) {
                        playerTwo.setSpeedYDirection(-1);
                        render.drawSecondCharacterBack(playerTwo.getNode());
                    } else if (ke.getCode() == KeyCode.S) {
                        playerTwo.setSpeedYDirection(1);
                        render.drawSecondCharacterFront(playerTwo.getNode());
                    } else if (ke.getCode() == KeyCode.A) {
                        playerTwo.setSpeedXDirection(-1);
                        render.drawSecondCharacterLeft(playerTwo.getNode());
                    } else if (ke.getCode() == KeyCode.D) {
                        playerTwo.setSpeedXDirection(1);
                        render.drawSecondCharacterRight(playerTwo.getNode());
                    }
                }
                //layBomb
                if (ke.getCode() == KeyCode.T) {
                    try {
                        playerTwo.layBomb();
                    } catch (InterruptedException e) {
                        //TODO
                    }
                }

                //layBomb
                else if (ke.getCode() == KeyCode.PERIOD) {
                    try {
                        playerOne.layBomb();
                    } catch (InterruptedException e) {
                        //TODO
                    }
                }

                else if(ke.getCode() == KeyCode.ESCAPE) {
                    game.endGame();
                    game.gameMenu(primaryStage, game.getPane());
                }
            }


        });

        primaryStage.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.UP || ke.getCode() == KeyCode.DOWN) {
                    //check to make releasing keys not stutter player if he is already moving the other way
                    if((ke.getCode()==KeyCode.UP&&playerOne.getSpeedY()<0)||(ke.getCode()==KeyCode.DOWN&&playerOne.getSpeedY()>0)) {
                        playerOne.setSpeedYDirection(0);
                    }
                        
                }
                if (ke.getCode() == KeyCode.LEFT || ke.getCode() == KeyCode.RIGHT) {
                    if((ke.getCode()==KeyCode.LEFT&&playerOne.getSpeedX()<0)||(ke.getCode()==KeyCode.RIGHT&&playerOne.getSpeedX()>0)) {
                        playerOne.setSpeedXDirection(0);
                    }
                }
                if (ke.getCode() == KeyCode.W || ke.getCode() == KeyCode.S) {
                    if((ke.getCode()==KeyCode.W&&playerTwo.getSpeedY()<0)||(ke.getCode()==KeyCode.S&&playerTwo.getSpeedY()>0)) {
                        playerTwo.setSpeedYDirection(0);
                    }
                }
                
                if (ke.getCode() == KeyCode.A || ke.getCode() == KeyCode.D) {
                    if((ke.getCode()==KeyCode.A&&playerTwo.getSpeedX()<0)||(ke.getCode()==KeyCode.D&&playerTwo.getSpeedX()>0)) {
                        playerTwo.setSpeedXDirection(0);
                    }
                }


            }
        });

    }
}
