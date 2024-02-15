package com.example.suitcase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase.R;

import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder> {

    static Context context;
    static ArrayList<String> iID;
    static ArrayList<String> iSID;
    static ArrayList<String> iname;
    static ArrayList<String> idescription;
    static ArrayList<String> iprice;
    static ArrayList<String> ipurchased;
    LayoutInflater layoutInflater;

    public myAdapter(Context context, ArrayList<String> iID, ArrayList<String> iSID, ArrayList<String> iname, ArrayList<String> idescription, ArrayList<String> iprice, ArrayList<String> ipurchased){
        this.context = context;
        this.iID = iID;
        this.iSID = iSID;
        this.iname = iname;
        this.idescription = idescription;
        this.iprice = iprice;
        this.ipurchased = ipurchased;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ID.setText(String.valueOf(iID.get(position)));
        holder.SID.setText(String.valueOf(iSID.get(position)));
        holder.names.setText(String.valueOf(iname.get(position)));
        holder.descriptions.setText(String.valueOf(idescription.get(position)));
        holder.price.setText("Rs. "+String.valueOf(iprice.get(position)));
        holder.purchased.setChecked(String.valueOf(ipurchased.get(position)).equals("true"));
    }

    @Override
    public int getItemCount() {
        return iname.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ID, SID, names, descriptions, price;
        CheckBox purchased;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ID = itemView.findViewById(R.id.textView4);
            SID = itemView.findViewById(R.id.textSID);
            names = itemView.findViewById(R.id.textView);
            descriptions = itemView.findViewById(R.id.textView3);
            price = itemView.findViewById(R.id.textprice);
            purchased = itemView.findViewById(R.id.checkBox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Options.class);
                    intent.putExtra("id", iID.get(getAbsoluteAdapterPosition()));
                    intent.putExtra("sid", iSID.get(getAbsoluteAdapterPosition()));
                    intent.putExtra("names", iname.get(getAbsoluteAdapterPosition()));
                    intent.putExtra("descriptions", idescription.get(getAbsoluteAdapterPosition()));
                    intent.putExtra("price", iprice.get(getAbsoluteAdapterPosition()));
                    if (ipurchased.get(getAbsoluteAdapterPosition()).equals("true")){
                        intent.putExtra("purchased", "true");
                    }else {
                        intent.putExtra("purchased", "false");
                    }
                    context.startActivity(intent);
                }
            });
        }
    }
}
