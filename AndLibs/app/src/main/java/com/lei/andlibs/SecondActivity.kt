package com.lei.andlibs

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lei.baselib_kotlin.showToast

/**
 * Created by rymyz on 2017/6/28.
 */
class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showToast("Hello")
    }
}