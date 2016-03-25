package sorcererscavelaclair;

import java.util.ArrayList;

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

class Creature extends CaveElement{
    ArrayList <Treasure> treasure = new ArrayList <Treasure> ();
    ArrayList <Artifact> artifacts = new ArrayList <Artifact> ();
    ArrayList <Job> jobs = new ArrayList <Job> ();
    ArrayList <Integer> artifactCount = new ArrayList <Integer> ();
    String name, type;
    int index, party, empathy, fear, carryCap, numStones, numWands, numPotions, numScrolls, numSwords, numArmour;
    double age, height, weight;
    boolean busyFlag = false;
    Party partyClass;

    
    //Creature constructor
    public Creature (int tempIndex, String tempType, String tempName, int tempParty, int tempEmpathy, 
    		int tempFear, int tempCarryCap, double tempAge, double tempHeight, double tempWeight) {
    	
    	this.index = tempIndex;
    	this.type = tempType;
    	this.name = tempName;
    	this.party = tempParty;
    	this.empathy = tempEmpathy;
    	this.fear = tempFear;
    	this.carryCap = tempCarryCap;
    	this.age = tempAge;
    	this.height = tempHeight;
    	this.weight = tempWeight;
    	}
    
    public String getName(){
		return name;
    }
    
    public Double getAge(){
		return age;
    }
    
    public Double getHeight(){
		return height;
    }
    
    public Double getWeight(){
		return weight;
    }
    
    public Integer getEmpathy(){
		return empathy;
    }
    
    public Integer getFear(){
		return fear;
    }
    
    public Integer getCarryCap(){
		return carryCap;
    }
    
    public ArrayList <Treasure> getMemberTreasure() {
        return treasure;
    }
    
    public ArrayList <Artifact> getMemberArtifacts() {
        return artifacts;
    }
    
    //Returns number of each type of artifact
    public void getNumArtifacts(){
    	for (Artifact a: artifacts){
    		if(a.type.equalsIgnoreCase("Stone")){
    			numStones++;
    		}
    		if(a.type.equals("Potion")){
             	numPotions++;
            }
    	    if(a.type.equalsIgnoreCase("Wand")){
        		numWands++;
    	    }
            if(a.type.equals("Scroll")){
            	numScrolls++;
            }
            if(a.type.equals("Sword")){
            	numSwords++;
            }
            if(a.type.equals("Armour")){
            	numArmour++;
            }
    	}
    	/*Previous edition stuff
    	artifactCount.add(numWands);
    	artifactCount.add(numPotions);
    	artifactCount.add(numStones);
    	artifactCount.add(numScrolls);
    	artifactCount.add(numSwords);
    	artifactCount.add(numArmour);
		return artifactCount;	
		*/
    }//End method
    
    ////For display method
    public String toString () {
        String output = "Name: " + name + "     Index: " + index + "     Type: " + type + "     Party: " 
                        + party + "\n     Empathy: " + empathy + "     Fear: " + fear + "     Carrying Capacity: " 
        		        + carryCap + "\n     Age: " + age + "     Height: " + height + "     Weight: " + weight;
 
        output += "\n---TREASURES:\n";
        for (Treasure t: treasure){
        	if(t.creature == index)
        		output += "-----Type: " + t.type + "     Index: " + t.index + "     Weight: " + t.weight 
        		          + "     Value: " + t.value + "\n";
        }
        
        output += "\n---ARTIFACTS:\n";
        for (Artifact a: artifacts){
        	if (a.creature == index)
        		output += "-----Type: " + a.type + "     Index: " + a.index + "     Name: "
                          + a.name + "\n";
        }
        
        output += "\n---JOBS:\n";
        for (Job j: jobs){
        	if (j.creature == index)
        		output += "-----Name: " + j.name + "     Index: " + j.index + "     Time Needed: "
                          + j.jobTime + "\n     Items Needed: " + j.itemList.toString() + "\n";
        }
        return output;
    }
}//End Creature class
