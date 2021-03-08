package com.ntikhoa.chillnmovie.view;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.FrameLayout;

import com.ntikhoa.chillnmovie.R;
import com.ntikhoa.chillnmovie.databinding.FragmentPreviewBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PreviewFragment extends Fragment {

    private static final String URI = "uri";
    private static final String MODE = "mode";
    public static final int POSTER = 0;
    public static final int BACKDROP = 1;

    private FragmentPreviewBinding binding;

    private onClickListener onClickListener;

    private Uri uri;
    private int mode;

    public PreviewFragment() {
        super(R.layout.fragment_preview);
    }

    public void setOnClickListener(PreviewFragment.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static PreviewFragment newInstance(Uri uri, int mode) {
        PreviewFragment fragment = new PreviewFragment();
        Bundle args = new Bundle();
        args.putString(URI, uri.toString());
        args.putInt(MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uri = Uri.parse(getArguments().getString(URI));
            mode = getArguments().getInt(MODE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentPreviewBinding.bind(view);

        if (mode == POSTER) {
            binding.imageViewPreview.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
        binding.imageViewPreview.setImageURI(uri);
        binding.btnSubmit.setOnClickListener(v -> onClickListener.onClick(mode));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    interface onClickListener {
        void onClick(int mode);
    }
}