package project;

import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;

public class main extends Application {

	private static Stage p;	
	@Override
	public void start(Stage primaryStage) {
		p = primaryStage;
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Entrance.fxml"));
			Scene scene = new Scene(root);
			
			primaryStage.setScene(scene);
			primaryStage.show();
		}catch(Exception e) 
		{
			e.printStackTrace();
		}
		}
		
	public static void main(String[] args) 
	{
		launch(args);
	}
	
	public static Stage getP()
	{
		return p;
	}

}
