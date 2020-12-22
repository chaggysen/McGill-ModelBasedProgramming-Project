package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Gameplay;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * 
 * @author Noah Chamberland
 *
 */
public class SortingAndRevealingDraftStepDefinitions {
	Gameplay gameplay;
	@Given("there is a next draft, face down")
	public void there_is_a_next_draft_face_down() {
		KingdominoController.initializeGame();
		KingdominoController.setPlayerNumber(4);
	    gameplay = new Gameplay();
	    gameplay.initReady();
	}

	@Given("all dominoes in current draft are selected")
	public void all_dominoes_in_current_draft_are_selected() {
	    Game game = KingdominoApplication.getKingdomino().getCurrentGame();
	    for(int i = 0; i < 4; i++) {
		    gameplay.selectDom(game.getNextDraft().getIdSortedDomino(i).getId());
		    gameplay.selectReady();
	    }
	}

	@When("next draft is sorted")
	public void next_draft_is_sorted() {
	   //Auto-transition
	}

	@When("next draft is revealed")
	public void next_draft_is_revealed() {
	    Game game = KingdominoApplication.getKingdomino().getCurrentGame();
	    game.getNextDraft().setDraftStatus(DraftStatus.FaceUp);
	}

	@Then("the next draft shall be sorted")
	public void the_next_draft_shall_be_sorted() {
	    Game game = KingdominoApplication.getKingdomino().getCurrentGame();
	    ArrayList<Integer> list = new ArrayList<Integer>();
	    for(int i = 0; i < 4; i++) {
	    	list.add(game.getNextDraft().getIdSortedDomino(i).getId());
	    }
	    Collections.sort(list);
	    assertEquals((int)list.get(0), game.getNextDraft().getIdSortedDomino(0).getId());
	    assertEquals((int)list.get(1), game.getNextDraft().getIdSortedDomino(1).getId());
	    assertEquals((int)list.get(2), game.getNextDraft().getIdSortedDomino(2).getId());
	    assertEquals((int)list.get(3), game.getNextDraft().getIdSortedDomino(3).getId());
	}

	@Then("the next draft shall be facing up")
	public void the_next_draft_shall_be_facing_up() {
	    Game game = KingdominoApplication.getKingdomino().getCurrentGame();
	    assertEquals(DraftStatus.FaceUp, game.getNextDraft().getDraftStatus());
	}

	@Then("it shall be the player's turn with the lowest domino ID selection")
	public void it_shall_be_the_player_s_turn_with_the_lowest_domino_ID_selection() {
	    Game game = KingdominoApplication.getKingdomino().getCurrentGame();
	    assertEquals(game.getCurrentDraft().getIdSortedDomino(0).getDominoSelection().getPlayer(), game.getNextPlayer());
	}
}
