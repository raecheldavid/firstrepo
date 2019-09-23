package com.faith.test;

public class Main {

	public static void main(String args[])
	{
		
		SalesData objSalesData = new  SalesData();
		System.out.println("Hello");
		displayGreetings();
		objSalesData.display();
		
	}
	
	
	private static void displayGreetings()
	{
		
		System.out.println("Welcome to sales");
	}
}
