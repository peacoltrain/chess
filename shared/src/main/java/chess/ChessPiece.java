package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }


    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece myPiece = board.getPiece(myPosition);
        Collection<ChessMove> myMoves = new ArrayList<>();

        switch(myPiece.type) {
            case BISHOP:
                myMoves = bMoves(board, myPosition);
                break;

            default:
                throw new RuntimeException("Not implemented");
        }

        return myMoves;
    }

    private Collection<ChessMove> bMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> myArray = new ArrayList<>();

        //UP-Right
        int tempRow = myPosition.getRow() + 1;
        int tempCol = myPosition.getColumn() + 1;
        while((tempRow != 0) && (tempCol != 0) && (tempRow < 9) && (tempCol < 9)){
            myArray.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), null));
            ++tempRow; ++tempCol;
        }

        //Down-Right
        tempRow = myPosition.getRow() - 1;
        tempCol = myPosition.getColumn() + 1;
        while((tempRow != 0) && (tempCol != 0) && (tempRow < 9) && (tempCol < 9)){
            myArray.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), null));
            --tempRow; ++tempCol;
        }

        //Down-Left
        tempRow = myPosition.getRow() - 1;
        tempCol = myPosition.getColumn() - 1;
        while((tempRow != 0) && (tempCol != 0) && (tempRow < 9) && (tempCol < 9)){
            myArray.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), null));
            --tempRow; --tempCol;
        }

        //Up-Left
        tempRow = myPosition.getRow() + 1;
        tempCol = myPosition.getColumn() - 1;
        while((tempRow != 0) && (tempCol != 0) && (tempRow < 9) && (tempCol < 9)){
            myArray.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), null));
            ++tempRow; --tempCol;
        }

        return myArray;
    }
}
