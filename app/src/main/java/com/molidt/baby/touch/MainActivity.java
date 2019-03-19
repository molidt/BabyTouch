package com.molidt.baby.touch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.rxbinding3.view.RxView;
import com.molidt.baby.touch.data.Touch;
import com.molidt.baby.touch.data.db.DbManager;
import com.molidt.baby.touch.utils.TimeUtil;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView rvContent;

    private TouchAdapter adapter;
    private final List<Touch> touchList = new ArrayList<>();
    private TouchReceiver touchReceiver = new TouchReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvContent = findViewById(R.id.rv_content);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TouchAdapter();
        rvContent.setAdapter(adapter);

        fab = findViewById(R.id.fab);
        RxView.clicks(fab)
                .throttleFirst(5, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        Intent intent = new Intent(MainActivity.this, BabyService.class);
                        intent.setAction(BabyService.ACTION_TOUCH);
                        startService(intent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("BABY", throwable.getMessage());
                    }
                });

        Intent intent = new Intent(this, BabyService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BabyService.ACTION_TOUCH_SAVE_SUCCESS);
        registerReceiver(touchReceiver, intentFilter);

        refreshList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(touchReceiver);
    }

    private void refreshList() {
        Observable.just(System.currentTimeMillis())
                .observeOn(Schedulers.io())
                .map(new Function<Long, List<Touch>>() {
                    @Override
                    public List<Touch> apply(Long aLong) throws Exception {
                        return DbManager.get(MainActivity.this).getAllTouch();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<List<Touch>>bindToLifecycle())
                .subscribe(new Consumer<List<Touch>>() {
                    @Override
                    public void accept(List<Touch> touches) throws Exception {
                        touchList.clear();
                        touchList.addAll(touches);
                        adapter.notifyDataSetChanged();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("BABY", throwable.getMessage());
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class TouchHolder extends RecyclerView.ViewHolder {

        private TextView tvTime;
        private Touch data;

        TouchHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
        }

        void setData(Touch data) {
            this.data = data;
            tvTime.setText(TimeUtil.formatTime(data.getTime()));
        }
    }

    private class TouchAdapter extends RecyclerView.Adapter<TouchHolder> {
        @NonNull
        @Override
        public TouchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TouchHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_touch, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TouchHolder holder, int position) {
            Touch touch = touchList.get(position);
            holder.setData(touch);
        }

        @Override
        public int getItemCount() {
            return touchList.size();
        }
    }

    private class TouchReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BabyService.ACTION_TOUCH_SAVE_SUCCESS.equals(action)) {
                refreshList();
                Snackbar.make(fab, getString(R.string.tips_baby_touch), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        }
    }
}
