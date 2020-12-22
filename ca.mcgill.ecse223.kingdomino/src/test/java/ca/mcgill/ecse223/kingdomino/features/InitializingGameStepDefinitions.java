package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Game;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * 
 * @author Noah Chamberland
 *
 */
public class InitializingGameStepDefinitions {
	
	@Given("the game has not been started")
	public void the_game_has_not_been_started() {
		KingdominoController.initializeGame();
	}
	
	@When("start of the game is initiated")
	public void start_of_the_game_is_initiated() {
		KingdominoController.startNewGame();
	}
	
	@Then("the pile shall be shuffled")
	public void the_pile_shall_be_shuffled() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		boolean orderNotSame = false;
		Domino cur = game.getTopDominoInPile();
		for(int i = 0; i < game.getAllDominos().size(); i++) {
			if(game.getAllDomino(i).getId() != cur.getId()) {
				orderNotSame = true;
				break;
			}
			cur = cur.getNextDomino();
		}
		assertEquals(true, orderNotSame);
	}
	
	@Then("the first draft shall be on the table")
	public void the_first_draft_shall_be_on_the_table() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		assertEquals(1, game.getAllDrafts().size());
	}
	
	@Then("the first draft shall be revealed")
	public void the_first_draft_shall_be_revealed() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		KingdominoController.orderAndRevealNextDraft();
		assertEquals(DraftStatus.FaceUp, game.getAllDraft(0).getDraftStatus());
	}
	
	@Then("the initial order of players shall be determined")
	public void the_initial_order_of_players_shall_be_determined() {
		boolean hasNextPlayer = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer() != null;
		assertEquals(true, hasNextPlayer);
	}
	
	@Then("the first player shall be selecting his\\/her first domino of the game")
	public void the_first_player_shall_be_selecting_his_her_first_domino_of_the_game() {
		boolean hasSelection = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().hasDominoSelection();
		assertEquals(false, hasSelection);
	}
	
	@Then("the second draft shall be on the table, face down")
	public void the_second_draft_shall_be_on_the_table_face_down() {
		KingdominoController.createNextDraftOfDominoes(1);
		assertEquals(DraftStatus.FaceDown, KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft().getDraftStatus());
	}
}
