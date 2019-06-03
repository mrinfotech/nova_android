package com.mrnovacrm.adapter;

import android.app.ActionBar;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.mrnovacrm.R;
import com.mrnovacrm.activity.TouchImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 09-03-2018.
 */

public class ProductImageAdapter extends PagerAdapter {


    private ArrayList<Integer> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    List<String> imagesList;
    private final static int FADE_DURATION = 1000; // in milliseconds
    private int lastPosition = -1;
   // ArrayList<Integer> array_image = new ArrayList<Integer>();
    public ProductImageAdapter(Context context, List<String> imagesList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.imagesList=imagesList;
//        array_image.add(R.drawable.product1);
//        array_image.add(R.drawable.product2);
//        array_image.add(R.drawable.product3);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.productimage_layout, view, false);

        assert imageLayout != null;
        final TouchImageView imageView = imageLayout
                .findViewById(R.id.product_image);
        imageView.setMaxWidth(ActionBar.LayoutParams.WRAP_CONTENT);
        imageView.setMaxHeight(ActionBar.LayoutParams.WRAP_CONTENT);
//        String bannerPath = hashMapArrayList.get(position).get("Path");
        //Log.e("instantiateItem","Called");

//        Picasso.with(context).load(imagesList.get(position)).placeholder(R.drawable.loading)
//                .into(imageView);

        Picasso.with(context)
                .load(imagesList.get(position))
                .placeholder(R.drawable.loading)
                .into(imageView);
        view.addView(imageLayout, 0);
//        setScaleAnimation(imageView, position);


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
