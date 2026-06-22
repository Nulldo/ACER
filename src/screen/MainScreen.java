package screen;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javafx.scene.paint.Color;

import color.HexGen;

public class MainScreen {
	
	private Pane mainPane;
	private Stage mainStage;
	private Image userImage;
	private ImageView imageHolder;
	private Button openFile;
	//private Text averageHex;
	private Label averageHex;


	public MainScreen() {
		
		mainPane = new Pane();
		
		userImage = new Image("file:images/white.png");
		imageHolder = new ImageView(userImage);
		imageHolder.setFitWidth(300);
		imageHolder.setFitHeight(300);
		imageHolder.setX(620);
		imageHolder.setY(100);
		mainPane.getChildren().add(imageHolder);
		
		openFile = new Button("Load Image");
		openFile.setOnAction(e -> {
			loadImage();
		});
		
		openFile.relocate(620, 500);
		mainPane.getChildren().add(openFile);
		
		averageHex = new Label("Placeholder");
		averageHex.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000;");
		averageHex.setFont(Font.font("Nunito", 48.0));
		averageHex.setAlignment(Pos.CENTER);
		averageHex.setMinSize(250, 250);
		averageHex.relocate(100, 100);
		mainPane.getChildren().add(averageHex);
		
		showScreen();
	}
	
	public void loadImage() {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg");
	    fileChooser.setFileFilter(filter);
	    int returnVal = fileChooser.showOpenDialog(null);
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	File fileHolder = fileChooser.getSelectedFile();
	    	imageHolder.setImage(new Image(fileHolder.toURI().toString()));
	    	HexGen hex = new HexGen(fileHolder);
	    	averageHex.setStyle("-fx-background-color: " + hex.getHex() +  ";-fx-border-color: " + hex.getCompliment());
	    	averageHex.setText(hex.getHex());
	    	averageHex.setTextFill(Color.web(hex.getCompliment()));
	    }

	}
	
	private void showScreen() {
		Scene scene = new Scene(mainPane,1920,1080);
		mainStage = new Stage();
		mainStage.setTitle("Average Color Picker");
		mainStage.setScene(scene);
		mainStage.setMaximized(true);
		mainStage.show();
	}
}
