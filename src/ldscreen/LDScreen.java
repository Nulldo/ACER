package ldscreen;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import color.HexGen;
import crud.Crud;

import screen.MainScreen;

public class LDScreen {
	
	private Button back;
	private Button loadColor;
	
	private Pane mainPane;
	private Scene mainScene;
	
	public LDScreen(Stage mainStage, Scene mainScene, ImageView imageHolder, Label averageHex, Button saveHex, Button getHex, File fileHolder) {
		mainPane = new Pane();
		
		this.mainScene = mainScene;
		
		back = new Button("Back");
		back.setOnAction(e ->{
			mainStage.setScene(mainScene);
		});
		
		mainPane.getChildren().add(back);
		
		loadColor = new Button("Load Color");
		loadColor.relocate(600, 600);
		
		loadColor.setOnAction(e -> {
			loadColor(imageHolder, averageHex, fileHolder);
			mainStage.setScene(mainScene);
			saveHex.setDisable(false);
			getHex.setDisable(false);

		});
		
		mainPane.getChildren().add(loadColor);

		
		showScreen(mainStage);
	}
	
	private void showScreen(Stage mainStage) {
		Scene scene = new Scene(mainPane,1920,1080);
		mainStage.setScene(scene);
	}
	
	private void loadColor(ImageView imageHolder, Label averageHex, File fileHolder) {
		Crud database = new Crud();
		ResultSet resultSet = database.load();
		try {
			String hex = null;
			String path = null;
			String comp = null;
			
			while (resultSet.next()) {
				 hex = resultSet.getString("hex");
				 path = resultSet.getString("image_path");
				 comp = resultSet.getString("compliment");
			}
			
			imageHolder.setImage( new Image(path));
			System.out.println(path);
			fileHolder = new File(path);
			averageHex.setText(hex);
			averageHex.setStyle("-fx-text-fill: "+ comp +";-fx-background-color: " + hex +  ";-fx-border-color: " + comp + ";");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		database.close();
		
	}
}
