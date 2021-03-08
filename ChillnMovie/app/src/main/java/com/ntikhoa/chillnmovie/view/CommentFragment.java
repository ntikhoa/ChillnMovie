package com.ntikhoa.chillnmovie.view;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.databinding.FragmentCommentBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CommentFragment extends Fragment {

    private OnClickSubmit onClickSubmit;

    private FragmentCommentBinding binding;

    public void setOnClickSubmit(OnClickSubmit onClickSubmit) {
        this.onClickSubmit = onClickSubmit;
    }

    public CommentFragment() {
        super(R.layout.fragment_comment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentCommentBinding.bind(view);

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubmit.onClick();
            }
        });
    }

    public String getComment() {
        return binding.editTextComment.getText().toString();
    }


    interface OnClickSubmit {
        void onClick();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}