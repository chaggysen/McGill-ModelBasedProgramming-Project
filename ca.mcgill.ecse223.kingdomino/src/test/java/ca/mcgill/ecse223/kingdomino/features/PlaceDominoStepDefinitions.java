package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import ca.mcgill.ecse223.kingdomino.model.*;

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
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.DominoSelection;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Gameplay;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.model.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * 
 * @author Sen Wang
 * @author Noah Chamberland
 *
 */
public class PlaceDominoStepDefinitions {
	
	

	/*@Given("the game is initialized for move current domino")
	public void the_game_is_initialized_for_move_current_domino() {
		
	}*/
	
	/*@Given("it is {string}'s turn")
	public void it_is_turn(String player) {
		
	}*/
	
	@Given("the {string}'s kingdom has the following dominoes:")
	public void the_kingdom_has_the_following_dominoes(String player, io.cucumber.datatable.DataTable dataTable) {

		List<Map<String, String>> valueMaps = dataTable.asMaps();		
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Kingdom kingdom = null;
		for(int i = 0; i < game.getNumberOfPlayers(); i++) {
			if(game.getPlayer(i).getColor() == getPlayerColor(player)) kingdom = game.getPlayer(i).getKingdom();
		}
		for (Map<String, String> map : valueMaps) {
			// Get values from cucumber table
			Integer id = Integer.decode(map.get("domino"));
			DirectionKind dir = getDirection(map.get("dominodir"));
			Integer posx = Integer.decode(map.get("posx"));
			Integer posy = Integer.decode(map.get("posy"));

			// Add the domino to a player's kingdom
			Domino dominoToPlace = getdominoByID(id);
			DominoInKingdom domInKingdom = new DominoInKingdom(posx, posy, kingdom, dominoToPlace);
			domInKingdom.setDirection(dir);
			dominoToPlace.setStatus(DominoStatus.PlacedInKingdom);
		}
	}
	
	/*@Given("{string} has selected domino {int}")
	public void has_selected_domino(String player, int id) {
		
	}*/
	
	@Given("domino {int} is tentatively placed at position {int}:{int} with direction {string}")
	public void domino_is_tentatively_placed_at_position_with_direction(int id, int posx, int posy, String dir) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = game.getNextPlayer();
		KingdominoController.placeDomino(aPlayer, getdominoByID(id), posx, posy, getDirection(dir), false);
	}
	
	@Given("domino {int} is in {string} status")
	public void domino_is_in_CorrectlyPreplaced_status(int id, String status) {
		DominoStatus aStatus = getDominoStatus(status);
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for (int i = 0; i < game.getAllDominos().size(); i++) {
			if (game.getAllDominos().get(i).getId() == id) {
				game.getAllDominos().get(i).setStatus(aStatus);
			}
		}
	}
	
	@When("{string} requests to place the selected domino {int}")
	public void requests_to_place_the_selected_domino(String player, int id) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = null;
		for (int i = 0; i < game.getPlayers().size(); i++){
			if (game.getPlayer(i).getColor() == getPlayerColor(player)) {
				aPlayer = game.getPlayers().get(i);
			}
		}
		int xPosition = 0;
		int yPosition = 0;
		DirectionKind direction = null;
		
		for (int i = 0; i < aPlayer.getKingdom().getTerritories().size(); i++) {
			if (aPlayer.getKingdom().getTerritories().get(i) instanceof DominoInKingdom) {
				DominoInKingdom dominoInKingdom = (DominoInKingdom)aPlayer.getKingdom().getTerritories().get(i);
				if (dominoInKingdom.getDomino().getId() == id) {
					xPosition = dominoInKingdom.getX();
					yPosition = dominoInKingdom.getY();
					direction = dominoInKingdom.getDirection();
					dominoInKingdom.getDomino().setStatus(DominoStatus.CorrectlyPreplaced);
				}
			}
			
		}
		
		KingdominoController.placeDomino(aPlayer, getdominoByID(id), xPosition, yPosition, direction, true);
	}
	
	@Then("{string}'s kingdom should now have domino {int} at position {int}:{int} with direction {string}")
	public void s_kingdom_should_now_have_domino_at_position_with_direction(String player, int id, int posx, int posy, String dir) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player aPlayer = null;
		for (int i = 0; i < game.getPlayers().size(); i++){
			if (game.getPlayer(i).getColor() == getPlayerColor(player)) {
				aPlayer = game.getPlayers().get(i);
			}
		}
		Boolean hasDomino = false;
		for (int i = 0; i < aPlayer.getKingdom().getTerritories().size(); i++) {
			if (aPlayer.getKingdom().getTerritories().get(i) instanceof DominoInKingdom) {
				DominoInKingdom dominoInKingdom = (DominoInKingdom)aPlayer.getKingdom().getTerritories().get(i);
				if (dominoInKingdom.getDomino().getId() == id && dominoInKingdom.getX() == posx && dominoInKingdom.getY() == posy && dominoInKingdom.getDirection() == getDirection(dir)) {
					hasDomino = true;
				}
			}
		}
		assertEquals(true, hasDomino);
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
}
