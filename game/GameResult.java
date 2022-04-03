
package edu.kit.informatik.scrabble.game;

import edu.kit.informatik.scrabble.entity.ExpressionResult;
import edu.kit.informatik.scrabble.entity.Player;
import edu.kit.informatik.scrabble.ui.Main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeSet;

/**
 * Encapsulates the result of an arithmetic scrabble game. Use {@link #getPlayerResult(Player)} and
 * {@link #getResult()} to get the scores for the different players.
 *
 * @author Sara
 * @version 1.0
 */
public class GameResult {

    /**
     * The score for an empty result.
     */
    public static final int EMPTY_RESULT = 0;
    private final Collection<ExpressionResult> results;
    private final boolean isValid;

    /**
     * Instantiates a new {@link GameResult} with the given parameters. Does not evaluate them. Use
     * {@link #getResult()} or {@link #getPlayerResult(Player)} to evaluate this {@link GameResult}.
     *
     * @param results the collection of {@link ExpressionResult} containing the expression evaluations
     * @param isValid whether or not this {@link GameResult} is valid
     */
    public GameResult(final Collection<ExpressionResult> results, final boolean isValid) {
        this.results = results;
        this.isValid = isValid;
    }

    /**
     * Fills the given map with empty {@link ExpressionResult} for all players that have no score.
     *
     * @param maximum the maximum(included) to which the given map is filled with {@link #EMPTY_RESULT}
     * @param resultMap the map containing the results as a mapping from the id to the {@link ExpressionResult}
     *         for a
     *         player
     *
     * @return the filled map as a collection
     */
    private static Collection<ExpressionResult> fillMapToMaximum(final int maximum,
            final Map<Integer, ExpressionResult> resultMap) {
        for (int index = 0; index <= maximum; index++) {
            if (!resultMap.containsKey(index)) {
                if (Player.getPlayer(index) != null) {
                    resultMap.put(index, new ExpressionResult(Player.getPlayer(index), EMPTY_RESULT, true));
                }
            }
        }
        return new TreeSet<>(resultMap.values());
    }

    /**
     * Calculates the score for the given {@link Player} and returns it. If no player is found, returns null.
     *
     * @param player a player which participated in this {@link ArithmeticScrabble}
     *
     * @return the score of the given player
     */
    public ExpressionResult getPlayerResult(final Player player) {
        final Collection<ExpressionResult> results = getResult();
        for (final ExpressionResult result : results) {
            if (result.getPlayer().equals(player)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Calculates the Scores for all players and returns them as a collection.
     *
     * @return a Collection with the results for all players
     */
    public Collection<ExpressionResult> getResult() {
        final Map<Integer, ExpressionResult> collectedResults = new HashMap<>();
        for (final ExpressionResult result : results) {
            if (result != null && result.isValid() && result.getPlayer() != null) {
                if (!collectedResults.containsKey(result.getPlayer().getIndex())) {
                    collectedResults.put(result.getPlayer().getIndex(), result);
                } else {
                    collectedResults.get(result.getPlayer().getIndex()).addScore(result);
                }
            }

        }
        return fillMapToMaximum(Player.getHighestPlayerIndex(), collectedResults);
    }

    @Override public String toString() {
        final Collection<ExpressionResult> playerScores = getResult();
        final StringJoiner output = new StringJoiner(Main.LINE_SEPARATOR);
        playerScores.forEach(result -> output.add(String.valueOf(result.getScore())));
        // add the player representation as the last line of the output
        final List<ExpressionResult> sortedResults = new ArrayList<>(playerScores);
        sortedResults.sort(Comparator.comparing(ExpressionResult::getScore).reversed());
        if (sortedResults.size() > 1) {
            final Iterator<ExpressionResult> iterator = sortedResults.iterator();
            if (iterator.next().getScore() == iterator.next().getScore()) {
                output.add(Main.DRAW);
            } else {
                output.add(sortedResults.iterator().next().getPlayer().toString() + Main.COMMAND_SEPARATOR + Main.WINS);
            }
        } else {
            output.add(sortedResults.iterator().next().getPlayer().toString() + Main.COMMAND_SEPARATOR + Main.WINS);
        }
        return output.toString();
    }

    /**
     * Returns whether or not this result is valid.
     *
     * @return whether or not this {@link GameResult} is valid
     */
    public boolean isValid() {
        return isValid;
    }
}
