package com.itaim.launcher;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {

    private PackageManager packageManager;
    private List<ResolveInfo> availableActivities ;
    private List<AppDetail> apps;
    private TableLayout apptable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        apptable = (TableLayout) findViewById(R.id.apptable);
        packageManager = getPackageManager();
        apps = new ArrayList<>();
        availableActivities = packageManager.queryIntentActivities(intent, 0);
        int i = 0;
        for (ResolveInfo ri:availableActivities) {
            AppDetail app = new AppDetail();
            app.label = ri.loadLabel(packageManager);
            app.name = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(packageManager);
           if (!(app.name.toString().equals("com.itaim.launcher"))) {
               apps.add(app);
               TableRow row = new TableRow(this);
               TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
               lp.span = 2;
               row.setLayoutParams(lp);
               ImageView appImg = new ImageView(this);
               TextView appName = new TextView(this);
               appImg.setAdjustViewBounds(true);
               appImg.setMaxHeight(150);
               appImg.setMaxWidth(150);
               appImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
               appName.setText(app.label);
               appImg.setImageDrawable(app.icon);
               row.addView(appImg);
               row.addView(appName);
               row.setTag(i);
               row.setOnClickListener(OpenApp);
               apptable.addView(row);
               i++;
           }
        }
    }
    View.OnClickListener OpenApp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Intent intent = packageManager.getLaunchIntentForPackage(apps.get((int)v.getTag()).name.toString());
                startActivity(intent);
            }catch (Exception e) {
            }
        }
    };

}

class AppDetail {
    CharSequence label;
    CharSequence name;
    Drawable icon;
}

