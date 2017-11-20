package com.example.a4ia2.picapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a4ia2.picapp.Helpers.Note;
import com.example.a4ia2.picapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mtond_000 on 18.11.2017.
 */

public class NoteArrayAdapter extends ArrayAdapter {

    private ArrayList<Note> _list;
    private Context _context;
    private int _resource;
    private File currentImage;

    public NoteArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Note> objects) {
        super(context, resource, objects);

        this._list = objects;
        this._context = context;
        this._resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //inflater - klasa konwertująca xml na kod javy
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.note, null);
        //convertView = inflater.inflate(_resource, null);
        //szukamy każdego TextView w layoucie



        TextView title = convertView.findViewById(R.id.title);
        title.setText(_list.get(position).getTitle());


        TextView description = convertView.findViewById(R.id.description);
        description.setText(_list.get(position).getDescription());

        title.setTextColor(Color.parseColor(_list.get(position).getColor()));

//        currentImage = new File(_list.get(position).getPath());

        Bitmap myBitmap = BitmapFactory.decodeFile(_list.get(position).getPath(), null);


        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        image.setImageBitmap(myBitmap);


        //gdybyśmy chcieli klikać Imageview wewnątrz wiersza:
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // klik w obrazek
            }
        });

        return convertView;
    }
}
