
/**
 * @author Bikram Singh
 *
 */
public class SimpleCalendar 
{
	public static void main(String[] args) 
	{
		// create a scheduler/model 
		final MyCalendar myCal = new MyCalendar();
		//load serialized events
		myCal.load();
		//add the modle to view/controller
		final CalView view = new CalView(myCal);
		//add the changeListener to Model
		myCal.addListener(view);	
	}// end main
	
}
