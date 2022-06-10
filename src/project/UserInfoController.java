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

public class UserInfoController {
	
	public TabPane tabPane;
	public Button  makeChangesButton, saveChangesButton, nextButton1, nextButton2, nextButton3, submitButton;
	public RadioButton addOpRadio;
	public Tab reserveDateTab, reserveRoomTab, optionsTab, userInfoTab;
	public ChoiceBox<Room> roomNameChoice;
	public DatePicker startDate, endDate;
	public TextField firstNameTF, lastNameTF, addressTF, address2TF, cityTF, countryTF, zipTF, emailTF, phoneTF, regionTF, roomCostTF;
	
	public void setInfo()
	{
		roomNameChoice.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) ->
		{
			roomCostTF.setText("" + newValue.getCost());
		});
		
		Guest person = storeInfo.getGuest();
		
		person.setAddress();
		firstNameTF.setText(person.getFirstName());
		lastNameTF.setText(person.getLastName());
		addressTF.setText(person.getAddress1());
		address2TF.setText(person.getAddress2());
		cityTF.setText(person.getCity());
		countryTF.setText(person.getCountry());
		zipTF.setText("" + person.getCode());
		emailTF.setText(person.getEmailAddress());
		phoneTF.setText(person.getPhoneNumber());
		regionTF.setText(person.getRegion());
		firstNameTF.setEditable(false);
		lastNameTF.setEditable(false);
		addressTF.setEditable(false);
		address2TF.setEditable(false);
		cityTF.setEditable(false);
		countryTF.setEditable(false);
		zipTF.setEditable(false);
		emailTF.setEditable(false);
		phoneTF.setEditable(false);
		regionTF.setEditable(false);
		
		
		
		}

	
	public void switchTab()
	{
			tabPane.getSelectionModel().select(reserveDateTab);
	}
	
	public void nextButton1(ActionEvent e)
	{
		tabPane.getSelectionModel().select(reserveDateTab);
	}
	
	public void nextButton2(ActionEvent e)
	{
		if(endDate.getValue().isAfter(startDate.getValue()))
		{
			tabPane.getSelectionModel().select(reserveRoomTab);
			
			ObservableList<Room> oList = FXCollections.observableArrayList();
			
			try
			{
				ArrayList<Room> aList = Room.getRoomList(startDate.getValue(), endDate.getValue());
				for(int i = 0; i < aList.size(); i++)
				{
					oList.add(aList.get(i));
				}
			}
			catch (EmptySetException exc)
			{
				AlertBox.display("Error", exc.getMessage());
			}
			roomNameChoice.setItems(oList);
			
		}
		else
			AlertBox.display("Error", "End Date is before Start Date");
	}
	
	public void nextButton3(ActionEvent e)
	{
		tabPane.getSelectionModel().select(optionsTab);
	}
	
	public void submitButton(ActionEvent e) throws IOException
	{
		try
		{
		Reservation r = new Reservation(storeInfo.getGuest(),roomNameChoice.getValue());
		r.setCheckIn(Date.valueOf(startDate.getValue()));
		r.setCheckOut(Date.valueOf(endDate.getValue()));
		r.setOptions(addOpRadio.isSelected());
		r.setCycleCost(roomNameChoice.getValue().getCost());
		
		storeInfo.setReserve(r);
		if(r.insertReservation())
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("creditCard.fxml"));
			Parent ViewParent = loader.load();
			Scene Scene = new Scene(ViewParent);
			
			
			Stage Window = (Stage)((Node)e.getSource()).getScene().getWindow();
			
			Window.setScene(Scene);
			Window.show();
		}
		else
			AlertBox.display("Error", r.getErrorMessage());
		
		
		storeInfo.setReserve(r);
		}
		catch (EmptySetException exc)
		{
			AlertBox.display("Error", exc.getMessage());
		}
		
	}
	
	public void makeChanges(ActionEvent e)
	{

		firstNameTF.setEditable(true);
		lastNameTF.setEditable(true);
		addressTF.setEditable(true);
		address2TF.setEditable(true);
		cityTF.setEditable(true);
		countryTF.setEditable(true);
		zipTF.setEditable(true);
		emailTF.setEditable(true);
		phoneTF.setEditable(true);
		regionTF.setEditable(true);
	}
	
	public void saveChanges(ActionEvent e)
	{
		Guest jeremy = storeInfo.getGuest();
		jeremy.setFirstName(firstNameTF.getText());
		jeremy.setLastName(lastNameTF.getText());
		jeremy.setEmailAddress(emailTF.getText());
		jeremy.setPhoneNumber(phoneTF.getText());
		
		boolean successful = jeremy.updateGuest();
		
		if(successful)
		{
			jeremy.updateAddress(addressTF.getText(), address2TF.getText(), cityTF.getText(), countryTF.getText(), regionTF.getText(), Integer.parseInt(zipTF.getText()));

			firstNameTF.setEditable(false);
			lastNameTF.setEditable(false);
			addressTF.setEditable(false);
			address2TF.setEditable(false);
			cityTF.setEditable(false);
			countryTF.setEditable(false);
			zipTF.setEditable(false);
			emailTF.setEditable(false);
			phoneTF.setEditable(false);
			regionTF.setEditable(false);
		}
	}
	
}
