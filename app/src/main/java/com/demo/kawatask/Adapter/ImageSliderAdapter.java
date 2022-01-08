package com.demo.kawatask.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.demo.kawatask.R;
import com.demo.kawatask.model.Result;
import com.demo.kawatask.utils.Utility;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ImageSliderAdapter extends PagerAdapter { //for slider we use PagerAdapter

    public List<Result> resultList;
    private LayoutInflater inflater;
    private Context context;

    public ImageSliderAdapter(Context context, List<Result> resultList) {
        this.context = context;
        this.resultList = resultList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (null != resultList) {
            return resultList.size()-1;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View viewLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);
        assert viewLayout != null;
        TextView fullnameTV, street_numberTV, address_nameTV, counteryTV, postalTV, timezoneTV, genderTV;

        ImageView imageView = viewLayout.findViewById(R.id.userProfile);
        fullnameTV = viewLayout.findViewById(R.id.fullnameTV);
        street_numberTV = viewLayout.findViewById(R.id.street_numberTV);
        address_nameTV = viewLayout.findViewById(R.id.address_nameTV);
        counteryTV = viewLayout.findViewById(R.id.counteryTV);
        postalTV = viewLayout.findViewById(R.id.postalTV);
        timezoneTV = viewLayout.findViewById(R.id.timezoneTV);
        genderTV = viewLayout.findViewById(R.id.genderTV);

        Glide.with(context).setDefaultRequestOptions(Utility.imageDefault()) // to fetch and set the image
                .load(resultList.get(position).getPicture().getMedium())
                .into(imageView);

        String fullnameSTR = resultList.get(position).getName().getTitle() + " " + resultList.get(position).getName().getFirst() + " " + resultList.get(position).getName().getLast();
        SpannableString content = new SpannableString(fullnameSTR);
        content.setSpan(new UnderlineSpan(), 0, fullnameSTR.length(), 0); // to underline the text
        fullnameTV.setText(content);

        street_numberTV.setText(resultList.get(position).getLocation().getStreet().getNumber().toString());
        address_nameTV.setText(", " + resultList.get(position).getLocation().getStreet().getName() + ", " + resultList.get(position).getLocation().getCity() + ", " + resultList.get(position).getLocation().getState());
        counteryTV.setText(resultList.get(position).getLocation().getCountry());
        postalTV.setText(", " + resultList.get(position).getLocation().getPostcode());
        timezoneTV.setText(resultList.get(position).getLocation().getTimezone().getOffset() + ", " + resultList.get(position).getLocation().getTimezone().getDescription());
        genderTV.setText(resultList.get(position).getGender());

        view.addView(viewLayout);
        return viewLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}