package org.terifan.model;

import org.terifan.nodeeditor.Node;

import java.util.List;

public interface NodeSelectionModel {
	interface Observer {
		void onNodesSelected(List<Node> nodes);

		void onNodeSelected(Node node);

		//public void onLinkSelected(Connection<Path> link);

		void onUnselect();
	}

	void subscribe(Observer subscriber);

	List<Node> getSelectedNodes();

	//Connection<Path> selectedLink();

	void selectNodes(List<Node> nodes);

	//public void requestSelectLink(Connection<Path> link);

	void selectNode(Node node);

	void requestUnselectAll();

	void requestUnselectNode(Node node);
}
