package com.example.mcb.genesisapp.Views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mcb.genesisapp.R;

/**
 * Fragment holding the Obtainer view
 */
public class ObtainerFragment extends Fragment {


    public ObtainerFragment() {
        // Required empty public constructor
    }

    public static ObtainerFragment newInstance(){
        return new ObtainerFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_obtainer, container, false);
    }

}
