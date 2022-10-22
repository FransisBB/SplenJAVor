package ui;

import java.awt.Color;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class NButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8680482530193999150L;

	public NButton() {
		create();
	}

	public NButton(String s) {
		this.setText(s);
		create();
	}

	public NButton(ImageIcon img) {
		this.setIcon(img);
	}

	public void create() {
		this.setFocusPainted(false);

		ResourceBundle txt = ResourceBundle.getBundle("Labels");
		try {
			if (is(txt.getString("cancel")) || is(txt.getString("pass"))) {
				setBackground(new Color(255, 210, 100));
			} else if (is(txt.getString("close"))) {
				setBackground(new Color(207, 56, 71));
			} else
				setBackground(new Color(145, 255, 144));
		} catch (Exception e) {
			setBackground(new Color(145, 255, 144));
		}
	}

	public boolean is(String s) {
		return this.getText().equals(s);

	}

}
