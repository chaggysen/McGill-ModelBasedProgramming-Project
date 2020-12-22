package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.BonusOption;
import ca.mcgill.ecse223.kingdomino.model.Castle;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Gameplay;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.KingdomTerritory;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.model.User;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.DominoSelection;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;

/**
 * @author Noah Chamberland
 * Note : Duplicate methods contain different input, but the state machine itself should work fine
 */


public class CalculatingPlayerScoreStepDefinitions {
	
	Domino domino;
	int xPlacement;
	int yPlacement;
	

	@Given("the game is initialized for calculating player score")
	public void the_game_is_initialized_for_calculating_player_score() {
		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		KingdominoApplication.setKingdomino(kingdomino);
		Game game = new Game(48, kingdomino);
		kingdomino.setCurrentGame(game);

		//createAllDominoes(game);

		//gamePlay.initReady();
	}
	
	@Given("the current player has no dominoes in his\\/her kingdom yet")
	public void the_current_player_has_no_dominoes_in_his_her_kingdom_yet() {
	    Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();
	    List <KingdomTerritory> territoryCopy = new ArrayList <KingdomTerritory>(currentGame.getPlayers().get(0).getKingdom().getTerritories());
	    territoryCopy.clear();
	}
	
	
	
	@Given("the current player is placing his\\/her domino with ID {int}")
	public void the_current_player_is_placing_his_her_domino_with_ID(Integer int1) {
		Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();
		Player player = currentGame.getPlayers().get(0);

	    //gamePlay.discardDom(player.getKingdom(), getdominoByID(int1));
		currentGame.setNextPlayer(player);
		DominoSelection selection = new DominoSelection(player, getdominoByID(int1), currentGame.getNextDraft());
		currentGame.getNextDraft().addSelection(selection);
		//gamePlay.initPlacement();

	}
	
	
	

	
	@Then("the score of the current player shall be {int}")
	public void the_score_of_the_current_player_shall_be(int score) {
		Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();
		assertEquals(score, currentGame.getPlayers().get(0).getTotalScore());
	}
	
	@Given("the game has no bonus options selected")
	public void the_game_has_no_bonus_options_selected() {
		Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();
		List <BonusOption> bonusOptionCopy = new ArrayList <BonusOption>(currentGame.getSelectedBonusOptions());
		bonusOptionCopy.clear();
	}
	
	
	
	@Given("the score of the current player is {int}")
	public void the_score_of_the_current_player_is(int value) {
		Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();
		currentGame.getPlayers().get(0).setBonusScore(0);
		currentGame.getPlayers().get(0).setPropertyScore(value);
	}
		
///////////////////////////////////////
/// -----Private Helper Methods---- ///
///////////////////////////////////////
	
	
	private void addDefaultUsersAndPlayers(Game game) {
		String[] userNames = { "User1", "User2", "User3", "User4" };
		for (int i = 0; i < userNames.length; i++) {
			User user = game.getKingdomino().addUser(userNames[i]);
			Player player = new Player(game);
			player.setUser(user);
			player.setColor(PlayerColor.values()[i]);
			Kingdom kingdom = new Kingdom(player);
			new Castle(0, 0, kingdom, player);
		}
	}

	private void createAllDominoes(Game game) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("src/main/resources/alldominoes.dat"));
			String line = "";
			String delimiters = "[:\\+()]";
			while ((line = br.readLine()) != null) {
				String[] dominoString = line.split(delimiters); // {id, leftTerrain, rightTerrain, crowns}
				int dominoId = Integer.decode(dominoString[0]);
				TerrainType leftTerrain = getTerrainType(dominoString[1]);
				TerrainType rightTerrain = getTerrainType(dominoString[2]);
				int numCrown = 0;
				if (dominoString.length > 3) {
					numCrown = Integer.decode(dominoString[3]);
				}
				new Domino(dominoId, leftTerrain, rightTerrain, numCrown, game);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new java.lang.IllegalArgumentException(
					"Error occured while trying to read alldominoes.dat: " + e.getMessage());
		}
	}

	private Domino getdominoByID(int id) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for (Domino domino : game.getAllDominos()) {
			if (domino.getId() == id) {
				return domino;
			}
		}
		throw new java.lang.IllegalArgumentException("Domino with ID " + id + " not found.");
	}

	private TerrainType getTerrainType(String terrain) {
		switch (terrain) {
		case "W":
			return TerrainType.WheatField;
		case "F":
			return TerrainType.Forest;
		case "M":
			return TerrainType.Mountain;
		case "G":
			return TerrainType.Grass;
		case "S":
			return TerrainType.Swamp;
		case "L":
			return TerrainType.Lake;
		default:
			throw new java.lang.IllegalArgumentException("Invalid terrain type: " + terrain);
		}
	}

	private DirectionKind getDirection(String dir) {
		switch (dir) {
		case "up":
			return DirectionKind.Up;
		case "down":
			return DirectionKind.Down;
		case "left":
			return DirectionKind.Left;
		case "right":
			return DirectionKind.Right;
		default:
			throw new java.lang.IllegalArgumentException("Invalid direction: " + dir);
		}
	}

	private DominoStatus getDominoStatus(String status) {
		switch (status) {
		case "InPile":
			return DominoStatus.InPile;
		case "Excluded":
			return DominoStatus.Excluded;
		case "InCurrentDraft":
			return DominoStatus.InCurrentDraft;
		case "InNextDraft":
			return DominoStatus.InNextDraft;
		case "ErroneouslyPreplaced":
			return DominoStatus.ErroneouslyPreplaced;
		case "CorrectlyPreplaced":
			return DominoStatus.CorrectlyPreplaced;
		case "PlacedInKingdom":
			return DominoStatus.PlacedInKingdom;
		case "Discarded":
			return DominoStatus.Discarded;
		default:
			throw new java.lang.IllegalArgumentException("Invalid domino status: " + status);
		}
	}
	
	private PlayerColor getPlayerColor(String color) {
		switch (color) {
		case "pink":
			return PlayerColor.Pink;
		case "blue":
			return PlayerColor.Blue;
		case "yellow":
			return PlayerColor.Yellow;
		case "green":
			return PlayerColor.Green;
		default:
			throw new java.lang.IllegalArgumentException("Invalid player color: " + color);
		}
	}
}
