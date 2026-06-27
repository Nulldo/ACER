package screen;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import crud.Crud;
import ldscreen.*;

import color.HexGen;

public class MainScreen {
	
	private StackPane mainPane;
	private Stage mainStage;
	
	private Image userImage;
	private ImageView imageHolder;
	
	private Button openFile;
	private Button getHex;
	private Button saveHex;
	private Button loadHex;

	
	private Label averageHex;
	
	private Label name;
	
	private HexGen hex;
	//private File fileHolder;
	
	private Scene mainScene;
	
	private TextField nameField;
	private FileWork fileHolder;


	public MainScreen() {
		//file workaround for getting hex from a file
		fileHolder = new FileWork();
		
		mainPane = new StackPane();
		
		//Default image and sizing
		userImage = new Image("file:images/white.png");
		imageHolder = new ImageView(userImage);
		imageHolder.setFitWidth(300);
		imageHolder.setFitHeight(300);
		imageHolder.setTranslateY(-200);
		imageHolder.setY(100);
		mainPane.getChildren().add(imageHolder);
		
		
		//Default Hex and sizing
		averageHex = new Label("Placeholder");
		averageHex.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000;");
		averageHex.setFont(Font.font("Nunito", 48.0));
		averageHex.setAlignment(Pos.CENTER);
		averageHex.setMinSize(250, 250);
		averageHex.setTranslateX(-400);
		averageHex.setTranslateY(-200);
		
		mainPane.getChildren().add(averageHex);
		openFile = new Button("Load Image");
		
		openFile.setOnAction(e -> {
			loadImage();
			getHex.setDisable(false);
		});
		
		openFile.setTranslateY(20);
		openFile.setTranslateX(-100);
		
		mainPane.getChildren().add(openFile);
		
		getHex = new Button("Generate Hex");
		
		getHex.setDisable(true);
		getHex.setOnAction(e -> {
			genHex();
			saveHex.setDisable(false);
		});
		
		mainPane.getChildren().add(getHex);
		
		
		
		
		saveHex = new Button("Save Color");
		saveHex.setDisable(true);
		saveHex.setOnAction(e -> {
			new Crud().save(averageHex.getText(), hex.getCompliment(averageHex.getText()), nameField.getText(), imageHolder.getImage().getUrl());
		});
		
		saveHex.setTranslateY(20);
		saveHex.setTranslateX(100);
		mainPane.getChildren().add(saveHex);
		
		loadHex = new Button("View Color List");
		loadHex.setAlignment(Pos.CENTER);
		loadHex.relocate(1020, 500);
		
		loadHex.setOnAction(e -> {
			new LDScreen(mainStage, mainScene, imageHolder, averageHex, getHex, saveHex, fileHolder);
		});
		
		loadHex.setTranslateY(200);
		loadHex.setTranslateX(350);
		
		mainPane.getChildren().add(loadHex);
		
		name = new Label("Enter Name:");
		nameField = new TextField();
		HBox nameHolder = new HBox();
		nameHolder.getChildren().addAll(name,nameField);
		nameHolder.setMaxHeight(name.getHeight());
		nameHolder.setMaxWidth(250);
		nameHolder.setTranslateY(60);
		mainPane.getChildren().add(nameHolder);

		
		
		showScreen();
	}
	
	private void loadImage() {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg");
	    fileChooser.setFileFilter(filter);
	    int returnVal = fileChooser.showOpenDialog(null);
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	fileHolder.setFile(fileChooser.getSelectedFile());
	    	imageHolder.setImage(new Image(fileHolder.getFile().toURI().toString()));
	    	
	    }

	}
	
	private void genHex() {
		
		hex = new HexGen(fileHolder.getFile());
    	averageHex.setStyle("-fx-background-color: " + hex.getHex() +  ";-fx-border-color: " + hex.getCompliment());
    	averageHex.setText(hex.getHex());
    	averageHex.setTextFill(Color.web(hex.getCompliment()));
	}
		
	private void showScreen() {
		mainScene = new Scene(mainPane, 1550, 790);
		mainStage = new Stage();
		mainStage.setTitle("Average Color Picker");
        
		mainStage.setScene(mainScene);
		mainStage.setMaximized(true);
		mainStage.show();
	}
}
