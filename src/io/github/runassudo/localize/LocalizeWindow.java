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
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import javax.swing.JScrollPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser(".");
				if (fc.showOpenDialog(frmLocalizeTool) == JFileChooser.APPROVE_OPTION) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							openFile(fc.getSelectedFile());
						}
					}).start();
				}
			}
		});
		mnFile.add(mntmOpen);

		JMenuItem mntmSaveAs = new JMenuItem("Save As…");
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser(".");
				if (fc.showSaveDialog(frmLocalizeTool) == JFileChooser.APPROVE_OPTION) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							saveFile(fc.getSelectedFile());
						}
					}).start();
				}
			}
		});
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
		mntmFindReplace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String query = JOptionPane.showInputDialog(frmLocalizeTool,
						"Query: ", "Find", JOptionPane.QUESTION_MESSAGE);

				for (int i = (table.getSelectedRow() >= 0 ? table
						.getSelectedRow() : 0); i < table.getRowCount(); i++) {
					if (((String) table.getValueAt(i, 3)).contains(query)) {
						table.setRowSelectionInterval(i, i);
						table.scrollRectToVisible(table.getCellRect(i, 0, true));
						return;
					}
				}

				JOptionPane.showMessageDialog(frmLocalizeTool,
						"Could not find query string.", "Find",
						JOptionPane.ERROR_MESSAGE);
			}
		});
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

		int valid = 0;

		for (int i = 0; i < string.length(); i++) {
			int code = string.codePointAt(i);

			if (isValidChar(code))
				valid++;
		}

		if ((valid * 100) / string.length() >= Integer
				.parseInt(preferences.txtMinValid.getText()))
			return true;
		return false;
	}

	private File inFile;

	private void importFile(File file) {
		inFile = file;

		ProgressWindow window = new ProgressWindow();
		window.progressBar.setMinimum(0);
		window.progressBar.setMaximum((int) inFile.length());

		// to enable sorting
		final ArrayList<LocalizableString> strings = new ArrayList<LocalizableString>();

		try {

			if (preferences.cbASCII.isSelected())
				directImport(file, window, strings,
						LocalizableString.Encoding.ASCII, 0);
			if (preferences.cbUTF8.isSelected())
				directImport(file, window, strings,
						LocalizableString.Encoding.UTF8, 0);
			if (preferences.cbShiftJIS.isSelected())
				directImport(file, window, strings,
						LocalizableString.Encoding.SHIFTJIS, 0);

			if (preferences.cbUTF16LE.isSelected()) {
				direct2Import(file, window, strings,
						LocalizableString.Encoding.UTF16LE, 0);
				direct2Import(file, window, strings,
						LocalizableString.Encoding.UTF16LE, 1);
			}
			if (preferences.cbUTF16BE.isSelected()) {
				direct2Import(file, window, strings,
						LocalizableString.Encoding.UTF16BE, 0);
				direct2Import(file, window, strings,
						LocalizableString.Encoding.UTF16BE, 1);
			}

			Collections.sort(strings, new Comparator<LocalizableString>() {
				@Override
				public int compare(LocalizableString arg0,
						LocalizableString arg1) {
					return arg0.location - arg1.location;
				}
			});

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					table.setModel(new LocalizeTableModel(strings));
				}
			});

			window.setVisible(false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void directImport(File file, ProgressWindow window,
			ArrayList<LocalizableString> strings,
			LocalizableString.Encoding encoding, int skip) throws IOException {
		window.label.setText("Scanning " + encoding);

		int current = 0; // Caret location
		int location = 0; // String location
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		FileInputStream in = new FileInputStream(file);

		for (int i = 0; i < skip; i++) {
			in.read();
			current++;
		}

		int c = -1;
		while ((c = in.read()) >= 0) {
			window.progressBar.setValue(current);

			if (c == 0x00) {
				String string = new String(buffer.toByteArray(),
						encoding.getCharset());

				if (isValidString(buffer.size(), string)) {
					LocalizableString lstring = new LocalizableString(location,
							buffer.size(), encoding, string);
					strings.add(lstring);
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
	}

	private void direct2Import(File file, ProgressWindow window,
			ArrayList<LocalizableString> strings,
			LocalizableString.Encoding encoding, int skip) throws IOException {
		window.label.setText("Scanning " + encoding);

		int current = 0; // Caret location
		int location = 0; // String location
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		FileInputStream in = new FileInputStream(file);

		for (int i = 0; i < skip; i++) {
			in.read();
			current++;
		}

		int c = -1, d = -1;
		while ((c = in.read()) >= 0) {
			if ((d = in.read()) >= 0) {
				window.progressBar.setValue(current);

				if (c == 0x00 && d == 0x00) {
					String string = new String(buffer.toByteArray(),
							encoding.getCharset());

					if (isValidString(buffer.size(), string)) {
						LocalizableString lstring = new LocalizableString(
								location, buffer.size(), encoding, string);
						strings.add(lstring);
					}

					// Reset
					location = current + 2;
					buffer.reset();
				} else {
					buffer.write(c);
					buffer.write(d);
				}

				current += 2;
			}
		}
		in.close();
	}

	private void exportFile(File file) {
		ProgressWindow window = new ProgressWindow();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

			}
		});
		window.progressBar.setMinimum(0);
		window.progressBar.setMaximum((int) inFile.length());

		try {

			int current = 0; // Caret location

			FileInputStream in = new FileInputStream(inFile);
			FileOutputStream out = new FileOutputStream(file);
			ArrayList<LocalizableString> strings = ((LocalizeTableModel) table
					.getModel()).getStrings();

			int c = -1;
			while ((c = in.read()) >= 0) {
				window.progressBar.setValue(current);

				boolean found = false;
				for (LocalizableString string : strings) {
					if (string.location == current && string.edited) {
						byte[] bytes = null;

						bytes = string.translation.getBytes(string.encoding
								.getCharset());

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
							current++;
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

	@SuppressWarnings("unchecked")
	private void openFile(File file) {
		try {

			FileInputStream fis = new FileInputStream(file);

			DataInputStream dis = new DataInputStream(fis);
			inFile = new File(dis.readUTF());

			ObjectInputStream ois = new ObjectInputStream(dis);
			final ArrayList<LocalizableString> strings = (ArrayList<LocalizableString>) ois
					.readObject();
			fis.close();

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					table.setModel(new LocalizeTableModel(strings));
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveFile(File file) {
		try {

			FileOutputStream fos = new FileOutputStream(file);

			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeUTF(inFile.getPath());

			ObjectOutputStream oos = new ObjectOutputStream(dos);
			oos.writeObject(((LocalizeTableModel) table.getModel())
					.getStrings());

			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
