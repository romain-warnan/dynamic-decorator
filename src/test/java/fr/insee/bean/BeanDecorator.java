package fr.insee.bean;

import fr.insee.decorator.Decorator;

public class BeanDecorator extends Decorator<Bean> implements Runnable {
	
	public BeanDecorator(Bean instance) {
		super(instance);
	}

	public String getNom(){
		return underlying.getNom() + " [décoré]";
	}
	
	public String getGabarit(){
		return "Dossier";
	}

	@Override
	public void run() {
		// Do nothing
	}
}
