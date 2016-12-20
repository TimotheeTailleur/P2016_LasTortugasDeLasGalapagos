package fr.tse.info4.project.controller;

import fr.tse.info4.project.controller.userParameter.AliceParameter;
import fr.tse.info4.project.controller.userParameter.BobParameter;
import fr.tse.info4.project.controller.userParameter.DaveParameter;

/**
 * 
 * Class handling the Personae objects creation.
 */
public class UserFactory {
	
	private String accessToken = null;

	/**
	 * Default constructor.
	 */
	public UserFactory() {

	}

	/**
	 * Constructor.
	 * @param accessToken
	 */
	public UserFactory(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * Creates a DaveParameter object.
	 * @return DaveParameter object.
	 */
	public DaveParameter newDave() {
		if (accessToken == null) {
			return new DaveParameter();
		} else {
			return new DaveParameter(accessToken);
		}
	}

	/**
	 * Creates an AliceParameter object.
	 * @return AliceParameter object.
	 */
	public AliceParameter newAlice() {
		if (accessToken == null) {
			return new AliceParameter();
		} else {
			return new AliceParameter(accessToken);
		}
	}
	
	/**
	 * Creates a BobParameter object.
	 * @return BobParameter object.
	 */
	public BobParameter newBob(){
		if (accessToken == null){
			return new  BobParameter();
		} else {
			return new BobParameter(accessToken);
		}
	}

}
