package com.mrnovacrm.b2b_dealer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrnovacrm.R;
import com.mrnovacrm.adapter.ProductImageAdapter;
import com.mrnovacrm.constants.GlobalShare;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by android on 09-03-2018.
 */

public class ProductImageActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    //ImageView product_image1;
    //          , product_image2, product_image3;
    ViewPager mPager;
    GlobalShare globalShare;
    List<String> imagesList;
    LinearLayout imglinear;
    private ViewPager samllimagepager;
    public static Activity mainfinish;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainfinish=this;
        setContentView(R.layout.product_image_views);
        View includedLayout = findViewById(R.id.include_actionbar);
        TextView actionbarheadertxt = includedLayout.findViewById(R.id.actionbarheadertxt);

        ImageView backimg = includedLayout.findViewById(R.id.backimg);
        backimg.setOnClickListener(ProductImageActivity.this);

        imglinear = findViewById(R.id.imglinear);
        mPager = findViewById(R.id.pager);

        globalShare = (GlobalShare) getApplicationContext();
        Gallery gallery = (Gallery) findViewById(R.id.mygallery);
        gallery.setOnItemClickListener(ProductImageActivity.this);

        Bundle bundle=getIntent().getExtras();
       String itemname=bundle.getString("itemname");
        actionbarheadertxt.setText(itemname);
        if (globalShare.getImagesList() != null) {
            imagesList = globalShare.getImagesList();
            if (imagesList.size() > 0) {
                mPager.setAdapter(new ProductImageAdapter(getApplicationContext(), imagesList));
                gallery.setAdapter(new ImageAdapter(this,imagesList,0));

                //                for(int i=0;i<imagesList.size();i++)
//                {
//                    ImageView image = new ImageView(this);
//                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams(120,120));
//                    image.setMaxHeight(120);
//                    image.setMaxWidth(120);
//                    image.setPadding(20,0,20,0);
//                    image.setImageResource(R.drawable.noimage);
//                    // Adds the view to the layout
//                    imglinear.addView(image);
//                }

//                for (int i = 0; i < imagesList.size(); i++) {
//                    LinearLayout.LayoutParams params = new LinearLayout
//                            .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
//                    // Add image path from drawable folder.
//                    ImageView imageView=new ImageView(this);
//                    imageView.setImageResource(R.drawable.noimage);
//                    imageView.setLayoutParams(params);
//                    imglinear.addView(imageView);
//                }
            }
        }
//        product_image1 = findViewById(R.id.product_image1);
//        product_image1.setOnClickListener(this);
//        product_image2 = findViewById(R.id.product_image2);
//        product_image2.setOnClickListener(this);
//        product_image3 = findViewById(R.id.product_image3);
//        product_image3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backimg:
                finish();
                break;
//            case R.id.product_image1:
////                product_image1.setBackgroundResource(R.drawable.imageview_selected_border);
////                product_image2.setBackgroundResource(R.drawable.imageview_border);
////                product_image3.setBackgroundResource(R.drawable.imageview_border);
////                mPager.setCurrentItem(0);
//                break;
//            case R.id.product_image2:
////                product_image1.setBackgroundResource(R.drawable.imageview_border);
////                product_image2.setBackgroundResource(R.drawable.imageview_selected_border);
////                product_image3.setBackgroundResource(R.drawable.imageview_border);
////                mPager.setCurrentItem(1);
//                break;
//            case R.id.product_image3:
////                product_image1.setBackgroundResource(R.drawable.imageview_border);
////                product_image2.setBackgroundResource(R.drawable.imageview_border);
////                product_image3.setBackgroundResource(R.drawable.imageview_selected_border);
////                mPager.setCurrentItem(2);
//                break;
            default:
                break;
        }
    }

    public void onItemClick(AdapterView adapterView,View view,int position,long id)
    {
        mPager.setCurrentItem(position);
    }
    public class ImageAdapter extends BaseAdapter
    {
        Context ctx;
        int itemBackground;
        List<String> imagesList;
        int pos;
        public ImageAdapter(Context ctx,List<String> imagesList,int pos)
        {
            this.ctx = ctx;
            this.imagesList=imagesList;
            this.pos=pos;
        }
        public int getCount()
        {
            return imagesList.size();
        }
        public Object getItem(int position)
        {
            return position;
        }
        public long getItemId(int position)
        {
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView imageView=new ImageView(ctx);
            //imageView.setImageResource(imageIDs[position]);
            Picasso.with(ctx)
                    .load(imagesList.get(position))
                    .placeholder(R.drawable.loading)
                    .into(imageView);
            //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new Gallery.LayoutParams(250,300));
            imageView.setPadding(0,50,50,0);
            return imageView;
        }
    }
}