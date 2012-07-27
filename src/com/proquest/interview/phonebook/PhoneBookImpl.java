package com.proquest.interview.phonebook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.proquest.interview.util.DatabaseUtil;

public class PhoneBookImpl implements PhoneBook {
	
	//INFO:
	//Using a HashMap for the local store instead of a list because set/get are constant time
	//We're assuming that we don't have to handle multiple people with the
	//same name (many values per key).  If we did, we could use a smarter collection (a multimap implementation) or
	//use a HashMap with the signature HashMap<Name, ArrayList<Person>>
	public HashMap<Name, Person> peopleMap;
	
	/**
	 * Iterates over the contents of the peopleMap (local collection of people) and uses
	 * Prepared statements to insert records into the database.
	 * 
	 * @exception {@link ClassNotFoundException}
	 * @exception {@link SQLException}
	 * @return none
	 */
	@Override
	public void  addPhonebookContentsToDatabase() throws ClassNotFoundException, SQLException {
		
		//We're using a Prepared Statement so we can prevent SQLInjection
		//Probably overkill for this example.
		
		Connection dbConnection = null;
		PreparedStatement pStmt = null;
		
		try {
			dbConnection = DatabaseUtil.getConnection();
			String pStmtInsert = "INSERT INTO PHONEBOOK (NAME, PHONENUMBER, ADDRESS) VALUES(?,?, ?)";
			
			for(Person p : peopleMap.values()) {
				pStmt = dbConnection.prepareStatement(pStmtInsert);
				pStmt.setString(1, p.name.toString());
				pStmt.setString(2, p.phoneNumber);
				pStmt.setString(3, p.address);
			
				pStmt.executeUpdate();
			
				//Release the prepared statement 
			 	pStmt.close();
			}
									
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		}
		
	}
	
	
	/**
	 * Adds a person object to the peopleMap (local collection of people).  Does not add the person to the database.
	 * Database synchronization of phonebook contents happens whenever addPhoneBookContentsToDatabase 
	 * is called.
	 * 
	 * @param newPerson The Person object to add to the local storage
	 * @return none
	 */
	@Override
	public void addPerson(Person newPerson) {
		//Add to our internal datastructure
		peopleMap.put(newPerson.name, newPerson);
	}
	
	/**
	 * Looks for a match of the passed in Name first in the local peopleMap and then in the database.
	 * The database search is limited to one result, as entries with the same name (key) are not handled throughout the
	 * project
	 * 
	 * @param Name Name object to search for
	 * @exception {@link ClassNotFoundException}
	 * @exception {@link SQLException}
	 * @return Person Object matching Name or null, if no match is found.
	 */
	@Override
	public Person findPerson(Name findMe) throws ClassNotFoundException, SQLException  {
		
		Person retPerson = null;
		
		retPerson =  peopleMap.get(findMe);
		
		//We missed in the Local Store, check the database;
		if( null == retPerson ){
			
			try {
				Connection dbConnection = DatabaseUtil.getConnection();
				Statement stmt = dbConnection.createStatement();
				
				StringBuilder queryBuilder = new StringBuilder();
				queryBuilder.append("SELECT * FROM PHONEBOOK WHERE NAME = '");
				queryBuilder.append(findMe.toString());
				queryBuilder.append("' LIMIT 1");
			
			
				ResultSet rs = stmt.executeQuery(queryBuilder.toString());
				while( rs.next() ) {
					retPerson = new Person();
					retPerson.name = new Name(rs.getString("NAME"));
					retPerson.address = rs.getString("ADDRESS");
					retPerson.phoneNumber = rs.getString("PHONENUMBER");
				}
				
			} catch (ClassNotFoundException e) {
				throw e;
			} catch (SQLException e) {
				throw e;
			}
				
		}
		
		return retPerson;
		
	}
	
	/**
	 * Prints all entries in the local peopleMap (PhoneBook)
	 */
	public void printPhoneBook() {
		for(Person p : peopleMap.values())
			System.out.println(p.toString());
	}
	
	
	
	public static void main(String[] args) {
		
		DatabaseUtil.initDB();  //You should not remove this line, it creates the in-memory database

		PhoneBookImpl pbi = new PhoneBookImpl();
		pbi.peopleMap = new HashMap<Name, Person>();
		
		/* TODO: create person objects and put them in the PhoneBook and database
		 * John Smith, (248) 123-4567, 1234 Sand Hill Dr, Royal Oak, MI
		 * Cynthia Smith, (824) 128-8758, 875 Main St, Ann Arbor, MI
		*/ 
		// TODO: print the phone book out to System.out
		// TODO: find Cynthia Smith and print out just her entry
		// TODO: insert the new person objects into the database
		
		//NOTE:  We are not synchronizing the contents of the in memory database and the phonebook initially.
		//This means the two entries from the database will not be found in the phonebook.
		//We are assuming that the goal of this project is to synchronize entries from the in-memory phonebook to permanent storage
		//in a database.
	
		
		//Create the person objects.
		Person johnSmith = new Person();
		johnSmith.name = new Name("John", "Smith");
		johnSmith.address = "1234 Sand Hill Dr, Royal Oak, MI";
		johnSmith.phoneNumber = "(248) 123-4567";
		
		Person cynthiaSmith = new Person();
		cynthiaSmith.name = new Name("Cynthia", "Smith");
		cynthiaSmith.address = "875 Main St, Ann Arbor, MI";
		cynthiaSmith.phoneNumber = "(824) 128-8758";
		
		//insert the new Person Objects into the local peopleMap (phonebook)
		pbi.addPerson(johnSmith);
		pbi.addPerson(cynthiaSmith);
		
		
		// TODO: print the phone book out to System.out
		System.out.println("LOCAL Phonebook Contents:");
		pbi.printPhoneBook();
				
		// TODO: find Cynthia Smith and print out just her entry
		// Requirements for findPerson are unclear.  Are we supposed to search the database and the 
		// in memory store for the name?  This would seem logical
		
		System.out.println("\nSearching for Cynthia Smith...");
		Name nameToFind = new Name("Cynthia", "Smith");
		
		Person foundPerson = null;
		
		try {
			foundPerson = pbi.findPerson(nameToFind);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		
		if( foundPerson != null )
			System.out.println(foundPerson.toString());
		else
			System.out.println(nameToFind.toString() + " not found.");
		
		
		//Assuming we want to synchronize phonebook contents to database all at once instead of individually.
		//This has a tradeoff of potentially losing state prior to synchronization, but you could utilize a 
		//bulk insert to get a performance increase
		try {
			pbi.addPhonebookContentsToDatabase();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
