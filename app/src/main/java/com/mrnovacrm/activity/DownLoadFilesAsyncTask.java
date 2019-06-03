package com.mrnovacrm.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DownLoadFilesAsyncTask extends AsyncTask<String, Void, Void> {

    private Context mContext;
    private boolean isConnectedToInternet;
    ArrayList<String> searchList;
    private String response;
    int status;
    public DownLoadFilesAsyncTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onPreExecute() {
        /* Show ProgressDialog */
    }

    /* Getting RoadNumber and RoadName using highway and avenue urls */
    @Override
    protected Void doInBackground(String... params) {

        try {
            String downnLoadURL = params[0];
            String fileName = params[1];

            String extStorageDirectory = Environment
                    .getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "NOVA");
            folder.mkdir();
            File imageFile = new File(folder, fileName);
            try {
                imageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(downnLoadURL, imageFile);
        } catch (Exception e) {
        }
        return null;
    }

    protected void onPostExecute(Void results) {
        try {

        //    Toast.makeText(mContext, "downloaded successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}