package screen;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.Color;
import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import crud.Crud;
import ldscreen.*;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;

import color.HexGen;

public class MainScreen {
	
	private StackPane mainPane;
	private Stage mainStage;
	
	private Image userImage;
	private ImageView imageHolder;
	private HBox imageBox;
	
	private HBox nameHolder;
	
	private Button openFile;
	private Button getHex;
	private Button saveHex;
	private Button loadHex;
	private Button clearImage;
	
	private Label averageHex;
	private Label average;
	private Label compHex;
	private Label comp;
	
	private Label name;
	
	private HexGen hex;
	
	private Scene mainScene;
	
	private TextField nameField;
	private FileWork fileHolder;
	
	private Slider red;
	private Slider green;
	private Slider blue;
	
	private Label rLabel;
	private Label gLabel;
	private Label bLabel;
	
	private Button help;


	public MainScreen() {
		//file workaround for getting hex from a file
		fileHolder = new FileWork();
		hex = new HexGen(new File("images/white.png"));
		
		mainPane = new StackPane();
		
		//Default image and sizing
		userImage = new Image("file:images/white.png");
		imageHolder = new ImageView(userImage);
		imageHolder.setPreserveRatio(true);
		
		imageBox = new HBox(imageHolder);
		imageBox.setMaxSize(500, 500);
		
		imageHolder.fitWidthProperty().bind(Bindings.min(imageHolder.getImage().widthProperty(), imageBox.widthProperty()));
		imageHolder.fitHeightProperty().bind(Bindings.min(imageHolder.getImage().heightProperty(), imageBox.heightProperty()));
		
		imageBox.setAlignment(Pos.CENTER);
		
		mainPane.getChildren().add(imageBox);
		
		
		//Default Hex and sizing
		averageHex = new Label("Average\nHex");
		averageHex.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000;");
		averageHex.setFont(Font.font("Nunito", 48.0));
		averageHex.setAlignment(Pos.CENTER);
		averageHex.setMinSize(300, 300);
		
		average = new Label("Average Hex");
		average.setFont(Font.font("Nunito", 48.0));
		average.setVisible(false);
		
		compHex = new Label("Compliment\nColor");
		compHex.setStyle("-fx-background-color: #000000; -fx-border-color: #FFFFFF;-fx-text-fill: #FFFFFF");
		compHex.setFont(Font.font("Nunito", 48.0));
		compHex.setAlignment(Pos.CENTER);
		compHex.setMinSize(300, 300);
		
		comp = new Label("Compliment Color");
		comp.setFont(Font.font("Nunito", 48.0));
		comp.setVisible(false);
		
		
		mainPane.getChildren().addAll(averageHex, compHex, average, comp);
		openFile = new Button("Load Image");
		
		openFile.setOnAction(e -> {
			loadImage();
			getHex.setDisable(false);
			if (comp.isDisabled() == false) {
				saveHex.setDisable(false);
			}
		});
		
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
			
			if (imageHolder.getImage() != null) {
				new Crud().save(averageHex.getText(), hex.getCompliment(averageHex.getText()), nameField.getText(), URLDecoder.decode(imageHolder.getImage().getUrl(), StandardCharsets.UTF_8));
				} else {
				new Crud().save(averageHex.getText(), hex.getCompliment(averageHex.getText()), nameField.getText());
			}
			
		});
		
		
		mainPane.getChildren().add(saveHex);
		
		loadHex = new Button("View Color List");
		loadHex.setAlignment(Pos.CENTER);
		
		loadHex.setOnAction(e -> {
			new LDScreen(mainStage, mainScene, imageHolder, averageHex, saveHex, getHex, fileHolder, nameField, compHex, red, green, blue, imageBox);
		});
		
		mainPane.getChildren().add(loadHex);
		
		name = new Label("Color Name:");
		nameField = new TextField("");
		nameHolder = new HBox();
		nameHolder.getChildren().addAll(name,nameField, saveHex);
		nameHolder.setMaxHeight(name.getHeight());
		nameHolder.setMaxWidth(290);
		mainPane.getChildren().add(nameHolder);
		
		clearImage = new Button("Clear Image");
		
		clearImage.setOnAction(e ->{
			imageHolder.setImage(null);
			saveHex.setDisable(false);
			getHex.setDisable(true);
			average.setVisible(true);
			comp.setVisible(true);
			if (averageHex.getText() == ("Average\nHex")) {
				averageHex.setText("#FFFFFF");
				compHex.setText("#000000");
			}
		});
		
		mainPane.getChildren().add(clearImage);
		
		help = new Button("?");
		help.setShape(new Circle(1));
		help.setAlignment(Pos.CENTER);
		help.setMinWidth(42);
		
		mainPane.getChildren().add(help);
		
		scrolls();
		positions();
		
		showScreen();
		style();
	}
	
	private void loadImage() {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg");
	    fileChooser.setFileFilter(filter);
	    int returnVal = fileChooser.showOpenDialog(null);
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	fileHolder.setFile(fileChooser.getSelectedFile());
	    	imageHolder.setImage(new Image(fileHolder.getFile().toURI().toString()));
	    	imageHolder.fitWidthProperty().bind(Bindings.min(imageHolder.getImage().widthProperty(), imageBox.widthProperty()));
			imageHolder.fitHeightProperty().bind(Bindings.min(imageHolder.getImage().heightProperty(), imageBox.heightProperty()));
	    	
	    }

	}
	
	private void genHex() {
		average.setVisible(true);
		comp.setVisible(true);
		hex = new HexGen(fileHolder.getFile());
    	averageHex.setStyle("-fx-background-color: " + hex.getHex() +  ";-fx-border-color: " + hex.getCompliment() +"; -fx-text-fill: " + hex.getCompliment());
    	averageHex.setText(hex.getHex());
    	
    	compHex.setText(hex.getCompliment());
    	compHex.setStyle("-fx-background-color: " + hex.getCompliment() +  ";-fx-border-color: " + hex.getHex() +"; -fx-text-fill: " + hex.getHex());
    	
    	Color rgb = Color.decode(hex.getHex());
		
		red.setValue(rgb.getRed());
		green.setValue(rgb.getGreen());
		blue.setValue(rgb.getBlue());
    	
	}
		
	private void showScreen() {
		mainScene = new Scene(mainPane, 1535, 790);
		mainStage = new Stage();
		mainStage.setTitle("Average Color Picker - By Marcos Galindo");
        
		mainStage.setScene(mainScene);
		mainStage.setMaximized(true);
		mainStage.show();
	}
	
	private void scrolls() {
		red = new Slider();
		red.setMaxWidth(250);
		red.setTranslateX(-470);
		red.setTranslateY(130);
		red.setMax(255);
		red.setMin(0);
		red.setMax(255);
		red.setBlockIncrement(1);
		red.setShowTickLabels(true);
		red.setValue(255);
		
		rLabel = new Label("Red");
		rLabel.setTranslateX(-610);
		rLabel.setTranslateY(120);
		
		red.valueProperty().addListener(e -> {
			changeHex();
			saveHex.setDisable(false);
		});
		
		green = new Slider();
		green.setMaxWidth(250);
		green.setTranslateX(-470);
		green.setTranslateY(160);
		green.setMin(0);
		green.setMax(255);
		green.setBlockIncrement(1);
		green.setShowTickLabels(true);
		green.setValue(255);
		
		gLabel = new Label("Green");
		gLabel.setTranslateX(-610);
		gLabel.setTranslateY(150);
		
		green.valueProperty().addListener(e -> {
			changeHex();
			saveHex.setDisable(false);
		});
		
		blue = new Slider();
		blue.setMaxWidth(250);
		blue.setTranslateX(-470);
		blue.setTranslateY(190);
		blue.setMin(0);
		blue.setMax(255);
		blue.setBlockIncrement(1);
		blue.setShowTickLabels(true);
		blue.setValue(255);
		
		bLabel = new Label("Blue");
		bLabel.setTranslateX(-610);
		bLabel.setTranslateY(180);
		
		blue.valueProperty().addListener(e -> {
			changeHex();
			saveHex.setDisable(false);
		});
		
		
		mainPane.getChildren().addAll(red,green,blue,rLabel,gLabel,bLabel);
	}
	
	private void changeHex() {
		average.setVisible(true);
		comp.setVisible(true);
		int r = (int)red.getValue();
		int g = (int)green.getValue();
		int b = (int)blue.getValue();
		
		String hexS = String.format("#%02X%02X%02X", r, g, b);
		
		averageHex.setStyle("-fx-background-color: " + hexS +  ";-fx-border-color: " + hex.getCompliment(hexS) +"; -fx-text-fill: " + hex.getCompliment(hexS));
		averageHex.setText(hexS);
		compHex.setStyle("-fx-background-color: " + hex.getCompliment(hexS) +  ";-fx-border-color: " + hexS +"; -fx-text-fill: " + hexS);
		compHex.setText(hex.getCompliment(hexS));
	}
	
	private void positions() {
		//position of all objects beside the sliders
		imageBox.setTranslateY(-100);
		
		averageHex.setTranslateX(-475);
		averageHex.setTranslateY(-50);
		average.setTranslateX(-475);
		average.setTranslateY(-250);
		
		compHex.setTranslateX(475);
		compHex.setTranslateY(-50);
		comp.setTranslateX(475);
		comp.setTranslateY(-250);
		
		openFile.setTranslateX(-100);
		openFile.setTranslateY(165);
		
		loadHex.setTranslateX(475);
		loadHex.setTranslateY(200);
		
		//getHex.setTranslateX(0);
		getHex.setTranslateY(225);
		
		nameHolder.setTranslateY(290);
		
		clearImage.setTranslateX(100);
		clearImage.setTranslateY(165);
		
		help.setTranslateX(-725);
		help.setTranslateY(-365);
	}
	
	private void style() {
		String buttonStyle = "-fx-font-size:20";
		getHex.setStyle(buttonStyle);
		openFile.setStyle(buttonStyle);
		clearImage.setStyle(buttonStyle);
		loadHex.setStyle(buttonStyle);
		help.setStyle(buttonStyle);
		
		loadHex.setMinHeight(loadHex.getWidth());
	}
	
}
