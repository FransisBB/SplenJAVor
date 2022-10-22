package game.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.obj.Chair;
import ui.MPanel;

public class GameSettings extends MPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8603249141333696943L;

	public GameSettings (String t, Game game) {
		super(t);
		setPreferredSize(new Dimension(450, 250));
		setLayout(new BorderLayout());
		
		Chair[] chairs = game.chairs;
		JCheckBox [] options = game.options;
		Boolean isServer = game.isServer;
		
		
		JPanel settings = new JPanel();
		settings.setLayout(new GridBagLayout());
		GridBagConstraints gbcs = new GridBagConstraints();
		gbcs.gridy = 0;
		gbcs.gridx = 0;
		gbcs.insets = new Insets(0, 0, 5, 5);
		gbcs.anchor = GridBagConstraints.WEST;
		for (int i = 0; i < 9; i++) {
			int labelWidth = 300;
			gbcs.gridy = i;
			int j = i;
			if (isServer) {
				options[i].setEnabled(true);
				options[i].addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent ie) {
						game.sendAction("OPTION" + j + ie.getStateChange());
					}
				});

			}

			gbcs.gridwidth = 1;
			gbcs.gridheight = 1;
			gbcs.weightx = 0;
			gbcs.gridx = 0;

			if (i == 6) {
				gbcs.gridy = 4;
				gbcs.gridx = 2;
				gbcs.gridheight = 2;
			}
			if (i == 8) {
				gbcs.gridy = 7;
				gbcs.gridx = 2;
			}
			settings.add(options[i], gbcs);

			gbcs.weightx = 1;
			gbcs.gridx = 1;
			gbcs.gridwidth = 3;

			if (i > 3) {
				gbcs.gridwidth = 1;
				gbcs.weightx = 0;
				labelWidth = 100;
			}
			if (i == 6) {
				gbcs.weightx = 1;
				gbcs.gridy = 4;
				gbcs.gridheight = 2;
				gbcs.gridx = 3;
				labelWidth = 180;
			}
			if (i == 8) {
				gbcs.weightx = 1;
				gbcs.gridy = 7;
				gbcs.gridheight = 1;
				gbcs.gridx = 3;
				labelWidth = 180;
			}
			String labelText = String.format("<html><div style=\"width:%dpx;\">%s</div></html>", labelWidth,
					game.lt("option" + i));
			JLabel optionText = new JLabel(labelText);
			optionText.setFont(new Font("Arial", Font.PLAIN, 15));
			settings.add(optionText, gbcs);

		}
		if (isServer) {
			options[8].setEnabled(false);
		}
		options[7].addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getStateChange() == 1) {
					if (isServer) {
						options[8].setEnabled(true);
						options[8].setSelected(true);
					}
					game.table.add(chairs[4]);
					game.table.repaint();
					game.table.revalidate();

				}
				if (ie.getStateChange() == 2) {
					if (isServer) {
						game.server.resetSeat(4);
						options[8].setEnabled(false);
						options[8].setSelected(false);
					}
					game.table.remove(chairs[4]);
					game.table.repaint();
					game.table.revalidate();
				}
				for (int i = 0; i < game.maxPlayers; i++) {
					if (chairs[i].sp != null)
						chairs[i].sp.update();
				}

			}
		});
		settings.setOpaque(false);
		add(settings, BorderLayout.NORTH);
	}
	
}
