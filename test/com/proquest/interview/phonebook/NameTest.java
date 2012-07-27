package com.proquest.interview.phonebook;

import static org.junit.Assert.*;

import org.junit.Test;

public class NameTest {

	@Test
	public void testNoSpaceInName() {
		String singleName = "Ronaldo";
		
		//assert that a single name results in last Name = "" and firstName = singleName
		Name testName = new Name(singleName);
		assertEquals("Ronaldo", testName.getFirstName());
		assertEquals("", testName.getLastName());

	}
	
	@Test
	public void testNameAsSingleString() {
		String nameWithSpace = "Lionel Messi";
		Name testName = new Name(nameWithSpace);
		assertEquals("Lionel", testName.getFirstName());
		assertEquals("Messi", testName.getLastName());
	}
	
	@Test
	public void testNameWithMoreThanOneSpace(){
		String nameWithMultipleSpaces = "Wayne Misunderstood Rooney";
		Name testName = new Name(nameWithMultipleSpaces);
		assertEquals("Wayne", testName.getFirstName());
		assertEquals("Misunderstood Rooney", testName.getLastName());
		
	}

}
