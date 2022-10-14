package com.teguh.storyapp.ui.widget

import android.content.Intent
import android.widget.RemoteViewsService

class StoryWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        StoryRemoteViewsFactory(this.applicationContext)
}