package com.arcias.melocate.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.arcias.melocate.Model.ClusterMarker;
import com.arcias.melocate.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class ClusterRenderer extends DefaultClusterRenderer<
        ClusterMarker> {
    private final IconGenerator iconGenerator;
    private final ImageView profileImage;
    private final int markerWidth;
    private final int markerHeight;

    public ClusterRenderer(Context context, GoogleMap map,
                           ClusterManager<ClusterMarker>
                                   clusterManager
                           ) {
        super(context, map, clusterManager);

        iconGenerator=new IconGenerator(
                context.getApplicationContext()
        );
        profileImage=new ImageView(context
        .getApplicationContext());
        markerWidth=(int)context.getResources()
                .getDimension(R.dimen.
                        custom_profile_image);
        markerHeight=(int)context.getResources()
                .getDimension(R.dimen.
                        custom_profile_image);
        profileImage.setLayoutParams(
                new ViewGroup.LayoutParams
                        (markerWidth,markerHeight)
        );
      int padding=(int)context.getResources().getDimension(R.dimen
      .custom_profile_padding);
      profileImage.setPadding(padding,padding,padding,padding);
        iconGenerator.setContentView(profileImage);
    }

    @Override
    protected void onBeforeClusterItemRendered
            (@NonNull ClusterMarker item,
             @NonNull MarkerOptions markerOptions) {
profileImage.setImageResource(item.getIconPicture());
      try
      {
          //Picasso.get().load(item.getIconPicture()).into(profileImage);


          Bitmap icon=iconGenerator.makeIcon();

          markerOptions.icon(
                  BitmapDescriptorFactory.fromBitmap(icon)
          ).title(item.getTitle());
      }
catch (IllegalArgumentException e)
{
    e.printStackTrace();
}
    }

    @Override
    protected boolean shouldRenderAsCluster
            (@NonNull Cluster<ClusterMarker>
                     cluster) {
       return  false;
    }

}
