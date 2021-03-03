package com.happysmacks.happysmacks.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.happysmacks.happysmacks.R;
import com.squareup.picasso.Picasso;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    String globalImagesList[];
    String globalStickerDesc[];

    public MyAdapter(Context ctx, String _imagesList[], String _stickerListDesc[]) {
        context = ctx;
        globalImagesList = _imagesList;
        globalStickerDesc = _stickerListDesc;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.add_sticker_row, parent, false);

        return new MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder._textView.setText(globalStickerDesc[position]);
        Picasso.get().load(Uri.parse(globalImagesList[position])).placeholder(R.drawable.loading).into(holder._imageView);
        holder._button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println(position);
                        System.out.println(position);
                        System.out.println(position);
                        System.out.println(position);
                        holder._view.setVisibility(View.GONE);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return globalImagesList.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView _imageView;
        Button _button;
        TextView _textView;
        View _view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            _imageView = itemView.findViewById(R.id.add_sticker_image);
            _button = itemView.findViewById(R.id.add_sticker_button);
            _textView = itemView.findViewById(R.id.add_sticker_text);
            _view = itemView.findViewById(R.id.add_sticker_view);
        }
    }
}
