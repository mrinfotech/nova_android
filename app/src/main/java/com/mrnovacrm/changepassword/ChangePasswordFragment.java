package com.mrnovacrm.changepassword;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Login;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by android on 08-03-2018.
 */

public class ChangePasswordFragment extends Fragment implements View.OnClickListener {

    Context context;
    private boolean isConnectedToInternet;
    EditText oldpasswordedittext,newenterpwdedittext,reenterpwd;
    Button submitbtn;
    private String oldPassword = "";
    private String newPassword = "";
    private String reEnterNewPwd = "";
    private HashMap<String, String> values;
    private String PRIMARYID="";
    private String USERTYPE="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_changepassword, container, false);
        context=getActivity();
        oldpasswordedittext=rootView.findViewById(R.id.oldpasswordedittext);
        newenterpwdedittext=rootView.findViewById(R.id.newenterpwdedittext);
        reenterpwd=rootView.findViewById(R.id.reenterpwd);
        submitbtn=rootView.findViewById(R.id.submitbtn);
        submitbtn.setOnClickListener(ChangePasswordFragment.this);

        if(SharedDB.isLoggedIn(getActivity()))
        {
            values=SharedDB.getUserDetails(getActivity());
            PRIMARYID=values.get(SharedDB.PRIMARYID);
            USERTYPE=values.get(SharedDB.USERTYPE);
        }
        return rootView;
    }

    @Override
    public void onClick(View view) {
        oldPassword = oldpasswordedittext.getText().toString();
        newPassword = newenterpwdedittext.getText().toString();
        reEnterNewPwd = reenterpwd.getText().toString();

        if (oldPassword == null || "".equalsIgnoreCase(oldPassword)
                || oldPassword.equals("") || newPassword == null
                || "".equalsIgnoreCase(newPassword) || newPassword.equals("")
                || reEnterNewPwd == null || "".equalsIgnoreCase(reEnterNewPwd)
                || reEnterNewPwd.equals("")) {
            Toast.makeText(getActivity(), "All fields are mandatory",
                    Toast.LENGTH_LONG).show();
        } else {
            if (newPassword.equals(reEnterNewPwd)) {
                if (!oldPassword.equals(newPassword)) {
                    showValidtaion();
                } else {
                    Toast.makeText(getActivity(),
                            "Old Password and New password should not be same",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(),
                        "New password and Re-enter password not match",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void showValidtaion() {

        isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(context);
        if (isConnectedToInternet) {
            final TransparentProgressDialog dialog = new TransparentProgressDialog(context);
            dialog.show();

            RetrofitAPI mApiService = SharedDB.getInterfaceService();
            Call<Login> mService = mApiService.changePassword(PRIMARYID,USERTYPE,oldPassword,newPassword);
            mService.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    dialog.dismiss();
                    try {
                        Login mobject = response.body();
                        String status = mobject.getStatus();

                        Log.e("status",""+status);

                        if (status.equals("1")) {
                            String message=mobject.getMessage();
                            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                            oldpasswordedittext.setText("");
                            newenterpwdedittext.setText("");
                            reenterpwd.setText("");
                        }else if (status.equals("2")) {
                            String message=mobject.getMessage();
                            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                        } else {
                            String message=mobject.getMessage();
                            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        dialog.dismiss();
                    }
                }
                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    call.cancel();
                    dialog.dismiss();
                    Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, R.string.networkerror, Toast.LENGTH_SHORT).show();
        }
    }
}