package reversi;

import java.util.Random;

import game.*;
import game.Stats.DifficultyType;

public class Program {

	public static void main(String[] args) {
		Stats.setM(8);
		Stats.setN(8);
		Stats.setDifficulty(DifficultyType.Penny);
		// PLayer 1 humain
		//Stats.setPlayer1(new Player(-1, -1,"Computer1"));
		// PLayer 1 MinMax
		Stats.setPlayer1(new CPMinMax(-1, -1,"Computer1", 5));
		// PLayer 1 Alpha Beta
		//Stats.setPlayer1(new CPAlphaBeta(-1, -1,"Computer1", 5));
		// PLayer 1 MCTS
		//Stats.setPlayer1(new CPMCTS(-1, -1,"Computer1"));

		// PLayer 2 humain
		//Stats.setPlayer2(new Player(-1, -1,"Computer2"));
		// PLayer 2 MinMax
		//Stats.setPlayer2(new CPMinMax(-1, -1,"Computer2", 5));
		// PLayer 2 Alpha Beta
		Stats.setPlayer2(new CPAlphaBeta(-1, -1,"Computer2", 5));
		// PLayer 2 MCTS
		//Stats.setPlayer2(new CPMCTS(Stats.getPlayer1().getHue(), Stats.getPlayer1().getImageID(),"Computer2"));

		Random rand = new Random();
		int r = rand.nextInt(2);
		if (r == 0)
			Stats.setCurrentPlayer(Stats.getPlayer1());
		else
			Stats.setCurrentPlayer(Stats.getPlayer2());
		new MainScreen();
	}
}
