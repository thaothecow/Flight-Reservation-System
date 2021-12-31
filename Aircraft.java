// Thao Nguyen (501017698)

import java.util.Arrays;
import java.util.TreeMap;

public class Aircraft implements Comparable<Aircraft>
{
  int numEconomySeats;
  int numFirstClassSeats; 
  String model;
  // new fields
  String[][] seatLayout;
  int row;
  int column;
  TreeMap<String, String> reservedSeats;
  
  public Aircraft(int seats, String model)
  {
  	this.numEconomySeats = seats;
  	this.numFirstClassSeats = 0;
  	this.model = model;
	// new fields to initialize seatLayout
	reservedSeats = new TreeMap<>();
	row = 4;
	column = seats / 4;
	seatLayout = new String[row][column];
	seatLayout = assignSeatNum(seatLayout);
	seatLayout = assignSeatLetter(seatLayout);

  }

  public Aircraft(int economy, int firstClass, String model)
  {
  	this.numEconomySeats = economy;
  	this.numFirstClassSeats = firstClass;
  	this.model = model;
	// new fields to initialize seatLayout
	reservedSeats = new TreeMap<>();
	row = 4;
	column = (economy + firstClass) / 4;
	seatLayout = new String[row][column];
	seatLayout = assignSeatNum(seatLayout);
	seatLayout = assignSeatLetter(seatLayout);
	seatLayout = assignFirstClass(seatLayout);
  }
  
	public int getNumSeats()
	{
		return numEconomySeats;
	}
	public int getTotalSeats()
	{
		return numEconomySeats + numFirstClassSeats;
	}
	public int getNumFirstClassSeats()
	{
		return numFirstClassSeats;
	}
	public String getModel()
	{
		return model;
	}
	public void setModel(String model)
	{
		this.model = model;
	}
	// get seatLayout method
	public String[][] getSeatLayout()
	{
		return seatLayout;
	}
	public void print()
	{
		System.out.println("Model: " + model + "\t Economy Seats: " + numEconomySeats + "\t First Class Seats: " + numFirstClassSeats);
	}

  public int compareTo(Aircraft other)
  {
  	if (this.numEconomySeats == other.numEconomySeats)
  		return this.numFirstClassSeats - other.numFirstClassSeats;
  	
  	return this.numEconomySeats - other.numEconomySeats; 
  }

  // methods for new field seatLayout
  
  // assign seat numbers for seatLayout
  public String[][] assignSeatNum(String[][] seatLayout)
  {
	 int num = 1; 

	 for (int i = 0; i < row; i++) {
		 for (int j = 0; j < column; j++) {
			 seatLayout[i][j] = Integer.toString(num);
			 num++;
		 }
		 num = 1;
	 }
	 return seatLayout;
  }
  // assign seat letters for seatLayout
  public String[][] assignSeatLetter(String[][] seatLayout) 
  {
	char c = 'A';

	for (int i = 0; i < row; i++) {
		for (int j = 0; j < column; j++) {
			seatLayout[i][j] += Character.toString(c);
		}
		c++;
	}
	return seatLayout;
  }
  // assign first class seats
  public String[][] assignFirstClass(String[][] seatLayout)
  {
	  for (int i = 0; i < row; i++) {
		  for (int j = 0; j < this.numFirstClassSeats / 4; j++) {

			seatLayout[i][j] += "+";
		  }
	  }
	  return seatLayout;
  }

}
