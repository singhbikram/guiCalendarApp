import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CalController extends JFrame implements ChangeListener{
	private MyCalendar myCal;
	
	public CalController(MyCalendar cal){
		myCal = cal;
		JPanel panel = new JPanel();
		panel.setSize(100, 200);
		final JTextArea textArea = new JTextArea(200,400);
		JPanel panelnext = new JPanel();
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
				panel.add(textfeild2);
				panel.add(new JLabel("Start Time: "));
				panel.add(textfeild3);
				panel.add(new JLabel("End Time(if any): "));
				panel.add(textfeild4);
				submitBtn.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						myCal.create(textfeild1.getText(), textfeild2.getText(),textfeild3.getText(),textfeild4.getText());
						frame.dispose();
					}
					
				});
				panel.add(submitBtn);
				frame.add(panel);
				//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setVisible(true);
			}
		});
		JButton prevBtn = new JButton("<");
		prevBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//textArea.setText(myCal.previousDay());
			}
		});
		JButton nextBtn = new JButton(">");
		nextBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//textArea.setText(myCal.nextDay());	
			}
		});
		JButton quitBtn = new JButton("Quit");
		quitBtn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		    	myCal.saveAndQuit(); System.out.println("Changes are updated!");
		    	dispose();
		    }
		});
		
		JPanel calPanel = new JPanel();
		calPanel.setSize(200, 400);
		calPanel.setLayout(new GridLayout(5, 7));
		panelnext.add(prevBtn,  BorderLayout.SOUTH);
		panelnext.add(nextBtn, BorderLayout.SOUTH);
		panelnext.add(newEventBtn, BorderLayout.NORTH);
		
		
		for(int i =1; i < 30; i++){
			JButton btn = new JButton();
			if(i==12)
			btn.setBackground(Color.MAGENTA); 
			//btn.setSize(20,20);
			btn.setText(""+i);
			calPanel.add(btn);
		}
		panel.add(panelnext, BorderLayout.WEST);
		panel.add(quitBtn, BorderLayout.EAST);
		
		add(panel, BorderLayout.NORTH);
		add(calPanel, BorderLayout.WEST);
		add(textArea, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    pack();
	    setVisible(true);

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}

}
