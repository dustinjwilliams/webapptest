package com.proquest.interview.phonebook;

//One reason to make this class final is because it's being used as a key in a hashMap and should be immutable.
public final class Name {
	
	private String firstName;
	private String lastName;
	
	
	Name(String inFirstName, String inLastName) {
		firstName = inFirstName;
		lastName = inLastName;
	}
	
	//NOTE: this constructor assumes too much for real world use.  It's meant to be used
	//to reconstruct a Name object from the concatenated string that is stored in the database.
	//In the real world, we'd either split first/last name up into columns in the database or do a lot of error checking
	//trimming on the input string.  If splitting on  space returns nothing, we're assuming Ronaldo only has one name (the first)
	Name( String fullName ) {
		
		firstName = "";
		lastName = "";
		
		String[] temp;
		String delim = " ";
		temp = fullName.split(delim, 2);
		
		if(temp.length == 1)
			firstName = temp[0];
		else if(temp.length == 2){
			firstName = temp[0];
			lastName = temp[1];
		}
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	@Override
	public String toString() {
		return firstName + " " + lastName;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	//Override equals method so we can compare internal values to make sure the 
	//contents of the object are equal.
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Name other = (Name) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}
	
}
