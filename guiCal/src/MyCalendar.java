import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**MyCalendar implements a calendar similar to the one on phone. 
 * The calendar is implemented as a console application.
 * @author Bikram Singh
 *added to github
 */
enum MONTHS
{
	January, February, March, April, May, June, July, August, September, October, November, December;
}
enum DAYS
{
	Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday ;
}
	public class MyCalendar implements Serializable 
	{	
		private static final long serialVersionUID = 1L;
		private TreeMap<Integer, TreeMap<Integer, Event>> events;
		public GregorianCalendar cal;
		ArrayList<ChangeListener> listeners;
	
	/**Constructor for MyCalendar
	 */	
	public MyCalendar()
	{
		cal = new GregorianCalendar(); // capture today
		events = new TreeMap<Integer, TreeMap<Integer, Event>>();
		listeners = new ArrayList<ChangeListener>();
	}
	
	public void addListener(ChangeListener l){
		listeners.add(l);
	}
	/**
	 * This method helps the system to load events.txt to populate the calendar. 
	 * If there is no such file because it is the first run, 
	 * the load function prompts a message to the user indicating this is the first run.
	 * Method uses Java deSerialization to populate events.
	 */
	@SuppressWarnings("unchecked")
	public void load()
	{
		// check if file exits before loading 
		File eventsFile = new File("./src/events.txt");
		if(eventsFile.exists())
		{
			try
			{		
				FileInputStream inputfile = new FileInputStream("./src/events.txt"); 
			    ObjectInputStream input = new ObjectInputStream(inputfile);
			    try {
					this.events = (TreeMap<Integer, TreeMap<Integer, Event>>) input.readObject();
				} catch (ClassNotFoundException e) {e.printStackTrace();
				}
		
			    input.close();
	
			} catch (FileNotFoundException e) {e.printStackTrace();
			} catch (IOException e) {e.printStackTrace();
			}
		}
		else
		System.out.println("This is the first run.");	
	}
	
	/**This method takes the user input to create an event. 
	 * @param sc Scanner takes the input from user
	 */
	public void create(String eventName, String dateString, String sTime, String endTime)
	{
		Integer date;
		GregorianCalendar gc;
		
		Pattern dateRegex = Pattern.compile("^(0[1-9]|[1][0-2])/(0[1-9]|[12][0-9]|3[01])/([0-2][0-9]{3})$");
		Matcher matchVerifier;

	    do
	    {
		    matchVerifier = dateRegex.matcher(dateString);
	
		          if(!matchVerifier.matches())
		          {
		              System.out.println("\nPlease enter the date as mm/dd/yyyy");
		          }
	     } while(!matchVerifier.matches());
	    
	    String yearString = dateString.substring(6, 10);
	    String monthDayStr = dateString.substring(0, 6);
	    dateString = yearString + monthDayStr ; 

		date = Integer.parseInt(dateString.replaceAll("[^0-9]+", ""));
		
		Integer startTime = Integer.parseInt(sTime.replaceAll("[^0-9]+", ""));
		
		
		gc = new GregorianCalendar(date / 10000, ((date%10000)/100)-1, (date % 100));
		Event newEvent = new Event(eventName, gc, sTime, endTime);
		
		if(events.containsKey(date)){
			//TreeMap<Integer, Event> event1 = new TreeMap<Integer, Event>();
			(events.get(date)).put(startTime, newEvent);
		}
		else
		{
			TreeMap<Integer, Event> addEvent = new TreeMap<Integer, Event>();
			addEvent.put(startTime, newEvent);
			events.put(date, addEvent);
		}
			
		System.out.println("\nFollowing Event is Added : ");
		System.out.println(newEvent);	
		
		saveAndQuit();
		setCal(date / 10000, ((date%10000)/100)-1, (date % 100));
		
	}// end create
	
	/**This method saves the information when client wants to exit the system.
	 * And uses Java Serialization to store the events before exiting.
	 */
	public void saveAndQuit()
	{
		FileOutputStream output;
		try{
			output = new FileOutputStream("./src/events.txt");
			ObjectOutputStream outputStream = new ObjectOutputStream(output);
			outputStream.writeObject(events);
			outputStream.close();
		}
		catch (FileNotFoundException e1) {e1.printStackTrace();
		} catch (IOException e2) {e2.printStackTrace();
		}
	}// method saveAndQuit
	
	/**
	 * This method creates a sorted list of all the events in the system.
	 */
	public String eventList()
	{		
		return displayAllEvents(events, true);
	}// end eventList
	
	public void previousDay(){
		daysEvents(events, dayKey(0), false);
		dayKey(-1);
		ChangeEvent event = new ChangeEvent(this);
		for (ChangeListener listener : listeners)
			listener.stateChanged(event);
		//int i = dayKey(-1);
		//return daysEvents(events, i, false);
	}

	public void nextDay(){
		daysEvents(events, dayKey(0), false);
		//int i = dayKey(1);
		//return daysEvents(events, i, false);
		dayKey(1);
		ChangeEvent event = new ChangeEvent(this);
		for (ChangeListener listener : listeners)
			listener.stateChanged(event);
		
	}
	// string of events of the day with key i of treeMap
	public String currentDay(){
		int day =  (cal.get(Calendar.DAY_OF_MONTH));
		String improvedDay = String.format("%02d", day);
		int month =  (cal.get(Calendar.MONTH) +1);
		String improvedMonth = String.format("%02d", month);

		int k = Integer.valueOf(String.valueOf(cal.get(Calendar.YEAR)) + improvedMonth + improvedDay );

		return daysEvents(events, k, false);
	}
	

	/**sets the calendar to given date
	 * @param year
	 * @param month
	 * @param date
	 */
	public void setCal(int year, int month, int date) {
		cal.set(year, month, date);
		ChangeEvent event = new ChangeEvent(this);
		for (ChangeListener listener : listeners)
			listener.stateChanged(event);
		
	}


	/**This method displays events per day when "Day" option is chosen
	 * And highlights the events on the monthly calendar, when chosen to see "Month" view.
	 * @param scanner Scanner takes the user even when needed.
	 */
	public void viewBy(Scanner scanner)
	{
		System.out.println("\n[D]ay or [M]onth?\n");
		//Scanner scanner = new Scanner(System.in);
		String option = scanner.nextLine();
		if(option.equals("d"))
		{
			printCalendar(cal);// prints today
			System.out.println("");
			daysEvents(events, dayKey(0), false);
			System.out.println("\n[P]revious or [N]ext or [M]ain menu?\n");
			String choice = scanner.nextLine();
			while(!choice.equals("m"))
			{
				switch(choice)
				{
					case "p" : case "P":
						int i = dayKey(-1);
						printCalendar(cal);
						System.out.println("");
						daysEvents(events, i, false);
							break;
					
					case "n" : case "N":
						int k = dayKey(1);
						printCalendar(cal);
						System.out.println("");
						daysEvents(events, k, false);
							default: break;
				}
				System.out.println("\n[P]revious or [N]ext or [M]ain menu?\n");
				choice = scanner.nextLine();
			}// end while
			cal = new GregorianCalendar();// sets the calendar back to today
		}
		else
		{	int[] eventDays = eventDays();
			
			printEventsOfMonth(eventDays);
			System.out.println("\n[P]revious or [N]ext or [M]ain menu?\n");
			String choice = scanner.nextLine();
			while(!choice.equals("m"))
			{
				switch(choice)
				{
					case "p" : case "P":
						cal.add(Calendar.MONTH,-1);
						eventDays = eventDays();
						printEventsOfMonth(eventDays);
							break;
					
					case "n" : case "N":
						cal.add(Calendar.MONTH,1);
						eventDays = eventDays();
						printEventsOfMonth(eventDays);
						break;
					default: break;
				}
				System.out.println("\n[P]revious or [N]ext or [M]ain menu?\n");
				choice = scanner.nextLine();
			}// end while
			cal = new GregorianCalendar();// sets the calendar back to today			
		}
	}
	/**Method "dayKey" return the key value date date in the "events" instance variable
	 * by adding and subtracting days requested as parameters
	 * @param dayGap , number of days back or forward
	 * @return key value of instance variable of "events" 
	 */
	public int dayKey(int dayGap)
	{
		cal.add(Calendar.DAY_OF_MONTH, dayGap);
		
		int day =  (cal.get(Calendar.DAY_OF_MONTH));
		String improvedDay = String.format("%02d", day);
		int month =  (cal.get(Calendar.MONTH) +1);
		String improvedMonth = String.format("%02d", month);

		int k = Integer.valueOf(String.valueOf(cal.get(Calendar.YEAR)) + improvedMonth + improvedDay );
		return k;
	}
	
	/**Method eventDays creates an array of all the days which have events.
	 * @return array of days with events.
	 */
	public int[] eventDays()
	{
		int[] a = new int[31];
		Set<Integer> keys = events.keySet();
		Iterator<Integer> iterator = keys.iterator();
		int i = 0;
		while(iterator.hasNext())
		{
	        Integer key = (Integer) iterator.next();
	        int thisMonth = cal.get(Calendar.MONTH) + 1;
	        int day = key%100; // get the day
	        if(thisMonth == (key % 10000)/100) // get current month
	        {
	        	a[i] = day; // add the day if it has events to the array
	        	i++;
	        }
		}
		return a;
	}
	
	/**Method "goto" shows the events of the day selected
	 * @param sc Scanner object takes the input of the day
	 */
	public void goTo(Scanner sc)
	{
		System.out.print("Please Enter a date(MM/DD/YYYY) : ");
		String dateString = sc.nextLine();
		int date = 0;
		String yearString = dateString.substring(6, 10);
	    String monthDayStr = dateString.substring(0, 6);
	    dateString = yearString + monthDayStr ; 
		date = Integer.parseInt(dateString.replaceAll("[^0-9]+", ""));
		GregorianCalendar temp = new GregorianCalendar(date / 10000, ((date%10000)/100)-1, (date % 100));
		printCalendar(temp);
		System.out.println("");
		daysEvents(events, date, false);
	}//end goTo

	public boolean isTimeAvailable(int day, int startTime, int endTime)
	{
		//System.out.println("date :"+day+"stime : "+startTime+"endtime :"+endTime);
		boolean available = true;
		//ArrayList<Integer> occupied = new ArrayList<Integer>();
		if(events.isEmpty() || events == null){
			return available; 
		}
		else{
		TreeMap<Integer, Event> t =  events.get(day);
		
		if(t != null){
			Set<Entry<Integer, Event>> set = t.entrySet(); // create a set of all the keys in the map
		
		Iterator<Entry<Integer, Event>> iterator = set.iterator();
		
		while(iterator.hasNext())
		{
	        @SuppressWarnings("rawtypes")
			Map.Entry mentry = (Map.Entry)iterator.next();
	        Event event = (Event) mentry.getValue();
	        //System.out.println("Event stime : "+event.getStartTime()+"endtime :"+event.getEndTime());
	        if (startTime >= event.getStartTime() && startTime <= event.getEndTime())
	        	available = false;
	        if (endTime >= event.getStartTime() && endTime <= event.getEndTime())
	        	available = false;
	        if (startTime <= event.getStartTime() && endTime >= event.getEndTime())
	        	available = false;	        
	     }
		}
		/*Iterator<Integer> itr = occupied.iterator();
		while(itr.hasNext()){
			if(itr.next().equals(day))
				return false;
		}*/
		return available;
		}
	}
	
	/**Method "daysEvents" displays the evetns of the day
	 * @param t	is the TreeMap object has the list of events
	 * @param s	helps find the day for which one wants to see the events.
	 * @param f helps to select the display format.
	 */
 	public static String daysEvents(TreeMap<Integer, TreeMap<Integer, Event>> t, int s, boolean f)
	{
		String events = null;
		if(t.isEmpty()){
			System.out.println("No Events found.");
			return "No Events found.";
		}
		else{
		TreeMap<Integer, Event> todaysEvents =  t.get(s);		
		events = displayEvents(todaysEvents, f);
		}
		
		return events;
	}
	
	/**Method "displayEvents" circle through sub TreeMap to show the events of the day.
	 * @param t subtree which has all the events of one day.
	 * @param f helps chose the showing format of the event requested.
	 */
	public static String displayEvents(TreeMap<Integer, Event>  t, boolean f )
	{
		String eventsList = "";
		if (t == null || t.isEmpty())
		{	
			System.out.println("Sorry, No Events Scheduled.");
			return "Sorry, No Events Scheduled.";
		}
		else
		{
			Set<Entry<Integer, Event>> set = t.entrySet(); // create a set of all the keys in the map
			Iterator<Entry<Integer, Event>> iterator = set.iterator();
			
			while(iterator.hasNext())
			{
		         @SuppressWarnings("rawtypes")
				Map.Entry mentry = (Map.Entry)iterator.next();
		         Event printEvent = (Event) mentry.getValue();
		         if(f)
		        	 eventsList= printEvent.toString();
		         else
		         {
		        	 eventsList = eventsList + printEvent.printEventnoDate();
		         }
		     }
		}
		return eventsList;
	}// end displayEvents
	
	/**Method "displayAllEvents" takes the "events" main TreeMap and makes it possible to traverse the 
	 * main tree. 
	 * @param t "events" collection of all the lists.
	 * @param f defines event showing format.
	 */
	public static String displayAllEvents(TreeMap<Integer, TreeMap<Integer, Event>> t, boolean f)
	{
		String events = null;
		if (t.isEmpty())
		{	
			//System.out.println("No Events were found.");
			return "No Events were found.";
		}
		else
		{
			Set<Integer> keys = t.keySet();
			Iterator<Integer> iterator = keys.iterator();
			int year = 0;
			while(iterator.hasNext())
			{
		        Integer key = (Integer) iterator.next();
		        if(year != key / 10000) // checks to display year before the list of all the events
		        {
		        	year = key / 10000;
		        	System.out.println(year);
		        }
		        events = events + daysEvents(t, key, f);
			}
		}
		return events;
	}// end method displayAllEvents
	
	/**
	 * Method "printMonth" displays a monthly view of calendar with today's date highlighted. 
	 */
	public void printMonth()
	{
		MONTHS[] arrayOfMonths = MONTHS.values();
	    DAYS[] arrayOfDays = DAYS.values();
		System.out.println("         "+arrayOfMonths[cal.get(Calendar.MONTH)] +
				" "+ cal.get(Calendar.YEAR)+"\n");
		System.out.println(" Su  Mo  Tu  We  Th  Fr  Sa\n");
		GregorianCalendar temp = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
	    int firstDayOfMonth = temp.get(Calendar.DAY_OF_WEEK)-1;
	    //System.out.println(firstDayOfMonth);
	    
	    for(int i = 1-firstDayOfMonth; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH) ; i++)
		{		
			if(i <= 0 )
			{
				System.out.printf("    ");
			} 
			else 
			{
				GregorianCalendar today = new GregorianCalendar();
				if(i == cal.get(Calendar.DAY_OF_MONTH) && cal.get(Calendar.MONTH) == today.get(Calendar.MONTH))
				{
					System.out.printf("[%2d]", i);
				}
				else
					System.out.printf(" %2d ", i);
			
			if(((i+firstDayOfMonth)%7) == 0)
				System.out.println("\n");
		}
		}
		
	}// end method printMonth
	
	/**Method "deleteEvent" takes the input from user as a date and removes event of that day.
	 * Or if selected all, will remove all the events from the calendar.
	 * @param sc Scanner takes the user input as requested.
	 */
	public void deleteEvent()
	{
		
		/*System.out.print("[S]elected or [A]ll ?");
		String input = sc.nextLine();
	    if(input.equals("s"))
	    {
	    	//TreeMap<Integer, Event> todaysEvents;
			String dateString;
			Pattern dateRegex = Pattern.compile("^(0[1-9]|[1][0-2])/(0[1-9]|[12][0-9]|3[01])/([0-2][0-9]{3})$");
			Matcher matchVerifier;
			System.out.print("\nPlease Enter the Date to delete event/s: ");
		    do
		    {
			    dateString = sc.nextLine();
			    matchVerifier = dateRegex.matcher(dateString);
		
			          if(!matchVerifier.matches())
			          {
			              System.out.println("Please enter the date as mm/dd/yyyy");
			          }
		     } while(!matchVerifier.matches());
		    String today = dateString;
		    String yearString = dateString.substring(6, 10);
		    String monthDayStr = dateString.substring(0, 6);
		    dateString = yearString + monthDayStr ; 
			int eventDate = Integer.parseInt(dateString.replaceAll("[^0-9]+", ""));
			events.remove(eventDate);
			System.out.println("No more events for "+ today + "!");
	    }
	    else
	    {*/
	    	events.clear();
	    	ChangeEvent event = new ChangeEvent(this);
			for (ChangeListener listener : listeners)
				listener.stateChanged(event);
			
	    	System.out.println("All the events are deleted!");
	    //}
	    saveAndQuit();
	}// end deleteEvent 
	/**Method "printEventsOfMonth" takes a list of all the days which have events and 
	 * highlights them on the monthly calendar.
	 * @param eventDays is the list of days which has events. 
	 */
	public void printEventsOfMonth(int[] eventDays)
	{
		MONTHS[] arrayOfMonths = MONTHS.values();
	    DAYS[] arrayOfDays = DAYS.values();
	    System.out.print("         ");
		System.out.print(arrayOfMonths[cal.get(Calendar.MONTH)]);
		System.out.print(" ");
		System.out.print(cal.get(Calendar.YEAR));
	    System.out.print("\n");
	    
	    System.out.println("\n Su  Mo  Tu  We  Th  Fr  Sa\n");
	    
	    GregorianCalendar temp = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
	    int firstDayOfMonth = temp.get(Calendar.DAY_OF_WEEK)-1;
	    
	    for(int i = 1-firstDayOfMonth; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH) ; i++)
		{		
			if(i <= 0 )
			{
				System.out.printf("    ");
			} 
			else 
			{
			
			if(isEventDay(i, eventDays))
				{
					System.out.printf("[%2d]", i);
				}
			else
				System.out.printf(" %2d ", i);
			
			if(((i+firstDayOfMonth)%7) == 0)
				System.out.println("\n");
		}
		}
	}
	/**Method "isEvent" compares a day with a list and tells if the list has that day
	 * @param i the day users is looking to compare
	 * @param array a list of some days
	 * @return boolean is returned if list has particular day.
	 */
	public static boolean isEventDay(int i, int[] array)
	{	
		boolean result = false;
		for(int a : array)
		{
			if(a == i)
				result = true;
		}
		return result;
			
	}//end isEventDay
	
	/**Method "printCalendar" displays the day
	 * @param c Calendar is referenced to display day
	 */
	public static void printCalendar(Calendar c)
	{   MONTHS[] arrayOfMonths = MONTHS.values();
	    DAYS[] arrayOfDays = DAYS.values();
	    
	    System.out.print(arrayOfDays[c.get(Calendar.DAY_OF_WEEK)-1]);
	    System.out.print(" ");
		System.out.print(arrayOfMonths[c.get(Calendar.MONTH)]);
		System.out.print(" ");
		System.out.print(c.get(Calendar.DAY_OF_MONTH));
		System.out.print(", ");
		System.out.print(c.get(Calendar.YEAR));
		System.out.print(" ");
	}

	/**Class Event proides objects to store the events.
	 * @author Bikram Singh
	 *
	 */
	public class Event implements Serializable 
	{
		private static final long serialVersionUID = 1L;
		private String eventName;
		private GregorianCalendar date;
		private String startTime;
		private String endTime;
		/**Constructor of class Events takes following parameters
		 * @param eventName name of the event
		 * @param date date of the event
		 * @param startTime start time of the event
		 * @param endTime end time of the event
		 */
		public Event(String eventName, GregorianCalendar date, String startTime, String endTime) 
		{
			this.eventName = eventName;
			this.date = date;
			this.startTime = startTime;
			this.endTime = endTime;
		}
		@Override
		public String toString()
		{
			printCalendar(date);
			return String.format(" %s-%s %s\n", startTime, endTime, eventName );
		}
		/**Method "printEventnoDate" method formats the event without date.
		 * @return a formatted string
		 */
		public String printEventnoDate()
		{
			return String.format(" %5s-%5s | %s\n----------------------------------\n", startTime, endTime, eventName );	
		}
		
		public int getStartTime(){
			return Integer.parseInt(startTime.replaceAll("[^0-9]+", ""));
		}
		
		public int getEndTime(){
			return Integer.parseInt(endTime.replaceAll("[^0-9]+", ""));
		}
		
		public long getEventDuration(){
			
			//HH converts hour in 24 hours format (0-23), day calculation
			SimpleDateFormat format = new SimpleDateFormat("mm:ss");

			Date d1 = null;
			Date d2 = null;

			try {
				d1 = format.parse(startTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				d2 = format.parse(endTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return d1.getTime()-d2.getTime();
		}
		
	}// end inner class events
	
	
}//end main class MyCalendar
