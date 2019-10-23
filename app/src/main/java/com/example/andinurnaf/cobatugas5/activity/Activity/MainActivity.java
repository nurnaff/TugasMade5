package com.example.andinurnaf.cobatugas5.activity.Activity;

import android.app.LoaderManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.content.Loader;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andinurnaf.cobatugas5.activity.R;
import com.example.andinurnaf.cobatugas5.activity.Adapter.RecyclerViewAdapter;
import com.example.andinurnaf.cobatugas5.activity.Entity.Movie;
import com.example.andinurnaf.cobatugas5.activity.Entity.MyAsyncTaskLoader;
import com.example.andinurnaf.cobatugas5.activity.Widget.AlarmReceiver;
import com.example.andinurnaf.cobatugas5.activity.Widget.UpdateWidgetService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>>, MyAsyncTaskLoader.MoviesLoadingListener{

    DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private SearchView searchView;

    List<Movie> movies = new ArrayList<Movie>();
    @BindView(R.id.pbSearch) ProgressBar pbSearch;

    @BindView(R.id.btNext) Button btNext;
    @BindView(R.id.btPrev) Button btPrev;
    @BindView(R.id.tvFound) TextView tvFound;

    @BindView(R.id.rvMovieList) RecyclerView rvMovieList;
    RecyclerViewAdapter adapter;

    String keyword = "";

    int total_results, total_pages, page;

    // notification purposes
    private AlarmReceiver alarmReceiver;
    Switch notifswitch;
    MenuItem mNotif;
    private boolean notificationChecked = false;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initDrawerLayout();

        String starprev = String.format( getResources().getString(R.string.previous), 20);
        btPrev.setText(starprev);

        final LinearLayoutManager rvLayoutManager = new LinearLayoutManager(this);
        rvMovieList.setLayoutManager(rvLayoutManager);
        adapter = new RecyclerViewAdapter(this, movies);
        rvMovieList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        alarmReceiver = new AlarmReceiver();

        rvMovieList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean loading = true;
            int pastVisiblesItems, visibleItemCount, totalItemCount;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = rvLayoutManager.getChildCount();
                totalItemCount = rvLayoutManager.getItemCount();
                pastVisiblesItems = rvLayoutManager.findFirstVisibleItemPosition();

                int lastitem = visibleItemCount + pastVisiblesItems;

                if ( lastitem == totalItemCount
                        && page < total_pages
                        && !keyword.equals("") ) {

                    String starnext;
                    if (page == total_pages - 1) {
                        int last = total_results - (page * 20);
                        starnext = String.format(getResources().getString(R.string.next), last);
                    } else {
                        starnext = String.format(getResources().getString(R.string.next), 20);
                    }
                    btNext.setText(starnext);
                    btNext.setVisibility(View.VISIBLE);
                } else {
                    btNext.setVisibility(View.INVISIBLE);
                }

                if(pastVisiblesItems == 0 &&  page > 1){
                    btPrev.setVisibility(View.VISIBLE);
                } else {
                    btPrev.setVisibility(View.INVISIBLE);
                }

            }
        });

        btPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSearch(page - 1);
            }
        });
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSearch(page + 1);
            }
        });


        if( savedInstanceState != null ){
            movies = savedInstanceState.getParcelableArrayList("movielist");
            adapter.setMovieList( movies );
            adapter.notifyDataSetChanged();
        } else {
            doSearch(page);
        }


//        startJob();

    }


    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int i, Bundle bundle) {
        String keyword = "";
        int page = 1;
        if( bundle != null) {
            keyword = bundle.getString("keyword");
            page = bundle.getInt("page");
        }
        return new MyAsyncTaskLoader(this,keyword,page,this);
    }

        public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {
        this.movies = movies;
        adapter.setMovieList( movies );
        adapter.notifyDataSetChanged();
        pbSearch.setVisibility(View.INVISIBLE);

        if(page == 1) btPrev.setVisibility(View.INVISIBLE);
        rvMovieList.smoothScrollToPosition(0);
    }

    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        adapter.setMovieList( movies );
    }

    @Override
    public void onMoviesLoaded(final int total_results, final int total_pages, final int page) {

        this.total_results = total_results;
        this.total_pages = total_pages;
        this.page = page;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String txt = String.format(getString(R.string.recomended));
                if( !keyword.equals("") ){
                    if( keyword.equals( "_np" ) ) {
                        txt = String.format(getString(R.string.nowplaying), total_results, page, total_pages );
                    }
                    else if( keyword.equals( "_up" ) ) {
                        txt = String.format(getString(R.string.upcoming), total_results, page, total_pages );
                    }
                    else {
                        txt = String.format(getString(R.string.found), total_results, page, total_pages);
                    }
                }
                tvFound.setText(Html.fromHtml(txt));
            }
        });

    }

    private void doSearch(int page){
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        bundle.putInt("page", page);
        getLoaderManager().restartLoader(0, bundle, MainActivity.this);
        pbSearch.setVisibility(View.VISIBLE);
    }

    private void initDrawerLayout() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.myDrawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = (NavigationView)findViewById(R.id.my_navigation);

        Menu searchmenu = navigationView.getMenu();
        searchmenu.findItem(R.id.mSearch).setVisible(false);

        mNotif = navigationView.getMenu().findItem(R.id.mNotif);
        notifswitch = (Switch) mNotif.getActionView().findViewById(R.id.notifswitch);
        notifswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                notificationChecked = !mNotif.isChecked();
                notifswitch.setChecked(notificationChecked);
                mNotif.setChecked(notificationChecked);
                setNotification();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch ( item.getItemId() ){
                    case R.id.mRecomended:
                        keyword = "";
                        doSearch(1);
                        break;

                    case R.id.mUpcoming:
                        keyword = "_up";
                        doSearch(1);
                        break;

                    case R.id.mNowPlaying:
                        keyword = "_np";
                        doSearch(1);
                        break;

                    case R.id.mFavorit:
                        Intent fintent = new Intent(MainActivity.this, FavoriteActivity.class);
                        startActivity(fintent);
                        break;

                    case R.id.mLang:
                        Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                        startActivity(intent);
                        break;

                    case R.id.mNotif:
                        Log.d(">>_NOTIF CLICKED", "TRUE");
                        notificationChecked = !item.isChecked();
                        notifswitch.setChecked(notificationChecked);
                        break;
                }

                return false;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);

        MenuItem search = menu.findItem(R.id.mSearch);
        searchView = (SearchView) search.getActionView();

        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String keyword) {
                MainActivity.this.keyword = keyword;
                MainActivity.this.doSearch(1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String keyword) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkable = menu.findItem(R.id.mNotif);
        checkable.setChecked(notificationChecked);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movielist", new ArrayList<Movie>( adapter.getMovieList()));
        super.onSaveInstanceState(outState);
    }

    // JobScheduler
    private static int jobId = 100;
    private static int PERIODE_SCHEDULER = 10000;

    private void startJob(){
        ComponentName mServiceComponent = new ComponentName(this, UpdateWidgetService.class);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, mServiceComponent);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);
        builder.setPeriodic(PERIODE_SCHEDULER);
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());

        Toast.makeText(this, "Job Service started", Toast.LENGTH_SHORT).show();
    }

    private void setNotification(){
        if(notificationChecked){

            Log.d(">>_NOTIF CHECKED", String.valueOf(notificationChecked));
            alarmReceiver.setNotificationAlarm(MainActivity.this,  "daily");
            alarmReceiver.setNotificationAlarm(MainActivity.this,  "nowplaying");

        } else {

            Log.d(">>_NOTIF CHECKED", String.valueOf(notificationChecked));
            alarmReceiver.cancelNotification(MainActivity.this);

        }
    }


}