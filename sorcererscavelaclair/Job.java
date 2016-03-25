package sorcererscavelaclair;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

/*CMSC 335
 * Project 3 - Sorcerer's Cave Version 3.0
 * Matthew LaClair
 * This program is designed to read an input file
 * into separate array lists and provide the user
 * the ability to view the contents as a JTree. The
 * user also has the ability to search for a specific
 * item and sort by different variables. In this version
 * the user has the option to run jobs assigned to
 * specific creatures and view their respective progress
 * on JProgress bars.
 */

public class Job extends CaveElement implements Runnable {
    String name;
    int index, jobTime, party, creature, wand, stone, scroll, potion, sword, armour;
    Creature c;
	JButton button,cancel;
	JProgressBar progress;
	JTextArea statusText;
	JLabel nameLabel;
	Boolean pauseThreadFlag = false, terminateFlag = false, doneFlag = false, busyFlag = false, availableFlag = false, doableFlag = false;
    Status status = Status.PAUSED;
    enum Status {RUNNING, WAITING, RESUMING, PAUSED, IMPOSSIBLE, CANCELLED, DONE};
    Thread t;
    ArrayList <String> itemList = new ArrayList <String> ();
    ArrayList <Integer> partyArtifacts = new ArrayList <Integer>();
    ArrayList <Job> jobs = new ArrayList <Job>();
    ArrayList <String> missing = new ArrayList <String>();
    private static ArrayList <Thread> threads = new ArrayList <Thread>();
    
    /*Previous edition stuff
    boolean completable;
    ArrayList <JButton> pauseButtons = new ArrayList <JButton>();
    ArrayList <JButton> resumeButtons = new ArrayList <JButton>();
    ArrayList <JButton> cancelButtons = new ArrayList <JButton>();
    ArrayList <JTextArea> textAreas = new ArrayList <JTextArea>();
    ArrayList <JProgressBar> progressBars = new ArrayList <JProgressBar>();
    */
    
    
    //Job constructor
    public Job (int tempIndex, String tempName, int tempCreatureIndex, double tempTime, ArrayList <String> tempList, Creature c) {
    	
    	//Job attributes
    	this.index = tempIndex;
    	this.name = tempName;
    	this.creature = tempCreatureIndex;
    	this.jobTime = (int)tempTime;
    	this.itemList = tempList;
    	this.c = c;

    	//Sort out required items
    	for(int i = 0; i < tempList.size(); i++){
    		if(tempList.get(i).equals("Wand"))
    			this.wand = Integer.parseInt(tempList.get(i+1));
    		if(tempList.get(i).equals("Potion"))
    			this.potion = Integer.parseInt(tempList.get(i+1));
    		if(tempList.get(i).equals("Stone"))
    			this.stone = Integer.parseInt(tempList.get(i+1));
    		if(tempList.get(i).equals("Scroll"))
    			this.scroll = Integer.parseInt(tempList.get(i+1));
    		if(tempList.get(i).equals("Sword"))
    			this.sword = Integer.parseInt(tempList.get(i+1));
    		if(tempList.get(i).equals("Armour"))
    			this.armour = Integer.parseInt(tempList.get(i+1));
    	}

    	//Create thread
		this.t = new Thread(this, this.name);
		threads.add(this.t);
		
		//Create name label
		this.nameLabel = new JLabel();
		this.nameLabel.setText("   " + c.name + "   ");
		this.nameLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		
		
		//Create status text area
		this.statusText = new JTextArea();
		this.statusText.setLineWrap(true);
		this.statusText.setText(" is ready to start: " +  name);
		this.statusText.setBorder(BorderFactory.createRaisedBevelBorder());
		
		//Create progress bar
		this.progress = new JProgressBar(0,10);
		this.progress.setValue(0);
		this.progress.setStringPainted(true);
		
		//Create pause/resume button
		this.button = new JButton("Pause Job");
		this.button.setBackground(Color.YELLOW);
		//Add button actionlistener
		this.button.addActionListener ( new ActionListener () {
             public void actionPerformed (ActionEvent e) {
                    toggleThread ();
              	}
          	}
 		);
		
		//Create cancel button
		this.cancel = new JButton("Cancel Job");
		this.cancel.setBackground(Color.RED);
		
	    //Add cancel actionlistener
	  	this.cancel.addActionListener ( new ActionListener () {
	         public void actionPerformed (ActionEvent e) {
	                cancelThread ();
	             }
	  		}
	    );
    	
    	/*Previous edition stuff
    	this.stones = tempStones;
    	this.potions = tempPotions;
    	this.wands = tempWands;
    	this.weapons = tempWeapons;
    	this.completable = tempCompletable;
    	*/
	  	
    }//End job constructor
    

    //Blank constructor to access methods
    public Job(){		
    }
    
    /*Previous edition stuff
    //Constructor for runnable
    public Job(ArrayList<Job> doableJobs, ArrayList<Thread> threads2,ArrayList<JProgressBar> progressBars2,
			ArrayList<JButton> pauseButtons2, ArrayList<JButton> resumeButtons2, ArrayList<JButton> cancelButtons2) {
    }
    */

	//Create job GUI/Threads for jobs
    public void startJobsGUI (Cave cave) {
    	
    	//Retrieve all jobs from cave
    	 jobs = cave.getJobs();
    	
    	/*Previous edition stuff
    	ArrayList <Job> doableJobs = cave.getJobs();
    	int [] cIndex = new int [doableJobs.size()];
    	String [] cName = new String [doableJobs.size()];
    	
    	//Get creature index for each doable job
    	for (int i = 0; i < doableJobs.size(); i++){
    		cIndex[i] = doableJobs.get(i).creature;
    	}
    	
    	//Match creature index with creature name
    	for (int i = 0; i < doableJobs.size(); i++){
    		cName[i] = cave.getName(cIndex[i]);
    	}
    	*/
    		
    	//Create GUI
		JFrame frame = new JFrame();
		frame.setTitle ("Sorcerer's Cave Jobs Panel");
	    frame.setSize (1000, 700);
	    frame.setLocationRelativeTo(null);
	    frame.setLayout(new BorderLayout());
	    
	    //Create start panel/start button
	    JPanel startPanel = new JPanel();
	    startPanel.setBorder(BorderFactory.createRaisedBevelBorder());
	    JLabel startLabel = new JLabel ("Start jobs for all creatures: ");
	    JButton startButton = new JButton ("Start Jobs");
	    startButton.setBackground(Color.GREEN); 
	    
	    //Create available resource panel
	    JPanel resourcePanel = new JPanel();
	    resourcePanel.setBorder(BorderFactory.createRaisedBevelBorder());
	    JTextArea resourceText = new JTextArea();
	    resourceText.append(cave.caveArtifacts());
	    resourcePanel.add(resourceText);
	    
	    //Create name panel
	    JPanel namePanel = new JPanel ();
	    namePanel.setLayout(new GridLayout(0,1));
	    namePanel.setBorder(BorderFactory.createRaisedBevelBorder());
	    
	    //Create status panel
	    JPanel statusPanel = new JPanel ();
	    statusPanel.setLayout(new GridLayout(0,2));
	    statusPanel.setBorder(BorderFactory.createRaisedBevelBorder());
	    
	    //Create button panel
	    JPanel buttonPanel = new JPanel ();
	    buttonPanel.setLayout(new GridLayout(0,2));
	    buttonPanel.setBorder(BorderFactory.createRaisedBevelBorder());
	    
	    //Dynamically create/add status area/progress bars/buttons
  		for (int i = 0;i < jobs.size(); i++){
			
  			//Add labels to panels
  			namePanel.add(jobs.get(i).nameLabel);
  			statusPanel.add(jobs.get(i).statusText);
  			statusPanel.add(jobs.get(i).progress);
  			buttonPanel.add(jobs.get(i).button);
  			buttonPanel.add(jobs.get(i).cancel);
  			
  			/*Previous edition stuff
  			//Create status text
  			//statusText.append("\n\n\n is ready to start: " +  jobs.get(i).name);
  			
  			//textAreas.add(statusText);
  			
  			//Create progress bar
  			//progressBars.add(progress);
  			
  			//Pause buttons
  			//pauseButtons.add(pause);
  			//Add pause actionlistener
  		
  			//Resume buttons
  			JButton resume = new JButton("Resume Job");
  			resume.setBackground(Color.GREEN);
  			buttonPanel.add(resume);
  			//resumeButtons.add(resume);
  	  		//Add resume actionlistener 
  	  		resume.addActionListener ( new ActionListener () {
  	               public void actionPerformed (ActionEvent e) {
  	                   resumeThread ();
  	                   showStatus(Status.RESUMING);
  	               	}
  	           	}
  	  		);
  	  		*/
  			
  		}//End button/status/progress bar creation
	    
	    //Start button listener
	    startButton.addActionListener ( new ActionListener () {
	        public void actionPerformed (ActionEvent e) {
	        	startButton.setText("Jobs Started");
	        	for(int i = 0; i < threads.size(); i++){
	        		threads.get(i).start();
	        		//Previous edition stuff
	        		/*RunJob rj = new RunJob(doableJobs.get(i).name, i, doableJobs.get(i).jobTime, progressBars.get(i),
	        				pauseButtons.get(i), cancelButtons.get(i),resumeButtons.get(i), textAreas.get(i));
	        		textAreas.get(i).setText("\n\n\n has started job: " + doableJobs.get(i).name);
	        		threads.add(rj.t);
	        		*/
	        		}	
	        	}
	    	}
	     );//End start button listener
	    
	    //Add start label & button to panel
	    startPanel.add(startLabel);
	    startPanel.add(startButton);
	   	    
		//Add panels to GUI
		frame.add(startPanel, BorderLayout.PAGE_START) ;
		frame.add(resourcePanel, BorderLayout.PAGE_END);
		frame.add(namePanel, BorderLayout.LINE_START);
	    frame.add(statusPanel, BorderLayout.CENTER);
	    frame.add(buttonPanel, BorderLayout.LINE_END);
	    
	    //Display GUI
	    frame.setVisible(true);
	    frame.validate();
    }//End GUI creation
		
	//Change status text button text depending on situation
	public void showStatus (Status st) {
	    status = st;
	    switch (status) {
	      case RUNNING:
	    	c.busyFlag = true;
	        statusText.setText(" has acquired " + this.itemList + " and is doing job " + t.getName());
	        statusText.setBackground(Color.GREEN);
	        break;
	      case WAITING:
	    	c.busyFlag = false;
	    	statusText.setWrapStyleWord(true);
	        statusText.setBackground(Color.ORANGE);
	    	statusText.setText(" is waiting for " + this.missing);
	        break;
	      case RESUMING:
	    	  if (terminateFlag || doneFlag)
	    		  break;
	        button.setBackground (Color.yellow);
	        button.setText ("Pause Job");
	        statusText.setText(" is resuming " + t.getName());
	        statusText.setBackground(Color.GREEN);
	        c.busyFlag = true;
	        break;
	      case PAUSED:
	    	  if (terminateFlag || doneFlag)
	    		  break;
	        button.setBackground (Color.orange);
	        button.setText ("Resume");
	        statusText.setText(" is having a beer......");
	        statusText.setBackground(Color.YELLOW);
	        break;
	      case IMPOSSIBLE:
	        button.setBackground (Color.BLUE);
	        button.setText ("Nope!");
	        cancel.setBackground (Color.BLUE);
	        cancel.setText ("Nope!");
	        statusText.setText("Party is missing required artifacts");
	        statusText.setBackground(Color.BLUE);
	        break;
	      case CANCELLED:
	    	if (doneFlag)
	    		break;
	    	statusText.setText(" " + t.getName() + " has been cancelled!");
	    	statusText.setBackground(Color.gray);
	        button.setBackground (Color.gray);
	        button.setText ("Job Cancelled");
	        //resume.setBackground (Color.gray);
	        cancel.setBackground (Color.gray);
	        cancel.setText ("Job Cancelled");
	    	break;
	      case DONE:
	    	statusText.setText(" " + t.getName() + " has been completed!");
	    	statusText.setBackground(Color.gray);
	        button.setBackground (Color.gray);
	        button.setText ("Job Done");
	        //resume.setBackground (Color.gray);
	        cancel.setBackground (Color.gray);
	        cancel.setText ("Job Done");
	        break;
	    }
	}//End showStatus method
	
	//Cancels current thread
	public void cancelThread() {
		showStatus(Status.CANCELLED);
		terminateFlag = true;	
	}

	//Pauses/resumes current thread
	public void toggleThread(){
		if(pauseThreadFlag){
			pauseThreadFlag = false;
			showStatus(Status.RESUMING);
		}else{
			pauseThreadFlag = true;
			showStatus(Status.PAUSED);
		}
	}
	
	//Check if required items are available
	public void checkAvailable(Job job){
	    this.availableFlag = c.partyClass.checkIfAvailable(job);
	}

	
	/*Previous edition stuff
	//Resumes current thread
	public void resumeThread() {
		synchronized(t){
            pauseThreadFlag = false;
            t.notify();
        }
    }*/
	
	//Thread run method
	public void run() {
		long time = System.currentTimeMillis();
		long startTime = time;
		long stopTime = time + 1000 * jobTime;
		double duration = stopTime - time;
		
		//Check if job is possible...
		if(!this.doableFlag)
			showStatus(Status.IMPOSSIBLE);
		else{
		    synchronized (c.partyClass) {
		    	
				//See if creature has artifacts to start working
				checkAvailable(this);
				
				//If creature is busy or does not have necessary items
				//continue to check
			    while (c.busyFlag || !availableFlag) {
			    	showStatus (Status.WAITING);
			    	this.checkAvailable(this);
			    	try {
			    		c.partyClass.wait();
			    		this.checkAvailable(this);
			    		showStatus (Status.WAITING);
			    	}
			    	catch (InterruptedException e) {
			    	} // end try/catch block
			    } // end while waiting for worker to be free
			    c.busyFlag = true;
		     } // end sychronized on worker

		    while (time < stopTime && !terminateFlag && !doneFlag && availableFlag) {
		    	try {
		    		Thread.sleep (100);
		    	} catch (InterruptedException e) {}
		    	if (!doneFlag && !c.busyFlag && !pauseThreadFlag && !terminateFlag) {
		    		showStatus (Status.RUNNING);
		    		c.busyFlag= true;
		    		time += 100;
		    		progress.setValue ((int)(((time - startTime) / duration) * 20));
		    		if(progress.getValue() == 10){
		    			doneFlag = true;
		    		}
		    	}
		    	if (pauseThreadFlag)
		    		showStatus (Status.PAUSED);
		    	if (terminateFlag){
		    		synchronized (c.partyClass) {
	    				c.busyFlag = false;
	    				c.partyClass.checkInItems(this);
	    				c.partyClass.notifyAll ();
	    				showStatus (Status.CANCELLED);
		    		}
		    	}
		    	c.busyFlag = false;
		    }
		    if(!terminateFlag){
		    	synchronized (c.partyClass) {
		    		c.busyFlag = false; 
		    		c.partyClass.checkInItems(this);
		    		c.partyClass.notifyAll ();
		    		showStatus (Status.DONE);
		    	}
		    }
		    else{
		    	showStatus (Status.CANCELLED);
		    }
		}//End else loop
	}//End run method
}//End Job class
		
		
/*Previous edition stuff
		//Run method for threads
		public void run() {
		    long time = System.currentTimeMillis();
		    long startTime = time;
		    long stopTime = time + 1000 * jobTime;
		    double duration = stopTime - time;
		    showStatus(Status.WAITING);
		    synchronized(c.partyClass){
		    	checkAvailable(this);
				while (this.busyFlag) {
					try {
						c.partyClass.wait();
						showStatus(Status.WAITING);
					}
					catch (InterruptedException e) {
					} // end try/catch block
				} // end while waiting for worker to be free
				busyFlag = true;
		    }//End creature sync
		
	    	while(time < stopTime && !this.terminateFlag){
	    		if (!this.terminateFlag && !this.pauseThreadFlag & this.availableFlag) {
		        try {
		        	showStatus(Status.RUNNING);
		        	Thread.sleep (100);//slow thread down
		        } catch (InterruptedException e) {}
		          time += 100;
		          progress.setValue ((int)(((time - startTime) / duration) * 10));
		       }
		       if(!this.availableFlag)
	    			showStatus(Status.WAITING);
		       if(this.pauseThreadFlag && this.availableFlag)
	    			showStatus(Status.PAUSED);
		       if (this.terminateFlag){
		    	   synchronized (c.partyClass){
		    		   c.partyClass.notifyAll();
		    		   this.busyFlag = false;
				       c.partyClass.checkInItems(this);
		    	   }
		       }
	    	}
	    	if(this.terminateFlag){
	    		showStatus(Status.CANCELLED);
	    	}
	    	else{
	    	showStatus(Status.DONE);
	    	progress.setValue (100);
	        synchronized (c.partyClass) {
	            c.busy = false; 
	            c.partyClass.notifyAll ();
		    	c.partyClass.checkInItems(this);
		    	}
	       }
	 }
}//End GUI creation/Job Class




/*Previous edition stuff
//RunJob class begins
class RunJob implements Runnable{
	String threadName;
	int index, jobTime;
	Thread t;
	JButton pause,cancel,resume;
	JProgressBar pb;
	JTextArea statusText;
	Boolean pauseThreadFlag = false, terminate = false, done = false;
    Status status = Status.PAUSED;
    enum Status {RESUMING, PAUSED, CANCELLED, DONE};
	
    //RunJob constructor passed information from GUI construction
	public RunJob (String name, int index,int jobTime, JProgressBar pb, JButton pause, JButton cancel,
			JButton resume, JTextArea textArea){
		this.threadName = name;
		this.index = index;
		this.jobTime = jobTime;
		this.pb = pb;
		this.pause = pause;
		this.cancel = cancel;
		this.resume = resume;
		this.statusText = textArea;
		this.t = new Thread(this, threadName);
		t.start();
		
		//Add pause actionlistener
		pause.addActionListener ( new ActionListener () {
             public void actionPerformed (ActionEvent e) {
                 pauseThread ();
                 showStatus(Status.PAUSED);
             	}
         	}
		);
		
		//Add cancel actionlistener
		cancel.addActionListener ( new ActionListener () {
             public void actionPerformed (ActionEvent e) {
                 cancelThread ();
                 showStatus(Status.CANCELLED);
             	}
         	}
		);
		//Add resume actionlistener 
		resume.addActionListener ( new ActionListener () {
             public void actionPerformed (ActionEvent e) {
                 resumeThread ();
                 showStatus(Status.RESUMING);
             	}
         	}
		);
	}//End runjob constructor
	
	//Events for buttons
	public void actionPerformed(ActionEvent e) {
		String str = e.getActionCommand();
		switch (str){
		case "Pause Job":
					pauseThread();
					
		case "Resume Job":
					resumeThread();
			
		case "Cancel Job":
					cancelThread();
				}
	}//End method
	
	//Change status text button text depending on situation
	void showStatus (Status st) {
	    status = st;
	    switch (status) {
	      case RESUMING:
	    	  if (terminate || done)
	    		  break;
	        pause.setBackground (Color.yellow);
	        pause.setText ("Pause Job");
	        statusText.setText("\n\n\n is resuming " + threadName);
	        break;
	      case PAUSED:
	    	  if (terminate || done)
	    		  break;
	        pause.setBackground (Color.orange);
	        pause.setText ("Job Paused....");
	        statusText.setText("\n\n\n is having a beer......");
	        break;
	      case CANCELLED:
	    	  if (done)
	    		  break;
	    	statusText.setText("\n\n\n" + threadName + " Job has been cancelled!");
	        pause.setBackground (Color.gray);
	        resume.setBackground (Color.gray);
	        cancel.setBackground (Color.gray);
	    	break;
	      case DONE:
	    	done = true;
	    	statusText.setText("\n\n\n" + threadName + " has been completed!");
	        pause.setBackground (Color.gray);
	        resume.setBackground (Color.gray);
	        cancel.setBackground (Color.gray);
	        break;
	    }
	  }//End showStatus method
	
	//Cancels current thread
	public void cancelThread() {
		terminate = true;	
	}

	//Pauses current thread
	public void pauseThread(){
		pauseThreadFlag = true;
	}
	
	//Resumes current thread
	public void resumeThread() {
		synchronized(t){
            pauseThreadFlag = false;
            t.notify();
        }
    }

	//Run method for threads
	public void run() {
	    long time = System.currentTimeMillis();
	    long startTime = time;
	    long stopTime = time + 1000 * jobTime;
	    double duration = stopTime - time;
    	while(time < stopTime && !terminate){	
	        try {
	        	 Thread.sleep (1000);//slow thread down
	        } catch (InterruptedException e) {}
	       if (!terminate && !pauseThreadFlag) {
	          time += 100;
	          pb.setValue ((int)(((time - startTime) / duration) * 10));
	       }
	       if (terminate){
	    	   synchronized (t){
	    		   t.notifyAll();
	    		}
	       	}
    	}
    	if(terminate){
    		showStatus(Status.CANCELLED);
    	}
    	else{
    	showStatus(Status.DONE);
    	pb.setValue (100);
    	synchronized (t) {
    		t.notifyAll ();
    		}
    	}
	}//End run method
}//End runjob class
*/
