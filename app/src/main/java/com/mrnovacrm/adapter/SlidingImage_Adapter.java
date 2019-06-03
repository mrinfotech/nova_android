package com.mrnovacrm.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.mrnovacrm.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class SlidingImage_Adapter extends PagerAdapter {


    private ArrayList<Integer> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    ArrayList<HashMap<String, String>> hashMapArrayList;
    private final static int FADE_DURATION = 1000; // in milliseconds
    private int lastPosition = -1;

    ArrayList<Integer> array_image = new ArrayList<Integer>();

    FragmentManager mfragmentManager;

    public SlidingImage_Adapter(Context context, ArrayList<HashMap<String, String>> hashmapList, FragmentManager fragmentManager) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        hashMapArrayList = hashmapList;
        mfragmentManager = fragmentManager;
//        array_image.add(R.drawable.todaydeals);
//        array_image.add(R.drawable.offerdeals);
        array_image.add(R.drawable.hotdeals);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        //return 3;
        return 1;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout
                .findViewById(R.id.image);

//        String bannerPath = hashMapArrayList.get(position).get("Path");

        Picasso.with(context).load(array_image.get(position)).fit().centerCrop()
                .into(imageView);

        view.addView(imageLayout, 0);
        setScaleAnimation(imageView, position);

        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("position", position);
//                Fragment fragment = new ProductsListFragment();
//                fragment.setArguments(bundle);
//                FragmentTransaction fragmentTransaction = mfragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.container_body, fragment);
//                fragmentTransaction.commit();

            }
        });


        return imageLayout;
    }

    private void setScaleAnimation(View view, int position) {
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
            lastPosition = position;
        }

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
