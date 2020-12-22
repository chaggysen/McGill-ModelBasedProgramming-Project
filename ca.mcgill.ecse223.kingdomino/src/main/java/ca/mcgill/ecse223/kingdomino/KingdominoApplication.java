package ca.mcgill.ecse223.kingdomino;

import ca.mcgill.ecse223.kingdomino.model.Gameplay;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.view.BrowsePage;
import ca.mcgill.ecse223.kingdomino.view.GamePage;
import ca.mcgill.ecse223.kingdomino.view.KingdominoPlayerOptionsPage;
import ca.mcgill.ecse223.kingdomino.view.WelcomePage;

public class KingdominoApplication {

	private static Kingdomino kingdomino;
	private static WelcomePage lobby;
	private static GamePage game;
	private static Gameplay gameplay;
	private static KingdominoPlayerOptionsPage optionsPage;
	private static BrowsePage browsePage;

	public static void main(String[] args) {
		startGame();
	}

	public static Kingdomino getKingdomino() {
		if (kingdomino == null) {
			kingdomino = new Kingdomino();
		}
		return kingdomino;
	}

	public static void setKingdomino(Kingdomino kd) {
		kingdomino = kd;
	}

	public static void startGame() {
		delete();
		lobby = new WelcomePage();
		lobby.setVisible(true);
	}

	public static void delete() {
		lobby = null;
		optionsPage = null;
		game = null;
		if (gameplay != null)
			gameplay.delete();
	}

	public static void setWelcomePage() {
		if (optionsPage != null)
			optionsPage.setAllVisible(false);
		lobby = new WelcomePage();
		lobby.setVisible(true);
	}

	public static void setOptionsPage() {
		if (lobby != null)
			lobby.setVisible(false);
		if (game != null)
			game.setVisible(false);
		optionsPage = new KingdominoPlayerOptionsPage();
		optionsPage.setVisible(true);
	}

	public static void setMainPage() {
		if (optionsPage != null)
			optionsPage.setAllVisible(false);
		if (lobby != null)
			lobby.setVisible(false);
		createNewGameplay();
		gameplay.initReady();
		game = new GamePage();
		game.setVisible(true);
	}

	public static void setLoadPage() {
		if (optionsPage != null)
			optionsPage.setAllVisible(false);
		if (lobby != null)
			lobby.setVisible(false);
		createNewGameplay();
		gameplay.initLoaded();
		game = new GamePage();
		game.setVisible(true);
	}

	public static Gameplay getGameplay() {
		return gameplay;
	}

	public static Gameplay createNewGameplay() {
		gameplay = new Gameplay();
		return gameplay;
	}

	public static void closeBrowsePage() {
		if (browsePage != null)
			browsePage.setVisible(false);
	}

	public static void openBrowsePage() {
		browsePage = new BrowsePage();
		browsePage.setVisible(true);
	}
}