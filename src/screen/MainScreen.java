package screen;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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
import java.util.function.UnaryOperator;

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
	
	private TextField rTF;
	private TextField gTF;
	private TextField bTF;
	
	UnaryOperator<TextFormatter.Change> rFilter;
	UnaryOperator<TextFormatter.Change> gFilter;
	UnaryOperator<TextFormatter.Change> bFilter;
	
	private Button help;
	private Button left;
	private Button right;
	private Button back;
	
	private ImageView display;
	
	private Label explanation;
	
	private int counter = 0;

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
		name.setStyle("-fx-text-fill: #5B3D6F");
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
		
		help.setOnAction(e -> {
			left.setVisible(true);
			right.setVisible(true);
			display.setVisible(true);
			help.setVisible(false);
			back.setVisible(true);
			left.setDisable(true);
			changeText(counter);
			explanation.setVisible(true);
		});
		
		scrolls();
		
		ImageView lArrow = new ImageView(new Image("file:images/lArrow.png"));
		lArrow.setFitHeight(50);
		lArrow.setFitWidth(50);
		
		left = new Button();
		left.setGraphic(lArrow);
		left.setOnAction(e ->{
			right.setDisable(false);
			counter -= 1;
			changeText(counter);
			if (counter == 0) {
				left.setDisable(true);
			}
		});
		
		
		ImageView rArrow = new ImageView(new Image("file:images/rArrow.png"));
		rArrow.setFitHeight(50);
		rArrow.setFitWidth(50);
		
		right = new Button();
		right.setGraphic(rArrow);
		right.setOnAction(e ->{
			left.setDisable(false);
			counter += 1;
			changeText(counter);
			if (counter == 5) {
				right.setDisable(true);
			}
		});
		
		left.setVisible(false);
		right.setVisible(false);
		
		display = new ImageView(new Image("file:images/display.png"));
		display.setVisible(false);
		display.setFitHeight(790);
		display.setFitWidth(1532);
		display.setTranslateX(-1);
		
		back = new Button("X");
		back.setShape(new Circle(1));
		back.setAlignment(Pos.CENTER);
		back.setFont(Font.font("Ariel",20));
		back.setMinWidth(42);
		back.setMinHeight(42);
		back.setVisible(false);
		
		back.setOnAction(e ->{
			left.setVisible(false);
			right.setVisible(false);
			right.setDisable(false);
			display.setVisible(false);
			help.setVisible(true);
			back.setVisible(false);
			counter = 0;
			explanation.setVisible(false);
		});
		explanation = new Label();
		explanation.setFont(Font.font("Nunito",25));
		explanation.setStyle("-fx-background-color: #A4C290");
		mainPane.getChildren().addAll(display,left,right,help,back,explanation);
		
		
		
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
		filters();
		
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
		rLabel.setStyle("-fx-text-fill: #5B3D6F");		
		
		rTF = new TextField("255");
		rTF.setTranslateX(-320);
		rTF.setTranslateY(120);
		rTF.setStyle("-fx-text-fill: #5B3D6F");
		rTF.setMaxWidth(35);
		rTF.setAlignment(Pos.CENTER);
		rTF.setTextFormatter(new TextFormatter<>(rFilter));
		rTF.setOnKeyTyped(e-> {red.setValue(Double.valueOf(rTF.getText()));});
		
		red.valueProperty().addListener(e -> {
			if ((int)red.getValue() != Integer.valueOf(rTF.getText())) {
				rTF.setText(String.valueOf((int)red.getValue()));
			}
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
		gLabel.setStyle("-fx-text-fill: #5B3D6F");
		
		gTF = new TextField("255");
		gTF.setTranslateX(-320);
		gTF.setTranslateY(150);
		gTF.setStyle("-fx-text-fill: #5B3D6F");
		gTF.setMaxWidth(35);
		gTF.setAlignment(Pos.CENTER);
		gTF.setTextFormatter(new TextFormatter<>(gFilter));
		gTF.setOnKeyTyped(e-> {green.setValue(Double.valueOf(gTF.getText()));});
		
		green.valueProperty().addListener(e -> {
			if ((int)green.getValue() != Integer.valueOf(gTF.getText())) {
				gTF.setText(String.valueOf((int)green.getValue()));
			}
			
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
		blue.setMajorTickUnit(25);
		blue.setShowTickLabels(true);
		blue.setValue(255);
		
		bLabel = new Label("Blue");
		bLabel.setTranslateX(-610);
		bLabel.setTranslateY(180);
		bLabel.setStyle("-fx-text-fill: #5B3D6F");
		
		bTF = new TextField("255");
		bTF.setTranslateX(-320);
		bTF.setTranslateY(180);
		bTF.setStyle("-fx-text-fill: #5B3D6F");
		bTF.setMaxWidth(35);
		bTF.setAlignment(Pos.CENTER);
		bTF.setTextFormatter(new TextFormatter<>(bFilter));
		bTF.setOnKeyTyped(e-> {blue.setValue(Double.valueOf(bTF.getText()));});
		
		blue.valueProperty().addListener(e -> {
			if ((int)blue.getValue() != Integer.valueOf(bTF.getText())) {
				bTF.setText(String.valueOf((int)blue.getValue()));
			}
			
			changeHex();
			saveHex.setDisable(false);
		});
		
		mainPane.getChildren().addAll(red,green,blue,rLabel,gLabel,bLabel,rTF,gTF,bTF);
	
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
		
		back.setTranslateX(-725);
		back.setTranslateY(-365);
		
		left.setTranslateX(-700);
		
		right.setTranslateX(700);
		
		explanation.setTranslateY(-320);
	}
	
	private void style() {
		String buttonStyle = "-fx-font-size:20";
		getHex.setStyle(buttonStyle);
		openFile.setStyle(buttonStyle);
		clearImage.setStyle(buttonStyle);
		loadHex.setStyle(buttonStyle);
		help.setStyle(buttonStyle);
		
		loadHex.setMinHeight(loadHex.getWidth());
		
		mainPane.setStyle("-fx-background-color: #A4C290");

		average.setStyle("-fx-text-fill: #5B3D6F");
		comp.setStyle("-fx-text-fill: #5B3D6F");
	}
	
	private void changeText(int count) {
		switch(count) {
		case 0: explanation.setText("Welcome to ACER, an application that can take the colors from an image and turn it into one average color.");
				display.setImage(new Image("file:images/display.png"));
				break;
		case 1: explanation.setText("After loading an image, you can press the generate hex button to display the average color of the image.");
				display.setImage(new Image("file:images/display2.png"));
				break;
		case 2: explanation.setText("From here, you can adjust the color by its RGB values, clear the image, or name and save it for later use.");
				display.setImage(new Image("file:images/display3.png"));
				break;
		case 3: explanation.setText("Clicking on the view color list button, it displays any saved colors.");
				display.setImage(new Image("file:images/display4.png"));
				break;
		case 4: explanation.setText("You can also delete any unwanted colors. Beware that there is no recovery system, so colors are deleted for good.");
				display.setImage(new Image("file:images/display5.png"));
				break;
		case 5: explanation.setText("That's all the features of ACER. Have fun messing around with images and colors!");
				display.setImage(new Image("file:images/display6.png"));
				break;
		default: explanation.setText("Beef");
		}
	}
	
	private void filters() {
		rFilter = change -> {
		    String text = change.getControlNewText();
		    
		    if (text.matches("")) {
		    	rTF.setText("0");
		    } else if (text.matches("\\d*") && text.length() <= 4 && Integer.valueOf(text) > 256 ) {
		    	rTF.setText("255");
		    } else if (text.matches("\\d*") && text.length() <= 3 && Integer.valueOf(text) < 256 ) {
		    	return change;
		    }
		    return null;
		};
		
		gFilter = change -> {
		    String text = change.getControlNewText();
		    
		    if (text.matches("")) {
		    	gTF.setText("0");
		    	return null;
		    } else if (text.matches("\\d*") && text.length() <= 4 && Integer.valueOf(text) > 256 ) {
		    	gTF.setText("255");
		    	return null;
		    } else if (text.matches("\\d*") && text.length() <= 3 && Integer.valueOf(text) < 256 ) {
		        return change; 
		    }
		    return null;
		};
		
		bFilter = change -> {
		    String text = change.getControlNewText();
		    
		    if (text.matches("")) {
		    	bTF.setText("0");
		    	return null;
		    } else if (text.matches("\\d*") && text.length() <= 4 && Integer.valueOf(text) > 256 ) {
		    	bTF.setText("255");
		    	return null;
		    } else if (text.matches("\\d*") && text.length() <= 3 && Integer.valueOf(text) < 256 ) {
		        return change; 
		    }
		    return null;
		};
	}
	
}
