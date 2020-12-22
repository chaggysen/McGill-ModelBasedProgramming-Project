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
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.model.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.DominoSelection;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;

/**
 * 
 * @author Noah Chamberland
 *
 */
public class MoveCurrentDominoStepDefinitions {

	@Given("the game is initialized for move current domino")
	public void the_game_is_initialized_for_move_current_domino() {
		// Intialize empty game
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
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
	
	@Given("it is {string}'s turn")
	public void it_is_s_turn(String player) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for(int i = 0; i < game.getNumberOfPlayers(); i++) {
			if(game.getPlayer(i).getColor() == getPlayerColor(player)) game.setNextPlayer(game.getPlayer(i));
		}
	}
	
	@Given("{string}'s kingdom has following dominoes:")
	public void s_kingdom_has_following_dominoes(String player, io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> valueMaps = dataTable.asMaps();		
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Kingdom kingdom = null;
		for(int i = 0; i < game.getNumberOfPlayers(); i++) {
			if(game.getPlayer(i).getColor() == getPlayerColor(player)) kingdom = game.getPlayer(i).getKingdom();
		}
		for (Map<String, String> map : valueMaps) {
			// Get values from cucumber table
			Integer id = Integer.decode(map.get("id"));
			DirectionKind dir = getDirection(map.get("dir"));
			Integer posx = Integer.decode(map.get("posx"));
			Integer posy = Integer.decode(map.get("posy"));

			// Add the domino to a player's kingdom
			Domino dominoToPlace = getdominoByID(id);
			DominoInKingdom domInKingdom = new DominoInKingdom(posx, posy, kingdom, dominoToPlace);
			domInKingdom.setDirection(dir);
			dominoToPlace.setStatus(DominoStatus.PlacedInKingdom);
		}
	}
	
	@When("{string} has selected domino {int}")
	public void has_selected_domino(String player, int id) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = null;
		for(int i = 0; i < game.getNumberOfPlayers(); i++) {
			if(game.getPlayer(i).getColor() == getPlayerColor(player)) aPlayer = game.getPlayer(i);
		}
		aPlayer.setDominoSelection(new DominoSelection(aPlayer, getdominoByID(id), game.getCurrentDraft()));	
	}
	
	@When("{string} removes his king from the domino {int}")
	public void removes_his_king_from_the_domino(String player, int id) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = null;
		for(int i = 0; i < game.getNumberOfPlayers(); i++) {
			if(game.getPlayer(i).getColor() == getPlayerColor(player)) aPlayer = game.getPlayer(i);
		}
		KingdominoController.initPlacement(aPlayer, getdominoByID(id));
	}
	
	@Then("domino {int} should be tentative placed at position {int}:{int} of {string}'s kingdom with ErroneouslyPreplaced status")
	public void domino_should_be_tentative_placed_at_position_of_s_kingdom_with_ErroneouslyPreplaced_status(int id, int x, int y, String player) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = null;
		for(int i = 0; i < game.getNumberOfPlayers(); i++) {
			if(game.getPlayer(i).getColor() == getPlayerColor(player)) aPlayer = game.getPlayer(i);
		}
		DominoInKingdom dom = KingdominoController.getDominoInKingdomByDomino(getdominoByID(id), aPlayer.getKingdom());
		assertEquals(x, dom.getX());
		assertEquals(y, dom.getY());
		assertEquals(dom.getKingdom(), aPlayer.getKingdom());
		assertEquals(getDominoStatus("ErroneouslyPreplaced"), dom.getDomino().getStatus());
	}
	
	/*@Given("domino {int} is tentatively placed at position {int}:{int} with direction {string}")
	public void domino_is_tentatively_placed_at_position_with_direction(int id, int x, int y, String direction) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = game.getNextPlayer();
		KingdominoController.placeDomino(aPlayer, getdominoByID(id), x, y, getDirection(direction), false);
	}*/
	
	@Given("domino {int} has status {string}")
	public void domino_has_status(int id, String dstatus) {
	    getdominoByID(id).setStatus(getDominoStatus(dstatus));
	}
	
	@When("{string} requests to move the domino {string}")
	public void requests_to_move_the_domino(String player, String movement) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = null;
		for(int i = 0; i < game.getNumberOfPlayers(); i++) {
			if(game.getPlayer(i).getColor() == getPlayerColor(player)) aPlayer = game.getPlayer(i);
		}
		KingdominoController.moveCurrentDomino(KingdominoController.getDominoInKingdomByDomino(aPlayer.getDominoSelection().getDomino(), aPlayer.getKingdom()), movement, aPlayer);
	}
	
	@Then("the domino {int} should be tentatively placed at position {int}:{int} with direction {string}")
	public void the_domino_should_be_tentatively_placed_at_position_with_direction(int id, int x, int y, String direction) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = game.getNextPlayer();
		DominoInKingdom dom = KingdominoController.getDominoInKingdomByDomino(getdominoByID(id), aPlayer.getKingdom());
		assertEquals(x, dom.getX());
		assertEquals(y, dom.getY());
		assertEquals(getDirection(direction), dom.getDirection());	
	}
	
	@Then("the new status of the domino is {string}")
	public void the_new_status_of_the_domino_is(String status) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = game.getNextPlayer();
		assertEquals(getDominoStatus(status), aPlayer.getDominoSelection().getDomino().getStatus());
	}
	
	@Then("the domino {int} is still tentatively placed at position {int}:{int}")
	public void the_domino_is_still_tentatively_placed_at_position(int id, int x, int y) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = game.getNextPlayer();
		DominoInKingdom dom = KingdominoController.getDominoInKingdomByDomino(getdominoByID(id), aPlayer.getKingdom());
		assertEquals(x, dom.getX());
		assertEquals(y, dom.getY());
	}
	
	@Then("the domino should still have status {string}")
	public void the_domino_should_still_have_status(String status) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = game.getNextPlayer();
		assertEquals(getDominoStatus(status), aPlayer.getDominoSelection().getDomino().getStatus());
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
		case "InPile":
			return DominoStatus.InPile;
		case "Excluded":
			return DominoStatus.Excluded;
		case "InCurrentDraft":
			return DominoStatus.InCurrentDraft;
		case "InNextDraft":
			return DominoStatus.InNextDraft;
		case "ErroneouslyPreplaced":
			return DominoStatus.ErroneouslyPreplaced;
		case "CorrectlyPreplaced":
			return DominoStatus.CorrectlyPreplaced;
		case "PlacedInKingdom":
			return DominoStatus.PlacedInKingdom;
		case "Discarded":
			return DominoStatus.Discarded;
		default:
			throw new java.lang.IllegalArgumentException("Invalid domino status: " + status);
		}
	}
	
	private PlayerColor getPlayerColor(String color) {
		switch (color) {
		case "pink":
			return PlayerColor.Pink;
		case "blue":
			return PlayerColor.Blue;
		case "yellow":
			return PlayerColor.Yellow;
		case "green":
			return PlayerColor.Green;
		default:
			throw new java.lang.IllegalArgumentException("Invalid player color: " + color);
		}
	}
/*
	private Game game;
	private Player aPlayer;
	private Kingdom kingdom;
	private Draft draft;
	private DominoInKingdom domino;

	
	@Given("the game is initialized for move current domino")
	public void the_game_is_initialized_for_move_current_domino() {
		// Intialize empty game
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
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
	
	@Given("it is {string}'s turn")
	public void it_is_s_turn(String player) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for(int i = 0; i < game.getNumberOfPlayers(); i++) {
			if(game.getPlayer(i).getColor() == getPlayerColor(player)) game.setNextPlayer(game.getPlayer(i));
		}
	}
	
	@Given("{string}'s kingdom has following dominoes:")
	public void s_kingdom_has_following_dominoes(String player, io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> valueMaps = dataTable.asMaps();		
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Kingdom kingdom = null;
		for(int i = 0; i < game.getNumberOfPlayers(); i++) {
			if(game.getPlayer(i).getColor() == getPlayerColor(player)) kingdom = game.getPlayer(i).getKingdom();
		}
		for (Map<String, String> map : valueMaps) {
			// Get values from cucumber table
			Integer id = Integer.decode(map.get("id"));
			DirectionKind dir = getDirection(map.get("dir"));
			Integer posx = Integer.decode(map.get("posx"));
			Integer posy = Integer.decode(map.get("posy"));

			// Add the domino to a player's kingdom
			Domino dominoToPlace = getdominoByID(id);
			DominoInKingdom domInKingdom = new DominoInKingdom(posx, posy, kingdom, dominoToPlace);
			domInKingdom.setDirection(dir);
			dominoToPlace.setStatus(DominoStatus.PlacedInKingdom);
		}
	}
	
	@When("{string} has selected domino {int}")
	public void has_selected_domino(String player, int id) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = null;
		for(int i = 0; i < game.getNumberOfPlayers(); i++) {
			if(game.getPlayer(i).getColor() == getPlayerColor(player)) aPlayer = game.getPlayer(i);
		}
		aPlayer.setDominoSelection(new DominoSelection(aPlayer, getdominoByID(id), game.getCurrentDraft()));	
	}
	
	@When("{string} removes his king from the domino {int}")
	public void removes_his_king_from_the_domino(String player, int id) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = null;
		for(int i = 0; i < game.getNumberOfPlayers(); i++) {
			if(game.getPlayer(i).getColor() == getPlayerColor(player)) aPlayer = game.getPlayer(i);
		}
		KingdominoController.initPlacement(aPlayer, getdominoByID(id));
	}
	
	@Then("domino {int} should be tentative placed at position {int}:{int} of {string}'s kingdom with ErroneouslyPreplaced status")
	public void domino_should_be_tentative_placed_at_position_of_s_kingdom_with_ErroneouslyPreplaced_status(int id, int x, int y, String player) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = null;
		for(int i = 0; i < game.getNumberOfPlayers(); i++) {
			if(game.getPlayer(i).getColor() == getPlayerColor(player)) aPlayer = game.getPlayer(i);
		}
		DominoInKingdom dom = KingdominoController.getDominoInKingdomByDomino(getdominoByID(id), aPlayer.getKingdom());
		assertEquals(x, dom.getX());
		assertEquals(y, dom.getY());
		assertEquals(dom.getKingdom(), aPlayer.getKingdom());
		assertEquals(getDominoStatus("ErroneouslyPreplaced"), dom.getDomino().getStatus());
	}
	
	@Given("domino {int} is tentatively placed at position {int}:{int} with direction {string}")
	public void domino_is_tentatively_placed_at_position_with_direction(int id, int x, int y, String direction) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = game.getNextPlayer();
		KingdominoController.placeDomino(aPlayer, getdominoByID(id), x, y, getDirection(direction), false);
	}
	
	@Given("domino {int} has status {string}")
	public void domino_has_status(int id, String dstatus) {
	    getdominoByID(id).setStatus(getDominoStatus(dstatus));
	}
	
	@When("{string} requests to move the domino {string}")
	public void requests_to_move_the_domino(String player, String movement) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = null;
		for(int i = 0; i < game.getNumberOfPlayers(); i++) {
			if(game.getPlayer(i).getColor() == getPlayerColor(player)) aPlayer = game.getPlayer(i);
		}
		KingdominoController.moveCurrentDomino(KingdominoController.getDominoInKingdomByDomino(aPlayer.getDominoSelection().getDomino(), aPlayer.getKingdom()), movement, aPlayer);
	}
	
	@Then("the domino {int} should be tentatively placed at position {int}:{int} with direction {string}")
	public void the_domino_should_be_tentatively_placed_at_position_with_direction(int id, int x, int y, String direction) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = game.getNextPlayer();
		DominoInKingdom dom = KingdominoController.getDominoInKingdomByDomino(getdominoByID(id), aPlayer.getKingdom());
		assertEquals(x, dom.getX());
		assertEquals(y, dom.getY());
		assertEquals(getDirection(direction), dom.getDirection());	
	}
	
	@Then("the new status of the domino is {string}")
	public void the_new_status_of_the_domino_is(String status) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = game.getNextPlayer();
		assertEquals(getDominoStatus(status), aPlayer.getDominoSelection().getDomino().getStatus());
	}
	
	@Then("the domino {int} is still tentatively placed at position {int}:{int}")
	public void the_domino_is_still_tentatively_placed_at_position(int id, int x, int y) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = game.getNextPlayer();
		DominoInKingdom dom = KingdominoController.getDominoInKingdomByDomino(getdominoByID(id), aPlayer.getKingdom());
		assertEquals(x, dom.getX());
		assertEquals(y, dom.getY());
	}
	
	@Then("the domino should still have status {string}")
	public void the_domino_should_still_have_status(String status) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = game.getNextPlayer();
		assertEquals(getDominoStatus(status), aPlayer.getDominoSelection().getDomino().getStatus());
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
		case "InPile":
			return DominoStatus.InPile;
		case "Excluded":
			return DominoStatus.Excluded;
		case "InCurrentDraft":
			return DominoStatus.InCurrentDraft;
		case "InNextDraft":
			return DominoStatus.InNextDraft;
		case "ErroneouslyPreplaced":
			return DominoStatus.ErroneouslyPreplaced;
		case "CorrectlyPreplaced":
			return DominoStatus.CorrectlyPreplaced;
		case "PlacedInKingdom":
			return DominoStatus.PlacedInKingdom;
		case "Discarded":
			return DominoStatus.Discarded;
		default:
			throw new java.lang.IllegalArgumentException("Invalid domino status: " + status);
		}
	}
	
	private PlayerColor getPlayerColor(String color) {
		switch (color) {
		case "pink":
			return PlayerColor.Pink;
		case "blue":
			return PlayerColor.Blue;
		case "yellow":
			return PlayerColor.Yellow;
		case "green":
			return PlayerColor.Green;
		default:
			throw new java.lang.IllegalArgumentException("Invalid player color: " + color);
		}
	}*/
}
