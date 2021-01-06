package com.ntikhoa.chillnmovie.view;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ntikhoa.chillnmovie.R;

public class PreviewFragment extends Fragment {

    private static final String URI = "uri";
    private static final String MODE = "mode";
    public static final int POSTER = 0;
    public static final int BACKDROP = 1;

    private onClickListener onClickListener;

    private Uri uri;
    private int mode;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_preview, container, false);
        ImageView preview = root.findViewById(R.id.imageViewPreview);
        Button btnSubmit = root.findViewById(R.id.btnSubmit);
        if (mode == POSTER) {
            preview.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
        preview.setImageURI(uri);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(mode);
            }
        });
        return root;
    }

    interface onClickListener {
        void onClick(int mode);
    }
}