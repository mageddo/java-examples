package cucumber.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class HelloWorldStepDefs {

	@Given("this action will be called")
	public void when(){
		System.out.println("when");
	}

	@Then("cucumber must be working perfectly")
	public void then(){
		System.out.println("then");
	}

}
