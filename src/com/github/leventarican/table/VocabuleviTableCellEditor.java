package com.github.leventarican.table;

import com.github.leventarican.utility.GlobalData;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class VocabuleviTableCellEditor extends AbstractCellEditor implements TableCellEditor {

	JComponent component = new JTextField();

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int col) {		
		((JTextField)component).setFont(GlobalData.FONT_TABLE_EDIT);
		((JTextField)component).setForeground(GlobalData.DARK_BLUE);
		((JTextField)component).setText(value.toString());
		return component;
	}

	@Override
	public Object getCellEditorValue() {
		return ((JTextField)component).getText(); 
	}

}
