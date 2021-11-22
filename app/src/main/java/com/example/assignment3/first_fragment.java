package com.example.assignment3;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class first_fragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      View v =  inflater.inflate(R.layout.first_fragment,container,false);

        TextView question = v.findViewById(R.id.first_fragmentText);
        Bundle extras = getArguments();
        question.setText(extras.getInt("question"));
        return v;

    }
}
