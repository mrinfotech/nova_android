package com.mrnovacrm.b2b_dealer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.CategoryDTO;
import com.mrnovacrm.model.CategoryListDTO;
import com.mrnovacrm.model.SubCategoryListDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by android on 15-03-2018.
 */

public class ProductCategoriesFragment extends Fragment {

    private ExpandableListView expListView;
    ExpandableListAdapter listAdapter;
    private int screen_orientaiton;
    private boolean isConnectedToInternet;
    private ArrayList<String> groupList;
    ArrayList<String> categoryNamesList = new ArrayList<String>();
    Map<String, List<String>> itemsCollection = new LinkedHashMap<String, List<String>>();
    Map<String, List<String>> iditemsCollection = new LinkedHashMap<String, List<String>>();
    private ArrayList<String> groupIdsList = new ArrayList<>();
    String categoryID="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_productcategory_expandablelist, container, false);
        expListView = rootView.findViewById(R.id.category_list);
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        GlobalShare globalShare=(GlobalShare)getActivity().getApplicationContext();
        if(globalShare!=null) {
            categoryID=globalShare.getSelectCategoryId();
        }

        screen_orientaiton = getResources().getConfiguration().orientation;
        if (getResources().getBoolean(R.bool.normalscreen)) {

            if (screen_orientaiton == Configuration.ORIENTATION_PORTRAIT) {
                if (width >= 1080) {

                    (new Handler()).post(new Runnable() {
                        @Override
                        public void run() {
                            expListView.setIndicatorBounds(expListView.getRight() - 100, expListView.getWidth());
                        }
                    });

                } else {
                    if (width >= 720) {
                        (new Handler()).post(new Runnable() {
                            @Override
                            public void run() {
                                expListView.setIndicatorBounds(expListView.getRight() - 120, expListView.getWidth());
                            }
                        });
                    } else {

                        (new Handler()).post(new Runnable() {
                            @Override
                            public void run() {
                                expListView.setIndicatorBounds(expListView.getRight() - 90, expListView.getWidth());
                            }
                        });
                    }
                }
            }
        } else if (getResources().getBoolean(R.bool.seveninchscreen)) {
            if (screen_orientaiton == Configuration.ORIENTATION_PORTRAIT) {

                (new Handler()).post(new Runnable() {
                    @Override
                    public void run() {
                        expListView.setIndicatorBounds(expListView.getRight() - 100, expListView.getWidth());
                    }
                });
            }
        } else if (getResources().getBoolean(R.bool.teninchscreen)) {
            if (screen_orientaiton == Configuration.ORIENTATION_PORTRAIT) {

                (new Handler()).post(new Runnable() {
                    @Override
                    public void run() {
                        expListView.setIndicatorBounds(expListView.getRight() - 100, expListView.getWidth());
                    }
                });
            }
        }

        isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getActivity());
        if (isConnectedToInternet) {
            getCategoriesList();
        }
        createGroupList();
        return rootView;
    }

    private void createGroupList() {
        groupList = new ArrayList<String>();
        groupList.clear();
        groupList = categoryNamesList;
    }

    public void getCategoriesList() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<CategoryDTO> mService = mApiService.categoriesList(categoryID);
        mService.enqueue(new Callback<CategoryDTO>() {
            @Override
            public void onResponse(@NonNull Call<CategoryDTO> call, @NonNull Response<CategoryDTO> response) {
                CategoryDTO mstoresObject = response.body();
                dialog.dismiss();
                try {
                    Log.e("response",""+response);
                    assert mstoresObject != null;
                    String status = mstoresObject.getStatus();
                    Log.e("status", "" + status);
                    if (status.equals("1")) {
                        categoryNamesList.clear();
                        List<CategoryListDTO> categoryDTOList = mstoresObject.getCategoryList();
                        if (categoryDTOList != null && categoryDTOList.size() > 0) {

                            ArrayList<ArrayList<HashMap<String, String>>> viewpagersubcategoryhashmaplist
                                    = new ArrayList<>();

                            ArrayList<HashMap<String, String>> categoryhashmapList = new ArrayList<HashMap<String, String>>();
                            for (int i = 0; i < categoryDTOList.size(); i++) {
                                HashMap<String, String> categoryhashMap = new HashMap<String, String>();
                                ArrayList<HashMap<String, String>> subcategoryhashmaplist = new ArrayList<HashMap<String, String>>();

                                ArrayList<String> subcategorynameslist = new ArrayList<>();
                                ArrayList<String> subcategoryidslist = new ArrayList<>();

                                String category_id = categoryDTOList.get(i).getId();
                                String category_name = categoryDTOList.get(i).getCategory();

                                groupIdsList.add(category_id);

                                List<SubCategoryListDTO> subCategoryList = categoryDTOList.get(i).getSubcategoryList();
                                if (subCategoryList != null && subCategoryList.size() > 0) {

                                    for (int j = 0; j < subCategoryList.size(); j++) {
                                        HashMap<String, String> subCategoryHashMap = new HashMap<String, String>();
                                        String subcategory_id = subCategoryList.get(j).getId();
                                        String subcategory_name = subCategoryList.get(j).getCategory();
                                        subCategoryHashMap.put("subcategory_id", subcategory_id);
                                        subCategoryHashMap.put("subcategory_name", subcategory_name);
                                        subcategoryhashmaplist.add(subCategoryHashMap);
                                        subcategoryidslist.add(subcategory_id);
                                        subcategorynameslist.add(subcategory_name);
                                    }
                                    viewpagersubcategoryhashmaplist.add(subcategoryhashmaplist);
                                }
                                categoryhashMap.put("id", category_id);
                                categoryhashMap.put("category_name", category_name);
                                categoryhashmapList.add(categoryhashMap);

                                categoryNamesList.add(category_name);
                                itemsCollection.put(category_name, subcategorynameslist);
                                iditemsCollection.put(category_id, subcategoryidslist);

                            }
                            showList();
                        }
                    } else {

                        String message = mstoresObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<CategoryDTO> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
              //  Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void showList() {
        listAdapter = new ExpandableListAdapter(groupList, itemsCollection);
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String selected = (String) listAdapter.getChild(
                        groupPosition, childPosition);

                String groupName = groupList.get(groupPosition);

                List<String> subNamesList = new ArrayList<String>();
                subNamesList = itemsCollection.get(groupName);

                String groupID = groupIdsList.get(groupPosition);

                List<String> subCatIdList = new ArrayList<String>();
                subCatIdList = iditemsCollection.get(groupID);
                Intent intent = new Intent(getActivity(), ProductListActivity.class);
                intent.putStringArrayListExtra("subcatId", (ArrayList<String>) subCatIdList);
                intent.putStringArrayListExtra("subcatName", (ArrayList<String>) subNamesList);
                intent.putExtra("groupName",groupName);
                intent.putExtra("childPosition", childPosition);
                intent.putExtra("categoryID", categoryID);
                startActivity(intent);
                return true;
            }
        });
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        // private Activity context;
        private Map<String, List<String>> groupItemsCollections;
        private List<String> groupitems;
        private GlobalShare globalShare;
        private TextView act_favtxt;

        int count = 0;
        private HashMap<Integer, boolean[]> mChildCheckStates;
        private ChildViewHolder childViewHolder;
        ArrayList<String> al;
        HashMap<String, String> hashMap = new HashMap<String, String>();

        public ExpandableListAdapter(List<String> groupitems,

                                     Map<String, List<String>> groupItemsCollections) {
            // this.context = context;
            this.groupItemsCollections = groupItemsCollections;
            this.groupitems = groupitems;
            mChildCheckStates = new HashMap<Integer, boolean[]>();
            al = new ArrayList<String>();
        }

        public Object getChild(int groupPosition, int childPosition) {
            return groupItemsCollections.get(groupitems.get(groupPosition)).get(
                    childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public View getChildView(final int groupPosition,
                                 final int childPosition, boolean isLastChild, View convertView,
                                 ViewGroup parent) {
            final String childitem = (String) getChild(groupPosition,
                    childPosition);

            final int mGroupPosition = groupPosition;
            final int mChildPosition = childPosition;
            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item, null);

                childViewHolder = new ChildViewHolder();

                childViewHolder.lblListItem = (TextView) convertView
                        .findViewById(R.id.lblListItem);
                convertView.setTag(R.layout.list_item, childViewHolder);
            } else {
                childViewHolder = (ChildViewHolder) convertView
                        .getTag(R.layout.list_item);
            }
            childViewHolder.lblListItem.setText(childitem);
            return convertView;
        }

        public int getChildrenCount(int groupPosition) {
            return groupItemsCollections.get(groupitems.get(groupPosition)).size();
        }

        public Object getGroup(int groupPosition) {
            return groupitems.get(groupPosition);
        }

        public int getGroupCount() {
            return groupitems.size();
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {

            String headerName = (String) getGroup(groupPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }
            TextView item = (TextView) convertView.findViewById(R.id.lblListHeader);
            item.setTypeface(null, Typeface.BOLD);
            item.setText(headerName);
            return convertView;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        public final class ChildViewHolder {

            TextView lblListItem;
        }
    }


////        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
//        expListView.setAdapter(listAdapter);
//
//    // Listview Group click listener
//        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//
//        @Override
//        public boolean onGroupClick(ExpandableListView parent, View v,
//        int groupPosition, long id) {
//            // Toast.makeText(getApplicationContext(),
//            // "Group Clicked " + listDataHeader.get(groupPosition),
//            // Toast.LENGTH_SHORT).show();
//            return false;
//        }
//    });
//
//    // Listview Group expanded listener
//        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//        int lastExpandedPosition = -1;
//
//        @Override
//        public void onGroupExpand(int groupPosition) {
//
//            if (lastExpandedPosition != -1
//                    && groupPosition != lastExpandedPosition) {
//                expListView.collapseGroup(lastExpandedPosition);
//            }
//            lastExpandedPosition = groupPosition;
//        }
//    });
//
//    // Listview Group collasped listener
//        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//
//        @Override
//        public void onGroupCollapse(int groupPosition) {
//
//
//        }
//    });
//
//    // Listview on child click listener
//        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//
//        @Override
//        public boolean onChildClick(ExpandableListView parent, View v,
//        int groupPosition, int childPosition, long id) {
//            // TODO Auto-generated method stub
//
//            Intent intent = new Intent(getActivity(), ProductListActivity.class);
//            startActivity(intent);
//
//            return false;
//        }
//    });

}
