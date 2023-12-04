package com.sprtcoding.obslearn.UserMenu;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sprtcoding.obslearn.R;
import com.sprtcoding.obslearn.UserMenu.Module.ClinicVisitModule;
import com.sprtcoding.obslearn.UserMenu.Module.DangerSignOfPregnancy;
import com.sprtcoding.obslearn.UserMenu.Module.MinorSignOfPregnancy;
import com.sprtcoding.obslearn.UserMenu.Module.NutritionDuringPregnancyModule;
import com.sprtcoding.obslearn.UserMenu.Module.PrenatalCareModule;
import com.sprtcoding.obslearn.UserMenu.Module.Syllabuls;

public class LearnFragment extends Fragment {
    private CardView parental_care_card, nutrition_card, clinic_visit_card, minor_discomfort_card
    , danger_sign_card, syllabuls_card;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_learn, container, false);
        _init();

        parental_care_card.setOnClickListener(view1 -> {
            Intent i = new Intent(getContext(), PrenatalCareModule.class);
            startActivity(i);
        });

        nutrition_card.setOnClickListener(view1 -> {
            Intent i = new Intent(getContext(), NutritionDuringPregnancyModule.class);
            startActivity(i);
        });

        clinic_visit_card.setOnClickListener(view1 -> {
            Intent i = new Intent(getContext(), ClinicVisitModule.class);
            startActivity(i);
        });

        minor_discomfort_card.setOnClickListener(view1 -> {
            Intent i = new Intent(getContext(), MinorSignOfPregnancy.class);
            startActivity(i);
        });

        danger_sign_card.setOnClickListener(view1 -> {
            Intent i = new Intent(getContext(), DangerSignOfPregnancy.class);
            startActivity(i);
        });

        syllabuls_card.setOnClickListener(view1 -> {
            Intent i = new Intent(getContext(), Syllabuls.class);
            startActivity(i);
        });

        return view;
    }

    private void _init() {
        syllabuls_card = view.findViewById(R.id.syllabuls_card);
        parental_care_card = view.findViewById(R.id.parental_care_card);
        nutrition_card = view.findViewById(R.id.nutrition_card);
        clinic_visit_card = view.findViewById(R.id.clinic_visit_card);
        minor_discomfort_card = view.findViewById(R.id.minor_discomfort_card);
        danger_sign_card = view.findViewById(R.id.danger_sign_card);
    }
}