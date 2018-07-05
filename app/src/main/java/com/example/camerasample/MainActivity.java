package com.example.camerasample;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ListActivity {
    private static final String TITLE = "title";
    private static final String CLASS_NAME = "class_name";


    private String[][] content = {
            {
                    "CameraTextureViewDemo",
                    "CameraTextureViewActivity",
            },
            {
                    "CameraSurfaceTextureDemo",
                    "CameraTextureSurfaceActivity"
            },
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setListAdapter(new SimpleAdapter(this, getActivitList(), android.R.layout.simple_list_item_1, new String[]{TITLE},
                new int[]{android.R.id.text1}));
    }

    private List getActivitList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (String[] c : content) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(TITLE, c[0]);

            Intent intent = new Intent();
            Class cla = null;
            try {
                cla = Class.forName("com.example.camerasample." + c[1]);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            intent.setClass(this, cla);
            map.put(CLASS_NAME, intent);
            list.add(map);
        }

        return list;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
        Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);
        Intent intent = (Intent) map.get(CLASS_NAME);
        startActivity(intent);

    }
}
