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


public class NodeModel implements Serializable, NodeSelectionModel, INodeModel {
	@Serial
	private final static long serialVersionUID = 1L;
	protected final ArrayList<Node> mComponents;
	private final List<Node> selectedNodes;
	private final List<NodeSelectionModel.Observer> selectionListeners = new ArrayList<>();
	private final List<INodeModel.Observer> modelChangeListeners = new ArrayList<>();
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

	public NodeModel addNode(Node aComponent) {
		mComponents.add(aComponent);
		return this;
	}

	public ArrayList<Node> getNodes() {
		return mComponents;
	}

	public void moveToFront(Node aComponent) {
		if (aComponent != null && mComponents.contains(aComponent)) {
			mComponents.remove(aComponent);
			mComponents.addLast(aComponent);
		}
	}

	public void moveToBack(Node aComponent) {
		if (aComponent != null && mComponents.contains(aComponent)) {
			mComponents.remove(aComponent);
			mComponents.addFirst(aComponent);
		}
	}

	public void removeComponents(List<Node> components) {
		components.forEach(this::removeNode);
	}

	public void removeNode(Node component) {
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
		return addConnection(new Connection<>(aFromItem, aToItem));
	}

	@Override
	public NodeModel addConnection(Connection<Property> aConnection) {
		//TODO: применить класс ConnectionResolver который будет отвечать на вопрос одобрить или запретить соединение
		Property aFromItem = aConnection.getFrom();
		Property aToItem = aConnection.getTo();
		if (aFromItem.getNode() != aToItem.getNode()) {
			mConnections.add(aConnection);
		}
		return this;
	}

	@Override
	public void notifyEntityMoved(Node node) {
		modelChangeListeners.forEach(listener -> listener.entityMoved(node));
	}

	@Override
	public void notifyEntityUpdated(Node node) {

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
	public void subscribe(NodeSelectionModel.Observer subscriber) {
		selectionListeners.add(subscriber);
	}

	@Override
	public void subscribe(INodeModel.Observer subscriber) {
		modelChangeListeners.add(subscriber);
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
