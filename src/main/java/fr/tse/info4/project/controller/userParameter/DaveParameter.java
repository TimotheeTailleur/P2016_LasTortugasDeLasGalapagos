package fr.tse.info4.project.controller.userParameter;

import fr.tse.info4.project.model.user.Dave;

public class DaveParameter {
	private Dave dave;
	
	public DaveParameter(){
		dave = new Dave();
	}
	
	public DaveParameter(String accessToken){
		dave = new Dave(accessToken);
	}
	
	public DaveParameter withNbUsers(int nbUsers){
		dave.setNbUsers(nbUsers);
		return this;
	}
	
	public DaveParameter withForceUpdate(){
		dave.setForceUpdateTopAnswerers(true);
		return this;
	}
	
	public DaveParameter withoutForceUpdate(){
		dave.setForceUpdateTopAnswerers(false);
		return this;
	}
	
	public DaveParameter withRefreshRate(int refreshRate){
		dave.setRefreshRateTopAnswerers(refreshRate);
		return this;
	}

	public Dave get(){
		return dave;
	}
	
	
}
