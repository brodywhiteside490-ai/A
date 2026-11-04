package com.example.amongushostmenu;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FloatingMenuService extends Service {
    private WindowManager windowManager;
    private View bubble;
    private View panel;
    private boolean panelVisible = false;
    private boolean hostMode = false;
    private int overlayType;

    @Override
    public void onCreate() {
        super.onCreate();
        startAsForeground();
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            stopSelf();
            return;
        }
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        overlayType = Build.VERSION.SDK_INT >= 26
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_PHONE;
        createBubble();
    }

    private void startAsForeground() {
        String channel = "amongus_menu_channel";
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel nc = new NotificationChannel(channel,
                    "Floating menu", NotificationManager.IMPORTANCE_LOW);
            ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(nc);
        }
        Intent open = new Intent(this, MainActivity.class);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= 23) flags |= PendingIntent.FLAG_IMMUTABLE;
        PendingIntent pi = PendingIntent.getActivity(this, 0, open, flags);
        Notification.Builder nb = Build.VERSION.SDK_INT >= 26
                ? new Notification.Builder(this, channel)
                : new Notification.Builder(this);
        Notification n = nb.setContentTitle("Among Us Host Menu")
                .setContentText("Floating menu is running")
                .setSmallIcon(android.R.drawable.ic_menu_view)
                .setContentIntent(pi).build();
        startForeground(7, n);
    }

    private WindowManager.LayoutParams params(int w, int h, int gravity) {
        return new WindowManager.LayoutParams(w, h, overlayType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
    }

    private void createBubble() {
        Button b = new Button(this);
        b.setText("AU");
        b.setTextColor(Color.WHITE);
        b.setTextSize(14);
        b.setTypeface(null, Typeface.BOLD);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFCF2638));
        WindowManager.LayoutParams p = params(62, 62, Gravity.TOP | Gravity.LEFT);
        p.gravity = Gravity.TOP | Gravity.LEFT;
        p.x = 18; p.y = 220;
        b.setOnClickListener(v -> togglePanel());
        b.setOnTouchListener(new View.OnTouchListener() {
            int startX, startY;
            float downX, downY;
            boolean moved;
            public boolean onTouch(View v, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    startX = p.x; startY = p.y;
                    downX = e.getRawX(); downY = e.getRawY();
                    moved = false; return false;
                } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
                    int dx = (int)(e.getRawX() - downX);
                    int dy = (int)(e.getRawY() - downY);
                    if (Math.abs(dx) + Math.abs(dy) > 12) moved = true;
                    if (moved) {
                        p.x = startX + dx; p.y = startY + dy;
                        windowManager.updateViewLayout(b, p);
                    }
                    return moved;
                }
                return false;
            }
        });
        bubble = b;
        windowManager.addView(bubble, p);
    }

    private void togglePanel() {
        if (panelVisible) {
            if (panel != null) windowManager.removeView(panel);
            panel = null; panelVisible = false;
        } else {
            createPanel();
        }
    }

    private void createPanel() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(18, 14, 18, 14);
        box.setBackgroundColor(0xF21A1F2B);

        TextView heading = new TextView(this);
        heading.setText("HOST MENU  •  LOCAL UI");
        heading.setTextColor(Color.WHITE);
        heading.setTextSize(16);
        heading.setTypeface(null, Typeface.BOLD);
        box.addView(heading);

        TextView warning = new TextView(this);
        warning.setText("Prototype only: these switches do not change Among Us.");
        warning.setTextColor(0xFFFFD4D4);
        warning.setTextSize(11);
        box.addView(warning);

        Button host = item("Host mode: OFF  (tap to toggle)");
        host.setOnClickListener(v -> {
            hostMode = !hostMode;
            host.setText("Host mode: " + (hostMode ? "ON" : "OFF") + "  (local setting)");
            Toast.makeText(this, "Local host-mode setting changed. Real host status is not detected.", Toast.LENGTH_SHORT).show();
        });
        box.addView(host);

        addInfoButton(box, "Role settings", "Requires an authorized private-game mod/API.");
        addInfoButton(box, "Cooldown settings", "Requires an authorized private-game mod/API.");
        addInfoButton(box, "Role overview", "Not connected to game data.");
        addInfoButton(box, "Visibility / movement", "No game changes are made by this overlay.");
        addInfoButton(box, "Player management", "No players are kicked or modified.");
        Button close = item("Close menu");
        close.setOnClickListener(v -> togglePanel());
        box.addView(close);

        WindowManager.LayoutParams p = params(
                (int)(getResources().getDisplayMetrics().widthPixels * 0.78f),
                WindowManager.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.RIGHT);
        p.gravity = Gravity.TOP | Gravity.RIGHT;
        p.x = 16; p.y = 110;
        panel = box;
        windowManager.addView(panel, p);
        panelVisible = true;
    }

    private void addInfoButton(LinearLayout parent, String label, String message) {
        Button b = item(label);
        b.setOnClickListener(v -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
        parent.addView(b);
    }

    private Button item(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setAllCaps(false);
        b.setTextSize(12);
        b.setTextColor(Color.WHITE);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF343B4B));
        return b;
    }

    @Override
    public void onDestroy() {
        if (windowManager != null) {
            if (panel != null) windowManager.removeView(panel);
            if (bubble != null) windowManager.removeView(bubble);
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}
