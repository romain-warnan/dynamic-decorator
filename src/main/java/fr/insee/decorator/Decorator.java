package fr.insee.decorator;

public abstract class Decorator<T> {

	protected T underlying;

	public Decorator(T underlying) {
		super();
		this.underlying = underlying;
	}
}