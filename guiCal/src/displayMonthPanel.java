import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

public class displayMonthPanel extends JPanel{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MyCalendar myCal;
	private int currentBtnIndex;
	private JButton[] buttons;

public displayMonthPanel(GregorianCalendar currentCal)
{
	//currentCal = myCal.cal;
	currentBtnIndex = currentCal.get(Calendar.DATE);
	System.out.println("Date in displayMonthPanel : "+currentCal.get(Calendar.DATE)+"/"+(currentCal.get(Calendar.MONTH)+1));
	JPanel calPanel = new JPanel();// outermost Panel for whole MonthView
	//calPanel.removeAll();
	//calPanel.updateUI();
	calPanel.setPreferredSize(new Dimension(400, 200));
	calPanel.setLayout(new FlowLayout());
    
	//calDayBtnPanel is panel for all the buttons
	JPanel calDayBtnPanel = new JPanel(); 
	
    MONTHS[] arrayOfMonths = MONTHS.values();
    //DAYS[] arrayOfDays = DAYS.values();
    JPanel dayListPanel = new JPanel();
    JLabel monthYearLabel = new JLabel("         "+arrayOfMonths[currentCal.get(Calendar.MONTH)] +
			" "+ currentCal.get(Calendar.YEAR));
    calPanel.add(monthYearLabel);
    
    JLabel days = new JLabel("Su            Mo           Tu           We           Th            Fr             Sa");
    dayListPanel.add(days, BorderLayout.SOUTH);
    
    calPanel.add(days, BorderLayout.NORTH);
    
    //currentCal = myCal.cal;
    GregorianCalendar temp = new GregorianCalendar(currentCal.get(Calendar.YEAR), currentCal.get(Calendar.MONTH), 1);
    int firstDayOfMonth = temp.get(Calendar.DAY_OF_WEEK)-1;
    
    calDayBtnPanel.setLayout(new GridLayout(0,7));//buttons
    int totalDaysOfMonth = currentCal.getActualMaximum(Calendar.DAY_OF_MONTH)+1;
    
    System.out.println("Days in the Month : "+totalDaysOfMonth+"/"+totalDaysOfMonth+firstDayOfMonth);
    this.buttons = new JButton[totalDaysOfMonth+firstDayOfMonth];
    
    ActionListener buttonPress = new ActionListener()
    {
    		public void actionPerformed(ActionEvent e) 
			{
				final JButton b = (JButton) e.getSource();
		        final int i = Integer.parseInt(b.getText());
		        System.out.println("CurrentBtnIndex after button clicked : "+currentBtnIndex);
		        //buttons[currentBtnIndex].setBackground(null);
		        //b.setBackground(Color.MAGENTA);
		        //currentBtnIndex = i;
		        System.out.println("Button Clicked "+i);
		        int day = i;
				String improvedDay = String.format("%02d", day);
				int month =  (myCal.cal.get(Calendar.MONTH) +1);
				String improvedMonth = String.format("%02d", month);
				// find the k, key for TreeMap
				int k = Integer.valueOf(String.valueOf(myCal.cal.get(Calendar.YEAR)) + improvedMonth + improvedDay );
				myCal.setCal(myCal.cal.get(Calendar.YEAR), myCal.cal.get(Calendar.MONTH), i);
			}
	};
    
    for (int i = 1-firstDayOfMonth; i < totalDaysOfMonth; i++) {
    	final JButton btn = new JButton("");
		//btn.setPreferredSize(new Dimension(50, 50));
		// disable button in days before 1st
    	if(i <= 0)// || i > totalDaysOfMonth)
		{
			btn.setEnabled(false);;
		}
    	else 
		{/*
			GregorianCalendar today = new GregorianCalendar();
			//System.out.println("Day Of Month : "+currentCal.get(Calendar.DAY_OF_MONTH));
			if(i == currentCal.get(Calendar.DAY_OF_MONTH) && currentCal.get(Calendar.MONTH) == today.get(Calendar.MONTH))
			{
				//btn.setBackground(Color.MAGENTA);
				currentBtnIndex = i;
				//System.out.println("Current Button in if : "+ currentBtnIndex);
				btn.setText(""+i);
			}
			else{
				btn.setText(""+i);
			}
			
			*/
    		btn.setText(""+i);
			//currentBtnIndex = currentCal.get(Calendar.DATE);
			//System.out.println("CurrentBtnIndex : "+ currentBtnIndex);
			if( i == currentBtnIndex){
				System.out.println("CurrentBtnIndex : "+ currentBtnIndex);
				btn.setBackground(Color.MAGENTA);
			}
			this.buttons[i] = btn;

		}
        btn.addActionListener(buttonPress);   
        
        calDayBtnPanel.add(btn);
    }
    calPanel.add(calDayBtnPanel, BorderLayout.SOUTH);
    //return calPanel;
	}//end constructor
}
