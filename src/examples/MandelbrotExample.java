package examples;

import org.terifan.boxcomponentpane.DiagramView;
import org.terifan.boxcomponentpane.NodeModel;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.Styles.DefaultNodeColors;
import org.terifan.nodeeditor.util.SimpleNodesFactory;
import org.terifan.nodeeditor.widgets.ValueProperty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class MandelbrotExample {


	static void main() {
		try {
			NodeModel __model = new NodeModel()
				.addComponent(
					new Node("Mandelbrot")
						.setTitleBackground(DefaultNodeColors.BROWN)
						.setBounds(-500, 100, 150, 0)
						.addProperty(new ValueProperty("Iterations"))
						.addProperty(new ValueProperty("Coordinate").setId("coord"))
						.addProperty(new ValueProperty("X").setId("x"))
						.addProperty(new ValueProperty("Y").setId("y"))
						.addProperty(new ValueProperty("Zoom").setId("zoom"))
						.addProperty(new ValueProperty("Limit").setId("limit"))
				)
				.addComponent(
					new Node("Palette")
						.setTitleBackground(DefaultNodeColors.GREEN)
						.setBounds(0, -20, 150, 0)
						.addProperty(new ValueProperty("Color"))
						.addProperty(new ValueProperty("Red").setId("rf"))
						.addProperty(new ValueProperty("Green").setId("gf"))
						.addProperty(new ValueProperty("Blue").setId("bf"))
						.addProperty(new ValueProperty("Scale").setId("sf"))
						.addProperty(new ValueProperty("Iterations").setId("iterations"))
				)
				.addComponent(SimpleNodesFactory.createIntermediateColorMix().setLocation(220, -140));

			//__model.print();

			// -- debugging only, serialize/deserialize model to ensure it's stateless
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (ObjectOutputStream dos = new ObjectOutputStream(baos)) {
				dos.writeObject(__model);
			}
			NodeModel model;
			try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
				model = (NodeModel) ois.readObject();
			}
			// --

			DiagramView editor = new DiagramView(model)
				.center();

			JToolBar toolbar = new JToolBar();

			toolbar.add(new AbstractAction("Math") {
				@Override
				public void actionPerformed(ActionEvent aE) {
					model.addComponent(SimpleNodesFactory.createIntermediateMath());
					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("Mix") {
				@Override
				public void actionPerformed(ActionEvent aE) {
					model.addComponent(SimpleNodesFactory.createIntermediateColorMix());
					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("Alpha") {
				@Override
				public void actionPerformed(ActionEvent aE) {
					model.addComponent(SimpleNodesFactory.createSourceAlpha());
					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("Color") {
				@Override
				public void actionPerformed(ActionEvent aE) {
					model.addComponent(SimpleNodesFactory.createSourceColor());
					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("RGB") {
				@Override
				public void actionPerformed(ActionEvent aE) {
					model.addComponent(SimpleNodesFactory.createSourceColorRGB());
					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("RGBA") {
				@Override
				public void actionPerformed(ActionEvent aE) {
					model.addComponent(SimpleNodesFactory.createSourceColorRGBA());
					editor.repaint();
				}
			});

			toolbar.add(new AbstractAction("Value") {
				@Override
				public void actionPerformed(ActionEvent aE) {
					model.addComponent(SimpleNodesFactory.createSourceValue());
					editor.repaint();
				}
			});


			JPanel panel = new JPanel(new BorderLayout());
			panel.add(toolbar, BorderLayout.NORTH);
			panel.add(editor, BorderLayout.CENTER);


			JFrame frame = new JFrame();
			frame.add(panel);
			frame.setSize(1600, 1000);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		} catch (Throwable e) {
			e.printStackTrace(System.out);
		}
	}
}
