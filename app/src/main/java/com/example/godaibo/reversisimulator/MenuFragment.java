package com.example.godaibo.reversisimulator;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {


    public MenuFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        ImageAdapter ia = new ImageAdapter(getActivity());

        GridView gv = (GridView) v.findViewById(R.id.pinGrid);
        gv.setAdapter(ia);

        // Save Btn
        final TextView newGame = (TextView) v.findViewById(R.id.newGame);
        final Animation animNewGame = TableConfig.getRotateAnim(this.getActivity());
        animNewGame.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ( (MenuEventListener) getActivity()).onCloseMenuSelected();
            }
        });

        // Clear btn
        final TextView boardSize3 = (TextView) v.findViewById(R.id.start);
        final Animation animBoard3 = TableConfig.getRotateAnim(this.getActivity());
        animBoard3.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ( (MenuEventListener) getActivity()).onStartSelected();
            }
        });

        // Background btn
        final TextView boardSize4 = (TextView) v.findViewById(R.id.Stop);
        final Animation animBoard4 = TableConfig.getRotateAnim(this.getActivity());

        animBoard4.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ( (MenuEventListener) getActivity()).onStopSelected();
            }
        });




        boardSize3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boardSize3.startAnimation(animBoard3);

            }
        });

        newGame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                newGame.startAnimation(animNewGame);
            }
        });

        boardSize4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boardSize4.startAnimation(animBoard4);

            }
        });


        return v;
    }

    class ImageAdapter extends BaseAdapter {

        private Context ctx;

        public ImageAdapter(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public int getCount() {
            return TableConfig.pinIdList.length;
        }


        @Override
        public Object getItem(int arg0) {

            return TableConfig.pinIdList[arg0];
        }

        @Override
        public long getItemId(int arg0) {
            return TableConfig.pinIdList[arg0];
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ImageView imageView = null;
            if (imageView == null) {
                imageView = new ImageView(ctx);
                imageView.setLayoutParams(new GridView.LayoutParams(75, 75));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(2, 2, 2, 2);
                imageView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //   Log.d("PIN", "Pin:" + TableConfig.pinIdList[position]);
                        ( (MenuEventListener) getActivity()).onPinSelected(TableConfig.pinIdList[position]);

                    }
                });
            }
            else
                imageView = (ImageView) convertView;

            imageView.setImageResource(TableConfig.pinIdList[position]);

            return imageView;
        }

    }

    public static interface MenuEventListener {

        public void onPinSelected(int pinId);

        public void onCloseMenuSelected();

        public void onStartSelected() ;

        public void onStopSelected();

    }


}
