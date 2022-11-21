/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ultimatetictactoegame;

import static java.lang.Integer.max;

/**
 *
 * @author Jacob
 */
public class Board {

    public int[] tiles;
    boolean catCounts;
    public int state;

    public static final int CAT = -1, NONE = 0, X = 1, O = 2;

    private final int[][] toCheck = {
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // rows
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // cols
        {0, 4, 8}, {2, 4, 6} // diagonals
    };

    public Board(boolean catCounts) {
        tiles = new int[9];
        this.catCounts = catCounts;
        this.state = NONE;
    }

    public void markTile(int n, int who) {
        tiles[n] = who;
        updateState();
    }

    public void updateState() {
        // check wins
        for (int[] t : toCheck) {
            int match = max(tiles[t[0]], max(tiles[t[1]], tiles[t[2]]));
            if ((tiles[t[0]] == match || (catCounts && tiles[t[0]] == CAT)) &&
                (tiles[t[1]] == match || (catCounts && tiles[t[1]] == CAT)) &&
                (tiles[t[2]] == match || (catCounts && tiles[t[2]] == CAT))) {
                if (match != 0) {
                    state = match;
                    return;
                }
            }
        }

        // check if full
        for (int t : tiles) {
            if (t == 0)
                return;
        }
        state = CAT;
    }
}
