

package edu.kit.informatik.scrabble.game;

import edu.kit.informatik.scrabble.GameException;
import edu.kit.informatik.scrabble.entity.ExpressionResult;
import edu.kit.informatik.scrabble.entity.Orientation;
import edu.kit.informatik.scrabble.entity.Player;
import edu.kit.informatik.scrabble.entity.Token;
import edu.kit.informatik.scrabble.entity.TokenType;
import edu.kit.informatik.scrabble.ui.ExecutionState;
import edu.kit.informatik.scrabble.ui.Main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Encapsulates an arithmetic scribble game as described in the assignment.
 *
 * @author Sara
 * @version 1.0
 */
public class ArithmeticScrabble implements ArithmeticScrabbleGame {
    private final Player[] players;
    private GameBoard board;
    private int activePlayerIndex = 0;
    private boolean hasEnded = false;
    /**
     * The execution state of this database.
     */
    private ExecutionState executionState;

    /**
     * Instantiates a new {@link ArithmeticScrabble}.
     *
     * @param playerRepresentations Strings containing the token lists for the players
     *
     * @throws GameException occurs if the token lists are malformed
     */
    public ArithmeticScrabble(final String[] playerRepresentations) throws GameException {
        executionState = ExecutionState.RUNNING;
        players = new Player[playerRepresentations.length];
        initiatePlayers(playerRepresentations, playerRepresentations.length);
        board = new ArithmeticScrabbleBoard();
    }

    private static Player initiatePlayer(final int playerIndex, final String playerRepresentation) {
        final Player player = new Player(playerIndex);
        for (final char token : playerRepresentation.toCharArray()) {
            player.addToken(new Token(TokenType.parseFromString(String.valueOf(token)), player));
        }
        return player;
    }

    private Player getActivePlayer() {
        return players[activePlayerIndex];
    }

    private void changePlayer() {
        activePlayerIndex = activePlayerIndex == players.length - 1
                ? 0
                : activePlayerIndex + 1;
    }

    @Override public String place(final int row, final int column, final Orientation orientation,
            final Token... tokens) {
        int rowIndex = row;
        int columnIndex = column;
        executableOrThrow();
        final List<Token> playerTokens = getTokensFromPlayer(tokens);
        final GameBoard changedBoard = board.cloneBoard();
        for (final Token token : playerTokens) {
            if (!changedBoard.add(rowIndex, columnIndex, token)) {
                players[activePlayerIndex].addAllTokens(playerTokens);
                throw new GameException("token " + token + " could not be placed!");
            }
            rowIndex += orientation.getDirectionalDifference()[0];
            columnIndex += orientation.getDirectionalDifference()[1];
        }
        try {
            changedBoard.evaluate();
        } catch (final GameException exception) {
            players[activePlayerIndex].addAllTokens(playerTokens);
            throw exception;
        }
        if (changedBoard.evaluate().isValid()) {
            changePlayer();
            board = changedBoard;
            return Main.OK;
        }
        throw new GameException("invalid placement!");
    }

    private List<Token> getTokensFromPlayer(final Token[] tokens) {
        final List<Token> playerTokens = new ArrayList<>();
        for (final Token token : tokens) {
            final Token availableToken = players[activePlayerIndex].removeToken(token);
            if (availableToken == null) {
                players[activePlayerIndex].addAllTokens(playerTokens);
                throw new GameException(
                        "token " + token + " not found in this players bag " + bag(players[activePlayerIndex]));
            }
            playerTokens.add(availableToken);
        }
        return playerTokens;
    }

    private void initiatePlayers(final String[] playerRepresentations, final int playerCount) {
        for (int index = 0; index < playerCount; index++) {
            if (playerRepresentations[index] != null && playerRepresentations[index]
                    .matches(TokenType.getTokensPattern().pattern())) {
                players[index] = initiatePlayer(index + 1, playerRepresentations[index]);
            } else {
                throw new GameException("given player tokens are invalid " + playerRepresentations[index]);
            }
        }
    }

    @Override public boolean isActive() {
        return executionState == ExecutionState.RUNNING;
    }

    @Override public void quit() {
        executionState = ExecutionState.EXITED;
    }

    @Override public GameResult end() {
        executableOrThrow();
        hasEnded = true;
        return board.evaluate();
    }

    @Override public ExpressionResult score(final Player player) {
        return board.evaluate().getPlayerResult(player);
    }

    @Override public Collection<Token> bag(final Player player) {
        return player.getTokens();
    }

    @Override public String print() {
        return board.print();
    }

    private void executableOrThrow() throws GameException {
        if (hasEnded) {
            throw new GameException("this command cannot be executed after the game has ended");
        }
    }
}
