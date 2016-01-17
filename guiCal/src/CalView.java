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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CalView extends JFrame implements ChangeListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyCalendar myCal;
	GregorianCalendar currentCal = new GregorianCalendar(); 
	int currentBtnIndex = 0;
	final JTextArea textArea = new JTextArea(200,400);
	JButton[] buttons;
	JPanel calPanel;
	int flagFirst = 0;
	public CalView(MyCalendar cal)// Cal View and Controller
	{
		myCal = cal;
		
		paint();
	}
	
	// paints the view and controller
	public void paint()
	{
		setLayout(new BorderLayout(5,5));
		JPanel panel = new JPanel();
		panel.setSize(100, 200);
		JPanel panelnext = new JPanel();
		//newEventBtn opens a form to create an event
		JButton newEventBtn = new JButton("Create Event");
		newEventBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JPanel panel= new JPanel();
				panel.setLayout(new GridLayout(5,2));
				final JFrame frame = new JFrame("Enter Event Info");
				JButton submitBtn = new JButton("Submit");
				final JTextField textfeild1 = new JTextField(20);
				final JTextField textfeild2 = new JTextField(20);
				final JTextField textfeild3 = new JTextField(20);
				final JTextField textfeild4 = new JTextField(20);
				panel.add(new JLabel("Event Name: "));
				panel.add(textfeild1);
				panel.add(new JLabel("Event Date(mm/dd/yyyy)"));
				textfeild2.setText(String.format("%02d", myCal.cal.get(Calendar.MONTH) +1)+"/"+String.format("%02d", myCal.cal.get(Calendar.DATE))+"/"+myCal.cal.get(Calendar.YEAR));
				panel.add(textfeild2);
				panel.add(new JLabel("Start Time: "));
				panel.add(textfeild3);
				panel.add(new JLabel("End Time(if any): "));
				panel.add(textfeild4);
				submitBtn.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						String dateString = textfeild2.getText();
						int date = 0;
						String yearString = dateString.substring(6, 10);
					    String monthDayStr = dateString.substring(0, 6);
					    dateString = yearString + monthDayStr ; 
						date = Integer.parseInt(dateString.replaceAll("[^0-9]+", ""));
						int sTime = Integer.parseInt(textfeild3.getText().replaceAll("[^0-9]+", ""));
						int eTime = Integer.parseInt(textfeild4.getText().replaceAll("[^0-9]+", ""));
						
						if(!myCal.isTimeAvailable(date, sTime, eTime)){
							JFrame frame = new JFrame("Error Message!");
							JOptionPane.showMessageDialog(frame, "Event is Already Scheduled at this time."); 
						}
						else{
						myCal.create(textfeild1.getText(), textfeild2.getText(),textfeild3.getText(),textfeild4.getText());
						
						frame.dispose();
						}
					}
				});
				panel.add(submitBtn);
				frame.add(panel);
				frame.setLocation(500,300);
				//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setVisible(true);
			}
		});
		JButton prevBtn = new JButton("<");
		prevBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				myCal.previousDay();
			}
		});
		
		JButton nextBtn = new JButton(">");
		nextBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				myCal.nextDay();
				}
		});
		JButton quitBtn = new JButton("Quit");
		quitBtn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		    	myCal.saveAndQuit(); 
		    	System.out.println("Changes are updated!");
		    	dispose();
		    }
		});
		//deleteEvent
		JButton deleteAll = new JButton("deleteAll");
		deleteAll.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		    	myCal.deleteEvent();
		    }
		});

		panelnext.add(prevBtn,  BorderLayout.SOUTH);
		panelnext.add(nextBtn, BorderLayout.SOUTH);
		panelnext.add(newEventBtn, BorderLayout.NORTH);
		
		//add calendar to the view
		calPanel = displayMonthPanel(myCal.cal);
		
		//add < > buttons to the panel 
		panel.add(panelnext, BorderLayout.WEST);
		
		panel.add(deleteAll, BorderLayout.EAST);
		panel.add(quitBtn, BorderLayout.CENTER);
		
		add(panel, BorderLayout.NORTH);
		add(calPanel, BorderLayout.WEST);
		
		textArea.setText(myCal.currentDay());
		add(textArea, BorderLayout.CENTER);
		
		setSize(800,500);
		setLocation(300,100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //pack();
	    setVisible(true);
	}// end  constructor
	
	
	

	
	public JPanel displayMonthPanel(GregorianCalendar currentCal)
	{
		currentCal = myCal.cal;
		JPanel calPanel = new JPanel();// outermost Panel for whole MonthView
		calPanel.setPreferredSize(new Dimension(400, 200));
		calPanel.setLayout(new FlowLayout());
	    
		//calDayBtnPanel is panel for all the buttons
		JPanel calDayBtnPanel = new JPanel(); 
		
	    MONTHS[] arrayOfMonths = MONTHS.values();
	    JPanel dayListPanel = new JPanel();
	    JLabel monthYearLabel = new JLabel("         "+arrayOfMonths[currentCal.get(Calendar.MONTH)] +
				" "+ currentCal.get(Calendar.YEAR));
	    calPanel.add(monthYearLabel);
	    
	    JLabel days = new JLabel("Su            Mo           Tu           We           Th            Fr             Sa");
	    dayListPanel.add(days, BorderLayout.SOUTH);
	    
	    calPanel.add(days, BorderLayout.NORTH);
	    GregorianCalendar temp = new GregorianCalendar(currentCal.get(Calendar.YEAR), currentCal.get(Calendar.MONTH), 1);
	    int firstDayOfMonth = temp.get(Calendar.DAY_OF_WEEK)-1;
	    
	    calDayBtnPanel.setLayout(new GridLayout(0,7));//buttons
	    int totalDaysOfMonth = currentCal.getActualMaximum(Calendar.DAY_OF_MONTH)+1;
	    
	    buttons = new JButton[totalDaysOfMonth+firstDayOfMonth];
	    
	    ActionListener buttonPress = new ActionListener()
	    {
	    		public void actionPerformed(ActionEvent e) 
				{
					final JButton b = (JButton) e.getSource();
			        final int i = Integer.parseInt(b.getText());
			        buttons[currentBtnIndex].setBackground(null);
			        b.setBackground(Color.MAGENTA);
			        currentBtnIndex = i;
			        myCal.setCal(myCal.cal.get(Calendar.YEAR), myCal.cal.get(Calendar.MONTH), i);
				}
		};
        
	    for (int i = 1-firstDayOfMonth; i < totalDaysOfMonth; i++) {
        	final JButton btn = new JButton("");
			btn.setPreferredSize(new Dimension(50, 50));
			
        	// disable button in days before 1st
        	if(i <= 0)
			{
				btn.setEnabled(false);;
			}
        	else 
			{
        		btn.setText(""+i);
				currentBtnIndex = currentCal.get(Calendar.DATE);
				if( i == currentBtnIndex)
					btn.setBackground(Color.MAGENTA);
				this.buttons[i] = btn;
			}
            btn.addActionListener(buttonPress);   
            calDayBtnPanel.add(btn);
        }
        calPanel.add(calDayBtnPanel, BorderLayout.SOUTH);
	    return calPanel;
	}//end displayMonthPanel

	@Override
	public void stateChanged(ChangeEvent arg0) {
		remove(calPanel);
		textArea.setText(myCal.currentDay());
		calPanel = displayMonthPanel(myCal.cal);
		add(calPanel, BorderLayout.WEST);
	}
}//end main class