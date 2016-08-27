/*
 *Bryan Leung
 *Mrs. Gallatin
 *Period 3	
 *ButtonFrame
 *Contains the main button, which turns the metronome on and off and the beat count
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
*Contains the main button for the metronome and the display of the beat count. 
*In addition, it contains methods makeVisible(), getTempo(), setTempo(), getSoundThread(), setTotalBeatNumber(), setCurrentBeatNumber(), and setIfTotalBeatCountChanged()
*which are used to communicate between the two frames using the ButtonFrame object in the ToolFrame class
*In addition, it contains the subclasses MainButtonListener ( the listener for the button) and soundThread ( the Thread object which plays the sound)
**/
	
public class ButtonFrame extends JFrame implements ActionListener  
{
	/*** Listeners Used 
	 *ActionListener - MainButtonListener
	 */
	 
	 /*** GUI Elements Used 
	 *JButton, JTextField
	***/
		private JPanel panEl;
		private JPanel upper_panel;
		private JLabel label;
		private JTextField field;
		private JButton button;
		
		private int totalBeatNum;
		private int currentBeatNum;
		private boolean ifTotalBeatCountChanged;

			
		private soundThread threadSound;
		private int tempo;
		//private boolean toContinue;
		private boolean isAlive;
		
		private int isFirst;
		
		
		/**
		 *Contains the main button for the metronome and the display of the beat count. 
		 *In addition, it creates 2 panels upperPanel and panEl
		 **/		
		public ButtonFrame()
		{
			//add tempo in the frame?
			tempo = 60; //starting value of tempo 
			isAlive = false;
			isFirst = 0;
			//mainButtonPressed = true;
						
			totalBeatNum = 2; //default value
			currentBeatNum = 1; //default value
			ifTotalBeatCountChanged = false;
			
			setSize(192,110); //300,300
			setTitle("Main Button");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
			this.setLayout(new FlowLayout());
			
	
			/**Upper Panel**/
			upper_panel = new JPanel(new BorderLayout());
			upper_panel.setSize(20,50);
			label = new JLabel("Beat Count");
			field = new JTextField("1");
			field.setEditable(false);
			field.setSize(1,1);
			
			upper_panel.add(label, BorderLayout.NORTH );
			upper_panel.add(field, BorderLayout.CENTER );
			
			
			/**lower Panel**/
			panEl = new JPanel(new FlowLayout());
			panEl.setSize(20,30);
			panEl.setBackground(Color.CYAN);
			
			//button
			button = new JButton("Play");
			button.setSize(100, 100);
			button.addActionListener(new MainButtonListener());
			//button = new ButtonComponent();
			
			//adding stuff to the panels
			panEl.add(button);
			add(upper_panel);
			add(panEl); //add to borderlayout.center
			
			this.getContentPane().setBackground(Color.CYAN);
			setLocation(540,190);
							

		}
		
		//this method was only made cause it was annoying when both windows popped when I need to only work with one. And to prevent compiler errors.
		/**
		 *sets the window visible
		 **/
		public void makeVisible()
		{
			setVisible(true);
		}
		
		/**
		 *Gets the tempo
		 *@return the tempo
		 **/
		public int getTempo()
		{
			return tempo;
		}
		
		/**
		 *Sets the tempo to the given int
		 *@param n the integer to which the tempo will be set to
		 **/
		public void setTempo(int n)
		{
			tempo = n;
		}
		
		/**
		 *Gets the current soundThread
		 *@return the soundThread
		 **/
		public soundThread getSoundThread()
		{
			return threadSound;
		}
		
		/**
		 *Sets the totalBeatNumber to the given int
		 *@param n the integer to which the totalBeatNumber will be set to
		 **/
		public void setTotalBeatNumber(int n)
		{
			totalBeatNum = n;
		}
		
		/**
		 *Sets the current beat number to the given int
		 *@param n the integer to which the current beat number will be set to
		 **/
		public void setCurrentBeatNumber(int n)
		{
			currentBeatNum = n;
			ifTotalBeatCountChanged = true;
		}
		
		/**
		 *Sets if the total beat count changed
		 **/
		public void setIfTotalBeatCountChanged()
		{
			ifTotalBeatCountChanged = true; //CHANGE
		}
		
		/**
		 *unused
		 **/
		public void actionPerformed(ActionEvent e) //called whenever ANYTHING is clicked
		{
			
		}
	/**
	 *the listener for the button fucntion. Implements ActionListener
	**/
	class MainButtonListener implements ActionListener
	{
		/**
		 *Plays the threadSound according to whether the button is pressed or not
		 *if the button is not on, we create a new thread and start the thread
		 *if the button is on, we set toContinue to false to effectively exit the thread and then set isAlive to false so that the process can repeat
		 *@param e the ActionEvent being read from 
		 **/
		public void actionPerformed(ActionEvent e)
		{
		
		if(!isAlive)
		{
			threadSound = new soundThread("main_button");
				
			System.out.println ("0 currently off");
			threadSound.setToContinue(true);
			threadSound.start();
			System.out.println ("0 now on");
			button.setText("Stop");
			panEl.setBackground(Color.RED);
			getContentPane().setBackground(Color.RED);
			isAlive = true;
			
		}			
					
		else 
		{
			System.out.println ("currently on");
			threadSound.setToContinue(false);
//			try
//			{
//				Thread.sleep(2000);
//			}
//			catch (Exception en){};
			button.setText("Play");
			panEl.setBackground(Color.CYAN);
			getContentPane().setBackground(Color.CYAN);
			
			isAlive = false;
			System.out.println ("now off");
	    }
			
		
			
		} 
	}
		
	/**
	 *the Thread for the sound fucntion. Extends Thread and implements Runnable .
	**/
	class soundThread extends Thread implements Runnable
	{
		public final int FILE_TIME = 42; //time it takes for the sound to play out = 30 milliseconds
		private String name;
		private Sound sound;
		private boolean toContinue;
		
		/**
		 *creates a new soundThread, which takes a string parameter for its name. In addition, the soundThread class contains a Sound object, which itself takes a string for the filename to read from.
		 *@param n the String of the new soundThread
		 **/
		public soundThread(String n)
		{
			name = n;
			sound = new Sound("sound4.wav");
		}
		
		/**
		 *the overrided default method that the soundThread calls whenever start() is called
		 *while the toContinue flag remains true, run() will do 4 things: 1) Play the sound by calling the playSound() of the Sound object 2) get tempo through the getTempo() method 
		 *3) determine and set the beat count. The beat count should start at 1 and then increment until it reaches the totalBeat count. If the beat changed, we check if the current beat we are at is > or < than the beat we want to change to. 
		 *If the beat we want to change to is > current beat, we continue going but stop at the new totalBeat. If the beat we want to change to is < current beat, we go back to beat 1 and then keep going until we reach the new totalBeat.			
		 *4) determine sleep time and then tell the Thread to sleep for that amoumnt of time
		 **/
		public void run()
		{
			//will loop inside the while loop
			System.out.println ("begin");
			System.out.println ("name: " + name);
						
			while(toContinue)
			{
				//check if there has been changes to tempo. If there has been, adjust
				int n = getTempo();
				System.out.println ("tempo: " + n);
				
				System.out.println ("sleep time: " + getSleepTime());
				
				try
				{
					sound.playSound();
						if(currentBeatNum < totalBeatNum)
						{
							if(ifTotalBeatCountChanged) //if the beatCount changed
							{
								//check if totalBeatNum < currentBeatNum or not
								if(totalBeatNum < currentBeatNum)
								{
									currentBeatNum = 1;
									field.setText(String.valueOf(currentBeatNum));
									//ifTotalBeatCountChanged = false;
								}
								
								//check if totalBeatNum > currentBeatNum, so continue with new totalBeatNum
								else
								{
									currentBeatNum++;
									field.setText(String.valueOf(currentBeatNum));
								}
							}
							else //increment
							{
								currentBeatNum++;
								field.setText(String.valueOf(currentBeatNum));
								//ifTotalBeatCountChanged = false;
							}
							
							ifTotalBeatCountChanged = false;
						}
						
						else
						{
							currentBeatNum = 1;
							field.setText(String.valueOf(currentBeatNum));
						}
						
					
					Thread.sleep(getSleepTime());
								
				}
				
				catch (Exception e)
				{
//					e.printStackTrace();
//					
//					System.out.println (e.getCause());
				};
				
				
			}
			System.out.println ("stopped");
			return;
						
			
			
		}
		
		/**
		 *For every tempo, we allocate [60000 / (tempo)] milliseconds for every beat. Since it takes a set amount of time for the .wav file to play, the Thread must wait a small amount of time before it can play the .wav file again.
		 *Thus, we get the equation:  Total time allocated = Time to play wav file + sleep time. sleep time = Total time allocated - Time to play wav file
		 *@return the time the THread should sleep
		 **/
		public int getSleepTime() //public double inBetweenTime
		{
			//1. get the tempo (somehow)
			//2. do 60/tempo to get total time alloted given tempo
			//3. return (60/tempo) - FILE_TIME
			
//			int total = 60000 / getTempo();
//			return total - FILE_TIME;
			
			return ((int)60000 / getTempo()) - FILE_TIME;
		}
		
		/**
		 *sets toContinue to the given flag 
		 *@param b the flag that toContinue will be set to
		 **/
		public void setToContinue(boolean b)
		{
			toContinue = b;
		}
		

		/**
		 *determines whether toContinue is true or not
		 *@return whether toContinue is true or not
		 **/
		public boolean getToContinue()
		{
			return toContinue;
		}
		

		
	}
					
		
}





		
	

	