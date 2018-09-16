package com.atschoolPioneerSchool.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atschoolPioneerSchool.R;
import com.atschoolPioneerSchool.model.ReceiptVoucher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 28/05/2017.
 */

public class ReceiptVoucherListAdapter extends RecyclerView.Adapter<ReceiptVoucherListAdapter.ViewHolder> implements Filterable {

    private List<ReceiptVoucher> original_items = new ArrayList<>();
    private List<ReceiptVoucher> filtered_items = new ArrayList<>();
    private ReceiptVoucherListAdapter.ItemFilter mFilter = new ReceiptVoucherListAdapter.ItemFilter();
    private Context ctx;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView content;
        public TextView date;
        public ImageView image;
        public LinearLayout lyt_parent;
        public TextView Num;
        public TextView Account;
        public TextView amount;

        public ViewHolder(View v) {
            super(v);
            content = (TextView) v.findViewById(R.id.content);
            date = (TextView) v.findViewById(R.id.date);
            image = (ImageView) v.findViewById(R.id.image);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
            Num = (TextView) v.findViewById(R.id.Num);
            Account = (TextView) v.findViewById(R.id.Account);
            amount = (TextView) v.findViewById(R.id.amount);
        }

    }

    public Filter getFilter() {
        return mFilter;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ReceiptVoucherListAdapter(Context ctx, List<ReceiptVoucher> items) {
        this.ctx = ctx;
        original_items = items;
        filtered_items = items;
    }

    @Override
    public ReceiptVoucherListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_receiptvoucher, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ReceiptVoucherListAdapter.ViewHolder vh = new ReceiptVoucherListAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ReceiptVoucherListAdapter.ViewHolder holder, final int position) {
        final ReceiptVoucher n = filtered_items.get(position);

        if (!n.LastObject) {
            holder.content.setText(Html.fromHtml(n.ACC_TRANS_TYPE_NAMEA));
            holder.date.setText("  " + n.Date_G);
            holder.amount.setText("  " + String.valueOf(n.TRANS_AMOUNT_DEBIT + n.TRANS_AMOUNT_CREDIT) + "  ");
            holder.Num.setText(n.Book_Voucher_No);
            holder.Account.setText("  ");

//        Picasso.with(ctx).load(n.getFriend().getPhoto())
//                .resize(60, 60)
//                .transform(new CircleTransform())
//                .into(holder.image);

            setAnimation(holder.itemView, position);
            // view detail message conversation
            holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, " " + n.Book_Voucher_No + " رقم الحركه ", Snackbar.LENGTH_SHORT).show();
                }
            });
        } else {
            holder.content.setText(Html.fromHtml("المجموع   " + "   " + n.TOTAL_TRANS_AMOUNT_CREDIT + "        "));
            holder.date.setText("  ");
            holder.amount.setText("  ");
            holder.Num.setText("");
            holder.Account.setText("  ");

            holder.image.setVisibility(View.GONE);
            holder.content.setTextSize(17);
            holder.content.setTextColor(Color.RED);
        }
    }

    // Here is the key method to apply the animation
    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filtered_items.size();
    }

    @Override
    public long getItemId(int position) {
        return filtered_items.get(position).getId();
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String query = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            final List<ReceiptVoucher> list = original_items;
            final List<ReceiptVoucher> result_list = new ArrayList<>(list.size());

            for (int i = 0; i < list.size(); i++) {
                String str_title = list.get(i).ACC_TRANS_TYPE_NAMEA;
                if (str_title.toLowerCase().contains(query)) {
                    result_list.add(list.get(i));
                }
            }

            results.values = result_list;
            results.count = result_list.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered_items = (List<ReceiptVoucher>) results.values;
            notifyDataSetChanged();
        }

    }
}