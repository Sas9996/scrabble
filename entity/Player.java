

package edu.kit.informatik.scrabble.entity;

import edu.kit.informatik.scrabble.GameException;
import edu.kit.informatik.scrabble.ui.Main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Encapsulates a player of this board game.
 *
 * @author Sara
 * @version 1.0
 */
public class Player {

    private static final Map<Integer, Player> AVAILABLE_PLAYERS = new TreeMap<>();
    private final int index;
    private final List<Token> tokens;

    /**
     * Instantiates a new player with the given index.
     *
     * @param index the index of this player
     *
     * @throws GameException occurs if a player with the same index shall be added
     */
    public Player(final int index) throws GameException {
        if (AVAILABLE_PLAYERS.containsKey(index)) {
            throw new GameException("attempted to initiate another player with the same index " + index);
        }
        this.index = index;
        tokens = new ArrayList<>();
        AVAILABLE_PLAYERS.put(index, this);
    }

    /**
     * The regular expression for a player with no limits.
     *
     * @return the pattern for a player input sequence
     */
    public static String getPlayerPattern() {
        return Main.PLAYER_CHARACTER + "\\d+";
    }

    /**
     * Returns the player with the given index if possible, null otherwise.
     *
     * @param index the index of the player
     *
     * @return the player with the given index if it exists, null otherwise
     */
    public static Player getPlayer(final int index) {
        return AVAILABLE_PLAYERS.get(index);
    }

    /**
     * Returns the index of the player with the highest index.
     *
     * @return the index of the player with the highest index
     */
    public static int getHighestPlayerIndex() {
        int playerIndex = 0;
        for (final Player player : AVAILABLE_PLAYERS.values()) {
            playerIndex = Math.max(playerIndex, player.getIndex());
        }
        return playerIndex;
    }

    /**
     * Removes the given token from this player if possible.
     *
     * @param token the token to remove
     *
     * @return whether or not the given token could be removed from the inventory of this player
     */
    public Token removeToken(final Token token) {
        if (tokens.contains(token)) {
            final Token availableToken = tokens.get(tokens.indexOf(token));
            tokens.remove(availableToken);
            return availableToken;
        }
        return null;
    }

    /**
     * Adds the given collection of tokens to this player.
     *
     * @param tokens the token collection to add
     *
     * @return whether or not all tokens could be added
     */
    public boolean addAllTokens(final Collection<Token> tokens) {
        return this.tokens.addAll(tokens);
    }

    /**
     * Adds the given token to this player if possible.
     *
     * @param token the token to add
     *
     * @return whether or not the token could be added
     *
     * @throws GameException occurs if the given token belongs to another player
     */
    public boolean addToken(final Token token) throws GameException {
        if (!token.getPlayer().equals(this)) {
            throw new GameException("tried to add a token from another player to " + "this one!");
        }
        return tokens.add(token);
    }

    /**
     * Returns the index of this player.
     *
     * @return the index of this player
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the tokens of this player.
     *
     * @return a sorted and unmodifiable collection of the tokens of this player
     */
    public Collection<Token> getTokens() {
        Collections.sort(tokens);
        return Collections.unmodifiableCollection(tokens);
    }

    @Override public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }
        final Player player = (Player) o;
        return index == player.index;
    }

    @Override public int hashCode() {
        return Objects.hash(index);
    }

    @Override public String toString() {
        return Main.PLAYER_CHARACTER + index;
    }
}
