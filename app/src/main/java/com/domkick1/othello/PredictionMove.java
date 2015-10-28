package com.domkick1.othello;

import java.util.ArrayList;

/**
 * Created by dominikschmidtlein on 10/14/2015.
 */
public class PredictionMove implements MoveStrategy {
    @Override
    public Move selectMove(Othello othello) {
        return evaluateBoard(othello, othello.getTurn(), 3);
    }

    private Move evaluateBoard(Othello othello, char turn, int depth){
        if(depth == 0){
            ArrayList<Integer> counts = othello.countChips();
            if(turn == othello.X)
                return new Move(counts.get(0) - counts.get(1));
            return new Move(counts.get(1) - counts.get(0));
        }
        ArrayList<Move> moves = othello.generateMoves(othello.getTurn());

        Move bestMove = new Move(Integer.MIN_VALUE);
        if(othello.getTurn() != turn)
            bestMove.setRating(Integer.MAX_VALUE);
        if(moves == null)
            return bestMove;
        for(Move move: moves){
            Othello otemp = new Othello(othello);
            otemp.play(move);
            move.setRating(evaluateBoard(otemp, turn, depth - 1).getRating());
            if(turn == othello.getTurn()){
                if(move.getRating() > bestMove.getRating())
                    bestMove = move;
            }
            else {
                if(move.getRating() < bestMove.getRating())
                    bestMove = move;
            }
        }
        return bestMove;
    }
}
