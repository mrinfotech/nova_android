package com.mrnovacrm.b2b_dealer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.mrnovacrm.R;

public class VideosListFragment extends Fragment {

    RelativeLayout videorel, imgrel;
    VideoView videoview;
    private int stopPosition;

    boolean isClicked = false;
    private MediaController media_Controller;
    private DisplayMetrics dm;

    boolean isVideoClicked=false;

    public VideosListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_videos, container, false);
        videorel = rootView.findViewById(R.id.videorel);
        imgrel = rootView.findViewById(R.id.imgrel);
        isClicked = false;
        videorel.setVisibility(View.GONE);

        videoview = rootView.findViewById(R.id.videoview);
        ImageView playicon = rootView.findViewById(R.id.playicon);

        playicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = true;
                imgrel.setVisibility(View.GONE);
                videorel.setVisibility(View.VISIBLE);

//                videoview.setMediaController(new MediaController(getActivity()));
//                Uri video = Uri.parse("android.resource://" + getActivity().getPackageName() + "/"
//                        + R.raw.novavideo);
//                //do not add any extension
////if your file is named sherif.mp4 and placed in /raw
////use R.raw.sherif
//                videoview.setVideoURI(video);
//                videoview.start();

                media_Controller = new MediaController(getActivity());
                dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                int height = dm.heightPixels;
                int width = dm.widthPixels;
                videoview.setMinimumWidth(width);
                videoview.setMinimumHeight(height);
                videoview.setMediaController(media_Controller);
                videoview.setVideoPath("android.resource://" + getActivity().getPackageName() + "/"+ R.raw.novavideo);
                videoview.start();
                media_Controller.show(900000000);
                isVideoClicked=true;
            }
        });

        videorel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(media_Controller!=null)
                {
                    media_Controller.hide();
                }
                return false;
            }
        });

        videorel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(media_Controller!=null)
                {
                    media_Controller.show();
                }
                if(isVideoClicked)
                {
                    isVideoClicked=false;
                    if(videoview!=null)
                    {
                        videoview.start();
                    }
                }else{
                    if (videoview != null) {
                        isVideoClicked=true;
                        videoview.pause();
                        stopPosition = videoview.getCurrentPosition(); //stopPosition is an int
                        videoview.pause();
                        if(media_Controller!=null)
                        {
                            media_Controller.hide();
                        }
                    }
                }

            }
        });



        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    //    Toast.makeText(getActivity(), "onpause clicked", Toast.LENGTH_SHORT).show();
        //if(isClicked) {
//        if (videoview != null) {
//            videoview.pause();
//            stopPosition = videoview.getCurrentPosition(); //stopPosition is an int
//            videoview.pause();
//
//            if(media_Controller!=null)
//            {
//                media_Controller.hide();
//            }
//        }
        //}
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
       // Toast.makeText(getActivity(),"ON HIDDEN CHAGNED",Toast.LENGTH_SHORT).show();
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible())
        {
            if (!isVisibleToUser)   // If we are becoming invisible, then...
            {
                if (videoview != null) {
                    isVideoClicked=true;
                    videoview.pause();
                    stopPosition = videoview.getCurrentPosition(); //stopPosition is an int
                    videoview.pause();
                    if(media_Controller!=null)
                    {
                        media_Controller.hide();
                    }
                }
            }
            if (isVisibleToUser)
            {

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(getActivity(),"ON RESUME CALLED",Toast.LENGTH_SHORT).show();
    }


}