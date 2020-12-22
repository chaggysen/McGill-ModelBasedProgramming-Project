package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;

import java.io.FileReader;

import java.io.IOException;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;

import ca.mcgill.ecse223.kingdomino.model.Castle;

import ca.mcgill.ecse223.kingdomino.model.Domino;

import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;

import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;

import ca.mcgill.ecse223.kingdomino.model.DominoSelection;

import ca.mcgill.ecse223.kingdomino.model.Draft;

import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;

import ca.mcgill.ecse223.kingdomino.model.Game;

import ca.mcgill.ecse223.kingdomino.model.Gameplay;

import ca.mcgill.ecse223.kingdomino.model.Gameplay.GamestatusInGame;

import ca.mcgill.ecse223.kingdomino.model.Kingdom;

import ca.mcgill.ecse223.kingdomino.model.Kingdomino;

import ca.mcgill.ecse223.kingdomino.model.Player;

import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;

import ca.mcgill.ecse223.kingdomino.model.TerrainType;

import ca.mcgill.ecse223.kingdomino.model.User;

import io.cucumber.java.en.And;

import io.cucumber.java.en.Given;

import io.cucumber.java.en.Then;

import io.cucumber.java.en.When;

/**
 * 
 * @author Sen Wang
 * 
 */

public class PlacingDominoStepDefinitions {

	Domino domino = null;

	int xPlacement;

	int yPlacement;

	@Given("the game has been initialized for placing domino")

	public void the_game_has_been_initialized_for_placing_domino() {

		Kingdomino kingdomino = KingdominoApplication.getKingdomino();

		KingdominoApplication.setKingdomino(kingdomino);

		Game game = new Game(48, kingdomino);

		kingdomino.setCurrentGame(game);
		Gameplay gamePlay = KingdominoApplication.createNewGameplay();
		gamePlay.setGamestatus("InGame");

		gamePlay.initReady();

	}

	@Given("it is not the last turn of the game")

	public void it_is_not_the_last_turn_of_the_game() {

		// DEFAULT

	}

	@Given("the current player is not the last player in the turn")

	public void the_current_player_is_not_the_last_player_in_the_turn() {

		// Only one domino selection, thus the current player is not the last player in
		// the turn

		Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();

		Draft nextDraft = currentGame.getNextDraft();

		Domino dominoInDraft = nextDraft.getIdSortedDominos().get(0);

		Player playerInTurn = currentGame.getPlayers().get(3);

		DominoSelection randomSelection = new DominoSelection(playerInTurn, dominoInDraft, nextDraft);

		nextDraft.addSelection(randomSelection);

	}

	@Given("the current player is preplacing his\\/her domino with ID {int} at location {int}:{int} with direction {string}")

	public void the_current_player_is_preplacing_his_her_domino_with_ID_at_location_with_direction(Integer int1,
			Integer int2, Integer int3, String string) {

		// gamePlay = new Gameplay();

		Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();

		Player player = currentGame.getPlayers().get(0);

		Domino domino = getdominoByID(int1);


		xPlacement = int2;


		yPlacement = int3;

		DirectionKind direction = getDirection(string);
		Gameplay gamePlay = KingdominoApplication.getGameplay();
		gamePlay.setGamestatus("InGame");


		gamePlay.placeDom(player, domino, int2, int3, direction, false);


	}

	@And("the preplaced domino has the status {string}")

	public void the_preplaced_domino_has_the_status(String string) {

		Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();

		Player player = currentGame.getPlayers().get(0);

		for (int i = 0; i < currentGame.getAllDominos().size(); i++) {

			if (currentGame.getAllDominos().get(i).getStatus() == DominoStatus.CorrectlyPreplaced) {

				domino = currentGame.getAllDomino(i);

			}

		}

		assertEquals(getDominoStatus(string), domino.getStatus());

	}

	@When("the current player places his\\/her domino")

	public void the_current_player_places_his_her_domino() {

		Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();

		Boolean satisfied = true;

		Player player = currentGame.getPlayers().get(0);


		Gameplay gamePlay = KingdominoApplication.getGameplay();

		gamePlay.placeDom(player, domino, xPlacement, yPlacement, DirectionKind.Down, satisfied);


		gamePlay.placeReady();

	}

	@Then("this player now shall be making his\\/her domino selection")

	public void this_player_now_shall_be_making_his_her_domino_selection() {

		GamestatusInGame expectedStatus = GamestatusInGame.SelectingDomino;
		Gameplay gamePlay = KingdominoApplication.getGameplay();
		GamestatusInGame actualStatus = gamePlay.getGamestatusInGame();

		assertEquals(expectedStatus, actualStatus);

	}

	@Given("the current player is the last player in the turn")

	public void the_current_player_is_the_last_player_in_the_turn() {

		Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();

		Draft nextDraft = currentGame.getNextDraft();

		Domino domino0InDraft = nextDraft.getIdSortedDominos().get(0);

		Player player0InTurn = currentGame.getPlayers().get(0);

		Domino domino1InDraft = nextDraft.getIdSortedDominos().get(1);

		Player player1InTurn = currentGame.getPlayers().get(1);

		Domino domino2InDraft = nextDraft.getIdSortedDominos().get(2);

		Player player2InTurn = currentGame.getPlayers().get(2);

		Domino domino3InDraft = nextDraft.getIdSortedDominos().get(3);

		Player player3InTurn = currentGame.getPlayers().get(3);

		DominoSelection randomSelection0 = new DominoSelection(player0InTurn, domino0InDraft, nextDraft);

		DominoSelection randomSelection1 = new DominoSelection(player1InTurn, domino1InDraft, nextDraft);

		DominoSelection randomSelection2 = new DominoSelection(player2InTurn, domino2InDraft, nextDraft);

		DominoSelection randomSelection3 = new DominoSelection(player3InTurn, domino3InDraft, nextDraft);

		nextDraft.addSelection(randomSelection0);

		nextDraft.addSelection(randomSelection1);

		nextDraft.addSelection(randomSelection2);

		nextDraft.addSelection(randomSelection3);

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