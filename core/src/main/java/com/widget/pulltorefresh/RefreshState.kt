package com.widget.pulltorefresh

import androidx.annotation.IntDef

/**
 *Created by KO Huyn on 5/9/2021
 */
class RefreshState {

    @IntDef(REFRESH, LOAD_MORE)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class REFRESH_STATE

    companion object {
        const val REFRESH = 10
        const val LOAD_MORE = 11
    }
}