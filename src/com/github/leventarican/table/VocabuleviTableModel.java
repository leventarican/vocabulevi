package com.github.leventarican.table;

import com.github.leventarican.model.Box;
import com.github.leventarican.model.Vocabulary;
import com.github.leventarican.utility.GlobalData;
import com.github.leventarican.utility.Utility;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

public class VocabuleviTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private static final int COLUMN_COUNT = 4;
	
	private Box box;
	private List<Vocabulary> vocabularies;
	
	private JLabel statusBar;
	
	public VocabuleviTableModel(JLabel statusBar) {
		vocabularies = new ArrayList<Vocabulary>();		
		this.statusBar = statusBar;
	}

	public void refreshTableData(Box box, JLabel statusBar) {		
		this.box = box;
		vocabularies = box.getVocabularies();
		
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return COLUMN_COUNT;
	}

	@Override
	public int getRowCount() {
		return vocabularies.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case GlobalData.SOURCE_COLUMN:
			return vocabularies.get(row).getSource();
		case GlobalData.INPUT_COLUMN:
			return vocabularies.get(row).getInput();
		case GlobalData.CHECK_COLUMN:
			return vocabularies.get(row).getCheck();
		case GlobalData.LEARNED_COLUMN:
			return vocabularies.get(row).getLearned();
		default:
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		if (col == GlobalData.INPUT_COLUMN || col == GlobalData.LEARNED_COLUMN) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		
		if(col == GlobalData.INPUT_COLUMN) {
			String temp = ((String) value).trim();
			vocabularies.get(row).setInput(temp);
			
			if (vocabularies.get(row).getSolution().equalsIgnoreCase(temp)) {
				vocabularies.get(row).setCheck(GlobalData.OK_MESSAGE);
			} else {
				vocabularies.get(row).setCheck(GlobalData.ERROR_MESSAGE);
			}
		}
		
		if (col == GlobalData.LEARNED_COLUMN) {
			vocabularies.get(row).setLearned((Boolean) value);
			if (((Boolean) value).booleanValue()) {
				((Box)box.getParent().getChildAt(0)).addVocabulary(vocabularies.get(row));
			} else {
				((Box)box.getParent().getChildAt(1)).addVocabulary(vocabularies.get(row));
			}
			this.box.getVocabularies().remove(row);
		}
		
		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int name) {
		switch (name) {
		case GlobalData.SOURCE_COLUMN:
			return GlobalData.SOURCE_COLUMN_NAME;
		case GlobalData.INPUT_COLUMN:
			return GlobalData.INPUT_COLUMN_NAME;
		case GlobalData.CHECK_COLUMN:
			return GlobalData.CHECK_COLUMN_NAME;
		case GlobalData.LEARNED_COLUMN:
			return GlobalData.LEARNED_COLUMN_NAME;
		}
		return null;
	}

	@Override
	public Class<?> getColumnClass(int index) {
		if (index == GlobalData.LEARNED_COLUMN) {
			return Boolean.class;
		} else {
			return String.class;			
		}
	}

	@Override
	public void fireTableDataChanged() {
		super.fireTableDataChanged();
		
		if (vocabularies.size() > 0) {
			int numberOfError = 0;
			for (Vocabulary	v : vocabularies) {
				if (v.getCheck().equals(GlobalData.ERROR_MESSAGE)) {
					numberOfError++;
				}
			}
			int percentage = (int) ((100.0f / vocabularies.size()) * (vocabularies.size() - numberOfError));
			statusBar.setText("[" + box.toString() + "] " + GlobalData.STATUSBAR + percentage + "%");
			if (percentage == 100) {
				statusBar.setForeground(GlobalData.DARK_GREEN);
				statusBar.setHorizontalTextPosition(JLabel.LEFT);
				statusBar.setIcon(Utility.createImageIcon("star.png"));
				statusBar.setIconTextGap(10);
			} else {
				statusBar.setForeground(Color.DARK_GRAY);
				statusBar.setIcon(null);
			}
		} else {
			statusBar.setText("[" + box.toString() + "] " + GlobalData.STATUSBAR_1);
			statusBar.setForeground(Color.DARK_GRAY);
			statusBar.setIcon(null);
		}
	}
}
