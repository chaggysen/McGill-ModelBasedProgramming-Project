package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * 
 * @author Noah Chamberland
 *
 */
public class BrowseDominoPileStepDefinitions {
	
	Domino domino;
	List<Domino> list;
	@Given("the program is started and ready for browsing dominoes")
	public void the_program_is_started_and_ready_for_browsing_dominoes() {
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		kingdomino.setCurrentGame(game);
		createAllDominoes(game);
		KingdominoApplication.setKingdomino(kingdomino);
	}

	@When("I initiate the browsing of all dominoes")
	public void i_initiate_the_browsing_of_all_dominoes() {
	    list = KingdominoController.browseAllDomino();
	}

	@Then("all the dominoes are listed in increasing order of identifiers")
	public void all_the_dominoes_are_listed_in_increasing_order_of_identifiers() {
		
		for(int i = 0; i < KingdominoApplication.getKingdomino().getCurrentGame().getAllDominos().size(); i++) {
			assertEquals(getdominoByID(i+1), list.get(i));
		}
	}

	@When("I provide a domino ID {int}")
	public void i_provide_a_domino_ID(Integer int1) {
		domino = KingdominoController.observeDomino(int1);
	}

	@Then("the listed domino has {string} left terrain")
	public void the_listed_domino_has_left_terrain(String string) {
		assertEquals(getTerrainType(string), domino.getLeftTile());
	}

	@Then("the listed domino has {string} right terrain")
	public void the_listed_domino_has_right_terrain(String string) {
		assertEquals(getTerrainType(string), domino.getRightTile());
	}

	@Then("the listed domino has {int} crowns")
	public void the_listed_domino_has_crowns(int int1) {
		assertEquals(int1, domino.getLeftCrown() + domino.getRightCrown());

	}

	@When("I initiate the browsing of all dominoes of {string} terrain type")
	public void i_initiate_the_browsing_of_all_dominoes_of_terrain_type(String string) {
		list = KingdominoController.filterByTerrainType(string);
	}

	@Then("list of dominoes with IDs {string} should be shown")
	public void list_of_dominoes_with_IDs_should_be_shown(String string) {
		String[] dominos = string.split(",");
		ArrayList<Domino> array = new ArrayList<Domino>();
		for(String domino: dominos) {
			array.add(getdominoByID(Integer.decode(domino)));
		}
		for(int i = 0; i < array.size(); i++) {
			assertEquals(array.get(i), list.get(i));
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
