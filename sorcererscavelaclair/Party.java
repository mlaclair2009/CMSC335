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

class Party extends CaveElement{
    ArrayList <Creature> members = new ArrayList <Creature> ();
    ArrayList <String> missing;
    int index, numStones, numWands, numPotions, numScrolls, numSwords, numArmour;
    
    //Party constructor
    public Party (int tempIndex, String tempName) {
    	this.index = tempIndex;
    	this.name = tempName;
    }
    
    public ArrayList <Creature> getPartyMembers() {
        return members;
    }
    
    public String getName(){
		return name;
    }
    
    //Gather numbers of each type of artifact within party/Mark jobs as doable
    public void getPartyArtifacts(){
    	for(Creature c: members){
    		c.getNumArtifacts();
    		this.numWands+=c.numWands;
    		this.numArmour+=c.numArmour;
    		this.numPotions+=c.numPotions;
    		this.numScrolls+=c.numScrolls;
    		this.numSwords+=c.numSwords;
    		this.numStones+=c.numStones;
    		}
    	for(Creature c: members){
    		for(Job j : c.jobs){
    			if(j.armour <= this.numArmour && j.potion <= this.numPotions && j.sword <= this.numSwords &&
    	    			j.stone <= this.numStones && j.wand <= this.numWands && j.scroll <= this.numScrolls){
    				j.doableFlag = true;
    			}
    		}
    	}
    }
        
    //Check out items if they are available
    public Boolean checkIfAvailable(Job j){
    	//See if all items are available
    	if(j.armour <= this.numArmour && j.potion <= this.numPotions && j.sword <= this.numSwords &&
    			j.stone <= this.numStones && j.wand <= this.numWands && j.scroll <= this.numScrolls){
    		//Checkout necessary items
    		this.numArmour = this.numArmour - j.armour;
    		this.numPotions = this.numPotions - j.potion;
    		this.numSwords = this.numSwords - j.sword;
    		this.numStones = this.numStones - j.stone;
    		this.numWands = this.numWands - j.wand;
    		this.numScrolls = this.numScrolls - j.scroll;
    		return true;
    	}else{
    	findItemsOutstanding(j);
		return false;
    	}
    }
    
    public void findItemsOutstanding(Job j) {
    	missing = new ArrayList <String> ();
		if(j.armour > this.numArmour)
			missing.add(" Armour x " + j.armour);
		if (j.potion > this.numPotions)
			missing.add(" Potion x " + j.potion);
		if (j.sword > this.numSwords)
			missing.add(" Sword x " + j.sword);
		if (j.stone > this.numStones)
			missing.add(" Stone x " + j.stone);
		if (j.wand > this.numWands)
			missing.add(" Wand x " + j.wand);
		if (j.scroll > this.numScrolls)
			missing.add(" Scroll x " + j.scroll);
		j.missing.addAll(missing);
	}

	//Check in items once job is done
    public void checkInItems(Job j){
		this.numArmour += j.armour;
		this.numPotions += j.potion;
		this.numSwords += j.sword;
		this.numStones += j.stone;
		this.numWands += j.wand;
		this.numScrolls += j.scroll;
    }
      			 			
  //For display method
    public String toString () {
    	String output = "\nParty Name: " + name + "     Party Index: " + index + "      Consists of: \n\n";
        for (Creature c: members)
            output +="-----" + c + "\n";
        return output;
    }
}//End Party class
