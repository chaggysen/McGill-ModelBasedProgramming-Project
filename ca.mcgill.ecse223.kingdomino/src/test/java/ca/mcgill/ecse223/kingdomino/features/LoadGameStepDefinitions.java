package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * 
 * @author Noah Chamberland
 *
 */
public class LoadGameStepDefinitions {
	
	private Kingdomino kd;
	private Game game;
	private String message = "";
	
	@Given("the game is initialized for load game")
	public void the_game_is_initialized_for_load_game() {
		kd = KingdominoApplication.getKingdomino();
	}
	
	@When("I initiate loading a saved game from {string}")
	public void i_initiate_loading_a_saved_game_from(String filename) {
		try
		{
			KingdominoController.loadGame(filename);
			game = kd.getCurrentGame();
		}
		catch(Exception e) {
			message = e.getMessage();
		}
	}
	
	@When("each tile placement is valid")
	public void each_tile_placement_is_valid() {
		assertEquals(true, message.length()==0);
	}
	
	@When("the game result is not yet final")
	public void the_game_result_is_not_yet_final() {
		for(int i =  0; i < game.numberOfPlayers(); i++) {
			assertEquals(1, game.getPlayer(i).getCurrentRanking());
		}
	}
	
	@Then("it shall be player {int}'s turn")
	public void it_shall_be_player_turn(Integer player){
		int number = 0;
		for(int i = 0; i < game.getPlayers().size(); i++) {
			if(game.getPlayer(i).equals(game.getNextPlayer())) number = i;
		}
		assertEquals(player - 1, number);
	}
	
	@Then("each of the players should have the corresponding tiles on their grid:")
	public void each_of_the_players_should_have_the_corresponding_tiles_on_their_grid(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			// Get values from cucumber table
			Integer playerNumber = Integer.decode(map.get("playerNumber"));
			String[] playerTiles = map.get("playerTiles").split(",");
			ArrayList<Integer> tiles = new ArrayList<Integer>();
			for(int i = 0; i < playerTiles.length; i++) {
				tiles.add(Integer.decode(playerTiles[i]));
			}
			for(int i = 0; i < tiles.size(); i++) {
				if(game.getPlayer(playerNumber-1).getKingdom().getTerritory(i+1) instanceof DominoInKingdom) {
					DominoInKingdom domino = (DominoInKingdom) game.getPlayer(playerNumber-1).getKingdom().getTerritory(i+1);
					assertEquals(tiles.get(i), (Integer) domino.getDomino().getId());
				}
			}
		}
	}
	
	@Then("each of the players should have claimed the corresponding tiles:")
	public void each_of_the_players_should_have_claimed_the_corresponding_tiles(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			// Get values from cucumber table
			Integer playerNumber = Integer.decode(map.get("playerNumber"));
			int claimedTile = Integer.decode(map.get("claimedTile"));
			assertEquals(getdominoByID(claimedTile), game.getPlayer(playerNumber-1).getDominoSelection().getDomino());
		}
	}
	
	@Then("tiles {string} shall be unclaimed on the board")
	public void tiles_shall_be_unclaimed_on_the_board(String unclaimed) {
		String[] array = unclaimed.split(",");
		for(int i = 0; i < array.length; i++) {
			array[i] = array[i].trim();
		}
		for(int i = 0; i < array.length; i++) {
			assertEquals(DominoStatus.InNextDraft, getdominoByID(Integer.decode(array[i])).getStatus());
		}
	}

	@Then("the game shall notify the user that the loaded game is invalid")
	public void the_game_shall_notify_the_user_that_the_loaded_game_is_invalid() {
		assertEquals("Invalid File", message);
	}
	
	@Then("the game shall become ready to start")
	public void the_game_shall_become_ready_to_start() {
	    // Write code here that turns the phrase above into concrete actions
	    assertEquals("", message);
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
}
