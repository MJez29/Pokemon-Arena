/**
 * PokemonArena.java
 *
 *
 * Michal Jez 
 * Version 3.14159 13/1/2016
 */

import java.util.*;
import java.io.*;

public class Pokemon 
{
	private int energy, numAttacks;
	private double hitPoints, maxHitPoints;
	private String name, type, resistance, weakness;
	private ArrayList<Attack> attacks;
	private boolean disabled, stunned, dead, hasBeenDisabled;

    public Pokemon(String str) 
    {	/*Constructor - Takes in the line of the Pokemon from the text file and parses it*/
    	disabled = false;
    	stunned = false;
    	dead = false;
    	hasBeenDisabled = false;
    	String[] info = str.split(",");
    	name = info[0];
    	hitPoints = (double) Integer.parseInt(info[1]);
    	maxHitPoints = (double) Integer.parseInt(info[1]);
    	type = info[2];
    	resistance = info[3];
    	weakness = info[4];
    	numAttacks = Integer.parseInt(info[5]);
    	energy = 50;
    	
    	attacks = new ArrayList<Attack>();
    	for (int i = 0; i < numAttacks; i++) /*There is a variable number of attacks so they must be read ina loop*/
    	{
    		attacks.add(new Attack(info[6 + 4 * i],
    							   Integer.parseInt(info[7 + 4 * i]),
    							   Integer.parseInt(info[8 + 4 * i]),
    							   info[9 + 4 * i]));
    	}
    	
    }
    public Pokemon(Pokemon pk)
    {
    	
    }
    
    //-------------------------------------------------------Hit Points-----------------------------------------------------------
    
    public void setHP(int h)
    {
    	hitPoints = h;
    }
    
    public double getHP()
    {return hitPoints;
    }
    
    public void inflictDamage(double dam)
    {	/*Inflicts the given amount of damage, their HP cannot go below 0, if it reaches 0 they are dead*/
    	hitPoints -= dam;
    	if (hitPoints <= 0.0)
    	{	
    		dead = true;
    		hitPoints = 0.0;
    	}
    }
    
    public void recoverHP(int amt)
    {	/*Replenishes the hitpoints of the Pokemon
    	Its hitPoints cannot exceed the amount that it started with in the .txt file*/
    	hitPoints += amt;
    	if (hitPoints > maxHitPoints)
    	{	hitPoints = maxHitPoints;  	}
    }
    
    public boolean isDead()
    {	/*Returns whether the Pokemon has 0 HP or not*/
    	return dead;
    }
    
    //-------------------------------------------------Resistance and Weakness------------------------------------------------
    public String getResistance()
    {	return resistance;    }
    
    public String getWeakness()
    {	return weakness;    }
    
    //-----------------------------------------------------Disabling--------------------------------------------------
    //The Pokemon deals 10 less damage when disabled
    public void disable()
    {
    	disabled = true;
    }
    
    public void unDisable()
    {
    	disabled = false;
    }
    
    //------------------------------------------------------Stunning--------------------------------------------------
    public void stun()
    {	/*Makes the pokemon stunned*/
    	stunned = true;
    }
    
    public void unStun()
    {	/*Unstuns the Pokemon*/
    	stunned = false;
    }
    
    public boolean isStunned()
    {	/*Returns state of stunned*/
    	return stunned;
    }
    
    //-------------------------------------------------------Attacks--------------------------------------------------
    public ArrayList<Attack> getAttacks()
    {	//Returns an ArrayList of the Attacks
    	return attacks;
    }
    
    public Attack getAttack(int index)
    {	/*Gets the attack at the given index*/
    	return attacks.get(index);
    }
    
    public Integer[] getAffordableAttackNums()
    {	/*Returns an array of the indexes of the attacks that the Pokemon can afford*/
    	ArrayList<Integer> canAfford = new ArrayList<Integer> ();
    	for (int i = 0; i < attacks.size(); i++)
    	{
    		if (attacks.get(i).getEnergyCost() <= energy) /*Goes through each attack to see if it costs less than the enrgy it has*/
    		{
    			canAfford.add(i);
    		}
    	}
    	return canAfford.toArray(new Integer[canAfford.size()]);
    }
    
    public String[] getAttackNames()
    {	/*Returns the names of all of the attacks that the Pokemon has*/
    	String[] names = new String[attacks.size()];
    	for (int i = 0; i < attacks.size(); i++)
    	{
    		names[i] = attacks.get(i).getName();
    	}
    	return names;
    }
    
    public int getNumOfAttacks()
    {	/*Returns the total number of attacks that the Pokemon has*/
    	return attacks.size();
    }
    
    public boolean attack(int attNum, Pokemon enemy)
    {	/*Inflicts damage on the enemy Pokemon using the attack specified by attNum.
    	Returns a boolean indicating the success of the attack, true if successful or false
    	if the Pokemon cannot perform that attack.*/
    	
    	if (attacks.get(attNum).getEnergyCost() > energy) //If they cannot afford the attack
    	{	
    		Print.slowPrint("You cannot afford this attack.\n");
    		return false;
      	}
      	
      	Random rand = new Random();
      	String spec = attacks.get(attNum).getSpecial();
      	double dam = attacks.get(attNum).getDamage();
      	
      	if (disabled)						//If the pokemon is disabled its attacks do 10 less
      		dam = Math.max(0, dam - 10);	//damage to a minimum of 0
      	
      	if (enemy.getResistance().equals(type)) 		//If the Pokemon is attacking something that is resistant to it
      	{	dam /= 2.0;		}							//Damage is cut in half
      	
      	if (enemy.getWeakness().equals(type))			//If the Pokemon is attacking something that has a weakness to it
      	{	dam *= 2.0;   	}							//Damage is doubled
      	
      	Print.slowPrint(name + " attacked using " + attacks.get(attNum).getName() + "\n");
      	
      	if ("stun".equals(spec))		//If its special is stun
      	{	
      		enemy.inflictDamage(dam);	//Inflicts damage and theres
      		if (rand.nextBoolean()) 	//A 50% chance that the opponent gets stunned
      		{	
      			enemy.stun();
      			Print.slowPrint(enemy.getName() + " was stunned!\n");
      		}
      	}
      	else if ("wild card".equals(spec))	//If its special is wild card
      	{
      		if (rand.nextBoolean()) //50 % chance of inflicting damage
      		{	
      			enemy.inflictDamage(dam);	
      		}
      		else					//If it missed
      		{
      			Print.slowPrint(name + " missed!\n");
      			dam = 0.0;
      		}
      	}
      	else if ("wild storm".equals(spec))
      	{
      		double accumulative = 0.0;		//Counter of how damage has been dealt in total
      		while (true)					//Keeps on inflicting damage until it misses
      		{
      			if (rand.nextBoolean())		//50 % chance of inflicting damage
      			{
      				enemy.inflictDamage(dam);
      				accumulative += dam;
      				continue;
      			}
      			Print.slowPrint(name + " missed after " + (accumulative / dam) + " successful attacks!\n");
      			break;
      		}
      		dam = accumulative;
      	}
      	else if ("disable".equals(spec))
      	{
      		enemy.disable();				//Disables opponent
      		Print.slowPrint(enemy.getName() + " has been disabled\n");
      		enemy.inflictDamage(dam);
      	}
      	
      	else if ("recharge".equals(spec))
      	{
      		recoverEnergy(20);
      		enemy.inflictDamage(dam);
      	}
      	else								//If the attack has no special
      	{
      		enemy.inflictDamage(dam);
      	}
      	
      	energy -= attacks.get(attNum).getEnergyCost();
      	
      	Print.slowPrint(name + "'s attack did " + (dam > 0.0 ? dam : "no") + " damage\n");
      	Print.slowPrint(name + " now has " + (energy > 0 ? energy : "no") + " energy\n");
      	Print.slowPrint(enemy.getName() + " now has " + enemy.getHP() + " hit points\n");
    	return true;		//Attack was a success
    }
    
    //-------------------------------------------------------Energy---------------------------------------------------
    
    public void recoverEnergy(int amt)
    {	/*The pokemon regains the given amount of energy or until it has 50 energy points*/
    	energy += amt;
    	if (energy > 50)
    		energy = 50;
    }
    
    public void replenishEnergy()
    {	/*The pokemon's energy supply is replenished AKA his energy is 50*/
    	energy = 50;
    }
    
    //-------------------------------------------------Printing Methods---------------------------------------------
    
    public String toString()
    {
    	return String.format("%s is of type %s. It has %d hitpoints.", name, type, hitPoints);
    }
    
    public String getName() {	return name;   }
    
    public String[] getStats()
    {	/*Returns the info of the Pokemon in an array thats nicely formatted*/
    	String[] words = {"Pokemon", name, "Hitpoints", Double.toString(hitPoints), "Type", type, "Resistance", resistance, "Weakness", weakness};
    	String[] toReturn = new String[words.length / 2];
    	
    	for (int a = 0; a < words.length; a++)
    	{	/*Creates an array of what to print so that it can print with delay in Print.printPokemon()*/
    		String str = "";
	    	for (int i = 0; i <= 40; i++)
	    		str += "-";
	    	str += String.format("\n|%-19s|%19s|\n", words[a++], words[a]);
	    	toReturn[a / 2] = str;
    	}
	    
	    return toReturn;
    }
    
    public String[] getRawStats()
    {	/*Returns the same information as getStats() but it is unformatted and not ready for printing*/
    	return new String[] {"Pokemon", name, "Hitpoints", Double.toString(hitPoints), "Type", type, "Resistance", resistance, "Weakness", weakness};
    }
}