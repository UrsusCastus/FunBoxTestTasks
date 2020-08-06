package com.example.funboxtest.switchbuttoncontroller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.funboxtest.R;
import com.example.funboxtest.backend.BackEndActivity;

public class SwitchButtonsFragment extends Fragment {
    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_switch_buttons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SwitchButtonController switchButtonController = SwitchButtonController.getSwitchButtonControllerInstance();
        Button buttonStoreFront = view.findViewById(R.id.fragment_switch_buttons_button_store_front);
        Button buttonBackEnd = view.findViewById(R.id.fragment_switch_buttons_button_back_end);

        if (switchButtonController.getStoreFrontActivityRun()) {
            buttonStoreFront.setBackground(mContext.getDrawable(R.drawable.button_pressed_background));
            buttonStoreFront.setClickable(false);
            buttonBackEnd.setBackground(mContext.getDrawable(R.drawable.button_not_pressed_background));
            buttonBackEnd.setOnClickListener(v -> {
                runBackAndActivity();
                switchButtonController.setStoreFrontActivityRun(false);
            });
        } else {
            buttonStoreFront.setBackground(mContext.getDrawable(R.drawable.button_not_pressed_background));
            buttonStoreFront.setOnClickListener(v -> {
                finishBackAndActivity();
                switchButtonController.setStoreFrontActivityRun(true);
            });
            buttonBackEnd.setBackground(mContext.getDrawable(R.drawable.button_pressed_background));
            buttonBackEnd.setClickable(false);
        }
    }

    private void runBackAndActivity() {
        Intent intent = new Intent(getActivity(), BackEndActivity.class);
        startActivity(intent);
    }

    private void finishBackAndActivity() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }
}
