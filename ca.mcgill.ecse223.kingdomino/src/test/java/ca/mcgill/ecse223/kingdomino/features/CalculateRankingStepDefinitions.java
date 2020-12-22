package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.*;
import ca.mcgill.ecse223.kingdomino.model.Castle;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.model.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * @author Sen Wang
 */
public class CalculateRankingStepDefinitions {


	HashMap <Player, Integer> rankedPlayerMap = new HashMap <Player, Integer>();
	
	
	@Given("the game is initialized for calculate ranking")
	public void the_game_is_initialized_for_calculate_ranking() {
		// Intialize empty game
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game);
		// Populate game
		addDefaultUsersAndPlayers(game);
		createAllDominoes(game);
		game.setNextPlayer(game.getPlayer(0));
		KingdominoApplication.setKingdomino(kingdomino);
	}

	/*
	 * @Given("the players have the following two dominoes in their respective kingdoms:"
	 * ) public void
	 * the_players_have_the_following_two_dominoes_in_their_respective_kingdoms(io.
	 * cucumber.datatable.DataTable dataTable) {
	 * 
	 * Game game = KingdominoApplication.getKingdomino().getCurrentGame();
	 * List<Map<String, String>> valueMaps = dataTable.asMaps(); for (Map<String,
	 * String> map : valueMaps) { // Get values from cucumber table PlayerColor
	 * playerColor = getColor(map.get("player"));
	 * 
	 * Integer player_id = null;
	 * 
	 * if (playerColor == PlayerColor.Blue) { player_id = 0; } else if (playerColor
	 * == PlayerColor.Green) { player_id = 1; } else if (playerColor ==
	 * PlayerColor.Yellow) { player_id = 2; } else if (playerColor ==
	 * PlayerColor.Pink) { player_id = 3; }
	 * 
	 * Integer domino1_id = Integer.decode(map.get("domino1")); DirectionKind
	 * domino1_dir = getDirection(map.get("dominodir1")); Integer domino1_posx =
	 * Integer.decode(map.get("posx1")); Integer domino1_posy =
	 * Integer.decode(map.get("posy1"));
	 * 
	 * Integer domino2_id = Integer.decode(map.get("domino2")); DirectionKind
	 * domino2_dir = getDirection(map.get("dominodir2")); Integer domino2_posx =
	 * Integer.decode(map.get("posx2")); Integer domino2_posy =
	 * Integer.decode(map.get("posy2"));
	 * 
	 * // Add the domino 1 to a player's kingdom Kingdom kingdom =
	 * game.getPlayer(player_id).getKingdom();
	 * 
	 * Domino domino1ToPlace = getdominoByID(domino1_id); DominoInKingdom
	 * dom1InKingdom = new DominoInKingdom(domino1_posx, domino1_posy, kingdom,
	 * domino1ToPlace); dom1InKingdom.setDirection(domino1_dir);
	 * domino1ToPlace.setStatus(DominoStatus.PlacedInKingdom);
	 * 
	 * // Add the domino 2 to a player's kingdom
	 * 
	 * Domino domino2ToPlace = getdominoByID(domino2_id); DominoInKingdom
	 * dom2InKingdom = new DominoInKingdom(domino2_posx, domino2_posy, kingdom,
	 * domino2ToPlace); dom2InKingdom.setDirection(domino2_dir);
	 * domino2ToPlace.setStatus(DominoStatus.PlacedInKingdom); }
	 * 
	 * 
	 * }
	 */

	@Given("the players have no tiebreak")
	public void the_players_have_no_tiebreak() {
		// included in Controller method
	}

	@When("calculate ranking is initiated")
	public void calculate_ranking_is_initiated() {
		rankedPlayerMap = KingdominoController.calculateRanking();
	}

	@Then("player standings shall be the followings:")
	public void player_standings_shall_be_the_followings(io.cucumber.datatable.DataTable dataTable) {

		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		rankedPlayerMap = KingdominoController.calculateRanking();
		boolean correctlyRanked = true;
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			// Get values from cucumber table
			PlayerColor playerColor = getColor(map.get("player"));
			int playerRanking = Integer.decode(map.get("standing"));

			for (int i = 0; i < game.getPlayers().size(); i++) {
				if (game.getPlayers().get(i).getColor().equals(playerColor)
						&& rankedPlayerMap.get(game.getPlayers().get(i)) != playerRanking) {
					correctlyRanked = false;
				}
			}
		}

		assertEquals(true, correctlyRanked);

	}

	///////////////////////////////////////
	/// -----Private Helper Methods---- ///
	///////////////////////////////////////

	private void addDefaultUsersAndPlayers(Game game) {
		String[] users = { "User1", "User2", "User3", "User4" };
		for (int i = 0; i < users.length; i++) {
			game.getKingdomino().addUser(users[i]);
			Player player = new Player(game);
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

	private PlayerColor getColor(String color) {
		switch (color) {
		case "blue":
			return PlayerColor.Blue;
		case "green":
			return PlayerColor.Green;
		case "pink":
			return PlayerColor.Pink;
		case "yellow":
			return PlayerColor.Yellow;
		default:
			throw new java.lang.IllegalArgumentException("Invalid player color: " + color);
		}
	}

	private String colorToString(PlayerColor color) {
		switch (color) {
		case Blue:
			return "blue";
		case Green:
			return "green";
		case Pink:
			return "pink";
		case Yellow:
			return "yellow";
		default:
			throw new java.lang.IllegalArgumentException("Invalid player color: " + color);
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
		case "inPile":
			return DominoStatus.InPile;
		case "excluded":
			return DominoStatus.Excluded;
		case "inCurrentDraft":
			return DominoStatus.InCurrentDraft;
		case "inNextDraft":
			return DominoStatus.InNextDraft;
		case "erroneouslyPreplaced":
			return DominoStatus.ErroneouslyPreplaced;
		case "correctlyPreplaced":
			return DominoStatus.CorrectlyPreplaced;
		case "placedInKingdom":
			return DominoStatus.PlacedInKingdom;
		case "discarded":
			return DominoStatus.Discarded;
		default:
			throw new java.lang.IllegalArgumentException("Invalid domino status: " + status);
		}
	}

}
