package de.unikassel.chefcoders.codecampkitchen.model;

public enum ItemKind
{
	WASSER("Wasser"),
	SAFT("Saft"),
	EIS("Eis"),
	KAFFEE("Kaffee"),
	MILCHKAFFEE("Milchkaffee"),
	ESPRESSO("Espresso");

	private String name;

	private ItemKind(String name){
		this.name = name;
	}

	@Override
	public String toString(){
		return name;
	}
}
