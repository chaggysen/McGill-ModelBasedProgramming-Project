package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.en.And;

public class SelectingFirstDominoStepDefinitions {

	// We use the annotation @And to signal precondition check instead of
	// initialization (which is done in @Given methods)
	@And("the validation of domino selection returns {string}")
	public void the_validation_of_domino_selection_returns(String expectedValidationResultString) {
		boolean expectedValidationResult = true;
		if ("success".equalsIgnoreCase(expectedValidationResultString.trim())) {
			expectedValidationResult = true;
		} else if ("error".equalsIgnoreCase(expectedValidationResultString.trim())) {
			expectedValidationResult = false;
		} else {
			throw new IllegalArgumentException(
					"Unknown validation result string \"" + expectedValidationResultString + "\"");
		}
		boolean actualValidationResult = false;

		// TODO call here the guard function from the statemachine and store the result
		// actualValidationResult = gameplay.isSelectionValid();

		// Check the precondition prescribed by the scenario
		assertEquals(expectedValidationResult, actualValidationResult);
	}

}
