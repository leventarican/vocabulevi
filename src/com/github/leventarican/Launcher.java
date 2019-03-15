package com.github.leventarican;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeNode;

import com.github.leventarican.model.Box;
import com.github.leventarican.model.Language;
import com.github.leventarican.model.Vocabulary;
import com.github.leventarican.table.VocabuleviTableCellEditor;
import com.github.leventarican.table.VocabuleviTableCellRenderer;
import com.github.leventarican.table.VocabuleviTableModel;
import com.github.leventarican.utility.FileHandling;
import com.github.leventarican.utility.GlobalData;
import com.github.leventarican.utility.Utility;

public class Launcher {

	private static String filename = "vocabulevi.txt";
	
	private static DefaultMutableTreeNode dictionary = new DefaultMutableTreeNode("Dictionary");
	private static JTree tree;

	public static void main(String[] args) {
		buildData();
		buildView();
	}
	
	private static void buildData() {
		buildModel();
		if (tree != null) {
			tree.updateUI();
		}
	}

	private static void buildView() {
		JFrame frame = new JFrame("Vocabulevi - Version 1.0");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setLocation(100, 100);
		
		JPanel panel = new JPanel(new BorderLayout());
		
		JToolBar toolBar = new JToolBar("Toolbar");
		buildButtons(toolBar);
		
		tree = new JTree(dictionary);		
		ImageIcon leafIcon = Utility.createImageIcon("box.png");
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setLeafIcon(leafIcon);
		tree.getSelectionModel().setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setCellRenderer(renderer);
		
		final JLabel statusBar = new JLabel();
		statusBar.setHorizontalAlignment(JLabel.CENTER);
		final VocabuleviTableModel tableModel = new VocabuleviTableModel(statusBar);		
		JTable table = new JTable(tableModel);
		table.getColumnModel().getColumn(1).setCellEditor(new VocabuleviTableCellEditor());
		table.setDefaultRenderer(Object.class, new VocabuleviTableCellRenderer());
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(28);
		table.setColumnSelectionAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.getColumnModel().getColumn(0).setMinWidth(150);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setMaxWidth(70);
		table.getColumnModel().getColumn(3).setMaxWidth(70);
		
		modifyTableHeader(table);
		
		JScrollPane scrollTable = new JScrollPane(table);
		
		final JPanel rightComponent = new JPanel(new BorderLayout());
		rightComponent.add(scrollTable, BorderLayout.CENTER);
		rightComponent.add(statusBar, BorderLayout.PAGE_END);
		
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		final JLabel label = new JLabel();		
		
		splitPane.add(new JScrollPane(tree));
		splitPane.add(rightComponent);
		splitPane.setDividerLocation(GlobalData.DIVIDER_LOCATION);
		splitPane.getRightComponent().setPreferredSize(new Dimension(500, 400));
		
        panel.add(toolBar, BorderLayout.PAGE_START);
        panel.add(splitPane, BorderLayout.CENTER);
        
		frame.add(panel);
		
		tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				if (tree.getLastSelectedPathComponent() instanceof Box) {
					tableModel.refreshTableData((Box) tree.getLastSelectedPathComponent(), statusBar);
					splitPane.setRightComponent(rightComponent);
					splitPane.setDividerLocation(GlobalData.DIVIDER_LOCATION);
				}
				else {
					String text = null;
					if (tree.getLastSelectedPathComponent() instanceof Language) {
						int vocabularyLearnedCount = ((Box)((Language)tree.getLastSelectedPathComponent()).getChildAt(0)).getVocabularies().size();
						int vocabularyToLearnCount = ((Box)((Language)tree.getLastSelectedPathComponent()).getChildAt(1)).getVocabularies().size();
						text = "<HTML><BODY><H3>" + ((Language) tree.getLastSelectedPathComponent()).toString() + "</H3>" + "Count of learned vocabularies: " + vocabularyLearnedCount + "<BR>" + "Count of vocabularies to learn: " + vocabularyToLearnCount + "</BODY></HTML>";
					} else {
						text = GlobalData.DESCRIPTION;
					}
					label.setText(text);
					label.setHorizontalAlignment(JLabel.CENTER);					
					splitPane.setBottomComponent(label);
					splitPane.setDividerLocation(GlobalData.DIVIDER_LOCATION);
				}
			}
		});
		
		frame.setIconImage(Utility.createImageIcon("app.png").getImage());
		frame.pack();
		Utility.centerFrame(frame);
		tree.setSelectionRow(0);
		frame.setVisible(true);
	}

	private static void modifyTableHeader(JTable table) {
		JLabel[] header = {null, null, null, null};
		Border blackline = BorderFactory.createLineBorder(Color.BLACK);
		TableCellRenderer renderer = new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				return (JComponent) value;
			}
		};
		for (int i = 0; i < 4; i++) {
			switch (i) {
			case 0:
				header[i] = new JLabel(GlobalData.SOURCE_COLUMN_NAME, JLabel.CENTER);
				break;
			case 1:
				header[i] = new JLabel(GlobalData.INPUT_COLUMN_NAME, Utility.createImageIcon("pencil.png"), JLabel.CENTER);
				header[i].setHorizontalTextPosition(JLabel.LEFT);
				header[i].setIconTextGap(10);
				break;
			case 2:
				header[i] = new JLabel(GlobalData.CHECK_COLUMN_NAME, JLabel.CENTER);
				break;
			case 3:
				header[i] = new JLabel(GlobalData.LEARNED_COLUMN_NAME, JLabel.CENTER);
				break;
			}
			header[i].setBorder(blackline);
			header[i].setOpaque(true);
			header[i].setBackground(Color.GRAY);
			header[i].setForeground(Color.WHITE);
			table.getColumnModel().getColumn(i).setHeaderRenderer(renderer);
			table.getColumnModel().getColumn(i).setHeaderValue(header[i]);
		}
	}

	private static void buildButtons(JToolBar toolBar) {
		JButton buttonSave = new JButton("Save", Utility.createImageIcon("save.png"));
		buttonSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {Launcher.saveAction();}
		});
		JButton buttonOpen = new JButton("Open", Utility.createImageIcon("open.png"));
		buttonOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {Launcher.openAction();}
		});
		JButton resetOpen = new JButton("Reset", Utility.createImageIcon("reset.png"));
		resetOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {Launcher.resetAction();}
		});
		toolBar.add(buttonSave);
		toolBar.add(buttonOpen);
		toolBar.add(resetOpen);
	}

	protected static void resetAction() {
		dictionary.removeAllChildren();
		buildData();		
		tree.setSelectionRow(0);
	}

	protected static void openAction() {
		final JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            filename = fc.getSelectedFile().getAbsolutePath();
            dictionary.removeAllChildren();
            buildData();
        }
	}

	@SuppressWarnings("unchecked")
	protected static void saveAction() {
		List<String> list = new ArrayList<String>();
		Enumeration<TreeNode> e = dictionary.children();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode language = (DefaultMutableTreeNode) e.nextElement();
			list.add(String.format("language=%s", language.toString().trim()));
			for (Vocabulary v : ((Box)language.getChildAt(0)).getVocabularies())
				list.add(String.format("%s,%s,1", v.getSource(), v.getSolution()));
			for (Vocabulary v : ((Box)language.getChildAt(1)).getVocabularies())
				list.add(String.format("%s,%s,0", v.getSource(), v.getSolution()));
		}		
		Utility.writeLinesToFile(filename, list);
	}

	private static void buildModel() {
		try {
			FileHandling fh = new FileHandling(new File(filename));
			for (int i = 0; i < fh.getLineHolder().size(); i++) {
				if(fh.getLineHolder().get(i).startsWith("language=")) {
					buildTree(fh.getLineHolder(), i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	private static void buildTree(List<String> lineHolder, int index) {
		Language language = new Language(lineHolder.get(index).substring(9));
		index++;
		for (int i = index; i < lineHolder.size(); i++) {
			if(!lineHolder.get(i).startsWith("language=")) {
				if(lineHolder.get(i).contains(",")) {
					String[] temp = lineHolder.get(i).split(",");
					Vocabulary v = new Vocabulary();
					v.setSource(temp[0].trim());
					v.setSolution(temp[1].trim());
					v.setInput("");
					v.setCheck(GlobalData.ERROR_MESSAGE);
					
					if (temp.length > 2) {
						if (temp[2].trim().equals("0")) {
							v.setLearned(false);
							((Box)language.getChildAt(1)).addVocabulary(v);
						} else {
							v.setLearned(true);
							((Box)language.getChildAt(0)).addVocabulary(v);
						}
					} else {
						v.setLearned(false);
						((Box)language.getChildAt(1)).addVocabulary(v);
					}
				}
			} else {
				break;
			}
		}
		dictionary.add(language);
	}
}
