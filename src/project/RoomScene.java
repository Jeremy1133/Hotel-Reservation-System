package project;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RoomScene
{
	private static Scene roomScene;
	private static BorderPane inPane;
	private static HBox searchBox;
		private static VBox option1, option2, option3;
			private static TextField entry1, entry2, entry3;
			private static ChoiceBox<String> choiceBox1, choiceBox2, choiceBox3;

	private static TableView<Room> roomTable;
	
	private static Room room;
	public static Room roomSearch(LocalDate start, LocalDate end)
	{
		room = new Room();
		Stage roomStage = new Stage();
		roomStage.initModality(Modality.APPLICATION_MODAL);
		roomStage.setTitle("Room Search");
		
		createRoomTable(start, end);
		
		inPane = new BorderPane();
		inPane.setPadding(new Insets(5,5,5,5));
		
				searchBox = new HBox(8);
				searchBox.setPadding(new Insets(5,5,5,5));
				
					option1 = new VBox(8);
					option1.setPadding(new Insets(5,5,5,5));
						entry1 = new TextField();
						entry1.setEditable(true);
						choiceBox1 = new ChoiceBox<>();
						choiceBox1.getItems().addAll("Name", "Type",
								 "Cost", "# Patios", "# Forests");
						choiceBox1.setValue("Name");
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
						choiceBox2.getItems().addAll("Name", "Type",
								 "Cost", "# Patios", "# Forests");
						choiceBox2.setValue("Type");
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
						choiceBox3.getItems().addAll("Name", "Type",
								 "Cost", "# Patios", "# Forests");
						choiceBox3.setValue("Cost");
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
					for(int i = 0; i < roomTable.getItems().size() && !tableSelected; i++)
					{
						tableSelected = roomTable.getSelectionModel().isSelected(i);
					}
						
					if(tableSelected)
					{room = roomTable.getSelectionModel().getSelectedItem();
					roomStage.close();}
					else
						AlertBox.display("Error", "No Room Selected");}
				);
				okPane.setRight(okButton);
				
		inPane.setBottom(okPane);
		inPane.setTop(searchBox);
		inPane.setCenter(roomTable);
		
		roomScene = new Scene(inPane, inPane.getPrefHeight(), inPane.getPrefWidth());
		
		roomStage.setScene(roomScene);
		roomStage.showAndWait();
		
		return room;
	}
	
	private static void createRoomTable(LocalDate start, LocalDate end)
	{
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
		roomTable.setItems(getRoomTable(start,end));
		roomTable.getColumns().addAll(rNameColum,rTypeColumn,rCostColumn
				, rLocationColumn, rConditionColumn, rStatusColumn, rPatiosColumn, rForestsColumn);
	}
	
	public static ObservableList<Room> getRoomTable(LocalDate start, LocalDate end)
	{
		ObservableList<Room> oList = FXCollections.observableArrayList();
		try
		{	ArrayList<Room> aList;
			if(start.equals(end))
				aList = Room.getRoomList(";");
			else
				aList = Room.getRoomList(start,end);

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
	
	public static Scene getRoomScene()
	{
		return roomScene;
	}

}
