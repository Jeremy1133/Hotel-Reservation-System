package project;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.layout.*;

public class EmployeeSystem2 extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}
	
		private Stage pStage;
		private Scene loginScene;
		private Employee loginEmployee;	
		boolean isManager, isReceptionist, isHouseKeeping;
		Button signInButton;
	public void start(Stage primaryStage) throws Exception
	{
		pStage = primaryStage;
		primaryStage.setTitle("Employee System");
		
		HBox hBox = new HBox(8);
		hBox.setPadding(new Insets(5,5,5,5));
		
		VBox vBox = new VBox(8);
		vBox.setPadding( new Insets(5,5,5,5));
		vBox.setAlignment(Pos.TOP_CENTER);
		
		Rectangle proxyIcon = new Rectangle(200,200,Color.GREEN);
		
		TextField usernameField = new TextField();
		usernameField.setEditable(true);
		usernameField.setPromptText("Username");
		usernameField.setAlignment(Pos.TOP_LEFT);
		
		PasswordField passwordField = new PasswordField();
		passwordField.setEditable(true);
		passwordField.setPromptText("Password");
		passwordField.setAlignment(Pos.CENTER_LEFT);
		passwordField.setOnKeyPressed(evt -> {
			if(evt.getText().equalsIgnoreCase("Enter") || evt.getText().equals("\n"))
				try
			{
				loginEmployee = new Employee(usernameField.getText(), passwordField.getText());
				isManager = loginEmployee.getPosition().equalsIgnoreCase("Manager");
				isReceptionist = loginEmployee.getPosition().equalsIgnoreCase("Receptionist") || loginEmployee.getPosition().equalsIgnoreCase("Manager");
				isHouseKeeping = loginEmployee.getPosition().equalsIgnoreCase("House Keeping") || loginEmployee.getPosition().equalsIgnoreCase("Manager");
				if(loginEmployee.getEmployeeID() > 0)
				{
				usernameField.clear();
				passwordField.clear();
				createLoggedIn();
				primaryStage.setScene(loggedInScene);
				}
			}
			catch(EmptySetException ex)
			{
				AlertBox.display("Combination Not Found", ex.getMessage());
			}
		});
		
		signInButton = new Button("Sign In");
		signInButton.setAlignment(Pos.BOTTOM_RIGHT);
		signInButton.setOnAction(e ->
			{
				try
				{
					loginEmployee = new Employee(usernameField.getText(), passwordField.getText());
					isManager = loginEmployee.getPosition().equalsIgnoreCase("Manager");
					isReceptionist = loginEmployee.getPosition().equalsIgnoreCase("Receptionist") || loginEmployee.getPosition().equalsIgnoreCase("Manager");
					isHouseKeeping = loginEmployee.getPosition().equalsIgnoreCase("House Keeping") || loginEmployee.getPosition().equalsIgnoreCase("Manager");
					if(loginEmployee.getEmployeeID() > 0)
					{
					usernameField.clear();
					passwordField.clear();
					createLoggedIn();
					primaryStage.setScene(loggedInScene);
					}
				}
				catch(EmptySetException ex)
				{
					AlertBox.display("Combination Not Found", ex.getMessage());
				}
			});
		
		vBox.getChildren().addAll(usernameField, passwordField, signInButton);

		StackPane centering = new StackPane();
		centering.getChildren().add(vBox);
		hBox.getChildren().addAll(proxyIcon, centering);
		
		loginScene = new Scene(hBox, hBox.getPrefWidth(), hBox.getPrefHeight());
		
		primaryStage.setScene(loginScene);
		primaryStage.show();
	}
		
		private Scene loggedInScene;
		private BorderPane innerPane;
		private VBox headerBar;
	private void createLoggedIn()
	{
		createTableHeader();
		createSearch();
		createGuestTable();
		createRoomTable();
		createReservationTable();
		createEmployeeTable();
		createGuestInsert();
		createRoomInsert();
		createReservationInsert();
		createEmployeeInsert();
		
		innerPane = new BorderPane();
		innerPane.setPadding(new Insets(5,5,5,5));
		
		headerBar = new VBox(8);
		headerBar.setPadding(new Insets(5,5,5,5));
		headerBar.getChildren().addAll(tableHeader, searchBar);
		
		innerPane.setTop(headerBar);
		if(isReceptionist)
			setGuestScene();
		if(isHouseKeeping)
			setRoomScene();
		if(isManager)
			setEmployeeScene();
		
		loggedInScene = new Scene(innerPane, innerPane.getPrefWidth(), innerPane.getPrefHeight());	
	}
	
		private FlowPane tableHeader;
	private void createTableHeader()//One and Done
	{
		setObservableChoiceBoxList();
		tableHeader = new FlowPane();
		tableHeader.setPadding( new Insets(5,5,5,5));
		tableHeader.setVgap(8);
		tableHeader.setHgap(8);
		
		Button guest = new Button("Guest Table");
		guest.setOnAction(e -> setGuestScene());
		guest.setVisible(isReceptionist);
		
		Button room = new Button("Room Table");
		room.setOnAction(e -> setRoomScene());
		room.setVisible(isManager);
		
		Button reservation = new Button("Reservation Table");
		reservation.setOnAction(e -> setReservationScene());
		reservation.setVisible(isReceptionist);
	
		Button employee = new Button("Employee Table");
		employee.setOnAction(e -> setEmployeeScene());
		employee.setVisible(isManager);
		
		Button logOut = new Button("Log Out");
		logOut.setOnAction(e -> {pStage.setScene(loginScene);});
		
		if(isReceptionist && !isManager)
			tableHeader.getChildren().addAll(guest,reservation,logOut);
		if(isHouseKeeping && !isManager)
			tableHeader.getChildren().addAll(logOut);
		if(isManager)
			tableHeader.getChildren().addAll(guest, room, reservation, employee, logOut);
		
	}
	
		private HBox searchBar;
		private VBox[] option;
		private ChoiceBox<String>[] choiceBox;
		private TextField[] textEntry;
		private DatePicker[] dateEntry;
		private ComboBox<String>[] choiceStringEntry;
		private ComboBox<Integer>[] choiceIntEntry;
		private ComboBox<Boolean>[] choiceBooleanEntry;
		private StackPane[] center;
		private ChoiceBox<String>[] andOr;
		private Button search;
	private void createSearch()
	{
		searchBar = new HBox(8);
		searchBar.setPadding(new Insets(5,5,5,5));
		
		int l = 1;
		option = new VBox[l];
		choiceBox = new ChoiceBox[l];
		textEntry = new TextField[l];
		dateEntry = new DatePicker[l];
		choiceStringEntry = new ComboBox[l];
		choiceIntEntry = new ComboBox[l];
		choiceBooleanEntry = new ComboBox[l];
		if(l>1) andOr = new ChoiceBox[l-1];
		center = new StackPane[l];
		
		
		for(int i = 0; i < l; i++)
		{
			if(i > 0)
			{
				center[i-1] = new StackPane();
				andOr[i-1] = new ChoiceBox<>();
				andOr[i-1].getItems().setAll("AND","OR");
				andOr[i-1].setValue("AND");
				center[i-1].getChildren().add(andOr[i-1]);
				searchBar.getChildren().add(center[i-1]);
			}
			option[i] = new VBox(8);
			option[i].setPadding(new Insets(5,5,5,5));
			choiceBox[i] = new ChoiceBox<>();
			textEntry[i] = new TextField();
			dateEntry[i] = new DatePicker();
			choiceStringEntry[i] = new ComboBox<>();
			choiceIntEntry[i] = new ComboBox<>();
			choiceBooleanEntry[i] = new ComboBox<>();
			option[i].getChildren().addAll(choiceBox[i],textEntry[i]);
			searchBar.getChildren().add(option[i]);
			choiceBox[i].getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue) -> searchOptions());
		}
		center[l-1] = new StackPane();
		search = new Button("Search");
		center[l-1].getChildren().add(search);
		searchBar.getChildren().add(center[l-1]);
		
	}

	private void searchOptions()
	{
		option[0].getChildren().removeAll(textEntry[0], dateEntry[0], choiceStringEntry[0], choiceIntEntry[0], choiceBooleanEntry[0]);
		switch(choiceBox[0].getValue())
		{
		case "Condition": option[0].getChildren().add(choiceStringEntry[0]); choiceStringEntry[0].getItems().clear();
						  choiceStringEntry[0].getItems().addAll("Unready", "Clean", "Repaired", "Ready");
						  choiceStringEntry[0].setPromptText("Condition");
		break;
		case "Status": option[0].getChildren().add(choiceStringEntry[0]); choiceStringEntry[0].getItems().clear();
		  choiceStringEntry[0].getItems().addAll("Occupied", "Unoccupied");
		  choiceStringEntry[0].setPromptText("Status");
		break;
		case "Patios": option[0].getChildren().add(choiceIntEntry[0]); choiceIntEntry[0].getItems().clear();
		  choiceIntEntry[0].getItems().addAll(1,2);
		  choiceStringEntry[0].setPromptText("Number of Patios");
		  break;
		case "Forests": option[0].getChildren().add(choiceIntEntry[0]); choiceIntEntry[0].getItems().clear();
		  choiceIntEntry[0].getItems().addAll(1,2);
		  choiceStringEntry[0].setPromptText("Number of Forests");
		break;
		case "Check In": case "Check Out": option[0].getChildren().add(dateEntry[0]); dateEntry[0].setValue(LocalDate.now());
		break;
		case "Options": option[0].getChildren().add(choiceBooleanEntry[0]); choiceBooleanEntry[0].getItems().clear();
		choiceBooleanEntry[0].getItems().addAll(true, false); choiceBooleanEntry[0].setPromptText("Options");
		break;
			default: option[0].getChildren().add(textEntry[0]); textEntry[0].setPromptText(choiceBox[0].getValue());
		}
	}

	private String firstCondition;
	private int secondCondition = -1;//string =0, int = 1, double =2 boolean = 3, date = 4
	private void craftSQLConditions()
	{
		firstCondition = choiceBox[0].getValue();
		switch(choiceBox[0].getValue())
		{
		case "First Name": secondCondition = 0; break;
		case "Last Name": secondCondition = 0; break;
		case "Email Address": secondCondition = 0; break;
		case "Phone Number": secondCondition = 0; break;
		case "RoomID": secondCondition = 0; break;
		case "Name": secondCondition = 0; break;
		case "Location": secondCondition = 0; break;
		case "Condition": secondCondition = 0; break;
		case "Status": secondCondition = 0; break;
		case "Type": secondCondition = 0; break;
		case "Cost": secondCondition = 2; break;
		case "Patios": secondCondition = 1; break;
		case "Forests": secondCondition = 1; break;
		case "Room Name": secondCondition = 0; break;
		case "Check In": secondCondition = 4; break;
		case "Check Out": secondCondition = 4; break;
		case "Options": secondCondition = 3; break;
		case "Cycle Cost": secondCondition = 2; break;
		case "Position": secondCondition = 0; break;
		case "Wage": secondCondition = 2; break;
		case "Address1": secondCondition = 0; break;
		case "Address2": secondCondition = 0; break;
		case "City": secondCondition = 0; break;
		case "State": secondCondition = 0; break;
		case "Zipcode": secondCondition = 1; break;
			default: secondCondition = -1;
		}
	}
	
		private TableView<Guest> guestTable;
	private void createGuestTable()//One and Done
	{
		ContextMenu rightClick = new ContextMenu();
		MenuItem open = new MenuItem("Open");
		open.setOnAction(e -> {
			gGuest = guestTable.getSelectionModel().getSelectedItem();
			AlertBox.guest(gGuest);
		});
		MenuItem update = new MenuItem("Update");
		update.setOnAction(e -> {
			gGuest = guestTable.getSelectionModel().getSelectedItem();
			guestFirstName.setText(gGuest.getFirstName());
			guestLastName.setText(gGuest.getLastName());
			guestEmailAddress.setText(gGuest.getEmailAddress());
			guestPhoneNumber.setText(gGuest.getPhoneNumber());
			insertGuest.getChildren().removeAll(guestAdd, guestUpdate);
			insertGuest.getChildren().add(guestUpdate);
		});
		update.setVisible(isManager);
		MenuItem delete = new MenuItem("Delete");
			delete.setOnAction(e -> {guestTable.getSelectionModel().getSelectedItem().deleteGuest();
				guestTable.getItems().remove(guestTable.getSelectionModel().getSelectedItem());});
		rightClick.getItems().addAll(open,update, delete);
		delete.setVisible(isManager);
		
		TableColumn<Guest, String> gFirstNameColumn = new TableColumn<>("First Name");
		gFirstNameColumn.setMinWidth(75);
		gFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		
		TableColumn<Guest, String> gLastNameColumn = new TableColumn<>("Last Name");
		gLastNameColumn.setMinWidth(75);
		gLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		
		TableColumn<Guest, String> gEmailColumn = new TableColumn<>("Email Address");
		gEmailColumn.setMinWidth(75);
		gEmailColumn.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
		
		TableColumn<Guest, String> gPhoneNumberColumn = new TableColumn<>("Phone Number");
		gPhoneNumberColumn.setMinWidth(75);
		gPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
		
		guestTable = new TableView<>();
		guestTable.getColumns().addAll(gFirstNameColumn,gLastNameColumn,gEmailColumn,gPhoneNumberColumn);
		
		guestTable.addEventHandler(MouseEvent.MOUSE_CLICKED, t ->
		{
			if(t.getButton() == MouseButton.SECONDARY)
			{
				rightClick.show(guestTable, t.getScreenX(), t.getScreenY());
			}
		});
	}
	
		private TableView<Room> roomTable;
	private void createRoomTable()//One and Done
	{
		ContextMenu rightClick = new ContextMenu();
		MenuItem clean = new MenuItem("Clean");
		clean.setOnAction(e -> {
			Room room = roomTable.getSelectionModel().getSelectedItem();
			if(room.getCondition().equals("Unready"))
			{
				room.setCondition("Cleaned");
			}
			else if( room.getCondition().equals("Repaired"))
			{
				room.setCondition("Ready");
			}
			room.updateRoom();
			roomTable.getItems().clear();
			roomTable.getItems().addAll(getRoomTable());
		});
		MenuItem repair = new MenuItem("Repair");
		repair.setOnAction(e -> {
			Room room = roomTable.getSelectionModel().getSelectedItem();
			if(room.getCondition().equals("Unready"))
			{
				room.setCondition("Repaired");
				rightClick.getItems().remove(repair);
			}
			else if( room.getCondition().equals("Cleaned"))
			{
				room.setCondition("Ready");
				rightClick.getItems().remove(repair);
			}
			room.updateRoom();
			roomTable.getItems().clear();
			roomTable.getItems().addAll(getRoomTable());
		});
		MenuItem update = new MenuItem("Update");
		update.setOnAction(e -> {
			rRoom = roomTable.getSelectionModel().getSelectedItem();
			roomName.setText(rRoom.getName());
			roomLocation.setText(rRoom.getLocation());
			roomCondition.setValue(rRoom.getCondition());
			roomStatus.setValue(rRoom.getStatus());
			roomType.setValue(rRoom.getType());
			roomCost.setText("" + rRoom.getCost());
			roomPatios.setValue(rRoom.getPatios());
			roomForests.setValue(rRoom.getForests());
			insertRoom.getChildren().removeAll(roomAdd, roomUpdate);
			insertRoom.getChildren().add(roomUpdate);
		});
		update.setVisible(isManager);
		MenuItem delete = new MenuItem("Delete");
			delete.setOnAction(e -> {roomTable.getSelectionModel().getSelectedItem().deleteRoom();
				roomTable.getItems().remove(roomTable.getSelectionModel().getSelectedItem());});
			delete.setVisible(isManager);
		rightClick.getItems().addAll(clean, repair, update,delete);
		
		TableColumn<Room, String> rNameColum = new TableColumn<>("Name");
		rNameColum.setMinWidth(75);
		rNameColum.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		TableColumn<Room, String> rTypeColumn = new TableColumn<>("Type");
		rTypeColumn.setMinWidth(75);
		rTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
		
		TableColumn<Room, Double> rCostColumn = new TableColumn<>("Cost");
		rCostColumn.setMinWidth(75);
		rCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
		
		TableColumn<Room, String> rLocationColumn = new TableColumn<>("Location");
		rLocationColumn.setMinWidth(75);
		rLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
		
		TableColumn<Room, String> rConditionColumn = new TableColumn<>("Condition");
		rConditionColumn.setMinWidth(75);
		rConditionColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));
		
		TableColumn<Room, String> rStatusColumn = new TableColumn<>("Status");
		rStatusColumn.setMinWidth(75);
		rStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
		
		TableColumn<Room, Integer> rPatiosColumn = new TableColumn<>("# of Patios");
		rPatiosColumn.setMinWidth(75);
		rPatiosColumn.setCellValueFactory(new PropertyValueFactory<>("patios"));
		
		TableColumn<Room, Integer> rForestsColumn = new TableColumn<>("# of Forests");
		rForestsColumn.setMinWidth(75);
		rForestsColumn.setCellValueFactory(new PropertyValueFactory<>("forests"));
		
		roomTable = new TableView<>();
		roomTable.getColumns().addAll(rNameColum,rTypeColumn,rCostColumn
				, rLocationColumn, rConditionColumn, rStatusColumn, rPatiosColumn, rForestsColumn);
		
		roomTable.addEventHandler(MouseEvent.MOUSE_CLICKED, t ->
		{
			if(t.getButton() == MouseButton.SECONDARY)
			{
				rightClick.show(roomTable, t.getScreenX(), t.getScreenY());
			}
		});
	}
	
		private TableView<Reservation> reservationTable;
	private void createReservationTable()//One and Done)
	{
		ContextMenu rightClick = new ContextMenu();
		//MenuItem open = new MenuItem("Open");
		MenuItem update = new MenuItem("Update");
		update.setOnAction(e -> {
			rReservation = reservationTable.getSelectionModel().getSelectedItem();
			reservationGuestID.setText("" + rReservation.getGuestID());
			reservationRoomID.setText(rReservation.getRoomID());
			reservationCheckIn.setValue(LocalDate.of(rReservation.getCheckIn().getYear() + 1900, rReservation.getCheckIn().getMonth() +1, rReservation.getCheckIn().getDate()));
			reservationCheckOut.setValue(LocalDate.of(rReservation.getCheckOut().getYear() + 1900, rReservation.getCheckOut().getMonth() +1, rReservation.getCheckOut().getDate()));
			reservationOptions.setValue(rReservation.isOptions());
			reservationCycleCost.setText("" + rReservation.getCycleCost());
			insertReservation.getChildren().removeAll(reservationAdd, reservationUpdate);
			insertReservation.getChildren().add(reservationUpdate);
		});
		MenuItem delete = new MenuItem("Delete");
			delete.setOnAction(e -> {reservationTable.getSelectionModel().getSelectedItem().deleteReservation();
			reservationTable.getItems().remove(reservationTable.getSelectionModel().getSelectedItem());});
		rightClick.getItems().addAll(update,delete);
		
		TableColumn<Reservation, String> firstNameColumn = new TableColumn<>("First Name");
		firstNameColumn.setMinWidth(75);
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("guestFirstName"));
		
		TableColumn<Reservation, String> lastNameColumn = new TableColumn<>("Last Name");
		lastNameColumn.setMinWidth(75);
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("guestLastName"));
		
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
		
		reservationTable = new TableView<>();
		reservationTable.getColumns().addAll(firstNameColumn,lastNameColumn,roomNameColumn,
											 checkInColumn, checkOutColumn, cycleCostColumn, optionsColumn);
		reservationTable.addEventHandler(MouseEvent.MOUSE_CLICKED, t ->
		{
			if(t.getButton() == MouseButton.SECONDARY)
			{
				if(isManager)
				rightClick.show(reservationTable, t.getScreenX(), t.getScreenY());
			}
		});
	}
	
		private TableView<Employee> employeeTable;
	private void createEmployeeTable()//One and Done
	{
		ContextMenu rightClick = new ContextMenu();
		//MenuItem open = new MenuItem("Open");
		MenuItem update = new MenuItem("Update");
		update.setOnAction(e -> {
			eEmployee = employeeTable.getSelectionModel().getSelectedItem();
			employeeFirstName.setText(eEmployee.getFirstName());
			employeeLastName.setText(eEmployee.getLastName());
			employeeEmailAddress.setText(eEmployee.getEmailAddress());
			employeePhoneNumber.setText(eEmployee.getPhoneNumber());
			employeePosition.setText(eEmployee.getPosition());
			employeeWage.setText("" + eEmployee.getWage());
			employeeAddress1.setText(eEmployee.getAddress1());
			employeeAddress2.setText(eEmployee.getAddress2());
			employeeCity.setText(eEmployee.getCity());
			employeeState.setText(eEmployee.getState());
			employeeZipcode.setText("" + eEmployee.getZipcode());
			
			eEmployee = employeeTable.getSelectionModel().getSelectedItem();
			insertEmployee.getChildren().removeAll(employeeAdd, employeeUpdate);
			insertEmployee.getChildren().add(employeeUpdate);});
		MenuItem delete = new MenuItem("Delete");
			delete.setOnAction(e -> {employeeTable.getSelectionModel().getSelectedItem().deleteEmployee();
				employeeTable.getItems().remove(employeeTable.getSelectionModel().getSelectedItem());});
		rightClick.getItems().addAll(update, delete);
		
		TableColumn<Employee, String> eFirstNameColumn = new TableColumn<>("First Name");
		eFirstNameColumn.setMinWidth(75);
		eFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		
		TableColumn<Employee, String> eLastNameColumn = new TableColumn<>("Last Name");
		eLastNameColumn.setMinWidth(75);
		eLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		
		TableColumn<Employee, String> ePositionColumn = new TableColumn<>("Position");
		ePositionColumn.setMinWidth(75);
		ePositionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
		
		TableColumn<Employee, Double> eWageColumn = new TableColumn<>("Wage");
		eWageColumn.setMinWidth(75);
		eWageColumn.setCellValueFactory(new PropertyValueFactory<>("wage"));
		
		TableColumn<Employee, Double> eAddressColumn = new TableColumn<>("Address");
		eAddressColumn.setMinWidth(75);
		eAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address1"));
		
		TableColumn<Employee, Double> ePhoneNumberColumn = new TableColumn<>("Phone Number");
		ePhoneNumberColumn.setMinWidth(75);
		ePhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
		
		employeeTable = new TableView<>();
		employeeTable.getColumns().addAll(eFirstNameColumn,eLastNameColumn,ePositionColumn, eWageColumn, eAddressColumn, ePhoneNumberColumn);
		employeeTable.addEventHandler(MouseEvent.MOUSE_CLICKED, t ->
		{
			if(t.getButton() == MouseButton.SECONDARY)
			{
				rightClick.show(employeeTable, t.getScreenX(), t.getScreenY());
			}
		});
	}
	
		private FlowPane insertGuest;
		private TextField guestFirstName, guestLastName, guestEmailAddress, guestPhoneNumber;
		private Button guestAdd, guestUpdate;
		private Guest gGuest;
	private void createGuestInsert()//One and Done
	{
		insertGuest = new FlowPane();
		insertGuest.setPadding(new Insets(5,5,5,5));
		insertGuest.setVgap(5);
		insertGuest.setHgap(5);
		
		guestFirstName = new TextField();
		guestFirstName.setEditable(true);
		guestFirstName.setPromptText("First Name");
			
		guestLastName = new TextField();
		guestLastName.setEditable(true);
		guestLastName.setPromptText("Last Name");
			
		guestEmailAddress = new TextField();
		guestEmailAddress.setEditable(true);
		guestEmailAddress.setPromptText("Email Address");
			
		guestPhoneNumber = new TextField();
			guestPhoneNumber.setEditable(true);
			guestPhoneNumber.setPromptText("Phone Number");
			
			guestAdd = new Button("Add Guest");
			guestAdd.setOnAction(e -> {
				Guest g = new Guest(guestFirstName.getText(), guestLastName.getText(), guestEmailAddress.getText(), guestPhoneNumber.getText());
				if(g.insertGuest())
				{
						guestTable.getItems().add(g);
						guestFirstName.clear();
						guestLastName.clear();
						guestEmailAddress.clear();
						guestPhoneNumber.clear();
						
						
				}
				else
				{
					AlertBox.display("ERROR", g.getErrorMessage());
				}
			});
			
			guestUpdate = new Button("Update Guest");
			guestUpdate.setOnAction(e -> {
				gGuest.setFirstName(guestFirstName.getText());
				gGuest.setLastName(guestLastName.getText());
				gGuest.setEmailAddress(guestEmailAddress.getText());
				gGuest.setPhoneNumber(guestPhoneNumber.getText());
				if(gGuest.updateGuest())
				{
					guestFirstName.clear();
					guestLastName.clear();
					guestEmailAddress.clear();
					guestPhoneNumber.clear();
					guestTable.getItems().clear();
					insertGuest.getChildren().removeAll(guestUpdate, guestAdd);
					insertGuest.getChildren().add(guestAdd);
					try
					{					
						guestTable.getItems().addAll(Guest.getGuestList(";"));
					}
					catch(EmptySetException exc)
					{
						AlertBox.display("Error",exc.getMessage());
					}
				}
				else
				{
					AlertBox.display("Error", gGuest.getErrorMessage());
				}
			});
		insertGuest.getChildren().addAll(guestFirstName, guestLastName, guestEmailAddress, guestPhoneNumber, guestAdd);
	}
	
		private FlowPane insertRoom;
		private TextField roomID, roomName, roomLocation, roomCost;
		private ComboBox<String> roomType;
		private ComboBox<Integer> roomPatios, roomForests;
		private ComboBox<String> roomCondition, roomStatus;
		private Button roomAdd, roomUpdate;
		private Room rRoom;
	private void createRoomInsert()//One and Done
	{
		insertRoom = new FlowPane();
		insertRoom.setPadding(new Insets(5,5,5,5));
		insertRoom.setVgap(5);
		insertRoom.setHgap(5);
		
		roomID = new TextField();
		roomID.setEditable(true);
		roomID.setPromptText("Room ID");
			
		roomName = new TextField();
		roomName.setEditable(true);
		roomName.setPromptText("Name");
			
		roomLocation = new TextField();
		roomLocation.setEditable(true);
		roomLocation.setPromptText("Location");
		
		roomCondition = new ComboBox<>();
		roomCondition.setEditable(false);
		roomCondition.getItems().addAll("Unready", "Cleaned", "Repaired", "Ready");
		roomCondition.setPromptText("Room Condition");
		
		roomStatus = new ComboBox<>();
		roomStatus.setEditable(false);
		roomStatus.getItems().addAll("Occupied","Unoccupied");
		roomStatus.setPromptText("Room Status");
			
		roomType = new ComboBox<>();
		roomType.setEditable(false);
		roomType.getItems().addAll("Cottage2","Cottage3","Cottage4",
					 "Queen1Bed","Queen2Bed","TwoRoom", "ThreeRoom","Bridal");
		roomType.setPromptText("Type");

		roomPatios = new ComboBox<>();
		roomPatios.setEditable(false);
		roomPatios.getItems().addAll(1,2);
		roomPatios.setPromptText("Patios");

		roomForests = new ComboBox<>();
		roomForests.setEditable(false);
		roomForests.getItems().addAll(1,2);
		roomForests.setPromptText("Forest");
		
		roomCost = new TextField();
		roomCost.setEditable(true);
		roomCost.setPromptText("Cost");
		
		roomAdd = new Button("Add Room");
		roomAdd.setOnAction(e -> {
			Room r = new Room(roomID.getText(), roomName.getText(), roomLocation.getText(), "Ready", "Unoccupied", roomType.getValue(), Double.parseDouble(roomCost.getText()), roomPatios.getValue(), roomForests.getValue());
			if(r.insertRoom())
			{
				roomTable.getItems().add(r);
				roomID.clear();
				roomName.clear();
				roomLocation.clear();
				roomType.setPromptText("Type");
				roomPatios.setPromptText("Patios");
				roomForests.setPromptText("Forest");
				roomCost.clear();
			}
		
		});
		
		roomUpdate = new Button("Update Room");
		roomUpdate.setOnAction(e -> {
			rRoom.setName(roomName.getText());
			rRoom.setLocation(roomLocation.getText());
			rRoom.setCondition(roomCondition.getValue());
			rRoom.setCost(Double.parseDouble(roomCost.getText()));
			rRoom.setPatios(roomPatios.getValue());
			rRoom.setForests(roomForests.getValue());
			rRoom.setStatus(roomStatus.getValue());
			rRoom.setType(roomType.getValue());
			if(rRoom.updateRoom())
			{
				roomID.clear();
				roomName.clear();
				roomLocation.clear();
				roomType.setPromptText("Type");
				roomPatios.setPromptText("Patios");
				roomForests.setPromptText("Forest");
				roomCost.clear();
				roomTable.getItems().clear();
				insertRoom.getChildren().removeAll(roomUpdate, roomAdd);
				insertRoom.getChildren().add(roomAdd);
				try
				{					
					roomTable.getItems().addAll(Room.getRoomList(";"));
				}
				catch(EmptySetException exc)
				{
					AlertBox.display("Error",exc.getMessage());
				}
			}
			else
			{
				AlertBox.display("Error",rRoom.getErrorMessage());
			}
		});
		
		insertRoom.getChildren().addAll(roomID, roomName, roomLocation, roomCondition, roomStatus, roomType, roomPatios, roomForests, roomCost, roomAdd);
	}
	
		private FlowPane insertReservation;
		private TextField reservationRoomID, reservationGuestID, reservationCycleCost;
		private DatePicker reservationCheckIn, reservationCheckOut;
		private ComboBox<Boolean> reservationOptions;
		Room r; Guest g; String gFirstName; String gLastName; String gEmailAddress; String rName;
		private Button reservationAdd, reservationUpdate;
		private Reservation rReservation;
		private boolean optionSelected = false;
	private void createReservationInsert()//One and Done
	{

		insertReservation = new FlowPane();
		insertReservation.setPadding(new Insets(5,5,5,5));
		insertReservation.setVgap(5);
		insertReservation.setHgap(5);
		
			
			Button reservationGuest = new Button("Pick Guest");
			reservationGuest.setOnAction(e -> {
				g = GuestScene.guestSearch();
				reservationGuestID.setText("" + g.getGuestID());
				gFirstName = g.getFirstName();
				gLastName = g.getLastName();
				gEmailAddress = g.getEmailAddress();});
			
			Button reservationRoom = new Button("Pick Room");
			reservationRoom.setOnAction(e -> {
				r = RoomScene.roomSearch(reservationCheckIn.getValue(),reservationCheckOut.getValue());
				reservationRoomID.setText(r.getRoomID());
				reservationCycleCost.setText("" + r.getCost());
				rName = r.getName();});

			reservationRoomID = new TextField();
			reservationRoomID.setEditable(false);
			reservationRoomID.setPromptText("Room ID");
			
			reservationGuestID = new TextField();
			reservationGuestID.setEditable(false);
			reservationGuestID.setPromptText("Guest ID");
			 
			 //checkInCalender
			reservationCheckIn = new DatePicker();
			reservationCheckIn.setValue(LocalDate.now());
			
			 //checkOutCalender
			reservationCheckOut = new DatePicker();
			
			final Callback<DatePicker, DateCell> checkInFactory= 
		            new Callback<DatePicker, DateCell>() {
		                @Override
		                public DateCell call(final DatePicker datePicker) {
		                    return new DateCell() {
		                        @Override
		                        public void updateItem(LocalDate item, boolean empty) {
		                            super.updateItem(item, empty);
		                            if (item.isBefore(LocalDate.now()))
		                            {
		                                    setDisable(true);
		                                    setStyle("-fx-background-color: #ffc0cb;");
		                            }
		                            try
		                            {
		                            ArrayList<Reservation> aList = Reservation.getReservationList("Room ID",reservationRoomID.getText());
		                            for(int i = 0; i < aList.size(); i++)
			                            {
		                            		Date start = aList.get(i).getCheckIn();
		                            		Date end = aList.get(i).getCheckOut();
		                            		LocalDate startL = LocalDate.of(start.getYear() + 1900, start.getMonth()+1, start.getDate());
		                            		LocalDate endL = LocalDate.of(end.getYear() + 1900, end.getMonth()+1, end.getDate());
			                            	
		                            		if(item.isBefore(endL) && item.isAfter(startL.minusDays(1)))
		                            		{
		                            			setDisable(true);
			                                    setStyle("-fx-background-color: #ffc0cb;");
		                            		}
			                            }
		                            }
		                            catch (EmptySetException exc) {}
		                            
		                        }
		                };
		            }
		        };
		        
		    reservationCheckIn.setDayCellFactory(checkInFactory);
		    
		    reservationCheckIn.setOnAction(e -> {reservationCheckOut.setValue(reservationCheckIn.getValue().plusDays(1));});
			 
			final Callback<DatePicker, DateCell> checkOutFactory= 
		            new Callback<DatePicker, DateCell>() {
		                @Override
		                public DateCell call(final DatePicker datePicker) {
		                    return new DateCell() {
		                        @Override
		                        public void updateItem(LocalDate item, boolean empty) {
		                            super.updateItem(item, empty);
		                            if (item.isBefore(reservationCheckIn.getValue().plusDays(1)))
		                            {
		                                    setDisable(true);
		                                    setStyle("-fx-background-color: #ffc0cb;");
		                            }
		                            long p = ChronoUnit.DAYS.between(reservationCheckOut.getValue(), item );
		                            setTooltip(new Tooltip( "You're about to stay for " + p + " days") );
		                            try
		                            {
		                            ArrayList<Reservation> aList = Reservation.getReservationList("Room ID",reservationRoomID.getText());
		                            for(int i = 0; i < aList.size(); i++)
			                            {
		                            		Date start = aList.get(i).getCheckIn();
		                            		Date end = aList.get(i).getCheckOut();
		                            		LocalDate startL = LocalDate.of(start.getYear() + 1900, start.getMonth()+1, start.getDate());
		                            		LocalDate endL = LocalDate.of(end.getYear() + 1900, end.getMonth()+1, end.getDate());
			                            	
		                            		if( (item.isBefore(endL) && item.isAfter(startL)) ||
		                            			(reservationCheckIn.getValue().isBefore(startL)	&& item.isAfter(endL.minusDays(1))))
		                            		{
		                            			setDisable(true);
			                                    setStyle("-fx-background-color: #ffc0cb;");
		                            		}
			                            }
		                            }
		                            catch (EmptySetException exc) {}
		                        }
		                };
		            }
		        };
		        
		    reservationCheckOut.setDayCellFactory(checkOutFactory);

			reservationCheckIn.setValue(LocalDate.now());
			reservationCheckOut.setValue(LocalDate.now().plusDays(1));
			
			reservationOptions = new ComboBox<>();
			reservationOptions.setEditable(false);
			reservationOptions.getItems().addAll(true, false);
			reservationOptions.setPromptText("Options");
			reservationOptions.setOnAction(e -> {
				if(reservationOptions.getValue() && !optionSelected)
				{
				reservationCycleCost.setText(""+ (12.00 + Double.parseDouble(reservationCycleCost.getText())));
				optionSelected = true;
				}
				else if(!reservationOptions.getValue() && optionSelected)
				{
					reservationCycleCost.setText(""+ (Double.parseDouble(reservationCycleCost.getText()) - 12.00));
					optionSelected = false;
				}});
			 
			 reservationCycleCost = new TextField();
			 reservationCycleCost.setEditable(true);
			 reservationCycleCost.setPromptText("Cycle Cost");
			
			reservationAdd = new Button("Add Reservation");
			reservationAdd.setOnAction(e -> {
				Reservation res = new Reservation(Integer.parseInt(reservationGuestID.getText()), reservationRoomID.getText(),
						Date.valueOf(reservationCheckIn.getValue()), Date.valueOf(reservationCheckOut.getValue()), reservationOptions.getValue(),Double.parseDouble(reservationCycleCost.getText()),
						gFirstName, gLastName, gEmailAddress, rName);
		
				if(res.insertReservation())
				{
					try
					{
						Guest g = new Guest(res.getGuestID());
						if(g.setCreditCard()){
							reservationTable.getItems().add(res);
							reservationRoomID.clear();
							reservationGuestID.clear();
							reservationCheckIn.setValue(LocalDate.now());
							reservationCheckOut.setValue(LocalDate.now());
							reservationOptions.setPromptText("Options");
							reservationCycleCost.clear();
							optionSelected = false;
						}
						else if(AlertBox.guestCardInfo(g))
						{
							reservationTable.getItems().add(res);
							reservationRoomID.clear();
							reservationGuestID.clear();
							reservationCheckIn.setValue(LocalDate.now());
							reservationCheckOut.setValue(LocalDate.now());
							reservationOptions.setPromptText("Options");
							reservationCycleCost.clear();
							optionSelected = false;
						}
						else
						{
							AlertBox.display("Error", g.getErrorMessage());
							res.deleteReservation();
						}
					
					}
					catch( EmptySetException exc)
					{
						AlertBox.display("Error", exc.getMessage());
						res.deleteReservation();
					}
					
				}
				else
					AlertBox.display("Error", res.getErrorMessage());});
			

			
			reservationUpdate = new Button("Update Reservation");
			reservationUpdate.setOnAction(e -> {
				rReservation.setCheckIn(Date.valueOf(reservationCheckIn.getValue()));
				rReservation.setCheckOut(Date.valueOf(reservationCheckOut.getValue()));
				rReservation.setOptions(reservationOptions.getValue());
				if(reservationOptions.getValue())
					optionSelected = true;
				else
					optionSelected = false;
				rReservation.setCycleCost(Double.parseDouble(reservationCycleCost.getText()));
				if(rReservation.updateReservation())
				{
					reservationRoomID.clear();
					reservationGuestID.clear();
					reservationCheckIn.setValue(LocalDate.now());
					reservationCheckOut.setValue(LocalDate.now());
					reservationOptions.setPromptText("Options");
					reservationCycleCost.clear();
					reservationTable.getItems().clear();
					insertReservation.getChildren().removeAll(reservationUpdate, reservationAdd);
					insertReservation.getChildren().add(reservationAdd);
					optionSelected = false;
					try
					{					
						reservationTable.getItems().addAll(Reservation.getReservationList(";"));
					}
					catch(EmptySetException exc)
					{
						AlertBox.display("Error",exc.getMessage());
					}
				}
				else
				{
					AlertBox.display("Error", rReservation.getErrorMessage());
				}
			});
			
			insertReservation.getChildren().addAll(reservationGuest, reservationRoom, reservationGuestID, reservationRoomID, reservationCheckIn, reservationCheckOut, reservationOptions, reservationCycleCost, reservationAdd);
	}
	
		private FlowPane insertEmployee;
		private TextField employeeFirstName, employeeLastName, employeeEmailAddress, employeePhoneNumber, employeePosition, employeeWage,
			employeeAddress1, employeeAddress2, employeeCity, employeeState, employeeZipcode;
			private Button employeeAdd, employeeUpdate;
			private Employee eEmployee;
	private void createEmployeeInsert()//One and Done
	{
		insertEmployee = new FlowPane();
		insertEmployee.setPadding(new Insets(5,5,5,5));
		insertEmployee.setVgap(5);
		insertEmployee.setHgap(5);
		
		employeeFirstName = new TextField();
		employeeFirstName.setEditable(true);
		employeeFirstName.setPromptText("First Name");
			
		employeeLastName = new TextField();
		employeeLastName.setEditable(true);
		employeeLastName.setPromptText("Last Name");
			
			employeeEmailAddress = new TextField();
			employeeEmailAddress.setEditable(true);
			employeeEmailAddress.setPromptText("Email Address");
			
			employeePhoneNumber = new TextField();
			employeePhoneNumber.setEditable(true);
			employeePhoneNumber.setPromptText("Phone Number");
			
			employeeAddress1 = new TextField();
			employeeAddress1.setEditable(true);
			employeeAddress1.setPromptText("Address1");
		
			employeeAddress2 = new TextField();
			employeeAddress2.setEditable(true);
			employeeAddress2.setPromptText("Address2");
			
			employeeCity = new TextField();
			employeeCity.setEditable(true);
			employeeCity.setPromptText("City");
			
			employeeState = new TextField();
			employeeState.setEditable(true);
			employeeState.setPromptText("State");
			
			employeeZipcode = new TextField();
			employeeZipcode.setEditable(true);
			employeeZipcode.setPromptText("Zipcode");
			
			employeePosition = new TextField();
			employeePosition.setEditable(true);
			employeePosition.setPromptText("Position");
			
			employeeWage = new TextField();
			employeeWage.setEditable(true);
			employeeWage.setPromptText("Wage");
			
			employeeAdd = new Button("Add Employee");
			employeeAdd.setOnAction(e -> {
				try {
				Employee emp = new Employee(employeeFirstName.getText(), employeeLastName.getText(), employeeEmailAddress.getText(), employeePhoneNumber.getText(), employeePosition.getText(), Double.parseDouble(employeeWage.getText()), employeeAddress1.getText(), employeeAddress2.getText(), employeeCity.getText(), employeeState.getText(), Integer.parseInt(employeeZipcode.getText()));
				if(emp.insertEmployee())
				{
					employeeTable.getItems().add(emp);
					employeeFirstName.clear();
					employeeLastName.clear();
					employeeEmailAddress.clear();
					employeePhoneNumber.clear();
					employeePosition.clear();
					employeeWage.clear();
					employeeAddress1.clear();
					employeeAddress2.clear();
					employeeCity.clear();
					employeeState.clear();
					employeeZipcode.clear();
					AlertBox.addEmployeeSecurity(emp);
				}
				else
				{
					AlertBox.display("Error", emp.getErrorMessage());
				}}
				catch(NumberFormatException exc)
				{
					AlertBox.display("Number Format Error", exc.getMessage());
				}
			});
			
			employeeUpdate = new Button("Update Employee");
			employeeUpdate.setOnAction(e -> {
				eEmployee.setFirstName(employeeFirstName.getText());
				eEmployee.setLastName(employeeLastName.getText());
				eEmployee.setEmailAddress(employeeEmailAddress.getText());
				eEmployee.setPhoneNumber(employeePhoneNumber.getText());
				eEmployee.setPosition(employeePosition.getText());
				eEmployee.setWage(Double.parseDouble(employeeWage.getText()));
				eEmployee.setAddress1(employeeAddress1.getText());
				if(employeeAddress2.getText() != null) eEmployee.setAddress2(employeeAddress2.getText()); else eEmployee.setAddress2("");
				eEmployee.setCity(employeeCity.getText());
				eEmployee.setState(employeeState.getText());
				eEmployee.setZipcode(Integer.parseInt(employeeZipcode.getText()));
				if(eEmployee.updateEmployee())
				{
					employeeFirstName.clear();
					employeeLastName.clear();
					employeeEmailAddress.clear();
					employeePhoneNumber.clear();
					employeePosition.clear();
					employeeWage.clear();
					employeeAddress1.clear();
					employeeAddress2.clear();
					employeeCity.clear();
					employeeState.clear();
					employeeZipcode.clear();
					employeeTable.getItems().clear();
					insertEmployee.getChildren().removeAll(employeeUpdate,employeeAdd);
					insertEmployee.getChildren().add(employeeAdd);
					try
					{					
						employeeTable.getItems().addAll(Employee.getEmployeeList(";"));
					}
					catch(EmptySetException exc)
					{
						AlertBox.display("Error",exc.getMessage());
					}
				}
				else
				{
					AlertBox.display("Error", eEmployee.getErrorMessage());
				}
			});
			
		insertEmployee.getChildren().addAll(employeeFirstName, employeeLastName, employeeEmailAddress, employeePhoneNumber, employeePosition, employeeWage,
					employeeAddress1, employeeAddress2,employeeCity, employeeState, employeeZipcode, employeeAdd);
		
	}
	
	private ObservableList<Guest> getGuestTable()
	{
		ObservableList<Guest> oList = FXCollections.observableArrayList();
		try
		{
			ArrayList<Guest> aList;
			switch(secondCondition)
			{
			case 0: aList = Guest.getGuestList(firstCondition,textEntry[0].getText()); break;
			default:  aList = Guest.getGuestList(";");
			}

		for(int i = 0; i < aList.size(); i++)
		{
			oList.add(aList.get(i));
		}
		}
		catch(EmptySetException e)
		{
			AlertBox.display("Error", e.getMessage());
		}
		
		return oList;
	}

	private ObservableList<Room> getRoomTable()
	{
		ObservableList<Room> oList = FXCollections.observableArrayList();
		try
		{
		ArrayList<Room> aList;
		switch(secondCondition)
		{
		case 0: aList = Room.getRoomList(firstCondition,textEntry[0].getText()); break;
		case 1: aList = Room.getRoomList(firstCondition,choiceIntEntry[0].getValue()); break;
		case 2: aList = Room.getRoomList(firstCondition,Double.parseDouble(textEntry[0].getText())); break;
		default:  aList = Room.getRoomList(";");
		}
		for(int i = 0; i < aList.size(); i++)
		{
			oList.add(aList.get(i));
		}
		}
		catch(EmptySetException e)
		{
			
		}
		
		return oList;
	}
	
	private ObservableList<Reservation> getReservationTable()
	{
		ObservableList<Reservation> oList = FXCollections.observableArrayList();
		try
		{
		ArrayList<Reservation> aList;
			switch(secondCondition)
			{
			case 0: aList = Reservation.getReservationList(firstCondition, textEntry[0].getText()); break;
			case 2: aList = Reservation.getReservationList(firstCondition, Double.parseDouble(textEntry[0].getText())); break;
			case 3: aList = Reservation.getReservationList(firstCondition, choiceBooleanEntry[0].getValue()); break;
			case 4: aList = Reservation.getReservationList(firstCondition, Date.valueOf(dateEntry[0].getValue())); break;
				default: aList = Reservation.getReservationList(";");
			}

		for(int i = 0; i < aList.size(); i++)
		{
			oList.add(aList.get(i));
		}
		}
		catch(EmptySetException e)
		{
			
		}
		
		return oList;
	}

	private ObservableList<Employee> getEmployeeTable()
	{
		ObservableList<Employee> oList = FXCollections.observableArrayList();
		try
		{
		ArrayList<Employee> aList;
		switch(secondCondition)
		{
		case 0: aList = Employee.getEmployeeList(firstCondition, textEntry[0].getText()); break;
		case 1: aList = Employee.getEmployeeList(firstCondition, Integer.parseInt(textEntry[0].getText())); break;
		case 2: aList = Employee.getEmployeeList(firstCondition, Double.parseDouble(textEntry[0].getText())); break;
			default: aList = Employee.getEmployeeList(";");
		}
		for(int i = 0; i < aList.size(); i++)
		{
			oList.add(aList.get(i));
		}
		}
		catch(EmptySetException e)
		{
			
		}
		
		return oList;
	}

	private ObservableList<String> choiceList;
	private void setObservableChoiceBoxList()
	{
		choiceList = FXCollections.observableArrayList();
		
		for(int j = 1; j < Guest.DISPLAY_ATTRIBUTE.length; j++)
			choiceList.add(Guest.DISPLAY_ATTRIBUTE[j]);
		for(int j = 0; j < Room.DISPLAY_ATTRIBUTE.length; j++)
			choiceList.add(Room.DISPLAY_ATTRIBUTE[j]);
		for(int j = 0; j < Reservation.DISPLAY_ATTRIBUTE.length; j++)
			if(j!= 4 && j != 5) choiceList.add(Reservation.DISPLAY_ATTRIBUTE[j]);
		for(int j = 0; j < Employee.DISPLAY_ATTRIBUTE.length; j++)
			choiceList.add(Employee.DISPLAY_ATTRIBUTE[j]);
	}
	
	private void setGuestScene()
	{
		textEntry[0].clear();
		dateEntry[0].setValue(LocalDate.now());
		
		guestFirstName.clear();
		guestLastName.clear();
		guestEmailAddress.clear();
		guestPhoneNumber.clear();
		secondCondition = -1;
		for(int i = 0; i < choiceBox.length; i++)
		{
			choiceList.clear();
			for(int j = 1; j < Guest.DISPLAY_ATTRIBUTE.length; j++)
				choiceList.add(Guest.DISPLAY_ATTRIBUTE[j]);
			choiceBox[i].getItems().setAll(choiceList);
			choiceBox[i].setValue(Guest.DISPLAY_ATTRIBUTE[(i+1)%Guest.DISPLAY_ATTRIBUTE.length]);
		}
		
		guestTable.getItems().clear();
		guestTable.getItems().addAll(getGuestTable());
		
		innerPane.getChildren().removeAll(roomTable, reservationTable, employeeTable, insertRoom, insertReservation, insertEmployee);
		innerPane.setCenter(guestTable);
		innerPane.setBottom(insertGuest);
		insertGuest.setVisible(isReceptionist);
		
		search.setOnAction(e -> {
			craftSQLConditions();
			guestTable.getItems().clear();
			guestTable.getItems().addAll(getGuestTable());
		});
	}
	
	private void setRoomScene()
	{
		textEntry[0].clear();
		dateEntry[0].setValue(LocalDate.now());
	
		roomID.clear();
		roomName.clear();
		roomLocation.clear();
		roomType.setPromptText("Type");
		roomPatios.setPromptText("Patios");
		roomForests.setPromptText("Forest");
		roomCost.clear();
		
		secondCondition = -1;
		for(int i = 0; i < choiceBox.length; i++)
		{	
			choiceList.clear();
			for(int j = 0; j < Room.DISPLAY_ATTRIBUTE.length; j++)
				choiceList.add(Room.DISPLAY_ATTRIBUTE[j]);
			choiceBox[i].getItems().setAll(choiceList);
			choiceBox[i].setValue(Room.DISPLAY_ATTRIBUTE[i%Room.DISPLAY_ATTRIBUTE.length]);
		}
		
		roomTable.getItems().clear();
		roomTable.getItems().addAll(getRoomTable());

		
		innerPane.getChildren().removeAll(guestTable, reservationTable, employeeTable, insertGuest, insertReservation, insertEmployee);
		innerPane.setCenter(roomTable);
		innerPane.setBottom(insertRoom);
		insertRoom.setVisible(isManager);
		
		search.setOnAction(e -> {
			craftSQLConditions();
			roomTable.getItems().clear();
			roomTable.getItems().addAll(getRoomTable());
		});
	}
	
	private void setReservationScene()
	{
		textEntry[0].clear();
		dateEntry[0].setValue(LocalDate.now());
		reservationRoomID.clear();
		reservationGuestID.clear();
		reservationCheckIn.setValue(LocalDate.now());
		reservationCheckOut.setValue(LocalDate.now());
		reservationOptions.setPromptText("Options");
		reservationCycleCost.clear();
		
		secondCondition = -1;
		for(int i = 0; i < choiceBox.length; i++)
		{
			choiceList.clear();
			for(int j = 0; j < Reservation.DISPLAY_ATTRIBUTE.length; j++)
				if(j!= 4 && j != 5) choiceList.add(Reservation.DISPLAY_ATTRIBUTE[j]);
			choiceBox[i].getItems().setAll(choiceList);
			choiceBox[i].setValue(Reservation.DISPLAY_ATTRIBUTE[i%Reservation.DISPLAY_ATTRIBUTE.length]);
		}
		
		reservationTable.getItems().clear();
		reservationTable.getItems().addAll(getReservationTable());

		innerPane.getChildren().removeAll(roomTable, guestTable, employeeTable, insertRoom, insertGuest, insertEmployee);
		innerPane.setCenter(reservationTable);
		innerPane.setBottom(insertReservation);
		insertReservation.setVisible(isReceptionist);
		
		search.setOnAction(e -> {
			craftSQLConditions();
			reservationTable.getItems().clear();
			reservationTable.getItems().addAll(getReservationTable());
			});
	}
	
	private void setEmployeeScene()
	{
		textEntry[0].clear();
		dateEntry[0].setValue(LocalDate.now());
		employeeFirstName.clear();
		employeeLastName.clear();
		employeeEmailAddress.clear();
		employeePhoneNumber.clear();
		employeePosition.clear();
		employeeWage.clear();
		employeeAddress1.clear();
		employeeAddress2.clear();
		employeeCity.clear();
		employeeState.clear();
		employeeZipcode.clear();
		
		secondCondition = -1;
		for(int i = 0; i < choiceBox.length; i++)
		{	choiceList.clear();
			for(int j = 0; j < Employee.DISPLAY_ATTRIBUTE.length; j++)
				choiceList.add(Employee.DISPLAY_ATTRIBUTE[j]);
			choiceBox[i].getItems().setAll(choiceList);
			choiceBox[i].setValue(Employee.DISPLAY_ATTRIBUTE[i%Employee.DISPLAY_ATTRIBUTE.length]);
		}
		
		employeeTable.getItems().clear();
		employeeTable.getItems().addAll(getEmployeeTable());

		
		innerPane.getChildren().removeAll(roomTable, reservationTable, guestTable, insertRoom, insertReservation, insertGuest);
		innerPane.setCenter(employeeTable);
		innerPane.setBottom(insertEmployee);
		
		search.setOnAction(e -> {
			craftSQLConditions();
			employeeTable.getItems().clear();
			employeeTable.getItems().addAll(getEmployeeTable());
			});
	}
	
	

}
