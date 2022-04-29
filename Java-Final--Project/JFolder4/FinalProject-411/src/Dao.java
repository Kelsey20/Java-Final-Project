
   // This is inserting table

   
   // Method of Inserting a ticket into the database table given some ticketnum    
   //Update record (fields?) by some ticketnum
   //Method of Delete record  by some ticketnum.  Include a popup message dialog box asking the user if it is okay to delete the record. Show the ticket number in question in the popup box area.
 //Method of Viewing record(s) by ticketnum(s)
 //Method of Closing a desired ticket by ticketnum

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dao {
	// instance fields
	static Connection connect = null;
	Statement statement = null;

	// constructor
	public Dao() {
	  
	}

	public Connection getConnection() {
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}

	// CRUD implementation

	public void createTables() {
		// variables for SQL Query table creations
		
		final String createTicketsTable = "CREATE TABLE Lxiao_final1_tickets" +
				"(ticket_id INTEGER not NULL AUTO_INCREMENT, " +
				" ticket_issuer VARCHAR(30), " +
				" ticket_description VARCHAR(200), " +
				" start_date datetime, " +
				" end_date datetime, " +
				" ticket_status VARCHAR(20), " +
				" PRIMARY KEY ( ticket_id ))";
		
		final String createUsersTable = "CREATE TABLE Lxiao_final1_users" +
     		   	"(uid INTEGER not NULL AUTO_INCREMENT, " +
     		   	" uname VARCHAR(20), " +
     		   	" upass VARCHAR(20), " +
     		   	" admin int(11), " +
     		   	" PRIMARY KEY ( uid ))";
		try {

			// execute queries to create tables

			statement = getConnection().createStatement();

			statement.executeUpdate(createTicketsTable);
			statement.executeUpdate(createUsersTable);
			System.out.println("Created tables in given database...");

			// end create table
			// close connection/statement object
			statement.close();
			connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// add users to user table
		addUsers();
	}

	public void addUsers() {
		// add list of users from userlist.csv file to users table

		// variables for SQL Query inserts
		String sql;

		Statement statement;
		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // list to hold (rows & cols)

		// read data from file
		try {
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} catch (Exception e) {
			System.out.println("There was a problem loading the file");
		}

		try {

			// Setup the connection with the DB

			statement = getConnection().createStatement();

			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "Insert into Lxiao_final1_users(uname,upass,admin) " + "values('"+ rowData.get(0) +"'," + " '"+ rowData.get(1) +"','"+ rowData.get(2) +"');";
				statement.executeUpdate(sql);
			}
			System.out.println("Inserts completed in the given database...");

			// close statement object
			statement.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public int insertRecords(String ticketName, String ticketDesc, String start_date, String ticket_status) {
		int id = 0;
		try {
			statement = getConnection().createStatement();
			statement.executeUpdate("INSERT INTO Lxiao_final1_tickets" + "(ticket_issuer, ticket_description, start_date, ticket_status) VALUES("+"'"+ ticketName +"','"+ ticketDesc +"','"+ start_date +"','"+ticket_status+"')", Statement.RETURN_GENERATED_KEYS);
			// retrieve ticket id number newly auto generated upon record insertion
			ResultSet resultSet = null;
			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				// retrieve first field in table
				id = resultSet.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;

	}
	
	// continue coding for updateRecords implementation
	public int updateRecords(int ticketId, String ticketDesc) {
		int re = 0;
		try {
			statement = getConnection().createStatement();
			re = statement.executeUpdate("UPDATE Lxiao_final1_tickets SET ticket_description='"+ ticketDesc +"' where ticket_id=" +ticketId);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return re;

	}

	// continue coding for deleteRecords implementation
	public int deleteRecords(int ticketId) {
		int re = 0;
		try {
			statement = getConnection().createStatement();
			re=statement.executeUpdate("DELETE FROM Lxiao_final1_tickets where ticket_id=" +ticketId);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return re;

	}
	
	// continue coding for closeRecords implementation
	public int closeRecords(int ticketId,String end_date, String ticket_status) {
		int re = 0;
		try {
			statement = getConnection().createStatement();
			re= statement.executeUpdate("UPDATE Lxiao_final1_tickets SET end_date='"+ end_date +"' where ticket_id=" +ticketId);
			re = statement.executeUpdate("UPDATE Lxiao_final1_tickets SET ticket_status='"+ ticket_status +"' where ticket_id=" +ticketId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return re;
	}
	
	
	
	public ResultSet readRecords() {

		ResultSet results = null;
		try {
			statement = connect.createStatement();
			results = statement.executeQuery("SELECT * FROM Lxiao_final1_tickets");
			//connect.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return results;
	}

}
   