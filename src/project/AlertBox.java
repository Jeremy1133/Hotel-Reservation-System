package project;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class AlertBox
{
	public static void display(String title, String message)
	{
		Stage alertStage = new Stage();
		
		alertStage.initModality(Modality.APPLICATION_MODAL);//Block input for other windows.
		
		alertStage.setTitle(title);
		alertStage.setMinWidth(200);
		
		Label label = new Label();
		label.setText(message);
		Button closeButton = new Button();
		closeButton.setText("Close");
		closeButton.setOnAction(e -> alertStage.close());
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label,closeButton);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout, layout.getPrefWidth(), layout.getPrefWidth());
		alertStage.setScene(scene);
		alertStage.showAndWait();//Used in conjunction with modality.APPLICATION_MODAL
	}
	
	public static void guest(Guest g)
	{
		Stage guestStage = new Stage();
		
		guestStage.initModality(Modality.APPLICATION_MODAL);
		
		guestStage.setTitle(g.getFirstName() + " " + g.getLastName());
		
		boolean hasAddress = g.setAddress();
		boolean hasCard = g.setCreditCard();
		
		HBox hBox = new HBox(8);
		
		VBox vBox = new VBox(5);
		//SetIn Up Guest Info
		vBox.setPadding(new Insets(5,5,5,5));
		
		HBox firstName = new HBox(5);
			Label fN1 = new Label("Name");
			Label fN2 = new Label(g.getFirstName() + " " + g.getLastName());
		firstName.getChildren().addAll(fN1,fN2);
		
		HBox emailAddress = new HBox(5);
			Label eA1 = new Label("Email Address");
			Label eA = new Label(g.getEmailAddress());
		emailAddress.getChildren().addAll(eA1,eA);
		
		HBox phoneNumber = new HBox(5);
			Label pN = new Label("Phone Number");
			Label pN2 = new Label(g.getPhoneNumber());
		phoneNumber.getChildren().addAll(pN, pN2);
		
		vBox.getChildren().addAll(firstName,emailAddress,phoneNumber);
		if(hasAddress)
		{
			HBox address = new HBox(5);
				Label a1 = new Label("Address");
				Label a2 = new Label(g.getAddress1() + ", "
						+ g.getAddress2() + ", "
						+ g.getCity() + ", "
						+ g.getCountry() + ", "
						+ g.getRegion() + ", "
						+ g.getCode());
				address.getChildren().addAll(a1, a2);

			vBox.getChildren().addAll(address);
		}
		//Setting Up Guest Reservation Table
		if(hasCard)
		{
			HBox card = new HBox(5);
				Label c2 = new Label("Card Information");
				Label c1 = new Label(g.getCardNumber() + ", " + g.getExpirationDate().toString() + ", " + g.getSecurityCode());
			card.getChildren().addAll(c2, c1);
			vBox.getChildren().addAll(card);
		}
		
		TableColumn<Reservation, String> roomNameColumn = new TableColumn<>("Room Name");
		roomNameColumn.setMinWidth(75);
		roomNameColumn.setCellValueFactory(new PropertyValueFactory<>("roomName"));
		
		TableColumn<Reservation, Date> checkInColumn = new TableColumn<>("Check In");
		checkInColumn.setMinWidth(75);
		checkInColumn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
		
		TableColumn<Reservation, Date> checkOutColumn = new TableColumn<>("Check Out");
		checkOutColumn.setMinWidth(75);
		checkOutColumn.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
		
		TableColumn<Reservation, Double> cycleCostColumn = new TableColumn<>("Cycle Cost");
		cycleCostColumn.setMinWidth(75);
		cycleCostColumn.setCellValueFactory(new PropertyValueFactory<>("cycleCost"));
		
		TableColumn<Reservation, Boolean> optionsColumn = new TableColumn<>("Options");
		optionsColumn.setMinWidth(75);
		optionsColumn.setCellValueFactory(new PropertyValueFactory<>("options"));
		
		TableView<Reservation> reservationTable = new TableView<>();
		reservationTable.getColumns().addAll(roomNameColumn, checkInColumn, checkOutColumn, cycleCostColumn, optionsColumn);
		
		ObservableList<Reservation> oList = FXCollections.observableArrayList();
		try
		{
		ArrayList<Reservation> aList = Reservation.getReservationList("WHERE guestID = " + g.getGuestID() + ";");
		for(int i = 0; i < aList.size(); i++)
		{
			oList.add(aList.get(i));
		}
		}
		catch(EmptySetException e)
		{
			
		}
		
		reservationTable.getItems().addAll(oList);
		
		ContextMenu  rightClick = new ContextMenu();
		MenuItem checkIn = new MenuItem("Check In");
		checkIn.setOnAction(e -> {
		Room r = new Room(reservationTable.getSelectionModel().getSelectedItem().getRoomID());
		r.setStatus("Occupied");
		if(r.updateRoom())
			AlertBox.display("Checked In", g.getFirstName() + "has successfully check in to " + r.getName());
		else
			AlertBox.display("Error", r.getErrorMessage());
		});
		MenuItem checkOut = new MenuItem("Check Out");
		checkOut.setOnAction(e -> {
			Room r = new Room(reservationTable.getSelectionModel().getSelectedItem().getRoomID());
			r.setStatus("Unoccupied");
			r.setCondition("Unready");
			if(r.updateRoom())
				AlertBox.display("Checked In", g.getFirstName() + "has successfully check in to " + r.getName());
			else
				AlertBox.display("Error", r.getErrorMessage());
			});

		MenuItem openReport = new MenuItem("Open Report");
		openReport.setOnAction(e -> {openReport(reservationTable.getSelectionModel().getSelectedItem());});
		MenuItem printBill = new MenuItem("Print Bill");
		printBill.setOnAction(e -> {printBill(reservationTable.getSelectionModel().getSelectedItem());});
		rightClick.getItems().addAll(checkIn,checkOut,openReport,printBill);
		
		reservationTable.addEventHandler(MouseEvent.MOUSE_CLICKED, t ->
		{
			if(t.getButton() == MouseButton.SECONDARY)
			{
				rightClick.show(reservationTable, t.getScreenX(), t.getScreenY());
			}
		});
		hBox.getChildren().addAll(vBox,reservationTable);
		
		Scene scene = new Scene(hBox, hBox.getPrefWidth(), hBox.getPrefWidth());
		guestStage.setScene(scene);
		guestStage.showAndWait();
	}
	
	private static void printBill(Reservation r)
	{
		VBox vBox = new VBox(5);
		vBox.getChildren().addAll(
				new Label(r.getGuestFirstName() + " " + r.getGuestLastName()),
				new Label("Staying in"),
				new Label(r.getRoomName()),
				new Label("3 Old Wood Trail"), 
				new Label("Eucalyptus, California 90000"),
				new Label("on"),
				new Label(LocalDate.now().toString())
				);
		Room room = new Room(r.getRoomID());
		GridPane grid = new GridPane();
		grid.setVgap(5);
		grid.setHgap(5);
		grid.add(new Label("Room Cost: "), 0,0);
		double roomCost = room.getCost();
		grid.add(new Label("$" + roomCost),1,0);
		grid.add(new Label("Additional Charge: "), 0,1);
		double additionalCharge = 12.00;
		grid.add(new Label("$" + additionalCharge), 1, 1);
		grid.add(new Label("Tax: "), 0, 2);
		double tax = .08*(roomCost + additionalCharge);
		grid.add(new Label("$" + tax), 1, 2);
		grid.add(new Label("Final Cost: "), 0, 3);
		double finalCost = roomCost + additionalCharge + tax;
		grid.add(new Label("$" + finalCost), 1, 3);
		
		Label copy = new Label("Guest Copy");
		Label sign = new Label("X___________________________________");
		sign.setVisible(false);
		vBox.getChildren().addAll(grid,sign, copy);
		
		Stage printSummary = new Stage();
		Scene scene = new Scene(vBox, vBox.getPrefWidth(), vBox.getPrefWidth());
		printSummary.setScene(scene);
		printSummary.show();
		
		FXPrinter.pageSetup(vBox, printSummary);
		printSummary.close();
		
		copy.setText("Inn Copy");
		sign.setVisible(true);
		printSummary.show();
		
		FXPrinter.pageSetup(vBox, printSummary);
		printSummary.close();
		
	}
	private static GridPane primaryGrid;
	private static Stage reportSummary;
	public static void openReport(Reservation r)
	{
	reportSummary = new Stage();
	
	reportSummary.initModality(Modality.APPLICATION_MODAL);//Block input for other windows.
	
	reportSummary.setTitle(r.getGuestFirstName() + " " + r.getGuestLastName());
	reportSummary.setMinWidth(200);
	

	primaryGrid = new GridPane();
	primaryGrid.setVgap(5);
	primaryGrid.setHgap(5);
		primaryGrid.add(new Label("Statement"), 0, 0);
		primaryGrid.add(new Label(r.getGuestFirstName() + " " + r.getGuestLastName()),0,1);
		primaryGrid.add(new Label(r.getRoomName()), 1, 1);
		primaryGrid.add(new Label("3 Old Wood Trail"), 1, 2);
		primaryGrid.add(new Label("Eucalyptus, California 90000"), 1, 3);
		primaryGrid.add(new  Label("Dates"), 0, 4);
		primaryGrid.add(new Label("Amount"), 3, 4);
		primaryGrid.add(new Label(r.getCheckIn().toString()), 0, 5);
		Room room = new Room(r.getRoomID());
		primaryGrid.add(new Label(room.getType()), 1, 5);
		primaryGrid.add(new Label("$" + room.getCost()), 3, 5);
		primaryGrid.add(new Label("Additional Charge"), 1, 6);
		double additionalCharge = 12.00;
		primaryGrid.add(new Label("$" + additionalCharge), 3, 6);
		primaryGrid.add(new Label("Tax"), 1, 7);
		double tax = .08*(room.getCost() + 12.00);
		primaryGrid.add(new Label("$" + tax), 3, 7);
		primaryGrid.add(new Label("Nightly Total"), 2, 8);
		double total = (room.getCost() + additionalCharge + tax);
		primaryGrid.add(new Label("$" + total), 3, 8);
		primaryGrid.add(new Label(r.getCheckOut().toString()), 0, 9);
		int numberOfDays = (int)ChronoUnit.DAYS.between(
				LocalDate.of(r.getCheckIn().getYear() + 1900, r.getCheckIn().getMonth() +1, r.getCheckIn().getDate()),
				LocalDate.of(r.getCheckOut().getYear() + 1900, r.getCheckOut().getMonth() +1, r.getCheckOut().getDate())
				);
		primaryGrid.add(new Label("x" + numberOfDays), 3, 9);
		primaryGrid.add(new Label("Final Total"), 2, 10);
		double finalTotal = (total*numberOfDays);
		finalTotal = Math.round(finalTotal*100);
		finalTotal = finalTotal/100;
		primaryGrid.add(new Label("$" + finalTotal), 3, 10);
		Button printReport = new Button("Print Report");
		printReport.setOnAction(e -> {
			printReport.setVisible(false);
			FXPrinter.pageSetup(primaryGrid, reportSummary);
			printReport.setVisible(true);
			});
		primaryGrid.add(printReport, 3, 11);
	
	Scene scene = new Scene(primaryGrid, primaryGrid.getPrefWidth(), primaryGrid.getPrefWidth());
	reportSummary.setScene(scene);
	reportSummary.showAndWait();//Used in conjunction with modality.APPLICATION_MODAL
}

	public static void addEmployeeSecurity(Employee emp)
	{
		Stage employeeStage = new Stage();
		
		employeeStage.initModality(Modality.APPLICATION_MODAL);//Block input for other windows.
		
		employeeStage.setTitle(emp.getFirstName() + " " + emp.getLastName());
		employeeStage.setMinWidth(200);
		
		VBox vBox = new VBox(5);
		Label l = new Label("Input Employee Username and Password");
		TextField username = new TextField();
		TextField password = new TextField();
		Button add = new Button("Add");
		add.setOnAction(e -> {emp.addUsernamePassword(username.getText(), password.getText()); employeeStage.close();});
		vBox.getChildren().addAll(l,username,password,add);
		
		Scene scene = new Scene(vBox, vBox.getPrefWidth(), vBox.getPrefWidth());
		employeeStage.setScene(scene);
		employeeStage.showAndWait();//Used in conjunction with modality.APPLICATION_MODAL
	}
	public static boolean cardInsertSuccessful = false;
	public static boolean guestCardInfo(Guest g)
	{
		Stage guestCardStage = new Stage();
		guestCardStage.initModality(Modality.APPLICATION_MODAL);//Block input for other windows.
		
		guestCardStage.setTitle(g.getFirstName() + " " + g.getLastName());
		guestCardStage.setMinWidth(200);
		
		VBox vBox = new VBox();
		TextField cardNumber = new TextField();
		cardNumber.setPromptText("Card Number");
		DatePicker expirationDate = new DatePicker();
		expirationDate.setPromptText("Expiration Date");
		TextField securityCode = new TextField();
		securityCode.setPromptText("Security Code");
		TextField address1 = new TextField();
		address1.setPromptText("Address 1");
		TextField address2 = new TextField();
		address2.setPromptText("Address 2");
		TextField city = new TextField();
		city.setPromptText("City");
		TextField country = new TextField();
		country.setPromptText("Country");
		TextField region = new TextField();
		region.setPromptText("Region");
		TextField code = new TextField();
		code.setPromptText("Code");
		
		Button insertCard = new Button("Add Card");
		insertCard.setOnAction(e -> {
			cardInsertSuccessful = g.addCreditCard(cardNumber.getText(), Date.valueOf(expirationDate.getValue()), Integer.parseInt(securityCode.getText()),
					address1.getText(), address2.getText(), city.getText(), country.getText(), region.getText(), Integer.parseInt(code.getText()));
			guestCardStage.close();
		});
		
		vBox.getChildren().addAll(cardNumber, new Label("Pick the Last Day of a Month"), expirationDate, securityCode,
				address1, address2, city, country, region, code, insertCard
				);
		
		Scene scene = new Scene(vBox, vBox.getPrefWidth(), vBox.getPrefWidth());
		guestCardStage.setScene(scene);
		guestCardStage.showAndWait();
		
		return cardInsertSuccessful;
	}
}
