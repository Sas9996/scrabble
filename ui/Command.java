


package edu.kit.informatik.scrabble.ui;

import edu.kit.informatik.scrabble.entity.Expression;
import edu.kit.informatik.scrabble.entity.Orientation;
import edu.kit.informatik.scrabble.entity.Player;
import edu.kit.informatik.scrabble.entity.Token;
import edu.kit.informatik.scrabble.entity.TokenType;
import edu.kit.informatik.scrabble.game.ArithmeticScrabble;
import edu.kit.informatik.scrabble.game.ArithmeticScrabbleGame;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * List of available commands with their command line interaction expressions.
 *
 * @author Sara
 * @version 1.0
 */
public enum Command {

    /**
     * Places the given tokens at the given position with the given orientation if possible.
     */
    PLACE("place" + Main.COMMAND_SEPARATOR + TokenType.getTokensPattern() + Main.SEPARATOR + "(\\d+)" + Main.SEPARATOR
          + "(\\d+)" + Main.SEPARATOR + "(" + Orientation.getOrientationPattern() + ")") {
        @Override public String execute(final Matcher input, final ArithmeticScrabbleGame scrabble) {
            final String tokensString = input.group(Main.FIRST_PARAMETER_INDEX);
            final Token[] tokens = Token.parseTokensWithoutPlayer(tokensString);
            if (tokens.length > Expression.MINIMUM_EXPRESSION_LENGTH || tokens.length < 1) {
                return Main.ERROR + "invalid number of tokens " + tokens.length;
            }
            final int row = Integer.parseInt(input.group(Main.FIRST_PARAMETER_INDEX + 1));
            final int column = Integer.parseInt(input.group(Main.FIRST_PARAMETER_INDEX + 2));
            final Orientation orientation = Orientation.parseFromString(input.group(Main.FIRST_PARAMETER_INDEX + 3));
            return scrabble.place(row, column, orientation, tokens);
        }
    },

    /**
     * Finishes the game and returns the results.
     */
    END("end") {
        @Override public String execute(final Matcher input, final ArithmeticScrabbleGame scrabble) {
            return scrabble.end().toString();
        }
    },

    /**
     * Returns the score for the given player.
     */
    SCORE("score" + Main.COMMAND_SEPARATOR + Main.PLAYER_PATTERN) {
        @Override public String execute(final Matcher input, final ArithmeticScrabbleGame scrabble) {
            final Player player = Command.getPlayer(input);
            return String.valueOf(scrabble.score(player).getScore());
        }
    },

    /**
     * Returns the bag of tokens for the given player.
     */
    BAG("bag" + Main.COMMAND_SEPARATOR + Main.PLAYER_PATTERN) {
        @Override public String execute(final Matcher input, final ArithmeticScrabbleGame scrabble) {
            final Player player = Command.getPlayer(input);
            final StringBuilder output = new StringBuilder();
            scrabble.bag(player).forEach(output::append);
            return output.toString();
        }
    },

    /**
     * Returns the string representation of the game board.
     */
    PRINT("print") {
        @Override public String execute(final Matcher input, final ArithmeticScrabbleGame scrabble) {
            return scrabble.print();
        }
    },

    /**
     * Quits the program.
     */
    QUIT("quit") {
        @Override public String execute(final Matcher input, final ArithmeticScrabbleGame scrabble) {
            scrabble.quit();
            return null;
        }
    };

    /**
     * String constant containing an error message for the case that no command
     * could be found in this enum.
     */
    public static final String COMMAND_NOT_FOUND = Main.ERROR + "command not found!";

    /**
     * The pattern of this command.
     */
    private final Pattern pattern;

    /**
     * Instantiates a new command with the given String. The given String must be a
     * compilable {@link Pattern}.
     *
     * @param pattern the pattern of this command
     */
    Command(final String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    /**
     * Executes the command contained in the input if there is any, returns an error
     * message otherwise. If a command is found in the input, returns the result of
     * this input performed on the given playlist.
     *
     * @param input the line of input
     * @param scrabble the {@link ArithmeticScrabble} the command is executed on
     *
     * @return the result of the command execution, may contain error messages or be
     *         null if there is no output
     */
    public static String executeCommand(final String input, final ArithmeticScrabbleGame scrabble) {
        for (final Command command : Command.values()) {
            final Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                return command.execute(matcher, scrabble);
            }
        }
        return COMMAND_NOT_FOUND;
    }

    private static Player getPlayer(final Matcher input) {
        return Player.getPlayer(Integer.parseInt(String.valueOf(input.group(Main.FIRST_PARAMETER_INDEX).charAt(1))));
    }

    /**
     * Executes the given input on the given scrabble.
     *
     * @param input the line of input
     * @param scrabble the scrabble the command is executed on
     *
     * @return the result of the command execution, may contain error messages or be
     *         null if there is no output
     */
    abstract String execute(Matcher input, ArithmeticScrabbleGame scrabble);
}
