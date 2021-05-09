package com.widget.pulltorefresh

import androidx.annotation.IntDef
import java.lang.annotation.RetentionPolicy

/**
 *Created by KO Huyn on 5/9/2021
 */
class ViewRefreshStatus {
    @IntDef(CONTENT_STATUS, LOADING_STATUS, EMPTY_STATUS, ERROR_STATUS)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class VIEW_REFRESH_STATUS
    companion object {
        const val CONTENT_STATUS = 1
        const val LOADING_STATUS = 2
        const val EMPTY_STATUS = 3
        const val ERROR_STATUS = 4
    }
}