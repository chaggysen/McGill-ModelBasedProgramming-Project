package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.BonusOption;
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
public class SetGameOptionsStepDefinitions {
	BonusOption bonusHarmony;
	BonusOption bonusMK;
	@Given("the game is initialized for set game options")
	public void the_game_is_initialized_for_set_game_options() {
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		kingdomino.setCurrentGame(game);
		createAllDominoes(game);
		KingdominoApplication.setKingdomino(kingdomino);
		bonusMK = new BonusOption("Middle Kingdom", kingdomino);
		bonusHarmony = new BonusOption("Harmony", kingdomino);
	}

	@When("set game options is initiated")
	public void set_game_options_is_initiated() {
	    
	}

	@When("the number of players is set to {int}")
	public void the_number_of_players_is_set_to(Integer int1) {
		KingdominoController.setPlayerNumber(int1);
	}

	@When("Harmony {string} selected as bonus option")
	public void harmony_selected_as_bonus_option(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		boolean harmony = false;
		if(string.equals("is")) harmony = true;
		boolean mk = game.getSelectedBonusOptions().contains(bonusMK);
		KingdominoController.setBonusOptions(harmony, mk);
	}

	@When("Middle Kingdom {string} selected as bonus option")
	public void middle_Kingdom_selected_as_bonus_option(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		boolean mk = false;
		if(string.equals("is")) mk = true;
		boolean harmony =  game.getSelectedBonusOptions().contains(bonusHarmony);;
		KingdominoController.setBonusOptions(harmony, mk);
	}

	@Then("the number of players shall be {int}")
	public void the_number_of_players_shall_be(int int1) {
		assertEquals(int1, KingdominoApplication.getKingdomino().getCurrentGame().getNumberOfPlayers());
	}

	@Then("Harmony {string} an active bonus")
	public void harmony_an_active_bonus(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		boolean is = string.equals("is");
		assertEquals(is, game.getSelectedBonusOptions().contains(bonusHarmony));
	}
	
	@Then("Middle Kingdom {string} an active bonus")
	public void middle_Kingdom_an_active_bonus(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		boolean is = string.equals("is");
		assertEquals(is, game.getSelectedBonusOptions().contains(bonusMK));
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
		default:
			throw new java.lang.IllegalArgumentException("Invalid terrain type: " + terrain);
		}
	}
}