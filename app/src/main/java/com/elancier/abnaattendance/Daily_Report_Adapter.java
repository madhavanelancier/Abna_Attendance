package com.elancier.abnaattendance;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class Daily_Report_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<People> items = new ArrayList<>();
    private Context ctx;

    private OnItemClickListener onItemClickListener;
    private OnMoreButtonClickListener onMoreButtonClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }


    public Daily_Report_Adapter(Context context, List<People> items) {
        this.items = items;
        ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CircularImageView image;
        public TextView date;
        public TextView name;
        public TextView intime;
        public CardView layout;
        public TextView outtime;
        public TextView hrs;
        public TextView status;
        public ImageButton more;
        public View lyt_parent;

        public ViewHolder(View v) {
            super(v);
            image = (CircularImageView) v.findViewById(R.id.image);
            name = (TextView) v.findViewById(R.id.name);
            date = (TextView) v.findViewById(R.id.date);
           // more = (ImageButton) v.findViewById(R.id.more);
            status = (TextView) v.findViewById(R.id.status);
            //lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            intime=(TextView) v.findViewById(R.id.remark);
            layout=(CardView) v.findViewById(R.id.lay);
            //ip=(TextView) v.findViewById(R.id.ip);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_report_items, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final People o = items.get(position);
            view.name.setText(o.username);
            view.date.setText(o.time);
            if(o.logout.equals("signin")){
                view.status.setText("Logged In");
                view.status.setTextColor(ctx.getResources().getColor(R.color.green_500));
            }
            else{
                view.status.setText("Logged Out");
                view.status.setTextColor(ctx.getResources().getColor(R.color.red_500));

            }
            view.intime.setText(o.print_user);
            if(!o.image.isEmpty()) {
                Glide.with(ctx).load(o.image).into(view.image);
            }
            else{
                Glide.with(ctx).load(R.drawable.placeholder_peace).into(view.image);

            }

            view.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCustomDialog(position);
                }
            });


            view.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showCustomDialog(position);

                }
            });

        }
    }
    private void showCustomDialog(int pos) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ImageView img=dialog.findViewById(R.id.imageView2);
        TextView names=dialog.findViewById(R.id.content);
        TextView time=dialog.findViewById(R.id.time);
        TextView date=dialog.findViewById(R.id.date);
        TextView statusval=dialog.findViewById(R.id.status);
        TextView remarks=dialog.findViewById(R.id.remarks);
        final People o = items.get(pos);

        if(!o.image.isEmpty()){
            Glide.with(ctx).load(o.image).into(img);
        }
        else{

        }
        names.setText     ("Name        : "+o.username);
        date.setText      ("Date         : "+o.date);
        time.setText      ("Time         : "+o.time);
        if(o.logout.equals("signin")){
        statusval.setText("Status       : Logged In");
        statusval.setTextColor(ctx.getResources().getColor(R.color.green_500));
        }
        else{
        statusval.setText("Status       : Logged Out");
        statusval.setTextColor(ctx.getResources().getColor(R.color.red_500));

        }
        remarks.setText   ("Reason      : "+o.print_user);



        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ctx, ((AppCompatButton) v).getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

  /*  private void onMoreButtonClick(final View view, final People people) {
        PopupMenu popupMenu = new PopupMenu(ctx, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, people, item);

                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_people_more);
        popupMenu.show();
    }*/

   /* private void onMoreButtonClick1(final View view, final People people) {
        PopupMenu popupMenu = new PopupMenu(ctx, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, people, item);

                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_people_more_de);
        popupMenu.show();
    }*/

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, People obj, int pos);
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, People obj, MenuItem item);

    }
}