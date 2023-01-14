
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

//Interface graphique
@SuppressWarnings("serial")
public class LabyrintheUI extends JFrame {
	private JPanel panel;
	private LabyrinthePanel lab;
	private JMenuBar menuBar;
	private JMenu menu;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			LabyrintheUI labyrinthe = new LabyrintheUI();
			labyrinthe.setVisible(true);
			}
		});
	}

	public LabyrintheUI() {
		setSize(650, 700);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Labyrinthe");
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(panel);
		panel.setLayout(new BorderLayout(0, 0));
		// Menu 
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		// Onglet 
		menu = new JMenu("Menu");
		menuBar.add(menu);
		
		//Item nouveau labyrinthe, lance la génération d'un nouveau labyrinthe qui sera affiché sur le panel à la place de l'ancien
		JMenuItem nouveauLabyrinthe = new JMenuItem(new AbstractAction("Nouveau labyrinthe") {
			@Override
			public void actionPerformed(ActionEvent e) {
				lab.creer(lab.cellules);
				repaint();
			}
		});

		//Item correspondant à la résolution du labyrinthe 
		JMenuItem resoudre = new JMenuItem(new AbstractAction("Résoudre") {
			@Override
			public void actionPerformed(ActionEvent e) {
				lab.debut.x=1;
				lab.debut.y=0;
				lab.resoudre((Graphics2D) lab.getGraphics());
			}
		});
		
		
		//Item correspondant à la sauvegarde du labyrinthe non résolu sous forme de PNG 
		JMenuItem exporter1 = new JMenuItem(new AbstractAction("Exporter le labyrinthe (PNG)") {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Ce parcours sert à rendre le labyrinthe vierge avant l'export, dans le cas où l'utilisateur a préalablement utilisé la fonction de résolution et donc coloré les bonnes cases en vert.
				for (int i = 1; i <= lab.lab.taille; i++) {
					for (int j = 1; j <= lab.lab.taille; j++) {
						//Les cases "2", donc coloriées en vert lors du paint(), redeviennent donc des cases "0", coloriées en blanc lors du paint()
						if (lab.cases[i][j] == 2) {
							lab.cases[i][j] = 0;
						}
					}
				}
				lab.paint(lab.getGraphics());
				lab.exporter(false);
			}
		});

		//Item correspondant à la sauvegarde du labyrinthe résolu sous forme de PNG 
		JMenuItem exporter2 = new JMenuItem(new AbstractAction("Exporter la solution (PNG)") {
			@Override
			public void actionPerformed(ActionEvent e) {
				lab.resoudre((Graphics2D) lab.getGraphics());
				lab.paint(lab.getGraphics());
				lab.exporter(true);
			}
		});
		
		//Ici 41 correspond à la largeur et la longueur souhaitée du labyrinthe+1, celui-ci est donc obligatoirement un carré
		lab = new LabyrinthePanel(41);
		panel.add(lab);
		menu.add(nouveauLabyrinthe);
		menu.add(resoudre);
		menu.add(exporter1);
		menu.add(exporter2);

	}
	
}
