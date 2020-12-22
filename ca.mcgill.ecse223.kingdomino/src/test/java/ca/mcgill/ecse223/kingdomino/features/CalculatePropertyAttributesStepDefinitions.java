package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.InvalidInputException;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Castle;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.model.User;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CalculatePropertyAttributesStepDefinitions {
	
	/*
	 * @author Sen Wang
	 */

	private Kingdomino kingdomino = new Kingdomino();
	private Game game = new Game(48, kingdomino);;

	@Given("the game is initialized for calculate property attributes")
	public void the_game_is_initialized_for_calculate_property_attributes() {
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

	//
	// @Given("the player's kingdom has the following dominoes:") public void
	// the_player_s_kingdom_has_the_following_dominoes(io.cucumber.datatable.
	// DataTable dataTable) { Duplicate step definition (already in
	// calculatePlayerScoreStepDefinition) }

	/*
	 * @Given("the player's kingdom has the following dominoes:") public void
	 * the_player_s_kingdom_has_the_following_dominoes(io.cucumber.datatable.
	 * DataTable dataTable) { //Duplicate step definition (already in
	 * calculatePlayerScoreStepDefinition) }
	 */

	@When("calculate property attributes is initiated")
	public void calculate_property_attributes_is_initiated() throws InvalidInputException {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();

		for (int i = 0; i < game.getPlayers().size(); i++) {
			KingdominoController.calculatePropertyAttribute(game.getPlayers().get(i).getKingdom());

		}
	}

	@Then("the player shall have a total of {int} properties")
	public void the_player_shall_have_a_total_of_properties(Integer int1) {

		assertEquals((int) int1, (int) game.getPlayers().get(0).getKingdom().getProperties().size());
	}

	@Then("the player shall have properties with the following attributes:")
	public void the_player_shall_have_properties_with_the_following_attributes(
			io.cucumber.datatable.DataTable dataTable) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		int propertyIndex = 0;
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			// Get values from cucumber table
			String terrainType = map.get("type");
			Integer size = Integer.decode(map.get("size"));
			Integer crowns = Integer.decode(map.get("crowns"));

			for (int i = 0; i < game.getPlayers().get(0).getKingdom().getProperties().size(); i++) {
				boolean found;
				if (game.getPlayers().get(0).getKingdom().getProperties().get(i).getPropertyType()
						.equals(getTerrainType(terrainType))
						&& game.getPlayers().get(0).getKingdom().getProperties().get(i).getSize() == size
						&& game.getPlayers().get(0).getKingdom().getProperties().get(i).getCrowns() == crowns) {
					found = true;
					assertEquals(true, found);
				}
			}

		}

	}

	@Given("the player's kingdom also includes the domino {int} at position {int}:{int} with the direction {string}")
	public void the_player_s_kingdom_also_includes_the_domino_at_position_with_the_direction(Integer int1, Integer int2,
			Integer int3, String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Domino thisdo = getdominoByID(int1);
		DirectionKind dir = getDirection(string);
		Integer posx = int2;
		Integer posy = int3;
		Kingdom kingdom = game.getPlayer(0).getKingdom();
		DominoInKingdom dom = new DominoInKingdom(posx, posy, kingdom, thisdo);
		dom.setDirection(dir);
	}

///////////////////////////////////////
/// -----Private Helper Methods---- ///
///////////////////////////////////////

	private Domino getdominoByID(int id) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for (Domino domino : game.getAllDominos()) {
			if (domino.getId() == id) {
				return domino;
			}
		}
		throw new java.lang.IllegalArgumentException("Domino with ID " + id + " not found.");
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
		case "wheat":
			return TerrainType.WheatField;
		case "forest":
			return TerrainType.Forest;
		case "mountain":
			return TerrainType.Mountain;
		case "grass":
			return TerrainType.Grass;
		case "swamp":
			return TerrainType.Swamp;
		case "lake":
			return TerrainType.Lake;
		default:
			throw new java.lang.IllegalArgumentException("Invalid terrain type: " + terrain);
		}
	}
}