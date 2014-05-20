package fr.insee.bean;



public class ComparableDecorator implements Comparable<ComparableDecorator> {

	private int order;
	
	public ComparableDecorator(int order) {
		super();
		this.order = order;
	}

	@Override
	public int compareTo(ComparableDecorator other) {
		return this.order - other.order;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
