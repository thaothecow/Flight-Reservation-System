import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;


// Flight System for one single day at YYZ (Print this in title) Departing flights!!


public class FlightReservationSystem 
{
	public static void main(String[] args) throws IOException
	{
		FlightManager manager = null;
		
		// catch IOExceptions
		try {
			manager = new FlightManager();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		ArrayList<Reservation> myReservations = new ArrayList<Reservation>();	// my flight reservations

		Scanner scanner = new Scanner(System.in);
		System.out.print(">");

		while (scanner.hasNextLine())
		{
			String inputLine = scanner.nextLine();
			if (inputLine == null || inputLine.equals("")) 
			{
				System.out.print("\n>");
				continue;
			}
			Scanner commandLine = new Scanner(inputLine);
			String action = commandLine.next();

			if (action == null || action.equals("")) 
			{
				System.out.print("\n>");
				continue;
			}
			else if (action.equals("Q") || action.equals("QUIT"))
				return;

			else if (action.equalsIgnoreCase("LIST"))
			{
				manager.printAllFlights(); 
			}
			else if (action.equalsIgnoreCase("RES"))
			{
				String flightNum = null;
				String passengerName = "";
				String passport = "";
				String seatType = "";

				if (commandLine.hasNext())
				{
					flightNum = commandLine.next();
				}
				if (commandLine.hasNext())
				{
					passengerName = commandLine.next();
				}
				if (commandLine.hasNext())
				{
					passport = commandLine.next();
				}
				if (commandLine.hasNext())
				{
					seatType = commandLine.next();
		
					try {
							Reservation res = manager.reserveSeatOnFlight(flightNum, passengerName, passport, seatType);
							myReservations.add(res);
							System.out.println();
							res.print();
						}
					catch (DuplicatePassengerException e) {
							System.out.println(e.getMessage());
					}
					catch (SeatOccupiedException e) {
							System.out.println(e.getMessage());
					}
					catch (InvalidSeatException e) {
							System.out.println(e.getMessage());
					}
					catch (FlightNotFoundException e) {
						System.out.println(e.getMessage());
					}
				}	
			}
			else if (action.equalsIgnoreCase("CANCEL"))
			{
				Reservation res = null;
				String flightNum = null;
				String passengerName = "";
				String passport = "";

				if (commandLine.hasNext())
				{
					flightNum = commandLine.next();
				}
				if (commandLine.hasNext())
				{
					passengerName = commandLine.next();
				}
				if (commandLine.hasNext())
				{
					passport = commandLine.next();
			
					try {
						int index = myReservations.indexOf(new Reservation(flightNum, passengerName, passport));
						
						if (index != -1) {
							manager.cancelReservation(myReservations.get(index)); 
							myReservations.remove(index);
						
						} else {
							res = new Reservation(flightNum, passengerName, passport);
							manager.cancelReservation(res); 
							myReservations.remove(index);
						}
					}
					catch (PassengerNotInManifestException e) {
						System.out.println(e.getMessage());
					}
					catch (FlightNotFoundException e) {
						System.out.println(e.getMessage());
					}			
				}
			}
			else if (action.equalsIgnoreCase("SEATS")) // print seatLayout
			{
				String flightNum = "";
				
				if (commandLine.hasNext())
				{
					flightNum = commandLine.next();
					
					try {
						manager.seatsAvailable(flightNum);
						System.out.println("\nXX = Occupied" + "   " + "+ = First Class");
					} 
					catch (FlightNotFoundException e) {
						System.out.println(e.getMessage());
					}
				}
			}
			else if (action.equalsIgnoreCase("MYRES"))
			{
				System.out.println();

				for (Reservation myres : myReservations)
					myres.print();
			}
			// print passenger manifest
			else if (action.equalsIgnoreCase("PASMAN"))
			{
				String flightNum = "";

				if (commandLine.hasNext())
				{
					flightNum = commandLine.next();
					
					try {
						manager.printManifest(flightNum);
					}
					catch (FlightNotFoundException e) {
						System.out.println(e.getMessage());
					}
				}
			}
			System.out.print("\n>");
		}
	} 
}


