


package edu.kit.informatik.scrabble;

/**
 * 
 *
 * @author Sara
 * @version 1.0
 */
public class GameException extends IllegalArgumentException {
    private static final long serialVersionUID = -4491591333105161142L;

    /**
     * Instantiates a new {@link GameException} with the give message.
     *
     * @param 
     */
    public GameException(final String message) {
        super(message);
    }
}
