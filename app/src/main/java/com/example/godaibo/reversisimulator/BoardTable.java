package com.example.godaibo.reversisimulator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;
import 	android.os.AsyncTask;
import android.widget.TextView;

import com.google.common.collect.MapConstraints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import edu.uab.cis.reversi.*;
import edu.uab.cis.reversi.strategy.baseline.MinMaxStrategy;
import edu.uab.cis.reversi.strategy.baseline.RandomStrategy;


public class BoardTable extends ViewGroup {

    private int parentWidth;
    private int parentHeight;
    private int dotSize;
    private int currentColor;
    private int numRow;
    private int numCol;

    public  int boardsize = 8;

    private int parentHeightmsr;

    private Context context;
    private Map<Player, Strategy> strategies;

    public Board board;

    public   PlayGameTask gameTask;



    public BoardTable(Context context) {
        super(context);
        this.context = context;
         Strategy blackStrategy = new MinMaxStrategy();
         Strategy whiteStrategy = new RandomStrategy();
        SetUpStrategy(blackStrategy, whiteStrategy);

    }

    public BoardTable(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        Strategy blackStrategy = new MinMaxStrategy();
        Strategy whiteStrategy = new RandomStrategy();
        SetUpStrategy(blackStrategy, whiteStrategy);

    }

    public BoardTable(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Strategy blackStrategy = new MinMaxStrategy();
        Strategy whiteStrategy = new RandomStrategy();
        SetUpStrategy(blackStrategy, whiteStrategy);

    }

    public void SetUpStrategy(Strategy blackStrategy , Strategy whiteStrategy){

        this.strategies = new HashMap<>();
        this.strategies.put(Player.BLACK, blackStrategy);
        this.strategies.put(Player.WHITE, whiteStrategy);
    }

    public int[] disposePins(int width, int height, int dotSize) {

        List<Strategy> strategies = new ArrayList<>();
        RandomStrategy strategy = new RandomStrategy();
     //   strategy.setChooseSquareTimeLimit(100, TimeUnit.MILLISECONDS);
        strategies.add(strategy);

        // Run N rounds, pairing each strategy with each other strategy. There will
        // actually be 2N games since each strategy gets to be both black and white
        Board board = new Board();
        for (int game = 0; game < 10; ++game) {
            for (int i = 0; i < strategies.size(); ++i) {
                for (int j = i + 1; j < strategies.size(); ++j) {
                    Strategy strategy1 = strategies.get(i);
                    Strategy strategy2 = strategies.get(j);


                    // first game: strategy1=BLACK, strategy2=WHITE
             //       reversi = new Reversi(strategy1, strategy2, timeout, timeoutUnit);
             //       updateResults(results, reversi, board);

                    // second game: strategy2=BLACK, strategy1=WHITE
                    //   reversi = new Reversi(strategy2, strategy1, timeout, timeoutUnit);
                    //   updateResults(results, reversi, board);
                }
            }
        }

        strategy = new RandomStrategy();
     //   strategy.setChooseSquareTimeLimit(100, TimeUnit.MILLISECONDS);
        strategies.add(strategy);

        parentHeightmsr = height;

        this.dotSize = dotSize;
        float numerator =  ((width-8)/boardsize);
        this.dotSize   = Math.round(numerator);

        numRow = height / dotSize + 1;
        numCol =  width / dotSize + 1;

        numRow = boardsize;
        numCol = boardsize;

        //Log.d("Pin", "Col x Row ["+numCol+"]x["+numRow+"]");
        ArrayList<Integer> tiles = new ArrayList<Integer>();
        if(boardsize == 8) {
            tiles.add(R.drawable.black_pin);
            tiles.add(R.drawable.white_pin);
            tiles.add(R.drawable.blank);
        }


        long seed = System.nanoTime();
        Collections.shuffle(tiles, new Random(seed));

        int[] dotColors = BoardTile.createDotArray(dotSize, true);
        int i = 0;


        for (int r=0; r < numRow ; r++) {
            for (int c=0; c < numCol; c++) {
                /*
                if( i > (tiles.size() -1)){
                    continue;
                }
                */
                BoardImageView pinImg = new BoardImageView(getContext(), r, c);
                pinImg.setImageResource(R.drawable.blank);
                pinImg.setCurrentPin(R.drawable.blank);

                ++i;
                this.addView(pinImg);
            }
        }
        Map<Square,Player> so = board.getSquareOwners();

        for(Square s : so.keySet()){

           Player p = so.get(s);
            BoardImageView pinImg = (BoardImageView) getChildAt(s.getColumn() + ( s.getRow()  * 8 ));
            if(p == Player.BLACK)
            {

                //Log.d("Pin", "Is Reset ["+isReset+"]");

                if (pinImg != null) {
                    pinImg.setPinColor(R.drawable.black_pin);

                }
            }
            else if ((p == Player.WHITE)) {
                if (pinImg != null) {
                    pinImg.setPinColor(R.drawable.white_pin);

                }
            }
        }

this.board = board;
        return new int[]{8, 8};
    }

    public void play(Board board)  {


      gameTask = new    PlayGameTask();

        gameTask.strategies = strategies;
        gameTask.execute(board);
      /*
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Board curr = board;
        while (!curr.isComplete()) {
            if (curr.getCurrentPossibleSquares().isEmpty()) {
                curr = curr.pass();
            } else {
                Player player = curr.getCurrentPlayer();
                final Strategy strategy = this.strategies.get(player);
                final Board boardForFuture = curr;
                Future<Square> future = executor.submit(new MyCallable(strategy, boardForFuture));

                try {
                    Square square = future.get(6000, TimeUnit.MILLISECONDS);
                    curr = curr.play(square);


                    UpdateBoard(curr);
                } catch (Exception e) {
                    future.cancel(true);
                }
            }
        }
        executor.shutdownNow();
        return curr;
        */
    }

    public void UpdateBoard(Board board){
        Map<Square,Player> so = board.getSquareOwners();
         int whitecount = 0;
        int blackcount = 0;
        for(Square s : so.keySet()){

            Player p = so.get(s);
            BoardImageView pinImg = (BoardImageView) getChildAt(s.getColumn() + ( s.getRow()  * 8 ));
            if(p == Player.BLACK)
            {
                ++blackcount;

                //Log.d("Pin", "Is Reset ["+isReset+"]");

                if (pinImg != null && pinImg.currentPinId != R.drawable.black_pin ) {
                    pinImg.setPinColor(R.drawable.black_pin);

                }
            }
            else if ((p == Player.WHITE  )) {
                ++whitecount;

                if (pinImg != null && pinImg.currentPinId != R.drawable.white_pin) {
                    pinImg.setPinColor(R.drawable.white_pin);

                }
            }
        }

        for (int r=0; r < 8 ; r++) {
            for (int c = 0; c < 8; c++) {
                /*
                if( i > (tiles.size() -1)){
                    continue;
                }
                */
                BoardImageView pinImg = (BoardImageView) getChildAt(c + ( r  * 8 ));
                if(pinImg.currentPinId == R.drawable.blackopen || pinImg.currentPinId == R.drawable.whiteopen ) {
                    pinImg.setPinColor(R.drawable.blank);
                }
            }
        }

        Set<Square> ps = board.getCurrentPossibleSquares();
        Player currentPlaya = board.getCurrentPlayer();
        for(Square s : ps) {
            BoardImageView pinImg = (BoardImageView) getChildAt(s.getColumn() + ( s.getRow()  * 8 ));
            if(currentPlaya == Player.BLACK)
            {

                //Log.d("Pin", "Is Reset ["+isReset+"]");

                if (pinImg != null && pinImg.currentPinId != R.drawable.blackopen ) {
                    pinImg.setPinColor(R.drawable.blackopen);

                }
            }
            else if ((currentPlaya == Player.WHITE  )) {
                if (pinImg != null && pinImg.currentPinId != R.drawable.whiteopen) {
                    pinImg.setPinColor(R.drawable.whiteopen);

                }
            }
        }


        ViewGroup row = (ViewGroup)  getParent();
        TextView textWhite = (TextView) row.findViewById(R.id.whitetextView);
        TextView textBlack = (TextView) row.findViewById(R.id.blackTextView);

        textWhite.setText("White - Random: "+ whitecount);
        textBlack.setText("Black - MinMax: "+ blackcount);


        invalidate();
        requestLayout();
    }

     class PlayGameTask extends AsyncTask<Board, Board, Board> {
         private Map<Player, Strategy> strategies;
         Board curr = null;

         @Override
        protected Board doInBackground(Board... board) {
             curr = board[0];
             while (!curr.isComplete()) {
                 if (curr.getCurrentPossibleSquares().isEmpty()) {
                     curr = curr.pass();
                 } else {
                     Player player = curr.getCurrentPlayer();
                     final Strategy strategy = this.strategies.get(player);
                     final Board boardForFuture = curr;


                     try {
                         Square square = strategy.chooseSquare(boardForFuture);
                         String boardForFuturestr = boardForFuture.toString();
                         curr = curr.play(square);
                         publishProgress(curr);
                     } catch (Exception e) {

                     }
                 }
                 try {
                     synchronized(this){
                         wait(1000);
                     }
                 }
                 catch(InterruptedException ex){
                 }
             }
             return curr;
        }

         @Override
        protected void onProgressUpdate(Board... progress) {
             UpdateBoard(progress[0]);
        }

         @Override
        protected void onPostExecute(Board result) {

        }


     }


    public static class MyCallable implements Callable<Square>
    {

        private  Strategy _strategy;
        private Board _board;

        public MyCallable (Strategy strategy, Board board)
        {
            _strategy= strategy;
            _board = board;
        }

        @Override
        public Square call() throws Exception {
            try {
                synchronized(this){
                    wait(1500);
                }
            }
            catch(InterruptedException ex){
            }
            return  _strategy.chooseSquare(_board);
        }
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        parentWidth  = MeasureSpec.getSize(widthMeasureSpec) ;
        parentHeight = MeasureSpec.getSize(heightMeasureSpec) ;
        // parentHeightmsr = heightMeasureSpec;
        this.setMeasuredDimension(parentWidth, parentHeight);
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {

        float numerator =  (parentHeightmsr/14);
        int marginTop    = Math.round(numerator);

        int childCount = getChildCount();
        //  Toast.makeText(context, ""+marginTop,Toast.LENGTH_LONG).show();

        for (int i=0; i < childCount; i++) {
            BoardImageView pinImg = (BoardImageView) getChildAt(i);

            //int left = pinImg.getCol() * dotSize + dotSize * (pinImg.getType() == PinImageView.COLOR_COMMANDS || pinImg.getType() == PinImageView.DELETE ? 0 : 1);
            int left = (pinImg.getCol() * dotSize) - 3;
            //int top = pinImg.getRow()  * dotSize + dotSize * (pinImg.getType() == PinImageView.COLOR_COMMANDS || pinImg.getType() == PinImageView.DELETE ? 0 : 1);
            int top = (pinImg.getRow()  * dotSize) + marginTop;
            int right = (left + dotSize) - 3 ;
            int bottom = top + dotSize + marginTop; ;

            pinImg.layout(left, top, right, bottom);
        }

    }

    public int getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(int currentColor) {
        this.currentColor = currentColor;
    }



    public void changePinColor(int x, int y) {
        int row = getRow(y);
        int col = getColumn(x);

        BoardImageView pinImg = (BoardImageView) getChildAt(col + ( row  * numCol ));
        //Log.d("Pin", "Is Reset ["+isReset+"]");

        if (pinImg != null) {
            pinImg.setPinColor(pinImg.currentPinId);

        }
    }

    public BoardImageView GetTileAt(int x, int y) {
        int row = getRow(y);
        int col = getColumn(x);

        BoardImageView pinImg = (BoardImageView) getChildAt(col + ( row  * numCol ));
        //Log.d("Pin", "Is Reset ["+isReset+"]");

        return pinImg;
    }

    public BoardImageView GetTileAtIndex(int row, int col) {

        BoardImageView pinImg = (BoardImageView) getChildAt(col + ( row  * numCol ));
        //Log.d("Pin", "Is Reset ["+isReset+"]");

        return pinImg;
    }

    public void SwapWithBlank(BoardImageView tile) {

        if(IsAjacentToBlank( tile)){

            BoardImageView blankImg = null;
            //Log.d("Pin", "Is Reset ["+isReset+"]");
            for (int r=0; r < numRow ; r++) {
                for (int c=0; c < numCol; c++) {
                    BoardImageView Img = (BoardImageView) getChildAt(r + ( c  * numCol ));
                    if(Img.currentPinId == R.drawable.blank){
                        blankImg = Img;
                        blankImg.setCurrentPin(tile.currentPinId);
                        blankImg.setPinColor(tile.currentPinId);
                        break;
                    }
                }
            }
            tile.setCurrentPin(R.drawable.blank);
            tile.setPinColor(R.drawable.blank);
        }
    }

    public boolean IsAjacentToBlank(int x, int y) {
        int row = getRow(y);
        int col = getColumn(x);

        BoardImageView pinImg = (BoardImageView) getChildAt(col + ( row  * numCol ));
        //Log.d("Pin", "Is Reset ["+isReset+"]");

        if (pinImg != null) {
            BoardImageView blankImg = null;
            for (int r=0; r < numRow ; r++) {
                for (int c=0; c < numCol; c++) {
                    BoardImageView Img = (BoardImageView) getChildAt(r + ( c  * numCol ));
                    if(Img.currentPinId == R.drawable.blank){
                        blankImg = Img;
                        break;
                    }
                }
            }

            if(blankImg != null){
                if(pinImg.getCol() == (blankImg.getCol() +1) && pinImg.getRow() == (blankImg.getRow()))
                {
                    return  true;
                }
                else if(pinImg.getCol() == (blankImg.getCol() - 1) && pinImg.getRow() == (blankImg.getRow()))
                {
                    return  true;
                }
                else if(pinImg.getCol() == blankImg.getCol() && (pinImg.getRow() + 1) == (blankImg.getRow()))
                {
                    return  true;
                }
                else if(pinImg.getCol() == blankImg.getCol() && (pinImg.getRow() - 1) == (blankImg.getRow()))
                {
                    return  true;
                }
                else if(pinImg.getCol() == blankImg.getCol() && (pinImg.getRow() - 1) == (blankImg.getRow()))
                {
                    return  true;
                }
            }
        }
        return false;
    }

    public boolean IsAjacentToBlank(BoardImageView pinImg) {


        if (pinImg != null) {
            BoardImageView blankImg = null;
            for (int r=0; r < numRow ; r++) {
                for (int c=0; c < numCol; c++) {
                    BoardImageView Img = (BoardImageView) getChildAt(r + ( c  * numCol ));
                    if(Img.currentPinId == R.drawable.blank){
                        blankImg = Img;
                        break;
                    }
                }
            }

            if(blankImg != null){
                if(pinImg.getCol() == (blankImg.getCol() +1) && pinImg.getRow() == (blankImg.getRow()))
                {
                    return  true;
                }
                else if(pinImg.getCol() == (blankImg.getCol() - 1) && pinImg.getRow() == (blankImg.getRow()))
                {
                    return  true;
                }
                else if(pinImg.getCol() == blankImg.getCol() && (pinImg.getRow() + 1) == (blankImg.getRow()))
                {
                    return  true;
                }
                else if(pinImg.getCol() == blankImg.getCol() && (pinImg.getRow() - 1) == (blankImg.getRow()))
                {
                    return  true;
                }
                else if(pinImg.getCol() == blankImg.getCol() && (pinImg.getRow() - 1) == (blankImg.getRow()))
                {
                    return  true;
                }
            }
        }
        return false;
    }

    public boolean IsBlank(int x, int y) {
        int row = getRow(y);
        int col = getColumn(x);

        BoardImageView pinImg = (BoardImageView) getChildAt(col + ( row  * numCol ));
        //Log.d("Pin", "Is Reset ["+isReset+"]");

        if (pinImg != null) {
            BoardImageView blankImg = null;
            for (int r=0; r < numRow ; r++) {
                for (int c=0; c < numCol; c++) {
                    BoardImageView Img = (BoardImageView) getChildAt(r + ( c  * numCol ));
                    if(Img.currentPinId == R.drawable.blank){
                        blankImg = Img;
                        break;
                    }
                }
            }

            if(blankImg != null){
                if(pinImg.getCol() == (blankImg.getCol()) && pinImg.getRow() == (blankImg.getRow()))
                {
                    return  true;
                }
            }
        }
        return false;
    }

    public int verify(int x, int y) {
        int x1 = x ;
        int y1 = y;

        int col = getColumn(x1);
        int row = getRow(y1);

        //Log.d("Pin", "X ["+x1+"] - Y ["+y1+"] : Row ["+row+"] - Col ["+col+"]");

        if (x1 < 0 || y1 < 0)
            return -1;


        // Calc board size
        int width = numCol * dotSize;
        int height = numRow * dotSize;

        if (x1 > width)
            return -1;

        if (y1 > height)
            return 0;

        return 1;

    }

    public int getRow(int y) {
        float numerator =  (parentHeightmsr/14);
        int marginTop    = Math.round(numerator);
        return (int) Math.ceil((y - marginTop) / dotSize);
    }

    public int getColumn(int x) {
        return (int) Math.ceil( x / dotSize);
    }



    public Bitmap createBitmap() {
        //Log.d("Pin", "Image W ["+this.getWidth()+"] x H ["+this.getHeight()+"]");
        Bitmap b = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);

        this.draw(c);

        return b;
    }



}
