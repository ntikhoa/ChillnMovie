package com.ntikhoa.chillnmovie.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ntikhoa.chillnmovie.R;

public class CommentFragment extends Fragment {

    private OnClickSubmit onClickSubmit;

    private Button btnSubmit;
    private EditText editTextComment;

    public void setOnClickSubmit(OnClickSubmit onClickSubmit) {
        this.onClickSubmit = onClickSubmit;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_comment, container, false);
        initComponent(root);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubmit.onClick();
            }
        });

        return root;
    }

    private void initComponent(View root) {
        btnSubmit = root.findViewById(R.id.btnSubmit);
        editTextComment = root.findViewById(R.id.editTextComment);
    }

    public String getComment() {
        return editTextComment.getText().toString();
    }


    interface OnClickSubmit {
        void onClick();
    }
}