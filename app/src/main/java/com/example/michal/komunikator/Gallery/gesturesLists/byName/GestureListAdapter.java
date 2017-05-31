package com.example.michal.komunikator.Gallery.gesturesLists.byName;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.michal.komunikator.Gallery.gesturesLists.GalleryActivity;
import com.example.michal.komunikator.Gesture;
import com.example.michal.komunikator.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by michal on 10.04.2017.
 */

public class GestureListAdapter extends ArrayAdapter<Gesture> implements Filterable {

    private int mLayoutResource;
    private Context mContext = null;
    private List<Gesture> mGesturesList = null;
    private List<Gesture> mFilteredGesturesList = null;
    private Filter mGesturesFilter = new GesturesFilter();


    public GestureListAdapter(Context context, int resource, List<Gesture> objects) {
        super(context, resource, objects);
        this.mLayoutResource = resource;
        mGesturesList = objects;
        mFilteredGesturesList = objects;
        this.mContext = context;
    }


    @Override
    public int getCount(){
        return mFilteredGesturesList.size();
    }

    @Override
    public Gesture getItem(int position){
        return mFilteredGesturesList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Filter getFilter(){
        return mGesturesFilter;
    }

    @Override
    public void sort(Comparator<? super Gesture> comparator){
        super.sort(comparator);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent){
        View v = convertView;

        Gesture gesture = getItem(position);

        if (v == null) {
            LayoutInflater vi =  LayoutInflater.from(getContext());
            v = vi.inflate(mLayoutResource, null);
        }

        TextView name = (TextView) v.findViewById(R.id.gestureName);
        TextView category = (TextView) v.findViewById(R.id.gestureCategory);
        ImageView symbol = (ImageView) v.findViewById(R.id.gestureSymbol);

        if (name != null)
            name.setText(gesture.getName());

        if (category != null)
            category.setText(gesture.getCategory());

        if (symbol != null)
            symbol.setImageDrawable(gesture.getSymbol());


        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String name = ((TextView)v.findViewById(R.id.gestureName)).getText().toString();

                Iterator<Gesture> it = mGesturesList.iterator();
                while(it.hasNext()){
                    Gesture g = it.next();
                    if(g.getName().contains(name)) {
                        ((GalleryActivity) mContext).addGestureToMessege(g);
                        Log.i("GestureListAdapter", "Message extended by " + g.getName() + " gesture");
                        break;
                    }
                }
            }
        } );

        return  v;
    }

    private class GesturesFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            List<Gesture> filteredList = new ArrayList<>();

            constraint = constraint.toString().toLowerCase();

            for(Gesture g : mGesturesList)
                if(g.getName().toLowerCase().contains(constraint))
                    filteredList.add(g);

            results.count = filteredList.size();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilteredGesturesList = (ArrayList<Gesture>) results.values;
            notifyDataSetChanged();
        }
    }

}
