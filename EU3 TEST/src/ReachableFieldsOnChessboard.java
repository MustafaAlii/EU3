
public class ReachableFieldsOnChessboard {
	
	public static void main (String[] args) throws NotValidFieldException
	{
		Chessboard chessboard = new Chessboard();
		
		System.out.println(chessboard);
		
		Chessboard.Chesspiece[] pieces = new Chessboard.Chesspiece[6];
		
		pieces[0] = chessboard.new Pawn('w', 'P');
		pieces[1] = chessboard.new Rook('b', 'R');
		pieces[2] = chessboard.new Queen('w', 'Q');
		pieces[3] = chessboard.new Bishop('w', 'B');
		pieces[4] = chessboard.new King('b', 'K');
        pieces[5] = chessboard.new Knight('w', 'N');
		
		for (Chessboard.Chesspiece piece : pieces) {
			piece.moveTo('d', (byte) (4));
            piece.markReachableFields();
            System.out.println(chessboard + "\n");
            piece.unmarkReachableFields();
            System.out.println(chessboard + "\n");
            piece.moveOut();
		}
	}
}

