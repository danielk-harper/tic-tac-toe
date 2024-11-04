package com.csc;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

// Your code goes here!
public class TicTacToe {
  // 0 = unset
  // 1 = x
  // 2 = o
  private int[] board = {
    0, 0, 0, 
    0, 0, 0, 
    0, 0, 0
  };
  
  private int current_player = 1;
  
  private boolean has_human_opponent = true;
  
  private void reset() {
    for(int i = 0; i < 9; i++) {
      board[i] = 0;
    }
    
    current_player = 1;
  }
  
  private String getPlayerString(int player) {
    if (player == 1) return "Player one";
    if (has_human_opponent) return "Player two";
    return "Computer";
  }
  
  private void printBoard() {
    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 3; col++) {
        int board_index = row * 3 + col;
        
        if (board[board_index] == 0) {
          System.out.print(String.format(" %d", board_index));
        } else if (board[board_index] == 1) {
          System.out.print(String.format(" X", board_index));
        } else {
          System.out.print(String.format(" O", board_index));
        }
        
        // if we are at a column before the last column
        if (col < 2) {
          System.out.print(" |");
        }
      }
      
      // if we are at or before the middle row.
      if (row < 2) {
        System.out.print("\n-----------\n");
      }
    }
  }
  
  private boolean isValidMove(int board_index) {
    if (board_index < 0 || board_index >= 9) return false;
    return board[board_index] == 0;
  }
  
  // returns an integer corresponding to the board index of the move.
  // guaranteed to be a valid move.
  private int questionPlayer() {
    if (!has_human_opponent && current_player == 2) {
      return randomMove();
    }
    
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.print(getPlayerString(current_player) + " - where would you like to move? ");
      
      if (scanner.hasNextInt()) {
        int move = scanner.nextInt();
        System.out.println();
        
        if (isValidMove(move)) return move;
        System.out.print("That move is invalid!");
      } else {
        while (!scanner.hasNextInt()) {
          scanner.next();
          System.out.println("Invalid input! Must be integer.");
        }
      }
    }
  }
  
  private int randomMove() {
    System.out.println("Computer thinking...");
    int randomNum = ThreadLocalRandom.current().nextInt(0, 10);
    while (!isValidMove(randomNum)) randomNum = ThreadLocalRandom.current().nextInt(0, 10);
    
    return randomNum;
  }
  
  // 0: no win
  // 1: player 1 win
  // 2: player 2 win
  private int checkRows() {
    for (int row = 0; row < 3; row++) {
      int initial = board[row * 3];
      boolean winning = true;
      
      // the row has an empty spot, so it can't be winning.
      if (initial == 0) continue;
      
      for (int col = 1; col < 3; col++) {
        // also can't be winning since different players in first row.
        if (board[row * 3 + col] != initial) {
          winning = false;
          break;
        }
      }
      
      if (winning) return initial;
    }
    
    return 0;
  }
  
  // 0: no win
  // 1: player 1 win
  // 2: player 2 win
  private int checkCols() {
    for (int col = 0; col < 3; col++) {
      int initial = board[col];
      boolean winning = true;
      
      // the row has an empty spot, so it can't be winning.
      if (initial == 0) continue;
      
      for (int row = 1; row < 3; row++) {
        // also can't be winning since different players in first row.
        if (board[row * 3 + col] != initial) {
          winning = false;
          break;
        }
      }
      
      if (winning) return initial;
    }
    
    return 0;
  }
  
  // 0: no win
  // 1: player 1 win
  // 2: player 2 win
  private int checkDiagonals() {
    // check first diagonal (top left to bottom right)
    int initial = board[0];
    boolean was_win = true;
    
    // if the top right is unset, then there cant be a win on this diagonal.
    if (initial != 0) {
      for (int rowcol = 1; rowcol < 3; rowcol++) {
        int cur_spot = board[rowcol * 3 + rowcol];

        if (initial != cur_spot) {
          was_win = false;
          break;
        }
      }
    } else {
      was_win = false;
    }
    
    if (was_win) {
      return initial;
    }
    
    // check second diagonal
    initial = board[2];
    was_win = true;
    
    // if the top right is unset, then there cant be a win on this diagonal.
    if (initial != 0) {
      for (int rowcol = 1; rowcol < 3; rowcol++) {
        int cur_spot = board[rowcol * 3 + (2 - rowcol)];

        if (initial != cur_spot) {
          was_win = false;
          break;
        }
      }
    } else {
      was_win = false;
    }
    
    if (was_win) {
      return initial;
    }
    
    return 0;
  }
  
  // returns true if there are no more unset cells.
  private boolean isFull() {
    for (int i = 0; i < 9; i++) {
      if (board[i] == 0) return false;
    }
    
    return true;
  }
  
  // 0: no win
  // 1: player 1 win
  // 2: player 2 win
  // 3: draw
  private int checkIfWin() {
    int i;
    
    i = checkRows();
    if (i != 0) return i;
    
    i = checkCols();
    if (i != 0) return i;
    
    i = checkDiagonals();
    if (i != 0) return i;
    
    if (isFull()) return 3;
    
    return 0;
  }
  
  // changes the board
  private void computeMove(int board_index) {
    if (current_player == 1) {
      board[board_index] = 1;
    } else {
      board[board_index] = 2;
    }
  }
  
  private int selectOption() {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.print("> ");
      
      if (scanner.hasNextInt()) {
        int selection = scanner.nextInt();
        System.out.println();
        
        if (selection == 1 || selection == 2) return selection;
        System.out.print("That selection is invalid!");
      } else {
        while (!scanner.hasNextInt()) {
          scanner.next();
          System.out.println("Invalid input! Must be integer.\n>");
        }
      }
    }
  }
  
  private void mainMenu() {
    System.out.println("Welcome to Tic-Tac-Toe! Who is playing? ");
    System.out.println("1. human vs. human");
    System.out.println("2. human vs. computer");
    
    int players = selectOption();
    if (players == 1) has_human_opponent = true;
    else has_human_opponent = false;
  }
  
  // returns true if the program should exit.
  private boolean exitMenu() {
    System.out.println("Would you like to play another game?");
    System.out.println("1. Yes");
    System.out.println("2. No");
    
    if (selectOption() == 1) {
      return false;
    }
    
    System.out.println("Have a good day!");
    return true;
  }
  
  private void printWinMessageOrSwap(int winning_player) {
    if (winning_player == 0) {
      // swap players
      current_player = (current_player % 2) + 1;
      return;
    }
    
    System.out.println();
    System.out.println();
    
    // draw
    if (winning_player == 3) {
      System.out.println("It's a draw!");
      return;
    }
    
    // player win
    System.out.println(getPlayerString(winning_player) + " wins!");
  }
  
  private void gameLoop() {
    mainMenu();
    
    while (true) {
      printBoard();
      
      while (true) {
        System.out.println();
        System.out.println();
        int move = questionPlayer();
        System.out.println();
        
        computeMove(move);
        printBoard();
        
        int winning_player = checkIfWin();
        printWinMessageOrSwap(winning_player);
        
        // if someone won
        if (winning_player != 0) {
          break;
        }
      }
      
      boolean should_exit = exitMenu();
      if (should_exit) break;
      reset();
      
    }
  }
  
  public static void main(String[] args)
  {
    TicTacToe t = new TicTacToe();
    
    t.gameLoop();
  }
}