package org.terifan.nodeeditor;

import org.terifan.boxcomponentpane.BoxComponentModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class NodeModel extends BoxComponentModel<Node> implements Serializable {
	@Serial
	private final static long serialVersionUID = 1L;

	private final ArrayList<Connection> mConnections;


	public NodeModel() {
		mConnections = new ArrayList<>();
	}


	@Override
	public NodeModel addComponent(Node aNode) {
		super.addComponent(aNode);

		aNode.bind(this);


		return this;
	}


	public ArrayList<Connection> getConnections() {
		return mConnections;
	}

	public NodeModel addConnection(Property aFromItem, Property aToItem) {
		mConnections.add(new Connection(aFromItem,aToItem));
		return this;
	}

	public List<Connection> getConnectionsTo(Property aProperty) {
		return mConnections.stream()
			.filter(connection -> connection.getIn() == aProperty)
			.collect(Collectors.toList());
	}


	public List<Connection> getConnectionsFrom(Property aProperty) {
		return mConnections.stream()
			.filter(connection -> connection.getOut() == aProperty)
			.collect(Collectors.toList());
	}


	public ArrayList<Node> getConnectedNodes(Node aNode) {
		ArrayList<Node> result = new ArrayList<>();

		for (Connection conn : mConnections) {
			if (conn.getOut().getNode() == aNode) {
				result.add(conn.getIn().getNode());
			}
		}

		return result;
	}


	@Override
	public String toString() {
		return "NodeModel{" + "mConnections=" + mConnections + '}';
	}
}
