package com.example.suitcase;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycleView;
    FloatingActionButton button;
    FloatingActionButton home;
    DBHelper dbh;
    ArrayList<String> iID, iSID, iname, idescription, iprice, ipurchased;
    com.example.suitcase.myAdapter myAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycleView = findViewById(R.id.recycle);
        button = findViewById(R.id.floatingActionButton2);
        home = findViewById(R.id.btnHome);
        dbh = new DBHelper(this);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddItem.class));
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Home.class));
            }
        });

        iID = new ArrayList<>();
        iSID = new ArrayList<>();
        iname = new ArrayList<>();
        idescription = new ArrayList<>();
        iprice = new ArrayList<>();
        ipurchased = new ArrayList<>();

        myAdapter = new myAdapter(MainActivity.this, iID, iSID, iname, idescription, iprice, ipurchased);
        recycleView.setAdapter(myAdapter);

        recycleView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recycleView);

        displaydata();

    }
    private void refreshData() {
        // For example:
        iID.clear();
        iSID.clear();
        iname.clear();
        idescription.clear();
        iprice.clear();
        ipurchased.clear();

        displaydata();

        myAdapter.notifyDataSetChanged();

        swipeRefreshLayout.setRefreshing(false);
    }
    private void displaydata() {
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        Integer userId = sessionManager.getID();
        Cursor cursor = dbh.getdata(userId);
        if (cursor.getCount() == -1){
            Toast.makeText(this, "No Items", Toast.LENGTH_SHORT).show();
            return;
        }else {
            Integer i = 1;
            while (cursor.moveToNext()){
                String ad = i.toString();
                iID.add(cursor.getString(0));
                iSID.add(ad);
                iname.add(cursor.getString(1));
                idescription.add(cursor.getString(2));
                iprice.add(cursor.getString(3));
                if(cursor.getInt(4) == 1){
                    ipurchased.add("true");
                }else {
                    ipurchased.add("false");
                }
                i++;
            }
        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();

            if (direction == ItemTouchHelper.LEFT) {
                // Handle swipe left (Delete)
                final String deletedItem = iID.get(position);
                final String dSID = iSID.get(position);
                final int deletedItemPosition = position;

                // Remove the item from the list and notify the adapter
                iID.remove(position);
                iSID.remove(position);
                iname.remove(position);
                idescription.remove(position);
                iprice.remove(position);
                ipurchased.remove(position);
                myAdapter.notifyItemRemoved(position);

                // Show a Snackbar with an "Undo" action
                Snackbar.make(recycleView, "Item deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Handle the "Undo" action
                                iID.add(deletedItemPosition, deletedItem);
                                iSID.add(deletedItemPosition, dSID);
                                Cursor cursor = dbh.getItemData(deletedItem);
                                if (cursor.moveToFirst()) {
                                    String itemName = cursor.getString(1);
                                    String itemDescription = cursor.getString(2);
                                    String itemPrice = cursor.getString(3);
                                    int itemPurchased = cursor.getInt(4);
                                    iname.add(deletedItemPosition, itemName);
                                    idescription.add(deletedItemPosition, itemDescription);
                                    iprice.add(deletedItemPosition, itemPrice);
                                    ipurchased.add(deletedItemPosition, (itemPurchased == 1) ? "true" : "false");
                                }

                                myAdapter.notifyItemInserted(deletedItemPosition);
                            }
                        })
                        .addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                    // The user did not click "Undo," so delete the item from the database
                                    dbh.deleteitemdata(deletedItem);
                                }
                            }
                        })
                        .show();

            } else if (direction == ItemTouchHelper.RIGHT) {
                // Handle swipe right (Mark as purchased)
                final int purchasedItemPosition = position;

                // Mark the item as purchased in the list and notify the adapter
                ipurchased.set(position, "true");
                myAdapter.notifyItemChanged(position);

                // Show a Snackbar with an "Undo" action
                Snackbar.make(recycleView, "Item marked as purchased", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Handle the "Undo" action
//                                String purchasedItemId = iID.get(purchasedItemPosition);
//                                Boolean result = dbh.unmarkAsPurchased(purchasedItemId);
//                                if (result == true){
                                ipurchased.set(purchasedItemPosition, "false");
                                myAdapter.notifyItemChanged(purchasedItemPosition);
//                                }
                            }
                        })
                        .addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                    // The user did not click "Undo," so mark the item as purchased in the database
                                    String purchasedItemId = iID.get(purchasedItemPosition);
                                    dbh.markAsPurchased(purchasedItemId);
                                }
                            }
                        })
                        .show();
            }
        }


        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            int direction = (dX > 0) ? ItemTouchHelper.RIGHT : ItemTouchHelper.LEFT;
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.deleteColor))
                    .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)
                    .addSwipeLeftLabel("Delete")
                    .setSwipeLeftLabelColor(ContextCompat.getColor(MainActivity.this, R.color.white))
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.markColor))
                    .addSwipeRightActionIcon(R.drawable.markaspurchased)
                    .setSwipeRightLabelColor(ContextCompat.getColor(MainActivity.this, R.color.white))
                    .addSwipeRightLabel("Mark As Purchased")
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}