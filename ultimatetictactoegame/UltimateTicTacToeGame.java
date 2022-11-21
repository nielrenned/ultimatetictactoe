/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatetictactoegame;

import java.util.Stack;

/**
 *
 * @author Jacob
 */
public class UltimateTicTacToeGame {
    public int whoseTurn;
    public int boardToPlay;
    
    public Board[] minorBoards;
    public Board majorBoard;
    
    Stack<int[]> moves;
    
    public UltimateTicTacToeGame(boolean catCounts, int firstPlayer) {
        whoseTurn = firstPlayer;
        boardToPlay = -1;
        minorBoards = new Board[9];
        for (int i = 0; i < 9; i++) {
            minorBoards[i] = new Board(false);
        }
        majorBoard = new Board(catCounts);
        moves = new Stack();
    }
    
    public boolean markTile(int board, int tile) {
        // @Refactor
        if (board == boardToPlay || (boardToPlay == -1 && minorBoards[board].state == Board.NONE) && majorBoard.state == Board.NONE) {
            Board b = minorBoards[board];
            if (b.tiles[tile] == 0) {
                // Record move
                int[] move = new int[3];
                move[0] = board; move[1] = tile; move[2] = boardToPlay;
                moves.add(move);
                
                // Change state
                b.markTile(tile, whoseTurn);
                majorBoard.markTile(board, b.state);
                whoseTurn = (whoseTurn == Board.X ? Board.O : Board.X);
                boardToPlay = tile;
                if (minorBoards[boardToPlay].state != Board.NONE)
                    boardToPlay = -1;
                return true;
            }
            return false;
        }
        return false;
    }
    
    public void undo() {
        if (moves.empty())
            return;
        int[] prevMove = moves.pop();
        int board = prevMove[0], tile = prevMove[1], oldBoard = prevMove[2];
        minorBoards[board].markTile(tile, 0);
        majorBoard.markTile(board, minorBoards[board].state);
        boardToPlay = oldBoard;
        whoseTurn = (whoseTurn == Board.X ? Board.O : Board.X);
    }
}
