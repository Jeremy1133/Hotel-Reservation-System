package project;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import java.sql.Date;


public class Reservation
{
	public static final String[] DISPLAY_ATTRIBUTE = {"First Name", "Last Name", "Email Address", "Room Name",
							"Room ID", "Guest ID", "Check In", "Check Out", "Options", "Cycle Cost"};
	public static final String[] COLUMN_ATTRIBUTE = {"guestFirstName", "guestLastName", "guestEmailAddress", "roomName",
			"roomID", "guestID", "checkIn", "checkOut", "options", "cycleCost"};
	private String guestFirstName, guestLastName, guestEmailAddress, roomName;
	private String roomID;  
	private int guestID;
	private Date checkIn, checkOut;
	private boolean options;
	private double cycleCost;
	private String errorMessage;
	
	public static java.sql.Date getCurrentDate()
	{
		java.util.Date today = new java.util.Date();
		return new java.sql.Date(today.getTime());
	}
	
	public static void main(String[] args)
	{
//		System.out.println("About to create Gary");
//		AccreditedGuest Gary = new AccreditedGuest();
//		Gary.setFirstName("Gary");
//		Gary.setLastName("Oak");
//		Gary.setEmailAddress("firstGenner@kanto.poke");
//		Gary.setPhoneNumber("1276342567");
//		Gary.setCardNumber("1235573386207555");
//		Gary.setExpirationDate(new Date(1001, 5, 5));
//		Gary.setSecurityCode(432);
//		Gary.setAddress1("124 N Grove ln.");
//		Gary.setAddress2("Room #2");
//		Gary.setCity("Pallet Town");
//		Gary.setCountry("PokeVerse");
//		Gary.setRegion("Kanto");
//		Gary.setCode(1412);
//		System.out.println("Gary insert was successful? " + Gary.insertGuest());
//		
//		System.out.println("\nAbout to create Karen");
//		AccreditedGuest Karen = new AccreditedGuest(new Guest("Karen", "Smith", "canIseeyourManager@gmail.com","1291574281"),
//				"5432102924223465", new Date(5,4,3), 123,
//				"987 West Lake St.", "", "Nowhere", "Everywhere", "InBetween", 17892);
//		System.out.println("Karen insert was successful? " + Karen.insertGuest());
//		
//		System.out.println("\nAbout to create Robert");
//		AccreditedGuest Robert = new AccreditedGuest(new Guest("Robert", "Alfrado", "rAlfrado@gmail.com","1029318203"),
//				"1029457301928375", new Date(1029,3,12), 123,
//				"789 North Lake St.", "", "Midland", "America", "Texas", 17892);
//		System.out.println("Robert insert was successful? " + Robert.insertGuest());
//		
//		System.out.println("\n\nCreating Edna");
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
//		
//		System.out.println("\nCreating Lialah");
//		Room Lialah = new Room("C3.2P.2F.01","Lialah","North Forest","Unready","Unoccupied","Cottage3",350.00,2,2);
//		System.out.println("Lialah Insert Successful? " + Lialah.insertRoom());
//		
//		System.out.println("\nCreating Rose");
//		Room Rose = new Room("C3.1P.1F.02","Rose","South Forest","Repaired","Unoccupied","Queen2Bed",300.00,1,1);
//		System.out.println("Rose Insert Successful? " + Rose.insertRoom());
//		
//		System.out.println("\n\nCreating and Print Gary-Edna Reservation");
//		Reservation Gary_Edna = new Reservation();
//		Gary_Edna.setGuestID(Gary.getGuestID());
//		Gary_Edna.setRoomID(Edna.getRoomID());
//		Gary_Edna.setCheckIn(new Date(120,5,6));
//		Gary_Edna.setCheckOut(new Date(121,6,7));
//		Gary_Edna.setOptions(false);
//		Gary_Edna.setCycleCost(Edna.getCost());
//		System.out.println("Gary-Edna Reservation successfully inserted? " + Gary_Edna.insertReservation());
//		System.out.println(Gary_Edna);
//		
//		System.out.println("\n\nCreating and Print Karen-Lialah Reservation");
//		Reservation Karen_Lialah = new Reservation(Karen.getGuestID(), Lialah.getRoomID(), new Date(120,1,11), new Date(121,2,12), true, Lialah.getCost(), Karen.getFirstName(), Karen.getLastName(), Karen.getEmailAddress(), Lialah.getName());
//		System.out.println("Karen-Lialah Reservation successfully inserted? " + Karen_Lialah.insertReservation());
//		System.out.println(Karen_Lialah);
//		
//		System.out.println("\n\nCreating and Print Robert-Rose Reservation");
//		Reservation Robert_Rose = new Reservation(Robert.getGuestID(), Rose.getRoomID(), new Date(120,3,6), new Date(121,12,4), true, Lialah.getCost(), Robert.getFirstName(), Robert.getLastName(), Robert.getEmailAddress(), Rose.getName());
//		System.out.println("Robert-Rose Reservation successfully inserted? " + Robert_Rose.insertReservation());
//		System.out.println(Robert_Rose);
//
//		
//		try
//		{
//			System.out.println("\n\nAbout to print all entries in reservationInfo:");
//			ArrayList<Reservation> reservationList = Reservation.getReservationList(";");
//			for(int i = 0; i < reservationList.size(); i++)
//			{
//				System.out.println(reservationList.get(i));
//			}
//
//			System.out.println("\n\nSearching for Gary_Edna");
//			
//			Reservation Gary_Edna2 = new Reservation(Gary.getGuestID(), Edna.getRoomID());
//			
//			System.out.println("Displaying Gary-Edna Search: \n" + Gary_Edna2);
//			
//			
//		}
//		catch(UsernamePasswordException e)
//		{
//			System.out.println(e.getMessage());
//		}
//		catch(GarbageInDatabaseException e)
//		{
//			System.out.println(e.getMessage());
//		}
//		
//		System.out.println("\n\nAbout to Delete Gary_Edna, Karen_Lialah, and Robert_Rose from reservationInfo");
//		System.out.println("Deletion of Gary_Edna Successful? " + Gary_Edna.deleteReservation());
//		System.out.println("Deletion of Karen_Lialah Successful? " + Karen_Lialah.deleteReservation());
//		System.out.println("Deletion of Robert_Rose Successful? " + Robert_Rose.deleteReservation());
//		
//		
//		try
//		{
//			System.out.println("About to reprint all entries in reservationInfo:");
//			ArrayList<Reservation> reservationList = Reservation.getReservationList(";");
//			for(int i = 0; i < reservationList.size(); i++)
//			{
//				System.out.println(reservationList.get(i));
//			}	
//		}
//		catch(GarbageInDatabaseException e)
//		{
//			System.out.println(e.getMessage());
//		}
//		
//		System.out.println("\nDeleting Edna, Lialah, and Rose:");
//		System.out.println("Edna Deletion Successful? " + Edna.deleteRoom());
//		System.out.println("Lialah Deletion Successful? " + Lialah.deleteRoom());
//		System.out.println("Rose Deletion Successful? " + Rose.deleteRoom());
//		
//		System.out.println("About to Delete Gary, Karen, and Robert from guestInfo");
//		System.out.println("Gary Deletion Successful? " + Gary.deleteGuest());
//		System.out.println("Karen Deletion Successful? " + Karen.deleteGuest());
//		System.out.println("Robert Deletion Successful? " + Robert.deleteGuest());
		
	}
	
	public Reservation()
	{
		guestID = 0;
		roomID = "";
		checkIn = new Date(0,0,0);
		checkOut = new Date(0,0,0);
		options = false;
		cycleCost = 0.00;
		
	}
	
	public Reservation(int guestID, String roomID) throws EmptySetException
	{
		this.roomID = roomID;
		this.guestID = guestID;
		
	}
	
	public Reservation(Guest guest, Room room) throws EmptySetException
	{
		this.guestFirstName = guest.getFirstName();
		this.guestLastName = guest.getLastName();
		this.guestEmailAddress = guest.getEmailAddress();
		this.roomName = room.getName();
		this.roomID = room.getRoomID();
		this.guestID = guest.getGuestID();
		
	}
	
	public Reservation(int guestID, String roomID, Date checkIn, Date checkOut, boolean options, double cycleCost)
	{
		this.guestID = guestID;
		this.roomID = roomID;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.options = options;
		this.cycleCost = cycleCost;
	}
	
	public Reservation(int guestID, String roomID, Date checkIn, Date checkOut, boolean options, double cycleCost, String guestFirstName, String guestLastName, String guestEmailAddress, String roomName)
	{
		this.guestID = guestID;
		this.roomID = roomID;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.options = options;
		this.cycleCost = cycleCost;
		this.guestFirstName = guestFirstName;
		this.guestLastName = guestLastName;
		this.guestEmailAddress = guestEmailAddress;
		this.roomName = roomName;
	}
	
	private void reservationQuery() throws EmptySetException
	{
		try
		{
        // load database driver class
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        // connect to database
        Connection connection = DriverManager.getConnection(Guest.DBL);
        
        // create Statement to query database
        PreparedStatement statement = connection.prepareStatement(
        		"SELECT checkIn, checkOut, options, cycleCost\n"
           		+ "FROM reservationInfo\n"
           		+ "WHERE roomID LIKE ? AND guestID = ?;");
        statement.setString(1, roomID);
        statement.setInt(2, guestID);

        // query database for guest information
        statement.execute();
        

        
        ResultSet resultSet = statement.getResultSet();
        if(!resultSet.next())
        {
        	throw new EmptySetException("Combination Not Found");
        }
        // close statement and connection
        
        checkIn = resultSet.getDate(1);
        checkOut = resultSet.getDate(2);
        options = resultSet.getBoolean(3);
        cycleCost = resultSet.getDouble(4);
        
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
	}
	
	public boolean insertReservation()
	{
		if(verifyRoomID() && verifyGuestID() && verifyCheckIn() && verifyCheckOut() && verifyOptions() && verifyCycleCost())
			try {
		         // load database driver class
		    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

		         // connect to database
		         Connection connection = DriverManager.getConnection(
		            Guest.DBL);
		         // create Statement to query database
		         PreparedStatement statement = connection.prepareStatement(
		        		 "INSERT INTO reservationInfo(roomID, guestID, checkIn, checkOut, options, cycleCost)\n" +
		 		        		"VALUES (?, ?, ?, ?, ?, ?);");

		         statement.setString(1, roomID);
		         statement.setInt(2, guestID);
		         statement.setDate(3,checkIn);
		         statement.setDate(4, checkOut);
		         statement.setBoolean(5, options);
		         statement.setDouble(6, cycleCost);
		         
		         statement.execute();
		         
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
	
	public boolean updateReservation()
	{
		if(verifyRoomID() && verifyGuestID() && verifyCheckIn() && verifyCheckOut() && verifyOptions() && verifyCycleCost())
			try {
		         // load database driver class
		    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

		         // connect to database
		         Connection connection = DriverManager.getConnection(
		            Guest.DBL);
		         // create Statement to query database
		         PreparedStatement statement = connection.prepareStatement(
		        		 "UPDATE reservationInfo\n"+
					     "SET checkIn = ? " +
					     "WHERE guestID = ? AND roomID LIKE ?;");
		         
		         statement.setDate(1, checkIn);
		         statement.setInt(2, guestID);
		         statement.setString(3, roomID);
		         
		         statement.execute();
		         
		         statement = connection.prepareStatement(
		        		 "UPDATE reservationInfo\n"+
					     "SET checkOut = ? " +
					     "WHERE guestID = ? AND roomID LIKE ?;");
		         
		         statement.setDate(1, checkOut);
		         statement.setInt(2, guestID);
		         statement.setString(3, roomID);
		         
		         statement.execute();
		         
		         statement = connection.prepareStatement(
		        		 "UPDATE reservationInfo\n"+
					     "SET options = ? " +
					     "WHERE guestID = ? AND roomID LIKE ?;");
		         
		         statement.setBoolean(1, options);
		         statement.setInt(2, guestID);
		         statement.setString(3, roomID);
		         
		         statement.execute();
		         
		         statement = connection.prepareStatement(
		        		 "UPDATE reservationInfo\n"+
					     "SET cycleCost = ? " +
					     "WHERE guestID = ? AND roomID LIKE ?;");
		         
		         statement.setDouble(1, cycleCost);
		         statement.setInt(2, guestID);
		         statement.setString(3, roomID);
		         
		         statement.execute();
		         
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
	
	public boolean deleteReservation()
	{
		try {
	         // load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            Guest.DBL);
	         // create Statement to query database
	         PreparedStatement statement = connection.prepareStatement(
	        		"DELETE FROM reservationInfo\n" +
	        		"WHERE guestID = ? AND roomID LIKE ?;");
	         
	         statement.setInt(1, guestID);
	         statement.setString(2, roomID);
	         
	         statement.execute();
	         
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
	
	public static ArrayList<Reservation> getReservationList(String sqlConditions) throws EmptySetException
	{
		ArrayList<Reservation> reservationList = new ArrayList<>();
		try
		{
        // load database driver class
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        // connect to database
        Connection connection = DriverManager.getConnection(Guest.DBL);
        
        // create Statement to query database
        PreparedStatement statement = connection.prepareStatement(
           		"SELECT reservationInfo.guestID, reservationInfo.roomID, checkIn, checkOut, options, cycleCost, firstName, lastName, emailAddress, name\n"
           		+ "FROM (guestInfo INNER JOIN reservationInfo ON guestInfo.guestID = reservationInfo.guestID) INNER JOIN roomInfo ON reservationInfo.roomID = roomInfo.roomID\n"
           		+ sqlConditions);
        
        //set Conditions
        
        statement.execute();
        
        ResultSet resultSet = statement.getResultSet();
        //int guestID, String roomID, Date checkIn, Date checkOut, boolean options, double cycleCost)
        
        int i = 0;
        while(resultSet.next())
        {
        	reservationList.add(new Reservation(resultSet.getInt(1), resultSet.getString(2),
        			resultSet.getDate(3), resultSet.getDate(4), resultSet.getBoolean(5), resultSet.getDouble(6),
        			resultSet.getString(7), resultSet.getString(8), resultSet.getString(9), resultSet.getString(10)));
        i++;
        }
        
        if(i == 0 )
        {
        	throw new EmptySetException("No Reservations Found");
        }
        
        // close statement and connection
        statement.close();
        connection.close();
	}
	catch(SQLException e){AlertBox.display("Error",  e.getMessage());}
	catch(ClassNotFoundException e){AlertBox.display("Error",  e.getMessage());}

	return reservationList;
	}

	public static ArrayList<Reservation> getReservationList(String columnName, String value) throws EmptySetException
	{
		ArrayList<Reservation> reservationList = new ArrayList<>();

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
           		"SELECT reservationInfo.guestID, reservationInfo.roomID, checkIn, checkOut, options, cycleCost, firstName, lastName, emailAddress, name\n"
           		+ "FROM (guestInfo INNER JOIN reservationInfo ON guestInfo.guestID = reservationInfo.guestID) INNER JOIN roomInfo ON reservationInfo.roomID = roomInfo.roomID\n"
           		+ condition);
        
        statement.setString(1, value);
        //set Conditions
        
        statement.execute();
        
        ResultSet resultSet = statement.getResultSet();
        //int guestID, String roomID, Date checkIn, Date checkOut, boolean options, double cycleCost)
        
        int i = 0;
        while(resultSet.next())
        {
        	reservationList.add(new Reservation(resultSet.getInt(1), resultSet.getString(2),
        			resultSet.getDate(3), resultSet.getDate(4), resultSet.getBoolean(5), resultSet.getDouble(6),
        			resultSet.getString(7), resultSet.getString(8), resultSet.getString(9), resultSet.getString(10)));
        i++;
        }
        
        if(i == 0 )
        {
        	throw new EmptySetException("No Reservations Found");
        }
        
        // close statement and connection
        statement.close();
        connection.close();
	}
	catch(SQLException e){AlertBox.display("Error",  e.getMessage());}
	catch(ClassNotFoundException e){AlertBox.display("Error",  e.getMessage());}

	return reservationList;
	}

	public static ArrayList<Reservation> getReservationList(String columnName, double value) throws EmptySetException
	{
		ArrayList<Reservation> reservationList = new ArrayList<>();

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
           		"SELECT reservationInfo.guestID, reservationInfo.roomID, checkIn, checkOut, options, cycleCost, firstName, lastName, emailAddress, name\n"
           		+ "FROM (guestInfo INNER JOIN reservationInfo ON guestInfo.guestID = reservationInfo.guestID) INNER JOIN roomInfo ON reservationInfo.roomID = roomInfo.roomID\n"
           		+ condition);
        
        statement.setDouble(1, value);
        
        //set Conditions
        
        statement.execute();
        
        ResultSet resultSet = statement.getResultSet();
        //int guestID, String roomID, Date checkIn, Date checkOut, boolean options, double cycleCost)
        
        int i = 0;
        while(resultSet.next())
        {
        	reservationList.add(new Reservation(resultSet.getInt(1), resultSet.getString(2),
        			resultSet.getDate(3), resultSet.getDate(4), resultSet.getBoolean(5), resultSet.getDouble(6),
        			resultSet.getString(7), resultSet.getString(8), resultSet.getString(9), resultSet.getString(10)));
        i++;
        }
        
        if(i == 0 )
        {
        	throw new EmptySetException("No Reservations Found");
        }
        
        // close statement and connection
        statement.close();
        connection.close();
	}
	catch(SQLException e){AlertBox.display("Error",  e.getMessage());}
	catch(ClassNotFoundException e){AlertBox.display("Error",  e.getMessage());}

	return reservationList;
	}

	public static ArrayList<Reservation> getReservationList(String columnName, boolean value) throws EmptySetException
	{
		ArrayList<Reservation> reservationList = new ArrayList<>();

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
           		"SELECT reservationInfo.guestID, reservationInfo.roomID, checkIn, checkOut, options, cycleCost, firstName, lastName, emailAddress, name\n"
           		+ "FROM (guestInfo INNER JOIN reservationInfo ON guestInfo.guestID = reservationInfo.guestID) INNER JOIN roomInfo ON reservationInfo.roomID = roomInfo.roomID\n"
           		+ condition);
        
        statement.setBoolean(1, value);
        
        //set Conditions
        
        statement.execute();
        
        ResultSet resultSet = statement.getResultSet();
        //int guestID, String roomID, Date checkIn, Date checkOut, boolean options, double cycleCost)
        
        int i = 0;
        while(resultSet.next())
        {
        	reservationList.add(new Reservation(resultSet.getInt(1), resultSet.getString(2),
        			resultSet.getDate(3), resultSet.getDate(4), resultSet.getBoolean(5), resultSet.getDouble(6),
        			resultSet.getString(7), resultSet.getString(8), resultSet.getString(9), resultSet.getString(10)));
        i++;
        }
        
        if(i == 0 )
        {
        	throw new EmptySetException("No Reservations Found");
        }
        
        // close statement and connection
        statement.close();
        connection.close();
	}
	catch(SQLException e){AlertBox.display("Error",  e.getMessage());}
	catch(ClassNotFoundException e){AlertBox.display("Error",  e.getMessage());}

	return reservationList;
	}

	public static ArrayList<Reservation> getReservationList(String columnName, Date value) throws EmptySetException
	{
		ArrayList<Reservation> reservationList = new ArrayList<>();

		String condition = "WHERE ";
		for(int i = 0 ; i < DISPLAY_ATTRIBUTE.length; i++)
			if(columnName.equals(DISPLAY_ATTRIBUTE[i])) condition += COLUMN_ATTRIBUTE[i];
		condition += " < ?;";
		
		try
		{
        // load database driver class
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        // connect to database
        Connection connection = DriverManager.getConnection(Guest.DBL);
        
        // create Statement to query database
        PreparedStatement statement = connection.prepareStatement(
           		"SELECT reservationInfo.guestID, reservationInfo.roomID, checkIn, checkOut, options, cycleCost, firstName, lastName, emailAddress, name\n"
           		+ "FROM (guestInfo INNER JOIN reservationInfo ON guestInfo.guestID = reservationInfo.guestID) INNER JOIN roomInfo ON reservationInfo.roomID = roomInfo.roomID\n"
           		+ condition);
        
        statement.setDate(1, value);
        //set Conditions
        
        statement.execute();
        
        ResultSet resultSet = statement.getResultSet();
        //int guestID, String roomID, Date checkIn, Date checkOut, boolean options, double cycleCost)
        
        int i = 0;
        while(resultSet.next())
        {
        	reservationList.add(new Reservation(resultSet.getInt(1), resultSet.getString(2),
        			resultSet.getDate(3), resultSet.getDate(4), resultSet.getBoolean(5), resultSet.getDouble(6),
        			resultSet.getString(7), resultSet.getString(8), resultSet.getString(9), resultSet.getString(10)));
        i++;
        }
        
        if(i == 0 )
        {
        	throw new EmptySetException("No Reservations Found");
        }
        
        // close statement and connection
        statement.close();
        connection.close();
	}
	catch(SQLException e){AlertBox.display("Error",  e.getMessage());}
	catch(ClassNotFoundException e){AlertBox.display("Error",  e.getMessage());}

	return reservationList;
	}

	//search database to verify
	private boolean verifyRoomID()
	{
		boolean isValid = false;
		try
		{
        // load database driver class
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        // connect to database
        Connection connection = DriverManager.getConnection(Guest.DBL);
        
        // create Statement to query database
        PreparedStatement statement = connection.prepareStatement(
        		"SELECT roomID\n"
           		+ "FROM roomInfo\n"
           		+ "WHERE roomID LIKE ?;");
        statement.setString(1, roomID);

        // query database for guest information
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        // close statement and connection
        
        if(resultSet.next())
        	isValid = true;
        
        statement.close();
        connection.close();
		}
		catch(SQLException e){System.out.println(e.getMessage());}
		catch(ClassNotFoundException e){System.out.println(e.getMessage());}
		
		if(!isValid)
			errorMessage += "RoomID is invalid/n";
		
		return isValid;
	}
	//search database to verify
	private boolean verifyGuestID()
	{
		boolean isValid = false;
		try
		{
        // load database driver class
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        // connect to database
        Connection connection = DriverManager.getConnection(Guest.DBL);
        
        // create Statement to query database
        PreparedStatement statement = connection.prepareStatement(
        		"SELECT guestID\n"
           		+ "FROM guestInfo\n"
           		+ "WHERE guestID = ?;");
        statement.setInt(1, guestID);

        // query database for guest information
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        // close statement and connection
        
        if(resultSet.next())
        	isValid = true;
        
        statement.close();
        connection.close();
		}
		catch(SQLException e){System.out.println(e.getMessage());}
		catch(ClassNotFoundException e){System.out.println(e.getMessage());}
		
		if(!isValid)
			errorMessage += "GuestID is invalid";
		
		return isValid;
	}
	//compare to today's date to verify
	private boolean verifyCheckIn()
	{
		boolean isValid = checkIn.after(getCurrentDate()) || checkIn.toLocalDate().isEqual(LocalDate.now());
		
		if(!isValid)
			errorMessage += "CheckIn Date of " + checkIn + " is invalid.\n";
		
		if(isValid)
			try
		{
        // load database driver class
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        // connect to database
        Connection connection = DriverManager.getConnection(Guest.DBL);
        
        // create Statement to query database
        PreparedStatement statement = connection.prepareStatement(
        		"SELECT checkIn, checkOut\n"
           		+ "FROM reservationInfo\n"
           		+ "WHERE roomID LIKE ? AND checkIn < ? AND checkOut > ?;");
        statement.setString(1, roomID);
        statement.setDate(2, checkIn);
        statement.setDate(3, checkIn);

        // query database for guest information
        statement.execute();
        
        
        ResultSet resultSet = statement.getResultSet();
        // close statement and connection
        if(resultSet.next())
        {
        	errorMessage += "CheckIn Date for " + roomName + " is already reserved.";
        	return false;
        }
        
        statement.close();
        connection.close();
		}
		catch(SQLException e){AlertBox.display("Error", e.getMessage());}
		catch(ClassNotFoundException e){AlertBox.display("Error", e.getMessage());}
		
		return isValid;
	}
	//compare to CheckIn date to verify
	private boolean verifyCheckOut()
	{
		boolean isValid = checkOut.after(checkIn);
		
		if(!isValid)
			errorMessage += ("CheckOut Date of " + checkOut + " is invalid.\n");
		
		if(isValid)
			try
		{
        // load database driver class
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        // connect to database
        Connection connection = DriverManager.getConnection(Guest.DBL);
        
        // create Statement to query database
        PreparedStatement statement = connection.prepareStatement(
        		"SELECT checkIn, checkOut\n"
           		+ "FROM reservationInfo\n"
           		+ "WHERE roomID LIKE ? AND checkIn < ? AND checkOut > ?;");
        statement.setString(1, roomID);
        statement.setDate(2, checkOut);
        statement.setDate(3, checkOut);

        // query database for guest information
        statement.execute();
        
        
        ResultSet resultSet = statement.getResultSet();
        // close statement and connection
        if(resultSet.next())
        {
        	errorMessage += "CheckOut Date for " + roomName + " is already reserved.";
        	return false;
        }
        
        statement.close();
        connection.close();
		}
		catch(SQLException e){AlertBox.display("Error", e.getMessage());}
		catch(ClassNotFoundException e){AlertBox.display("Error", e.getMessage());}
		return isValid;
	}
	//ensure it isn't null
	private boolean verifyOptions()
	{
		boolean isValid = options || !options;
		
		if(!isValid)
			errorMessage += ("Option is invalid\n");
		return isValid;
	}
	//ensure it is greater than 0
	private boolean verifyCycleCost()
	{
		boolean isValid = cycleCost > 0.00;
		
		if(!isValid)
			errorMessage += ("Cost is invalid\n");
		return isValid;
	}

	public String toString()
	{
		return "GuestID = " + guestID + "Guest Name = " + guestFirstName + " " + guestLastName + "\n" +
				"RoomID = " + roomID + ", Room Name = " + roomName + "\n" +
				"CheckIn date = " + checkIn + ", CheckOut Date = " + checkOut + "\n" +
				"Options active? " + options + ", Cycle cost is " + cycleCost;
	}
	
	public String getRoomID() {
		return roomID;
	}

	public int getGuestID() {
		return guestID;
	}

	public Date getCheckIn() {
		return checkIn;
	}

	public Date getCheckOut() {
		return checkOut;
	}

	public boolean isOptions() {
		return options;
	}

	public double getCycleCost() {
		return cycleCost;
	}

	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}

	public void setGuestID(int guestID) {
		this.guestID = guestID;
	}

	public void setCheckIn(Date checkIn) {
		this.checkIn = checkIn;
	}

	public void setCheckOut(Date checkOut) {
		this.checkOut = checkOut;
	}

	public void setOptions(boolean options) {
		this.options = options;
	}

	public void setCycleCost(double cycleCost) {
		this.cycleCost = cycleCost;
	}

	public String getGuestFirstName() {
		return guestFirstName;
	}

	public String getGuestLastName() {
		return guestLastName;
	}

	public String getGuestEmailAddress() {
		return guestEmailAddress;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setGuestFirstName(String guestFirstName) {
		this.guestFirstName = guestFirstName;
	}

	public void setGuestLastName(String guestLastName) {
		this.guestLastName = guestLastName;
	}

	public void setGuestEmailAddress(String guestEmailAddress) {
		this.guestEmailAddress = guestEmailAddress;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
