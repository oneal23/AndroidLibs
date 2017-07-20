package com.lei.baselib_kotlin

import android.content.Context
import android.view.Gravity
import android.widget.Toast

/**
 * Created by rymyz on 2017/6/28.
 */

//优化Toast
private var oneTime: Long = 0
private var twoTime: Long = 0
private var toast: Toast? = null
private var oldMsg: String? = null
fun Context.showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
    if (toast == null) {
        toast = Toast.makeText(this, message, length)
        toast?.setGravity(Gravity.CENTER, 0, 0)
        toast?.show()

        oneTime = System.currentTimeMillis()
    } else {
        twoTime = System.currentTimeMillis()
        if (message == oldMsg) {
            if (twoTime - oneTime > length) {
                toast?.show()
            }
        } else {
            oldMsg = message
            toast?.setText(message)
            toast?.show()
        }
    }

    twoTime = oneTime
}
