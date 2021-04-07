package com.arcias.melocate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.arcias.melocate.Model.SlideItem;
import com.arcias.melocate.R;

import java.util.List;

public class SlidePageAdapter extends PagerAdapter {
    private Context context;
    private List<SlideItem> itemList;


    public SlidePageAdapter(Context context, List<SlideItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater=(LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.slides_item,container,false);
        ImageView imageView=view.findViewById(R.id.slide_image);
        TextView title=view.findViewById(R.id.slide_title);
        TextView description=view.findViewById(R.id.slide_description);
        imageView.setImageResource(itemList.get(position).getImageId());
        title.setText(itemList.get(position).getTitle());
        description.setText(itemList.get(position).getDescription());
container.addView(view);
return view;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
