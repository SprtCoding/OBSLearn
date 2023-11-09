package com.sprtcoding.obslearn.UserMenu;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sprtcoding.obslearn.R;
import com.sprtcoding.obslearn.UserMenu.Calculator.AOGCalculator;
import com.sprtcoding.obslearn.UserMenu.UsersChildMenu.ExamCategories;

public class HomeFragment extends Fragment {
    View view;
    private CardView _examCardView, _statisticsCardView, _aboutCardView, _profileCardView, _obstetricsCardView, _calculatorCardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        _init();

        _examCardView.setOnClickListener(view1 -> {
            Intent gotoExams = new Intent(getContext(), ExamCategories.class);
            startActivity(gotoExams);
        });

        _statisticsCardView.setOnClickListener(view1 -> {
            replaceFragment(new StatisticFragment());
        });

        _aboutCardView.setOnClickListener(view1 -> {
            replaceFragment(new AboutFragment());
        });

        _profileCardView.setOnClickListener(view1 -> {
            replaceFragment(new ProfileFragment());
        });

        _obstetricsCardView.setOnClickListener(view1 -> {
            replaceFragment(new LearnFragment());
        });

        _calculatorCardView.setOnClickListener(view1 -> {
            Intent gotoExams = new Intent(getContext(), AOGCalculator.class);
            startActivity(gotoExams);
        });

        return view;
    }

    private void _init() {
        _examCardView = view.findViewById(R.id.examCardView);
        _statisticsCardView = view.findViewById(R.id.statisticsCardView);
        _aboutCardView = view.findViewById(R.id.aboutCardView);
        _profileCardView = view.findViewById(R.id.profileCardView);
        _obstetricsCardView = view.findViewById(R.id.obstetricsCardView);
        _calculatorCardView = view.findViewById(R.id.calculatorCardView);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}