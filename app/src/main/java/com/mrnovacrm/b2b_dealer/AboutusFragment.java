package com.mrnovacrm.b2b_dealer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrnovacrm.R;
import com.mrnovacrm.db.SharedDB;

import java.util.HashMap;

public class AboutusFragment extends Fragment {
    private String COMPANY;
    private String SHORTFORM;
    public AboutusFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_aboutus, container, false);

        TextView ourvisionval_txt = rootView.findViewById(R.id.ourvisionval_txt);
        TextView ourmissionval_txt = rootView.findViewById(R.id.ourmissionval_txt);
        TextView aboutusval_txt = rootView.findViewById(R.id.aboutusval_txt);
        TextView cmdmessageval_txt = rootView.findViewById(R.id.cmdmessageval_txt);

        if (SharedDB.isLoggedIn(getActivity())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getActivity());
            COMPANY = values.get(SharedDB.COMPANY);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
            try {
                if (COMPANY.equals("1")) {
                    ourvisionval_txt.setText("Nova AgriScience’s vision is to be a global leader in provision of comprehensive agriculture technology solutions while balancing the goals between food security and environment sustainability. The company aims to be recognized as one of the innovators in the segment and be an employer of choice by year 2020.\n");
                    ourmissionval_txt.setText("Nova Agri Science’s mission is to provide a wide range of cutting edge agriput solutions through continued and organized research and focus towards quality and excellence, ultimately leading to an ecologically sustainable, nutritionally balanced and financially profitable farming community.\n");
                    aboutusval_txt.setText("Nova Agri Sciences Pvt. Ltd. belongs to a Group Companies of Nova. The Management of Nova Agri Sciences Pvt. Ltd. consists of Directors with good experience and insight. Admirable Foundation and Passion to win are what set forth our Chairman Mr. Yeluri Sambasiva Rao who unstoppably drives his companies for continuous Reinvention and the Delineation.\n\n Establishment of Nova Started 11 years ago in 2007 making Hyderabad as its base. Nova believed in research which motivated group's vision to broaden its base from small entrepreneurship to a big Agri input base company. Nova's enormity led important and consequential establishment of Nova Agri Tech Pvt Limited which deals with Plant Growth Promotors, Bio-Pesticides, Horticultural Equipment’s, Micro Nutrients, and Water Soluble Fertilizers. One of the potent and influential dimension in to the one more feather of its cap is establishment of Nova Agri Sciences Pvt. Ltd. \n\n Nova Agri Sciences Pvt. Ltd. has a very professionally self-driven team under the able guidance of Mr.Yeluri Sambasiva Rao, Group Chairman taking this new Business to make a Global Presence. Nova Agri Sciences Pvt. Ltd.   dealing with Core Business of Manufacturing and Marketing of various group of Plant Protection Chemicals which required to cater major crops grown in India.\n\n We Nova Agri Sciences Pvt. Ltd. having a list of registered Pesticides are about 170 molecules which are predominant requirement of the Indian Agriculture towards itsCrop Protection. The organisation is looking forward to manufacture and market some exclusive molecules for better protection of important crops of India.\n");
                    cmdmessageval_txt.setText("I Believe that Eco- Friendly, Biological and Organic Agri inputs, when properly managed, have immense potential for improved agricultural production and protection of ecosystem. These in turn have variable positive effects on ecological balance, food and nutrition security, all of which are key prerequisites for attainment of sustainable socio-economic development and human wellbeing.\n");
                } else if (COMPANY.equals("2")) {
                    ourvisionval_txt.setText("Nova Agri tech’s vision is to be a global leader in provision of comprehensive agriculture technology solutions while balancing the goals between food security and environment sustainability. The company aims to be recognized as one of the innovators in the segment and be an employer of choice by year 2020.\n");
                    ourmissionval_txt.setText("Nova Agritech’s mission is to provide a wide range of cutting edge agriput solutions through continued and organized research and focus towards quality and excellence, ultimately leading to an ecologically sustainable, nutritionally balanced and financially profitable farming community.\n");
                    aboutusval_txt.setText("Nova Agritech Pvt ltd, established in 2007, as a research based company with manufacturing facilities located in Hyderabad. The company has won several awards for technical innovation and outstanding performance in the Agri Inputs industry and has many firsts in the field of Bio Stimulants, Micronutrients and Organic Inputs.\n\nThrough vibrant R&D efforts, our innovations has marked the rise of Nova Agritech Pvt Ltd (NATL) from a small unit in 2007 to one of the most successful and reputed companies in the Agro input Industry now. A strong and passionate team in our organization with expertise in industry has capitalized the company to take forward the vision of management.\n\nThis has led to the production of vital substitute products and cost-effective agrochemicals on a large scale and consequently to a strong, competitive national and international presence. And all this has been made possible by the vision and unswerving dedication and commitment of our Team.\n");
                    cmdmessageval_txt.setText("I Believe that Eco-friendly, Biological and Organic Agri inputs, when properly managed, have immense potential for improved agricultural production and protection of ecosystem. These in turn have variable positive effects on ecological balance, food and nutrition security, all of which are key prerequisites for attainment of sustainable socio-economic development and human wellbeing.\n");
                }
            }catch (Exception e)
            {
            }
        }
        return rootView;
    }
}