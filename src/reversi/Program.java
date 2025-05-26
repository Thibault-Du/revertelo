package reversi;

import java.util.Random;

import game.*;
import game.Stats.DifficultyType;

public class Program {

	public static void main(String[] args) {
		Stats.setM(8);
		Stats.setN(8);
		Stats.setDifficulty(DifficultyType.Penny);
		Stats.setPlayer1(new CPMinMax(-1, -1,"Computer1", 5));
		Stats.setPlayer2(new Player(-1, -1,"Computer2"));
		//Stats.setPlayer2(new ComputerPlayer(Stats.getPlayer1().getHue(), Stats.getPlayer1().getImageID(),"Computer"));
		//Stats.setPlayer2(new CPAlphaBeta(Stats.getPlayer1().getHue(), Stats.getPlayer1().getImageID(),"Computer2", 5));
		//Stats.setPlayer2(new CPMinMax(Stats.getPlayer1().getHue(), Stats.getPlayer1().getImageID(),"Computer2", 5));

		Random rand = new Random();
		int r = rand.nextInt(2);
		if (r == 0)
			Stats.setCurrentPlayer(Stats.getPlayer1());
		else
			Stats.setCurrentPlayer(Stats.getPlayer2());
		new MainScreen();
	}
}
