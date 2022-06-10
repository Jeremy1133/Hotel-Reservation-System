package project;

import java.io.IOException;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

public class LoginController {

	public TextField usernameTF;
	public PasswordField passwordPF;
	
	public void signInButton(ActionEvent event) throws IOException
	{
		try
		{
		Guest jeremy = new Guest(usernameTF.getText(),passwordPF.getText());
		storeInfo.setGuest(jeremy);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("UserInfo.fxml"));
		Parent ViewParent = loader.load();
		Scene Scene = new Scene(ViewParent);
		
		UserInfoController uC = loader.getController();
		uC.setInfo();
		uC.switchTab();
		
		Stage Window = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		Window.setScene(Scene);
		Window.show();
		
		}
		catch (EmptySetException exp)
		{
			AlertBox.display("Error", exp.getMessage());
		}
		
	}
	
	public void cancelButton(ActionEvent event) throws IOException
	{
		Parent ViewParent = FXMLLoader.load(getClass().getResource("Entrance.fxml"));
		Scene Scene = new Scene(ViewParent);
		
		Stage Window = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		Window.setScene(Scene);
		Window.show();
	}
}
