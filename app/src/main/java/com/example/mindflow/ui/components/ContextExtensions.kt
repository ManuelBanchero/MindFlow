package com.example.mindflow.ui.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/* this method checks if context is an Activity.
    if is an Activity -> returns it
    if is not -> looks through the wrapper until find the activity, then returns it
    if there is not an activity -> returns null

    mainly used on "RecordingLifeCycleEffect"
*/
tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
