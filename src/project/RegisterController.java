package project;


import javafx.scene.control.*;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class RegisterController {
	
	public Button registerButton, cancelButton;
	public TextField firstNameTF, lastNameTF, addressTF, address2TF, cityTF, regionTF, codeTF, emailAddressTF, phoneTF, userNameTF, passwordTF, confirmPasswordTF, countryTF;
	
	public void registerButtonSave(ActionEvent event) throws IOException
	{
		if(passwordTF.getText().equals(confirmPasswordTF.getText()))
		{
	Guest jeremy = new Guest();
	jeremy.setFirstName(firstNameTF.getText());
	jeremy.setLastName(lastNameTF.getText());
	jeremy.setEmailAddress(emailAddressTF.getText());
	jeremy.setPhoneNumber(phoneTF.getText());
	
	boolean successful = jeremy.insertGuest() && jeremy.addUsernamePassword(userNameTF.getText(), passwordTF.getText());
	
	if(successful)
	{
		jeremy.addAddress(addressTF.getText(), address2TF.getText(), cityTF.getText(), countryTF.getText(), regionTF.getText(), Integer.parseInt(codeTF.getText()));
		//Need to change scene after.
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
	else AlertBox.display("Error", jeremy.getErrorMessage());
	
	}
	
	else
		AlertBox.display("Error", "Passwords do not match!");
	}
	
	public void cancel(ActionEvent e) throws IOException
	{
		Parent ViewParent = FXMLLoader.load(getClass().getResource("Entrance.fxml"));
		Scene Scene = new Scene(ViewParent);
		
		Stage Window = (Stage)((Node)e.getSource()).getScene().getWindow();
		
		Window.setScene(Scene);
		Window.show();
	}
	
	
}
