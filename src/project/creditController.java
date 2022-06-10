package project;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;

public class creditController {
	public TextField cardHolderNameTF, cardNumberTF, securityCodeTF, addressTF, address2TF, cityTF, countryTF, regionTF, codeTF; 
	public DatePicker dateTF;
	public Button submitButton;
	
	public void submitButton(ActionEvent e)throws IOException
	{
		if(storeInfo.getGuest().addCreditCard(cardNumberTF.getText(), Date.valueOf(dateTF.getValue()), Integer.parseInt(securityCodeTF.getText()), addressTF.getText(), address2TF.getText(), cityTF.getText(), countryTF.getText(), regionTF.getText(), Integer.parseInt(codeTF.getText())))
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Reciept.fxml"));
			Parent ViewParent = loader.load();
			Scene Scene = new Scene(ViewParent);
			
			Stage Window = (Stage)((Node)e.getSource()).getScene().getWindow();
			
			recieptController uC = loader.getController();
			uC.setReciept();
			
			Window.setScene(Scene);
			Window.show();
		}
		else
		{
			AlertBox.display("Error", storeInfo.getGuest().getErrorMessage());
		}
	}
	
	

}
