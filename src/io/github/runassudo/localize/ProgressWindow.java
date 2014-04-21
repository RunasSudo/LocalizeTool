package io.github.runassudo.localize;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import javax.swing.JLabel;

public class ProgressWindow extends JFrame {

	private JPanel contentPane;
	public JLabel label;
	public JProgressBar progressBar;

	/**
	 * Create the frame.
	 */
	public ProgressWindow() {
		setTitle("Status");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 74);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		progressBar = new JProgressBar();
		contentPane.add(progressBar, BorderLayout.CENTER);

		label = new JLabel("Status");
		contentPane.add(label, BorderLayout.NORTH);

		setVisible(true);
	}

	public JLabel getLabel() {
		return label;
	}

	public JProgressBar getBar() {
		return progressBar;
	}

}
