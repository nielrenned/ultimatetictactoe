/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatetictactoeserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import ultimatetictactoegame.Board;
import ultimatetictactoegame.UltimateTicTacToeGame;

/**
 *
 * @author Jacob
 */
public class UltimateTicTacToeServer {
    
    UltimateTicTacToeGame game;
    
    ServerSocket server;
    Socket p1, p2;
    PrintWriter p1Out, p2Out;
    BufferedReader p1In, p2In;
    Random r;
    
    BufferedReader currPlayer;
    PrintWriter otherPlayer;
    
    public UltimateTicTacToeServer() {
        
        r = new Random();
        int firstPlayer = (r.nextInt() % 2 == 0 ? Board.X : Board.O);
        
        game = new UltimateTicTacToeGame(false, firstPlayer);
        
        try (java.util.Scanner s = new java.util.Scanner(new java.net.URL("https://api.ipify.org").openStream(), "UTF-8").useDelimiter("\\A")) {
            System.out.println("IP address is " + s.next());
        } catch (java.io.IOException e) {
            System.out.println("Couldn't get IP Address. Exiting.");
            System.exit(1);
        }
        
        
        try {
            server = new ServerSocket(0);
        } catch (IOException ex) {
            System.out.println("Couldn't create server. Exiting.");
            System.exit(1);
        }
        System.out.println(server.getLocalPort());
    }
    
    public void start() {
        try {
            p1 = server.accept();
            System.out.println("Player 1 Connected.");
            p1Out = new PrintWriter(p1.getOutputStream(), true);
            p1In = new BufferedReader(new InputStreamReader(p1.getInputStream()));
        } catch (IOException e) {
            System.out.println("Couldn't get player 1. Exiting");
            System.exit(1);
        }
        
        try {
            p2 = server.accept();
            System.out.println("Player 2 Connected.");
            p2Out = new PrintWriter(p2.getOutputStream(), true);
            p2In = new BufferedReader(new InputStreamReader(p2.getInputStream()));
        } catch (IOException e) {
            System.out.println("Couldn't get player 2. Exiting");
            System.exit(1);
        }
        
        p1Out.println((game.whoseTurn == Board.X ? Board.X : Board.O));
        p2Out.println((game.whoseTurn == Board.X ? Board.O : Board.X));
        
        currPlayer = (game.whoseTurn == Board.X ? p1In : p2In);
        otherPlayer = (game.whoseTurn == Board.X ? p2Out : p1Out);
        
        while(game.majorBoard.state == Board.NONE) {
            String move;
            try {
                move = currPlayer.readLine();
            } catch(IOException e) {
                System.out.println("Error.");
                continue;
            }
            int board = Integer.parseInt(move.substring(0, 1));
            int tile = Integer.parseInt(move.substring(1, 2));
            game.markTile(board, tile);
            
            otherPlayer.println(move);
            
            currPlayer = (currPlayer == p1In ? p2In : p1In);
            otherPlayer = (otherPlayer == p1Out ? p2Out : p1Out);
        }
        System.out.println("Game Over");
    }
    
    public static void main(String[] args) {
        UltimateTicTacToeServer server = new UltimateTicTacToeServer();
        server.start();
    }
}
