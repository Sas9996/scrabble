


package edu.kit.informatik.scrabble.game;

import edu.kit.informatik.scrabble.entity.Token;

/**
 * Encapsulates the functionality of a game board.
 *
 * @author Sara
 * @version 1.0
 */
public interface GameBoard {
    /**
     * Adds the given token at the given position on this board.
     *
     * @param row the row index where to add the token
     * @param column the column index where to add the token
     * @param token the token to add
     *
     * @return whether or not the given token could be added
     */
    boolean add(int row, int column, Token token);

    /**
     * Clones this board and returns a board with the same tokens placed on it but with different internal data
     * structures.
     *
     * @return a cloned board with the same tokens
     */
    ArithmeticScrabbleBoard cloneBoard();

    /**
     * Returns the {@link Token} placed on the given position.
     *
     * @param row the row index
     * @param column the column index
     *
     * @return the Token placed at the position with the given indices
     */
    Token get(int row, int column);

    /**
     * Calculates the game result for this board.
     *
     * @return the {@link GameResult} containing the game results
     */
    GameResult evaluate();

    /**
     * Calculates the String representation of this board as described in the assignment.
     *
     * @return the String representation of this board
     */
    String print();
}
