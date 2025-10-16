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


public class MandelbrotExample {
	public final static boolean DEBUG = true;

	static void main() {
		try {
			ValueProperty mandelbrotIterations = new ValueProperty("Iterations");
			ValueProperty paletteColor = new ValueProperty("Color");
			ValueProperty paleteRedProperty = new ValueProperty("Red");
			NodeModel model = new NodeModel()
				.addComponent(
					new Node("Mandelbrot")
						.setTitleBackground(DefaultNodeColors.BROWN)
						.setBounds(-500, 100, 150, 0)
						.addProperty(mandelbrotIterations)
						.addProperty(new ValueProperty("Coordinate"))
						.addProperty(new ValueProperty("X"))
						.addProperty(new ValueProperty("Y"))
						.addProperty(new ValueProperty("Zoom"))
						.addProperty(new ValueProperty("Limit"))
				)
				.addComponent(
					new Node("Palette")
						.setTitleBackground(DefaultNodeColors.GREEN)
						.setBounds(0, -20, 150, 0)
						.addProperty(paletteColor)
						.addProperty(paleteRedProperty)
						.addProperty(new ValueProperty("Green"))
						.addProperty(new ValueProperty("Blue"))
						.addProperty(new ValueProperty("Scale"))
						.addProperty(new ValueProperty("Iterations"))
				)
				.addConnection(paletteColor, mandelbrotIterations)
				.addConnection(paletteColor, paleteRedProperty)
				.addComponent(
					SimpleNodesFactory.
						createIntermediateColorMix().
						setLocation(220, -140)
				);

			DiagramView editor = new DiagramView(model).center();

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
