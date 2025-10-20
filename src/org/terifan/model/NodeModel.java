package org.terifan.model;

import org.terifan.nodeeditor.Connection;
import org.terifan.nodeeditor.Node;
import org.terifan.nodeeditor.Property;

import java.awt.*;

public interface NodeModel {
	interface Observer {
		void entityCreated(Node node);

		void entityDestroyed(Node node);

		void entityUpdated(Node node);

		void entityMoved(Node node);

		void entityReordered(Node node);

		void linkCreated(Connection link);

		void linkDestroyed(Connection link);

		void linkUpdated(Connection link);

		void linkStyleChanged(Connection link);
	}

	class ObserverAdapter implements Observer {
		@Override
		public void entityCreated(Node node) {
		}

		@Override
		public void entityDestroyed(Node node) {
		}

		@Override
		public void entityUpdated(Node node) {
		}

		@Override
		public void entityMoved(Node entity) {
		}

		@Override
		public void entityReordered(Node entity) {
		}

		@Override
		public void linkCreated(Connection link) {
		}

		@Override
		public void linkDestroyed(Connection link) {
		}

		@Override
		public void linkUpdated(Connection link) {
		}

		@Override
		public void linkStyleChanged(Connection link) {
		}
	}

	void notifyEntityUpdated(Node node);

	void notifyEntityMoved(Node node);

	void subscribe(Observer path);

	Node getComponentAt(Point point);

	BaseNodeModel addNode(Node node);

	void removeNode(Node node);

	void moveToBack(Node node);

	void moveToFront(Node node);

	BaseNodeModel addConnection(Connection<Property> link);

	//int numEntities();

	//Node getEntity(int i);

	//int entityIndexOf(Node node);

	//int numLinks();

	//Connection linkAt(int x, int y);

	//void unlink(Connection link);

	//Connection getLink(int i);

	//void saveModel(DataOutputStream outputStream) throws IOException;

	//void loadModel(DataInputStream inputStream) throws IOException;

	//void notifyLinkUpdated(Connection link);

	//void notifyLinkStyleChanged(Connection link);
}
