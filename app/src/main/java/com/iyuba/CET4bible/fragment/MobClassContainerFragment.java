package com.iyuba.CET4bible.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iyuba.CET4bible.R;
import com.iyuba.configation.Constant;
import com.iyuba.imooclib.ui.mobclass.MobClassFragment;

import java.util.ArrayList;

public class MobClassContainerFragment extends Fragment {

    private boolean visible = false;
    private boolean isLoaded;
    MobClassFragment fragment;

    public static MobClassContainerFragment newInstance(Bundle args) {
        MobClassContainerFragment fragment = new MobClassContainerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.empty, container, false);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        visible = isVisibleToUser;
        Log.d("diao", visible + "");
        if (visible) {
            if(!isLoaded){
                getChildFragmentManager().beginTransaction().add(R.id.frame_container, fragment).commit();
                isLoaded = true ;
            }else {
                getChildFragmentManager().beginTransaction().show(fragment);
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private MobClassFragment buildMobFragment() {
        ArrayList<Integer> filter = getEnglishFilter();
        Bundle args = MobClassFragment.buildArguments(Integer.parseInt(Constant.APP_CONSTANT.courseTypeId()), false,
                filter);
        return MobClassFragment.newInstance(args);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragment = buildMobFragment();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public static ArrayList<Integer> getEnglishFilter(){
        ArrayList<Integer> filter  = new ArrayList<>();
        for(int i = 0 ; i < 30 ; i++){
            filter.add(i);
        }
        filter.remove((Integer)1);
        filter.remove((Integer)5);
        filter.remove((Integer)6);
        filter.add(61);
        filter.add(91);
        filter.add(52);
        return filter ;
    }
}
