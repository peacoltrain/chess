package chess;

//import java.util.ArrayList;
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
        Collection<ChessMove> myMoves = switch (this.type) {
            case BISHOP -> bMoves(board, myPosition);
            case ROOK -> rMoves(board, myPosition);
            case QUEEN -> qMoves(board, myPosition);
            case KNIGHT -> kightMoves(board, myPosition);
            case PAWN -> pMoves(board, myPosition);
            default -> throw new RuntimeException("Not implemented");
        };

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
    private Collection<ChessMove> rMoves(ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> rookMoves = new HashSet<>();

        //UP
        ChessPosition tempChesspositon = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
        while(true) {

            //Verify if position is invalid
            if (outOfRange((tempChesspositon.getRow()), tempChesspositon.getColumn())) {
                break;
            }

            if (board.getPiece(tempChesspositon) == null) {
                rookMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                tempChesspositon = new ChessPosition(tempChesspositon.getRow() + 1, tempChesspositon.getColumn());
            } else if (board.getPiece(tempChesspositon).pieceColor != board.getPiece(myPosition).pieceColor) {
                rookMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                break;
            } else if (board.getPiece(tempChesspositon).pieceColor == board.getPiece(myPosition).pieceColor) {
                break;
            }
        }

        //Right
        tempChesspositon = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
        while(true) {

            //Verify if position is invalid
            if (outOfRange((tempChesspositon.getRow()), tempChesspositon.getColumn())) {
                break;
            }

            if (board.getPiece(tempChesspositon) == null) {
                rookMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                tempChesspositon = new ChessPosition(tempChesspositon.getRow(), tempChesspositon.getColumn() + 1);
            } else if (board.getPiece(tempChesspositon).pieceColor != board.getPiece(myPosition).pieceColor) {
                rookMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                break;
            } else if (board.getPiece(tempChesspositon).pieceColor == board.getPiece(myPosition).pieceColor) {
                break;
            }
        }

        //Down
        tempChesspositon = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
        while(true) {

            //Verify if position is invalid
            if (outOfRange((tempChesspositon.getRow()), tempChesspositon.getColumn())) {
                break;
            }

            if (board.getPiece(tempChesspositon) == null) {
                rookMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                tempChesspositon = new ChessPosition(tempChesspositon.getRow() - 1, tempChesspositon.getColumn());
            } else if (board.getPiece(tempChesspositon).pieceColor != board.getPiece(myPosition).pieceColor) {
                rookMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                break;
            } else if (board.getPiece(tempChesspositon).pieceColor == board.getPiece(myPosition).pieceColor) {
                break;
            }
        }

        //Left
        tempChesspositon = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
        while(true) {

            //Verify if position is invalid
            if (outOfRange((tempChesspositon.getRow()), tempChesspositon.getColumn())) {
                break;
            }

            if (board.getPiece(tempChesspositon) == null) {
                rookMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                tempChesspositon = new ChessPosition(tempChesspositon.getRow(), tempChesspositon.getColumn() - 1);
            } else if (board.getPiece(tempChesspositon).pieceColor != board.getPiece(myPosition).pieceColor) {
                rookMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                break;
            } else if (board.getPiece(tempChesspositon).pieceColor == board.getPiece(myPosition).pieceColor) {
                break;
            }
        }

        return rookMoves;
    }
    private Collection<ChessMove> qMoves(ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> queenMoves = new HashSet<>();

        //UP
        ChessPosition tempChesspositon = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
        while(true) {

            //Verify if position is invalid
            if (outOfRange((tempChesspositon.getRow()), tempChesspositon.getColumn())) {
                break;
            }

            if (board.getPiece(tempChesspositon) == null) {
                queenMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                tempChesspositon = new ChessPosition(tempChesspositon.getRow() + 1, tempChesspositon.getColumn());
            } else if (board.getPiece(tempChesspositon).pieceColor != board.getPiece(myPosition).pieceColor) {
                queenMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                break;
            } else if (board.getPiece(tempChesspositon).pieceColor == board.getPiece(myPosition).pieceColor) {
                break;
            }
        }

        //Right
        tempChesspositon = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
        while(true) {

            //Verify if position is invalid
            if (outOfRange((tempChesspositon.getRow()), tempChesspositon.getColumn())) {
                break;
            }

            if (board.getPiece(tempChesspositon) == null) {
                queenMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                tempChesspositon = new ChessPosition(tempChesspositon.getRow(), tempChesspositon.getColumn() + 1);
            } else if (board.getPiece(tempChesspositon).pieceColor != board.getPiece(myPosition).pieceColor) {
                queenMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                break;
            } else if (board.getPiece(tempChesspositon).pieceColor == board.getPiece(myPosition).pieceColor) {
                break;
            }
        }

        //Down
        tempChesspositon = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
        while(true) {

            //Verify if position is invalid
            if (outOfRange((tempChesspositon.getRow()), tempChesspositon.getColumn())) {
                break;
            }

            if (board.getPiece(tempChesspositon) == null) {
                queenMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                tempChesspositon = new ChessPosition(tempChesspositon.getRow() - 1, tempChesspositon.getColumn());
            } else if (board.getPiece(tempChesspositon).pieceColor != board.getPiece(myPosition).pieceColor) {
                queenMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                break;
            } else if (board.getPiece(tempChesspositon).pieceColor == board.getPiece(myPosition).pieceColor) {
                break;
            }
        }

        //Left
        tempChesspositon = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
        while(true) {

            //Verify if position is invalid
            if (outOfRange((tempChesspositon.getRow()), tempChesspositon.getColumn())) {
                break;
            }

            if (board.getPiece(tempChesspositon) == null) {
                queenMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                tempChesspositon = new ChessPosition(tempChesspositon.getRow(), tempChesspositon.getColumn() - 1);
            } else if (board.getPiece(tempChesspositon).pieceColor != board.getPiece(myPosition).pieceColor) {
                queenMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                break;
            } else if (board.getPiece(tempChesspositon).pieceColor == board.getPiece(myPosition).pieceColor) {
                break;
            }
        }

        tempChesspositon = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        while(true) {

            //Verify if position is invalid
            if (outOfRange((tempChesspositon.getRow()), tempChesspositon.getColumn())) {
                break;
            }

            if (board.getPiece(tempChesspositon) == null) {
                queenMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                tempChesspositon = new ChessPosition(tempChesspositon.getRow() + 1, tempChesspositon.getColumn() + 1);
            } else if (board.getPiece(tempChesspositon).pieceColor != board.getPiece(myPosition).pieceColor) {
                queenMoves.add(new ChessMove(myPosition, tempChesspositon, null));
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
                queenMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                tempChesspositon = new ChessPosition(tempChesspositon.getRow() - 1, tempChesspositon.getColumn() + 1);
            }
            else if (board.getPiece(tempChesspositon).pieceColor != board.getPiece(myPosition).pieceColor) {
                queenMoves.add(new ChessMove(myPosition, tempChesspositon, null));
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
                queenMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                tempChesspositon = new ChessPosition(tempChesspositon.getRow() - 1, tempChesspositon.getColumn() - 1);
            }
            else if (board.getPiece(tempChesspositon).pieceColor != board.getPiece(myPosition).pieceColor) {
                queenMoves.add(new ChessMove(myPosition, tempChesspositon, null));
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
                queenMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                tempChesspositon = new ChessPosition(tempChesspositon.getRow() + 1, tempChesspositon.getColumn() - 1);
            }
            else if (board.getPiece(tempChesspositon).pieceColor != board.getPiece(myPosition).pieceColor) {
                queenMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                break;
            }
            else if (board.getPiece(tempChesspositon).pieceColor == board.getPiece(myPosition).pieceColor) {
                break;
            }

        }

        return queenMoves;
    }
    private Collection<ChessMove> kightMoves(ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> knightMoves = new HashSet<>();
        int[] smallStep = {-1,1};
        int[] largeStep = {-2,2};

        //Vertical
        for(int l : largeStep) {
            ChessPosition tempChesspositon = new ChessPosition(myPosition.getRow() + l, myPosition.getColumn());
            for (int s : smallStep) {
                tempChesspositon = new ChessPosition(tempChesspositon.getRow(), myPosition.getColumn() + s);

                //Verify if position is valid
                if (outOfRange((tempChesspositon.getRow()), tempChesspositon.getColumn())) {
                    continue;
                }

                if (board.getPiece(tempChesspositon) == null) {
                    knightMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                } else if (board.getPiece(tempChesspositon).pieceColor != board.getPiece(myPosition).pieceColor) {
                    knightMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                }
            }
        }


        //Horizontal
        for(int l : largeStep) {
            ChessPosition tempChesspositon = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + l);
            for (int s : smallStep) {
                tempChesspositon = new ChessPosition(myPosition.getRow() + s, tempChesspositon.getColumn());

                //Verify if position is valid
                if (outOfRange((tempChesspositon.getRow()), tempChesspositon.getColumn())) {
                    continue;
                }

                if (board.getPiece(tempChesspositon) == null) {
                    knightMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                } else if (board.getPiece(tempChesspositon).pieceColor != board.getPiece(myPosition).pieceColor) {
                    knightMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                }
            }
        }


        return knightMoves;
    }
    private Collection<ChessMove> pMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> pawnMoves = new HashSet<>();
        int[] sides = {-1, 1};
        ChessPiece.PieceType[] upgrades = {PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT, PieceType.QUEEN};

        if(board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE){
            ChessPosition tempChesspositon = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());

            //Check directly foward
            //Verify if position is valid
            if (!outOfRange((tempChesspositon.getRow()), tempChesspositon.getColumn())) {
                if (board.getPiece(tempChesspositon) == null) {
                    if(tempChesspositon.getRow() == 8){
                        for(var v : upgrades){ pawnMoves.add(new ChessMove(myPosition, tempChesspositon, v)); }
                    }
                    else {
                        pawnMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                    }
                }
            }

            if(myPosition.getRow() == 2){
                tempChesspositon = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                if (board.getPiece(tempChesspositon) == null) {
                    pawnMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                }
            }


        }
        else{
            ChessPosition tempChesspositon = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());

            //Check directly foward
            //Verify if position is valid
            if (!outOfRange((tempChesspositon.getRow()), tempChesspositon.getColumn())) {
                if (board.getPiece(tempChesspositon) == null) {
                    if(tempChesspositon.getRow() == 1){
                        for(var v : upgrades){ pawnMoves.add(new ChessMove(myPosition, tempChesspositon, v)); }
                    }
                    else {
                        pawnMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                    }
                }
            }

            if(myPosition.getRow() == 7){
                tempChesspositon = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                if (board.getPiece(tempChesspositon) == null) {
                    pawnMoves.add(new ChessMove(myPosition, tempChesspositon, null));
                }
            }
        }



        return pawnMoves;
    }

    private boolean outOfRange(int row, int col){ return row >= 9 || row <= 0 || col <= 0 || col >= 9; }

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
