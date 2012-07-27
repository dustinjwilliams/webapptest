package com.proquest.interview.phonebook;

import java.sql.SQLException;

public interface PhoneBook {
	public Person findPerson(Name findMe) throws ClassNotFoundException, SQLException;
	public void addPerson(Person newPerson);
	public void addPhonebookContentsToDatabase() throws ClassNotFoundException, SQLException;
}
