/**
 * PokemonArena.java
 *
 *
 * Michal Jez 
 * Version 3.14159 13/1/2016
 *
 *---PokemonArena class---
 *Most of the game logic takes place in this class. All user interactions are handled in this class.
 *The main method runs through the steps that take place in the game, once the program reaches the end of the method, 
 *it means that the arena battling is over and that eather the player or the computer won.
 */

import java.util.*;
import java.io.*;

public class PokemonArena {
	
	private static ArrayList<Pokemon> enemyPokemon;		//Pokemon that the player must defeat
	private static ArrayList<Pokemon> playerPokemon;	//Pokemon that the player has left
	private static Pokemon curOpponent;					//The Pokemon that the player is currently versing
	private static Pokemon curPokemon;					//The Pokemon that the player is currently using
														//Both are shallow copies of the object from the arraylists
	
	private static void pause(int time)
	{	/*Pauses the program for the given time*/
		try {
		    Thread.sleep(time);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
	
	
	
	private static void printEnemyPokemon()
	{	/*Prints all of the enemy pokemon */
		System.out.printf("\n%-10s%s\n", "Number", "Pokemon");
		int i = 0;
		for (Pokemon pk : enemyPokemon)
		{
			System.out.printf("%-10d%s\n", i++, pk.getName());
			try {
		    	Thread.sleep(75);
		    } catch(InterruptedException ex) {
		    	Thread.currentThread().interrupt();
			}
		}
	}
	
	private static void chooseStartingPokemon()
	{	/*Allows the player to choose which 4 pokemon they want from the list of enemy Pokemon at the beginning of
		the game*/
		int[] chosen = new int[4];
		Scanner kb = new Scanner(System.in);
		for (int i = 0; i < 4; i++)
		{
			while (true) /*Loops until they enter a valid choice*/
			{
				Print.slowPrint("\nEnter a number: ");
				int choice = kb.nextInt() - 1;
				
				if (choice < 0 || choice >= enemyPokemon.size() * 2) /*If choice is out of range*/
				{
					System.out.println("Invalid choice. Index out of range.");
					continue;
				}
				if (choice >= enemyPokemon.size()) 	//If they want more info about a PK
				{
					Print.printPokemon(enemyPokemon.get(choice - enemyPokemon.size()));
					continue;
				}
				boolean reLoop = false;
				for (int n = 0; n < i; n++)
				{
					if (choice == chosen[n]) /*If they rechose a selected pokemon*/
						{
							System.out.println("Invalid choice. Already selected " + enemyPokemon.get(choice).getName() + ".");
							reLoop = true;
							continue;
						}
				}
				if (reLoop) continue;
				
				chosen[i] = choice;
				Print.slowPrint(String.format("%s, I choose you!\n", enemyPokemon.get(choice).getName()));
				break;
			}
		}
		playerPokemon = new ArrayList<Pokemon>();
		for (int i = 0; i < chosen.length; i++) /*Adds all the chosen Pokemon to the player's ArrayList*/
		{
			playerPokemon.add(enemyPokemon.get(chosen[i]));
		}
		for (Pokemon pk : playerPokemon) /*Removes the chosen ones from the enemy ones*/
		{
			enemyPokemon.remove(pk);
		}
	}
	private static void chooseCurPokemon(String toPrint)
	{	/*Allows the player to choose their pokemon for the current battle*/
		Print.slowPrint(toPrint);
		Print.printPokemon(playerPokemon);
		int pick;
		Scanner kb = new Scanner(System.in);
		while (true)	/*Loops until they enter a valid attack*/
		{
			Print.slowPrint("\nEnter your pick: ");
			pick = kb.nextInt() - 1;
			if (pick < 0 || pick >= playerPokemon.size() * 2) /*If out of range*/
			{	
				Print.slowPrint("Invalid input. Index out of range\n\n");
				continue;	 
			}
			if (pick >= playerPokemon.size())	/*If they want more info*/
			{
				Print.printPokemon(playerPokemon.get(pick - playerPokemon.size()));
				continue;
			}
			if (validate("choose " + playerPokemon.get(pick).getName())) 	/*If they confirm they want this PK*/
				break;														/*Stops choosing*/
			
		}
		curPokemon = playerPokemon.get(pick);								/*Overwrites past info about the curPK*/
		Print.slowPrint(String.format("%s, I choose you!\n", curPokemon.getName()));
	}
	
	private static void makePokemon() throws IOException
	{   /*Creates the ArrayList of enemy pokemon from pokemon.txt*/
		Scanner inFile = new Scanner(new BufferedReader(new FileReader("Text Files\\pokemon.txt")));
    	
    	enemyPokemon = new ArrayList<Pokemon>();
    	int numPokemon = Integer.parseInt(inFile.nextLine());
    	for (int i = 0; i < numPokemon; i++)
    	{
    		enemyPokemon.add(new Pokemon(inFile.nextLine()));
    	}
    	
    	inFile.close();
	}
	
	private static void chooseOpponent()
	{	/*Chooses the new opponent and prints it dramatically*/
		Random rand = new Random();
		curOpponent = enemyPokemon.get(rand.nextInt(enemyPokemon.size()));
		Print.slowPrint("New opponent: ");
		pause(1000);
		Print.slowPrint(curOpponent.getName() + "\n");
	}
	
	public static int chooseOption()
	{	/*Asks the user what option they want to do and returns their choice*/
		Scanner kb = new Scanner(System.in);
		int numAttacks = curPokemon.getNumOfAttacks();
		while (true)
		{
			Print.slowPrint("\nChoose what you want to do:\n");
			Print.printOptions(curPokemon);
			int choice = kb.nextInt() - 1;
			if (choice < 0 || choice >= (numAttacks + 2) * 2)
			{
				Print.slowPrint("Invalid choice. Index out of range.\n");
				continue;
			}
			if (choice >= numAttacks + 2)			//If they want more info about something
			{
				choice -= (numAttacks + 2);
				
				if (choice == numAttacks) 			//If they want more info about retreat
				{
					Print.slowPrint("Retreat: The Pokemon is replaced with one of your remaining Pokemon\n");
				}
				else if (choice == numAttacks + 1)	//If they want more info about passing
				{
					Print.slowPrint("Pass: Your Pokemon does nothing for a turn while its energy recharges\n");
				}
				else								//If they want more info about an attack
				{
					Print.slowPrint(curPokemon.getAttack(choice).getStats());
				}
				continue;
			}
			if (validate("choose this option"))
				return choice;
		}
	}
	
	public static void replenishPlayerPk()
	{	/*Replenishes all of the player's pokemon's energy to 50
		and they all regain 20 HP*/
		for (int i = 0; i < playerPokemon.size(); i++)
		{
			playerPokemon.get(i).replenishEnergy();
			playerPokemon.get(i).recoverHP(20);
			playerPokemon.get(i).unDisable();
		}
	}
	
	public static boolean validate(String str)
	{	/*Asks the user if they are content with the choice they made.
		  If they are, it returns true and the game will continue, if not, 
		  they will have to reselect their option*/
		int choice;
		Scanner kb = new Scanner(System.in);
		while (true)
		{
			Print.slowPrint("\nAre you sure you want to " + str + "?\n(0: NO, 1: YES)\n");
			choice = kb.nextInt();
			if (choice != 0 && choice != 1)
			{
				Print.slowPrint("Invalid input. Index out of range.\n\n");
				continue;
			}
			break;
			
		}
		return choice == 1;
		
	}
	
	public static void battle(boolean playerTurn)
	{	/*The player's Pokemon battle the one enemy PK until it or all of the player's PK are defeated*/
		boolean playerStarts = playerTurn;
		double round = 1;
		Scanner kb = new Scanner(System.in);
		Random rand = new Random();
		while (true)
		{	
			if (round - (int) round < 0.5)		//If each Pokemon has had a turn (new round starts)
			{	Print.slowPrint("\n" + Print.repeat('-', 20) + "Round " + (int) round + Print.repeat('-', 20) + '\n');	}
			
			Print.printTurn(playerTurn, curOpponent);	//Prints who's turn it is
			
			if (playerStarts == playerTurn && ((int) round) > 1)	//If a round has been completed
			{	/*Pokemon recharge*/
				for (int i = 0; i < playerPokemon.size(); i++)
				{
					playerPokemon.get(i).recoverEnergy(10);			//All pokemon regain 10 energy
				}
				curOpponent.recoverEnergy(10);
			}
			
			if (playerTurn)		//If stunned it misses its turn and gets unstunned
			{
				if (curPokemon.isStunned())	//Can't attack if stunned
				{
					Print.slowPrint(curPokemon.getName() + " is stunned\n");
					curPokemon.unStun();
				}
				else
				{
					boolean successful = false;
					while (!successful)
					{
						int choice = chooseOption();
						if (choice < curPokemon.getNumOfAttacks()) 			//If they choose to attack
						{
							successful = curPokemon.attack(choice, curOpponent);	//Attempts to do the attack
							continue;												//If unsuccessful, retry
						}
						else if (choice == curPokemon.getNumOfAttacks())	//If they choose to retreat
						{
							Print.slowPrint(curPokemon.getName() + " retreats\n");
							chooseCurPokemon("\nChoose a replacement Pokemon\n");
						}
						else 		//If they choose to pass nothing happens
						{	
							Print.slowPrint(curPokemon.getName() + " passes its turn\n");
						}
						break;
					}
					
					if (curOpponent.isDead())	//If the enemy pokemon has been KOed, the battle is over
					{
						Print.slowPrint(curOpponent.getName() + " has been defeated!\nYou won the battle!\n\n");
						enemyPokemon.remove(curOpponent);	//Removes it from the arraylist of PK that the user still has to defeat
						return;								//Battle is over
					}
				}
				
				
			}
			else	//Opponents turn
			{
				if (curOpponent.isStunned())		//If stunned it misses its turn and gets unstunned
				{
					Print.slowPrint(curOpponent.getName() + " is stunned and must pass\n");
					curOpponent.unStun();
				}
				else
				{
					Integer[] optionNums = curOpponent.getAffordableAttackNums();
					
					if (optionNums.length == 0)		//If it cannot afford anything
					{	
						Print.slowPrint(curOpponent.getName() + " passes\n");	//Passes
					}
					else
					{
						int choice = rand.nextInt(optionNums.length);					//Chooses a random affordable attack
						curOpponent.attack(optionNums[choice].intValue(), curPokemon);	//Performs the attack
						
						if (curPokemon.isDead())	/*If the player's Pokemon has been KOed*/
						{
							Print.slowPrint(curPokemon.getName() + " has been defeated!\n\n");
							playerPokemon.remove(curPokemon);	//They can no longer use that Pokemon
							if (playerPokemon.isEmpty())		//If the have lost all their Pokemon
							{	return;	}						//Battle is over
							else								//If they have other Pokemon left
							{
								chooseCurPokemon("Choose a replacement Pokemon:\n"); //PLayer chooses a replacement and the round continues
							}
						}
					}
				}
			}
			
			playerTurn = !playerTurn;	//Switches turns
			round += 0.5;				//Each round lasts 2 iterations
		}
	}

    public static void main(String[] args) throws IOException
    {
    	makePokemon();												//Creates enemy Pokemon from the text file
    	    
  		//Print.printOutro(true);
    	    	
    	Print.slowPrint("Welcome to Pokemon Arena!\n\nChoose from the following:\n");
		Print.printPokemon(enemyPokemon);
		chooseStartingPokemon();									//Player chooses 4 pokemon from the enemy Pokemon, the 4 are then removed
		
		while (true)
		{
			if (validate(String.format("choose %s, %s, %s and %s", playerPokemon.get(0).getName(), playerPokemon.get(1).getName(),
																   playerPokemon.get(2).getName(), playerPokemon.get(3).getName())))		
				break;												/*If they are content with their choices*/
			Print.printPokemon(enemyPokemon);
			chooseStartingPokemon();
		}
		
		pause(1000);
		Print.slowPrint("\nLet the battle begin!\n\n");
		
		Random rand = new Random();
		
		while (!enemyPokemon.isEmpty() && !playerPokemon.isEmpty())
		{
			chooseOpponent();										//Gets opponent
			
			chooseCurPokemon("\nChoose a Pokemon\n");				//Gets the player's pokemon for the battle
			
			Print.printPokemon(curPokemon, curOpponent);
			
			
			boolean playerTurn = rand.nextBoolean();				//Chooses who goes first
			
			battle(playerTurn);
			
			replenishPlayerPk();									//All of the player's Pokemon reset to 50 energy
																	//And regain 20 HP
			
		}
		
		Print.printOutro(!playerPokemon.isEmpty());					//Prints the outro based on who won
		
	
		
    }
    
    
}