package com.example.godaibo.reversisimulator;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class ReversiFragment extends Fragment implements View.OnTouchListener {

    public BoardTable boardTable;
    private int currentPinDraw = R.drawable.blank;
    Button btMenu;
    BoardImageView lastTile = null;
    int numberofmoves = 0;

    private TextView titleMsg;

    public ReversiFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reversi, container, false);
        boardTable = (BoardTable) v.findViewById(R.id.pinsTable);
        boardTable.setOnTouchListener(this);
        createBoard();

        boardTable.setCurrentColor(currentPinDraw);
        this.setRetainInstance(true);
        btMenu = (Button) v.findViewById(R.id.button_menu);

        titleMsg = (TextView) v.findViewById(R.id.titleMsg);
        return  v;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int x = (int) event.getX() ;
        int y = (int) event.getY() ;


        if(boardTable.IsBlank(x, y)){
            if(lastTile != null){
                boardTable.SwapWithBlank(lastTile);
                ++numberofmoves;
                if(numberofmoves == 1) {
                    titleMsg.setText(numberofmoves + " move");
                }
                else {
                    titleMsg.setText(numberofmoves + " moves");
                }
                GetCurrentState();
                lastTile = null;
            }
        }
        if(!boardTable.IsAjacentToBlank(x, y)){
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            boardTable.changePinColor(x, y);
            lastTile =  boardTable.GetTileAt(x,y);
            return true;
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            boardTable.changePinColor(x, y);
            lastTile =  boardTable.GetTileAt(x,y);
            return true;
        }

        return false;

    }

    public void setCurrentPin(int id) {
        this.currentPinDraw = id;
        boardTable.setCurrentColor(currentPinDraw);
    }

    public void clearBoard() {
        boardTable.removeAllViews();
        boardTable.invalidate();
        createBoard();

    }

    public Bitmap createBitmap() {
        return boardTable.createBitmap();
    }



    public void setBackground(int resourceId) {
        boardTable.setBackgroundResource(resourceId);
    }


    @SuppressWarnings("deprecation")
    public void setBackground(Bitmap b) {
        boardTable.setBackgroundDrawable(new BitmapDrawable(this.getResources(), b ) );
    }

    public void StartGame(){

        clearBoard();
        ReversiActivity.gameTable = boardTable;
        boardTable.play(boardTable.board);
    }

    public void New4Board(){
        boardTable.boardsize = 4;
        lastTile = null;
        titleMsg.setText(numberofmoves+" moves");
        clearBoard();
    }


    private void createBoard() {
        // We start defining the table grid
        Display d = getActivity().getWindowManager().getDefaultDisplay();

        int pinSize = (int) (TableConfig.convertDpToPixel(TableConfig.DEFAULT_PIN_SIZE, getActivity()));

        int[] vals = boardTable.disposePins(d.getWidth(), d.getHeight(), pinSize);

        // oracle = new Astar(GetCurrentState());

    }


    public int[][] GetCurrentState() {

        int[][] state = new int[boardTable.boardsize][boardTable.boardsize];

        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < boardTable.boardsize; r++) {
            for (int c = 0; c < boardTable.boardsize; c++) {
                BoardImageView biv = boardTable.GetTileAtIndex(r, c);
                if (biv.currentPinId == R.drawable.black_pin) {
                    state[r][c] = 1;
                } else if (biv.currentPinId == R.drawable.white_pin) {
                    state[r][c] = 2;
                } else if (biv.currentPinId == R.drawable.blank) {
                    state[r][c] = 0;
                }

                sb.append(state[r][c] + ",");
            }


        }

        return state;
    }


}
