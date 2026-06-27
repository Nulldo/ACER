package ldscreen;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import crud.Crud;


public class LDScreen {
	
	private Button back;
	
	private StackPane mainPane;
	private ScrollPane scrollPane;
	private BorderPane bPane;
	private Scene mainScene;
	private Stage mainStage;
	
	private VBox mainV;
	private ImageView imageHolder;
	private Label averageHex;
	private Button saveHex;
	private Button getHex;
	private FileWork fileHolder;
	
	public LDScreen(Stage mainStage, Scene mainScene, ImageView imageHolder, Label averageHex, Button saveHex, Button getHex, FileWork fileHolder) {
		
		scrollPane = new ScrollPane();
		mainPane = new StackPane();
		bPane = new BorderPane();
		mainPane.getChildren().add(bPane);
		
		scrollPane.setContent(mainPane);
		scrollPane.setFitToWidth(true); 
		
		
		this.mainScene = mainScene;
		this.mainStage = mainStage;
		this.imageHolder = imageHolder;
		this.averageHex = averageHex;
		this.saveHex = saveHex;
		this.getHex = getHex;
		this.fileHolder = fileHolder;
		
		back = new Button("Back");
		back.setOnAction(e ->{
			mainStage.setScene(mainScene);
			mainStage.setMaximized(true);
		});
		
		back.toFront();
		
		mainPane.getChildren().add(back);
		back.setTranslateX(-735);
		back.setTranslateY(-370);
		
		mainV = new VBox();
		mainV.setMaxWidth(650);
		mainV.setAlignment(Pos.CENTER);
		
		loadList(mainV);
		bPane.setCenter(mainV);
		
		

		
		showScreen(mainStage);
		mainStage.setMaximized(true);
	}
	
	private void showScreen(Stage mainStage) {
		Scene scene = new Scene(mainPane, 1550, 790);
		mainStage.setScene(scene);
	}
	
	private void loadColor(ImageView imageHolder, Label averageHex, FileWork fileHolder, int id) {
		Crud database = new Crud();
		ResultSet resultSet = database.load(id);
		try {
			String hex = "";
			String path = "";
			String comp = "";
			
			while (resultSet.next()) {
				 hex = resultSet.getString("hex");
				 path = resultSet.getString("image_path");
				 comp = resultSet.getString("compliment");
			}
			
			imageHolder.setImage( new Image(path));
			fileHolder.setFile( new File(path.substring(6)));
			
			averageHex.setText(hex);
			averageHex.setStyle("-fx-text-fill: "+ comp +";-fx-background-color: " + hex +  ";-fx-border-color: " + comp + ";");
			
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
				 label.setMinWidth(650);
				 label.setAlignment(Pos.CENTER);
				 
				 Label color = new Label(hex);
				 color.setStyle("-fx-text-fill: "+ comp +";-fx-background-color: " + hex +  ";-fx-border-color: " + comp + ";");
				 color.setFont(Font.font("Nunito", 48.0));
				 color.setAlignment(Pos.CENTER);
				 color.setMinWidth(300);
				 color.setMinHeight(300);
				 
				 ImageView image = new ImageView(new Image(path));
				 image.setFitWidth(300);
				 image.setFitHeight(300);
				 
				 Button loadColor = new Button("Load Color");
					
				loadColor.setOnAction(e -> {
					loadColor(imageHolder, averageHex, fileHolder, id);
					mainStage.setScene(mainScene);
					saveHex.setDisable(false);
					getHex.setDisable(false);

				});
				 
				 HBox hbox = new HBox();
				 hbox.getChildren().addAll(color,image);
				 hbox.setSpacing(50);
				 
				 VBox vbox = new VBox();
				 vbox.getChildren().addAll(label, hbox, loadColor);
				 
				 mainV.getChildren().add(vbox);
				 
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		database.close();
	}
}
