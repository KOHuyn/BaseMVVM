package com.kohuyn.basemvvm.ui.main

import android.os.Bundle
import com.core.BaseActivity
import com.event.EventNextFragment
import com.kohuyn.basemvvm.R
import com.kohuyn.basemvvm.databinding.ActivityMainBinding
import com.utils.ext.register
import com.utils.ext.unregister
import org.greenrobot.eventbus.Subscribe

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getLayoutBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun updateUI(savedInstanceState: Bundle?) {
        openFragment(R.id.mainContainer, MainFragment::class.java, null, true)
    }

    override fun onStart() {
        super.onStart()
        register(this)
    }

    override fun onStop() {
        super.onStop()
        unregister(this)
    }

    @Subscribe
    fun openFrag(ev: EventNextFragment) {
        openFragment(R.id.mainContainer, ev.clazz, ev.bundle, ev.isAddToBackStack)
    }
}