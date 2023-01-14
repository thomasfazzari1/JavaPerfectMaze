
public class Position {
	
	//Coordonnées de la case sur le quadrillage
	int x;
	int y;
	
	Position(int x, int y){
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		Position p2 = (Position) obj;
		return x == p2.x && y == p2.y;
	}

}
