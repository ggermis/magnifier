package org.codenut.app.magnifier;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.ImageView;

import java.io.File;

public class ImagePreviewFragment extends Fragment {
    private static final String PARAM_PREVIEW_FILE = "preview.file";

    public static ImagePreviewFragment newInstance(final File file) {
        ImagePreviewFragment f = new ImagePreviewFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAM_PREVIEW_FILE, file);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.image_preview_fragment, container, false);

        final File file = (File) getArguments().getSerializable(PARAM_PREVIEW_FILE);

        final ImageView preview = (ImageView) v.findViewById(R.id.preview);
        preview.setScaleType(ImageView.ScaleType.MATRIX);
        preview.setOnTouchListener(new ImagePreviewGestures());

        new AsyncTask<File, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(File... params) {
                WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                return BitmapUtil.decodeSampledBitmapFromFile(file.getPath(), display.getWidth(), display.getHeight());
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                preview.setImageBitmap(bitmap);
            }
        }.execute(file);

        return v;
    }
}
