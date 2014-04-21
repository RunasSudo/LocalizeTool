package io.github.runassudo.localize;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class PreferencesWindow extends JFrame {

	private JPanel contentPane;
	public JCheckBox cbASCII;
	public JCheckBox cbUTF8;
	public JCheckBox cbShiftJIS;
	public JCheckBox cbUCS2LE;
	public JCheckBox cbUCS2BE;
	public JCheckBox cbUTF16LE;
	public JCheckBox cbUTF16BE;
	public JCheckBox cbControls;
	public JCheckBox cbBasicLatin;
	public JCheckBox cbExtendedLatin;
	public JCheckBox cbCJK;
	public JTextField txtMinBytes;
	public JTextField txtMinValid;

	/**
	 * Create the frame.
	 */
	public PreferencesWindow() {
		setTitle("Preferences");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel pnEncodings = new JPanel();
		JScrollPane spEncodings = new JScrollPane(pnEncodings);
		tabbedPane.addTab("Encodings", null, spEncodings, null);
		pnEncodings.setLayout(new FormLayout(
				new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		cbASCII = new JCheckBox("ASCII");
		pnEncodings.add(cbASCII, "2, 2");

		cbUTF8 = new JCheckBox("UTF-8");
		pnEncodings.add(cbUTF8, "2, 4");

		cbShiftJIS = new JCheckBox("Shift JIS");
		cbShiftJIS.setSelected(true);
		pnEncodings.add(cbShiftJIS, "2, 6");

		cbUCS2LE = new JCheckBox("UCS-2 (LE)");
		cbUCS2LE.setSelected(true);
		pnEncodings.add(cbUCS2LE, "2, 8");

		cbUCS2BE = new JCheckBox("UCS-2 (BE)");
		pnEncodings.add(cbUCS2BE, "2, 10");

		cbUTF16LE = new JCheckBox("UTF-16 (LE)");
		pnEncodings.add(cbUTF16LE, "2, 12");

		cbUTF16BE = new JCheckBox("UTF-16 (BE)");
		pnEncodings.add(cbUTF16BE, "2, 14");

		JPanel pnFilter = new JPanel();
		JScrollPane spFilter = new JScrollPane(pnFilter);
		pnFilter.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"), }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblMinimumLength = new JLabel("Minimum Bytes");
		pnFilter.add(lblMinimumLength, "2, 2, left, default");

		txtMinBytes = new JTextField();
		txtMinBytes.setText("4");
		pnFilter.add(txtMinBytes, "4, 2, fill, default");
		txtMinBytes.setColumns(10);

		JLabel lblMinimumValid = new JLabel("Minimum % Valid");
		pnFilter.add(lblMinimumValid, "2, 4, left, default");

		txtMinValid = new JTextField();
		txtMinValid.setText("90");
		pnFilter.add(txtMinValid, "4, 4, fill, default");
		txtMinValid.setColumns(10);
		tabbedPane.addTab("Filter", null, spFilter, null);

		JPanel pnBlocks = new JPanel();
		JScrollPane spBlocks = new JScrollPane(pnBlocks);
		tabbedPane.addTab("Allowed Blocks", null, spBlocks, null);
		pnBlocks.setLayout(new FormLayout(
				new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		cbControls = new JCheckBox("Textual Control Characters");
		pnBlocks.add(cbControls, "2, 2");

		cbBasicLatin = new JCheckBox("Basic Latin");
		cbBasicLatin.setSelected(true);
		pnBlocks.add(cbBasicLatin, "2, 4");

		cbExtendedLatin = new JCheckBox("Extended Latin");
		pnBlocks.add(cbExtendedLatin, "2, 6");

		cbCJK = new JCheckBox("CJK");
		cbCJK.setSelected(true);
		pnBlocks.add(cbCJK, "2, 8");
	}

}
