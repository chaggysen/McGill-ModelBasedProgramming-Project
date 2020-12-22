package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Castle;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.DominoSelection;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Gameplay;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.model.User;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Gameplay.GamestatusFinalizing;
import ca.mcgill.ecse223.kingdomino.model.Gameplay.GamestatusInGame;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


/*
 * @author Sen Wang
 */
public class PlacingLastDominoStepDefinitions {
	

	@Given("the game has been initialized for placing last domino")
	public void the_game_has_been_initialized_for_placing_last_domino() {
		Kingdomino kingdomino = new Kingdomino();
		KingdominoApplication.setKingdomino(kingdomino);
		Game game = new Game(48, kingdomino);
		kingdomino.setCurrentGame(game);
		Gameplay gamePlay=KingdominoApplication.createNewGameplay();
		gamePlay = new Gameplay();
		gamePlay.setGamestatus("InGame");
		gamePlay.initReady();
	}

	@Given("it is the last turn of the game")
	public void it_is_the_last_turn_of_the_game() {
	    KingdominoController.createNextDraftOfDominoes(12);
	    Gameplay gamePlay=KingdominoApplication.getGameplay();
	    gamePlay.selectReady();
	 
	}

	@Then("the next player shall be placing his\\/her domino")
	public void the_next_player_shall_be_placing_his_her_domino() {
		GamestatusInGame expectedStatus = GamestatusInGame.SelectingDomino;
		Gameplay gamePlay=KingdominoApplication.getGameplay();
		GamestatusInGame actualStatus = gamePlay.getGamestatusInGame();
		assertEquals(expectedStatus, actualStatus);
	}
	
	@Then("the game shall be finished")
	public void the_game_shall_be_finished() {
		Gameplay gamePlay=KingdominoApplication.getGameplay();
		GamestatusFinalizing expectedStatus = GamestatusFinalizing.Null;
		GamestatusFinalizing actualStatus = gamePlay.getGamestatusFinalizing();
		assertEquals(expectedStatus,actualStatus);
	}

	@Then("the final results after successful placement shall be computed")
	public void the_final_results_after_successful_placement_shall_be_computed() {
		GamestatusFinalizing expectedStatus = GamestatusFinalizing.Null;
		Gameplay gamePlay=KingdominoApplication.getGameplay();
		GamestatusFinalizing actualStatus = gamePlay.getGamestatusFinalizing();
		assertEquals(expectedStatus,actualStatus);
	}
	
	
	
	
	
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
