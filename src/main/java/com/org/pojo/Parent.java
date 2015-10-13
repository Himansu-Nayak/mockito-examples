package com.org.pojo;

public class Parent {

	private Child child;
	
	public Child getChild() {
		return child;
	}

	public String getChildName() {
		return child.getName();
	}

	public void wuzzle() {
		Child newChild = new Child();
		newChild.setName("Toofan Singh");
		child.display(newChild);
	}

	public void setChild(Child child) {
		this.child = child;
	}

	public String parent() {
		return "parent!";
	}

	public void setName(String name) {
		child.setName(name);
	}
}