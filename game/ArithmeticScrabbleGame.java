

package edu.kit.informatik.scrabble.game;

import edu.kit.informatik.scrabble.entity.ExpressionResult;
import edu.kit.informatik.scrabble.entity.Orientation;
import edu.kit.informatik.scrabble.entity.Player;
import edu.kit.informatik.scrabble.entity.Token;

import java.util.Collection;

/**
 * Encapsulates an arithmetic scrabble game with different commands.
 *
 * @author Sara
 * @version 1
 */
public interface ArithmeticScrabbleGame extends Executable {
    /**
     * Places the given tokens with the given orientation at the given position.
     *
     * @param row the row the tokens placed start at
     * @param column the column the tokens placed start at
     * @param orientation the orientation of the tokens
     * @param token the tokens to be placed
     *
     * @return a String containing whether or not the placement succeeded
     */
    String place(int row, int column, Orientation orientation, Token... token);

    /**
     * Finishes the game and calculates the results.
     *
     * @return result of the game
     */
    GameResult end();

    /**
     * Calculates the score for the given player.
     *
     * @param player a player of this {@link ArithmeticScrabble}
     *
     * @return the score of the given player
     */
    ExpressionResult score(Player player);

    /**
     * Returns the tokens for the given player.
     *
     * @param player a player of this {@link ArithmeticScrabble}
     *
     * @return the bag of tokens of the given player
     */
    Collection<Token> bag(Player player);

    /**
     * Returns the String representation for this game.
     *
     * @return the String representation for this game
     */
    String print();
}
