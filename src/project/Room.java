package project;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Room
{
	public static final String[] DISPLAY_ATTRIBUTE = {"Room ID", "Name", "Location", "Condition", "Status", "Type", "Cost", "Patios", "Forests"};
	public static final String[] COLUMN_ATTRIBUTE = {"roomID", "name", "location", "condition", "status", "type", "cost", "patios", "forest"};
	private String roomID, name, location, condition, status, type; 
	private int patios, forests;
	private double cost;
	private String errorMessage = "";//not in use yet.
	
	public static void main(String[] args)
	{
		Room r;
		String[] name = 
			{"George Washington", "John Adams", "Thomas Jefferson", "James Madison",
					"James Monroe", "John Adams", "Andrew Jackson", "Martin Buren",
					"William Harrison", "John Tyler", "James Polk", "Zachary Taylor",
					"Millard Fillmore", "Franklin Pierce", "James Buchanan", "Abraham Lincon",
					"Andrew Johnson", "Ulysses Grant","Rutherford Hayes", "James Garfield",
					"Chester Arthur", "Grover Cleveland", "Benjamin Harrison", "William McKinley",
					"Theodore Roosevelt", "William Taft", "Woodrow Wilson", "Warren Harding",
					"Calvin Collidge", "Herbery Hoover", "Franklin Roosevelt", "Harry Truman",
					"Dwight Eisenhower", "John F. Kennedy", "Lyndon Johnson", "Richard Nixon",
					"Gerald Ford", "Fimmy Carter", "Ronald Reagan", "George H. Bush",
					"Bill Clinton", "George W. Bush", "Barack Obama", "Donald Trump",
					"Peter Burnett", "John McDougal", "John Bigler", "Neely Johnson",
					"John Weller", "Milton Latham", "John Downey", "Leland Stanford",
					"Frederick Low", "Henry Haight", "Newton Booth", "Romualdo Pacheco",
					"William Irwin", "George Perkins", "George Stoneman", "Washington Bartlett",
					"Robery Waterman", "Henry Markham", "James Budd", "Henry Gage",
					"George Pardee", "James Gillett", "Hiram Johnson", "William Stephens",
					"Friend Richardson", "C.C. Young", "James Rolph", "Frank Merriam",
					"Culbert Olson", "Earl Warren", "Goodwin Knight", "Pat Brown",
					"Ronald Reagan CG", "Jerry Brown", "George Deukmejian", "Pete Wilson",
					"Gray Davis", "Arnold Schwarzenegger", "Jerry Brown", "Gavin Newsom",
					"Alexander Bellen", "Jair Bolsonaro", "Hun Sen", "Sebastian Pinera",
					"Miguel Diaz", "Emmanual Macron", "Frank-Walter Steinmeier", "Prokopis Pavlopoulos",
					"Muhammadu Buhari", "Marcelo Rebelo de Sousa", "Andrzej Duda", "Shinzo Abe"	
			};
		String rID = "";
		double c = 0;
		int typeCounter = 0;
		boolean isCottage = true;
		for(int i = 0; i < name.length; i++)
		{
			r = new Room();
			
			r.setName(name[i]);
			
			
			switch(i%8)
			{
			case 0: r.setType("Cottage2"); rID += "C2"; c = 330; isCottage = true; break;
			case 1: r.setType("Cottage3"); rID += "C3"; c = 360; break;
			case 2: r.setType("Cottage4"); rID += "C4"; c = 380; break;
			case 3: r.setType("Queen1Bed"); rID += "Q1"; c = 208; isCottage = false; break;
			case 4: r.setType("Queen2Bed"); rID += "Q2"; c = 238; break;
			case 5: r.setType("TwoRoom"); rID += "R2"; c = 258; break;
			case 6: r.setType("ThreeRoom"); rID += "R3"; c = 308; break;
			case 7: r.setType("Bridal"); rID += "B"; c = 258; typeCounter++; break;
			}
			
			rID += "0" + typeCounter;
			
			switch(typeCounter%4)
			{
			case 0: r.setLocation("North Lake"); r.setForests(1); r.setPatios(1); rID += ".NL.1.1"; if(isCottage) c+=40; else c+=24; break;
			case 1: r.setLocation("East Lake"); r.setForests(1); r.setPatios(2); rID += ".NL.1.2"; if(isCottage) c+=60; else c+=36; break;
			case 2: r.setLocation("South Lake"); r.setForests(2); r.setPatios(1); rID += ".NL.2.1"; if(isCottage) c+=60; else c+=36; break;
			case 3: r.setLocation("West Lake"); r.setForests(2); r.setPatios(2); rID += ".NL.2.2"; if(isCottage) c+=80; else c+=48; break;
			}
			
			r.setCondition("Ready");
			r.setStatus("Unoccupied");
			r.setRoomID(rID);
			r.setCost(c);
			rID = "";
			
			if(!r.insertRoom())
				System.out.println(r.getErrorMessage());
		}
		
//		
//		System.out.println("Creating and printing Edna");
//		Room Edna = new Room();
//		Edna.setName("Edna");
//		Edna.setLocation("East Lake");
//		Edna.setPatios(2);
//		Edna.setForests(1);
//		Edna.setType("Bridal");
//		Edna.setRoomID("B.P2.F1");
//		Edna.setCondition("Ready");
//		Edna.setStatus("Occupied");
//		Edna.setCost(1000.00);
//		System.out.println("Edna Insert Successful? " + Edna.insertRoom());
//		System.out.println(Edna);
//		
//		System.out.println("\nCreating and printing Lialah");
//		Room Lialah = new Room("C3.2P.2F.01","Lialah","North Forest","Unready","Unoccupied","Cottage3",350.00,2,2);
//		System.out.println("Lialah Insert Successful? " + Lialah.insertRoom());
//		System.out.println(Lialah);
//		
//		System.out.println("\nCreating and printing Rose");
//		Room Rose = new Room("C3.1P.1F.02","Rose","South Forest","Repaired","Unoccupied","Queen2Bed",300.00,1,1);
//		System.out.println("Rose Insert Successful? " + Rose.insertRoom());
//		System.out.println(Rose);
//		Rose.setStatus("Occupied");
//		Rose.setCondition("Ready");
//		System.out.println("Rose Update Successful? " + Rose.updateRoom());
//		System.out.println("Rose Post-Update:\n" + Rose);
//		
//		try
//		{
//			System.out.println("\nFetching and Printing entire room list:\n");
//			ArrayList<Room> roomList = Room.getRoomList(";");
//			
//			for(int i = 0; i < roomList.size(); i++)
//			{
//				System.out.println(roomList.get(i));
//			}
//			
//			System.out.println("\nSearching for Edna in Database:");
//			Room Edna2 = new Room(Edna.getRoomID());
//			System.out.println(Edna2);
//		}
//		catch(EmptySetException e)
//		{
//			System.out.println(e.getMessage());
//		}
//		
//		System.out.println("\nDeleting Edna, Lialah, and Rose:");
//		System.out.println("Edna Deletion Successful? " + Edna.deleteRoom());
//		System.out.println("Lialah Deletion Successful? " + Lialah.deleteRoom());
//		System.out.println("Rose Deletion Successful? " + Rose.deleteRoom());
//		
//		try
//		{
//			System.out.println("\nRefetching and Printing entire room list:\n");
//			ArrayList<Room> roomList = Room.getRoomList(";");
//			
//			for(int i = 0; i < roomList.size(); i++)
//			{
//				System.out.println(roomList.get(i));
//			}
//		}
//		catch(EmptySetException e)
//		{
//			System.out.println(e.getMessage());
//		}
		
	}
	//Basic Constructor
	public Room()
	{
		roomID = "";
		name = "";
		location = "";
		condition = "";
		status = "";
		type = "";
		patios = 0;
		forests = 0;
	}
	//Quick set Constructor for creating  Room to insert into database
	public Room(String roomID, String name, String location, String condition, String status, String type, double cost, int patios, int forests)
	{
		this.roomID = roomID;
		this.name = name;
		this.location = location;
		this.condition = condition;
		this.status = status;
		this.type = type;
		this.cost = cost;
		this.patios = patios;
		this.forests = forests;
		
	}
	//Search Constructor for creating a Room from database
	public Room(String roomName)
	{
		roomQuery(roomName);
	}
			//Accessible only from the above Constructor to set Room given roomID or name.
	private void roomQuery(String roomName)
	{
		try
		{
	        // load database driver class
	    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	        // connect to database
	        Connection connection = DriverManager.getConnection(Guest.DBL);
	        
	        // create Statement to query database
	        Statement statement = connection.createStatement();

	        // query database
	        ResultSet resultSet = statement.executeQuery(
	           		"SELECT location, condition, status, type, name, roomID, cost, patios, forests\n"
	           		+ "FROM roomInfo\n"
	           		+ "WHERE name LIKE \"" + roomName +"\" OR roomID LIKE \"" + roomName + "\";");
	        
	        // process query results
	        resultSet.next();//positions on first row
			location = resultSet.getNString(1);
			condition = resultSet.getNString(2);
			status = resultSet.getNString(3);
			type = resultSet.getString(4);
			name = resultSet.getNString(5);
			roomID = resultSet.getNString(6);
			cost = resultSet.getDouble(7);
			patios = resultSet.getInt(8);
			forests = resultSet.getInt(9);
	        // close statement and connection
	        statement.close();
	        connection.close();
	        
	         
		}// end try
	      // detect problems interacting with the database
		catch ( SQLException sqlException ) {
	    	  AlertBox.display("Error", sqlException.getMessage());
	      }
	      catch(ClassNotFoundException cnfex) {
	    	  AlertBox.display("Error", "Problem in loading or "
	                  + "registering MS Access JDBC driver");
	          cnfex.printStackTrace();
	      }
	}
	//Inserts the Room into the database. Returns true if successful.
	public boolean insertRoom()
	{
		if(verifyRoomID() && verifyName() && verifyLocation() && verifyCondition() && verifyStatus()
				&& verifyType() && verifyCost() && verifyPatios() && verifyForests())
			try {
		         // load database driver class
		    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

		         // connect to database
		         Connection connection = DriverManager.getConnection(
		            Guest.DBL);
		         // create Statement to query database
		         PreparedStatement statement = connection.prepareStatement(
		        		"INSERT INTO roomInfo(roomID, name, location, condition, status, type, patios, forests, cost)\n" +
		 		        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
		         
		         statement.setNString(1, roomID);
		         statement.setNString(2, name);
		         statement.setNString(3, location);
		         statement.setNString(4, condition);
		         statement.setNString(5, status);
		         statement.setNString(6, type);
		         statement.setInt(7, patios);
		         statement.setInt(8, forests);
		         statement.setDouble(9, cost);
		         
		         statement.execute();
		         
		         
		         statement.close();
		         connection.close();
		         return true;
		      }  // end try
		      // detect problems interacting with the database
		catch ( SQLException sqlException ) {
	    	  System.out.println(sqlException.getMessage());//AlertBox.display("Error", sqlException.getMessage());
	    	  return false;
	      }
	      catch(ClassNotFoundException cnfex) {
	    	  AlertBox.display("Error", "Problem in loading or "
	                  + "registering MS Access JDBC driver");
	          cnfex.printStackTrace();
	    	  return false;
	      }

 	  return false;
	}
	//Deletes the Room from the database. Returns true if successful.
	public boolean deleteRoom()
	{
		try {
	         // load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            Guest.DBL);
	         // create Statement to query database
	         Statement statement = connection.createStatement();

	         // query database
	         statement.executeUpdate(
	        		"DELETE FROM roomInfo\n" +
	        		"WHERE roomID = \"" + roomID + "\";");
	         
	         connection.close();
	         return true;
	      }  // end try
	      // detect problems interacting with the database
		catch ( SQLException sqlException ) {
	    	  AlertBox.display("Error", sqlException.getMessage());
	    	  return false;
	      }
	      catch(ClassNotFoundException cnfex) {
	    	  AlertBox.display("Error", "Problem in loading or "
	                  + "registering MS Access JDBC driver");
	          cnfex.printStackTrace();
	    	  return false;
	      }
	}
	//Updates the Room in the database. Returns true if successful.
	public boolean updateRoom()
	{
		if(verifyRoomID() && verifyName() && verifyLocation() && verifyCondition() && verifyStatus()
				&& verifyType() && verifyCost() && verifyPatios() && verifyForests())
			try {
		         // load database driver class
		    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

		         // connect to database
		         Connection connection = DriverManager.getConnection(
		            Guest.DBL);
		         // create Statement to query database
		         Statement statement = connection.createStatement();
		         // query database
		         statement.executeUpdate(
		        		"UPDATE roomInfo\n"+
		        		"SET name = \"" + name + "\"\n"+
		        		"WHERE roomID = \"" + roomID + "\";");		         

		         statement.executeUpdate(
			        		"UPDATE roomInfo\n"+
			        		"SET location = \"" + location + "\"\n"+
			        		"WHERE roomID = \"" + roomID + "\";");			         

		         statement.executeUpdate(
			        		"UPDATE roomInfo\n"+
			        		"SET condition = \"" + condition + "\"\n"+
			        		"WHERE roomID = \"" + roomID + "\";");	

		         statement.executeUpdate(
			        		"UPDATE roomInfo\n"+
			        		"SET status = \"" + status + "\"\n"+
			        		"WHERE roomID = \"" + roomID + "\";");
		         
		         statement.executeUpdate(
			        		"UPDATE roomInfo\n"+
			        		"SET type = \"" + type + "\"\n"+
			        		"WHERE roomID = \"" + roomID + "\";");
		         
		         statement.executeUpdate(
			        		"UPDATE roomInfo\n"+
			        		"SET patios = " + patios + "\n"+
			        		"WHERE roomID = \"" + roomID + "\";");	
		         
		         statement.executeUpdate(
			        		"UPDATE roomInfo\n"+
			        		"SET forests = " + forests + "\n"+
			        		"WHERE roomID = \"" + roomID + "\";");	

		         statement.executeUpdate(
			        		"UPDATE roomInfo\n"+
			        		"SET cost = " + cost + "\n"+
			        		"WHERE roomID = \"" + roomID + "\";");	

		         statement.close();
		         connection.close();
		         return true;
		      }  // end try
		      // detect problems interacting with the database
		catch ( SQLException sqlException ) {
	    	  AlertBox.display("Error", sqlException.getMessage());
	    	  return false;
	      }
	      catch(ClassNotFoundException cnfex) {
	    	  AlertBox.display("Error", "Problem in loading or "
	                  + "registering MS Access JDBC driver");
	          cnfex.printStackTrace();
	    	  return false;
	      }
		return false;
	}
	//Given a search Condition, fills an ArrayList of valid Rooms.
	public static ArrayList<Room> getRoomList(String sqlConditions) throws EmptySetException
	{
		ArrayList<Room> roomList = new ArrayList<>();
		try
		{
        // load database driver class
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        // connect to database
        Connection connection = DriverManager.getConnection(Guest.DBL);
        
        // create Statement to query database
        PreparedStatement statement = connection.prepareStatement(
           		"SELECT roomID, name, location, condition, status, type, cost, patios, forests\n"
           		+ "FROM roomInfo \n"
           		+ sqlConditions);
        
        //set Conditions
        
        statement.execute();
        
        ResultSet resultSet = statement.getResultSet();
        
       
        
        int i = 0;
        while(resultSet.next())
        {
        	roomList.add(new Room(
        			resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
        			resultSet.getString(4), resultSet.getString(5), resultSet.getString(6),
        			resultSet.getDouble(7), resultSet.getInt(8), resultSet.getInt(9)
        			));
        i++;
        }
        
        if(i ==0 )
        {
        	throw new EmptySetException("No Rooms Found");
        }
        	
          
        // close statement and connection
        statement.close();
        connection.close();
	}
		catch ( SQLException sqlException ) {
	    	  AlertBox.display("Error", sqlException.getMessage());
	      }
	      catch(ClassNotFoundException cnfex) {
	    	  AlertBox.display("Error", "Problem in loading or "
	                  + "registering MS Access JDBC driver");
	          cnfex.printStackTrace();
	      }

	return roomList;
	}
	
	public static ArrayList<Room> getRoomList(String columnName, String value) throws EmptySetException
	{
		ArrayList<Room> roomList = new ArrayList<>();
		String condition = "WHERE ";
		for(int i = 0 ; i < DISPLAY_ATTRIBUTE.length; i++)
			if(columnName.equals(DISPLAY_ATTRIBUTE[i])) condition += COLUMN_ATTRIBUTE[i];
		condition += " LIKE ?;";
		
		try
		{
        // load database driver class
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        // connect to database
        Connection connection = DriverManager.getConnection(Guest.DBL);
        
        // create Statement to query database
        PreparedStatement statement = connection.prepareStatement(
           		"SELECT roomID, name, location, condition, status, type, cost, patios, forests\n"
           		+ "FROM roomInfo \n"
           		+ condition);
        
        statement.setString(1, value);
        //set Conditions
        
        statement.execute();
        
        ResultSet resultSet = statement.getResultSet();
        
       
        
        int i = 0;
        while(resultSet.next())
        {
        	roomList.add(new Room(
        			resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
        			resultSet.getString(4), resultSet.getString(5), resultSet.getString(6),
        			resultSet.getDouble(7), resultSet.getInt(8), resultSet.getInt(9)
        			));
        i++;
        }
        
        if(i ==0 )
        {
        	throw new EmptySetException("No Rooms Found");
        }
        	
          
        // close statement and connection
        statement.close();
        connection.close();
	}
		catch ( SQLException sqlException ) {
	    	  AlertBox.display("Error", sqlException.getMessage());
	      }
	      catch(ClassNotFoundException cnfex) {
	    	  AlertBox.display("Error", "Problem in loading or "
	                  + "registering MS Access JDBC driver");
	          cnfex.printStackTrace();
	      }

	return roomList;
	}
	
	public static ArrayList<Room> getRoomList(String columnName, int value) throws EmptySetException
	{
		ArrayList<Room> roomList = new ArrayList<>();
		String condition = "WHERE ";
		for(int i = 0 ; i < DISPLAY_ATTRIBUTE.length; i++)
			if(columnName.equals(DISPLAY_ATTRIBUTE[i])) condition += COLUMN_ATTRIBUTE[i];
		condition += " = ?;";
		
		try
		{
        // load database driver class
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        // connect to database
        Connection connection = DriverManager.getConnection(Guest.DBL);
        
        // create Statement to query database
        PreparedStatement statement = connection.prepareStatement(
           		"SELECT roomID, name, location, condition, status, type, cost, patios, forests\n"
           		+ "FROM roomInfo \n"
           		+ condition);
        
        statement.setInt(1, value);
        //set Conditions
        
        statement.execute();
        
        ResultSet resultSet = statement.getResultSet();
        
       
        
        int i = 0;
        while(resultSet.next())
        {
        	roomList.add(new Room(
        			resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
        			resultSet.getString(4), resultSet.getString(5), resultSet.getString(6),
        			resultSet.getDouble(7), resultSet.getInt(8), resultSet.getInt(9)
        			));
        i++;
        }
        
        if(i ==0 )
        {
        	throw new EmptySetException("No Rooms Found");
        }
        	
          
        // close statement and connection
        statement.close();
        connection.close();
	}
		catch ( SQLException sqlException ) {
	    	  AlertBox.display("Error", sqlException.getMessage());
	      }
	      catch(ClassNotFoundException cnfex) {
	    	  AlertBox.display("Error", "Problem in loading or "
	                  + "registering MS Access JDBC driver");
	          cnfex.printStackTrace();
	      }

	return roomList;
	}
	
	public static ArrayList<Room> getRoomList(String columnName, double value) throws EmptySetException
	{
		ArrayList<Room> roomList = new ArrayList<>();
		String condition = "WHERE ";
		for(int i = 0 ; i < DISPLAY_ATTRIBUTE.length; i++)
			if(columnName.equals(DISPLAY_ATTRIBUTE[i])) condition += COLUMN_ATTRIBUTE[i];
		condition += " = ?;";
		
		try
		{
        // load database driver class
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        // connect to database
        Connection connection = DriverManager.getConnection(Guest.DBL);
        
        // create Statement to query database
        PreparedStatement statement = connection.prepareStatement(
           		"SELECT roomID, name, location, condition, status, type, cost, patios, forests\n"
           		+ "FROM roomInfo \n"
           		+ condition);
        
        statement.setDouble(1, value);
        //set Conditions
        
        statement.execute();
        
        ResultSet resultSet = statement.getResultSet();
        
       
        
        int i = 0;
        while(resultSet.next())
        {
        	roomList.add(new Room(
        			resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
        			resultSet.getString(4), resultSet.getString(5), resultSet.getString(6),
        			resultSet.getDouble(7), resultSet.getInt(8), resultSet.getInt(9)
        			));
        i++;
        }
        
        if(i ==0 )
        {
        	throw new EmptySetException("No Rooms Found");
        }
        	
          
        // close statement and connection
        statement.close();
        connection.close();
	}
		catch ( SQLException sqlException ) {
	    	  AlertBox.display("Error", sqlException.getMessage());
	      }
	      catch(ClassNotFoundException cnfex) {
	    	  AlertBox.display("Error", "Problem in loading or "
	                  + "registering MS Access JDBC driver");
	          cnfex.printStackTrace();
	      }

	return roomList;
	}
	
	public static ArrayList<Room> getRoomList(LocalDate startDate, LocalDate endDate) throws EmptySetException
	{
	ArrayList<Room> aList = getRoomList(";");
	try
	{
	        // load database driver class
	    Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	        // connect to database
	        Connection connection = DriverManager.getConnection(Guest.DBL);
	       
	        // create Statement to query database
	        PreparedStatement statement = connection.prepareStatement(
	            "SELECT roomID\n"
	            + "FROM roomInfo INNER JOIN reservationInfo ON roomInfo.roomID = reservationInfo.roomID \n"
	            + "WHERE (checkIn > ? AND checkIn < ?)"//checkIn is between start and end Date
	            + " OR (checkOut > ? AND checkOut < ?)"//checkOut is between start and end Date
	            + " OR    (checkIn < ? AND checkOut > ?)"//startDate is between check In and Out
	            + " OR (checkIn < ? AND checkOut > ?);");//endDate is between check In and Out
	       
	        //set Conditions
	        statement.setDate(1, Date.valueOf(startDate));
	        statement.setDate(2, Date.valueOf(endDate));
	        statement.setDate(3, Date.valueOf(startDate));
	        statement.setDate(4, Date.valueOf(endDate));
	        statement.setDate(5, Date.valueOf(startDate));
	        statement.setDate(6, Date.valueOf(startDate));
	        statement.setDate(7, Date.valueOf(endDate));
	        statement.setDate(8, Date.valueOf(endDate));
	       
	       
	       
	        statement.execute();
	       
	        ResultSet resultSet = statement.getResultSet();
	        int i = 0;
	       
	        String rID = "";
	        while(resultSet.next())
	        {
	        rID = resultSet.getString(1);
	        for(int j = 0; j < aList.size(); j++)
	        if(aList.get(i).getRoomID().equalsIgnoreCase(rID))
	        {
	        aList.remove(i);
	        i--;
	        }
	        }
	       
	         
	        // close statement and connection
	        statement.close();
	        connection.close();
	}
	catch ( SQLException sqlException ) {
	     AlertBox.display("Error", sqlException.getMessage());
	     }
	     catch(ClassNotFoundException cnfex) {
	     AlertBox.display("Error", "Problem in loading or "
	                 + "registering MS Access JDBC driver");
	         cnfex.printStackTrace();
	     }

	return aList;
	}
	//Verifies RoomID is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyRoomID()
	{
		boolean isValid = !roomID.equals("");
		
		errorMessage = "";
		if(!isValid)
			errorMessage += "RoomID is Invalid\n";
		return isValid;
	}
	//Verifies Room Name is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyName()
	{
		boolean isValid = true;
		
		if(name.equals("") || name == null)
			isValid = false;
		
		if(!isValid)
			errorMessage += "Name is Invalid\n";
		
		return isValid;
	}
	//Verifies Location is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyLocation()
	{
		boolean isValid = true;
		
		if(location.equals("") || location == null)
			isValid = false;
		if(!isValid)
			errorMessage += "Locaiton is Invalid\n";
		
		return isValid;
	}
	//Verifies Condition is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyCondition()
	{
		boolean isValid = condition.equals("Unready") || condition.equals("Cleaned")
				|| condition.equals("Repaired") || condition.equals("Ready");
		
		if(!isValid)
			errorMessage += "Condition is Invalid\n";
		
		return isValid;
	}
	//Verifies Status is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyStatus()
	{
		boolean isValid = status.equals("Occupied") || status.equals("Unoccupied");
		
		if(!isValid)
			errorMessage += "Status is Invalid";
		
		return isValid;
	}
	//Verifies Type is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyType()
	{	
		boolean isValid = type.equals("Cottage2") || type.equals("Cottage3") || type.equals("Cottage4") ||
		type.equals("Queen1Bed") || type.equals("Queen2Bed") || type.equals("TwoRoom") || type.equals("ThreeRoom") || type.equals("Bridal");
		
		if(!isValid)
			errorMessage += "Type " + type + " is Invalid\n";
		
		return isValid;
	}
	//Verifies Number of Patios is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyPatios()
	{
		boolean isValid = patios == 1 || patios == 2;
		
		if(!isValid)
			errorMessage += "Patio is Invalid\n";
		
		return isValid;
	}
	//Verifies Number of Forests is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyForests()
	{
		boolean isValid = forests == 1 || forests == 2;
		
		if(!isValid)
			errorMessage += "Forests is Invalid\n";
		
		return isValid;
	}
	//Verifies Cost is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyCost()
	{
		boolean isValid = cost > 0.00;
		
		if(!isValid)
			errorMessage += "Cost is Invalid";
		
		return isValid;
	}
	
	public String toString()
	{
		return name; 

	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRoomID() {
		return roomID;
	}

	public String getType() {
		return type;
	}

	public double getCost() {
		return cost;
	}

	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public int getPatios() {
		return patios;
	}

	public int getForests() {
		return forests;
	}

	public void setPatios(int patios) {
		this.patios = patios;
	}

	public void setForests(int forests) {
		this.forests = forests;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	
}
