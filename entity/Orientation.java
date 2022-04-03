

package edu.kit.informatik.scrabble.entity;

/**
 * Encapsulates the two orientations available for expressions in this arithmetic scrabble.
 *
 * @author Sara
 * @version 1.0
 */
public enum Orientation {
    /**
     * The orientation vertical (x,y) -> (x,y+1).
     */
    VERTICAL("V") {
        @Override public int[] getDirectionalDifference() {
            return new int[] {1, 0};
        }
    },
    /**
     * The orientation horizontal (x,y) -> (x+1,y).
     */
    HORIZONTAL("H") {
        @Override public int[] getDirectionalDifference() {
            return new int[] {0, 1};
        }
    };

    private final String pattern;

    /**
     * Instantiates a new {@link Orientation} with the given pattern.
     *
     * @param pattern the pattern of the orientation
     */
    Orientation(final String pattern) {
        this.pattern = pattern;
    }

    /**
     * The regular expression to match an orientation string to.
     *
     * @return the pattern for a orientation
     */
    public static String getOrientationPattern() {
        return "[" + VERTICAL.pattern + "|" + HORIZONTAL.pattern + "]";
    }

    /**
     * Parses the given String to a orientation if possible.
     *
     * @param group the String containing the orientation pattern
     *
     * @return the parsed {@link Orientation} if possible, null otherwise
     */
    public static Orientation parseFromString(final String group) {
        for (final Orientation orientation : Orientation.values()) {
            if (orientation.pattern.equals(group)) {
                return orientation;
            }
        }
        return null;
    }

    /**
     * The directional difference to grow an expression.
     *
     * @return integer array with two elements, containing the difference from one field to another with {xDiff,yDiff}
     */
    public abstract int[] getDirectionalDifference();
}
