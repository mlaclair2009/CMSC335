package sorcererscavelaclair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

public class Cave {
	ArrayList <Party> parties = new ArrayList <Party> ();
	ArrayList <CaveElement> stuff = new ArrayList <CaveElement> ();
	String name;
	
	//Sort creatures method
	public String sortCreatures(String field){
		String compareBy = field;
		switch (compareBy){
			case "Name":
						for(Party p : parties){
							Collections.sort(p.getPartyMembers(), (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
						}
						return "Creatures are sorted by Name!";
			case  "Age":
						for(Party p : parties){
							Collections.sort(p.getPartyMembers(), new Comparator <Creature>() {
							    public int compare(Creature o1, Creature o2) {
							        return new Double(o1.getAge()).compareTo(o2.getAge());
							    }
							});
						}
						return "Creatures are sorted by Age!";
			case  "Height":
						for(Party p : parties){
							Collections.sort(p.getPartyMembers(), new Comparator <Creature>() {
							    public int compare(Creature o1, Creature o2) {
							        return new Double(o1.getHeight()).compareTo(o2.getHeight());
							    }
							});
						}
						return "Creatures are sorted by Height!";
			case  "Weight":
						for(Party p : parties){
							Collections.sort(p.getPartyMembers(), new Comparator <Creature>() {
							    public int compare(Creature o1, Creature o2) {
							        return new Double(o1.getWeight()).compareTo(o2.getWeight());
							    }
							});
						}
						return "Creatures are sorted by Weight!";
			case  "Empathy":
						for(Party p : parties){
							Collections.sort(p.getPartyMembers(), new Comparator <Creature>() {
							    public int compare(Creature o1, Creature o2) {
							        return new Integer(o1.getEmpathy()).compareTo(o2.getEmpathy());
							    }
							});
						}
						return "Creatures are sorted by Empathy!";
			case  "Fear":
						for(Party p : parties){
							Collections.sort(p.getPartyMembers(), new Comparator <Creature>() {
							    public int compare(Creature o1, Creature o2) {
							        return new Integer(o1.getFear()).compareTo(o2.getFear());
							    }
							});
						}
						return "Creatures are sorted by Fear!";
			case  "Carry Cap":
						for(Party p : parties){
							Collections.sort(p.getPartyMembers(), new Comparator <Creature>() {
							    public int compare(Creature o1, Creature o2) {
							        return new Integer(o1.getCarryCap()).compareTo(o2.getCarryCap());
							    }
							});
						}
						return "Creatures are sorted by Carry Capacity!";
			}
		return null;
	}//End sort creatures method
	
	 //Sort treasures method
    public String sortTreasure(String field){
		String compareBy = field;
		switch (compareBy){
			case  "Weight":
				for(Party p : parties){
					for(Creature c : p.members){
						Collections.sort(c.getMemberTreasure(), new Comparator <Treasure>() {
						    public int compare(Treasure o1, Treasure o2) {
						        return new Double(o1.getWeight()).compareTo(o2.getWeight());
						    }
						});
					}
				}
					return "Treasures are sorted by Weight!";
			case  "Value":
				for(Party p : parties){
					for(Creature c : p.members){
						Collections.sort(c.getMemberTreasure(), new Comparator <Treasure>() {
						    public int compare(Treasure o1, Treasure o2) {
						        return new Integer(o1.getValue()).compareTo(o2.getValue());
						    }
						});
					}
				}
				return "Treasures are sorted by Value!";
			}
			return null;
		}//End sort treasures method
    
    //Sort artifacts method
    public String sortArtifacts(String field){
		String compareBy = field;
		switch (compareBy){
			case  "Name":
				for(Party p : parties){
					for(Creature c : p.members){
						Collections.sort(c.getMemberArtifacts(), (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
					}
				}
				return "Artifacts are sorted by Name!";
			case  "Type":
				for(Party p : parties){
					for(Creature c : p.members){
						Collections.sort(c.getMemberArtifacts(), (a, b) -> a.getType().compareToIgnoreCase(b.getType()));
					}
				}
				return "Artifacts are sorted by Type!";
			}
			return null;
    	}//End sort artifacts method
    
    /*Previous edition stuff
    //Method checks if creature has necessary objects to complete job
    public boolean isCompletable(Creature creature, int tempStones, int tempPotions, int tempWands, int tempWeapons){
    	ArrayList <Integer> tempArr = new ArrayList <Integer> ();
		if (creature.artifacts.isEmpty())
					return false;
		else {
			tempArr = creature.getNumArtifacts();
			int stones = tempArr.get(0);
			int potions = tempArr.get(1);
			int wands = tempArr.get(2);
			int weapons = tempArr.get(3);
			if(stones >= tempStones && potions >= tempPotions && wands >= tempWands && weapons >= tempWeapons){
				return true;
				}
			}
		return false;
		}//End isCompletable method
    */
    
    
    //Cycle through each party building list of artifacts
    public void partyArtifacts(){
    	for(Party p : parties){
    		p.getPartyArtifacts();
    	}
    }
    
    //Cycle through each party building list of artifacts
    public String caveArtifacts(){
    	ArrayList <String> list = new ArrayList <String> ();
    	for(Party p : parties){
        	list.add("Pool for Party: " + p.name);
        	list.add("Armour x " + p.numArmour);
        	list.add("Potion x " + p.numPotions);
        	list.add("Scrolls x " + p.numScrolls);
        	list.add("Stones x " + p.numStones);
        	list.add("Swords x " + p.numSwords);
        	list.add("Wands x " + p.numWands);
        	list.add("\n");
    	}
		return list.toString();
    }
    
    //Build list of jobs
    public ArrayList <Job> getJobs(){
    	ArrayList <Job> jobs = new ArrayList <Job>();
    	for (Party p : parties){
    		for(Creature c : p.members){
    			for(Job j : c.jobs){
    	    		jobs.add(j);
    				}
    			}	
    		}
		return jobs;
    }
    
    //Return name of creature
	public String getName(int index) {
		for (Party p : parties){
			for (Creature c : p.members){
				if(c.index == index){
					return c.name;
				}
			}
		}
	return null;
	}	 
 	
	//For display method
    public String toString () {
        String output = "\n\nThe Cave Consists of the following:";
        for (Party p: parties)
            output += p + "\n";
        output += "\n+++++++\nThe Extra stuff:\n";
        for (CaveElement e: stuff)
        	output += e + "\n";
        return output;
    	}
}//End Cave class