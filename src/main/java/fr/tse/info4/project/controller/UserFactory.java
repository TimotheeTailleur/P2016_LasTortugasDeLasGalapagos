package fr.tse.info4.project.controller;

import fr.tse.info4.project.controller.userParameter.AliceParameter;
import fr.tse.info4.project.controller.userParameter.BobParameter;
import fr.tse.info4.project.controller.userParameter.DaveParameter;

public class UserFactory {
	private String accessToken = null;

	public UserFactory() {

	}

	public UserFactory(String accessToken) {
		this.accessToken = accessToken;
	}

	public DaveParameter newDave() {
		if (accessToken == null) {
			return new DaveParameter();
		} else {
			return new DaveParameter(accessToken);
		}
	}

	public AliceParameter newAlice() {
		if (accessToken == null) {
			return new AliceParameter();
		} else {
			return new AliceParameter(accessToken);
		}
	}
	
	public BobParameter newBob(){
		if (accessToken == null){
			return new  BobParameter();
		} else {
			return new BobParameter(accessToken);
		}
	}

}
