/**
 * PokemonArena.java
 *
 *
 * Michal Jez 
 * Version 3.14159 13/1/2016
 */

import java.util.*;
import java.io.*;
import java.util.Arrays.*;

public class Print
{	/*Class of static methods that handle printing to the console*/
	public static void slowPrint(String str)
	{	/*Prints one character every 75 milliseconds instead of the whole string at once.
		Like how old school video games would print text to the screen.*/
		for (char ch : str.toCharArray())
		{
			System.out.print(ch);
			try {
		    	Thread.sleep(75);
		    } catch(InterruptedException ex) {
		    	Thread.currentThread().interrupt();
			}
		}
	}
	
	public static void slowPrint(String[] strs)
	{	/*Prints every string in strs with a delay in between each.*/
		for (String str : strs)
		{
			System.out.print(str);
			try {
		    	Thread.sleep(75);
		    } catch(InterruptedException ex) {
		    	Thread.currentThread().interrupt();
			}
		}
	}
	
	public static void slowPrint(String[] strs, int delay)
	{	/*Prints every string in strs with the given delay in between each.*/
		for (String str : strs)
		{
			System.out.print(str);
			try {
		    	Thread.sleep(delay);
		    } catch(InterruptedException ex) {
		    	Thread.currentThread().interrupt();
			}
		}
	}
	
	public static void printPokemon(ArrayList<Pokemon> pk)
	{	/*Prints a table of Pokemon for the player to choose from
		Includes the option to pick them or to get more info from them*/
		printChar('-', 42);
		System.out.printf("\n|%-10s|%-10s|%-18s|\n", "To choose", "More info", "Name");
		printChar('-', 42);
		int i = 1;
		for (Pokemon p : pk)
		{
			System.out.printf("\n|%-10d|%-10d|%-18s|\n", i++, i + pk.size() - 1, p.getName());
			try {
		    	Thread.sleep(75);
		    } catch(InterruptedException ex) {
		    	Thread.currentThread().interrupt();
			}
			printChar('-', 42);
		}
	}
	
	public static void printPokemon(Pokemon pk1, Pokemon pk2)
	{	/*Prints the stats of 2 pokemon side by side*/
		String[] statsPk1 = pk1.getRawStats();
		String[] statsPk2 = pk2.getRawStats();
		
		String[] stats = new String[statsPk1.length / 2 + 1];
		
		String line = repeat('-', 80);
		
		for (int i = 0; i < stats.length - 1; i++)
		{
			
			stats[i] = String.format("%s\n|%-19s|%s|%19s|\n", line, statsPk1[i * 2 + 1], 
/*Centers the text that goes in the middle*/ 	repeat(' ', 19 - statsPk1[i * 2].length() / 2) + statsPk1[i * 2] + repeat(' ', 19 - statsPk1[i * 2].length() / 2 - statsPk1[i * 2].length() % 2),
									 			statsPk2[i * 2 + 1]);
		}
		stats[stats.length - 1] = line;
		slowPrint(stats);
	}
	
	public static void printPokemon(Pokemon pk)
	{	/*Prints the stats of the given Pokemon*/
		slowPrint(pk.getStats());
		System.out.print(repeat('-', 41) + "\n");
	}
	
	public static void printTurn(boolean playerTurn, Pokemon enemyPk)
	{	/*Prints out who's turn it is - if playerTurn == true, its the player's turn*/
		slowPrint(String.format("\nIt is %s turn!\n", ((playerTurn) ? "your" : (enemyPk.getName() + "'s"))));
	}
	
	public static String repeat(char ch, int times)
	{	/*Returns a string of the given char, the given number of times*/
		String str = "";
		for(int i = 0; i < times; i++)
		{
			str += ch;
		}
		return str;
	}
	
	public static void printChar(char ch, int times)
	{	/*Prints the given char the given number of times.
		All on the same line.*/
		for(int i = 0; i < times; i++)
		{
			System.out.print(ch);
		}
	}
	
	public static void printOptions(Pokemon pk)
	{	/*Prints the options available to the player
		and prints the codes to select the option or to get more info about it
		No IO is done here*/
		String[] attackNames = pk.getAttackNames();
		String[] options = new String[attackNames.length + 2];
		
		printChar('-', 54);
		System.out.printf("\n|%-10s|%-10s|%-30s|\n", "To choose", "More info", "Option");
		printChar('-', 54);
		
		for (int i = 0; i < attackNames.length; i++)
		{
			options[i] = String.format("\n|%-10s|%-10s|%-30s|", i + 1, options.length + i + 1, "Attack using " + attackNames[i]);
		}
		options[options.length - 2] = String.format("\n|%-10s|%-10s|%-30s|", options.length - 1, attackNames.length + options.length + 1, "Retreat");
		options[options.length - 1] = String.format("\n|%-10s|%-10s|%-30s|\n", options.length, attackNames.length + options.length + 2, "Pass");
		
		slowPrint(options);
		printChar('-', 54);
		System.out.println();
	}
	
	
	public static void printOutro(boolean playerWins) throws IOException
	{	/*Prints an outro based on the outcome of the game.
		A more extravagant one appears if the player wins*/
		if (playerWins)
		{
			slowPrint("Congratulations!\nYou have defeated all of the other Pokemon!\nYou are the:\n");
			Scanner inFile = new Scanner(new BufferedReader(new FileReader("Text Files\\Winning Text.txt")));
			ArrayList<String> lines = new ArrayList<String> ();
			
			while (inFile.hasNextLine())				//Reads fancy text from the text file
			{											//And prints it on the screen
				lines.add(inFile.nextLine() + "\n");
			}
			
			slowPrint(lines.toArray(new String[lines.size()]), 150);
			inFile.close();
		}
		
		else
		{
			slowPrint("All of your Pokemon have been defeated!\nYou lose...\n");
		}
	}
}