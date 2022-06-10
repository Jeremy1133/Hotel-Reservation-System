package project;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Guest
{
	public static final String DBL = "jdbc:ucanaccess://GiantDatabase.accdb";
	public static final String[] DISPLAY_ATTRIBUTE = {"Guest ID", "First Name", "Last Name", "Email Address", "Phone Number"};
	public static final String[] COLUMN_ATTRIBUTE = {"guestID", "firstName", "lastName","emailAddress","phoneNumber"};
	private int guestID;
	private String firstName, lastName, emailAddress, phoneNumber;
	private String errorMessage = "";
	private String address1, address2, city, country, region;
	private int code;
	private String cardNumber;
	private Date expirationDate;
	private int securityCode;
	
	public static void main(String[] args)
	{
		System.out.println("About to create and print Gary");
		Guest Gary = new Guest();
		Gary.setFirstName("Gary");
		Gary.setLastName("Oak");
		Gary.setEmailAddress("firstGenner@kanto.poke");
		Gary.setPhoneNumber("1276342567");
		Gary.insertGuest();
		System.out.println(Gary);
		
		System.out.println("About to create and print Karen");
		Guest Karen = new Guest("Karen", "Smith", "canIseeyourManager@gmail.com","1291574281");
		Karen.insertGuest();
		System.out.println(Karen);
		
		System.out.println("About to create, update, and print Robert");
		Guest Robert = new Guest("Robert", "Alfrado", "rAlfrado@yahoo.com","1241230987");
		Robert.insertGuest();
		Robert.setEmailAddress("r_alfrado@gmail.com");
		Robert.updateGuest();
		System.out.println(Robert);
		
		try
		{
			System.out.println("About to print all entries in guestInfo");
			ArrayList<Guest> guestList = Guest.getGuestList(";");
			for(int i = 0; i<guestList.size(); i++)
			{
				System.out.println(guestList.get(i));
			}

			System.out.println("\n\nSearching for Gary");
			
			Guest Gary2 = new Guest(Gary.getGuestID());
			
			System.out.println("Displaying Gary Search: \n" + Gary2);
		}
		catch(EmptySetException e)
		{
			System.out.println(e.getMessage());
		}
		
		
		System.out.println("About to Delete Gary, Karen, and Robert from guestInfo");
		Gary.deleteGuest();
		Karen.deleteGuest();
		Robert.deleteGuest();
		
		try
		{
			System.out.println("About to reprint all entires in guestInfo");
			ArrayList<Guest> guestList = Guest.getGuestList(";");
			for(int i = 0; i<guestList.size(); i++)
			{
				System.out.println(guestList.get(i));
			}	
		}
		catch(EmptySetException e)
		{
			System.out.println(e.getMessage());
		}
	}
	//Basic Constructor
	public Guest()
	{
		guestID = 0;
		firstName = "";
		lastName = "";
		emailAddress = "";
		phoneNumber = "";
	}
	//Quick set Constructor for creating a Guest to insert into database
	public Guest(String firstName, String lastName, String emailAddress, String phoneNumber)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
	}
	//Quick set Constructor for creating a Guest pulled from database
	public Guest(int guestID, String firstName, String lastName, String emailAddress, String phoneNumber)
	{
		this.guestID = guestID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
	}
	//Search Constructor for creating a Guest in database
	public Guest(int id) throws EmptySetException
	{
		guestID = id;
		guestQuery(id);
	}
			//Accessible only from the above Constructor to set a Guest given an ID.
	private void guestQuery(int id) throws EmptySetException
	{
		try
		{
        // load database driver class
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        // connect to database
        Connection connection = DriverManager.getConnection(DBL);
        
        // create Statement to query database
        Statement statement = connection.createStatement();

        // query database for guest information
        ResultSet resultSet = statement.executeQuery(
           		"SELECT firstName, lastName, emailAddress, phoneNumber\n"
           		+ "FROM guestInfo\n"
           		+ "WHERE guestID = " + guestID +";");
        
        if(!resultSet.next())
        {
        	throw new EmptySetException("Guest Not Found");
        }
		firstName = resultSet.getNString(1);
		lastName = resultSet.getNString(2);
		emailAddress = resultSet.getNString(3);
		phoneNumber = resultSet.getNString(4);
          
        // close statement and connection
        statement.close();
        connection.close();
	}
	catch(SQLException e){System.out.println(e.getMessage());}
	catch(ClassNotFoundException e){System.out.println(e.getMessage());}
}
	//Inserts the Guest into the database. Returns true if successful.
	
	public Guest(String username, String password) throws EmptySetException
	{
		guestID = guestQuery(username, password);
		if( 0 != guestID)
			guestQuery(guestID);
	}

	private int guestQuery(String username, String password)
	{
		try {
	         // load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            DBL);
	         // create Statement to query database

	         // query database
	         PreparedStatement statement = connection.prepareStatement(
	        		 "SELECT guestID\n"
	        	           		+ "FROM guestSecurity\n"
	        	           		+ "WHERE username = ? AND password = ?;");
	         
	         statement.setString(1,username);
	         statement.setString(2, password);
	         
	         statement.execute();
	          
	         ResultSet resultSet = statement.getResultSet();
	         
	         if(!resultSet.next())
	         {
	        	 errorMessage = "Username and Password Combination is Not Found";
	        	 return 0;
	         }
	         
	         int result = resultSet.getInt(1);
	         statement.execute();
	         connection.close();
	         return result;
	      }  // end try
	      // detect problems interacting with the database
		catch ( SQLException sqlException ) {
	    	  AlertBox.display("Error", sqlException.getMessage());
	    	  return 0;
	      }
	      catch(ClassNotFoundException cnfex) {
	    	  AlertBox.display("Error", "Problem in loading or "
	                  + "registering MS Access JDBC driver");
	          cnfex.printStackTrace();
	    	  return 0;
	      }
	}

	
	public boolean insertGuest()
	{
		if (guestID == 0 & verifyFirstName() & verifyLastName() & verifyEmailAddress() & verifyPhoneNumber())
			try {
		         // load database driver class
		    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

		         // connect to database
		         Connection connection = DriverManager.getConnection(
		            DBL);
		         // create Statement to query database
		         Statement statement = connection.createStatement();

		         // query database
		         statement.executeUpdate(
		        		"INSERT INTO guestInfo(firstName, lastName, emailAddress, phoneNumber)\n" +
		        		"VALUES (\"" + firstName + "\", \"" + lastName + "\", \"" + emailAddress + "\", \"" + phoneNumber + "\");"
		        		,Statement.RETURN_GENERATED_KEYS);		         	

		         ResultSet resultSet = statement.getGeneratedKeys();
		         resultSet.next();
		         guestID = resultSet.getInt(1);
		         
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
		else
		{
			
		}

  	  return false;
	}
	//Deletes the Guest from the database. Returns true if successful.
	public boolean deleteGuest()
	{
		try {
	         // load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            DBL);
	         // create Statement to query database
	         Statement statement = connection.createStatement();

	         // query database
	         statement.executeUpdate(
		        		"DELETE FROM guestSecurity\n" +
		    	        "WHERE guestID = " + guestID + ";");
	         
	         statement.executeUpdate(
		        		"DELETE FROM cardInfo\n" +
		    	        "WHERE guestID = " + guestID + ";");
	        		 
	         statement.executeUpdate(
	        		"DELETE FROM guestInfo\n" +
	        		"WHERE guestID = " + guestID + ";");
	         
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
	//Updates the Guest in the database. Returns true if successful.
	public boolean updateGuest()
	{
		if (guestID != 0 & verifyFirstName() & verifyLastName() & verifyEmailAddress() & verifyPhoneNumber())
			try {
		         // load database driver class
		    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

		         // connect to database
		         Connection connection = DriverManager.getConnection(
		            DBL);
		         // create Statement to query database
		         Statement statement = connection.createStatement();
		         // query database
		         statement.executeUpdate(
		        		"UPDATE guestInfo\n"+
		        		"SET firstName = \"" + firstName + "\"\n"+
		        		"WHERE guestID = " + guestID + ";");		         

		         statement.executeUpdate(
		        		"UPDATE guestInfo\n"+
		        		"SET lastName = \"" + lastName + "\"\n"+
		        		"WHERE guestID = " + guestID + ";");		         

		         statement.executeUpdate(
		        		"UPDATE guestInfo\n"+
		        		"SET emailAddress = \"" + emailAddress + "\"\n"+
		        		"WHERE guestID = " + guestID + ";");

		         statement.executeUpdate(
		        		"UPDATE guestInfo\n"+
		        		"SET phoneNumber = \"" + phoneNumber + "\"\n"+
		        		"WHERE guestID = " + guestID + ";");

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
	//Given a search Condition, fills an ArrayList of valid Guests.		
	public static ArrayList<Guest> getGuestList(String sqlConditions) throws EmptySetException
	{
		ArrayList<Guest> guestList = new ArrayList<>();
		try
		{
        // load database driver class
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        // connect to database
        Connection connection = DriverManager.getConnection(DBL);
        
        // create Statement to query database
        PreparedStatement statement = connection.prepareStatement(
           		"SELECT guestID, firstName, lastName, emailAddress, phoneNumber\n"
           		+ "FROM guestInfo\n"
           		+ sqlConditions);
        
        //set Conditions
        statement.execute();
        
        ResultSet resultSet = statement.getResultSet();
        
        int i = 0;
        while(resultSet.next())
        {
        	guestList.add(new Guest(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)));	
        i++;
        }
        
        if(i ==0 )
        {
        	throw new EmptySetException("Guests Not Found");
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
	      }

	return guestList;
	}
	
	public static ArrayList<Guest> getGuestList(String columnName, String value) throws EmptySetException
	{
		ArrayList<Guest> guestList = new ArrayList<>();
		String condition = "WHERE ";
		for(int i = 0 ; i < DISPLAY_ATTRIBUTE.length; i++)
			if(columnName.equals(DISPLAY_ATTRIBUTE[i])) condition += COLUMN_ATTRIBUTE[i];
		condition += " LIKE ?;";
		
		try
		{
        // load database driver class
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        // connect to database
        Connection connection = DriverManager.getConnection(DBL);
        
        // create Statement to query database
        PreparedStatement statement = connection.prepareStatement(
           		"SELECT guestID, firstName, lastName, emailAddress, phoneNumber\n"
           		+ "FROM guestInfo\n"
           		+ condition);
        
        statement.setNString(1, value);
        
        //set Conditions
        statement.execute();
        
        ResultSet resultSet = statement.getResultSet();
        
        int i = 0;
        while(resultSet.next())
        {
        	guestList.add(new Guest(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)));	
        i++;
        }
        
        if(i == 0 )
        {
        	throw new EmptySetException("Guests Not Found");
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

	return guestList;
	}
	//Verifies First Name is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyFirstName()
	{
		errorMessage = "";
		char[] stringToTest = firstName.toCharArray();
		boolean isValid = true;
		
		if(firstName.equals("") || firstName == null)
			isValid = false;
		
		for(int i = 0; i < stringToTest.length && isValid; i++)
			if( (stringToTest[i] >= 'A' && stringToTest[i] <= 'Z') ||
				(stringToTest[i] >= 'a' && stringToTest[i] <= 'z') ||
				stringToTest[i] == '\'' || stringToTest[i] == '-');
			else
				isValid = false;
		
		if(!isValid)
			errorMessage = "First Name is Invalid\n";
		
		return isValid;
	}
	//Verifies Last Name is valid. Used in insert and update. Sets the Error Message
	private boolean verifyLastName()
	{
		char[] stringToTest = lastName.toCharArray();
		boolean isValid = true;
		
		if(lastName.equals("") || lastName == null)
			isValid = false;
		
		for(int i = 0; i < stringToTest.length && isValid; i++)
			if( (stringToTest[i] >= 'A' && stringToTest[i] <= 'Z') ||
				(stringToTest[i] >= 'a' && stringToTest[i] <= 'z') ||
				stringToTest[i] == '\'' || stringToTest[i] == '-');
			else
				isValid = false;
		
		if(!isValid)
			errorMessage += "Last Name is Invalid\n";
		
		return isValid;
	}
	//Verifies Email Address is valid. Used in insert and update. Sets the Error Message
	private boolean verifyEmailAddress()
	{
		boolean isValid = true;
		
		if(!emailAddress.contains("@") || !emailAddress.contains("") || emailAddress.contains(" "))
			isValid = false;
		
		//maybe add database check as well
		
		if(!isValid)
			errorMessage += "Email Address is Invalid\n";
		
		return isValid;
	}
	//Verifies Phone Number is valid. Used in insert and update. Sets the Error Message
	private boolean verifyPhoneNumber()
	{
		char[] stringToTest = phoneNumber.toCharArray();
		boolean isValid = true;
		
		if(stringToTest.length != 10)
			isValid = false;
		
		for(int i = 0; i < stringToTest.length && isValid; i++)
			if( (stringToTest[i] >= '0' && stringToTest[i] <= '9'));
			else
				isValid = false;
		
		if(!isValid)
			errorMessage += "PhoneNumber is Invalid\n";
		
		return isValid;
	}

	public String toString()
	{
		return  "GuestID = " + guestID + "\n" +
				"First Name = " + firstName + "\nLast Name = " + lastName
				+ "\nEmail Address = " + emailAddress + "\nPhone Number = " + phoneNumber + "\n";
	}
	
	public int getGuestID() {
		return guestID;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}
	
	public void setGuestID(int guestID)
	{
		this.guestID = guestID;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setErrorMessage(String m)
	{
		errorMessage = m;
	}

	public boolean addUsernamePassword(String username, String password)
	{
		try {
	         // load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            DBL);
	         // create Statement to query database

	         // query database
	         PreparedStatement statement = connection.prepareStatement(
	        		 "SELECT username, password\n"
	        	           		+ "FROM guestSecurity\n"
	        	           		+ "WHERE username = ? AND password = ?;");
	         
	         statement.setString(1,username);
	         statement.setString(2, password);
	         
	         statement.execute();
	         
	         ResultSet resultSet = statement.getResultSet();
	         
	         if(resultSet.next())
	         {
	        	 errorMessage = "Username and Password Combination is Taken";
	        	 return false;
	         }
	         
	         statement = connection.prepareStatement(
	        		"INSERT INTO guestSecurity(guestID, password, username)\n" +
	        		"VALUES (?,?,?);");
	         
	         statement.setInt(1,  guestID);
	         statement.setString(2,  password);
	         statement.setString(3,  username);

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
		         AlertBox.display("Error", cnfex.getMessage());
	          cnfex.printStackTrace();
	    	  return false;
	      }
	}

	public static boolean checkUsernamePassword(String username, String password) throws EmptySetException
	{
		try {
	         // load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            DBL);
	         // create Statement to query database

	         // query database
	         PreparedStatement statement = connection.prepareStatement(
	        		 "SELECT username, password\n"
	        	           		+ "FROM guestSecurity\n"
	        	           		+ "WHERE username = ? AND password = ?;");
	         
	         statement.setString(1,username);
	         statement.setString(2, password);
	         
	         statement.execute();
	         
	         ResultSet resultSet = statement.getResultSet();
	         
	         if(!resultSet.next());
	         else
	         {
	        	 statement.close();
		         connection.close();
	        	 throw new EmptySetException("Username and Password Combination Taken!");
	         }
	         
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
		         AlertBox.display("Error", cnfex.getMessage());
	          cnfex.printStackTrace();
	    	  return false;
	      }
	}
	
	public boolean updateUsernamePassword(String username, String password)
	{
		try {
	         // load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            DBL);
	         // create Statement to query database

	         // query database
	         PreparedStatement statement = connection.prepareStatement(
	        		 "SELECT username, password\n"
	        	           		+ "FROM guestSecurity\n"
	        	           		+ "WHERE username = ? AND password = ?;");
	         
	         statement.setString(1,username);
	         statement.setString(2, password);
	         
	         statement.execute();
	         
	         ResultSet resultSet = statement.getResultSet();

	         statement.execute();
	         if(resultSet.next())
	         {
	        	 errorMessage = "Username and Password Combination is Taken";
	        	 return false;
	         }
	         
	         Statement s = connection.createStatement();
	         
	         s.executeUpdate(
		        		"UPDATE guestSecurity\n"+
		        		"SET username = \"" + username + "\"\n"+
		        		"WHERE guestID = " + guestID + ";");
	         
	         s.executeUpdate(
		        		"UPDATE guestSecurity\n"+
		        		"SET password = \"" + password + "\"\n"+
		        		"WHERE guestID = " + guestID + ";");

	         connection.close();
	         return true;
	      }  // end try
	      // detect problems interacting with the database
		catch ( SQLException sqlException ) {
	         AlertBox.display("Error", sqlException.getMessage());
	    	  return false;
	      }
	      catch(ClassNotFoundException cnfex) {
		         AlertBox.display("Error", cnfex.getMessage());
	          cnfex.printStackTrace();
	    	  return false;
	      }
	}
	
	public boolean deleteUsernamePassword()
	{
		try {
	         // load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            DBL);
	         // create Statement to query database
	         Statement statement = connection.createStatement();

	         // query database
	         statement.executeUpdate(
	        		"DELETE FROM guestSecurity\n" +
	        		"WHERE guestID = " + guestID + ";");
	         
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
	}
	
	public boolean setAddress()
	{
		try {
	         // load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            DBL);
	         // create Statement to query database

	         // query database
	         PreparedStatement statement = connection.prepareStatement(
	        		 "SELECT address1, address2, city, country, region, code\n"
	        	           		+ "FROM addresses\n"
	        	           		+ "WHERE guestID = ?;");
	         
	         statement.setInt(1,guestID);
	         
	         statement.execute();
	          
	         ResultSet resultSet = statement.getResultSet();
	         
	         if(!resultSet.next())
	         {
	        	 errorMessage = firstName + " does not have an address";
	        	 return false;
	         }
	         
	         address1 = resultSet.getNString(1);
	         address2 = resultSet.getNString(2);
	         city = resultSet.getNString(3);
	         country = resultSet.getNString(4);
	         region = resultSet.getNString(5);
	         code = resultSet.getInt(6);
	        
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
	
	public boolean addAddress(String address1, String address2, String city, String country, String region, int code)
	{
		try {
	         // load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            DBL);
	         // create Statement to query database

	         // query database
	         PreparedStatement statement = connection.prepareStatement(
	        		 "INSERT INTO addresses (guestID, address1, address2, city, country, region, code)\n"
	        	           		+ "VALUES (?, ?, ?, ?, ?, ?, ?);");
	         
	         statement.setInt(1,  guestID);
	         statement.setString(2, address1);
	         statement.setString(3, address2);
	         statement.setString(4, city);
	         statement.setString(5, country);
	         statement.setString(6, region);
	         statement.setInt(7,code);
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
	}
	
	public boolean updateAddress(String address1, String address2, String city, String country, String region, int code)
	{
		try {
	         // load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            DBL);
	         // create Statement to query database
	         Statement statement = connection.createStatement();
	         // query database
	         statement.executeUpdate(
	        		"UPDATE addresses\n"+
	        		"SET address1 = \"" + address1 + "\"\n"+
	        		"WHERE guestID = " + guestID + ";");		         

	         statement.executeUpdate(
	        		"UPDATE addresses\n"+
	        		"SET address2 = \"" + address2 + "\"\n"+
	        		"WHERE guestID = " + guestID + ";");		         

	         statement.executeUpdate(
	        		"UPDATE addresses\n"+
	        		"SET city = \"" + city + "\"\n"+
	        		"WHERE guestID = " + guestID + ";");

	         statement.executeUpdate(
	        		"UPDATE addresses\n"+
	        		"SET country = \"" + country + "\"\n"+
	        		"WHERE guestID = " + guestID + ";");

	         statement.executeUpdate(
	        		"UPDATE addresses\n"+
	        		"SET region = \"" + region + "\"\n"+
	        		"WHERE guestID = " + guestID + ";");

	         statement.executeUpdate(
	        		"UPDATE addresses\n"+
	        		"SET code = " + code + "\n"+
	        		"WHERE guestID = " + guestID + ";");

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
	}

	public boolean deleteAddress()
	{
		try {
	         // load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            DBL);
	         // create Statement to query database
	         Statement statement = connection.createStatement();

	         // query database
	         statement.executeUpdate(
	        		"DELETE FROM addresses\n" +
	        		"WHERE guestID = " + guestID + ";");
	         
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
	}

	public boolean setCreditCard()
	{
		try {
	         // load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            DBL);
	         // create Statement to query database

	         // query database
	         PreparedStatement statement = connection.prepareStatement(
	        		 "SELECT cardNumber, expirationDate, securityCode\n"
	        	           		+ "FROM cardInfo\n"
	        	           		+ "WHERE guestID = ?;");
	         
	         statement.setInt(1,guestID);
	         
	         statement.execute();
	          
	         ResultSet resultSet = statement.getResultSet();
	         
	         if(!resultSet.next())
	         {
	        	 errorMessage = firstName + " does not have a card";
	        	 return false;
	         }
	         
	         cardNumber = resultSet.getNString(1);
	         expirationDate = resultSet.getDate(2);
	         securityCode = resultSet.getInt(3);
	        
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
	
	public boolean addCreditCard(String cardNumber, Date expirationDate, int securityCode, String address1, String address2, String city, String country, String region, int code)
	{
		try {
	         // load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            DBL);
	         // create Statement to query database

	         // query database
	         PreparedStatement statement = connection.prepareStatement(
	        		 "INSERT INTO cardInfo (guestID, cardNumber, expirationDate, securityCode, address1, address2, city, country, region, code)\n"
	        	           		+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
	         
	         statement.setInt(1,  guestID);
	         statement.setString(2,  cardNumber);
	         statement.setDate(3,  expirationDate);
	         statement.setInt(4,  securityCode);
	         statement.setString(5, address1);
	         statement.setString(6, address2);
	         statement.setString(7, city);
	         statement.setString(8, country);
	         statement.setString(9, region);
	         statement.setInt(10,code);
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
	}
	
	public boolean updateCreditCard(String cardNumber, Date expirationDate, int securityCode, String address1, String address2, String city, String country, String region, int code)
	{
		try {
			// load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            DBL);
	         // create Statement to query database
	         Statement statement = connection.createStatement();
	         // query database

	         statement.executeUpdate(
	        		"UPDATE cardInfo\n"+
	        		"SET cardNumber = \"" + cardNumber + "\"\n"+
	        		"WHERE guestID = " + guestID + ";");		         

	         PreparedStatement s = connection.prepareStatement(
	        		 "UPDATE cardInfo\n"+
	        		"SET expirationDate = ?\n"+
	        		"WHERE guestID = " + guestID + ";");
	         
	         s.setDate(1, expirationDate);
	         s.execute();
	         s.close();

	         statement.executeUpdate(
	        		"UPDATE cardInfo\n"+
	        		"SET securityCode = " + securityCode + "\n"+
	        		"WHERE guestID = " + guestID + ";");
	         
	         statement.executeUpdate(
	        		"UPDATE cardInfo\n"+
	        		"SET address1 = \"" + address1 + "\"\n"+
	        		"WHERE guestID = " + guestID + ";");		         

	         statement.executeUpdate(
	        		"UPDATE cardInfo\n"+
	        		"SET address2 = \"" + address2 + "\"\n"+
	        		"WHERE guestID = " + guestID + ";");		         

	         statement.executeUpdate(
	        		"UPDATE cardInfo\n"+
	        		"SET city = \"" + city + "\"\n"+
	        		"WHERE guestID = " + guestID + ";");

	         statement.executeUpdate(
	        		"UPDATE cardInfo\n"+
	        		"SET country = \"" + country + "\"\n"+
	        		"WHERE guestID = " + guestID + ";");

	         statement.executeUpdate(
	        		"UPDATE cardInfo\n"+
	        		"SET region = \"" + region + "\"\n"+
	        		"WHERE guestID = " + guestID + ";");

	         statement.executeUpdate(
	        		"UPDATE cardInfo\n"+
	        		"SET code = " + code + "\n"+
	        		"WHERE guestID = " + guestID + ";");

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
	}

	public boolean deleteCreditCard()
	{
		try {
	         // load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            DBL);
	         // create Statement to query database
	         Statement statement = connection.createStatement();

	         // query database
	         statement.executeUpdate(
	        		"DELETE FROM cardInfo\n" +
	        		"WHERE guestID = " + guestID + ";");
	         
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
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public int getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(int securityCode) {
		this.securityCode = securityCode;
	}
}
