package com.demo.kawatask.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.demo.kawatask.Adapter.ImageSliderAdapter;
import com.demo.kawatask.R;
import com.demo.kawatask.model.Result;
import com.demo.kawatask.model.UserModel;
import com.demo.kawatask.utils.Connectivity;
import com.demo.kawatask.utils.ServiceManager;
import com.demo.kawatask.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    String msg, title;
    List<Result> USERDETAILS = new ArrayList<>();
    ViewPager viewPager;
    ImageSliderAdapter imageSliderAdapter;
    ImageView leftNav, rightNav;
    RecyclerView userListRecyclerView;
    private LinearLayoutManager layoutManager;
    private UserAdapter userAdapter;
    private Callback callBack;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webservicecall();
        viewPager = findViewById(R.id.viewpager);
        leftNav = findViewById(R.id.left_nav);
        rightNav = findViewById(R.id.right_nav);
        leftNav.setOnClickListener(viewV -> leftNavigaion());
        rightNav.setOnClickListener(viewV -> rightNavigaion());
        userListRecyclerView = findViewById(R.id.userListRecyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        userListRecyclerView.setLayoutManager(layoutManager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.hamburger_icon));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                userAdapter.rowindex = position;   //for change the card position
                userAdapter.notifyDataSetChanged(); // for refresh the list because on select and unselct
                userListRecyclerView.scrollToPosition(position); // focus move to selected positon or card
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  the menu; this adds items to the action bar
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //  the menu; click listnear
        switch (item.getItemId()) {

            case R.id.nav_menu_product:
                Toast.makeText(MainActivity.this,"You clicked on Product",Toast.LENGTH_LONG).show();
                return true;

            case R.id.nav_menu_download:
                Toast.makeText(MainActivity.this,"You clicked on Download",Toast.LENGTH_LONG).show();
                return true;

            case R.id.nav_menu_pricing:
                Toast.makeText(MainActivity.this,"You clicked on Download",Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void leftNavigaion() {
        viewPager.arrowScroll(ViewPager.FOCUS_LEFT); // slider scrllos to left
    }

    public void rightNavigaion() {
        viewPager.arrowScroll(ViewPager.FOCUS_RIGHT); // slider scrllos to right
    }


    public void webservicecall() {
        Utility.showLoader(MainActivity.this);
        ServiceManager.getInstance().getKawaService().GET_User_Info("gender,name,nat,location,picture,email", 20)  // for api call and passing required parameters
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                        Utility.hideLoader();
                        try {
                            if (response.isSuccessful() && response.body() != null) {
                                USERDETAILS = response.body().getResults();  // stored all api data in array for multiple use
                                Log.e(TAG, "onResponse: >> USERDETAILS" + USERDETAILS.size());
                                userAdapter = new UserAdapter(MainActivity.this, USERDETAILS, callBack);
                                userListRecyclerView.setAdapter(userAdapter); // set data to card list
                                imageSliderAdapter = new ImageSliderAdapter(MainActivity.this, USERDETAILS);
                                viewPager.setAdapter(imageSliderAdapter); // set data to slider
                            } else {
                                if (response.errorBody() != null) {
                                    String errorMsg = response.errorBody().toString();
                                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        Utility.hideLoader();
                        String msg = null;
                        String title = null;
                        if (!Connectivity.isConnected(MainActivity.this)) {  // checcks is internet is available
                            Log.e(TAG, "onFailure: internet not available");
                            msg = getString(R.string.Text_Connection_Issue);
                            title = getString(R.string.Popup_Title_Connection_Issue);
                        } else if (!Connectivity.isConnectedFast(MainActivity.this)) { //checcks poor internet connection
                            Log.e(TAG, "onFailure: poor network");
                            msg = getString(R.string.Text_Poor_Connection_Issue);
                            title = getString(R.string.Popup_Title_Connection_Issue);
                        } else {
                            Log.e(TAG, "onFailure: something wrong " + t);
                            msg = getString(R.string.Error_General); // faliure reson if internt is working properly
                            title = getString(R.string.Popup_Title_Error);
                        }
                        AlertDialog dialog = Utility.buildAlertDialog(MainActivity.this, title, msg, getResources().getString(R.string.Button_Retry), getResources().getString(R.string.Button_Cancel), false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int buttonType) {
                                dialogInterface.dismiss();
                                if (buttonType == DialogInterface.BUTTON_POSITIVE) {
                                    webservicecall();
                                }
                            }
                        });
                        if (!isFinishing())
                            dialog.show();
                    }
                });
    }

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> { //user adapter to customise and disply the list view data

        private Context context;
        private List<Result> resultList;
        private boolean isWebserviceCallInProgress;
        private boolean hasDataForPaging;
        private Callback callback;
        ArrayList<String> strDescription = new ArrayList<>();
        boolean isVisible = true;
        int rowindex;

        public UserAdapter(final Context context, List<Result> resultList, Callback callback) {
            this.context = context;
            this.resultList = resultList;
            this.callback = callback;
        }

        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.user_details_layout, viewGroup, false));
        }

        public void setHasDataForPaging(boolean hasDataForPaging) {
            this.hasDataForPaging = hasDataForPaging;
        }

        public void setWebserviceCallInProgress(boolean webserviceCallInProgress) {
            isWebserviceCallInProgress = webserviceCallInProgress;
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder offerViewHolder, int position) {
            try {
                final List<Result> results = resultList;
                if (results != null) {
                    if (resultList.get(position).getGender() != null) {
                        offerViewHolder.genderTV.setText(Utility.capitalizeFirstCharOfEachWord(resultList.get(position).getGender() + " . " + resultList.get(position).getNat())); //capitalizeFirstCharOfEachWord
                    }
                    offerViewHolder.fullnameTV.setText(Utility.capitalizeFirstCharOfEachWord(resultList.get(position).getName().getTitle() + " " + resultList.get(position).getName().getFirst() + " " + resultList.get(position).getName().getLast()));
                    offerViewHolder.emailIDTV.setText((resultList.get(position).getEmail()));
                    offerViewHolder.userCardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rowindex = position; // stroe the value of clicked card position
                            //notifyDataSetChanged();
                            viewPager.setCurrentItem(position);
                        }
                    });
                    setActiveTab(offerViewHolder, position, rowindex);  //to chagne the active and normal tab
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private void setActiveTab(UserViewHolder offerViewHolder, int position, int rowindex) {
            Log.e("onClick: >>", String.valueOf(position));
            offerViewHolder.userCardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            offerViewHolder.genderTV.setTextColor(context.getResources().getColor(R.color.black));
            offerViewHolder.fullnameTV.setTextColor(context.getResources().getColor(R.color.black));
            offerViewHolder.emailIDTV.setTextColor(context.getResources().getColor(R.color.red));
            if (position == rowindex) {
                offerViewHolder.userCardView.setCardBackgroundColor(context.getResources().getColor(R.color.purple)); // to chnage the backgrond color of selected tab
                offerViewHolder.genderTV.setTextColor(context.getResources().getColor(R.color.white)); // to chnage the text color of selected tab
                offerViewHolder.fullnameTV.setTextColor(context.getResources().getColor(R.color.white));
                offerViewHolder.emailIDTV.setTextColor(context.getResources().getColor(R.color.white));
            }
        }

        @Override
        public int getItemCount() {
            if (resultList == null)
                return 0;
            return resultList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class UserViewHolder extends RecyclerView.ViewHolder {

            private TextView genderTV, fullnameTV, emailIDTV;
            CardView userCardView;

            public UserViewHolder(@NonNull View itemView) {
                super(itemView);
                genderTV = itemView.findViewById(R.id.genderTV);
                fullnameTV = itemView.findViewById(R.id.fullnameTV);
                emailIDTV = itemView.findViewById(R.id.emailIDTV);
                userCardView = itemView.findViewById(R.id.userCardView);

            }
        }

    }
}