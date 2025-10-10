package org.terifan.nodeeditor.util;

import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.Styles;
import org.terifan.nodeeditor.widgets.*;

import java.awt.*;

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
			new ButtonProperty("Open"),
			new ImageProperty("Image", SIZE, SIZE),
			new ValueProperty("Vector").addConnector(IN, PURPLE)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createSourceColor()
	{
		return (Node)new Node("Color",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".ColorAlphaProducer"),
			new ColorChooserProperty("Color", Color.BLACK).setId("color"),
			new SliderProperty("Alpha").setRange(0, 1, 1, 0.001).setId("alpha").addConnector(IN, GRAY)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createSourceColorRGB()
	{
		return (Node)new Node("RGB",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".RGBProducer"),
			new SliderProperty("Red").setRange(0, 1, 0.5, 0.001).setId("r").addConnector(IN, GRAY),
			new SliderProperty("Green").setRange(0, 1, 0.5, 0.001).setId("g").addConnector(IN, GRAY),
			new SliderProperty("Blue").setRange(0, 1, 0.5, 0.001).setId("b").addConnector(IN, GRAY)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createSourceColorRGBA()
	{
		return (Node)new Node("RGBA",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".RGBAProducer"),
			new SliderProperty("Red").setRange(0, 1, 0, 0.001).setId("r").addConnector(IN, GRAY),
			new SliderProperty("Green").setRange(0, 1, 0.5, 0.001).setId("g").addConnector(IN, GRAY),
			new SliderProperty("Blue").setRange(0, 1, 0.5, 0.001).setId("b").addConnector(IN, GRAY),
			new SliderProperty("Alpha").setRange(0, 1, 1.0, 0.001).setId("a").addConnector(IN, GRAY)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createSourceAlpha()
	{
		return (Node)new Node("Alpha",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".AlphaProducer"),
			new SliderProperty("Alpha").setRange(0, 1, 1, 0.001).setId("a").addConnector(OUT, GRAY).addConnector(IN, GRAY)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.RED);
	}


	public static Node createIntermediateMath()
	{
		return (Node)new Node("Math",
			new ValueProperty("Value").addConnector(OUT, GRAY).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".MathProducer"),
			new ComboBoxProperty("Operation", 2, "Add", "Subtract", "Multiply", "Divide", "Modulo", "Greater Than").setId("function"),
			new CheckBoxProperty("Clamp", false).setId("clamp"),
			new SliderProperty("Value", 1000, 0.01).setId("value1").addConnector(IN, GRAY),
			new SliderProperty("Value", 0.5, 0.01).setId("value2").addConnector(IN, GRAY)
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.BLUE);
	}


	public static Node createIntermediateColorMix()
	{
		return (Node)new Node("ColorMix",
			new ValueProperty("Color").addConnector(OUT, YELLOW).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".ColorMixProducer"),
			new SliderProperty("Fac").setRange(0, 1, 0.5, 0.1).setId("fac").addConnector(IN, GRAY),
			new ColorChooserProperty("Color", new Color(0, 0, 0)).setId("color1").addConnector(IN, YELLOW),
			new ColorChooserProperty("Color", new Color(255, 255, 255)).setId("color2").addConnector(IN, YELLOW)
		).setSize(100, 0).setTitleBackground(Styles.DefaultNodeColors.BLUE);
	}


	public static Node createSourceValue()
	{
		return (Node)new Node("Value",
			new ValueProperty("Value").addConnector(OUT, GRAY).setProducer(SimpleNodesFactory.class.getCanonicalName() + ".ValueProducer"),
			new SliderProperty("").setId("value")
		).setSize(SIZE, 0).setTitleBackground(Styles.DefaultNodeColors.BLUE);
	}
}
