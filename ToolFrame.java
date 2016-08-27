/*
 *Bryan Leung
 *Mrs. Gallatin
 *Period 3	
 *ToolFrame
 *Contains the tools for the metronome, which include the Tempo display,
 *	a slider for quick tempo modification, - , + , and tap tempo function, 
 *	and a tool to set the the # of notes and type of notes per measure
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
 *Contains the tools for the metronome, which include the Tempo display,
 *	a slider for quick tempo modification, - , + , and tap tempo function, 
 *	and a tool to set the the # of notes and type of notes per measure
 *
 *	In addition, it contains several subclasses ButtonListener, SliderListener, LeftBoxListener,
 *	RightBoxListener, and TapListener that act as listeners for the given tools. 
*/
public class ToolFrame extends JFrame implements ActionListener, EventListener
{
	/*** Listeners Used 
	 *ActionListener - ButtonListener, LeftBoxListener, RightBoxListener
	 *MouseListener - MouseMouseListener
	 *ChangeListener - SliderListener
	 */
	 
	 /*** GUI Elements Used 
	 *JButton, JTextField, JLabel, JSlider, JComboBox, 
	***/
	
		private JPanel upperWestPanel;
		private JPanel upperEastPanel;
		private JPanel middlePanel;

		private JPanel lowerPanel;
		
		private JLabel labelTempo;
		private JLabel labelTimeSignature;
		private JTextField fieldTempo;
	
		
		private JButton minus;
		private JButton plus;
		private JButton tapTempo;
		private ArrayList<Long> tapTimes;
		private int tapCounter;

		
		private JComboBox leftBox;
		private JComboBox rightBox;
		private int noteType;
		
		private JButton button;
		private JLabel label;
		
		private JSlider slider;
		private ButtonFrame mainB;
			
		
		/**
		 *Creates a toolFrame that contains the tools for the metronome, 
		 *	which include the Tempo display, a slider for quick tempo modification, 
		 *	- , + , and tap tempo function, and a tool to set the the # of notes and type of notes per measure
		 *
		 *	In addition, it creates 4 panels upperWestPanel, upperEastPanel, middlePanel , and lowerPanel 
		 *	to contain the tools
		 *
		 *	In addition, it creates a ButtonFrame object that will spawn right above it upon 
		 *	the frame being set visible
		**/		
		public ToolFrame()
		{
			mainB = new ButtonFrame();	
				
			setSize(607,405); //300,300
			setTitle("Metronome Functions");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
			this.setLayout(new BorderLayout());
	
			/**Panels**/
			
			
			/**upperWestPanel**/
				
			upperWestPanel = new JPanel(new BorderLayout());
			//upperWestPanel.setSize(20,50); fix
			labelTempo = new JLabel("Tempo");
			//labelBpm = new JLabel("Bpm");
			fieldTempo = new JTextField("" + mainB.getTempo());
			fieldTempo.setEditable(false);
			fieldTempo.setSize(100, 20);
							
			upperWestPanel.add(labelTempo, BorderLayout.WEST );
			//upperWestPanel.add(labelBpm, BorderLayout.EAST );
			upperWestPanel.add(fieldTempo, BorderLayout.CENTER );
			
			
					
			/**upperEastPanel, located inside the UW panel**/
			
				upperEastPanel = new JPanel(new BorderLayout());
					//upperEastPanel.setSize(20,50); fix
				
				minus = new JButton("-");
				//minus.setSize(10, 10);
				minus.addActionListener(new ButtonListener());			
				
				plus = new JButton("+");
				//plus.setSize(10, 10);
				plus.addActionListener(new ButtonListener());
				
				tapTempo = new JButton("Tap 3x for tempo ");
				//tapTempo.setSize(10, 10);
				tapTimes = new ArrayList<Long>();
				tapCounter = 1;
				tapTempo.addMouseListener(new TapListener());	
				
				upperEastPanel.add(minus, BorderLayout.CENTER );
				upperEastPanel.add(plus, BorderLayout.EAST );
				upperEastPanel.add(tapTempo, BorderLayout.NORTH );
			
			upperWestPanel.add(upperEastPanel, BorderLayout.EAST );
			
			/**middle Panel**/
			middlePanel = new JPanel(new BorderLayout());

			slider = new JSlider(JSlider.HORIZONTAL, 30, 240, 60);
				//slider = new JSlider(JSlider.HORIZONTAL, 10, 1500, 60); //tester w/ extreme discrepancies
			slider.setPaintTicks(true);
			slider.setSnapToTicks(true);
			slider.setPaintLabels(true);
			slider.setMajorTickSpacing(10);
			slider.setMinorTickSpacing(1);
			slider.addChangeListener(new SliderListener());

			middlePanel.add(slider, BorderLayout.CENTER);
			
			
			/**lower Panel**/
			
			lowerPanel = new JPanel(new BorderLayout());
			//lowerPanel.setSize(20,30); fix
			labelTimeSignature = new JLabel("Time Signature");
			
			
			String[] left = new String[19];
			for(int i = 2; i < 21; i++)
			{
				left[i - 2] = i + " notes per measure";
			}
			leftBox = new JComboBox(left);
			leftBox.addActionListener(new LeftBoxListener());
			
			
			String[] right = {"4 - Quarter Note", "8 - Eighth Note"};
			rightBox = new JComboBox(right);
			noteType = 4; //by default, starts on quarter note
			rightBox.addActionListener(new RightBoxListener());
			
			
					
			
			lowerPanel.add(leftBox, BorderLayout.CENTER );
			lowerPanel.add(rightBox, BorderLayout.EAST );
			lowerPanel.add(labelTimeSignature, BorderLayout.WEST );
			/**Panels
			 *UW - tempo textField
			 *UE - minus, plus, tapTempo
			 *Mid- slider 
			 *low- combo boxes
			 **/
			
			add(upperWestPanel, BorderLayout.NORTH);
			//add(upperEastPanel, BorderLayout.EAST);
			add(middlePanel, BorderLayout.CENTER);
			add(lowerPanel, BorderLayout.SOUTH); //add to borderlayout.center
			
			//setLocation(500,500);
			setLocationRelativeTo(mainB);
			
			this.getContentPane().setBackground(Color.CYAN);
			setVisible(true);
			
			//mainB.setTotalBeatNumber(4); not needed, as it is already pre - set in buttonFrame class
			mainB.makeVisible();
			//this method was only made cause it was annoying when both windows popped when I need to only work with one. And to prevent compiler errors.	
		
		}
		
		/**
		 *unused but necessary to implement
		**/
		public void actionPerformed(ActionEvent e) //called whenever ANYTHING is clicked
		{
			
		}
		
	/**
	 *the listener for the - and + buttons. Implements ActionListener and uses a JButton
	**/
	class ButtonListener implements ActionListener
	{
		/**
		 *Gets the source and determines whether the button clicked is - or +
		 *If the current tempo is in range ( >30, <240), it increments or decrements the following tempo.
		 *Then, it sets the slider to that tempo and displays the value on the textfield
		 *@param e the ActionEvent to evaluate
		 **/
		public void actionPerformed(ActionEvent e) //called whenever ANYTHING is clicked
		{
			System.out.println ("entered");
			JButton temp = (JButton)e.getSource();
			
			if(temp.getLabel().equals("-"))
			{
				System.out.println ("-");
					//System.out.println ("previous tempo: " + mainB.getTempo());
				if(mainB.getTempo() > 30)
				{
					mainB.setTempo(mainB.getTempo() - 1);
					slider.setValue(mainB.getTempo());
					fieldTempo.setText(String.valueOf(mainB.getTempo()));
								//mainB.setTempo(10);
						System.out.println ("new tempo: " + mainB.getTempo());
					//fieldTempo.setText("" + (mainB.getTempo() - 1) );
				}
				
				
				
			}
			
			else
			{
				System.out.println ("+");
					//System.out.println ("previous tempo: " + mainB.getTempo());
				if(mainB.getTempo() < 240)
				{
					mainB.setTempo(mainB.getTempo() + 1);
					slider.setValue(mainB.getTempo());
					fieldTempo.setText(String.valueOf(mainB.getTempo()));
						//mainB.setTempo(1500);
						System.out.println ("new tempo: " + mainB.getTempo());
					//fieldTempo.setText("" + (mainB.getTempo() - 1) );
				}
				
			}
		} 
	}
	
	/**
	 *the listener for the slider. Implements ChangeListener and uses a JSlider
	**/	
	class SliderListener implements ChangeListener
	{
		/**
		 *Gets the source and sets the current tempo and the textfield to the value of the slider
		 *@param e the ChangeEvent to evaluate
		 **/
		public void stateChanged(ChangeEvent e)
		{
			JSlider slider = (JSlider) e.getSource();
			
			System.out.println("Slider changed: " + slider.getValue());
		    mainB.setTempo(slider.getValue());
		    fieldTempo.setText(String.valueOf(slider.getValue()));
						
//			if (slider.getValueIsAdjusting()) //if the slider is in motion
//		    {
//		       System.out.println("Slider changed: " + slider.getValue());
//		       mainB.setTempo(slider.getValue());
//		       fieldTempo.setText(String.valueOf(slider.getValue()));
//		       	       
//		    }
//		    
//		    else //if its not in motion, just use that value
//		    {
//		      System.out.println("Slider stopped: " + slider.getValue());
//		      mainB.setTempo(slider.getValue());
//		      fieldTempo.setText(String.valueOf(slider.getValue()));
//		    }
		}
	}
	
	/**
	 *the listener for the left Combo box. Implements ActionListener and uses a JComboBox
	**/
	class LeftBoxListener implements ActionListener
	{
		/**
		 *Gets the source and determines the number of notes from that JComboBox selection
		 *Then, it sets the total beat number on the ButtonFrame to that number
		 *@param e the ActionEvent to evaluate
		 **/
		public void actionPerformed(ActionEvent e)
		{
			JComboBox temp = (JComboBox)e.getSource();
			String name = (String)temp.getSelectedItem();
			String[] nameSplit = name.split(" ");
			mainB.setTotalBeatNumber(Integer.parseInt(nameSplit[0]));
			
		}
	}
	
	/**
	 *the listener for the right Combo box. Implements ActionListener and uses a JComboBox
	**/
	class RightBoxListener implements ActionListener
	{
		/**
		 *Gets the source and determines the type of note from that JComboBox selection
		 *Given that the number selected is NOT the type of note the tempo is currently set to,
		 *it multiplies or divides the tempo by 2
		 *@param e the ActionEvent to evaluate
		 **/
		public void actionPerformed(ActionEvent e)
		{
			JComboBox temp = (JComboBox)e.getSource();
			String name = (String)temp.getSelectedItem();
			int changeTo = Integer.parseInt(name.substring(0,1));
			if(changeTo == 4 && noteType != 4) //to prevent tempo from going out of range
			{
				mainB.setTempo(mainB.getTempo() / 2);
				noteType = 4;
				System.out.println ("changed tempo: " + mainB.getTempo());
			}
			
			else //nt == 8
			{
				if(noteType != 8)
				{
					mainB.setTempo(mainB.getTempo() * 2);
					noteType = 8;
					System.out.println ("changed tempo: " + mainB.getTempo());
				}
			}
			
			//mainB.setTotalBeatNumber(name.substring(0,1));
		}
	}
	
	/**
	 *the listener for the tap fucntion. Implements MouseListener and uses Java's time function.
	**/
	class TapListener implements MouseListener
	{
		/**
		 *Using Java's system time function, the tap function will record three times at which 
		 *the mouse was pressed. Then, it will average the difference in time between the first and last click
		 *to determine the average time between beats 
		 *@param event the MouseEvent to evaluate
		 **/
		public void mousePressed(MouseEvent event)
		{
			if(tapCounter == 3)
			{
				long last = System.currentTimeMillis(); 
				tapTimes.add(last);
				long total = tapTimes.get(tapTimes.size() - 1) - tapTimes.get(0);
				long divided = total / (tapTimes.size() - 1);
				long calculatedTempo = 60000 / divided;
				
				fieldTempo.setText(String.valueOf(calculatedTempo));
				slider.setValue((int)calculatedTempo);
				
									
				//resets, does NOT add current time
				
				tapTimes.clear();
				tapCounter = 1;
					
				
			}
			
			else
			{
				long first = System.currentTimeMillis(); 
				tapTimes.add(first);
				tapCounter++;
			}
		}
		
		
		public void mouseClicked(MouseEvent event) {} 
		public void mouseReleased(MouseEvent event) {}
		public void mouseEntered(MouseEvent event) {} 
		public void mouseExited(MouseEvent event) {} 
	}
					
		
}





		
	

	