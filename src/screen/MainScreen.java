package screen;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import crud.Crud;
import ldscreen.LDScreen;

import color.HexGen;

public class MainScreen {
	
	private Pane mainPane;
	private Stage mainStage;
	
	private Image userImage;
	private ImageView imageHolder;
	
	private Button openFile;
	private Button getHex;
	private Button saveHex;
	private Button loadHex;

	
	private Label averageHex;
	
	private HexGen hex;
	private File fileHolder;
	
	private Scene mainScene;


	public MainScreen() {
		
		mainPane = new Pane();
		
		//Default image and sizing
		userImage = new Image("file:images/white.png");
		imageHolder = new ImageView(userImage);
		imageHolder.setFitWidth(300);
		imageHolder.setFitHeight(300);
		imageHolder.setX(620);
		imageHolder.setY(100);
		mainPane.getChildren().add(imageHolder);
		
		
		//Default Hex and sizing
		averageHex = new Label("Placeholder");
		averageHex.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000;");
		averageHex.setFont(Font.font("Nunito", 48.0));
		averageHex.setAlignment(Pos.CENTER);
		averageHex.setMinSize(250, 250);
		averageHex.relocate(100, 100);
		mainPane.getChildren().add(averageHex);
		openFile = new Button("Load Image");
		
		openFile.setOnAction(e -> {
			loadImage();
			saveHex.setDisable(false);
			getHex.setDisable(false);
		});
		
		openFile.relocate(620, 500);
		mainPane.getChildren().add(openFile);
		
		getHex = new Button("Generate Hex");
		getHex.relocate(720, 550);
		getHex.setDisable(true);
		getHex.setOnAction(e -> {
			genHex();
		});
		
		mainPane.getChildren().add(getHex);
		
		
		
		saveHex = new Button("Save Color");
		saveHex.setDisable(true);
		saveHex.setOnAction(e -> {
			new Crud().save(averageHex.getText(), hex.getCompliment(averageHex.getText()), "Test", imageHolder.getImage().getUrl());
		});
		
		saveHex.relocate(820, 500);
		mainPane.getChildren().add(saveHex);
		
		loadHex = new Button("View Color List");
		loadHex.setAlignment(Pos.CENTER);
		loadHex.relocate(1020, 500);
		
		loadHex.setOnAction(e -> {
			new LDScreen(mainStage, mainScene, imageHolder, averageHex, getHex, saveHex, fileHolder);
		});
		
		mainPane.getChildren().add(loadHex);

		
		
		showScreen();
	}
	
	private void loadImage() {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg");
	    fileChooser.setFileFilter(filter);
	    int returnVal = fileChooser.showOpenDialog(null);
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	fileHolder = fileChooser.getSelectedFile();
	    	imageHolder.setImage(new Image(fileHolder.toURI().toString()));
	    	
	    }

	}
	
	private void genHex() {
		System.out.println(imageHolder.getImage().getUrl().substring(6));
		fileHolder = new File(imageHolder.getImage().getUrl().substring(6));
		hex = new HexGen(fileHolder);
    	averageHex.setStyle("-fx-background-color: " + hex.getHex() +  ";-fx-border-color: " + hex.getCompliment());
    	averageHex.setText(hex.getHex());
    	averageHex.setTextFill(Color.web(hex.getCompliment()));
	}
		
	private void showScreen() {
		mainScene = new Scene(mainPane,1920,1080);
		mainStage = new Stage();
		mainStage.setTitle("Average Color Picker");
		mainStage.setScene(mainScene);
		mainStage.setMaximized(true);
		mainStage.show();
	}
}
