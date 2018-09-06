package com.example.camerasample.filters;

import android.content.Context;

import com.example.camerasample.R;

public class LutColorFilter extends BaseFilter {

    public LutColorFilter(Context context) {
        super(context);
    }

    protected  int getFragmentShaderId(){
        return R.raw.fragment_shader_lut;
    }
}
