package org.terifan.boxcomponentpane;

import org.terifan.nodeeditor.Connection;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.Property;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class NodeModel implements Serializable, NodeSelectionModel {
	@Serial
	private final static long serialVersionUID = 1L;
	protected final ArrayList<Node> mComponents;
	private final List<Node> selectedNodes;
	private final List<NodeSelectionModel.Observer> selectionListeners = new ArrayList<>();
	private final ArrayList<Connection<Property>> mConnections;

	public NodeModel() {
		mComponents = new ArrayList<>();
		mConnections = new ArrayList<>();
		selectedNodes = new ArrayList<>();
	}

	public int size() {
		return mComponents.size();
	}

	public Node getComponent(int aIndex) {
		return mComponents.get(aIndex);
	}

	public Node getComponentAt(Point aPoint) {
		for (Node c : mComponents.reversed()) {
			if (c.getBounds().contains(aPoint)) {
				return c;
			}
		}
		return null;
	}

	public NodeModel addComponent(Node aComponent) {
		mComponents.add(aComponent);
		return this;
	}

	public ArrayList<Node> getComponents() {
		return mComponents;
	}

	public void moveTop(Node aComponent) {
		if (aComponent != null && mComponents.contains(aComponent)) {
			mComponents.remove(aComponent);
			mComponents.addLast(aComponent);
		}
	}

	public void removeComponents(List<Node> components) {
		components.forEach(this::removeComponent);
	}

	public void removeComponent(Node component) {
		component.getProperties().forEach(property -> {
			getConnectionsTo(property).forEach(connection -> {
				getConnections().remove(connection);
			});
			getConnectionsFrom(property).forEach(connection -> {
				getConnections().remove(connection);
			});
		});
		selectedNodes.remove(component);
		mComponents.remove(component);
	}

	public ArrayList<Connection<Property>> getConnections() {
		return mConnections;
	}

	public NodeModel addConnection(Property aFromItem, Property aToItem) {
		if (aFromItem.getNode() != aToItem.getNode()) {
			mConnections.add(new Connection<>(aFromItem, aToItem));
		}
		return this;
	}

	public List<Connection<Property>> getConnectionsTo(Property aProperty) {
		return mConnections.stream().filter(connection -> connection.getTo() == aProperty).collect(Collectors.toList());
	}

	public List<Connection<Property>> getConnectionsFrom(Property aProperty) {
		return mConnections.stream().filter(connection -> connection.getFrom() == aProperty).collect(Collectors.toList());
	}

	public ArrayList<Node> getConnectedNodes(Node aNode) {
		ArrayList<Node> result = new ArrayList<>();

		for (Connection<Property> conn : mConnections) {
			if (conn.getFrom().getNode() == aNode) {
				result.add(conn.getTo().getNode());
			}
		}

		return result;
	}

	@Override
	public List<Node> getSelectedNodes() {
		return selectedNodes;
	}

	@Override
	public void subscribe(Observer subscriber) {
		selectionListeners.add(subscriber);
	}

	@Override
	public void selectNodes(List<Node> nodes) {
		for (Node entity : nodes) selectNode(entity);
		selectionListeners.forEach(listener -> listener.onNodesSelected(nodes));
	}

	@Override
	public void selectNode(Node node) {
		if (selectedNodes.contains(node)) return;
		selectedNodes.add(node);
		selectionListeners.forEach(listener -> listener.onNodeSelected(node));
	}

	@Override
	public void requestUnselectAll() {
		if (selectedNodes.isEmpty()) return;
		selectedNodes.clear();
		selectionListeners.forEach(NodeSelectionModel.Observer::onUnselect);
	}

	@Override
	public void requestUnselectNode(Node node) {
		if (selectedNodes.isEmpty()) return;
		selectedNodes.remove(node);
		selectionListeners.forEach(NodeSelectionModel.Observer::onUnselect);
	}

	public String toString() {
		return "NodeModel{" + "mConnections=" + mConnections + '}';
	}
}
