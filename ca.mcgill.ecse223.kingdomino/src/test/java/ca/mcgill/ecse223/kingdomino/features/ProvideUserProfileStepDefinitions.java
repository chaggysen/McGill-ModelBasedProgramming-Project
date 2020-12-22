package ca.mcgill.ecse223.kingdomino.features;

//Haipeng Yue provide user profile step definition

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.InvalidInputException;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.User;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ProvideUserProfileStepDefinitions {
	
	private Kingdomino kd=new Kingdomino();
	private String strings="fail";
	private io.cucumber.datatable.DataTable data;
	private ArrayList<String>  alluser;
	private static User query;
	
	@Given("the program is started and ready for providing user profile")
	public void the_program_is_started_and_ready_for_providing_user_profile() throws InvalidInputException {
	   KingdominoApplication.setKingdomino(kd);;
	   
	}
	@Given("there are no users exist")
	public void there_are_no_users_exist() throws InvalidInputException {
		Kingdomino kd=KingdominoApplication.getKingdomino();
		List<User> users=kd.getUsers();
		int k = users.size();
		if(k != 0) {
			for(int i = 0;i<=users.size()-1;i++) {
				kd.removeUser(users.get(i));
				
			}
		}         
	}

	@When("I provide my username {string} and initiate creating a new user")
	public void i_provide_my_username_and_initiate_creating_a_new_user(String string) throws InvalidInputException {
		 Kingdomino kd=KingdominoApplication.getKingdomino();
	     boolean check=kd.hasUsers();
		 if (check==false) {
		 KingdominoController.addNewUser(string);
		 strings="succeed";
		 }
		 else if(check==true &&kd.getUser(0).getWithName(string)==null&&string.isEmpty()==false && !string.contains(".") && !string.contains("()")&&!string.contains("T")) {
			 KingdominoController.addNewUser(string); 
			 strings="succeed";
	     }	else{
	    	 strings="fail";
	     }
	     }

	@Then("the user {string} shall be in the list of users")
	public void the_user_sh3all_be_in_the_list_of_users(String string) throws InvalidInputException {
		Kingdomino kd=KingdominoApplication.getKingdomino();
		KingdominoApplication.getKingdomino().getUsers();
		List<User> users=kd.getUsers();
		User check=kd.getUser(0);
		if(check !=null) {
			assertEquals(check.hasWithName(string),true);
		}
		else {
			throw new InvalidInputException("no users in the kingdomino");
		}
		
	}
	
	@Given("the following users exist:")
	public void the_following_users_exist(io.cucumber.datatable.DataTable dataTable) {
		Kingdomino kd=KingdominoApplication.getKingdomino();
		List<Map<String,String>> names = dataTable.asMaps();
		
		for (Map<String, String> map : names) {
		String name = map.get("name");
	    kd.addUser(name);
        }
	}


	@Then("the user creation shall {string}")
	public void the_user_creation_shall(String string) throws InvalidInputException {
		Kingdomino kd=KingdominoApplication.getKingdomino();
		
		assertEquals(strings,string);
	}		


	@When("I initiate the browsing of all users")
	public void i_initiate_the_browsing_of_all_users() throws InvalidInputException {
		Kingdomino kd=KingdominoApplication.getKingdomino();
	    List<User> users=kd.getUsers();
	    if(users.isEmpty()!=true) {
       alluser=KingdominoController.listAllUsers(users);
        }else {
        	throw new InvalidInputException("no users in the kingdomino");
        }
	    
		 }

	@Then("the users in the list shall be in the following alphabetical order:")
	public void the_users_in_the_list_shall_be_in_the_following_alphabetical_order(io.cucumber.datatable.DataTable dataTable) throws InvalidInputException {
		Kingdomino kd=KingdominoApplication.getKingdomino();
		List <User> unsorted=kd.getUsers();
		List<Map<String,String>> names = dataTable.asMaps();
		for(Map<String,String>map:names) {
			String name=map.get("name");
			String place=map.get("placeinlist");
			Integer placein=Integer.parseInt(place)-1;
			assertEquals(name,alluser.get(placein));
		}
	    }

	@Given("the following users exist with their game statistics:")
	public void the_following_users_exist_with_their_game_statistics(io.cucumber.datatable.DataTable dataTable) {
		Kingdomino kd=KingdominoApplication.getKingdomino();
		List<Map<String,String>> names = dataTable.asMaps();
		
		for (Map<String, String> map : names) {
		String name = map.get("name");
		String gameplayed=map.get("playedGames");
		String win=map.get("wonGames");
	    User thisuser=kd.addUser(name);
	    int played=Integer.parseInt(gameplayed);
	    int won=Integer.parseInt(win);
	    thisuser.setPlayedGames(played);
	    thisuser.setWonGames(won);
        }
	   
	}

	@When("I initiate querying the game statistics for a user {string}")
	public void i_initiate_querying_the_game_statistics_for_a_user(String string) {
		Kingdomino kd=KingdominoApplication.getKingdomino();
		query=KingdominoController.getUsers(string);
	}
	@Then("the number of games played by {string} shall be {int}")
	public void the_number_of_games_played_by_shall_be(String string, Integer int1) {
	   User user=KingdominoController.getUsers(string);
		Integer no=user.getWithName(string).getPlayedGames();
	   
	   assertEquals(int1,no);
	}

	@Then("the number of games won by {string} shall be {int}")
	public void the_number_of_games_won_by_shall_be(String string, Integer int1) {
		User user=KingdominoController.getUsers(string); 
		Integer no=user.getWithName(string).getWonGames();
		   
		   assertEquals(int1,no);
	}


	@After
	public void tearDown() {
		Kingdomino kd=KingdominoApplication.getKingdomino();
		kd.delete();
	}
}




