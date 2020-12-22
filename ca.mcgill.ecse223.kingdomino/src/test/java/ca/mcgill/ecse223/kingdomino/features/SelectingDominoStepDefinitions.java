package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.model.Gameplay;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * 
 * @author Noah Chamberland
 *
 */
public class SelectingDominoStepDefinitions {
	
	@Given("the game has been initialized for selecting domino")
	public void the_game_has_been_initialized_for_selecting_domino() {
	    Gameplay gameplay = new Gameplay();
	    gameplay.setGamestatus("InGame");
	}

	@Given("the order of players is {string}")
	public void the_order_of_players_is(String string) {
	    
	}

	@Given("the {string} is selecting his\\/her domino with ID {int}")
	public void the_is_selecting_his_her_domino_with_ID(String string, Integer int1) {
	    
	}

	@Given("the {string} player is selecting his\\/her first domino of the game with ID {int}")
	public void the_player_is_selecting_his_her_first_domino_of_the_game_with_ID(String string, Integer int1) {

	}
	@Given("the next draft has the dominoes with ID {string}")
	public void the_next_draft_has_the_dominoes_with_ID(String string) {
	   
	}
}
