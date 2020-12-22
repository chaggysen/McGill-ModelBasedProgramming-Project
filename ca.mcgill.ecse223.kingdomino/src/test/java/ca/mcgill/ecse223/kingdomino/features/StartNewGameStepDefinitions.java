package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Castle;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import ca.mcgill.ecse223.kingdomino.model.Property;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.model.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * 
 * @author Noah Chamberland
 *
 */
public class StartNewGameStepDefinitions {
	
	@Given("the program is started and ready for starting a new game")
	public void the_program_is_started_and_ready_for_starting_a_new_game() {
		KingdominoController.initializeGame();
	}

	@Given("there are four selected players")
	public void there_are_four_selected_players() {
		KingdominoController.setPlayerNumber(4);
	}

	@Given("bonus options Harmony and MiddleKingdom are selected")
	public void bonus_options_Harmony_and_MiddleKingdom_are_selected() {
		KingdominoController.setBonusOptions(true, true);
	}

	@When("starting a new game is initiated")
	public void starting_a_new_game_is_initiated() {
		KingdominoController.startNewGame();
	}

	@When("reveal first draft is initiated")
	public void reveal_first_draft_is_initiated() {
		
		KingdominoController.orderAndRevealNextDraft();

	}

	@Then("all kingdoms shall be initialized with a single castle")
	public void all_kingdoms_shall_be_initialized_with_a_single_castle() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for (int i = 0; i < game.getPlayers().size(); i++) {
			int size = game.getPlayer(i).getKingdom().getTerritories().size();
			assertEquals(1, size);
		}
	}

	@Then("all castle are placed at {int}:{int} in their respective kingdoms")
	public void all_castle_are_placed_at_in_their_respective_kingdoms(int x, int y) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for (int i = 0; i < game.getPlayers().size(); i++) {
			Castle castle = (Castle) game.getPlayer(i).getKingdom().getTerritory(0);
			assertEquals(x, castle.getX());
			assertEquals(y, castle.getY());
		}
	}

	@Then("the first draft of dominoes is revealed")
	public void the_first_draft_of_dominoes_is_revealed() {
		assertEquals(1, KingdominoApplication.getKingdomino().getCurrentGame().getAllDrafts().size());
	}

	@Then("all the dominoes form the first draft are facing up")
	public void all_the_dominoes_form_the_first_draft_are_facing_up() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		assertEquals(DraftStatus.FaceUp, game.getCurrentDraft().getDraftStatus());

	}

	@Then("all the players have no properties")
	public void all_the_players_have_no_properties() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for (int i = 0; i < game.getPlayers().size(); i++) {
			assertEquals(0, game.getPlayer(i).getKingdom().getProperties().size());
		}
	}

	@Then("all player scores are initialized to zero")
	public void all_player_scores_are_initialized_to_zero() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for (int i = 0; i < game.getPlayers().size(); i++) {
			assertEquals(0, game.getPlayer(i).getTotalScore());
		}
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