package edu.uab.cis.reversi.strategy.baseline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.MinMaxPriorityQueue;

import edu.uab.cis.reversi.Board;
import edu.uab.cis.reversi.Move;
import edu.uab.cis.reversi.Player;
import edu.uab.cis.reversi.Square;
import edu.uab.cis.reversi.Strategy;

public class MinMaxStrategy implements Strategy {

    public static MinMaxNode root;
    public static int Maxdepth =2;
    @Override
    public Square chooseSquare(Board board) {

        root = new MinMaxNode();
        Player playa = board.getCurrentPlayer();
        root.player =  playa;
        String boardstr = board.toString();
        root.board = board;
        Set<Square> moves = board.getCurrentPossibleSquares();
        MinMaxSearcher engine = new MinMaxSearcher(Maxdepth);
        engine.eval(root);
        Square move = engine.bestMove.move;
        return move;
    }

/*
    @Override
    public void setChooseSquareTimeLimit(long time, TimeUnit unit) {
        // TODO Auto-generated method stub

    }
*/

    class MinMaxNode {
        Player player;
        public Board board;
        public MinMaxNode parent;
        public List<MinMaxNode> childern;
        public Square move;
        public double utility;
        public MinMaxNode(){
            childern = new ArrayList<MinMaxNode>();
        }

        public MinMaxNode(MinMaxNode parent, Square move){
            childern = new ArrayList<MinMaxNode>();
            this.parent = parent;
            this.move = move;

        }

        public int getScore(){
            Map<Player, Integer> mp =	board.getPlayerSquareCounts();
            return mp.get(player);
        }

        public List<MinMaxNode> expand(){
            Set<Square> moves = board.getCurrentPossibleSquares();
            for(Square aMove: moves)
            {
                MinMaxNode aNode = new MinMaxNode( this, aMove);
                // Make a copy of the game board.
                // Make a possible prospective move.
                Board newb =    board.play(aMove);
                String boardstr =  newb.toString();
                Player playa = newb.getCurrentPlayer();
                aNode.player = playa;
                aNode.board = newb;
                this.childern.add(aNode);


            }
            board = null;
            return this.childern;

        }

    }

    class MinMaxSearcher {

        private int depthLimit;
        private int nodeCount;
        MinMaxNode  bestMove = null;

        Player originalPlayer;


        public MinMaxSearcher(int depthLimit){
            this.depthLimit = depthLimit;
        }

        public double eval(MinMaxNode node ){
            nodeCount = 0;
            originalPlayer = node.player;
            return minimaxEval(node, depthLimit);
        }

        public double minimaxEval(MinMaxNode node, int depthLeft){
            MinMaxNode localBestMove = null;
            boolean maximizing =( node.player == originalPlayer);
            double bestUtility = maximizing ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
            nodeCount ++;

            if(node.board.isComplete() || depthLeft == 0){
                bestMove = node;
                return node.getScore();
            }

            List<MinMaxNode> children =	node.expand();

            for(MinMaxNode child : children){
                double childUtility = minimaxEval(child, depthLeft -1);
                child.utility = childUtility;
                if(maximizing == childUtility > bestUtility){
                    bestUtility = childUtility;
                    localBestMove = child;
                }
            }

            bestMove = localBestMove;
            return bestUtility ;
        }
    }
}
