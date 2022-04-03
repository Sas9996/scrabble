
 

package edu.kit.informatik.scrabble.ui;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.scrabble.GameException;
import edu.kit.informatik.scrabble.entity.TokenType;
import edu.kit.informatik.scrabble.game.ArithmeticScrabble;
import edu.kit.informatik.scrabble.game.ArithmeticScrabbleGame;

/**
 * Main class for the first task of the fifth assignment. Contains the entry
 * point and input/output constants.
 *
 * @author Sara
 * @version 1.0
 */
public class Main {
    /**
     * The separator between a command and the parameters.
     */
    public static final String COMMAND_SEPARATOR = " ";
    /**
     * Index of the first parameter.
     */
    public static final int FIRST_PARAMETER_INDEX = 1;
    /**
     * The separator between lines of output.
     */
    public static final String LINE_SEPARATOR = System.lineSeparator();
    /**
     * The separator between different parameters for the command line input.
     */
    public static final String SEPARATOR = ";";
    /**
     * An empty string used for some outputs.
     */
    public static final String EMPTY_STRING = "";
    /**
     * The start of an output string for a failed operation.
     */
    public static final String ERROR = "Error, ";
    /**
     * The output string for a succeeded operation.
     */
    public static final String OK = "OK";
    /**
     * The String pattern for the player output.
     */
    public static final String PLAYER_CHARACTER = "P";
    /**
     * String describing the pattern of a valid player index.
     */
    public static final String PLAYER_PATTERN = "(" + PLAYER_CHARACTER + "[" + 1 + "|" + 2 + "]" + ")";
    /**
     * The string added to the player representation for the output of the
     * {@link ArithmeticScrabbleGame#end()} command.
     */
    public static final String WINS = "wins";
    /**
     * The string used for the cmd output in case of a draw in the
     * {@link ArithmeticScrabbleGame#end()} command.
     */
    public static final String DRAW = "draw";
    /**
     * Representation for an empty token.
     */
    public static final char EMPTY_TOKEN_STRING = '#';

    /**
     * Private constructor to avoid object generation.
     *
     * @deprecated Utility-class constructor.
     */
    @Deprecated private Main() {
        throw new AssertionError("Utility class constructor.");
    }

    /**
     * Entry point to the program. Checks the given input and produces corresponding
     * output.
     *
     * @param args the two player token lists, format specified in {@link TokenType#getTokensPattern()}
     */
    public static void main(final String[] args) {
        if (args.length != 2) {
            Terminal.printError("number of players not supported!");
            return;
        }
        final ArithmeticScrabble scrabble;
        try {
            scrabble = new ArithmeticScrabble(args);
        } catch (final GameException exception) {
            Terminal.printError(exception.getMessage());
            return;
        }
        while (scrabble.isActive()) {
            final String input = Terminal.readLine();
            try {
                final String output = Command.executeCommand(input, scrabble);
                if (output != null) {
                    Terminal.printLine(output);
                }
            } catch (final GameException exception) {
                Terminal.printError(exception.getMessage());
            }
        }
    }
}
