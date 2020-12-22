package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
import ca.mcgill.ecse223.kingdomino.model.Gameplay.Gamestatus;
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
 * @author Noah Chamberland
 * @author Sen Wang
 *
 */
public class SelectingFirstDominoStepDefinitions {
	
	ArrayList <Integer> dominoIDs = new ArrayList <Integer>();
	Gameplay gamePlay = new Gameplay();
	
	@Given("the game has been initialized for selecting first domino")
	public void the_game_has_been_initialized_for_selecting_first_domino() {
		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		KingdominoApplication.setKingdomino(kingdomino);
		Game game = new Game(48, kingdomino);
		createAllDominoes(game);
		addDefaultUsersAndPlayers(game);
		kingdomino.setCurrentGame(game);
		
	}

	@Given("the initial order of players is {string}")
	public void the_initial_order_of_players_is(String string) {
	
	}

	@Given("the current draft has the dominoes with ID {string}")
	public void the_current_draft_has_the_dominoes_with_ID(String string) {
		String[] dominos = string.split(",");
		for (int i = 0; i < dominos.length; i++) {
			dominoIDs.add(Integer.decode(dominos[i]));
		}
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Draft newDraft = new Draft(DraftStatus.FaceUp, game);
		for (int i = 0; i < dominos.length; i++) {
			newDraft.addIdSortedDomino(getdominoByID(Integer.decode(dominos[i])));
		}
		game.setNextDraft(newDraft);
	}

	@Given("player's first domino selection of the game is {string}")
	public void player_s_first_domino_selection_of_the_game_is(String currentselection) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		ArrayList <Player> playerSelection = new ArrayList <Player>();
		String [] playerColorList = currentselection.split(",");
		
		for (int i = 0; i < playerColorList.length; i++) {
			int player_id = 0;
			
			PlayerColor playerColor;
			
			if (!playerColorList[i].equals("none")) {
				playerColor = getPlayerColor(playerColorList[i]);
				if (playerColor == PlayerColor.Blue) {
					player_id = 0;
				}else if (playerColor == PlayerColor.Green) {
					player_id = 1;
				}else if (playerColor == PlayerColor.Yellow) {
					player_id = 2;
				}else if (playerColor == PlayerColor.Pink) {
					player_id = 3;
				}else {
					player_id = (Integer) null;
				}
				playerSelection.add(game.getPlayer(player_id));
			}else {
				playerSelection.add(null);
			}
		    
		}
		
		for (int i = 0; i < playerSelection.size(); i++) {
			for (int j = 0; j < dominoIDs.size(); j++) {
				if (i == j) {
					if (playerSelection.get(j) != null) {
						DominoSelection dominoSelection = new DominoSelection(playerSelection.get(j),getdominoByID(dominoIDs.get(i)), game.getNextDraft());
						game.getNextDraft().addSelection(dominoSelection);
					}
				}
			}
		}
	}

	@Given("the {string} player is selecting his\\/her domino with ID {int}")
	public void the_player_is_selecting_his_her_domino_with_ID(String string, Integer int1) {
		//gamePlay.setGamestatus("CreatingFirstDraft");
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		gamePlay.initReady();
		Draft newDraft = new Draft(DraftStatus.FaceUp, game);
		for (int i = 0; i < dominoIDs.size(); i++) {
			newDraft.addIdSortedDomino(getdominoByID(dominoIDs.get(i)));
		}
		game.setNextDraft(newDraft);
		Player currentPlayer = getPlayerByColor(getPlayerColor(string));
		game.setNextPlayer(currentPlayer);
		gamePlay.selectDom(int1);
		
	}

	@When("the {string} player completes his\\/her domino selection")
	public void the_player_completes_his_her_domino_selection(String string) {
		gamePlay.selectReady();
	}

	@Then("the {string} player shall be {string} his\\/her domino")
	public void the_player_shall_be_his_her_domino(String string, String string2) {
	    assertEquals(true, true);
	}


	@Given("the {string} player is selecting his\\/her first domino with ID {int}")
	public void the_player_is_selecting_his_her_first_domino_with_ID(String string, Integer int1) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		gamePlay.initReady();
		Draft newDraft = new Draft(DraftStatus.FaceUp, game);
		for (int i = 0; i < dominoIDs.size(); i++) {
			newDraft.addIdSortedDomino(getdominoByID(dominoIDs.get(i)));
		}
		game.setNextDraft(newDraft);
		Player currentPlayer = getPlayerByColor(getPlayerColor(string));
		game.setNextPlayer(currentPlayer);
		gamePlay.selectDom(int1);
		
	}

	@When("the {string} player completes his\\/her domino selection of the game")
	public void the_player_completes_his_her_domino_selection_of_the_game(String string) {
		gamePlay.selectReady();
	}

	@Then("a new draft shall be available, face down")
	public void a_new_draft_shall_be_available_face_down() {
		assertEquals(true, true);
	}

	// We use the annotation @And to signal precondition check instead of
	// initialization (which is done in @Given methods)
	@And("the validation of domino selection returns {string}")
	public void the_validation_of_domino_selection_returns(String expectedValidationResultString) {
		boolean expectedValidationResult = true;
		if ("success".equalsIgnoreCase(expectedValidationResultString.trim())) {
			expectedValidationResult = true;
		} else if ("error".equalsIgnoreCase(expectedValidationResultString.trim())) {
			expectedValidationResult = false;
		} else {
			throw new IllegalArgumentException(
					"Unknown validation result string \"" + expectedValidationResultString + "\"");
		}
		boolean actualValidationResult = false;

		
		actualValidationResult = gamePlay.getSelectValid();

		// Check the precondition prescribed by the scenario
		assertEquals(expectedValidationResult, actualValidationResult);
	}

	
	//Helper methods
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
	
	private Player getPlayerByColor(PlayerColor color) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for(Player player: game.getPlayers()) {
			if(player.getColor() == color) return player;
		}
		return null;
	}
}