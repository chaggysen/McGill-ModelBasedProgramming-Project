package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Castle;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.model.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;

public class OrderAndRevealNextDraftStepDefinitions {
	/*
	 * @author Sen Wang
	 */
	
	Kingdomino kingdomino = new Kingdomino();
	Game game = new Game(48, kingdomino);

	@Given("the game is initialized for order next draft of dominoes")
	public void the_game_is_initialized_for_order_next_draft_of_dominoes() {
		// Intialize empty game
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game);
		// Populate game
		addDefaultUsersAndPlayers(game);
		createAllDominoes(game);
		game.setNextPlayer(game.getPlayer(0));
		KingdominoApplication.setKingdomino(kingdomino);
		Draft draft = new Draft(DraftStatus.FaceUp, game);
		game.setCurrentDraft(draft);
	}
	
	@Given("the next draft is {string}")
	public void the_next_draft_is(String unorderedids) {
		ArrayList <Integer> unorderedIds = new ArrayList <Integer>();
		String [] notOrderedIds = unorderedids.split(",");
		for (int i = 0; i < notOrderedIds.length; i++) {
			unorderedIds.add(Integer.decode(notOrderedIds[i]));
		}
		
	    Draft nextDraft = new Draft(DraftStatus.FaceDown, game);
	    game.setNextDraft(nextDraft);
		
		for (int i = 0; i < unorderedIds.size(); i++) {
			game.getNextDraft().addIdSortedDomino(getdominoByID(unorderedIds.get(i)));
		}
	}
	
	@Given("the dominoes in next draft are facing down")
	public void the_dominoes_in_the_next_draft_are_facing_down() {
	    game.getNextDraft().setDraftStatus(DraftStatus.FaceDown);
	}
	
	@When("the ordering of the dominoes in the next draft is initiated")
	public void the_ordering_of_the_dominoes_in_the_next_draft_is_initiated() {
		KingdominoController.orderAndRevealNextDraft();
	}
	
	@Then("the status of the next draft is sorted")
	public void the_status_of_the_next_draft_is_sorted() {
		DraftStatus actualStatus = game.getNextDraft().getDraftStatus();
		
		assertEquals(DraftStatus.Sorted, actualStatus);
	}
	
	@Then("the order of dominoes in the draft will be {string}")
	public void the_order_of_dominoes_in_the_draft_will_be(String orderedids) {
		ArrayList <Integer> actualSorting = new ArrayList <Integer>();
		ArrayList <Integer> expectedSorting = new ArrayList <Integer>();
		
		for (int i = 0; i < game.getNextDraft().getIdSortedDominos().size(); i++) {
			actualSorting.add(game.getNextDraft().getIdSortedDominos().get(i).getId());
		}
		
		String [] orderedIds = orderedids.split(",");
		for (int i = 0; i < orderedIds.length; i++) {
			expectedSorting.add(Integer.decode(orderedIds[i]));
		}
		
		assertEquals(expectedSorting, actualSorting);
	}
	
	@Given("the game is initialized for reveal next draft of dominoes")
	public void the_game_is_initialized_for_reveal_next_draft_of_dominoes() {
		// Intialize empty game
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game);
		// Populate game
		addDefaultUsersAndPlayers(game);
		createAllDominoes(game);
		game.setNextPlayer(game.getPlayer(0));
		KingdominoApplication.setKingdomino(kingdomino);
		Draft draft = new Draft(DraftStatus.FaceUp, game);
		game.setCurrentDraft(draft);
	}
	
	@Given("the dominoes in next draft are sorted")
	public void the_dominoes_in_next_draft_are_sorted() {
		game.getNextDraft().setDraftStatus(DraftStatus.Sorted);
	}
	
	@When("the revealing of the dominoes in the next draft is initiated")
	public void the_revealing_of_the_dominoes_in_the_next_draft_is_initiated() {
		KingdominoController.orderAndRevealNextDraft();
	}
	
	@Then("the status of the next draft is face up")
	public void the_status_of_the_next_draft_is_face_up() {
		DraftStatus actualStatus = game.getNextDraft().getDraftStatus();
		assertEquals(DraftStatus.FaceUp, actualStatus);
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
