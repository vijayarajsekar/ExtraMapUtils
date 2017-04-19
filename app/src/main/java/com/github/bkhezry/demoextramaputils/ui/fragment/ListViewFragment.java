package com.github.bkhezry.demoextramaputils.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.bkhezry.demoextramaputils.R;
import com.github.bkhezry.demoextramaputils.ui.MapsActivity;
import com.github.bkhezry.demoextramaputils.utils.AppUtils;
import com.github.bkhezry.extramaputils.builder.OptionViewBuilder;
import com.github.bkhezry.extramaputils.model.ViewOption;
import com.github.bkhezry.extramaputils.utils.MapUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;

public class ListViewFragment extends Fragment {
    private ListFragment mList;
    private MapAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view,
                container, false);
        mAdapter = new MapAdapter(getActivity(), LIST_OPTION_VIEW);
        mList = (ListFragment) getChildFragmentManager().findFragmentById(R.id.list);
        mList.setListAdapter(mAdapter);
//        AbsListView lv = mList.getListView();
//        lv.setRecyclerListener(mRecycleListener);
        return view;
    }

    public Fragment newInstance() {
        return new ListViewFragment();
    }

    private class MapAdapter extends ArrayAdapter<ViewOption> {

        private final HashSet<MapView> mMaps = new HashSet<>();
        private ViewOption[] viewOptions;

        public MapAdapter(Context context, ViewOption[] viewOptions) {
            super(context, R.layout.list_item, R.id.titleTextView, viewOptions);
            this.viewOptions = viewOptions;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item, null);
                holder = new ViewHolder();
                holder.mapView = (MapView) convertView.findViewById(R.id.mapLite);
                holder.title = (TextView) convertView.findViewById(R.id.titleTextView);
                holder.cardView = (CardView) convertView.findViewById(R.id.cardView);
                convertView.setTag(holder);
                holder.initializeMapView();
                mMaps.add(holder.mapView);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ViewOption viewOption = viewOptions[position];
            holder.mapView.setTag(viewOption);
            if (holder.map != null) {
                setMapLocation(viewOption, holder.map);
            }
            holder.title.setText(viewOption.getTitle());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putParcelable("optionView", viewOption);
                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                    intent.putExtra("args", args);
                    startActivity(intent);
                }
            });
            return convertView;
        }

        public HashSet<MapView> getMaps() {
            return mMaps;
        }
    }

    private static void setMapLocation(ViewOption viewOption, GoogleMap googleMap) {
        MapUtils.showElements(viewOption, googleMap);
    }

    private static class ViewHolder implements OnMapReadyCallback {
        MapView mapView;
        TextView title;
        GoogleMap map;
        CardView cardView;

        private ViewHolder() {

        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            final ViewOption viewOption = (ViewOption) mapView.getTag();
            if (viewOption != null) {
                setMapLocation(viewOption, map);
            }
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                }
            });
        }

        private void initializeMapView() {
            if (mapView != null) {
                mapView.onCreate(null);
                mapView.getMapAsync(this);
            }
        }

    }

    private AbsListView.RecyclerListener mRecycleListener = new AbsListView.RecyclerListener() {

        @Override
        public void onMovedToScrapHeap(View view) {
            ViewHolder holder = (ViewHolder) view.getTag();
            if (holder != null && holder.map != null) {
                holder.map.clear();
            }

        }
    };

    private static ViewOption[] LIST_OPTION_VIEW = {
            new OptionViewBuilder()
                    .withTitle("1")
                    .withCenterCoordinates(new LatLng(35.6892, 51.3890))
                    .withMarkers(AppUtils.getListExtraMarker())
                    .withForceCenterMap(false)
                    .withIsListView(true)
                    .build(),
            new OptionViewBuilder()
                    .withTitle("2")
                    .withCenterCoordinates(new LatLng(35.6892, 51.3890))
                    .withPolygons(
                            AppUtils.getPolygon_1(),
                            AppUtils.getPolygon_2()
                    )
                    .withForceCenterMap(false)
                    .withIsListView(true)
                    .build(),
            new OptionViewBuilder()
                    .withTitle("3")
                    .withCenterCoordinates(new LatLng(35.6892, 51.3890))
                    .withPolylines(
                            AppUtils.getPolyline_1(),
                            AppUtils.getPolyline_2()
                    )
                    .withForceCenterMap(false)
                    .withIsListView(true)
                    .build(),
            new OptionViewBuilder()
                    .withTitle("4")
                    .withCenterCoordinates(new LatLng(35.6892, 51.3890))
                    .withMarkers(AppUtils.getListMarker())
                    .withPolylines(AppUtils.getPolyline_3())
                    .withForceCenterMap(false)
                    .withIsListView(true)
                    .build()
    };
}
