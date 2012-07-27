package com.proquest.interview.phonebook;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.HashMap;

import org.junit.Test;

import com.proquest.interview.util.DatabaseUtil;

public class PhoneBookImplTest {
	
	@Test
	public void testFindPersonInLocalPhonebook() {
		Person testPerson = new Person();
		testPerson.name = new Name("John", "Blakemore");
		testPerson.address = "4 City Centre, Coventry, UK";
		testPerson.phoneNumber = "(111) 333-9000";
		
		PhoneBookImpl pbi = new PhoneBookImpl();
		pbi.peopleMap = new HashMap<Name, Person>();
		
		pbi.addPerson(testPerson);
		
		try {
			Person foundPerson = pbi.findPerson(testPerson.name);
			assertEquals(testPerson, foundPerson);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testFindPersonInDatabase() {
		
		//'Chris Johnson','(321) 231-7876', '452 Freeman Drive, Algonac, MI'
		Person testPerson = new Person();
		
		testPerson.name = new Name("Chris Johnson");
		testPerson.address = "452 Freeman Drive, Algonac, MI";
		testPerson.phoneNumber = "(321) 231-7876";
		
		PhoneBookImpl pbi = new PhoneBookImpl();
		pbi.peopleMap = new HashMap<Name, Person>();
		DatabaseUtil.initDB();
		
		//Here, we know that initDB added two people that are not in the local store, so we search for one of them
		//if we find it, it means we can find entries in the database via name
		try {
			assertEquals(testPerson, pbi.findPerson(testPerson.name) );
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testFindPersonNotFound() {
		//Verify we return null if a matching person is not found in the local or the database
		Person testPerson = new Person();
		testPerson.name = new Name("Ansel", "Adams");
		testPerson.address = "5555 Nikon Drive, Kodachrome, CA";
		testPerson.phoneNumber = "(248) 123-7000";
		
		PhoneBookImpl pbi = new PhoneBookImpl();
		pbi.peopleMap = new HashMap<Name, Person>();
		
		try {
			Person foundPerson = pbi.findPerson(testPerson.name);
			assertEquals(null, foundPerson);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
	}
	
	@Test
	public void testAddPersonLocalPhonebook() {
		Person testPerson = new Person();
		testPerson.name = new Name("Flickr", "User");
		testPerson.address = "5555 Nikon Drive, Kodachrome, CA";
		testPerson.phoneNumber = "(248) 123-7000";
		
		PhoneBookImpl pbi = new PhoneBookImpl();
		pbi.peopleMap = new HashMap<Name, Person>();
		
		pbi.addPerson(testPerson);
		
		//At this point, we know the database hasn't been initialized, so the only place this person object
		//could be stored is in the local store, which means we verify that Adding a person object to the local
		//store works.
		
		try {
			Person foundPerson = pbi.findPerson(testPerson.name);
			assertEquals(testPerson, foundPerson);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
	}
	
	@Test
	public void testAddPersonDatabase() {
		Person testPerson = new Person();
		testPerson.name = new Name("Flickr", "User");
		testPerson.address = "5555 Nikon Drive, Kodachrome, CA";
		testPerson.phoneNumber = "(248) 123-7000";
		
		PhoneBookImpl pbi = new PhoneBookImpl();
		pbi.peopleMap = new HashMap<Name, Person>();
		
		//Make sure we have an empty database
		DatabaseUtil.initDB();
		DatabaseUtil.wipeDb();
		
		
		//Add Person to localStore
		//Synchronize to database
		//Remove from localStore (so we are guaranteed when we do find the entry, it's from the database)
		//Use Find to verify existence in database
		
		try {
			pbi.addPerson(testPerson);	
			pbi.addPhonebookContentsToDatabase();
			
			//Make sure it's gone from the local store
			pbi.peopleMap.remove(testPerson.name);
			assertEquals(false, pbi.peopleMap.containsKey(testPerson.name));
		
			Person foundPerson = pbi.findPerson(testPerson.name);
			assertEquals(testPerson, foundPerson);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
