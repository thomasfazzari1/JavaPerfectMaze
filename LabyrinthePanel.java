
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

//Panel contenant le quadrillage representant le labyrinthe, qui sera affiche sur l'user interface
@SuppressWarnings("serial")
class LabyrinthePanel extends JPanel implements KeyListener {
	
	private Rectangle2D[][] cases2d;
	int[][] cases;
	int cellules;
	float tailleCase2d;
	Position debut,fin,current;
	public Labyrinthe lab;

	public LabyrinthePanel(int cellules) {
		addKeyListener(this);
		creer(cellules);
	}

	public void creer(int c) {
		cellules = c;
		setSize(600, 600);
		//On definit la taille que de devra avoir une cellule sur le quadrillage
		tailleCase2d = (float) (1.0 * 600 / cellules);	
		setFocusable(true);
		lab = new Labyrinthe(cellules - 2);
		cases = lab.cases;
		//On definit les positions des cases de depart (en haut à gauche) et fin (en bas à droite)
		debut =  new Position(1, 0);
		fin = new Position(cellules - 2, cellules - 1);
		//current sert au deplacement (fonctionnalite bonus)
		current =  debut;
		//Transposition du labyrinthe en un ensemble de cases (des Rectangle2D) sur le panel
		//Celles-ci seront plus tard coloriees avec la methode fill presente dans la librairie graphique java selon leur type de case
		cases2d = new Rectangle2D[cellules][cellules];
		for (int i = 0; i < cellules; i++) {
			for (int j = 0; j < cellules; j++) {
				cases2d[i][j] = new Rectangle2D.Double(j * tailleCase2d, i * tailleCase2d, tailleCase2d, tailleCase2d);
			}
		}
	}
	
	private void afficher(Graphics2D g2d) {
		//On represente une case accessible en blanc et un mur en noir
		//Les cases en vert representent le chemin correct, le second if() de cette methode sert uniquement dans le cas de l'exportation en PNG de la solution
		for (int i = 0; i < cellules; i++) {
			for (int j = 0; j < cellules; j++) {
				if (cases[i][j] == 0) {
					g2d.setColor(Color.WHITE);
				}else {
					if(cases[i][j] == 2) {
						g2d.setColor(Color.BLUE);
					}else {
						g2d.setColor(Color.BLACK);
					}

				}
				g2d.fill(cases2d[i][j]);
			}
		}
		//La case de depart est toujours automatiquement coloriee en vert
		g2d.setColor(Color.BLUE);
		g2d.fill(cases2d[debut.x][debut.y]);
	}
	
	public void resoudre(Graphics2D g2d) {
		//On recupère la suite de cases menant du depart à la fin dans une pile
		Stack<Position> cheminCorrect = lab.nettoyerSolution(debut, fin);
		//On colore ensuite toutes les cases presentes dans la pile à partir de leur Rectangle2D correspondant sur le panel, affichant ainsi le chemin correct en vert
		g2d.setColor(Color.BLUE);
		while (!cheminCorrect.empty()) {
			Position next = cheminCorrect.pop();
			g2d.fill(cases2d[next.x][next.y]);
			cases[next.x][next.y]= 2;
		}	
	}
	
	//Cree une image à partir du graphique2d contenu dans le LabyrinthePanel de l'interface utilisateur 
	//le booleen sert à reconnaitre si l'on vient de la fonction permettant l'export de la solution ou celle permettant l'export du labyrinthe vierge, ainsi, on affecte le bon nom de fichier à l'image de sortie 
	public void exporter(boolean solution) {		
		BufferedImage bi = new BufferedImage(this.getSize().width, this.getSize().height, BufferedImage.TYPE_INT_ARGB); 
		Graphics2D g = bi.createGraphics();
		this.paint(g); 
		g.dispose();
		if(solution == true) {
			try{
				ImageIO.write(bi,"png",new File("labyrintheResolu.png"));
			}catch (Exception e) {
				System.out.println("Erreur lors de l'export du labyrinthe.");
			}	
		}else {
			try{
				ImageIO.write(bi,"png",new File("labyrintheVierge.png"));
			}catch (Exception e) {
				System.out.println("Erreur lors de l'export de la solution.");
			}	
		}
		
	}
	
	
	//fonctionalite bonus, permet à l'utilisateur de se deplacer dans le labyrinthe avec les flêches directionnelles
	private void deplacement(Graphics2D g2d, Position current) {
		int x = current.x;
		int y = current.y;
		update((Graphics2D) this.getGraphics());
		g2d.setColor(Color.RED);
		g2d.fill(cases2d[x][y]);
		if(current.equals(fin)) {
			JOptionPane.showMessageDialog(getParent(), "Labyrinthe resolu", "Bravo", JOptionPane.PLAIN_MESSAGE);
			creer(this.cellules);
			repaint();
		}
	}

	//Selon la direction souhaitee par l'utilisateur, on verifie si le deplacement est possible, si oui on declenche la fonction
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (lab.moveHaut(current)) {
				current.x = current.x - 1;
				deplacement((Graphics2D) getGraphics(), current);
			}		
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (lab.moveDroite(current)) {
				current.y = current.y + 1;
				deplacement((Graphics2D) getGraphics(), current);
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (lab.moveBas(current)) {
				current.x = current.x + 1;
				deplacement((Graphics2D) getGraphics(), current);
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (lab.moveGauche(current)) {
				current.y = current.y- 1;
				deplacement((Graphics2D) getGraphics(), current);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		afficher((Graphics2D) g);
	}
	
	
}