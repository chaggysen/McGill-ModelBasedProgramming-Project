package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Castle;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.DominoSelection;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.model.User;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ChooseNextDominoStepDefinitions {
	/*
	 * @author Sen Wang
	 */
	private Kingdomino kingdomino = new Kingdomino();
	private Game game = new Game(48, kingdomino);;
	ArrayList <Integer> dominoIDs = new ArrayList <Integer>();

	@Given("the game is initialized for choose next domino")
	public void the_game_is_initialized_for_choose_next_domino() {
		// Intialize empty game
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game);
		// Populate game
		addDefaultUsersAndPlayers(game);
		createAllDominoes(game);
		//game.setNextPlayer(game.getPlayer(0));
		KingdominoApplication.setKingdomino(kingdomino);
		Draft draft = new Draft(DraftStatus.FaceUp, game);
		game.setCurrentDraft(draft);
		/*DominoSelection selection1 = new DominoSelection(game.getPlayers().get(0), getdominoByID(1),draft);
		DominoSelection selection2 = new DominoSelection(game.getPlayers().get(1), getdominoByID(2),draft);
		DominoSelection selection3 = new DominoSelection(game.getPlayers().get(2), getdominoByID(3),draft);
		DominoSelection selection4 = new DominoSelection(game.getPlayers().get(3), getdominoByID(4),draft);
		draft.addSelection(selection1);
		draft.addSelection(selection2);
		draft.addSelection(selection3);
		draft.addSelection(selection4);*/
	}

	@Given("the next draft is sorted with dominoes {string}")
	public void the_next_draft_is_sorted_with_dominoes(String string) {
		
		Draft nextDraft = new Draft (DraftStatus.FaceUp, game);
		game.setNextDraft(nextDraft);
		
		String[] dominos = string.split(",");
		for (int i = 0; i < dominos.length; i++) {
			dominoIDs.add(Integer.decode(dominos[i]));
		}
		
		for (int i = 0; i < dominoIDs.size(); i++) {
			game.getNextDraft().addIdSortedDomino(getdominoByID(dominoIDs.get(i)));
		}
	    
		
	}

	@Given("player's domino selection {string}")
	public void player_s_domino_selection(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		ArrayList <Player> playerSelection = new ArrayList <Player>();
		String [] playerColorList = string.split(",");
		
		for (int i = 0; i < playerColorList.length; i++) {
			int player_id = 0;
			
			PlayerColor playerColor;
			
			if (!playerColorList[i].equals("none")) {
				playerColor = getColor(playerColorList[i]);
				if (playerColor == PlayerColor.Blue) {
					player_id = 0;
				}else if (playerColor == PlayerColor.Green) {
					player_id = 1;
				}else if (playerColor == PlayerColor.Yellow) {
					player_id = 2;
				}else if (playerColor == PlayerColor.Pink) {
					player_id = 3;
				}else {
					player_id = (Integer) null;
				}
				playerSelection.add(game.getPlayer(player_id));
			}else {
				playerSelection.add(null);
			}
		    
		}
		
		for (int i = 0; i < playerSelection.size(); i++) {
			for (int j = 0; j < dominoIDs.size(); j++) {
				if (i == j) {
					if (playerSelection.get(j) != null) {
						DominoSelection selection = new DominoSelection(playerSelection.get(j),getdominoByID(dominoIDs.get(i)), game.getNextDraft());
						game.getNextDraft().addSelection(selection);
					}
				}
			}
		}
		
	}

	@Given("the current player is {string}")
	public void the_current_player_is(String string) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		
		PlayerColor playerColor = getColor(string);
		Integer player_id = null;
		
		if (playerColor == PlayerColor.Blue) {
			player_id = 0;
		}else if (playerColor == PlayerColor.Green) {
			player_id = 1;
		}else if (playerColor == PlayerColor.Yellow) {
			player_id = 2;
		}else if (playerColor == PlayerColor.Pink) {
			player_id = 3;
		}
		Player player = game.getPlayer(player_id);
		game.setNextPlayer(player);

	}

	@When("current player chooses to place king on {int}")
	public void current_player_chooses_to_place_king_on(Integer int1) {

	    KingdominoController.chooseNextDomino(int1);
	}

	@Then("current player king now is on {string}")
	public void current_player_king_now_is_on(String index) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		int actualID = game.getPlayer(3).getDominoSelection().getDomino().getId();

	    assertEquals(Integer.parseInt(index), actualID);
	    
	}

	@Then("the selection for next draft is now equal to {string}")
	public void the_selection_for_next_draft_is_now_equal_to(String string) {

		
		String[] newSelection = string.split(",");
		ArrayList <String> expectedSelectionArrayList = new ArrayList <String>();
		
		for (int i = 0; i < newSelection.length; i++) {
			if (!newSelection[i].equals("none")) {
			expectedSelectionArrayList.add(newSelection[i]);
		  }
		}
		
		ArrayList <String> actualSelectionArrayList = new ArrayList <String>();
		
		for (int i = 0; i < game.getNextDraft().getSelections().size(); i++) {
			actualSelectionArrayList.add(getColorString(game.getNextDraft().getSelections().get(i).getPlayer().getColor()));
		}
		
		assertEquals(expectedSelectionArrayList, actualSelectionArrayList);

	}

	@Then("the selection for the next draft selection is still {string}")
	public void the_selection_for_the_next_draft_selection_is_still(String string) {
		
		String[] newSelection = string.split(",");
		ArrayList <String> expectedSelectionArrayList = new ArrayList <String>();
		
		for (int i = 0; i < newSelection.length; i++) {
			if (!newSelection[i].equals("none")) {
			expectedSelectionArrayList.add(newSelection[i]);
		  }
		}
		
		ArrayList <String> actualSelectionArrayList = new ArrayList <String>();
		
		for (int i = 0; i < game.getNextDraft().getSelections().size(); i++) {
			actualSelectionArrayList.add(getColorString(game.getNextDraft().getSelections().get(i).getPlayer().getColor()));
		}
		
		assertEquals(expectedSelectionArrayList, actualSelectionArrayList);


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
	
	private String getColorString (PlayerColor color) {
		switch (color) {
		case Blue:
			return "blue";
		case Green:
			return "green";
		case Pink:
			return "pink";
		case Yellow:
			return "yellow";
			
		default:
			return "none";
		
		}	
	}
	
	private PlayerColor getColor(String color) {
		switch (color) {
		case "blue":
			return PlayerColor.Blue;
		case "green":
			return PlayerColor.Green;
		case "pink":
			return PlayerColor.Pink;
		case "yellow":
			return PlayerColor.Yellow;
		default:
			throw new java.lang.IllegalArgumentException("Invalid player color: " + color);
		}
	}
}
