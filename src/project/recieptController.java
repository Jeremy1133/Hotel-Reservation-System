package project;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;

public class recieptController {
	
	public Label guestL, roomL, startDateL, endDateL, perDayChargeL, taxChargeL, nightlyTotalL, optionChargeL, numberOfDaysL, finalTotalL;
	public Button printButton;
	public AnchorPane recieptPane;
	public void setReciept()
	{
		guestL.setText(storeInfo.getGuest().getFirstName() + " " + storeInfo.getGuest().getLastName());
		roomL.setText(storeInfo.getReserve().getRoomName());
		startDateL.setText(storeInfo.getReserve().getCheckIn().toString());
		endDateL.setText(storeInfo.getReserve().getCheckOut().toString());
		double cost = storeInfo.getReserve().getCycleCost();
		perDayChargeL.setText("$" + storeInfo.getReserve().getCycleCost());
		double optionCost = 0;
		if(storeInfo.getReserve().isOptions())
			optionCost = 20;
		optionChargeL.setText("$" + optionCost);
		cost = cost + optionCost;
		double tax = cost * .08;
		taxChargeL.setText("$" +tax );
		cost = cost + tax;
		nightlyTotalL.setText("$" + cost);
		int nDays = (int)ChronoUnit.DAYS.between(
				LocalDate.of(storeInfo.getReserve().getCheckIn().getYear() + 1900, storeInfo.getReserve().getCheckIn().getMonth() +1, storeInfo.getReserve().getCheckIn().getDate()),
				LocalDate.of(storeInfo.getReserve().getCheckOut().getYear() + 1900, storeInfo.getReserve().getCheckOut().getMonth() +1, storeInfo.getReserve().getCheckOut().getDate()));
		numberOfDaysL.setText(" " + nDays);
		
		finalTotalL.setText("$" + cost*nDays);
	}

	public void printButton(ActionEvent e)
	{
		try
		{
		FXPrinter.pageSetup(recieptPane, main.getP());
		}
		catch (Exception exc)
		{
			AlertBox.display("Error", exc.getMessage());
		}
	}
}
