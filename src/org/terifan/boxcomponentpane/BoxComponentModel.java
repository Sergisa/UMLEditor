package org.terifan.boxcomponentpane;

import org.terifan.nodeeditor.Node;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class BoxComponentModel<T extends BoxComponent> implements Serializable
{
	@Serial
	private final static long serialVersionUID = 1L;
	protected final ArrayList<T> mComponents;


	public BoxComponentModel()
	{
		mComponents = new ArrayList<>();
	}


	public int size()
	{
		return mComponents.size();
	}


	public T getComponent(int aIndex)
	{
		return mComponents.get(aIndex);
	}


	public BoxComponentModel<T> addComponent(T aComponent)
	{
		mComponents.add(aComponent);
		return this;
	}


	public ArrayList<T> getComponents()
	{
		return mComponents;
	}


	public void moveTop(T aComponent)
	{
		if (aComponent != null)
		{
			mComponents.remove(aComponent);
			mComponents.addLast(aComponent);
		}
	}


	public T getComponentAt(Point aPoint)
	{
		for (T c : mComponents.reversed())
		{
			Rectangle b = c.getBounds();

			if (b.contains(aPoint))
			{
				return c;
			}
		}

		return null;
	}

	public void removeComponents(List<Node> components){
		components.forEach(this::removeComponent);
	}

	public void removeComponent(Node component){

		component.getProperties().forEach(property -> {
			component.getModel().getConnectionsTo(property).forEach(connection -> {
				component.getModel().getConnections().remove(connection);
			});
			component.getModel().getConnectionsFrom(property).forEach(connection -> {
				component.getModel().getConnections().remove(connection);
			});
		});
		mComponents.remove(component);
	}
}
