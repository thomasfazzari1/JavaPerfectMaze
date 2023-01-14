
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;


//Classe contenant la generation du labyrinthe ainsi que les elements permettant sa resolution
public class Labyrinthe {
	
	int taille;
	Random random;
	int[][] cases;
	boolean[][] parcouru1,parcouru2;
	int accessibles = 0;

	public Labyrinthe(int taille) {

		this.taille = taille;
		random = new Random();
		cases = new int[taille + 2][taille + 2];
		parcouru1 = new boolean[taille + 2][taille + 2];
		parcouru2 = new boolean[taille + 2][taille + 2];
		generer();
	}

	//Indique s'il existe une case au dessus de celle passée en paramêtre
	private boolean haut(Position position) {
		return position.x - 2 > 0;
	}

	//Idem à droite
	private boolean droite(Position position) {
		return position.y + 2 <= taille;
	}

	//En bas
	private boolean bas(Position position) {
		return position.x + 2 <= taille;
	}

	//À gauche
	private boolean gauche(Position position) {
		return position.y - 2 > 0;
	}

	//Renvoie une case aleatoire situee autour de la case passee en paramètre
	private Position randNext(Position current) {
		ArrayList<Integer> nexts = new ArrayList<Integer>();
		if (haut(current))
			nexts.add(0);
		if (droite(current))
			nexts.add(1);
		if (bas(current))
			nexts.add(2);
		if (gauche(current))
			nexts.add(3);

		int value = nexts.get(random.nextInt(nexts.size()));
		
		switch (value) {
		case 0: {
			return new Position(current.x - 2, current.y);
		}
		case 1: {
			return new Position(current.x, current.y + 2);
		}
		case 2: {

			return new Position(current.x + 2, current.y);
		}
		case 3: {
			return new Position(current.x, current.y - 2);
		}
		default:
			return new Position(current.x - 2, current.y);
		}
	}

	private void genererChemin(Position current, Position next) {
		int x = (current.x + next.x) / 2;
		int y = (current.y + next.y) / 2;
		cases[x][y] = 0;
	}
	
	//Indique si l'on peut rejoindre la case du dessus
	public boolean moveHaut(Position position) {
		return position.x - 1 > 0 && cases[position.x - 1][position.y] == 0;
	}
	//Idem pour la case de droite
	public boolean moveDroite(Position position) {
		return position.y + 1 <= taille + 1 && cases[position.x][position.y + 1] == 0;
	}
	//Case du dessous
	public boolean moveBas(Position position) {
		return position.x + 1 <= taille && cases[position.x + 1][position.y] == 0;
	}
	//Case de gauche
	public boolean moveGauche(Position position) {
		return position.y - 1 > 0 && cases[position.x][position.y - 1] == 0;
	}

	public void generer() {
		//Exploration exhaustive
		//On entoure le labyrinthe par des murs
		for (int i = 0; i < taille + 2; i++) {
			cases[0][i] = 1;
			cases[taille + 1][i] = 1;
			cases[i][0] = 1;
			cases[i][taille + 1] = 1;
		}
		//Interieur du labyrinthe
		for (int i = 1; i <= taille; i++) {
			for (int j = 1; j <= taille; j++) {
				if (i % 2 == 1 && j % 2 == 1) {
					cases[i][j] = 0;
					parcouru1[i][j] = false;
					accessibles++;
					parcouru2[i][j] = false;
				} else {
					cases[i][j] = 1;
				}
			}
		}
		//On rfin les cases de depart et de fin accesibles
		cases[1][0] = 0;
		cases[taille][taille + 1] = 0;
		
		//Generation du chemin
		//On genère la premiere case accessible depuis la case depart, qui est forcement la case de coordonnees (1,1)
		int x = 1, y = 1;
		Position caseCourante = new Position(x, y);
		int casesParcourues = 1;
		parcouru1[x][y] = true;
		while (casesParcourues < accessibles) {
			Position next = randNext(caseCourante);
			int xNext = next.x;
			int yNext = next.y;
			if (parcouru1[xNext][yNext] == false) {
				parcouru1[xNext][yNext] = true;
				casesParcourues++;
				genererChemin(caseCourante, next);
			}
			//La case parcourue devient la case courante
			caseCourante.x = next.x;
			caseCourante.y = next.y;
		}
	}

	//Indique si l'on peut, à partir d'une case passee en paramètre, passer à une autre case passee en paramètre
	private boolean rejoindre(Position current, Position next) {
		ArrayList<Position> nexts = new ArrayList<Position>();
		if (moveHaut(current))
			nexts.add(new Position(current.x - 1, current.y));
		if (moveDroite(current))
			nexts.add(new Position(current.x, current.y + 1));
		if (moveBas(current))
			nexts.add(new Position(current.x + 1, current.y));
		if (moveGauche(current))
			nexts.add(new Position(current.x, current.y - 1));
		return nexts.contains(next);
	}

	private void cheminParcouru() {
		for (int i = 0; i < taille + 2; i++) {
			for (int j = 0; j < taille + 2; j++) {
				parcouru2[i][j] = false;
			}
		}
	}

	//Parcours en profondeur
	public Stack<Position> parcours(Position debut, Position fin) {		
		Stack<Position> parcouru = new Stack<Position>();
		Stack<Position> possibilites = new Stack<Position>();
		Position current = debut;
		parcouru2[1][0] = true;	
		parcouru.push(new Position(debut.x, debut.y));
		while (!current.equals(fin)) {
			//On regarde chaque possibilite de parcours à partir de la case courante
			if (moveHaut(current) && parcouru2[current.x - 1][current.y] == false)
				possibilites.push(new Position(current.x - 1, current.y));
			if (moveGauche(current) && parcouru2[current.x][current.y - 1] == false)
				possibilites.push(new Position(current.x, current.y - 1));
			if (moveDroite(current) && parcouru2[current.x][current.y + 1] == false)
				possibilites.push(new Position(current.x, current.y + 1));
			if (moveBas(current) && parcouru2[current.x + 1][current.y] == false)
				possibilites.push(new Position(current.x + 1, current.y));
			//On va à la dernière possibilite trouvee, qui devient dont la nouvelle case courante
			if(!possibilites.empty()) {
				current = possibilites.pop();
			}
			parcouru.push(new Position(current.x, current.y));
			parcouru2[current.x][current.y] = true;
		}
		//On enregistre le chemin effectue
		Stack<Position> chemin = new Stack<Position>();
		while (!parcouru.empty()) {
			chemin.push(parcouru.pop());
		}
		cheminParcouru();
		return chemin;
	}

	//Nettoie le stack renvoye par la methode parcours : les cases menant à des culs-de-sac qu'il contient à cause du parcours en profondeur sont ejectees du stack pour ne garder que le chemin menant à la fin
	public Stack<Position> nettoyerSolution(Position debut, Position fin) {
		Stack<Position> parcouru = parcours(debut, fin);
		Stack<Position> parcoursInverse = new Stack<Position>();
		while (!parcouru.empty()) {
			parcoursInverse.push(parcouru.pop());
		}
		parcouru.push(new Position(fin.x, fin.y));
		parcoursInverse.pop();
		Position current = fin;
		while (!parcoursInverse.empty()) {
			current = parcoursInverse.pop();
			Position next = parcouru.peek();
			if (rejoindre(current, next)) {
				parcouru.push(new Position(current.x, current.y));
			}
		}
		return parcouru;
	}
	

}