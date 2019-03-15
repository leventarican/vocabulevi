package com.github.leventarican.model;

import javax.swing.tree.DefaultMutableTreeNode;

public class Language extends DefaultMutableTreeNode {
	
	public Language(Object userObject) {
		super(userObject);
		
		add(new Box("Learned"));
		add(new Box("To learn"));
	}
}
