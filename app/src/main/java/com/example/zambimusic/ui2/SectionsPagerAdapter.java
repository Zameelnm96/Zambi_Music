package com.example.zambimusic.ui2;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.zambimusic.R;
import com.example.zambimusic.ui2.fragments.AlbumFragment;
import com.example.zambimusic.ui2.fragments.AllSongsFragment;
import com.example.zambimusic.ui2.fragments.PlaylistFragment;
import com.example.zambimusic.ui2.fragments.RecentFragment;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private static final int RECENT_TAB_POS = 0;
    private static final int PLAYLIST_TAB_POS = 1;
    private static final int ALBUM_TAB_POS = 2;
    private static final int ALL_SONGS_TAB_POS = 3;
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.recent_tab, R.string.playlist_tab,R.string.album_tab,R.string.all_song_tab};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){

            case RECENT_TAB_POS:
                fragment = RecentFragment.newInstance();
                return fragment;
            case PLAYLIST_TAB_POS:
                fragment = PlaylistFragment.newInstance();
                return fragment;
            case ALBUM_TAB_POS:
                fragment = AlbumFragment.newInstance();
                return fragment;
            case ALL_SONGS_TAB_POS:
                fragment = AllSongsFragment.newInstance();
                return fragment;
        }
        return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 4;
    }
}