package examples.deprecated;

import org.terifan.boxcomponentpane.DiagramView;
import org.terifan.boxcomponentpane.NodeModel;

import javax.swing.*;
import java.awt.*;


public class TestEditor {
	static void main(String... args) {
		try {
			NodeModel model = new NodeModel();

			DiagramView editor = new DiagramView(model);

			editor.center();
			editor.setScale(1);

			JToolBar toolbar = new JToolBar();

			JPanel panel = new JPanel(new BorderLayout());
			panel.add(toolbar, BorderLayout.NORTH);
			panel.add(editor, BorderLayout.CENTER);

			JFrame frame = new JFrame();
			frame.add(panel);
			frame.setSize((int) (1600 * editor.getScale()), (int) (1000 * editor.getScale()));
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		} catch (Throwable e) {
			e.printStackTrace(System.out);
		}
	}
}
