package org.terifan.nodeeditor.util;

import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.Styles;
import org.terifan.nodeeditor.widgets.ComboBoxProperty;
import org.terifan.nodeeditor.widgets.ValueProperty;


public class SimpleNodesFactory {
	private final static int SIZE = 150;

	public static Node createSourceTexture() {
		return new Node("Texture",
			new ValueProperty("Color"),
			new ValueProperty("Alpha"),
			new ValueProperty("Vector")
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createSourceColor() {
		return new Node("Color", new ValueProperty("Color"))
			.setSize(SIZE, 0)
			.setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createSourceColorRGB() {
		return new Node("RGB",
			new ValueProperty("Color")
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createSourceColorRGBA() {
		return new Node("RGBA",
			new ValueProperty("Color")
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createSourceAlpha() {
		return new Node("Alpha",
			new ValueProperty("Color")
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createIntermediateMath() {
		return new Node("Math",
			new ValueProperty("Value"),
			new ComboBoxProperty("Operation", 2, "Add", "Subtract", "Multiply", "Divide", "Modulo", "Greater Than").setId("function")
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.BLUE);
	}


	public static Node createIntermediateColorMix() {
		return new Node("ColorMix",
			new ValueProperty("Color"),
			new ValueProperty("Fac").setId("fac"),
			new ValueProperty("Color").setId("color1"),
			new ValueProperty("Color").setId("color2")
		).setSize(100, 0).setTitleBackground(Styles.DefaultNodeColors.BLUE);
	}


	public static Node createSourceValue() {
		return new Node("Value",
			new ValueProperty("Value")
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.BLUE);
	}
}
