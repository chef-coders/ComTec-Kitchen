package de.unikassel.chefcoders.codecampkitchen.model;

import org.fulib.Fulib;
import org.fulib.FulibTools;
import org.fulib.builder.ClassBuilder;
import org.fulib.builder.ClassModelBuilder;
import org.fulib.classmodel.ClassModel;

import static org.fulib.builder.ClassModelBuilder.MANY;
import static org.fulib.builder.ClassModelBuilder.ONE;

public class GenDataModel
{
	public static void main(String[] args)
	{
		final ClassModelBuilder mb = Fulib.classModelBuilder(GenDataModel.class.getPackage().getName());

		// classes

		final ClassBuilder user = mb.buildClass("User");
		user.buildAttribute("_id", "String");
		user.buildAttribute("created", "String");
		user.buildAttribute("name", "String");
		user.buildAttribute("mail", "String");
		user.buildAttribute("role", "String");
		user.buildAttribute("credit", "long");

		final ClassBuilder item = mb.buildClass("Item");
		item.buildAttribute("_id", "String");
		item.buildAttribute("name", "String");
		item.buildAttribute("price", "double");
		item.buildAttribute("amount", "int");
		item.buildAttribute("kind", "String");

		final ClassBuilder purchase = mb.buildClass("Purchase");
		purchase.buildAttribute("_id", "String");
		purchase.buildAttribute("created", "String");
		purchase.buildAttribute("user_id", "String");
		purchase.buildAttribute("item_id", "String");
		purchase.buildAttribute("amount", "int");

		// associations

		purchase.buildAssociation(user, "user", ONE, "purchases", MANY);
		purchase.buildAttribute("item", "Item"); // should be uni-directional

		// generate model

		final ClassModel model = mb.getClassModel();

		Fulib.generator().generate(model);

		FulibTools.classDiagrams().dumpPng(model, "../doc/classModel.png");
	}
}
