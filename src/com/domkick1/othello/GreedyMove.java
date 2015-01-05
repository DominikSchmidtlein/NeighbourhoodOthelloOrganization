package com.domkick1.othello;

import java.util.*;

public class GreedyMove implements MoveStrategy
{
   
    public GreedyMove()
    {
        
    }

    public Move selectMove(Othello othello)
    {
        ArrayList<Move> moves = othello.generateMoves(othello.getTurn());
        if(moves.isEmpty()){
            return null;
        }
        Move bestMove = moves.get(0);
        int bestScore = othello.evaluateMove(bestMove);
        
        for(Move move: moves){
            int evaluation = othello.evaluateMove(move);
            if(evaluation > bestScore){
                bestScore = evaluation;
                bestMove = move;
            }
        }
        return bestMove;
    }
}
