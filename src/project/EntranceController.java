package project;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class EntranceController {
	
	public void signUpButtonSceneChange(ActionEvent event) throws IOException
	{
		Parent registerViewParent = FXMLLoader.load(getClass().getResource("Register.fxml"));
		Scene registerScene = new Scene(registerViewParent);
		
		Stage RegisterWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		RegisterWindow.setScene(registerScene);
		RegisterWindow.show();
	}
	
	public void signInButtonSceneChange(ActionEvent event) throws IOException
	{
		Parent registerViewParent = FXMLLoader.load(getClass().getResource("Login.fxml"));
		Scene registerScene = new Scene(registerViewParent);
		
		Stage RegisterWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		RegisterWindow.setScene(registerScene);
		RegisterWindow.show();
	}

}
