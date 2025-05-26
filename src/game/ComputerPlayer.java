package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class which represents computer player which extends Player class
 */
public abstract class  ComputerPlayer extends Player {


	/**
	 * Constructor.
	 *
	 * @param notAllowedHue This hue is not allowed for the color of the player checkers.
	 * @param noImageID     This imageID is not allowed for the image destination of the player checkers.
	 * @param name          The name of the player.
	 */
	public ComputerPlayer(float notAllowedHue, int noImageID, String name) {
		super(notAllowedHue, noImageID, name);
	}

	public abstract void GetNextMove(Player[][] boardByPlayers, Integer[] rowColumnIndexes, Player player);
}
