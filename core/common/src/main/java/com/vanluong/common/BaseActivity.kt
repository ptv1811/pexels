package com.vanluong.common

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Created by van.luong
 * on 07,May,2025
 */
abstract class BaseActivity<T : ViewDataBinding>(
    @LayoutRes private val contentLayoutId: Int
) : AppCompatActivity() {

    protected val binding: T by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView(this, contentLayoutId)
    }

    protected inline fun binding(block: T.() -> Unit): T {
        return binding.apply(block)
    }

    init {
        addOnContextAvailableListener {
            binding.notifyChange()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}