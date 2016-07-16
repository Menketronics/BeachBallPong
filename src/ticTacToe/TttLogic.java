package ticTacToe;

import java.io.Serializable;

public class TttLogic implements Serializable{
	
	private static final long serialVersionUID = 1L;

	    // The TTT 3 x 3 will be filled with -1 (AVAILABLE) to start with
	    // There after, entries will receive either a the player's index (0 or 1). 
		private static final int AVAILABLE = -1;

		// GameControl Status values
		private static final int GAME_TIE = 2;
		private static final int GAME_IN_PROGRESS=-1;

		// Note that gameWinner will take on the values of:
		// GAME_TIE
		// 0 if the first player is the Winner
		// 1 if the second player is the Winner
		private int gameWinner = GAME_IN_PROGRESS;

		
		private int[][] board = new int[3][3]; 

		private int nextTurn = 0;
		private int startingTurn = 1;
		
		// Used to allow the User interface to fill in appropriate label for each cell
		public char getButtonLabel(int row, int col)
		{
			int value = board[row][col];
			if (value == AVAILABLE) 
				return ' ';
			if (value == 0)
				return 'O';
			else
				return 'X';
		}
		
		// Used to find out if the game is complete
		public boolean gameInProgress()
		{
			if (gameWinner == GAME_IN_PROGRESS)
				return true;
			else
				return false;
		}
		
		public String getGameStatus(int yourIndex)
		{
			if (yourIndex == gameWinner)
				return "You Win !!!";
			if (gameWinner >= 0 && gameWinner <=1)
				return "You Lose !!!";
			if (gameWinner == GAME_TIE)
				return "Tie";
			return "Game in Progress";
		}
		
		// New Game
		void clearBoard()
		{
			gameWinner = GAME_IN_PROGRESS;
			// Alternate who starts if multiple games are played
			if (startingTurn != 0)
				startingTurn = 0;
			else
				startingTurn = 1;

			nextTurn = startingTurn;

			for (int i=0; i < 3; i++)
				for (int j=0; j < 3; j ++)
					board[i][j] = AVAILABLE;
		}
		
		// Find out if it is your turn
		public boolean isTurn(int clientIndex)
		{
			if (clientIndex == nextTurn)
				return true;
			else 
				return false;
		}
		
		// Can you select this button
		public boolean checkAvailability(int clientIndex, int row,  int col)
		{
			if (!isTurn(clientIndex))
			{
				return false;
			}
			if (row < 0 || row > 2 || col < 0 || col > 2)
			{
				return false;
			}
			if (board[row][col] == AVAILABLE)
				return true;
			else
				return false;
			
		}
		
		// makeSelection allows a player to make a selection if possible.
		// Error messages are printed if it is a bad selection.
		// If it is an OK selection, then the "Turn" is switched and the 
		// gameWinner status is updated
		
		public boolean makeSelection(int clientIndex, int row, int col)
		{
			if (!isTurn(clientIndex))
			{
				System.out.println("Out of turn: " + clientIndex);
				return false;
			}
			if (row < 0 || row > 2 || col < 0 || col > 2)
			{
				System.out.println("indices out of bounds: "+ row + ": " + col);
				return false;
			}
			if (board[row][col] != AVAILABLE)
			{
				System.out.println(" Choice is already selected");
				return false;
			}
			// an OK selection
			board[row][col]= nextTurn;
			
			// change the turn
			nextTurn = (nextTurn +1) % 2;
			
			// update the game status
			gameWinner = scoreGame();
			return true;
		}
		
		
        // This routine returns:
		// GAME_IN_PROGRESS - if the game hasn't finished
		// GAME_TIE - if there are no more empty cells
		// 0 - if the first player wins
		// 1 - if the second player wins
		
		private int scoreGame()
		{
			// Check Rows For Winner
			for (int i=0; i < 3; i++)
			{
				int firstCol = board[i][0];
				if (firstCol != AVAILABLE && firstCol == board[i][1] && firstCol == board[i][2])
				{
					return firstCol;
				}
			}

			// Check Cols For Winner
			for (int i=0; i < 3; i++)
			{
				int firstRow = board[0][i];
				if (firstRow != AVAILABLE && firstRow == board[1][i] && firstRow == board[2][i])
				{
					return firstRow;
				}
			}

			// Check Diagonals
			int center = board[1][1];
			if (center != AVAILABLE)
				if (  (center == board[0][0] && center == board[2][2]) ||
						(center == board[0][2] && center == board[2][0]))
				{
					return center;
				}

			// Check to see if all available spaces are filled
			for (int i=0; i < 3; i++)
			{
				for (int j=0; j < 3; j++)
					if (board[i][j] == AVAILABLE) return GAME_IN_PROGRESS; // GAME_IN_PROGRESS
			}
			return GAME_TIE;
		}


}
