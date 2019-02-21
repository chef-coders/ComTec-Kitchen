package de.unikassel.chefcoders.codecampkitchen.model;

import org.fulib.Fulib;
import org.fulib.builder.ClassBuilder;
import org.fulib.builder.ClassModelBuilder;
import org.fulib.classmodel.ClassModel;

public class GenDataModel
{
	public static void main(String[] args)
	{
		final ClassModelBuilder mb = Fulib.classModelBuilder(GenDataModel.class.getPackage().getName());

		// classes

		final ClassBuilder user = mb.buildClass("User");
		user.buildAttribute("_id", "String");
		user.buildAttribute("created", "String");
		user.buildAttribute("token", "String");
		user.buildAttribute("name", "String");
		user.buildAttribute("mail", "String");
		user.buildAttribute("role", "String");
		user.buildAttribute("credit", "double");

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

		// generate model

		final ClassModel model = mb.getClassModel();

		Fulib.generator().generate(model);
	}
}
