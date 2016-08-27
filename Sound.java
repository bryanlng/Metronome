/*
 *Bryan Leung
 *Mrs. Gallatin
 *Period 3	
 *Sound
 *plays the Sound
*/
import java.io.File;
import java.io.*;


import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


/**
 *Using AudioInputStream, AudioFormat, and SourceDataLine, the Sound class loads in data from an
 *audio file and plays the content through the SourceDataLine
*/
public class Sound
{
    /*** Citations
     *Audio – Stack Overflow:
		http://stackoverflow.com/questions/2416935/how-to-play-wav-files-with-java
		users: m13r, greenLizard, 
	 ***/
    
    private final int BUFFER_SIZE = 128000;

    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;
    private String strFilename;

    private boolean loadedFile;
    private int fileSize = 0;
    private byte[] abData = new byte[BUFFER_SIZE];

   /**
    *creates a Sound object which takes a String for the filename
    *@param filename the name of the file to be read
    **/	
    public Sound(String filename)
    {
        strFilename = filename;
		loadedFile = false;

    }

   /**
     *loads the file ONCE by calling loadFileToBuffer(), then calls playMusic()		 
     **/
    public void playSound()
    {
		//in this way, the File will be loaded to the SourceLine only ONCE instead of every time the thread is called
		//this will reduce lag in the metronome
		
		//if the file is not loaded, call loadFileToBuffer
        if (!loadedFile)
        {
            loadFileToBuffer();
            loadedFile = true;

        }

        playMusic();

        //sees if the filepath exists

    }

   /**
     *Precondition: byte array abData is already contains the data				
     *Opens the AudioFormat and then begins writing and playing the stream from the file
     *Afterwards, it drains and closes the sourceLine				
     **/	
    private void playMusic()
    {
        //given the data, the sourceLine will begin writing and playing the data
        try
        {
	        sourceLine.open(audioFormat);
	        sourceLine.start();
	        sourceLine.write(abData, 0, fileSize);
	        sourceLine.drain();
	        sourceLine.close();
    	}
    	
    	catch (Exception e) 
    	{
    		
    	}
    	

    }

    /**
     *First begins by loading the soundFile. After getting the audiostream from the file and the format from that audiostream, 				
     *	A DataLine.Info class is created (since DataLine is sourceLine's superclass) with the audioFormat as a parameter.				
     *	Then, the DataLine.Info class is cast into a SourceLine class.
     *	Afterwards, data will be read from the audioStream into the SourceLine		
     **/
    private void loadFileToBuffer()
    {
		//loads the soundFile
        try
        {
            soundFile = new File(strFilename);
        }

        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }


        // obtains an audio input stream from the file
        try
        {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        }

        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        /*
         *getFormat() obtains the audio format of the sound data inside the audio input stream
         */
        audioFormat = audioStream.getFormat();

        /*
         *Line:  element of the digital audio "pipeline," such as a an input port
         *DataLine: Interface that adds media-related functionality to its superinterface, Line.
         *  Functionality includes transport-control methods that start, stop, drain, and flush
         *  the audio data that passes through the line
         *
         *Line.Info: Contains information about the given Line
         *DataLine.Info: Provides additional information about the DataLine
         *  such as: the audio formats supported by the data line
         *  Constructor:
         *      DataLine.Info(Class<?> lineClass, AudioFormat format)
         *
         *SourceDataLine: A DataLine to which data may be written to
         *  open(): opens the current AudioFormat Line
         *  start(): starts the Line. Inherited from DataLine
         *  write(): Writes audio data to the mixer through the SourceDataLine
         *      The requested number of bytes of data are read from the specified array,
         *      starting at the given offset into the array, and written to the data line's buffe
         *
         *AudioSystem: the ENTRY POINT to the audio classes (the highest one)
         *  getLine(): gets the current Line from DataLine.Info info
         *
         *AudioInputStream:  an input stream with a specified audio format and length.
         *  The length is expressed in sample frames, NOT bytes.
         *
         *  read(): Reads up to a specified maximum number of bytes of data from the audio stream,
         *          THEN puts them into the given byte array.
         *          Returns the total # of bytes read into the buffer
         *
         */
        
        //first makes a DataLine.Info class since DataLine is sourceLine's superclass
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try
        {
            //then creates a sourceLine from the DataLine.Info class 
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        }

        catch (LineUnavailableException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        //start(): starts the Line. Inherited from DataLine
        sourceLine.start();

      
        //Reads the data from the AudioInputStream and writes it into the byte array
        //data from the byte array will be read into the SourceLine in the playMusic() method
       
        int nBytesRead = 0;
	        try
            {
                nBytesRead = audioStream.read(abData, 0, abData.length);
                //System.out.println("Read " + nBytesRead + " bytes");
                fileSize = nBytesRead;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            /*
            if (nBytesRead >= 0)
            {
                @SuppressWarnings("unused")
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
            */
       

    }
}



