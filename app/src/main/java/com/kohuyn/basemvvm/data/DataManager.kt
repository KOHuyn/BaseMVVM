package com.kohuyn.basemvvm.data

import com.kohuyn.basemvvm.data.local.db.DbHelper
import com.kohuyn.basemvvm.data.local.prefs.PrefsHelper
import com.kohuyn.basemvvm.data.remote.ApiHelper

/**
 * Created by KOHuyn on 1/29/2021
 */
interface DataManager : ApiHelper, PrefsHelper,DbHelper {
}