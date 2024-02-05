package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard myCurrentBoard;

    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> potentialMoves = myCurrentBoard.getPiece(startPosition).pieceMoves(myCurrentBoard, startPosition);
        Collection<ChessMove> validMoves = new HashSet<ChessMove>();
        ChessBoard prevBoard = new ChessBoard(myCurrentBoard);
        for(ChessMove m : potentialMoves){
            myCurrentBoard.addPiece(m.endPosition, myCurrentBoard.getPiece(m.startPosition));
            myCurrentBoard.addPiece(m.startPosition, null);
            if(!isInCheck(teamTurn)){
               validMoves.add(m);
            }
            myCurrentBoard = new ChessBoard(prevBoard);
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //Set temp variables
        TeamColor ourPieceColor =  myCurrentBoard.getPiece(move.startPosition).getTeamColor();

        //If the proposed move is not the current teams move
        if(ourPieceColor != teamTurn){
            throw new InvalidMoveException();
        }

        //Get all the potential moves a piece could make
        Collection<ChessMove> valMoves = validMoves(move.startPosition);

        if(valMoves.contains(move)){

            if(move.promotionPiece != null){
                myCurrentBoard.addPiece(move.endPosition, new ChessPiece(ourPieceColor, move.promotionPiece));
                myCurrentBoard.addPiece(move.startPosition, null);
            }
            else{
                myCurrentBoard.addPiece(move.endPosition, myCurrentBoard.getPiece(move.startPosition));
                myCurrentBoard.addPiece(move.startPosition, null);
            }

            //Switch Teams
            if(this.teamTurn == TeamColor.WHITE){
                this.teamTurn = TeamColor.BLACK;
            }
            else{
                this.teamTurn = TeamColor.WHITE;
            }
        }

        else{
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor){
        ChessPosition kingPosition = null;
        Collection<ChessMove> allOppTeamMoves = new HashSet<>();

        for(int i = 1; i < 9; ++i){
            for(int j = 1; j < 9; ++j){
                ChessPosition tmpPos = new ChessPosition(i,j);
                if(myCurrentBoard.getPiece(tmpPos) != null) {
                    //Identify the color king
                    if (myCurrentBoard.getPiece(tmpPos).getPieceType() == ChessPiece.PieceType.KING && myCurrentBoard.getPiece(tmpPos).getTeamColor() == teamColor) {
                        kingPosition = tmpPos;
                    }
                    //Get all the possible moves of the other team.
                    if (myCurrentBoard.getPiece(tmpPos).getTeamColor() != teamColor) {
                        allOppTeamMoves.addAll(myCurrentBoard.getPiece(tmpPos).pieceMoves(myCurrentBoard, tmpPos));
                    }
                }
            }
        }

        if(kingPosition == null){
            return false;
        }

        for(ChessMove m : allOppTeamMoves){
                if(m.endPosition.getRow() == kingPosition.getRow() && m.endPosition.getColumn() == kingPosition.getColumn()){
                    return true;
                }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return false; //TODO, fix is in Checkmate
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return false; //TODO, fix Stalemate
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        myCurrentBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return myCurrentBoard;
    }
}
