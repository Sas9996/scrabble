


package edu.kit.informatik.scrabble.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Encapsulates an expression consisting of tokens.
 *
 * @author Sara
 * @version 1.0
 */
public class Expression {
    public static final int MINIMUM_EXPRESSION_LENGTH = 3;
    private final List<Token> tokens;
    private ExpressionResult result;

    /**
     * Instantiates a new Expression.
     */
    public Expression() {
        tokens = new ArrayList<>();
    }

    /**
     * Calculates the player that owns this expression by owning the most tokens used.
     *
     * @param playerTokenCounts the number of tokens from the different players
     *
     * @return the index of the player with the most tokens
     */
    private static int getOwnerIndex(final int[] playerTokenCounts) {
        int ownerIndex = 0;
        int playerTokenCount = 0;
        for (int index = 0; index < playerTokenCounts.length; index++) {
            if (playerTokenCounts[index] > playerTokenCount) {
                playerTokenCount = playerTokenCounts[index];
                ownerIndex = index;
            }
        }
        return ownerIndex;
    }

    /**
     * Adds the given token to this expression.
     *
     * @param token the token to add to this expression
     *
     * @return whether or not the token could be added
     */
    public boolean addToken(final Token token) {
        return tokens.add(token);
    }

    /**
     * Returns whether or not this expression is valid.
     *
     * @return whether or not this expression is valid (and thus a result can be calculated)
     */
    public boolean isValid() {
        if (result == null) {
            result = evaluate();
        }
        return result != null && result.isValid();
    }

    /**
     * Calculates the result of this expression.
     *
     * @return the result of the calculation of this expression, null if the expression is not valid
     */
    public ExpressionResult evaluate() {
        final int playerCount = Player.getHighestPlayerIndex();
        final Stack<Token> expression = new Stack<>();
        if (tokens.size() < MINIMUM_EXPRESSION_LENGTH) {
            return null;
        }
        if (evaluateExpression(expression)) {
            return null;
        }
        if (expression.size() != 1) {
            return null;
        }
        final int[] playerTokenCounts = new int[playerCount + 1];
        for (final Token token : tokens) {
            playerTokenCounts[token.getPlayer().getIndex()]++;
        }
        final int ownerIndex = getOwnerIndex(playerTokenCounts);
        return new ExpressionResult(Player.getPlayer(ownerIndex), expression.pop().getTokenType().getValue(), true);
    }

    /**
     * Evaluates the given stack.
     *
     * @param expression the stack containing the tokens
     *
     * @return whether or not an error occurred during evaluation
     */
    private boolean evaluateExpression(final Stack<Token> expression) {
        for (final Token token : tokens) {
            if (token.getTokenType().isOperator()) {
                if (expression.size() < 2) {
                    return true;
                }
                // the operands are pushed in reverse order so we have to access them reversed
                final Token secondOperand = expression.pop();
                final Token firstOperand = expression.pop();
                TokenType.ARBITRARY.setValue(
                        token.getTokenType().calculate(firstOperand.getTokenType(), secondOperand.getTokenType()));
                expression.push(new Token(TokenType.ARBITRARY, null));
            } else {
                expression.push(token);
            }
        }
        return false;
    }

    /**
     * Checks whether or not the given token is part of this expression.
     *
     * @param token the token which may be part of this expression
     *
     * @return whether or not the given token is part of this expression, using {@link Token#equalsCompletely(Token)}.
     */
    public boolean containsToken(final Token token) {
        for (final Token availableToken : tokens) {
            if (availableToken.equalsCompletely(token)) {
                return true;
            }
        }
        return false;
    }

    @Override public String toString() {
        return tokens.toString();
    }
}
