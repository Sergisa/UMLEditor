package org.terifan.boxcomponentpane;

import org.terifan.nodeeditor.Node;

import java.util.List;

public interface NodeSelectionModel {
	interface Observer {
		void onNodesSelected(List<Node> entity);

		void onNodeSelected(Node entity);

		//public void onLinkSelected(Connection<Path> link);

		void onUnselect();
	}

	void subscribe(Observer subscriber);

	List<Node> getSelectedNodes();

	//Connection<Path> selectedLink();

	void selectNodes(List<Node> entity);

	//public void requestSelectLink(Connection<Path> link);

	void selectNode(Node entity);

	void requestUnselectAll();

	void requestUnselectNode(Node node);
}
