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
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * 
 * @author Noah Chamberland
 *
 */
public class CreateNextDraftStepDefinitions {
	
	private Kingdomino kd; 
	private Game game;
	private int draftNum;

	@Given("the game is initialized to create next draft")
	public void the_game_is_initialized_to_create_next_draft() {
		// Intialize empty game
		kd = new Kingdomino();
		game = new Game(48, kd);
		game.setNumberOfPlayers(4);
		kd.setCurrentGame(game);
		// Populate game
		addDefaultUsersAndPlayers(game);
		createAllDominoes(game);
		game.setNextPlayer(game.getPlayer(0));
		KingdominoApplication.setKingdomino(kd);
	}
	
	@Given("there has been {int} drafts created")
	public void there_has_been_drafts_created(int draftnum) {
		for(int i = 0; i <= draftnum; i++) {
			game.addAllDraft(new Draft(DraftStatus.Sorted, game));
		}
	}
	
	@Given("there is a current draft")
	public void there_is_a_current_draft() {
		game.setCurrentDraft(new Draft(DraftStatus.FaceUp, game));
	}

	@Given("there is a next draft")
	public void there_is_a_next_draft() {
		game.setNextDraft(new Draft(DraftStatus.FaceDown, game));
	}
	
	@Given("this is a {int} player game")
	public void this_is_a_player_game(int num_players) {
		game.setNumberOfPlayers(num_players);
	}
	
	@Given("the top 5 dominoes in my pile have the IDs {string}")
	public void the_top_5_dominoes_in_my_pile_have_the_IDs(String list_of_ids) {
		String[] listOfIds = list_of_ids.split(",");
		game.setTopDominoInPile(getdominoByID(Integer.decode(listOfIds[0])));
		Domino domino = game.getTopDominoInPile();
		for(int i = 1; i < 5; i++) {
			domino.setNextDomino(getdominoByID(Integer.decode(listOfIds[i])));
			domino = domino.getNextDomino();
		}
	}

	@When("create next draft is initiated")
	public void create_next_draft_is_initiated() {
		draftNum = game.getAllDrafts().size();
		KingdominoController.createNextDraftOfDominoes(draftNum);
	}
	
	@Then("a new draft is created from dominoes {string}")
	public void a_new_draft_is_created_from_dominoes(String draft_ids) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		ArrayList<Domino> listOfDominos = new ArrayList<Domino>();
		for(int i = 0; i < game.getNextDraft().numberOfIdSortedDominos(); i++) {
			list.add(game.getNextDraft().getIdSortedDomino(i).getId());
		}
		
		String[] listOfIds = draft_ids.split(",");
		assertEquals(listOfIds.length, list.size());
		
		Draft draft = game.getNextDraft();
		for(int i = 0; i < listOfIds.length; i++) {
			listOfDominos.add(getdominoByID(Integer.decode(listOfIds[i])));
		}
		
		assertEquals(listOfDominos, draft.getIdSortedDominos());
		for(int i = 0; i < draft.getIdSortedDominos().size(); i++) {
			assertEquals(DominoStatus.InNextDraft, draft.getIdSortedDomino(i).getStatus());
		}
	}
	
	@Then("the next draft now has the dominoes {string}")
	public void the_next_draft_now_has_the_dominoes(String draft_ids) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < game.getNextDraft().numberOfIdSortedDominos(); i++) {
			list.add(game.getNextDraft().getIdSortedDomino(i).getId());
		}
		String[] listOfIds = draft_ids.split(",");
		for(int i = 0; i < listOfIds.length; i++) {
			assertEquals(Integer.decode(listOfIds[i]), list.get(i));
		}
	}
	
	@Then("the dominoes in the next draft are face down")
	public void the_dominoes_in_the_next_draft_are_face_down() {
		Draft draft = game.getCurrentDraft();
		assertEquals(DraftStatus.FaceDown, draft.getDraftStatus());
	}
	
	@Then("the top domino of the pile is ID {int}")
	public void the_top_domino_of_the_pile_is_ID(int topId) {
		assertEquals(getdominoByID(topId), game.getTopDominoInPile());
		game.setCurrentDraft(game.getNextDraft());
	}
	
	@Then("the former next draft is now the current draft")
	public void the_former_next_draft_is_now_the_current_draft() {
		assertEquals(game.getCurrentDraft(), game.getNextDraft());
	}
	
	@Then("the pile is empty")
	public void the_pile_is_empty() {
		assertEquals(null, game.getTopDominoInPile());
	}
	
	@Then("there is no next draft")
	public void there_is_no_next_draft() {
		Draft draft = game.getNextDraft();
		assertEquals(null, draft);
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
}
