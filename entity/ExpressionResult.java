

package edu.kit.informatik.scrabble.entity;

import edu.kit.informatik.scrabble.GameException;

import java.util.Objects;

/**
 * Encapsulates the result of the evaluation of an {@link Expression}. Consists of a {@link Player}, a score and
 * whether or not the expression was valid.
 *
 * @author Sara
 * @version 1.0
 */
public class ExpressionResult implements Comparable<ExpressionResult> {

    private final Player player;
    private final boolean valid;
    private int score;

    /**
     * Instantiates a new {@link ExpressionResult} with the given parameters.
     *
     * @param player the player who owned the most tokens in the expression
     * @param score the score the player reached
     * @param valid whether or not the expression was valid
     */
    public ExpressionResult(final Player player, final int score, final boolean valid) {
        this.player = player;
        this.score = score;
        this.valid = valid;
    }

    /**
     * The player who this result belongs to.
     *
     * @return the player that this expression result belongs to
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * The score of this result.
     *
     * @return the score of this result
     */
    public int getScore() {
        return score;
    }

    /**
     * Whether or not this result is valid.
     *
     * @return whether or not this expression result is valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Adds the score of the given {@link ExpressionResult} to this one.
     *
     * @param result another {@link ExpressionResult} to add to this one
     *
     * @throws GameException occurs if the given {@link ExpressionResult} belongs to another player
     */
    public void addScore(final ExpressionResult result) throws GameException {
        if (!player.equals(result.player)) {
            throw new GameException("tried to add a result from a different player!");
        }
        score += result.score;
    }

    @Override public int compareTo(final ExpressionResult o) {
        return player.getIndex() - o.player.getIndex();
    }

    @Override public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExpressionResult)) {
            return false;
        }
        final ExpressionResult result = (ExpressionResult) o;
        return Objects.equals(player, result.player);
    }

    @Override public int hashCode() {
        return Objects.hash(player);
    }
}
