package com.example.godaibo.reversisimulator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;


public class ReversiActivity extends Activity implements MenuFragment.MenuEventListener {

    public SlidingPaneLayout sPaneLy;
    private ReversiFragment pinTableFrag;
    private static final int SELECT_PICTURE = 1410;

    public static BoardTable gameTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_reversi);

        this.sPaneLy = (SlidingPaneLayout) findViewById(R.id.sp);

        pinTableFrag = (ReversiFragment) getFragmentManager()
                .findFragmentById(R.id.pinTable);

        pinTableFrag.btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show/hide the menu
                if(sPaneLy.isOpen()){
                    sPaneLy.closePane();
                }
                else{
                    sPaneLy.openPane();
                }
                // menu_right.setVisibility(View.GONE);;
                // menu_left.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected  void onPause(){
        super.onPause();
        if(gameTable != null){
            if(gameTable.gameTask != null){
              //  gameTable.gameTask.cancel(true);

            }
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reversi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**************************************************
     *
     * CallBack methods
     *
     **************************************************/

    @Override
    public void onPinSelected(int pinId) {

        //   pinTableFrag.setCurrentPin(pinId);
        closeMenu();
    }



    public void onBackgroundSelected() {
        /*
        LayoutInflater inf = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(R.layout.popbkg_layout, null, false);

        final PopupWindow pw = new PopupWindow(v);
        pw.setFocusable(true);
        pw.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        pw.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);

        TextView changeTxt = (TextView) v.findViewById(R.id.dlgChange);
        TextView resetTxt = (TextView) v.findViewById(R.id.dlgReset);
        TextView cancelTxt = (TextView) v.findViewById(R.id.dlgCancel);
        cancelTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });

        resetTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pw.dismiss();
                pinTableFrag.setBackground(R.drawable.tilebkg);
            }
        });

        changeTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pw.dismiss();
                // Start a new Intent to get the picture from the Gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, SELECT_PICTURE);
            }
        });

        pw.showAtLocation(v, Gravity.CENTER, 0, 0);
*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PICTURE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap bkgImg = BitmapFactory.decodeFile(filePath);
                    Display disp = getWindowManager().getDefaultDisplay();
                    int w = disp.getWidth();
                    int h = disp.getHeight();

                    double scaleX = (w * 1.0) / bkgImg.getWidth();
                    double scaleY = (h * 1.0) / bkgImg.getHeight();

                    double scale = Math.min(scaleX, scaleY);

                    double sw = w * scaleX;
                    double sh = h * scaleY;
                    // System.out.println("Scale X ["+scaleX+"] - Scale Y ["+scaleY+"] - Scale ["+scale+"]");

                    bkgImg = Bitmap.createBitmap(bkgImg, 0, 0, w, h);
                    //  pinTableFrag.setBackground(bkgImg);

                }
        }

    }

    private void closeMenu() {
        if (sPaneLy.isSlideable()) {
            // We close the left fragment
            sPaneLy.closePane();
        }

    }

    @Override
    public void onCloseMenuSelected() {

        closeMenu();
/*
        LayoutInflater inf = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(R.layout.popclear_layout, null, false);

        final PopupWindow pw = new PopupWindow(v);
        pw.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        pw.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);

        TextView yesTxt = (TextView) v.findViewById(R.id.dlgYes);
        TextView noTxt = (TextView) v.findViewById(R.id.dlgNo);
        noTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });

        yesTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pw.dismiss();
                pinTableFrag.clearBoard();
            }
        });

        pw.showAtLocation(v, Gravity.CENTER, 0, 0);
*/
    }

    @Override
    public void onStopSelected() {

        closeMenu();
      //  pinTableFrag.numberofmoves = 0;
      //  pinTableFrag.New3Board();
    }

    @Override
    public void  onStartSelected() {

        closeMenu();
        pinTableFrag.StartGame();


    }




}

