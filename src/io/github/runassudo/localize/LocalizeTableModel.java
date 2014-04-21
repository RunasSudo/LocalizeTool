package io.github.runassudo.localize;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class LocalizeTableModel extends AbstractTableModel {

	private ArrayList<LocalizeableString> strings;

	public LocalizeTableModel() {
		this.strings = new ArrayList<LocalizeableString>();
	}

	public LocalizeTableModel(ArrayList<LocalizeableString> strings) {
		this.strings = strings;
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "Location";
		case 1:
			return "Encoding";
		case 2:
			return "Original";
		case 3:
			return "Translation";
		}
		return null;
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
		case 0:
			return String.class;
		case 1:
			return LocalizeableString.Encoding.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		}
		return null;
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return strings.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		LocalizeableString string = strings.get(row);

		switch (col) {
		case 0:
			return String.format("%X", string.location);
		case 1:
			return string.encoding;
		case 2:
			return string.original;
		case 3:
			return string.translation;
		case 4:
			return string.edited;
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return col == 3;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		LocalizeableString string = strings.get(row);

		if (col == 3) {
			string.translation = value.toString();

			if (string.translation.equals(string.original))
				string.edited = false;
			else
				string.edited = true;

			fireTableRowsUpdated(row, row);
		}
	}

	public void addRow(LocalizeableString string) {
		strings.add(string);
		fireTableRowsInserted(strings.size() - 1, strings.size() - 1);
	}

	public ArrayList<LocalizeableString> getStrings() {
		return strings;
	}

}
