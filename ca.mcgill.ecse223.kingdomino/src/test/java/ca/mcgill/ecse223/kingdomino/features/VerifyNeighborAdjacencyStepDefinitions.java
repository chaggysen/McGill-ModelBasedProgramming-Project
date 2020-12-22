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
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * 
 * @author Sen Wang
 *
 */
public class VerifyNeighborAdjacencyStepDefinitions {
	
	@Given("the game is initialized for neighbor adjacency")
	public void the_game_is_initialized_for_neighbor_adjacency() {

		// Intialize empty game
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		kingdomino.setCurrentGame(game);
		// Populate game
		addDefaultUsersAndPlayers(game);
		createAllDominoes(game);
		game.setNextPlayer(game.getPlayer(0));
		KingdominoApplication.setKingdomino(kingdomino);

	}

	@When("check current preplaced domino adjacency is initiated")
	public void check_current_preplaced_domino_adjacency_is_initiated() {

		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Kingdom kingdom = game.getPlayer(0).getKingdom();

		// Domino 1
		int id_1 = 15;
		int posx_1 = 1;
		int posy_1 = 2;
		DirectionKind dir_1 = DirectionKind.Right;
		Domino dominoToPlace_1 = getdominoByID(id_1);
		DominoInKingdom domInKingdom_1 = new DominoInKingdom(posx_1, posy_1, kingdom, dominoToPlace_1);
		domInKingdom_1.setDirection(dir_1);
		for (int i = 1; i < game.getPlayers().get(0).getKingdom().getTerritories().size(); i++) {
			DominoInKingdom dominoToBeCompared = (DominoInKingdom) game.getPlayer(0).getKingdom().getTerritories()
					.get(i);
			if (KingdominoController.neighborAdjacency(domInKingdom_1, dominoToBeCompared)) {
				dominoToPlace_1.setStatus(DominoStatus.CorrectlyPreplaced);
			}
		}

		// Domino 2
		int id_2 = 15;
		int posx_2 = 1;
		int posy_2 = 3;
		DirectionKind dir_2 = DirectionKind.Down;
		Domino dominoToPlace_2 = getdominoByID(id_2);
		DominoInKingdom domInKingdom_2 = new DominoInKingdom(posx_2, posy_2, kingdom, dominoToPlace_2);
		domInKingdom_2.setDirection(dir_2);
		for (int i = 1; i < game.getPlayers().get(0).getKingdom().getTerritories().size(); i++) {
			DominoInKingdom dominoToBeCompared = (DominoInKingdom) game.getPlayer(0).getKingdom().getTerritories()
					.get(i);
			if (KingdominoController.neighborAdjacency(domInKingdom_2, dominoToBeCompared)) {
				dominoToPlace_2.setStatus(DominoStatus.CorrectlyPreplaced);
			}
		}

		// Domino 3
		int id_3 = 20;
		int posx_3 = 2;
		int posy_3 = 2;
		DirectionKind dir_3 = DirectionKind.Left;
		Domino dominoToPlace_3 = getdominoByID(id_3);
		DominoInKingdom domInKingdom_3 = new DominoInKingdom(posx_3, posy_3, kingdom, dominoToPlace_3);
		domInKingdom_3.setDirection(dir_3);
		for (int i = 1; i < game.getPlayers().get(0).getKingdom().getTerritories().size(); i++) {
			DominoInKingdom dominoToBeCompared = (DominoInKingdom) game.getPlayer(0).getKingdom().getTerritories()
					.get(i);
			if (KingdominoController.neighborAdjacency(domInKingdom_3, dominoToBeCompared)) {
				dominoToPlace_3.setStatus(DominoStatus.CorrectlyPreplaced);
			}
		}

		// Domino 4
		int id_4 = 12;
		int posx_4 = -1;
		int posy_4 = 0;
		DirectionKind dir_4 = DirectionKind.Up;
		Domino dominoToPlace_4 = getdominoByID(id_4);
		DominoInKingdom domInKingdom_4 = new DominoInKingdom(posx_4, posy_4, kingdom, dominoToPlace_4);
		domInKingdom_4.setDirection(dir_4);
		for (int i = 1; i < game.getPlayers().get(0).getKingdom().getTerritories().size(); i++) {
			DominoInKingdom dominoToBeCompared = (DominoInKingdom) game.getPlayer(0).getKingdom().getTerritories()
					.get(i);
			if (KingdominoController.neighborAdjacency(domInKingdom_4, dominoToBeCompared)) {
				dominoToPlace_4.setStatus(DominoStatus.CorrectlyPreplaced);
			}
		}

		// Domino 5
		int id_5 = 12;
		int posx_5 = -1;
		int posy_5 = 1;
		DirectionKind dir_5 = DirectionKind.Up;
		Domino dominoToPlace_5 = getdominoByID(id_5);
		DominoInKingdom domInKingdom_5 = new DominoInKingdom(posx_5, posy_5, kingdom, dominoToPlace_5);
		domInKingdom_5.setDirection(dir_5);
		for (int i = 1; i < game.getPlayers().get(0).getKingdom().getTerritories().size(); i++) {
			DominoInKingdom dominoToBeCompared = (DominoInKingdom) game.getPlayer(0).getKingdom().getTerritories()
					.get(i);
			if (KingdominoController.neighborAdjacency(domInKingdom_5, dominoToBeCompared)) {
				dominoToPlace_5.setStatus(DominoStatus.CorrectlyPreplaced);
			}
		}

		// Domino 6
		int id_6 = 44;
		int posx_6 = 2;
		int posy_6 = 0;
		DirectionKind dir_6 = DirectionKind.Down;
		Domino dominoToPlace_6 = getdominoByID(id_6);
		DominoInKingdom domInKingdom_6 = new DominoInKingdom(posx_6, posy_6, kingdom, dominoToPlace_6);
		domInKingdom_6.setDirection(dir_6);
		for (int i = 1; i < game.getPlayers().get(0).getKingdom().getTerritories().size(); i++) {
			DominoInKingdom dominoToBeCompared = (DominoInKingdom) game.getPlayer(0).getKingdom().getTerritories()
					.get(i);
			if (KingdominoController.neighborAdjacency(domInKingdom_6, dominoToBeCompared)) {
				dominoToPlace_6.setStatus(DominoStatus.CorrectlyPreplaced);
			}
		}

		// Domino 8
		int id_8 = 29;
		int posx_8 = 1;
		int posy_8 = 2;
		DirectionKind dir_8 = DirectionKind.Up;
		Domino dominoToPlace_8 = getdominoByID(id_8);
		DominoInKingdom domInKingdom_8 = new DominoInKingdom(posx_8, posy_8, kingdom, dominoToPlace_8);
		domInKingdom_8.setDirection(dir_8);
		for (int i = 1; i < game.getPlayers().get(0).getKingdom().getTerritories().size(); i++) {
			DominoInKingdom dominoToBeCompared = (DominoInKingdom) game.getPlayer(0).getKingdom().getTerritories()
					.get(i);
			if (KingdominoController.neighborAdjacency(domInKingdom_8, dominoToBeCompared)) {
				dominoToPlace_8.setStatus(DominoStatus.CorrectlyPreplaced);
			}
		}

		// Domino 9
		int id_9 = 48;
		int posx_9 = -1;
		int posy_9 = 3;
		DirectionKind dir_9 = DirectionKind.Down;
		Domino dominoToPlace_9 = getdominoByID(id_9);
		DominoInKingdom domInKingdom_9 = new DominoInKingdom(posx_9, posy_9, kingdom, dominoToPlace_9);
		domInKingdom_9.setDirection(dir_9);
		for (int i = 1; i < game.getPlayers().get(0).getKingdom().getTerritories().size(); i++) {
			DominoInKingdom dominoToBeCompared = (DominoInKingdom) game.getPlayer(0).getKingdom().getTerritories()
					.get(i);
			if (KingdominoController.neighborAdjacency(domInKingdom_9, dominoToBeCompared)) {
				dominoToPlace_9.setStatus(DominoStatus.CorrectlyPreplaced);
			}
		}

		// Domino 10
		int id_10 = 43;
		int posx_10 = 1;
		int posy_10 = -1;
		DirectionKind dir_10 = DirectionKind.Right;
		Domino dominoToPlace_10 = getdominoByID(id_10);
		DominoInKingdom domInKingdom_10 = new DominoInKingdom(posx_10, posy_10, kingdom, dominoToPlace_10);
		domInKingdom_10.setDirection(dir_10);
		for (int i = 1; i < game.getPlayers().get(0).getKingdom().getTerritories().size(); i++) {
			DominoInKingdom dominoToBeCompared = (DominoInKingdom) game.getPlayer(0).getKingdom().getTerritories()
					.get(i);
			if (KingdominoController.neighborAdjacency(domInKingdom_10, dominoToBeCompared)) {
				dominoToPlace_10.setStatus(DominoStatus.CorrectlyPreplaced);
			}
		}

	}

	@Then("the current-domino\\/existing-domino adjacency is {string}")
	public void the_current_domino_existing_domino_adjacency_is(String string) {

		int correctlyPreplacedCount = 0;
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();

		for (int i = 0; i < game.getAllDominos().size(); i++) {
			if (game.getAllDomino(i).getStatus() == DominoStatus.CorrectlyPreplaced) {
				correctlyPreplacedCount += 1;
			}
		}
		assertEquals(4, correctlyPreplacedCount);
	}

///////////////////////////////////////
/// -----Private Helper Methods---- ///
///////////////////////////////////////

	private void addDefaultUsersAndPlayers(Game game) {
		String[] users = { "User1", "User2", "User3", "User4" };
		for (int i = 0; i < users.length; i++) {
			game.getKingdomino().addUser(users[i]);
			Player player = new Player(game);
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
		case "inPile":
			return DominoStatus.InPile;
		case "excluded":
			return DominoStatus.Excluded;
		case "inCurrentDraft":
			return DominoStatus.InCurrentDraft;
		case "inNextDraft":
			return DominoStatus.InNextDraft;
		case "erroneouslyPreplaced":
			return DominoStatus.ErroneouslyPreplaced;
		case "correctlyPreplaced":
			return DominoStatus.CorrectlyPreplaced;
		case "placedInKingdom":
			return DominoStatus.PlacedInKingdom;
		case "discarded":
			return DominoStatus.Discarded;
		default:
			throw new java.lang.IllegalArgumentException("Invalid domino status: " + status);
		}
	}
}
