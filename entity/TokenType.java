

package edu.kit.informatik.scrabble.entity;

import edu.kit.informatik.scrabble.GameException;

import java.util.regex.Pattern;

/**
 * Represents all possible token types for the arithmetic scrabble game.
 *
 * @author Sara
 * @version 1.0
 */
public enum TokenType {
    /**
     * The number 0.
     */
    ZERO('0'),
    /**
     * The number 1.
     */
    ONE('1'),
    /**
     * The number 2.
     */
    TWO('2'),
    /**
     * The number 3.
     */
    THREE('3'),
    /**
     * The number 4.
     */
    FOUR('4'),
    /**
     * The number 5.
     */
    FIVE('5'),
    /**
     * The number 6.
     */
    SIX('6'),
    /**
     * The number 7.
     */
    SEVEN('7'),
    /**
     * The number 8.
     */
    EIGHT('8'),
    /**
     * The number 9.
     */
    NINE('9'),
    /**
     * The operator +.
     */
    PLUS('+') {
        @Override public int calculate(final TokenType firstOperand, final TokenType secondOperand)
                throws GameException {
            return firstOperand.getValue() + secondOperand.getValue();
        }

        @Override public int getIntPattern() throws GameException {
            throw new GameException("tried to get a value from an operator");
        }

        @Override public boolean isOperand() {
            return false;
        }

        @Override public boolean isOperator() {
            return true;
        }
    },
    /**
     * The operator -.
     */
    MINUS('-') {
        @Override public int calculate(final TokenType firstOperand, final TokenType secondOperand)
                throws GameException {
            return firstOperand.getValue() - secondOperand.getValue();
        }

        @Override public int getIntPattern() throws GameException {
            throw new GameException("tried to get a value from an operator");
        }

        @Override public boolean isOperand() {
            return false;
        }

        @Override public boolean isOperator() {
            return true;
        }
    },
    /**
     * The operator *.
     */
    TIMES('*') {
        @Override public int calculate(final TokenType firstOperand, final TokenType secondOperand)
                throws GameException {
            return firstOperand.getValue() * secondOperand.getValue();
        }

        @Override public int getIntPattern() throws GameException {
            throw new GameException("tried to get a value from an operator");
        }

        @Override public boolean isOperand() {
            return false;
        }

        @Override public boolean isOperator() {
            return true;
        }
    },
    /**
     * An arbitrary operand that can hold any integer value.
     */
    ARBITRARY('a') {
        private int value;

        @Override public void setValue(final int value) throws GameException {
            this.value = value;
        }

        @Override public int getValue() {
            return value;
        }
    };

    private final char pattern;

    /**
     * Instantiates a new {@link TokenType} with the given pattern.
     *
     * @param pattern the pattern of the token type
     */
    TokenType(final char pattern) {
        this.pattern = pattern;
    }

    /**
     * Returns the regular expression for one token type. The {@link #MINUS} operator is escaped as it counts as an
     * empty range.
     *
     * @return the regular expression pattern for one token type
     */
    public static String getTokenTypePattern() {
        // be sure to escape the standard minus pattern as it is an empty range
        return "[" + ZERO.pattern + "|" + ONE.pattern + "|" + TWO.pattern + "|" + THREE.pattern + "|" + FOUR.pattern
               + "|" + FIVE.pattern + "|" + SIX.pattern + "|" + SEVEN.pattern + "|" + EIGHT.pattern + "|" + NINE.pattern
               + "|" + PLUS.pattern + "|\\" + MINUS.pattern + "|" + TIMES.pattern + "]";
    }

    /**
     * Returns the grouped pattern for multiple token types.
     *
     * @return the regular expression for an arbitrary number of tokens
     */
    public static Pattern getTokensPattern() {
        return Pattern.compile("(" + getTokenTypePattern() + "*" + ")");
    }

    /**
     * Parses the given String to a {@link TokenType}.
     *
     * @param tokenType the String containing the token type pattern
     *
     * @return the token type with the identical pattern if available, null otherwise
     */
    public static TokenType parseFromString(final String tokenType) {
        for (final TokenType type : TokenType.values()) {
            if (type.getStringPattern().equals(tokenType)) {
                return type;
            }
        }
        return null;
    }

    /**
     * Calculates the result of this operation. If this method is called on an operand, a {@link GameException} is
     * thrown.
     *
     * @param firstOperand the first operand for an operation
     * @param secondOperand the second operand for an operation
     *
     * @return the result of the operation
     *
     * @throws GameException occurs if this method is called on operands instead of operators
     */
    public int calculate(final TokenType firstOperand, final TokenType secondOperand) throws GameException {
        throw new GameException("tried to calculate on an operand!");
    }

    /**
     * Returns the integer representation of this operand.
     *
     * @return the integer representation of this operand
     *
     * @throws GameException occurs if this method is called on operators instead of operands
     */
    public int getIntPattern() throws GameException {
        // since pattern is a char, a simple "" does not suffice
        return Integer.parseInt(String.valueOf(pattern));
    }

    /**
     * Returns the String representation of this operand.
     *
     * @return the String pattern of this token type
     */
    public String getStringPattern() {
        return String.valueOf(pattern);
    }

    /**
     * Returns whether this token type is an operand.
     *
     * @return whether or not this token type is an operand
     */
    public boolean isOperand() {
        return true;
    }

    /**
     * Returns whether this token type is an operator.
     *
     * @return whether or not this token type is an operator
     */
    public boolean isOperator() {
        return false;
    }

    /**
     * Sets the given value if possible, throws an exception if this method is called on an operand with predefined
     * value.
     *
     * @param value the new value containid in this token
     *
     * @throws GameException occurs if this method is called on an operand with a predefined value
     */
    public void setValue(final int value) throws GameException {
        throw new GameException("tried to set a value on a non arbitrary element!");
    }

    /**
     * Returns the integer value of this token type.
     *
     * @return the integer value of this token type
     *
     * @throws GameException occurs if this method is called on an operator
     */
    public int getValue() throws GameException {
        return getIntPattern();
    }
}
