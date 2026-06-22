import javafx.application.Application;
import javafx.stage.Stage;
import screen.MainScreen;
import color.HexGen;
public class ACER extends Application{
	
	public void start(Stage stage) {
		new MainScreen();
	}
	public static void main(String[] args) {
		launch(args);
	}

}
