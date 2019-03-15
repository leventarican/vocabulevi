package com.github.leventarican.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

public class Box extends DefaultMutableTreeNode {
	
	private List<Vocabulary> vocabularies;
	
	public Box(Object userObject) {
		super(userObject);		
		setVocabularies(new ArrayList<Vocabulary>());
	}
	
	@Override
	public void add(MutableTreeNode newChild) {
		super.add(newChild);
	}
	
	public void addVocabulary(Vocabulary v) {
		getVocabularies().add(v);
	}

	public void setVocabularies(List<Vocabulary> vocabularies) {
		this.vocabularies = vocabularies;
	}

	public List<Vocabulary> getVocabularies() {
		return vocabularies;
	}
}
