package io.github.runassudo.localize;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.JScrollPane;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.Action;

public class LocalizeWindow {

	private JFrame frmLocalizeTool;
	private JTable table;
	private final Action action = new ImportAction();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LocalizeWindow window = new LocalizeWindow();
					window.frmLocalizeTool.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LocalizeWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLocalizeTool = new JFrame();
		frmLocalizeTool.setTitle("Localize Tool");
		frmLocalizeTool.setBounds(100, 100, 450, 300);
		frmLocalizeTool.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frmLocalizeTool.getContentPane().add(menuBar, BorderLayout.NORTH);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmOpen = new JMenuItem("Open…");
		mnFile.add(mntmOpen);

		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);

		JMenuItem mntmSaveAs = new JMenuItem("Save As…");
		mnFile.add(mntmSaveAs);

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		JMenuItem mntmImport = new JMenuItem("Import…");
		mntmImport.setAction(action);
		mnFile.add(mntmImport);

		JMenuItem mntmExport = new JMenuItem("Export…");
		mnFile.add(mntmExport);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenuItem mntmFindReplace = new JMenuItem("Find/Replace…");
		mnEdit.add(mntmFindReplace);

		JSeparator separator_1 = new JSeparator();
		mnEdit.add(separator_1);

		JMenuItem mntmPreferences = new JMenuItem("Preferences…");
		mnEdit.add(mntmPreferences);

		table = new JTable(new LocalizeTableModel()) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer,
					int row, int col) {
				Component c = super.prepareRenderer(renderer, row, col);

				if ((boolean) getModel().getValueAt(row, 4)) { // is edited?
					c.setFont(c.getFont().deriveFont(Font.BOLD));
				}

				return c;
			}
		};
		table.setFillsViewportHeight(true);

		JScrollPane scrollPane = new JScrollPane(table);
		frmLocalizeTool.getContentPane().add(scrollPane, BorderLayout.CENTER);
	}

	private boolean isValidChar(int code) {
		if (code >= 0x0020 && code <= 0x007E)
			return true; // Basic Latin

		return false;
	}

	private boolean isValidString(String string) {
		float valid = 0;

		for (int i = 0; i < string.length(); i++) {
			int code = string.codePointAt(i);

			if (isValidChar(code))
				valid++;
		}

		if (valid / string.length() >= 0.70)
			return true;
		return false;
	}

	private void importFile(File file) {
		ProgressWindow window = new ProgressWindow();

		try {

			int current = 0; // Caret location
			int location = 0; // String location
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			// Read SHIFTJIS

			FileInputStream in = new FileInputStream(file);
			int c = -1;
			while ((c = in.read()) >= 0) {
				if (c == 0x00) {
					String string = new String(buffer.toByteArray(),
							"Shift_JIS");

					if (isValidString(string)) {
						LocalizeableString lstring = new LocalizeableString(
								location, LocalizeableString.Encoding.SHIFTJIS,
								buffer.toString());
						((LocalizeTableModel) table.getModel()).addRow(lstring);
					}

					// Reset
					location = current + 1;
					buffer.reset();
				} else {
					buffer.write(c);
				}

				current++;
			}
			in.close();

			window.setVisible(false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class ImportAction extends AbstractAction {
		public ImportAction() {
			putValue(NAME, "Import…");
			putValue(SHORT_DESCRIPTION, "Import a binary file");
		}

		public void actionPerformed(ActionEvent e) {
			final JFileChooser fc = new JFileChooser();
			if (fc.showOpenDialog(frmLocalizeTool) == JFileChooser.APPROVE_OPTION) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						importFile(fc.getSelectedFile());
					}
				}).start();
			}
		}
	}
}
