package project;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.PreparedStatement;

import javax.swing.JOptionPane;

public class Employee
{
	public static final String[] DISPLAY_ATTRIBUTE = {"Employee ID", "First Name", "Last Name", "Email Address", "Phone Number", "Position", "Wage",
			"Address 1", "Address 2", "City", "State", "Zipcode"};
	public static final String[] COLUMN_ATTRIBUTE = {"employeeID", "firstName", "lastName", "emailAddress", "phoneNumber", "position", "wage",
			"address 1", "address 2", "city", "state", "zipcode"};
	private String firstName, lastName, emailAddress, phoneNumber;
	private String address1, address2, city, state;
	private String position;
	private double wage;
	private int employeeID, zipcode;
	private String errorMessage;
	
	public static void main(String[] args)
	{
	
		System.out.println("About to create and print Gary");
		Employee Gary = new Employee();
		Gary.setFirstName("Gary");
		Gary.setLastName("Oak");
		Gary.setEmailAddress("firstGenner@kanto.poke");
		Gary.setPhoneNumber("1276342567");
		Gary.setAddress1("124 N Grove ln.");
		Gary.setAddress2("Room #2");
		Gary.setCity("Pallet Town");
		Gary.setState("Kanto");
		Gary.setZipcode(14112);
		Gary.setPosition("Bellboy");
		Gary.setWage(10000.00);
		System.out.println("Gary insert was successful? " + Gary.insertEmployee());
		System.out.println(Gary);
		//String firstName, String lastName, String emailAddress, String phoneNumber,
		//String address1, String address2, String city, String state, int zipcode,
		//String position, Double wage,
		//String username, String password)
		System.out.println("\nAbout to create and print Karen");
		Employee Karen = new Employee(
		"Karen", "Smith", "canIseeyourManager@gmail.com","1291574281",
		"Manager",64000.00,
		"987 West Lake St.", "", "Nowhere", "Everywhere", 17892);
		System.out.println("Karen insert was successful? " + Karen.insertEmployee());
		System.out.println(Karen);
		
		System.out.println("\nAbout to create and print Robert");
		Employee Robert = new Employee("Robert", "Alfrado", "rAlfrado@yahoo.com","1241230987",
				"Receptionist",32000.00, 
		"789 North Lake St.", "", "Midland", "Texas", 17892);
		System.out.println("Robert insert was successful? " + Robert.insertEmployee());
		System.out.println(Robert);
		Robert.setEmailAddress("r_alfrado@gmail.com");
		System.out.println("\nRobert update was successful? " + Robert.updateEmployee());
		System.out.println("About to print updated Robert");
		System.out.println(Robert);
		
		try
		{
			System.out.println("\n\nAbout to print all entries in employeeInfo");
			ArrayList<Employee> employeeList = Employee.getEmployeeList(";");
			for(int i = 0; i<employeeList.size(); i++)
			{
				System.out.println("\n" + employeeList.get(i));
			}	

			System.out.println("\n\nSearching for Gary");
			
			Employee Gary2 = new Employee(Gary.getEmployeeID());
			
			System.out.println("Displaying Gary Search: \n" + Gary2);
		}
		catch(EmptySetException e)
		{
			System.out.println(e.getMessage());
		}
		
		System.out.println("About to Delete Gary, Karen, and Robert from employeeInfo");
		Gary.deleteEmployee();
		Karen.deleteEmployee();
		Robert.deleteEmployee();
		
		try
		{
			System.out.println("\n\nAbout to reprint all entires in employeeInfo");
			ArrayList<Employee> employeeList = Employee.getEmployeeList(";");
			for(int i = 0; i<employeeList.size(); i++)
			{
				System.out.println("\n" + employeeList.get(i));
			}	
		}
		catch(EmptySetException e)
		{
			System.out.println(e.getMessage());
		}
	}
	//Basic Constructor
	public Employee()
	{
		firstName = "";
		lastName = "";
		position = "";
		wage = 0.0;
		emailAddress = "";
		address1 = "";
		address2 = "";
		city = "";
		state = "";
		phoneNumber = "";
		employeeID = 0;
		zipcode = 0;
	}
	//Quick set Constructor for creating Employee to insert into database
	public Employee(String firstName, String lastName, String emailAddress, String phoneNumber, String position, Double wage, String address1, String address2, String city, String state, int zipcode)
	{
		this.employeeID = 0;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.position = position;
		this.wage = wage;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
	}
	//Quick set Constructor for creating Employee from database
	private Employee(int employeeID, String firstName, String lastName, String emailAddress, String phoneNumber, String position, Double wage, String address1, String address2, String city, String state, int zipcode)
	{
		this.employeeID = employeeID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.position = position;
		this.wage = wage;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
	}
	//Search Constructor for creating an Employee from Username and Password
	public Employee(String username, String password) throws EmptySetException
	{
		employeeID = employeeQuery(username, password);
		if(0 != employeeID)
			employeeQuery(employeeID);
	}
		//Accessible from the Above Constructor.
	private int employeeQuery(String username, String password) throws EmptySetException
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
	           		"SELECT employeeID\n"
	           		+ "FROM employeeSecurity\n"
	           		+ "WHERE username LIKE \"" + username + "\" AND password LIKE \"" + password + "\";");
	        // process query results
	        if(!resultSet.next())
	        	throw new EmptySetException("Username Password Combination\nNot Found");
	        int result = Integer.parseInt(resultSet.getNString(1));

	        // close statement and connection
	        statement.close();
	        connection.close();
	        
	        return result;
	         
		}// end try
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
	//Search Constructor for creating an Employee from EmployeeID.
	public Employee(int employeeID) throws EmptySetException
	{
		this.employeeID = employeeID;
		employeeQuery(employeeID);
	}
		//Accessible from the Above Constructors.
	private void employeeQuery(int id) throws EmptySetException
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
	           		"SELECT firstName, lastName, position, wage, emailAddress, address1, address2, city, state, phoneNumber, zipcode\n"
	           		+ "FROM employeeInfo\n"
	           		+ "WHERE employeeID = \"" + employeeID +"\";");
	        // process query results
	        if(!resultSet.next())
	        	throw new EmptySetException();//positions on first row
	        
	        firstName = resultSet.getNString(1);
			lastName = resultSet.getNString(2);
			position = resultSet.getNString(3);
			wage = Double.parseDouble(resultSet.getNString(4));
			emailAddress = resultSet.getNString(5);
			address1 = resultSet.getNString(6);
			address2 = resultSet.getNString(7);
			city = resultSet.getNString(8);
			state = resultSet.getNString(9);
			phoneNumber = resultSet.getNString(10);
			zipcode = Integer.parseInt(resultSet.getNString(11));
			
	          
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
	//Inserts the Employee into the database. Returns true if successful.
	public boolean insertEmployee()
	{
		if (employeeID == 0 & verifyFirstName() & verifyLastName() & verifyEmailAddress() & verifyPhoneNumber()
			& verifyAddress1() & verifyAddress2() & verifyCity()
				&& verifyState() && verifyZipcode())
			try {
		         // load database driver class
		    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

		         // connect to database
		         Connection connection = DriverManager.getConnection(
		            Guest.DBL);
		         // create Statement to query database
		         PreparedStatement statement = connection.prepareStatement(
		        		 "INSERT INTO employeeInfo(firstName, lastName, emailAddress, phoneNumber, position, wage, address1, address2, city, state, zipcode)\n" +
		        		"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
		        		,Statement.RETURN_GENERATED_KEYS);

		         // query database			         	
				statement.setString(1, firstName);
				statement.setString(2, lastName);
				statement.setString(3, emailAddress);
				statement.setString(4, phoneNumber);
				statement.setString(5, position);
				statement.setDouble(6, wage);
				statement.setString(7, address1);
				statement.setString(8, address2);
				statement.setString(9, city);
				statement.setString(10, state);
				statement.setInt(11, zipcode);
				
				statement.execute();
				
				ResultSet resultSet = statement.getGeneratedKeys();
				
				resultSet.next();
				employeeID = resultSet.getInt(1);
//				
//				PreparedStatement s = connection.prepareStatement(
//				"INSERT INTO employeeSecurity(username, password, employeeID)\n" +
//		        		"VALUES (?, ?, ?);");
//		        					         	
//				s.setString(1, username);
//				s.setString(2, password);
//				s.setInt(3, employeeID);
//				
//				s.execute();
		         
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
	//Deletes the Employee from the database. Returns true if successful.
	public boolean deleteEmployee()
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
	        		"DELETE FROM employeeSecurity\n" +
	        		"WHERE employeeID = " + employeeID + ";");
	        		
	         statement.executeUpdate(
	        		"DELETE FROM employeeInfo\n" +
	        		"WHERE employeeID = " + employeeID + ";");
	         
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
	//Updates the Employee in the database. Returns true if successful.
	public boolean updateEmployee()
	{
		if (employeeID != 0 & verifyFirstName() & verifyLastName() & verifyEmailAddress() & verifyPhoneNumber()
			& verifyAddress1() & verifyAddress2() & verifyCity()
				&& verifyState() && verifyZipcode())
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
		        		"UPDATE employeeInfo\n"+
		        		"SET firstName = \"" + firstName + "\"\n"+
		        		"WHERE employeeID = " + employeeID + ";");
		         
		         statement.executeUpdate(
			        		"UPDATE employeeInfo\n"+
			        		"SET lastName = \"" + lastName + "\"\n"+
			        		"WHERE employeeID = " + employeeID + ";");
			        		
		         statement.executeUpdate(
		        		"UPDATE employeeInfo\n"+
		        		"SET emailAddress = \"" + emailAddress + "\"\n"+
		        		"WHERE employeeID = " + employeeID + ";");
		         
		         statement.executeUpdate(
			        		"UPDATE employeeInfo\n"+
			        		"SET phoneNumber = " + phoneNumber + "\n"+
			        		"WHERE employeeID = " + employeeID + ";");
		         
		         statement.executeUpdate(
			        		"UPDATE employeeInfo\n"+
			        		"SET position = \"" + position + "\"\n"+
			        		"WHERE employeeID = " + employeeID + ";");
		         
		         statement.executeUpdate(
			        		"UPDATE employeeInfo\n"+
			        		"SET wage = " + wage + "\n"+
			        		"WHERE employeeID = " + employeeID + ";");
		         
		         statement.executeUpdate(
			        		"UPDATE employeeInfo\n"+
			        		"SET address1 = \"" + address1 + "\"\n"+
			        		"WHERE employeeID = " + employeeID + ";");
		         
		         statement.executeUpdate(
			        		"UPDATE employeeInfo\n"+
			        		"SET address2 = \"" + address2 + "\"\n"+
			        		"WHERE employeeID = " + employeeID + ";");
		         
		         statement.executeUpdate(
			        		"UPDATE employeeInfo\n"+
			        		"SET city = \"" + city + "\"\n"+
			        		"WHERE employeeID = " + employeeID+ ";");
		         
		         statement.executeUpdate(
			        		"UPDATE employeeInfo\n"+
			        		"SET state = \"" + state + "\"\n"+
			        		"WHERE employeeID = " + employeeID + ";");
		         
		         statement.executeUpdate(
			        		"UPDATE employeeInfo\n"+
			        		"SET zipcode = " + zipcode + "\n"+
			        		"WHERE employeeID = " + employeeID + ";");
		         
//		         statement.executeUpdate(
//			        		"UPDATE employeeSecurity\n"+
//			        		"SET username = \"" + username + "\"\n"+
//			        		"WHERE employeeID = " + employeeID + ";");
//		         
//		         statement.executeUpdate(
//			        		"UPDATE employeeSecurity\n"+
//			        		"SET password = \"" + password + "\"\n"+
//			        		"WHERE employeeID = " + employeeID + ";");

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
	//Given a search Condition, fills an ArrayList of valid Employees.
	
	public static ArrayList<Employee> getEmployeeList(String sqlConditions) throws EmptySetException
	{
	ArrayList<Employee> employeeList = new ArrayList<>();
		try
		{
        // load database driver class
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        // connect to database
        Connection connection = DriverManager.getConnection(Guest.DBL);
        
        // create Statement to query database
        PreparedStatement statement = connection.prepareStatement(
           		"SELECT employeeID, firstName, lastName, emailAddress, phoneNumber, position, wage, address1, address2, city, state, zipcode\n"
           		+ "FROM employeeInfo\n"
           		+ sqlConditions);
        
        //set Conditions
        
        statement.execute();
        
        ResultSet resultSet = statement.getResultSet();
          
       	int i = 0;
        while(resultSet.next())
        {
        	employeeList.add(new Employee(
        	resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5),
        	resultSet.getString(6), resultSet.getDouble(7),
        	resultSet.getString(8), resultSet.getString(9), resultSet.getString(10), resultSet.getString(11), resultSet.getInt(12)));
        	i++;
       	}
        
        if(i ==0 )
        {
        	throw new EmptySetException("Employee Not Found");
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

	return employeeList;
	}
	
	public static ArrayList<Employee> getEmployeeList(String columnName, String value) throws EmptySetException
	{
	ArrayList<Employee> employeeList = new ArrayList<>();
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
           		"SELECT employeeID, firstName, lastName, emailAddress, phoneNumber, position, wage, address1, address2, city, state, zipcode\n"
           		+ "FROM employeeInfo\n"
           		+ condition);
        
        statement.setString(1,  value);
        
        //set Conditions
        
        statement.execute();
        
        ResultSet resultSet = statement.getResultSet();
          
       	int i = 0;
        while(resultSet.next())
        {
        	employeeList.add(new Employee(
        	resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5),
        	resultSet.getString(6), resultSet.getDouble(7),
        	resultSet.getString(8), resultSet.getString(9), resultSet.getString(10), resultSet.getString(11), resultSet.getInt(12)));
        	i++;
       	}
        
        if(i ==0 )
        {
        	throw new EmptySetException("Employee Not Found");
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

	return employeeList;
	}
	

	public static ArrayList<Employee> getEmployeeList(String columnName, int value) throws EmptySetException
	{
	ArrayList<Employee> employeeList = new ArrayList<>();
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
           		"SELECT employeeID, firstName, lastName, emailAddress, phoneNumber, position, wage, address1, address2, city, state, zipcode\n"
           		+ "FROM employeeInfo\n"
           		+ condition);
        
        statement.setInt(1,  value);

        //set Conditions
        
        statement.execute();
        
        ResultSet resultSet = statement.getResultSet();
          
       	int i = 0;
        while(resultSet.next())
        {
        	employeeList.add(new Employee(
        	resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5),
        	resultSet.getString(6), resultSet.getDouble(7),
        	resultSet.getString(8), resultSet.getString(9), resultSet.getString(10), resultSet.getString(11), resultSet.getInt(12)));
        	i++;
       	}
        
        if(i ==0 )
        {
        	throw new EmptySetException("Employee Not Found");
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

	return employeeList;
	}
	
	public static ArrayList<Employee> getEmployeeList(String columnName, double value) throws EmptySetException
	{
	ArrayList<Employee> employeeList = new ArrayList<>();
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
           		"SELECT employeeID, firstName, lastName, emailAddress, phoneNumber, position, wage, address1, address2, city, state, zipcode\n"
           		+ "FROM employeeInfo\n"
           		+ condition);
        
        statement.setDouble(1,  value);

        //set Conditions
        
        statement.execute();
        
        ResultSet resultSet = statement.getResultSet();
          
       	int i = 0;
        while(resultSet.next())
        {
        	employeeList.add(new Employee(
        	resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5),
        	resultSet.getString(6), resultSet.getDouble(7),
        	resultSet.getString(8), resultSet.getString(9), resultSet.getString(10), resultSet.getString(11), resultSet.getInt(12)));
        	i++;
       	}
        
        if(i ==0 )
        {
        	throw new EmptySetException("Employee Not Found");
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

	return employeeList;
	}
	//Verifies First Name is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyFirstName()
	{
		char[] stringToTest = firstName.toCharArray();
		boolean isValid = true;
		
		if(firstName.equals("") || firstName == null)
			isValid = false;
		
		for(int i = 0; i < stringToTest.length && isValid; i++)
			if( (stringToTest[i] >= 'A' && stringToTest[i] <= 'Z') ||
				(stringToTest[i] >= 'a' || stringToTest[i] <= 'z') ||
				(stringToTest[i] == '\'' || stringToTest[i] == '-'));
			else
				isValid = false;
		
		if(!isValid)
			errorMessage += "First Name is Invalid\n";
		
		return isValid;
	}
	//Verifies Last Name is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyLastName()
	{
		char[] stringToTest = lastName.toCharArray();
		boolean isValid = true;
		
		if(lastName.equals("") || lastName == null)
			isValid = false;
		
		for(int i = 0; i < stringToTest.length && isValid; i++)
			if( (stringToTest[i] >= 'A' && stringToTest[i] <= 'Z') ||
				(stringToTest[i] >= 'a' || stringToTest[i] <= 'z') ||
				(stringToTest[i] == '\'' || stringToTest[i] == '-'));
			else
				isValid = false;
		
		if(!isValid)
			errorMessage += "Last Name is Invalid\n";
		
		return isValid;
	}
	//Verifies Email Address is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyEmailAddress()
	{
		boolean isValid = true;

		if(!emailAddress.contains("@") || !emailAddress.contains(".") || emailAddress.contains(" "))
			isValid = false;
		
		//maybe add database check as well
		
		if(!isValid)
			errorMessage += "Email Address is Invalid\n";
		
		return isValid;
	}
	//Verifies Phone Number is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyPhoneNumber()
	{
		char[] stringToTest = phoneNumber.toCharArray();
		boolean isValid = true;
		
		if(stringToTest.length != 10)
		{
			isValid = false;
			errorMessage += "PhoneNumber is not Length 10\n";
		}
		
		for(int i = 0; i < stringToTest.length && isValid; i++)
			if( (stringToTest[i] >= '0' && stringToTest[i] <= '9'));
			else
			{
				isValid = false;
				errorMessage += "PhoneNumber contains a non-numerical character";
			}
		
		return isValid;
	}
	
	private boolean verifyPosition()
	{
		boolean isValid = true;
		
		if(!isValid)
			errorMessage += "Position is Invalid";
		return isValid;
	}
	
	private boolean verifyWage()
	{
		boolean isValid = wage >= 0;
		
		if(!isValid)
			errorMessage += "Wage is Invalid";
		
		return isValid;
	}
	
	//Verifies Address1 is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyAddress1()
	{
		String[] seperate = address1.split(" ");
		boolean isValid = true;
		if(address1.length() < 5 || seperate.length < 3)
			isValid = false;
		char[] stringToTest;
		for(int i = 0; i < seperate.length && isValid; i++)
		{
			stringToTest = seperate[i].toCharArray();
			for(int j = 0; j < stringToTest.length && isValid && i == 0; j++)
				if( (stringToTest[i] >= '0' && stringToTest[i] <= '9'));
				else
					isValid = false;
		
			if(seperate[i].length() < 1)
				isValid = false;
			
		}
		
		if(!isValid)
			errorMessage += "Address1 is Invalid\n";
		
		return isValid;
	}
	//Verifies Address2 is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyAddress2()
	{
		boolean isValid = address2.isEmpty() || !address2.isEmpty();
		if(!isValid)
			errorMessage += "Address2 is Invalid\n";
		return isValid;
	}
	//Verifies City is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyCity()
	{
		char[] stringToTest = city.toCharArray();
		boolean isValid = true;
		
		if(city.equals("") || city == null)
			isValid = false;
		
		for(int i = 0; i < stringToTest.length && isValid; i++)
			if( (stringToTest[i] >= 'A' && stringToTest[i] <= 'Z') ||
				(stringToTest[i] >= 'a' || stringToTest[i] <= 'z') ||
				(stringToTest[i] == '\'' || stringToTest[i] == '-'));
			else
				isValid = false;
		
		if(!isValid)
			errorMessage += "First Name is Invalid\n";
		
		return isValid;
	}
	//Verifies State is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyState()
	{
		char[] stringToTest = state.toCharArray();
		boolean isValid = true;
		
		if(state.equals("") || state == null)
			isValid = false;
		
		for(int i = 0; i < stringToTest.length && isValid; i++)
			if( (stringToTest[i] >= 'A' && stringToTest[i] <= 'Z') ||
				(stringToTest[i] >= 'a' || stringToTest[i] <= 'z') ||
				(stringToTest[i] == '\'' || stringToTest[i] == '-'));
			else
				isValid = false;
		
		if(!isValid)
			errorMessage += "First Name is Invalid";
		
		return isValid;
	}
	//Verifies zipcode is valid. Used in insert and update. Sets the Error Message.
	private boolean verifyZipcode()
	{
		char[] stringToTest = Integer.toString(zipcode).toCharArray();
		boolean isValid = true;
		
		if(stringToTest.length != 5)
			isValid = false;
		
		for(int i = 0; i < stringToTest.length && isValid; i++)
			if( (stringToTest[i] >= '0' && stringToTest[i] <= '9'));
			else
				isValid = false;
		
		if(!isValid)
			errorMessage += "Security Code is Invalid\n";
		
		return isValid;
	}

	public String toString()
	{
		return 
				"First Name = " + firstName + ", Last Name = " + lastName + "\n" + 
				"Email = " + emailAddress + ", Phone Number = " + phoneNumber +
				"Position = " + position + ", Wage = $" + wage + "\n" + 
				"Address1 = " + address1 + ", Address2 = " + address2 + ", City = " + city + "\n" + 
				"State = " + state + ", Zipcode = " + zipcode + "\n";
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public double getWage() {
		return wage;
	}
	public void setWage(double wage) {
		this.wage = wage;
	}
	public int getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}
	public int getZipcode() {
		return zipcode;
	}
	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean addUsernamePassword(String username, String password)
	{
		try {
	         // load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            Guest.DBL);
	         // create Statement to query database

	         // query database
	         PreparedStatement statement = connection.prepareStatement(
	        		 "SELECT username, password\n"
	        	           		+ "FROM employeeSecurity\n"
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
	        		"INSERT INTO employeeSecurity(employeeID, password, username)\n" +
	        		"VALUES (?,?,?);");
	         
	         statement.setInt(1,  employeeID);
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

	public boolean checkUsernamePassword(String username, String password)
	{
		try {
	         // load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            Guest.DBL);
	         // create Statement to query database

	         // query database
	         PreparedStatement statement = connection.prepareStatement(
	        		 "SELECT username, password\n"
	        	           		+ "FROM employeeSecurity\n"
	        	           		+ "WHERE username = ? AND password = ?;");
	         
	         statement.setString(1,username);
	         statement.setString(2, password);
	         
	         statement.execute();
	         
	         ResultSet resultSet = statement.getResultSet();
	         
	         if(!resultSet.next())
	         {
	        	 errorMessage = "Username and Password Combination is Not Found";
	        	 return false;
	         }
	         
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


	public boolean updateUsernamePassword(String username, String password)
	{
		try {
	         // load database driver class
	    	 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

	         // connect to database
	         Connection connection = DriverManager.getConnection(
	            Guest.DBL);
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
		        		"WHERE employeeID = " + employeeID + ";");
	         
	         s.executeUpdate(
		        		"UPDATE guestSecurity\n"+
		        		"SET password = \"" + password + "\"\n"+
		        		"WHERE employeeID = " + employeeID + ";");

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
	        		 Guest.DBL);
	         // create Statement to query database
	         Statement statement = connection.createStatement();

	         // query database
	         statement.executeUpdate(
	        		"DELETE FROM guestSecurity\n" +
	        		"WHERE employeeID = " + employeeID + ";");
	         
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
	
}

//private boolean verifyUsername()
//{
//	boolean isValid = true;
//	if(username.length() < 1)
//		isValid = false;
//	return isValid;	
//}
//
//private boolean verifyPassword()
//{
//	boolean isValid = true;
//	if(password.length() < 1)
//		isValid = false;
//	return isValid;	
//}
//public String getUsername() {
//return username;
//}
//
//public String getPassword() {
//return password;
//}
//
//public void setUsername(String username) {
//this.username = username;
//}
//
//public void setPassword(String password) {
//this.password = password;
//}