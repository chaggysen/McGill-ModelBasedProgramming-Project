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
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoSelection;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.KingdomTerritory;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.model.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 *@author Noah Chamberland
 */
public class RotateCurrentDominoStepDefinitions {

	
	@Given("the game is initialized for rotate current domino")
	public void the_game_is_initialized_for_move_current_domino() {
		// Intialize empty game
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game);
		// Populate game
		addDefaultUsersAndPlayers(game);
		createAllDominoes(game);
		Draft draft = new Draft(DraftStatus.FaceDown, game);
		game.setNextPlayer(game.getPlayer(0));
		KingdominoApplication.setKingdomino(kingdomino);
		game.setCurrentDraft(draft);
	}

	@When("{string} requests to rotate the domino with {string}")
	public void requests_to_rotate_the_domino_with(String player, String rotation) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = null;
		for(int i = 0; i < game.getNumberOfPlayers(); i++) {
			if(game.getPlayer(i).getColor() == getPlayerColor(player)) aPlayer = game.getPlayer(i);
		}
		KingdominoController.rotateCurrentDomino(aPlayer, aPlayer.getDominoSelection().getDomino(), rotation);
	}

	@Then("domino {int} is tentatively placed at the same position {int}:{int} with the same direction {string}")
	public void domino_is_tentatively_placed_at_the_same_position_with_the_same_direction(int id, int x, int y,
			String dir) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = game.getNextPlayer();
		DominoInKingdom dom = KingdominoController.getDominoInKingdomByDomino(getdominoByID(id), aPlayer.getKingdom());
		assertEquals(x, dom.getX());
		assertEquals(y, dom.getY());
		assertEquals(getDirection(dir), dom.getDirection());
	}
	
	@Then("the domino {int} is still tentatively placed at {int}:{int} but with new direction {string}")
	public void the_domino_is_still_tentatively_placed_at_but_with_new_direction(int id, int x,
			int y, String dir) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = game.getNextPlayer();
		DominoInKingdom dom = KingdominoController.getDominoInKingdomByDomino(getdominoByID(id), aPlayer.getKingdom());
		assertEquals(x, dom.getX());
		assertEquals(y, dom.getY());
		assertEquals(getDirection(dir), dom.getDirection());
	}

	@Then("the domino {int} should have status {string}")
	public void the_domino_should_have_status(int id, String status) {
		assertEquals(getDominoStatus(status), getdominoByID(id).getStatus());
	}

	@Then("domino {int} should still have status {string}")
	public void domino_should_still_have_status(int id, String status) {
		assertEquals(getdominoByID(id).getStatus(), getDominoStatus(status));
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
	
	/*Game game;
	Player aPlayer;
	Draft draft;
	DominoInKingdom domino;
	Kingdom kingdom;

	@Given("it is {string}'s turn")
	public void it_is_s_turn(String player) {
		for (int i = 0; i < game.getNumberOfPlayers(); i++) {
			if (game.getPlayer(i).getColor() == getPlayerColor(player))
				aPlayer = game.getPlayer(i);
		}
	}


	@Given("the game is initialized for rotate current domino")
	public void the_game_is_initialized_for_move_current_domino() {
		// Intialize empty game
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game);
		// Populate game
		addDefaultUsersAndPlayers(game);
		createAllDominoes(game);
		Draft draft = new Draft(DraftStatus.FaceDown, game);
		game.setNextPlayer(game.getPlayer(0));
		KingdominoApplication.setKingdomino(kingdomino);
		game.setCurrentDraft(draft);
	}

	@When("{string} requests to rotate the domino with {string}")
	public void requests_to_rotate_the_domino_with(String player, String rotation) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = null;
		for(int i = 0; i < game.getNumberOfPlayers(); i++) {
			if(game.getPlayer(i).getColor() == getPlayerColor(player)) aPlayer = game.getPlayer(i);
		}
		KingdominoController.rotateCurrentDomino(aPlayer, KingdominoController.getDominoInKingdomByDomino(aPlayer.getDominoSelection().getDomino(), aPlayer.getKingdom()).getDirection(), aPlayer.getDominoSelection().getDomino(), rotation);
	}

	@Then("domino {int} is tentatively placed at the same position {int}:{int} with the same direction {string}")
	public void domino_is_tentatively_placed_at_the_same_position_with_the_same_direction(int id, int x, int y,
			String dir) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = game.getNextPlayer();
		DominoInKingdom dom = KingdominoController.getDominoInKingdomByDomino(getdominoByID(id), aPlayer.getKingdom());
		assertEquals(x, dom.getX());
		assertEquals(y, dom.getY());
		assertEquals(getDirection(dir), dom.getDirection());
	}
	
	@Then("the domino {int} is still tentatively placed at {int}:{int} but with new direction {string}")
	public void the_domino_is_still_tentatively_placed_at_but_with_new_direction(int id, int x,
			int y, String dir) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = game.getNextPlayer();
		DominoInKingdom dom = KingdominoController.getDominoInKingdomByDomino(getdominoByID(id), aPlayer.getKingdom());
		assertEquals(x, dom.getX());
		assertEquals(y, dom.getY());
		assertEquals(getDirection(dir), dom.getDirection());
	}

	@Then("the domino {int} should have status {string}")
	public void the_domino_should_have_status(int id, String status) {
		assertEquals(getDominoStatus(status), getdominoByID(id).getStatus());
	}

	@Then("domino {int} should still have status {string}")
	public void domino_should_still_have_status(int id, String status) {
		assertEquals(getdominoByID(id).getStatus(), getDominoStatus(status));
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