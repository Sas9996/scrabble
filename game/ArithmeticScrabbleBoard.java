

package edu.kit.informatik.scrabble.game;

import edu.kit.informatik.scrabble.GameException;
import edu.kit.informatik.scrabble.entity.Expression;
import edu.kit.informatik.scrabble.entity.Orientation;
import edu.kit.informatik.scrabble.entity.Token;
import edu.kit.informatik.scrabble.ui.Main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Encapsulates a board for an {@link ArithmeticScrabbleGame}.
 *
 * @author Sara
 * @version 1.0
 */
public class ArithmeticScrabbleBoard implements GameBoard {
    /**
     * The size of this board.
     */
    public static final int BOARD_SIZE = 10;
    private final Token[][] board;

    /**
     * Instantiates a new {@link ArithmeticScrabbleBoard} with the {@link #BOARD_SIZE}.
     */
    public ArithmeticScrabbleBoard() {
        board = new Token[BOARD_SIZE][BOARD_SIZE];
    }

    private ArithmeticScrabbleBoard(final Token[][] board) {
        this.board = board;
    }

    /**
     * Checks whether or not the given tokens are part of at least one of the given expressions.
     *
     * @param tokens the tokens to check
     * @param results the expressions
     *
     * @return whether or not all given tokens are part of at least one of the expressions
     */
    private static boolean containsAllTokens(final Collection<Token> tokens, final Collection<Expression> results) {
        for (final Token token : tokens) {
            boolean tokenInResult = false;
            for (final Expression result : results) {
                if (result.containsToken(token)) {
                    tokenInResult = true;
                }
            }
            if (!tokenInResult) {
                // this token is not part in the result, thus it is invalid
                return false;
            }
        }
        return true;
    }

    @Override public boolean add(final int row, final int column, final Token token) {
        if (!(row >= 0 && row < board.length && column >= 0 && column < board[0].length)) {
            return false;
        }
        if (board[row][column] != null) {
            return false;
        }
        addToBoard(row, column, token);
        return true;
    }

    @Override public ArithmeticScrabbleBoard cloneBoard() {
        // System.arraycopy keeps references and a manual array copy is necessary
        final Token[][] clonedBoard = new Token[board.length][board[0].length];
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                clonedBoard[row][column] = board[row][column];
            }
        }
        return new ArithmeticScrabbleBoard(clonedBoard);
    }

    /**
     * @return a collection of all tokens placed on this board
     */
    private Collection<Token> getPlacedTokens() {
        final Collection<Token> placedTokens = new ArrayList<>();
        for (final Token[] row : board) {
            for (final Token token : row) {
                if (token != null) {
                    placedTokens.add(token);
                }
            }
        }
        return placedTokens;
    }

    private void addToBoard(final int row, final int column, final Token token) {
        board[row][column] = token;
    }

    @Override public Token get(final int row, final int column) {
        if (row < 0 || row > BOARD_SIZE - 1 || column < 0 || column > BOARD_SIZE - 1) {
            return null;
        }
        return board[row][column];
    }

    @Override public GameResult evaluate() {
        final Collection<Expression> results = new ArrayList<>();
        // search all rows for expressions and evaluate them
        for (final Orientation orientation : Orientation.values()) {
            searchDirection(results, orientation);
        }
        if (!containsAllTokens(getPlacedTokens(), results)) {
            throw new GameException("all tokens have to be part of an expression!");
        }
        return new GameResult(results.stream().map(Expression::evaluate).collect(Collectors.toList()), true);
    }

    private void searchDirection(final Collection<Expression> results, final Orientation orientation) {
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                growExpressionFromField(results, orientation, row, column);
            }
        }
    }

    private void growExpressionFromField(final Collection<Expression> results, final Orientation orientation,
            final int row, final int column) {
        if (board[row][column] != null) {
            final Expression expression = new Expression();
            growExpression(orientation, row, column, expression);
            if (expression.isValid()) {
                results.add(expression);
            }
        }
    }

    private void growExpression(final Orientation orientation, final int row, final int column,
            final Expression expression) {
        int tempRow = row;
        int tempCol = column;
        while (tempRow < board.length && tempCol < board[tempRow].length && board[tempRow][tempCol] != null) {
            expression.addToken(board[tempRow][tempCol]);
            tempRow += orientation.getDirectionalDifference()[0];
            tempCol += orientation.getDirectionalDifference()[1];
        }
    }

    @Override public String print() {
        final StringJoiner output = new StringJoiner(Main.LINE_SEPARATOR);
        for (final Token[] row : board) {
            final StringBuilder rowBuilder = new StringBuilder();
            for (final Token token : row) {
                rowBuilder.append(token != null
                        ? token.getTokenType().getStringPattern()
                        : Main.EMPTY_TOKEN_STRING);
            }
            output.add(rowBuilder.toString());
        }
        return output.toString();
    }

    @Override public String toString() {
        return print();
    }
}
