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

public class PreferencesWindow extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public PreferencesWindow() {
		setTitle("Preferences");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		pnEncodings.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
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
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JCheckBox cbASCII = new JCheckBox("ASCII");
		pnEncodings.add(cbASCII, "2, 2");
		
		JCheckBox cbUTF8 = new JCheckBox("UTF-8");
		pnEncodings.add(cbUTF8, "2, 4");
		
		JCheckBox cbShiftJIS = new JCheckBox("Shift JIS");
		cbShiftJIS.setSelected(true);
		pnEncodings.add(cbShiftJIS, "2, 6");
		
		JCheckBox cbUCS2LE = new JCheckBox("UCS-2 (LE)");
		cbUCS2LE.setSelected(true);
		pnEncodings.add(cbUCS2LE, "2, 8");
		
		JCheckBox cbUCS2BE = new JCheckBox("UCS-2 (BE)");
		pnEncodings.add(cbUCS2BE, "2, 10");
		
		JCheckBox cbUTF16LE = new JCheckBox("UTF-16 (LE)");
		pnEncodings.add(cbUTF16LE, "2, 12");
		
		JCheckBox cbUTF16BE = new JCheckBox("UTF-16 (BE)");
		pnEncodings.add(cbUTF16BE, "2, 14");
		
		JPanel pnFilter1 = new JPanel();
		JScrollPane spFilter1 = new JScrollPane(pnFilter1);
		tabbedPane.addTab("Filter 1", null, spFilter1, null);
		pnFilter1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JCheckBox cbControls = new JCheckBox("Textual Control Characters");
		pnFilter1.add(cbControls, "2, 2");
		
		JCheckBox cbBasicLatin = new JCheckBox("Basic Latin");
		cbBasicLatin.setSelected(true);
		pnFilter1.add(cbBasicLatin, "2, 4");
		
		JCheckBox cbExtendedLatin = new JCheckBox("Extended Latin");
		pnFilter1.add(cbExtendedLatin, "2, 6");
		
		JCheckBox cbCJK = new JCheckBox("CJK");
		cbCJK.setSelected(true);
		pnFilter1.add(cbCJK, "2, 8");
		
		JPanel pnFilter2 = new JPanel();
		JScrollPane spFilter2 = new JScrollPane(pnFilter2);
		tabbedPane.addTab("Filter 2", null, spFilter2, null);
	}

}
