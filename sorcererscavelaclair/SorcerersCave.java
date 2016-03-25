package sorcererscavelaclair;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/*CMSC 335
 * Project 4 - Sorcerer's Cave Version 4.0
 * Matthew LaClair
 * This program is designed to read an input file
 * into separate array lists and provide the user
 * the ability to view the contents as a JTree. The
 * user also has the ability to search for a specific
 * item and sort by different variables. The user has the 
 * option to run jobs assigned to specific creatures 
 * and view their respective progress on JProgress bars.
 * In this version creatures will share resources between
 * their entire party to complete jobs.
 */


public class SorcerersCave extends JFrame {
	private static final long serialVersionUID = 12345;
	JTextArea textArea = new JTextArea ();
    JComboBox <String> comboBox;
    JTextField textField; 
    Cave cave = new Cave ();
    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Cave");
    JTree tree = new JTree(rootNode);
    
    public SorcerersCave () throws IOException {   
        //Create tree scroll pane;
        JScrollPane scrollPaneTree = new JScrollPane (tree);
        scrollPaneTree.setMinimumSize(new Dimension(200,300));
        
        //Create text scroll pane;
        JScrollPane scrollPaneText = new JScrollPane (textArea);
        scrollPaneText.setMinimumSize(new Dimension(600,300));
        
        //Create horizontal split Pane
        JSplitPane splitPane = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT, scrollPaneTree, scrollPaneText);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(.5);
        add (splitPane, BorderLayout.CENTER);
        
        //Create buttons
        JButton browseButton = new JButton ("Browse");
        JButton displayButton = new JButton ("Display");
        JButton searchButton = new JButton ("Search");
        JButton jobsButton = new JButton ("Jobs");
        JButton sortCreaturesButton = new JButton ("Sort Creatures");
        JButton sortTreasureButton = new JButton ("Sort Treasures");
        JButton sortArtifactsButton = new JButton ("Sort Artifacts");
        
        //Create labels
        JLabel openFileLabel = new JLabel ("Open File:");
        JLabel displayLabel = new JLabel ("Show Tree:");
        JLabel searchLabel = new JLabel ("Search:");
        JLabel jobsLabel = new JLabel ("Jobs Control Panel:");
        JTextField textField = new JTextField (15);
        JLabel sortCreaturesLabel = new JLabel ("Sort Creatures By:");
        JLabel sortTreasuresLabel = new JLabel ("Sort Treasures By:");
        JLabel sortArtifactsLabel = new JLabel ("Sort Artifacts By:");
        
        //Search combobox
        JComboBox <String> searchComboBox = new JComboBox <String> ();
        searchComboBox.addItem ("Index");
        searchComboBox.addItem ("Type");
        searchComboBox.addItem ("Name");
        
        //Sort Creature combobox
        JComboBox <String> sortCreaturesComboBox = new JComboBox <String> ();
        sortCreaturesComboBox.addItem ("Name");
        sortCreaturesComboBox.addItem ("Age");
        sortCreaturesComboBox.addItem ("Height");
        sortCreaturesComboBox.addItem ("Weight");
        sortCreaturesComboBox.addItem ("Empathy");
        sortCreaturesComboBox.addItem ("Fear");
        sortCreaturesComboBox.addItem ("Carry Cap");
        
        //Sort Treasure combobox
        JComboBox <String> sortTreasureComboBox = new JComboBox <String> ();
        sortTreasureComboBox.addItem ("Weight");
        sortTreasureComboBox.addItem ("Value");
        
        //Sort Artifact combobox
        JComboBox <String> sortArtifactsComboBox = new JComboBox <String> ();
        sortArtifactsComboBox.addItem ("Name");
        sortArtifactsComboBox.addItem ("Type");
        
        //Create panel/add buttons to panels
        JPanel panel1 = new JPanel ();
        JPanel panel2 = new JPanel ();
        panel1.add (openFileLabel);
        panel1.add (browseButton);
        panel1.add (displayLabel);
        panel1.add (displayButton);
        panel1.add (searchLabel);
        panel1.add (textField);
        panel1.add (searchComboBox);
        panel1.add (searchButton);
        panel1.add (jobsLabel);
        panel1.add (jobsButton);
        panel2.add (sortCreaturesLabel);
        panel2.add (sortCreaturesComboBox);
        panel2.add (sortCreaturesButton);
        panel2.add (sortTreasuresLabel);
        panel2.add (sortTreasureComboBox);
        panel2.add (sortTreasureButton);
        panel2.add (sortArtifactsLabel);
        panel2.add (sortArtifactsComboBox);
        panel2.add (sortArtifactsButton);
        add (panel1, BorderLayout.PAGE_START);
        add (panel2, BorderLayout.PAGE_END);
        
    	//Create GUI
        setTitle ("Welcome to Sorcerer's Cave");
        setSize (1100, 700);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        validate ();
        
        //Add listeners to buttons
        browseButton.addActionListener ( new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                try {
                    readFile();
                } catch (Exception ex) {
                	JOptionPane.showMessageDialog(null, "Error During File Read", "Error During File Read", JOptionPane.ERROR_MESSAGE);
                	ex.printStackTrace();
                }
            }
        });
        //Display button listener
        displayButton.addActionListener ( new ActionListener () {
                public void actionPerformed (ActionEvent e) {
                    displayInfo ();
                }
            }
        );
        //Search button listener
        searchButton.addActionListener ( new ActionListener () {
                public void actionPerformed (ActionEvent e) {
                    search ((String)(searchComboBox.getSelectedItem()), textField.getText());
                }
            }
        );
        //Jobs button listener
        jobsButton.addActionListener ( new ActionListener () {
                public void actionPerformed (ActionEvent e) {
                   Job job = new Job();
                   job.startJobsGUI(cave);
                }
            }
        );
        //Sort creature button listener
        sortCreaturesButton.addActionListener ( new ActionListener () {
        		public void actionPerformed (ActionEvent e) {
        			sortCreature ((String)(sortCreaturesComboBox.getSelectedItem()));
        		}
        	}
        );
        //Sort treasure button listener
        sortTreasureButton.addActionListener ( new ActionListener () {
            	public void actionPerformed (ActionEvent e) {
            		sortCreature ((String)(sortTreasureComboBox.getSelectedItem()));
            	}
        	}
        );
        //Sort artifacts button listener
        sortArtifactsButton.addActionListener ( new ActionListener () {
            	public void actionPerformed (ActionEvent e) {
            		sortArtifacts ((String)(sortArtifactsComboBox.getSelectedItem()));
            	}
        	}
        );
    }//End GUI constructor
    
    //Read file into program
	public void readFile () throws IOException, Exception {
		HashMap <Integer, Party> hashParty = new HashMap <Integer, Party>();
		HashMap <Integer, Creature> hashCreature = new HashMap <Integer, Creature>();
		ArrayList<String> tokens = new ArrayList<String>();
		DefaultMutableTreeNode pNode = new DefaultMutableTreeNode("Parties");
		DefaultMutableTreeNode cNode = new DefaultMutableTreeNode("Creatures");
		DefaultMutableTreeNode tNode = new DefaultMutableTreeNode("Treasure");
		DefaultMutableTreeNode aNode = new DefaultMutableTreeNode("Artifacts");
		DefaultMutableTreeNode jNode = new DefaultMutableTreeNode("Jobs");
		DefaultMutableTreeNode eNode = new DefaultMutableTreeNode("Extra Stuff");
		rootNode.add(pNode);
		rootNode.add(eNode);
		Party p = null;
		
		/*Previous edition items
		ArrayList<Integer> partyIndex = new ArrayList<Integer>();
		ArrayList<Integer> creatureIndex = new ArrayList<Integer>();
		int a = 0;*/
        
        
        //User chooses file to input
        JFileChooser chooser = new JFileChooser(".");
        int selectedVal = chooser.showOpenDialog(null);
        if(selectedVal == JFileChooser.APPROVE_OPTION) {
        	textArea.append("You chose to open: " + chooser.getSelectedFile().getName() + "\n");
        	
        	//Read in file
        try {
            Scanner sc = new Scanner (chooser.getSelectedFile());
            sc.useDelimiter(":|\n");
            while (sc.hasNext()){
            	tokens.add(sc.nextLine());
            }
            sc.close();
        }catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Error During File Read", "Error During File Read", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        
        //See if file contained anything/if so, continue filling arrays
        if (!(tokens.isEmpty())) {
			for (int i = 0; i < tokens.size();i++){	
				String temp = tokens.get(i); 
               	
				//Add parties to cave/JTree
                if (temp.startsWith("p")){
                   	Scanner sc = new Scanner (temp);
                	sc.useDelimiter(":");
                	sc.next();
                	int tempIndex = Integer.parseInt(sc.next().trim());
                	String tempName = sc.next().trim();
                	cave.parties.add(p = new Party (tempIndex, tempName));
                	DefaultMutableTreeNode tempNode = addAnItem(p.name, pNode);
                	hashParty.put(tempIndex, p);
                	/*Previous edition items
                	partyIndex.add(a, tempIndex);
                	a++;*/
                	sc.close();
                }//End party creation
                
                //Add creature to cave and respective party 
                else if (temp.startsWith("c")){
                	Scanner sc = new Scanner (temp);
                	sc.useDelimiter(":");
                	sc.next();
                	int tempIndex = Integer.parseInt(sc.next().trim());
                	String tempType = sc.next().trim();
                	String tempName = sc.next().trim();
                	int tempParty = Integer.parseInt(sc.next().trim());
                	int tempEmpathy = Integer.parseInt(sc.next().trim());
                	int tempFear = Integer.parseInt(sc.next().trim());
                	int tempCarryCap = Integer.parseInt(sc.next().trim());
                	double tempAge = Double.valueOf(sc.next().trim()).doubleValue();
                	double tempHeight = Double.valueOf(sc.next().trim()).doubleValue();
                	double tempWeight = Double.valueOf(sc.next().trim()).doubleValue();
                	
                	Creature c = new Creature(tempIndex, tempType, tempName, tempParty,
                    tempEmpathy,tempFear, tempCarryCap, tempAge, tempHeight, tempWeight);
                	
                	//Previous edition item
                	//creatureIndex.add(tempIndex);
                	
                	//Add creature to hashmap/JTree
                	hashCreature.put(tempIndex, c);
                	if(hashParty.containsKey(tempParty)){
                		Party party = hashParty.get(tempParty);
                		party.members.add(c);
                		c.partyClass = party;
                		Enumeration <?> e = rootNode.breadthFirstEnumeration();
                	    while (e.hasMoreElements()){
                	    	DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
                	      if (node.toString().contains(party.name)) {
                	    	  cNode = addAnItem(c.name, node);
                	      }
                	    }
                	} else{
                		cave.stuff.add(c);
                		DefaultMutableTreeNode tempNode = addAnItem(c.name, eNode);
                	
                	/*Previous edition items
                	for(int b = 0; b < partyIndex.size();b++){
                			if(partyIndex.get(b).equals(tempParty)){
                				cave.parties.get(b).members.add(c);
                		}*/
					}
                	
                	sc.close();
                }
                //End creature creation
               
                //Add treasure to respective creature
                else if (temp.startsWith("t")){
                	Scanner sc = new Scanner (temp);
                	sc.useDelimiter(":");
                	sc.next();
                	int tempIndex = Integer.parseInt(sc.next().trim());
                  	String tempType = sc.next().trim();
                	int tempCreature = Integer.parseInt(sc.next().trim());
                   	double tempWeight = Double.valueOf(sc.next().trim()).doubleValue();
                	int tempValue = Integer.parseInt(sc.next().trim());
                	Treasure treasure = new Treasure(tempIndex, tempType, tempCreature, tempWeight, tempValue);
                	
                	//Check hashmap for creature/add treasure to creature/JTree if there is a match
                	if(hashCreature.containsKey(tempCreature)){
                		Creature creature = hashCreature.get(tempCreature);
                		creature.treasure.add(treasure);
                		Enumeration <?> e = rootNode.breadthFirstEnumeration();
                	    while (e.hasMoreElements()){
                	    	DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
                	      if (node.toString().contains(creature.name)) {
                	    	  tNode = addAnItem(treasure.type, node);
                	      }
                	   }
                	} else {
                		cave.stuff.add(treasure);
                		DefaultMutableTreeNode tempNode = addAnItem(treasure.type, eNode);
                	
                	/*Previous edition items
                	if (treasure.creature!=0){
                		for(int b = 0; b < cave.parties.size();b++){
                			for(int d = 0; d < cave.parties.get(b).members.size(); d++){
                				if(cave.parties.get(b).members.get(d).index == treasure.creature){
                					cave.parties.get(b).members.get(d).treasures.add(treasure);
                				}
                			}
                		}*/
                }
                	sc.close();
				}//End treasure creation
                
                //Add artifact to respective creature
                else if (temp.startsWith("a")){
                	Scanner sc = new Scanner (temp);
                	sc.useDelimiter(":");
                	sc.next();
                	int tempIndex = Integer.parseInt(sc.next().trim());
                	String tempType = sc.next().trim();
                	int tempCreature = Integer.parseInt(sc.next().trim());
                    String tempName = sc.next().trim();
                	Artifact artifact = new Artifact(tempIndex, tempType, tempCreature, tempName);
                	
                	//Check hashmap for creature/add artifact to creature/JTree if there is a match
                	if(hashCreature.containsKey(tempCreature)){
                		Creature creature = hashCreature.get(tempCreature);
                		creature.artifacts.add(artifact);
                		Enumeration<?> e = rootNode.breadthFirstEnumeration();
                	    while (e.hasMoreElements()){
                	    	DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
                	      if (node.toString().contains(creature.name)) {
                	    	  aNode = addAnItem(artifact.type, node);
                	      }
                	    }
                	} else {
                		cave.stuff.add(artifact);
                		DefaultMutableTreeNode tempNode = addAnItem(artifact.name, eNode);

                	/*Previous edition items
                	if (artifact.creature!=0){
                		for(int b = 0; b < cave.parties.size();b++){
                			for(int d = 0; d < cave.parties.get(b).members.size(); d++){
                				if(cave.parties.get(b).members.get(d).index == artifact.creature){
                					cave.parties.get(b).members.get(d).artifacts.add(artifact);
                							}
                					}
                			}*/
                	}
                	sc.close();
                }//End artifact creation
                
                //Add jobs to respective creature
                else if (temp.startsWith("j")){
                	ArrayList <String> tempList = new ArrayList<String> ();
                	Scanner sc = new Scanner (temp);
                	sc.useDelimiter(":");
                	sc.next();
                	int tempIndex = Integer.parseInt(sc.next().trim());
                	String tempName = sc.next().trim();
                	int tempCreatureIndex = Integer.parseInt(sc.next().trim());
                   	double tempTime = Double.valueOf(sc.next().trim()).doubleValue();
                   	while(sc.hasNext()){
                   		tempList.add(sc.next().trim());
                   	}
                   	//Previous edition stuff
                   	/*
                   	sc.next();
                   	int tempWands = Integer.parseInt(sc.next().trim());
                   	sc.next();
                   	int tempPotions = Integer.parseInt(sc.next().trim());
                	sc.next();
                	int tempStone = Integer.parseInt(sc.next().trim());
                	sc.next();
                	int tempScroll = Integer.parseInt(sc.next().trim());
                	sc.next();
                	int tempWeapons = Integer.parseInt(sc.next().trim());*/
                	
                	//Check hashmap for creature/add treasure to creature/JTree if there is a match
                	if(hashCreature.containsKey(tempCreatureIndex)){
                		Creature creature = hashCreature.get(tempCreatureIndex);
                		
                		/*Previous edition stuff
                		//Find out if creature has the artifacts necessary to complete this job
                		boolean tempCompletable = cave.isCompletable(creature, tempStone, tempPotions, tempWands, tempWeapons);
                		*/
                		
                		//Create job
                    	Job job = new Job(tempIndex, tempName, tempCreatureIndex, tempTime, tempList, creature);
                    	
                    	//Add job to appropriate creature
                		creature.jobs.add(job);
                		
                		//Place job node under appropriate creature
                		Enumeration <?> e = rootNode.breadthFirstEnumeration();
                	    while (e.hasMoreElements()){
                	    	DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
                	      if (node.toString().contains(creature.name)){
                	    	  jNode = addAnItem(job.name, node);
                	      }
                	   }
                	}   
                	sc.close();
                }//End job creation
			}
			//Get number of each type of artifacts for each party
			cave.partyArtifacts();
           } else{
        	textArea.append("File is empty!");
           }
        }
	}//End readfile method
                
    //Display entire cave
    public void displayInfo () {
    		textArea.setText("\n+++++++++++++++++++++++++SCREEN HAS BEEN REFRESHED+++++++++++++++++++++++++++\n");
    		textArea.append("" + cave);
    		textArea.setCaretPosition(0);
    }
    
    // Add items to the tree under respective parent
    public DefaultMutableTreeNode addAnItem (Object object, DefaultMutableTreeNode parentNode){
    	
    	// Creates a new node for the tree
    	DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(object);
    
    	// Add attaches a name to the node
    	parentNode.add(newNode);
    
    	// return the new node       
    	return newNode;
    }

    //Search method
    public void search (String type, String target) {
        String type1 = type;
        String target1 = target.trim().toLowerCase();
        Boolean bool = true;
        
        textArea.setText("\n\nSearching for TYPE: " + type1 + "     TARGET: " + target1 + "\n\n");

        //Begin search for index
        if(type1.equals("Index")){
        	try{
        		//Convert string to int & get first number from int
        		int targetIndex = Integer.parseInt(target1);
        		int firstNum = Integer.parseInt(target1.substring(0,1));
        		
        		//Check parties for matching index
        		if(firstNum == 1){
        			for (int x = 0; x < cave.parties.size();x++){
        					if (cave.parties.get(x).index == targetIndex){
        						textArea.append(targetIndex + " found for party Name:  " + cave.parties.get(x).name + 
        								"      with " + cave.parties.get(x).members.size()+" members.");
        						bool = false;
        					}
        				}
        			}
        		
        		//Check creatures for matching index
        		if ((firstNum == 2) && bool){
        			for (int x = 0; x < cave.parties.size();x++){
        				for(int y = 0; y < cave.parties.get(x).members.size(); y++){
        						 if (cave.parties.get(x).members.get(y).index == targetIndex){
        							 textArea.append(targetIndex + " found for creature Name:  " + cave.parties.get(x).members.get(y).name +
        									 "       & Type: " + cave.parties.get(x).members.get(y).type);
        							 bool = false;
        						 }
        					}
        				}
        			}
        		
        		//Check artifacts for matching index
            	if ((firstNum == 4) && bool){
            		for(int x = 0; x < cave.parties.size(); x++){
        				for(int y = 0; y < cave.parties.get(x).members.size(); y++){
        					for(int z = 0; z < cave.parties.get(x).members.get(y).artifacts.size(); z++){
        						if(cave.parties.get(x).members.get(y).artifacts.get(z).index == targetIndex){
        							textArea.append(targetIndex + " found for artifact Name:   " + cave.parties.get(x).members.get(y).artifacts.get(z).name +
        									"      & Type: " + cave.parties.get(x).members.get(y).artifacts.get(z).type);
        							bool = false;
    							}
    						}
    					}
        			}
        		}
            	
            	//Check treasures for matching index
        		if ((firstNum == 3) && bool){
        			for(int x = 0; x < cave.parties.size(); x++){
        				for(int y = 0; y < cave.parties.get(x).members.size(); y++){
        					for(int z = 0; z < cave.parties.get(x).members.get(y).treasure.size(); z++){
        						if(cave.parties.get(x).members.get(y).treasure.get(z).index == targetIndex){
        							textArea.append(targetIndex + " found for treasure Type:   " + cave.parties.get(x).members.get(y).treasure.get(z).type +
        									"       & Value: " + cave.parties.get(x).members.get(y).treasure.get(z).value);
            						bool = false;
        						}
        					}
        				} 
        			}
        		}
	        	if(bool) {
	            	textArea.append(targetIndex + " does not exist within this cave! Try again!");
	            }	
          	} catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Incorrect Input - Must Be Integer", "Incorrect Input - Must Be Integer", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
        	}//End index search

        //Begin search for type
        } else if (type1.equals("Type")){
        	try{
        		//Capitalize first letter of each word
        		String[] tokens = target1.split("\\s");
        		target1 = "";
        		for(int i = 0; i < tokens.length; i++){
        		    char capLetter = Character.toUpperCase(tokens[i].charAt(0));
        		    target1 += " " + capLetter + tokens[i].substring(1, tokens[i].length());
        		}
        		String targetType = target1.trim();
        		
        	//Check creatures for matching type
        	for (int x = 0; x < cave.parties.size();x++){
        			for(int y = 0; y < cave.parties.get(x).members.size(); y++){
        				
        				//Search through members for matching type
        				if (cave.parties.get(x).members.get(y).type.equals(targetType)){
        					textArea.append("\n"+targetType + " found for creature Name:   " + cave.parties.get(x).members.get(y).name + 
        							"       Index: " + cave.parties.get(x).members.get(y).index);
        					bool = false;
        				}
        				//Search through treasures for matching type
        				for(int z = 0; z < cave.parties.get(x).members.get(y).treasure.size(); z++){
        					if(cave.parties.get(x).members.get(y).treasure.get(z).type.equals(targetType)){
        							textArea.append("\n"+targetType + " found for  treasure with Value:   " + cave.parties.get(x).members.get(y).treasure.get(z).value +
        									"       Index: " + cave.parties.get(x).members.get(y).treasure.get(z).index);
        							bool = false;
        						} 
        					}
        				//Search through artifacts for matching type
        				for(int z = 0; z < cave.parties.get(x).members.get(y).artifacts.size(); z++){
        					if(cave.parties.get(x).members.get(y).artifacts.get(z).type.equals(targetType)){
        							textArea.append("\n"+targetType + " found for artifact Name:   " + cave.parties.get(x).members.get(y).artifacts.get(z).name +
        									"       Index: " + cave.parties.get(x).members.get(y).artifacts.get(z).index);
        							bool = false;
        						}
        					}
        				}
        			}
        	if (bool) {
            		textArea.append("\n"+targetType + " does not exist within this cave! Try again!");
            		}
            		
          	} catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Incorrect Input - Must Be String", "Incorrect Input - Must Be String", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
        	}//End search for type
        	
        //Begin search for name
        } else if (type1.equals("Name")){
        	try{
        		//Capitalize first letter of each word
        		String[] tokens = target1.split("\\s");
        		target1 = "";

        		for(int i = 0; i < tokens.length; i++){
        		    char capLetter = Character.toUpperCase(tokens[i].charAt(0));
        		    target1 += " " + capLetter + tokens[i].substring(1, tokens[i].length());
        		}
        		String targetName = target1.trim();
        		
        	//Check creatures for matching name
        	for (int x = 0; x < cave.parties.size();x++){
        			for(int y = 0; y < cave.parties.get(x).members.size(); y++){
        				//Search through members
        				if (cave.parties.get(x).members.get(y).name.equals(targetName)){
        					textArea.append("\n"+targetName + " found for creature Type:   " + cave.parties.get(x).members.get(y).type + 
        							"       Index: " + cave.parties.get(x).members.get(y).index);
        					bool = false;
        				}
        				//Search through artifacts for matching name
        				for(int z = 0; z < cave.parties.get(x).members.get(y).artifacts.size(); z++){
        					if(cave.parties.get(x).members.get(y).artifacts.get(z).name.equals(targetName)){
        							textArea.append("\n"+targetName + " found for artifact Type:  " + cave.parties.get(x).members.get(y).artifacts.get(z).type +
        									"       Index: " + cave.parties.get(x).members.get(y).artifacts.get(z).index);
        							bool = false;
        						}
        					}
        				}
        			}
        	if (bool) {
            		textArea.append("\n"+targetName + " does not exist within this cave! Try again!");
            		}
            		
          	} catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Incorrect Input - Must Be String", "Incorrect Input - Must Be String", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
        	}

        }
    }//End search method
    
    //Sort creature method
    public void sortCreature (String compareBy){
    	if (cave.parties.isEmpty()){
    		textArea.append("\nCAVE IS EMPTY!!!!!\n");
    		return;
    	}
    	displayInfo();
    	textArea.append("+++++++++++++++++++++++++" + cave.sortCreatures(compareBy) + "+++++++++++++++++++++++++");
    }
    
    //Sort treasure method
    public void sortTreasure (String compareBy){
    	if (cave.parties.isEmpty()){
    		textArea.append("CAVE IS EMPTY!!!!!");
    		return;
    	}
    	displayInfo();
    	textArea.append("+++++++++++++++++++++++++" + cave.sortTreasure(compareBy) + "+++++++++++++++++++++++++");
    }
    
    //Sort artifact method
    public void sortArtifacts (String compareBy){
    	if (cave.parties.isEmpty()){
    		textArea.append("CAVE IS EMPTY!!!!!");
    		return;
    	}
    	displayInfo();
    	textArea.append("+++++++++++++++++++++++++" + cave.sortArtifacts(compareBy) + "+++++++++++++++++++++++++");
    } 
        
	public static void main(String[] args) throws IOException {
        SorcerersCave runGame = new SorcerersCave ();
        runGame.textArea.append("Welcome to Sorcerer's Cave! \nBegin by selecting an input file above!\n");
    }
}
