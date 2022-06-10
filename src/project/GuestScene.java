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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.layout.*;

public class GuestScene
{
	private static Scene guestScene;
	private static BorderPane inPane;
	private static HBox searchBox;
		private static VBox option1, option2, option3;
			private static TextField entry1, entry2, entry3;
			private static ChoiceBox<String> choiceBox1, choiceBox2, choiceBox3;

	private static TableView<Guest> guestTable;
	
	private static Guest guest;
	public static Guest guestSearch()
	{
		guest = new Guest();
		Stage guestStage = new Stage();
		guestStage.initModality(Modality.APPLICATION_MODAL);
		guestStage.setTitle("Guest Search");
		
		createGuestTable();
		inPane = new BorderPane();
		inPane.setPadding(new Insets(5,5,5,5));
		
				searchBox = new HBox(8);
				searchBox.setPadding(new Insets(5,5,5,5));
				
					option1 = new VBox(8);
					option1.setPadding(new Insets(5,5,5,5));
						entry1 = new TextField();
						entry1.setEditable(true);
						choiceBox1 = new ChoiceBox<>();
						choiceBox1.getItems().addAll("First Name", "Last Name",
								 "Email", "Phone Number");
						choiceBox1.setValue("First Name");
						choiceBox1.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue) -> entry1.setPromptText(newValue));
					
					option1.getChildren().addAll(choiceBox1, entry1);

					StackPane center1 = new StackPane();
						ChoiceBox<String> andOr1 = new ChoiceBox<>();
						andOr1.getItems().addAll("AND","OR");
						andOr1.setValue("OR");
					center1.getChildren().add(andOr1);
					
					option2 = new VBox(8);
					option2.setPadding(new Insets(5,5,5,5));
						entry2 = new TextField();
						entry2.setEditable(true);
						choiceBox2 = new ChoiceBox<>();
						choiceBox2.getItems().addAll("First Name", "Last Name",
								 "Email", "Phone Number");
						choiceBox2.setValue("Last Name");
						choiceBox2.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue) -> entry2.setPromptText(newValue));
					option2.getChildren().addAll(choiceBox2, entry2);

					StackPane center2 = new StackPane();
						ChoiceBox<String> andOr2 = new ChoiceBox<>();
						andOr2.getItems().addAll("AND","OR");
						andOr2.setValue("OR");
					center2.getChildren().add(andOr2);
					
					option3 = new VBox(8);
					option3.setPadding(new Insets(5,5,5,5));
						entry3 = new TextField();
						entry3.setEditable(true);
						choiceBox3 = new ChoiceBox<>();
						choiceBox3.getItems().addAll("First Name", "Last Name",
								 "Email", "Phone Number");
						choiceBox3.setValue("Email");
						choiceBox3.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue) -> entry3.setPromptText(newValue));
					option3.getChildren().addAll(choiceBox3, entry3);

					StackPane center3 = new StackPane();
						Button searchButton = new Button("Search");
						searchButton.setOnAction(e -> {});
						searchButton.setPadding(new Insets(5,5,5,5));
					center3.getChildren().add(searchButton);
					
				searchBox.getChildren().addAll(option1, center1, option2, center2, option3, center3);
				
				BorderPane okPane = new BorderPane();
				Button okButton = new Button("Ok");
				okButton.setOnAction(e -> {
					
					boolean tableSelected = false;
					for(int i = 0; i < guestTable.getItems().size() && !tableSelected; i++)
					{
						tableSelected = guestTable.getSelectionModel().isSelected(i);
					}
						
					if(tableSelected)
					{guest = guestTable.getSelectionModel().getSelectedItem();
					guestStage.close();}
					else
						AlertBox.display("Error", "No Guest Selected");
					
				
				});
				okPane.setRight(okButton);
				
		inPane.setBottom(okPane);
		inPane.setTop(searchBox);
		inPane.setCenter(guestTable);
		
		guestScene = new Scene(inPane, inPane.getPrefHeight(), inPane.getPrefWidth());
		
		guestStage.setScene(guestScene);
		guestStage.showAndWait();
		
		
		return guest;
	}
	
	private static void createGuestTable()
	{
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
		guestTable.setItems(getGuestTable());
		guestTable.getColumns().addAll(gFirstNameColumn,gLastNameColumn,gEmailColumn,gPhoneNumberColumn);
	}
	
	private static ObservableList<Guest> getGuestTable()
	{
		ObservableList<Guest> oList = FXCollections.observableArrayList();
		try
		{
		ArrayList<Guest> aList = Guest.getGuestList(";");

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
	
	public static Scene getGuestScene()
	{
		return guestScene;
	}
}
