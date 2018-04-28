package BombermanInda;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
//https://gamedevelopment.tutsplus.com/tutorials/introduction-to-javafx-for-game-development--cms-23835

public class Game extends Application {

private Pane pane;
    @Override
    public void start(Stage primaryStage)  {
        
        //size of window
        int graphicsWindowX = 500;
        int graphicsWindowY = 500;
        
        
        
        //init graphic window
        primaryStage.setTitle("Bomberman");
        pane = new Pane();    
        pane.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        primaryStage.setScene(new Scene(pane, graphicsWindowX ,graphicsWindowY));
        Render render = new Render(pane, primaryStage); 
        

        
        
        //init background
        Image background = new Image( "BombermanInda/heart.jpg" );
        
        //init world
        World world = new World(20,20, render, 80 , background); //create 20x20 playfield with 80 crates
        
        //create main char 
        Node mainCharNode = render.createGraphicsEntity(Render.GraphicsObjects.MAINCHARACTER); //create node for char
        Character mainChar = new Character(mainCharNode,0,0, true, true);   //create char on (0,0)
        world.setObject(0,0, mainChar);
        //activate movement
        mainChar.testMovement3(primaryStage,pane);

        //draw background
        render.drawBackground(world);
         
        
        
        primaryStage.show();

         
        // Gameloop
        final LongProperty lastUpdateTime = new SimpleLongProperty(0);
        new AnimationTimer(){
           @Override
           public void handle(long timestamp) {   
            long elapsedTimeMs = (timestamp - lastUpdateTime.get())/100000000000l;               
            render.drawAllMapObjects(world);
            
                //mainChar.Move(elapsedTimeMs);
                world.moveAllMoveable(elapsedTimeMs);
           }
      
        }.start();
        
    }
    
}
