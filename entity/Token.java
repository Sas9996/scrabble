

package edu.kit.informatik.scrabble.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a token with a type, a player and a unique id.
 *
 * @author Sara
 * @version 1.0
 */
public class Token implements Comparable<Token> {
    private static int identifier = 0;
    private final TokenType tokenType;
    private final Player player;
    private final int id;

    /**
     * Instantiates a new Token with the given arguments.
     *
     * @param tokenType the {@link TokenType} of this token
     * @param player the {@link Player} that own this token
     */
    public Token(final TokenType tokenType, final Player player) {
        this.tokenType = tokenType;
        this.player = player;
        id = identifier++;
    }

    /**
     * Instantiates a new Token with the given arguments.
     *
     * @param tokenType the {@link TokenType} of this token
     */
    private Token(final TokenType tokenType) {
        player = null;
        this.tokenType = tokenType;
        id = identifier++;
    }

    /**
     * Parses the given token String to a token array. Uses {@link TokenType#getTokensPattern()} to determine whether
     * or not the given String is valid.
     *
     * @param tokensString the string containing the token representations
     *
     * @return an array containing the parsed tokens, null if the given String is malformed
     */
    public static Token[] parseTokensWithoutPlayer(final String tokensString) {
        if (tokensString == null || !tokensString.matches(TokenType.getTokensPattern().pattern())) {
            return null;
        }
        final Collection<Token> tokens = new ArrayList<>();
        for (final char tokenString : tokensString.toCharArray()) {
            tokens.add(new Token(TokenType.parseFromString(String.valueOf(tokenString))));
        }
        return tokens.toArray(new Token[0]);
    }

    /**
     * The token type of this token.
     *
     * @return the {@link TokenType} if this token
     */
    public TokenType getTokenType() {
        return tokenType;
    }

    /**
     * The player who owns this token.
     *
     * @return the {@link Player} owning this token
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Method to compare Tokens based on all of their attributes.
     *
     * @param token the token to compare to
     *
     * @return whether or not the given token and this one equal completely, thus having the same {@link TokenType},
     *         the same {@link Player} and the same unique id.
     */
    public boolean equalsCompletely(final Token token) {
        if (token == null) {
            return false;
        }
        if (this == token) {
            return true;
        }
        return tokenType.equals(token.tokenType) && Objects.equals(player, token.player) && id == token.id;
    }

    @Override public int compareTo(final Token o) {
        return tokenType.compareTo(o.tokenType);
    }

    @Override public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Token)) {
            return false;
        }
        final Token token = (Token) o;
        return tokenType == token.tokenType;
    }

    @Override public int hashCode() {
        return Objects.hash(tokenType);
    }

    @Override public String toString() {
        return tokenType.getStringPattern();
    }
}
