package sorcererscavelaclair;

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

class Treasure extends CaveElement{
    
	String type;
	int index, creature, value;
	double weight;
	
	//Treasure constructor
    public Treasure (int tempIndex, String tempType, int tempCreature, double tempWeight, int tempValue) {
    	this.index = tempIndex;
    	this.type = tempType;
    	this.creature = tempCreature;
    	this.weight = tempWeight;
    	this.value = tempValue;
    }
    
    public Double getWeight(){
		return weight;
    }
    
    public Integer getValue(){
		return value;
    }

}//End Treasure class