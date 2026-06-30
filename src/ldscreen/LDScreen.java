package ldscreen;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.Color;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import crud.Crud;


public class LDScreen {
	
	private Button back;
	
	private ScrollPane mainPane;
	private StackPane stackPane;
	private Scene mainScene;
	private Stage mainStage;
	
	private VBox mainV;
	private ImageView imageHolder;
	private Label averageHex;
	private Label compHex;
	private Button saveHex;
	private Button getHex;
	private FileWork fileHolder;
	private TextField nameField;
	private Slider red;
	private Slider green;
	private Slider blue;
	private HBox imageBox;
	
	
	public LDScreen(Stage mainStage, Scene mainScene, ImageView imageHolder, Label averageHex, Button saveHex, Button getHex, FileWork fileHolder, TextField nameField, Label compHex, Slider red, Slider green, Slider blue, HBox imageBox) {
		
		mainPane = new ScrollPane();
		
		stackPane = new StackPane();
		
		mainPane.setFitToWidth(true);
		//mainPane.setFitToHeight(true);		
		
		this.mainScene = mainScene;
		this.mainStage = mainStage;
		this.imageHolder = imageHolder;
		this.averageHex = averageHex;
		this.saveHex = saveHex;
		this.getHex = getHex;
		this.fileHolder = fileHolder;
		this.nameField = nameField;
		this.compHex = compHex;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.imageBox = imageBox;
		
		back = new Button("Back");
		back.setOnAction(e ->{
			mainStage.setScene(mainScene);
			mainStage.setMaximized(true);
		});
		
		stackPane.getChildren().add(back);
		back.setTranslateX(-735);
		
		 
		mainV = new VBox();
		mainV.setMaxWidth(650);
		mainV.setAlignment(Pos.CENTER);
		
		loadList(mainV);
		mainV.setSpacing(20);
		mainV.setPadding(new Insets(10,10,10,10));
		
		stackPane.getChildren().add(mainV);
		

		mainPane.setContent(stackPane);
		
		showScreen(mainStage);
		back.setTranslateY(mainV.getChildren().size()*-175);
		mainPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
	}
	
	private void showScreen(Stage mainStage) {
		Scene scene = new Scene(mainPane, 1535, 790);
		mainStage.setScene(scene);
	}
	
	private void loadColor(ImageView imageHolder, Label averageHex, FileWork fileHolder, int id, Button getHex, Stage mainStage, Scene mainScene, Button saveHex, Slider red, Slider green, Slider blue, HBox imageBox) {
		Crud database = new Crud();
		ResultSet resultSet = database.load(id);
		try {
			String hex = "";
			String path = null;
			String comp = "";
			String name = "";
			
			while (resultSet.next()) {
				 hex = resultSet.getString("hex");
				 comp = resultSet.getString("compliment");
				 name = resultSet.getString("name");
				 if (resultSet.getString("image_path") !=  null) {
					 path = resultSet.getString("image_path");
				 }
			}
			if (path != null) {
				imageHolder.setImage( new Image(path));
				getHex.setDisable(false);
				fileHolder.setFile( new File(path.substring(6)));

			} else {
				getHex.setDisable(true);
				imageHolder.setImage(null);				
			}
			saveHex.setDisable(false);
			
			averageHex.setText(hex);
			averageHex.setStyle("-fx-text-fill: "+ comp +";-fx-background-color: " + hex +  ";-fx-border-color: " + comp + ";");
			
			compHex.setText(comp);
			compHex.setStyle("-fx-text-fill: "+ hex +";-fx-background-color: " + comp +  ";-fx-border-color: " + hex + ";");
			nameField.setText(name);
			
			Color rgb = Color.decode(hex);
			
			red.setValue(rgb.getRed());
			green.setValue(rgb.getGreen());
			blue.setValue(rgb.getBlue());
			
			imageHolder.fitWidthProperty().bind(Bindings.min(imageHolder.getImage().widthProperty(), imageBox.widthProperty()));
			imageHolder.fitHeightProperty().bind(Bindings.min(imageHolder.getImage().heightProperty(), imageBox.heightProperty()));
			
			mainStage.setScene(mainScene);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		database.close();
		
	}
	
	private void loadList(VBox mainV) {
		Crud database = new Crud();
		ResultSet resultSet = database.loadAll();
		try {
			String hex = "";
			String path = "";
			String name = "";
			String comp = "";
			
			while (resultSet.next()) {
				hex = resultSet.getString("hex");
				path = resultSet.getString("image_path");
				name = resultSet.getString("name");
				int id = resultSet.getInt("color_id");
				comp = resultSet.getString("compliment");
				 
				Label label = new Label(name);
				label.setStyle("-fx-background-color: #FFFFFF");
				
				label.setAlignment(Pos.CENTER);
				
				Label color = new Label(hex);
				
				color.setStyle("-fx-text-fill: "+ comp +";-fx-background-color: " + hex +  ";-fx-border-color: " + comp + ";");
				color.setFont(Font.font("Nunito", 48.0));
				color.setAlignment(Pos.CENTER);
				color.setMinWidth(300);
				color.setMinHeight(300);
				
				ImageView image = new ImageView();
				
				HBox hbox = new HBox();
				hbox.setMaxWidth(300);
				hbox.setMaxHeight(300);
				
				 
				Button loadColor = new Button("Load");
				loadColor.setDefaultButton(false);
				loadColor.setOnAction(e -> {
					loadColor(imageHolder, averageHex, fileHolder, id, getHex, mainStage, mainScene, saveHex, red, green, blue, imageBox);
				});
				
				Button deleteColor = new Button("Delete");
				
				
				VBox buttonBox = new VBox();
				buttonBox.getChildren().addAll(loadColor, deleteColor);
				buttonBox.setMinWidth(50);
				buttonBox.setSpacing(250);
				buttonBox.setAlignment(Pos.CENTER_RIGHT);
				
				VBox vbox = new VBox();
				vbox.getChildren().addAll(label, hbox);
				vbox.setMaxHeight(330);
				vbox.setMaxWidth(330);
				
				if (path != null) {
					image = new ImageView(new Image(path));
					image.setPreserveRatio(true);
					image.fitHeightProperty().bind(Bindings.min(image.getImage().heightProperty(), 300));
					image.fitWidthProperty().bind(Bindings.min(image.getImage().widthProperty(), 300));
				}
				hbox.getChildren().addAll(color,image,buttonBox);
				
				hbox.setAlignment(Pos.CENTER);
				hbox.setSpacing(50);
				
				label.setMinWidth(750);
								 
				vbox.setStyle("-fx-border-color: " + hex +  ";-fx-border-width: 5; -fx-background-color: " + comp +  ";");
				vbox.setAlignment(Pos.CENTER);
				vbox.setPadding(new Insets(0,10,10,10));
				
				mainV.getChildren().add(vbox);
				deleteColor.setOnAction(e -> {
					new Crud().delete(id);
					mainV.getChildren().remove(vbox);
					back.setTranslateY(mainV.getChildren().size()*-175);					
				});
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		database.close();
	}
}
