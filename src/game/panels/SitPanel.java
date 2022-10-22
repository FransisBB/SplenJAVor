package game.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import game.main.Board;
import game.main.Utils;
import game.obj.Chair;
import ui.TButton;

public class SitPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2633538609067144658L;
	JTextField inputNick = new JTextField(13);
	JScrollPane jf;
	ComponentAdapter ca;
	int gender;
	Chair chair;

	public SitPanel(JScrollPane jf, Chair chair, boolean edit) {
		this.chair = chair;
		this.ca = new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				update();
			}
		};
		this.jf = jf;
		setLayout(new GridBagLayout());
		setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.ipadx = 2;
		gbc.ipady = 2;
		gbc.insets = new Insets(2, 2, 2, 6);

		gender = chair.gender;
		inputNick.setHorizontalAlignment(JTextField.CENTER);
		inputNick.setFont(new Font("Arial", Font.BOLD, 16));
		inputNick.setMargin(new Insets(2, 2, 2, 2));
		inputNick.setText(chair.nick);
		gbc.gridwidth = 2;
		gbc.gridy = 0;
		gbc.gridx = 0;
		add(new JLabel("Podaj p³eæ i nick:"), gbc);
		gbc.gridwidth = 1;
		gbc.gridy = 1;
		gbc.gridx = 1;
		add(inputNick, gbc);
		gbc.insets = new Insets(2, 2, 2, 2);
		JLabel genderLab1 = genderIcon(1);
		JLabel genderLab2 = genderIcon(2);
		if (gender == 1) {
			genderLab1.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
			genderLab1.setBackground(new Color(200, 200, 255));
			genderLab2.setBackground(new Color(255, 200, 255, 50));
			genderLab2.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0, 75), 1));
		} else {
			genderLab1.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 255, 75), 1));
			genderLab1.setBackground(new Color(200, 200, 255, 50));
			genderLab2.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
			genderLab2.setBackground(new Color(255, 200, 255));
		}

		genderLab1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				genderLab2.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0, 75), 1));
				genderLab2.setBackground(new Color(255, 200, 255, 50));
				genderLab1.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
				genderLab1.setBackground(new Color(200, 200, 255));
				gender = 1;
				repaint();
				revalidate();
			}
		});
		genderLab2.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				genderLab1.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 255, 75), 1));
				genderLab1.setBackground(new Color(200, 200, 255, 50));
				genderLab2.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
				genderLab2.setBackground(new Color(255, 200, 255));
				gender = 2;
				repaint();
				revalidate();
			}
		});
		gbc.gridx = 0;
		JPanel temp = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
		temp.setOpaque(false);
		temp.add(genderLab1);
		temp.add(genderLab2);
		add(temp, gbc);

		TButton confirm = new TButton(edit ? "Zmieñ" : "Usi¹dŸ", "green", 13);
		confirm.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					clear();
					if (edit) {
						chair.editNickFinish(inputNick.getText(), gender);
					} else {
						chair.sitDownAcceptedFinish(inputNick.getText(), gender);
					}

				}
			}
		});
		TButton cancel = new TButton("Anuluj", "yellow", 13);
		cancel.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (((TButton) me.getComponent()).releasedInside()) {
					clear();
					if (!edit) {
						chair.standUp();
					}

				}
			}
		});

		JPanel tempB = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
		tempB.setOpaque(false);
		tempB.add(confirm);
		tempB.add(cancel);
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.insets = new Insets(2, 2, 6, 6);
		add(tempB, gbc);

	}

	public void update() {
		chair.hideIcons();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				int widthp = getPreferredSize().width;
				int heightp = getPreferredSize().height;
				Point point = new Point(chair.getLocationOnScreen().x - jf.getLocationOnScreen().x,
						chair.getLocationOnScreen().y - jf.getLocationOnScreen().y);
				int x = 2 + point.x + (chair.getWidth() - widthp) / 2;
				int y = point.y + (70 - heightp) / 2;
				setBounds(x, y, widthp, heightp);
				repaint();
				revalidate();
			}
		});
	}

	public void showThis() {
		jf.add(this, 0);
		jf.addComponentListener(ca);
		chair.hideIcons();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				int widthp = getPreferredSize().width;
				int heightp = getPreferredSize().height;
				Point point = new Point(chair.getLocationOnScreen().x - jf.getLocationOnScreen().x,
						chair.getLocationOnScreen().y - jf.getLocationOnScreen().y);
				int x = 2 + point.x + (chair.getWidth() - widthp) / 2;
				int y = point.y + (70 - heightp) / 2;
				setBounds(x, y, widthp, heightp);
				repaint();
				revalidate();
				inputNick.requestFocus();
			}
		});
	}

	public void clear() {
		jf.removeComponentListener(ca);
		jf.remove(this);
		jf.repaint();
		jf.revalidate();
		chair.sp = null;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				chair.showIcons();
			}
		});
	}

	public JLabel genderIcon(int g) {
		JLabel gl = new JLabel(Utils.scaleIcon(getIcon("images/gender" + g + ".png"), 30, 30));
		gl.setPreferredSize(new Dimension(30, 30));
		gl.setOpaque(false);
		return gl;
	}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(new Color(0, 0, 0, 50));
		g2.fillRoundRect(2, 2, this.getWidth() - 3, this.getHeight() - 3, 10, 10);
		g2.setColor(new Color(255, 255, 245));
		g2.fillRoundRect(1, 1, this.getWidth() - 4, this.getHeight() - 4, 10, 10);
		g2.setColor(new Color(50, 50, 0, 150));
		g2.drawRoundRect(1, 1, this.getWidth() - 4, this.getHeight() - 4, 10, 10);
	}

}
