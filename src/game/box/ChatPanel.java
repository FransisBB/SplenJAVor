package game.box;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

import game.main.Game;
import game.main.StringUtils;

public class ChatPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1172515722533152110L;
	// JLabel chat;
	JScrollPane sp;
	JTextPane ta;
	JTextField tf;
	JButton btn;
	String text;
	Boolean inGame;
	public String nick;

	public ChatPanel(Game game, String n, boolean inGame) {
		setNick(n);
		setLayout(null);
		setOpaque(false);
		setPreferredSize(new Dimension(1,115));
		this.inGame = inGame;
		text = new String();
		ta = new JTextPane();
		ta.setContentType("text/html");
		text="Witaj na czacie! Teraz œmia³o mo¿esz komunikowaæ siê z innymi;)";
		ta.setText("<font style='font-family:Arial;font-size:11px;'>" + text + "</font>");
		
		tf = new JTextField();
		btn = new JButton();
		btn.setIcon(getIcon("images/enter.png"));
		btn.setBackground(new Color(150, 200, 150));
		ta.setOpaque(false);
		ta.setEditable(false);
		sp = new JScrollPane(ta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//JViewport viewport = new JViewport();
		//viewport.setView(ta);

		//viewport.setOpaque(false);
		sp.getViewport().setOpaque(false);
		tf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (tf.getText() != null && !tf.getText().isEmpty()) {
						String texta = "<br><b>" + nick + "</b>" + ": " + StringUtils.encodeHtml(tf.getText());
						addLine(texta);
						game.sendAction("CHAT" + texta);
						tf.setText("");
					}
				}
			}

		});
		btn.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				if (tf.getText() != null && !tf.getText().isEmpty()) {
					String texta = "<br><b>" + nick + "</b>" + ": " + StringUtils.encodeHtml(tf.getText());
					addLine(texta);
					game.sendAction("CHAT" + texta);
					tf.setText("");
				}
			}

		});
		//sp.setViewport(viewport);
		sp.setOpaque(false);
		sp.setBorder(null);
		sp.getVerticalScrollBar().setOpaque(false);
		sp.getVerticalScrollBar().setUI(new MyScrollBarUI());
		// sp.scroll
		add(sp);
		add(tf);
		add(btn);

	}
	public void setNick (String n) {
		nick=StringUtils.encodeHtml(n);
	}
	public String editNick (String n) {
		if (!nick.equals(StringUtils.encodeHtml(n))) {
		String s="<br><i><font color=red>"+nick+" -&gt; ";
		setNick(n);
		s+=nick+"</font></i>";
		addLine(s);
		return "CHAT"+s;
		} else return "";
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int posx;
		int posy;
		int r_padding;
		int b_padding;
		int width;
		int height;
		if (inGame) {
			posx = 10;
			posy = 1;
			r_padding = 1;
			b_padding = 5;
		} else {
			posx = 0;
			posy = 0;
			r_padding = 0;
			b_padding = 0;
		}
		width = getWidth() - posx - r_padding;
		height = getHeight() - posy - b_padding;
		sp.setBounds(posx, posy, width, height - 22);
		sp.getVerticalScrollBar().setPreferredSize(new Dimension(15, sp.getHeight()));
		tf.setBounds(posx, posy + sp.getHeight() + 2, sp.getWidth() - 30, 20);
		btn.setBounds(posx + tf.getWidth(), posy + sp.getHeight() + 2, 29, 19);
		if (inGame) {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(new Color(0, 0, 0, 30));
			g2d.fillRoundRect(1, 1, getWidth() + 30, getHeight() - 3, 30, 30);
			g2d.setColor(new Color(255, 255, 240, 180));
			g2d.fillRoundRect(0, 0, getWidth() + 30, getHeight() - 3, 30, 30);
			g2d.dispose();
		}

	}

	public void addLine(String s) {
		text += s;
		ta.setText("<font style='font-family:Arial;font-size:11px;'>" + text + "</font>");
		scrollToBottom(sp);
		}

	public ImageIcon getIcon(String img) {
		URL imageURL = getClass().getClassLoader().getResource(img);
		return new ImageIcon(imageURL);
	}

	private void scrollToBottom(JScrollPane scrollPane) {
		JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
		AdjustmentListener downScroller = new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				Adjustable adjustable = e.getAdjustable();
				adjustable.setValue(adjustable.getMaximum());
				verticalBar.removeAdjustmentListener(this);
			}
		};
		verticalBar.addAdjustmentListener(downScroller);
	}
}

class MyScrollBarUI extends BasicScrollBarUI {
	private final Dimension d = new Dimension();

	@Override
	protected JButton createDecreaseButton(int orientation) {
		return new JButton() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 6861381220814608149L;

			@Override
			public Dimension getPreferredSize() {
				return d;
			}
		};
	}

	@Override
	protected JButton createIncreaseButton(int orientation) {
		return new JButton() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 8261637352214903011L;

			@Override
			public Dimension getPreferredSize() {
				return d;
			}
		};
	}

	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(new Color(255, 255, 255, 150));
		g2.fillRoundRect(r.x, r.y, r.width - 1, r.height - 1, 10, 10);
		g2.setPaint(new Color(0, 0, 0, 50));
		g2.drawRoundRect(r.x, r.y, r.width - 1, r.height - 1, 10, 10);
	}

	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color color = null;
		JScrollBar sb = (JScrollBar) c;
		if (!sb.isEnabled()) {
			return;
		} else if (isDragging) {
			color = Color.LIGHT_GRAY;
		} else if (isThumbRollover()) {
			color = Color.GRAY;
		} else {
			color = Color.DARK_GRAY;
		}
		g2.setPaint(color);
		g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
		g2.dispose();
	}

	@Override
	protected void setThumbBounds(int x, int y, int width, int height) {
		super.setThumbBounds(x, y, width, height);
		scrollbar.repaint();
	}
}
