package org.terifan.nodeeditor.util;

import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.Styles;
import org.terifan.nodeeditor.widgets.ComboBoxProperty;
import org.terifan.nodeeditor.widgets.ValueProperty;

import static org.terifan.nodeeditor.Direction.IN;
import static org.terifan.nodeeditor.Direction.OUT;
import static org.terifan.nodeeditor.Styles.DefaultConnectorColors.*;


public class SimpleNodesFactory
{
	private final static int SIZE = 150;

	public static Node createSourceTexture()
	{
		return (Node)new Node("Texture",
			new ValueProperty("Color").addConnector(OUT, YELLOW),
			new ValueProperty("Alpha").addConnector(OUT, GRAY),
			new ValueProperty("Vector").addConnector(IN, PURPLE)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createSourceColor()
	{
		return (Node)new Node("Color",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".ColorAlphaProducer")
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createSourceColorRGB()
	{
		return (Node)new Node("RGB",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".RGBProducer")
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createSourceColorRGBA()
	{
		return (Node)new Node("RGBA",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".RGBAProducer")
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createSourceAlpha()
	{
		return (Node)new Node("Alpha",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".AlphaProducer")
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createIntermediateMath()
	{
		return (Node)new Node("Math",
			new ValueProperty("Value").addConnector(OUT, GRAY).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".MathProducer"),
			new ComboBoxProperty("Operation", 2, "Add", "Subtract", "Multiply", "Divide", "Modulo", "Greater Than").setId("function")
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.BLUE);
	}


	public static Node createIntermediateColorMix()
	{
		return (Node)new Node("ColorMix",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".ColorMixProducer"),
			new ValueProperty("Fac").setId("fac").addConnector(IN, GRAY),
			new ValueProperty("Color").setId("color1").addConnector(IN, YELLOW),
			new ValueProperty("Color").setId("color2").addConnector(IN, YELLOW)
		).setSize(100, 0).setTitleBackground(Styles.DefaultNodeColors.BLUE);
	}


	public static Node createSourceValue()
	{
		return (Node)new Node("Value",
			new ValueProperty("Value").addConnector(OUT, GRAY).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".ValueProducer")
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.BLUE);
	}
}
