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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class LocalizeWindow {

	private PreferencesWindow preferences = new PreferencesWindow();

	private JFrame frmLocalizeTool;
	private JTable table;

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
		mntmImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser(".");
				if (fc.showOpenDialog(frmLocalizeTool) == JFileChooser.APPROVE_OPTION) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							importFile(fc.getSelectedFile());
						}
					}).start();
				}
			}
		});
		mnFile.add(mntmImport);

		JMenuItem mntmExport = new JMenuItem("Export…");
		mntmExport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser(".");
				if (fc.showSaveDialog(frmLocalizeTool) == JFileChooser.APPROVE_OPTION) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							exportFile(fc.getSelectedFile());
						}
					}).start();
				}
			}
		});
		mnFile.add(mntmExport);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenuItem mntmFindReplace = new JMenuItem("Find/Replace…");
		mnEdit.add(mntmFindReplace);

		JSeparator separator_1 = new JSeparator();
		mnEdit.add(separator_1);

		JMenuItem mntmPreferences = new JMenuItem("Preferences…");
		mntmPreferences.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				preferences.setVisible(true);
			}
		});
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
		if (preferences.cbControls.isSelected()
				&& (code == 0x0009 || code == 0x000A || code == 000D))
			return true; // Textual Controls

		if (preferences.cbBasicLatin.isSelected() && code >= 0x0020
				&& code <= 0x007E)
			return true; // Basic Latin

		if (preferences.cbExtendedLatin.isSelected() && code >= 0x00A0
				&& code <= 0x00FF)
			return true; // Latin-1 Supplement
		if (preferences.cbExtendedLatin.isSelected() && code >= 0x0100
				&& code <= 0x017F)
			return true; // Latin Extended-A
		if (preferences.cbExtendedLatin.isSelected() && code >= 0x0180
				&& code <= 0x024F)
			return true; // Latin Extended-B

		if (preferences.cbCJK.isSelected() && code >= 0x4E00 && code <= 0x9FFF)
			return true; // CJK Unified Ideographs
		if (preferences.cbCJK.isSelected() && code >= 0x3400 && code <= 0x4DBF)
			return true; // CJK Unified Ideographs Extension A
		if (preferences.cbCJK.isSelected() && code >= 0x20000
				&& code <= 0x2A6DF)
			return true; // CJK Unified Ideographs Extension B
		if (preferences.cbCJK.isSelected() && code >= 0x2A700
				&& code <= 0x2B73F)
			return true; // CJK Unified Ideographs Extension C
		if (preferences.cbCJK.isSelected() && code >= 0x2B740
				&& code <= 0x2B81F)
			return true; // CJK Unified Ideographs Extension D

		return false;
	}

	private boolean isValidString(int bytes, String string) {
		if (bytes < Integer.parseInt(preferences.txtMinBytes.getText()))
			return false;

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

	private File inFile;

	private void importFile(File file) {
		inFile = file;

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

					if (isValidString(buffer.size(), string)) {
						LocalizeableString lstring = new LocalizeableString(
								location, buffer.size(),
								LocalizeableString.Encoding.SHIFTJIS, string);
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

	private void exportFile(File file) {
		ProgressWindow window = new ProgressWindow();

		try {

			int current = 0; // Caret location

			FileInputStream in = new FileInputStream(inFile);
			FileOutputStream out = new FileOutputStream(file);
			ArrayList<LocalizeableString> strings = ((LocalizeTableModel) table
					.getModel()).getStrings();

			int c = -1;
			while ((c = in.read()) >= 0) {

				boolean found = false;
				for (LocalizeableString string : strings) {
					if (string.location == current && string.edited) {
						byte[] bytes = null;

						if (string.encoding == LocalizeableString.Encoding.SHIFTJIS) {
							bytes = string.translation.getBytes("Shift_JIS");
						}

						out.write(bytes);

						if (bytes.length < string.length) {
							int pad = string.length - bytes.length;
							for (int i = 0; i < pad; i++) {
								out.write(0);
							}
						}

						found = true;

						for (int i = 0; i < string.length - 1; i++) {
							in.read();
						}

						break;
					}
				}

				if (!found) {
					out.write(c);
				}

				current++;
			}
			in.close();
			out.close();

			window.setVisible(false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
