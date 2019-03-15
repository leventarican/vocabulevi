package com.github.leventarican.table;

import com.github.leventarican.utility.GlobalData;
import com.github.leventarican.utility.Utility;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class VocabuleviTableCellRenderer implements TableCellRenderer {
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		JComponent label = new JLabel((String) value);
		label.setOpaque(true);
		label.setFont(GlobalData.FONT_TABLE);
		label.setForeground(table.getForeground());
		label.setBackground(table.getBackground());
		
		if (column != 3) {
			((JLabel)label).setHorizontalAlignment(JLabel.CENTER);
		}
		
		if (column == 1) {
			label.setBackground(GlobalData.LIGHT_BLUE);
		}
		
		if (hasFocus) {
			if (column != 1) {
				table.changeSelection(row, 1, false, false);
			}
		}
		
		if (isSelected) {
			if (column == 1) {
				label.setBackground(GlobalData.DARK_GREEN);
				label.setForeground(Color.WHITE);
			} else {
				label.setBackground(GlobalData.LIGHT_GREEN);
			}			
		}
		
		if (column == 2) {
			((JLabel)label).setText("");
			if (table.getValueAt(row, 2).equals("ERROR")) {
				((JLabel)label).setIcon(Utility.createImageIcon("error.png"));
			} else {
				((JLabel)label).setIcon(Utility.createImageIcon("ok.png"));
			}
		}
		
		return label;
	}
}
