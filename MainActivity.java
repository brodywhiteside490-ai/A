package com.example.amongushostmenu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int OVERLAY_REQUEST = 451;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(32, 40, 32, 32);
        page.setBackgroundColor(0xFF151923);

        TextView title = new TextView(this);
        title.setText("AMONG US  •  HOST MENU");
        title.setTextColor(0xFFFFFFFF);
        title.setTextSize(22);
        title.setGravity(Gravity.CENTER);
        page.addView(title, matchWrap());

        TextView note = new TextView(this);
        note.setText("\nFloating launcher and menu prototype. It does not alter the game or control other players. Host mode is a local setting, not automatic host detection.\n");
        note.setTextColor(0xFFCFD5E2);
        note.setTextSize(14);
        page.addView(note, matchWrap());

        Button permission = makeButton("1. Grant overlay permission");
        permission.setOnClickListener(v -> requestOverlayPermission());
        page.addView(permission, matchWrap());

        Button show = makeButton("2. Show floating menu");
        show.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                requestOverlayPermission();
                Toast.makeText(this, "Allow display over other apps, then tap Show floating menu again.", Toast.LENGTH_LONG).show();
                return;
            }
            Intent service = new Intent(this, FloatingMenuService.class);
            if (Build.VERSION.SDK_INT >= 26) startForegroundService(service);
            else startService(service);
            Toast.makeText(this, "Floating button started.", Toast.LENGTH_SHORT).show();
        });
        page.addView(show, matchWrap());

        Button launch = makeButton("Launch Among Us");
        launch.setOnClickListener(v -> {
            Intent game = getPackageManager().getLaunchIntentForPackage("com.innersloth.spacemafia");
            if (game != null) startActivity(game);
            else Toast.makeText(this, "Among Us was not found. Install the Google Play version first.", Toast.LENGTH_LONG).show();
        });
        page.addView(launch, matchWrap());

        Button stop = makeButton("Hide floating menu");
        stop.setOnClickListener(v -> stopService(new Intent(this, FloatingMenuService.class)));
        page.addView(stop, matchWrap());

        setContentView(page);
    }

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_REQUEST);
        } else {
            Toast.makeText(this, "Overlay permission is already allowed.", Toast.LENGTH_SHORT).show();
        }
    }

    private Button makeButton(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setAllCaps(false);
        b.setTextColor(0xFFFFFFFF);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFB92836));
        return b;
    }

    private LinearLayout.LayoutParams matchWrap() {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.bottomMargin = 16;
        return p;
    }
}
