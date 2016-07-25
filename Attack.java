/**
 * PokemonArena.java
 *
 *
 * Michal Jez 
 * Version 3.14159 13/1/2016
 *
 *---Attack Class---
 *A class where all the information for a specific attack is stored.
 *None of the game logic is done in methods of the class. It only contains getter
 *methods for accessing info of an attack. Once an attack has been created, its values
 *cannot be modified
 */

public class Attack 
{
	String name;
	int energyCost, damage;
	/*Damage can be affected by if the Pokemon is disabled
	 *but will return to origDamage at the end of the battle*/
	String special;
	
	public Attack(String str, int eCost, int dam, String spec)
	{
		name = str;
		energyCost = eCost;
		damage = dam;
		special = spec;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getEnergyCost()
	{	return energyCost;	}
	
	public String getSpecial()
	{	return special;		}
	
	public double getDamage()
	{	return (double) damage;		}
	
	public String[] getStats()
	{	/*Returns the stats of the Attack in a nicely formatted table ready to print*/
		String[] toFormat = {"Attack Name", name, "Energy Cost", Integer.toString(energyCost), "Damage", Integer.toString(damage), "Special", special};
		String[] toReturn = new String[toFormat.length / 2 + 1];
		
		for (int i = 0; i < toReturn.length - 1; i++)
		{
			toReturn[i] = String.format("%s\n|%-19s|%19s|\n", Print.repeat('-', 41), toFormat[i * 2], toFormat[i * 2 + 1]);
		}
		toReturn[toReturn.length - 1] = Print.repeat('-', 41) + '\n';
		return toReturn;
	}
	
}