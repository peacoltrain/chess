package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
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
        Collection<ChessMove> myMoves;

        switch(this.type) {
            case BISHOP:
                myMoves = bMoves(board, myPosition);
                break;

            default:
                throw new RuntimeException("Not implemented");
        }

        return myMoves;
    }

    private Collection<ChessMove> bMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> bishopMoves = new HashSet<>();

        //UP-Right
        ChessPosition tempChesspositon = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        while(true) {

            //Verify if position is invalid
            if (outOfRange((tempChesspositon.getRow()), tempChesspositon.getColumn())) {
                break;
            }

            if (board.getPiece(tempChesspositon) == null) {
                bishopMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                tempChesspositon = new ChessPosition(tempChesspositon.getRow() + 1, tempChesspositon.getColumn() + 1);
            } else if (board.getPiece(tempChesspositon).pieceColor != board.getPiece(myPosition).pieceColor) {
                bishopMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                break;
            } else if (board.getPiece(tempChesspositon).pieceColor == board.getPiece(myPosition).pieceColor) {
                break;
            }
        }

        //Down-Right
        tempChesspositon = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
        while(true) {

            //Verify if position is invalid
            if (outOfRange((tempChesspositon.getRow()), tempChesspositon.getColumn())) {
                break;
            }

            if(board.getPiece(tempChesspositon) == null) {
                bishopMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                tempChesspositon = new ChessPosition(tempChesspositon.getRow() - 1, tempChesspositon.getColumn() + 1);
            }
            else if (board.getPiece(tempChesspositon).pieceColor != board.getPiece(myPosition).pieceColor) {
                bishopMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                break;
            }
            else if (board.getPiece(tempChesspositon).pieceColor == board.getPiece(myPosition).pieceColor) {
                break;
            }

        }

        //Down-Left
        tempChesspositon = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
        while(true) {

            //Verify if position is invalid
            if (outOfRange((tempChesspositon.getRow()), tempChesspositon.getColumn())) {
                break;
            }

            if(board.getPiece(tempChesspositon) == null) {
                bishopMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                tempChesspositon = new ChessPosition(tempChesspositon.getRow() - 1, tempChesspositon.getColumn() - 1);
            }
            else if (board.getPiece(tempChesspositon).pieceColor != board.getPiece(myPosition).pieceColor) {
                bishopMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                break;
            }
            else if (board.getPiece(tempChesspositon).pieceColor == board.getPiece(myPosition).pieceColor) {
                break;
            }

        }

        //Up-Left
        tempChesspositon = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        while(true) {

            //Verify if position is invalid
            if (outOfRange((tempChesspositon.getRow()), tempChesspositon.getColumn())) {
                break;
            }

            if(board.getPiece(tempChesspositon) == null) {
                bishopMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                tempChesspositon = new ChessPosition(tempChesspositon.getRow() + 1, tempChesspositon.getColumn() - 1);
            }
            else if (board.getPiece(tempChesspositon).pieceColor != board.getPiece(myPosition).pieceColor) {
                bishopMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                break;
            }
            else if (board.getPiece(tempChesspositon).pieceColor == board.getPiece(myPosition).pieceColor) {
                break;
            }

        }

        return bishopMoves;
    }

    private boolean outOfRange(int row, int col){ return row == 9 || row == 0 || col == 0 || col == 9; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece that)) return false;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
