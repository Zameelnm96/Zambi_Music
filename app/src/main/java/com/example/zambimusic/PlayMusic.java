package com.example.zambimusic;

import android.app.Service;
import android.content.Intent;
import android.media.browse.MediaBrowser;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.service.media.MediaBrowserService;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PlayMusic extends MediaBrowserService {

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return null;
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowser.MediaItem>> result) {

    }
}

