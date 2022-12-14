package game.main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import ui.MPanel;
import ui.TButton;

public class GameAbout extends MPanel {

	/**
		 * 
		 */
	private static final long serialVersionUID = 7944595523751870996L;

	public GameAbout(String t, boolean isServer, int port) {
		super(t);

		setLayout(null);
		setPreferredSize(new Dimension(450, 420));
		String text = "";
		if (isServer) {
			text = getText("", port);
		} else {
			text = "<html><p align='justify'>Połączenie z serwerem udane! Zajmij miejsce, a następnie poczekaj na pozostałych graczy i na start gry. "
					+ "O rozpoczęciu rozgrywki decyduje host. <br><br>W prawym panelu widzisz ustawienia. O ich zmianie również decyduje host."
					+ "</p></html>";
		}
		JLabel about = new JLabel(text);
		about.setVerticalAlignment(SwingConstants.TOP);
		about.setHorizontalAlignment(SwingConstants.CENTER);
		about.setBounds(10, 20, 430, 350);
		about.setFont(new Font("Arial", Font.PLAIN, 15));
		add(about, 0);
		if (isServer) {
			TButton show = new TButton("pobierz IP z whatismyipaddress.com", "yellow", 12, 2);
			show.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent me) {
					if (((TButton) me.getComponent()).releasedInside()) {
						remove(show);
						about.setText(getText("czekaj...", port));
						about.repaint();
						Timer timer = new Timer(1000, new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								about.setText(getText(getPublicIP(), port));
								about.repaint();
							}
						});
						timer.setRepeats(false);
						timer.start();

					}
				}
			});
			show.setBounds(148, 145, show.getPreferredSize().width, show.getPreferredSize().height);
			add(show, 0);
		}
	}

	public String getText(String pubIP, int port) {
		String text = "<html><p align='justify'>Podaj pozostałym graczom odpowiednie dane. Jeśli znajdują się w sieci lokalnej, podaj lokalny adres ip "
				+ "lub nazwę komputera w sieci oraz port. Jeśli będą się łączyć przez internet, podaj im publiczny adres IP oraz port. Pamiętaj o "
				+ "przekierowaniu portu na routerze i ewentualnym utworzeniu odpowiedniej reguły w firewallu.</p><br>"
				+ "<p align='left'><b>Publiczny adres IP:</b> " + pubIP + "<br>" + "<b>Lokalny adres IP:</b> "
				+ getLocalIP() + "<br>" + "<b>Nazwa komputera w sieci lokalnej:</b> " + getLocalName() + "<br>"
				+ "<b>Port:</b> " + port + "<br><br>"

				+ "</p>" + "<p align='justify'>Jako host gry możesz:<br><br>"
				+ "- zmieniać ustawienia gry w prawym panelu. Inni gracze również będą je widzieć, ale nie mogą ich modyfikować.<br><br>"
				+ "- wyrzucić ewentualnego niechcianego gracza poprzez kliknięcie znaku '-' po najechaniu na jego nick.</p></html>";
		return text;
	}

	public String getLocalIP() {
		InetAddress localhost;
		try {
			localhost = InetAddress.getLocalHost();
			return (localhost.getHostAddress()).trim();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			return "Błąd";
		}

	}

	public String getLocalName() {
		InetAddress localhost;
		try {
			localhost = InetAddress.getLocalHost();
			return (localhost.getHostName()).trim();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			return "Błąd";
		}
	}

	public String getPublicIP() {
		String systemipaddress = "";
		try {
			URL url_name = new URL("http://bot.whatismyipaddress.com");
			BufferedReader sc = new BufferedReader(new InputStreamReader(url_name.openStream()));
			systemipaddress = sc.readLine().trim();
		} catch (Exception e) {
			systemipaddress = "error";
		}

		return systemipaddress;
	}
}
