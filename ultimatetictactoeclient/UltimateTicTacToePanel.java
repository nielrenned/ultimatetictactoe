/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatetictactoeclient;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.swing.JPanel;
import ultimatetictactoegame.*;

/**
 *
 * @author Jacob
 */
public class UltimateTicTacToePanel extends JPanel implements MouseListener {
    final static int myWidth = 600, myHeight = 600;
    
    final int minorInset = 4;
    final int majorInset = 8;
    final int boardInset = 16;
    
    final Rectangle boardRect = new Rectangle(boardInset, boardInset, myWidth - 2*boardInset, myHeight - 2*boardInset);
    final Rectangle[][] minorTiles;
    
    UltimateTicTacToeGame game;
    UltimateTicTacToeClient parent;
    
    public UltimateTicTacToePanel(UltimateTicTacToeGame game, UltimateTicTacToeClient parent) {
        super();
        setPreferredSize(new Dimension(myWidth, myHeight));
        minorTiles = new Rectangle[9][9];
        for (int n = 0; n < 9; n++) {
            for (int m = 0; m < 9; m++) {
                minorTiles[n][m] = getMinorTileRect(n, m);
            }
        }
        this.game = game;
        this.parent = parent;
        addMouseListener(this);
    }
    
    public Rectangle getMajorTileRect(int n) {
        int r = n%3;
        int c = n/3;
        int x = boardRect.x;
        int y = boardRect.y;
        int w = boardRect.width;
        int h = boardRect.height;
        Rectangle rect = new Rectangle(x + r*w/3 + majorInset, y + c*h/3 + majorInset, w/3 - 2*majorInset, h/3 - 2*majorInset);
        return rect;
    }
    
    public Rectangle getMinorTileRect(int n, int m) {
        int r = m%3;
        int c = m/3;
        Rectangle majorRect = getMajorTileRect(n);
        int x = majorRect.x;
        int y = majorRect.y;
        int w = majorRect.width;
        int h = majorRect.height;
        Rectangle rect = new Rectangle(x + r*w/3 + minorInset, y + c*h/3 + minorInset, w/3 - 2*minorInset, h/3 - 2*minorInset);
        return rect;
    }
    
    public void drawBoardLines(Graphics g, Rectangle r) {
        g.drawLine(r.x+r.width/3, r.y, r.x+r.width/3, r.y+r.height);
        g.drawLine(r.x+2*r.width/3, r.y, r.x+2*r.width/3, r.y+r.height);
        g.drawLine(r.x, r.y+r.height/3, r.x+r.width, r.y+r.height/3);
        g.drawLine(r.x, r.y+2*r.height/3, r.x+r.width, r.y+2*r.height/3);
    }
    
    public void drawX(Graphics g, Rectangle r) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.red);
        g.drawLine(r.x, r.y, r.x+r.width, r.y+r.height);
        g.drawLine(r.x + r.width, r.y, r.x, r.y+r.height);
        g2.setColor(Color.black);
    }
    
    public void drawO(Graphics g, Rectangle r) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.blue);
        g.drawOval(r.x, r.y, r.width, r.height);
        g2.setColor(Color.black);
    }
    
    public void drawC(Graphics g, Rectangle r) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.green);
        g.drawArc(r.x, r.y, r.width, r.height, 45, 270);
        g2.setColor(Color.black);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        // Draw large board
        g2.setStroke(new BasicStroke(2));
        drawBoardLines(g, boardRect);
        g2.setStroke(new BasicStroke(1));
        
        int offset = 8;
        for (int n = 0; n < 9; n++) {
            Rectangle r = getMajorTileRect(n);
            
            if ((n == game.boardToPlay || game.boardToPlay == -1) && 
                    game.minorBoards[n].state == Board.NONE && game.majorBoard.state == Board.NONE) {
                g2.setColor(new Color(100, 255, 100, 127));
                g.fillRect(r.x, r.y, r.width, r.height);
                g2.setColor(Color.black);
            }
            
            g2.setStroke(new BasicStroke(5));
            if (game.minorBoards[n].state == Board.X) {
                drawX(g, r);
            }
            else if (game.minorBoards[n].state == Board.O) {
                drawO(g, r);
            }
            else if (game.minorBoards[n].state == Board.CAT) {
                drawC(g, r);
            }
            g2.setStroke(new BasicStroke(1));
            drawBoardLines(g, r);
            
            for (int m = 0; m < 9; m++) {
                int tileState = game.minorBoards[n].tiles[m];
                if (tileState == Board.X) {
                    drawX(g, getMinorTileRect(n, m));
                }
                else if (tileState == Board.O) {
                    drawO(g, getMinorTileRect(n, m));
                }
            }
        }
        
        if (game.majorBoard.state != Board.NONE) {
            g2.setStroke(new BasicStroke(10));
            if (game.majorBoard.state == Board.X) {
                drawX(g, boardRect);
            }
            else if (game.majorBoard.state == Board.O) {
                drawO(g, boardRect);
            }
            else if (game.majorBoard.state == Board.CAT) {
                drawC(g, boardRect);
            }
            g2.setStroke(new BasicStroke(1));
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        for (int board = 0; board < 9; board++) {
            for (int tile = 0; tile < 9; tile++) {
                if (minorTiles[board][tile].contains(e.getPoint())) {
                    parent.clicked(board, tile);
                }
            }
        }
    }
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
