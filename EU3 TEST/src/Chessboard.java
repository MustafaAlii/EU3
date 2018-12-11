
public class Chessboard {
	public static class Field {
		private char row;
		private byte column;
		private Chesspiece piece = null;
		private boolean marked = false;

		public Field(char row, byte column) // skapar ett spelfält som man kan sätta pjäserna på
		{
			this.row = row;
			this.column = column;
		}

		public void put(Chesspiece piece) // adds chesspiece to board
		{
			this.piece = piece;
		}

		public Chesspiece take() // removes chesspiece from board
		{
			Chesspiece removePiece = this.piece;
			this.piece = null;
			return removePiece;
		}

		public void mark() { // markerar ett område på spelplan
			this.marked = true;
		}

		public void unmark() { // Avmarkerar ett område på spelplan
			this.marked = false;
		}

		public String toString() { // returnerar markerade områden på plan med "xx" och icke upptagna med "--"
			String mark = (marked) ? "xx" : "--";
			return (piece == null) ? mark : piece.toString();
		}
	}

	public static final int NUMBER_OF_ROWS = 8;
	public static final int NUMBER_OF_COLUMNS = 8;
	public static final int FIRST_ROW = 'a';
	public static final int FIRST_COLUMN = 1;
	private Field[][] fields; // 2 dimensionell array till spelplan

	public Chessboard() {

		fields = new Field[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
		char row = 0;
		byte column = 0; // vi skapar en array och fyller med rätt kolumn och rad värden
		for (int r = 0; r < NUMBER_OF_ROWS; r++) { // number of rows = 8
			row = (char) (FIRST_ROW + r); // fyller radens från char "a" --> "h"
			column = FIRST_COLUMN; // första kolumnen har index 0, tal 1
			for (int c = 0; c < NUMBER_OF_COLUMNS; c++) {
				fields[r][c] = new Field(row, column);
				column++; // skapar ett 8x8 spelplan
			}
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		// skapar ett ändringsbart objekt genom att använda konstruktor, objekt utan
		// tecken

		sb.append("   1   2   3   4   5   6   7   8   \n");
		// vi lägger till denna sträng i vårt objekt

		for (int i = 0; i < NUMBER_OF_ROWS; i++) {
			char row = (char) (FIRST_ROW + i);
			sb.append(row).append("  ");
			for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
				sb.append(fields[i][j].toString()).append("  ");
				// det som finns på index [i][j] ska omvandlas till en sträng
				// det vi skriver ut är spelplan med 1-8 som kolum och a-h som rad
			}
			sb.append("\n");
		}
		return sb.toString(); // returnerar spelplanen
	}

	public boolean isValidField(char row, byte column) { // en pjäs är endast giltig om den finns på spelplanen
		return (row >= 'a' && row <= 'h' && column >= 1 && column <= 8);
	}

	public abstract class Chesspiece {
		private char color;
		// w - white, b - black
		private char name;
		// K - King, Q - Queen, R - Rook, B - Bishop, N - Knight,
		// P – Pawn
		protected char row = 0;
		protected byte column = -1;
		// variabler som är deklarerade som protected i en superklass:
		// åtkomst endast via subklasser i andra paket eller
		// klasser i paketet av den protected variabelns klass.
		// Protected: kan användas i den klass där den deklareras, i alla subklasser
		// till den klassen
		// och i alla klasser i samma paket

		protected Chesspiece(char color, char name) { // detta är en konstruktor för att skapa ett nytt chesspiece
														// objekt!

		}

		public String toString() { // returnerar sträng med chesspiecens färg och namn.
			return "" + color + name;
		}

		public boolean isOnBoard() { // om den finns på fältet returnerar den --> true
			return Chessboard.this.isValidField(row, column);
		}

		public void moveTo(char row, byte column) throws NotValidFieldException
		// vi vill förflytta en schackpjäs till ett bestämt område i spelplanen
		{
			if (!Chessboard.this.isValidField(row, column))
				throw new NotValidFieldException("bad field: " + row + column);

			this.row = row;
			this.column = column;
			int r = row - FIRST_ROW;
			int c = column - FIRST_COLUMN;
			Chessboard.this.fields[r][c].put(this);
		}

		public void moveOut() {
		}

		public abstract void markReachableFields();

		public abstract void unmarkReachableFields();
	}

	public class Pawn extends Chesspiece { // alla extends = ny klass skapad via arv. Chesspiece är superklass.

		public Pawn(char color, char name) { // bonden

			// detta är en konstruktor för objektinstansen av klassen Pawn
			// bondens färg och namn
			super(color, name);
			super.color = color;
			super.name = name;
		}

		public void markReachableFields() {
			byte col = (byte) (column + 1); // vi kan ta ett steg från nuvarande position

			if (Chessboard.this.isValidField(row, col)) {
				int r = row - FIRST_ROW;
				int c = col - FIRST_COLUMN;
				Chessboard.this.fields[r][c].mark(); // markerar det!
			}
		}

		public void unmarkReachableFields() {
			byte col = (byte) (column + 1);

			if (Chessboard.this.isValidField(row, col)) {
				int r = row - FIRST_ROW;
				int c = col - FIRST_COLUMN;
				Chessboard.this.fields[r][c].unmark();// anropar konstruktorn unmark
				// avmarkera de områden denna figur (bonde) inte kan nå
			}
		}
	}

	public class Rook extends Chesspiece { // torn

		public Rook(char color, char name) { // konstruktor för objektinstans av klassen Rook
			super(color, name);
			super.color = color;
			super.name = name;
		}

		public void markReachableFields() { // abstract --> subklass

			if (Chessboard.this.isValidField(row, column)) {
				for (int i = 1; i <= 8; i++) {
					int r = row - FIRST_ROW;
					int c = (byte) (i) - FIRST_COLUMN;
					Chessboard.this.fields[r][c].mark();
					// markerar alla rutor förutom den figuren står på
					// vi markerar kolumnen!
				}

				for (char i = 'a'; i <= 'h'; i++) {
					int r = i - FIRST_ROW;
					int c = column - FIRST_COLUMN;
					Chessboard.this.fields[r][c].mark();
					// vi markerar alla rutor utom den figuren står på
					// vi markerar raden!
				}
			}
		}

		public void unmarkReachableFields() { // abstract --> subklass
			if (Chessboard.this.isValidField(row, column)) {
				for (int i = 1; i <= 8; i++) {
					int r = row - FIRST_ROW;
					int c = (byte) (i) - FIRST_COLUMN;
					Chessboard.this.fields[r][c].unmark();
					// avmarkerar alla kolumnrutor den inte kan nå
				}

				for (char i = 'a'; i <= 'h'; i++) {
					int r = i - FIRST_ROW;
					int c = column - FIRST_COLUMN;
					Chessboard.this.fields[r][c].unmark();
					// Avmarkerar alla radrutor den inte kan nå
				}
			}
		}
	}

	public class Knight extends Chesspiece {
		public Knight(char color, char name) {
			super(color, name);
			super.color = color;
			super.name = name;
		}

		public void markReachableFields() { // hästen
			// Jag löser förflyttningen för hästen genom att skapa en matris där varje index
			// bildar koordinater till en ruta i brädet tillsammans i formatet: {row,column}

			int moveVectors[][] = { { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 }, { 1, 2 }, { 1, -2 }, { -1, 2 },
					{ -1, -2 } }; // rör sig med hjälp av punkter, alla 8 kombinationer som hästen kan röra sig

			for (int[] move : moveVectors) { // rör element från en vektor till en annan
				byte col = (byte) (column + move[1]); // flyttar hästen till en annan vektor inom spelplanen
				char row = (char) (this.row + move[0]);

				if (Chessboard.this.isValidField(row, col)) {
					int r = row - FIRST_ROW;
					int c = col - FIRST_COLUMN; // tar -1 eftersom indexet börjar på "0"
					Chessboard.this.fields[r][c].mark(); // markerar alla punkter den kan röra sig till
				}
			}
		}

		public void unmarkReachableFields() {
			int moveVectors[][] = { { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 }, { 1, 2 }, { 1, -2 }, { -1, 2 },
					{ -1, -2 } };

			for (int[] move : moveVectors) {
				byte col = (byte) (column + move[1]);
				char row = (char) (this.row + move[0]);

				if (Chessboard.this.isValidField(row, col)) {
					int r = row - FIRST_ROW;
					int c = col - FIRST_COLUMN;
					Chessboard.this.fields[r][c].unmark(); // avmarkerar alla punkter den inte kan röra sig till
				}
			}
		}
	}

	public class Bishop extends Chesspiece { // löpare

		public Bishop(char color, char name) {
			super(color, name);
			super.color = color;
			super.name = name;
		}

		public void markReachableFields() {
			for (int i = -8; i <= 8; i++) {
				byte col = (byte) (column + i); // rör sig till genom kolumnerna
				for (int j = -1; j <= 1; j += 2) {
					char row = (char) (this.row + (j * i)); // rör sig diagonalt genom spelplan

					if (Chessboard.this.isValidField(row, col)) {
						int r = row - FIRST_ROW;
						int c = col - FIRST_COLUMN;
						Chessboard.this.fields[r][c].mark();// rör sig diagonalt och markerar området som
															// den kan nå

					}
				}
			}
		}

		public void unmarkReachableFields() {
			for (int i = -8; i <= 8; i++) {
				byte col = (byte) (column + i);
				for (int j = -1; j <= 1; j += 2) {
					char row = (char) (this.row + (j * i));

					if (Chessboard.this.isValidField(row, col)) {
						int r = row - FIRST_ROW;
						int c = col - FIRST_COLUMN;
						Chessboard.this.fields[r][c].unmark();// rör sig diagonalt och avmarkerar området som
																// den inte kan röra sig till
					}
				}
			}
		}
	}

	public class Queen extends Chesspiece {
		public Queen(char color, char name) { // Drottningen
			super(color, name);
			super.color = color;
			super.name = name;
		}

		public void markReachableFields() {
			for (int i = -8; i <= 8; i++) {
				int y = (i == 0) ? 8 : 1; // kan röra sig horisontellt genom raderna

				byte col = (byte) (column + i); // kan röra sig vertikalt genom kolumnerna
				for (int j = -y; j <= y; j++) {
					char row = (i == 0) ? (char) (this.row + (j)) : (char) (this.row + (j * i)); // kan även röra sig
																									// diagonalt
					if (Chessboard.this.isValidField(row, col)) {
						int r = row - FIRST_ROW;
						int c = col - FIRST_COLUMN;
						Chessboard.this.fields[r][c].mark(); // markerar området drottningen kan nå
					}
				}
			}
		}

		public void unmarkReachableFields() {
			for (int i = -8; i <= 8; i++) {
				int y = (i == 0) ? 8 : 1;

				byte col = (byte) (column + i);
				for (int j = -y; j <= y; j++) {
					char row = (i == 0) ? (char) (this.row + (j)) : (char) (this.row + (j * i));

					if (Chessboard.this.isValidField(row, col)) {
						int r = row - FIRST_ROW;
						int c = col - FIRST_COLUMN;
						Chessboard.this.fields[r][c].unmark(); // avmarkerar de punkter den inte kan nå
					}
				}
			}
		}
	}

	public class King extends Chesspiece {
		public King(char color, char name) {
			super(color, name);
			super.color = color;
			super.name = name;
		}

		public void markReachableFields() {
			for (int i = -1; i <= 1; i++) { // första loopen säger att den kan röra sig ett steg i horisontellt
				for (int j = -1; j <= 1; j++) { // andra loopen... ett steg vertikalt
					byte col = (byte) (column + i);
					char row = (char) (this.row + j);

					if (Chessboard.this.isValidField(row, col)) {
						int r = row - FIRST_ROW;
						int c = col - FIRST_COLUMN;
						Chessboard.this.fields[r][c].mark(); // markerar området
					}
				}
			}
		}

		public void unmarkReachableFields() {
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					byte col = (byte) (column + i);
					char row = (char) (this.row + j);

					if (Chessboard.this.isValidField(row, col)) {
						int r = row - FIRST_ROW;
						int c = col - FIRST_COLUMN;
						Chessboard.this.fields[r][c].unmark(); // avmarkerar området
					}
				}
			}
		}
	}
}
