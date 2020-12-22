package ca.mcgill.ecse223.kingdomino;

import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;

public class Test {

	public static void main(String[] args) {

		KingdominoController.initializeGame();
		KingdominoController.startNewGame();
		KingdominoController.setBonusOptions(true, true);
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		new DominoInKingdom(-2, 0, player.getKingdom(), getdominoByID(10)).setDirection(DirectionKind.Right);
		new DominoInKingdom(0, 1, player.getKingdom(), getdominoByID(4)).setDirection(DirectionKind.Up);
		new DominoInKingdom(1, 0, player.getKingdom(), getdominoByID(9)).setDirection(DirectionKind.Right);
		new DominoInKingdom(0, -2, player.getKingdom(), getdominoByID(1)).setDirection(DirectionKind.Up);
		KingdominoController.calculatePlayerScore();
		System.out.println(player.getTotalScore());

	}

	public static Domino getdominoByID(int id) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for (Domino domino : game.getAllDominos()) {
			if (domino.getId() == id) {
				return domino;
			}
		}
		throw new java.lang.IllegalArgumentException("Domino with ID " + id + " not found.");
	}
}