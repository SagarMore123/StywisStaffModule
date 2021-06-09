package com.astrika.stywis_staff.utils

import android.app.Activity
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.network.network_utils.SERVER_IMG_URL
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


class BindingAdapter {
    companion object {

        @BindingAdapter(value = ["errorText", "activity"], requireAll = false)
        @JvmStatic
        fun setErrorMessage(
            view: TextView,
            errorMessage: String?,
            activity: Activity
        ) {
//            view.text = "Error - $errorMessage"
            view.text = errorMessage ?: ""
        }

        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String?) {
            val placeholder = R.drawable.ic_person_placeholder

            if (!url.isNullOrEmpty()) {
                if (!url.contains("null")) {
//                            Glide.with(view.context).load("https://s3.amazonaws.com/appsdeveloperblog/Micky.jpg").into(view)
                    if (!url.contains("https://")) {
                        Glide.with(view.context)
                            .load("$SERVER_IMG_URL$url")
                            .placeholder(placeholder)
                            .centerCrop()
                            .error(placeholder)
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .skipMemoryCache(true)
                            .into(view)
                    } else {
                        Glide.with(view.context)
                            .load(url)
                            .placeholder(placeholder)
                            .error(placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(view)
                    }

                }
            }
        }

        @JvmStatic
        @BindingAdapter("selectDate", requireAll = true)
        fun dateClicks(editText: EditText, mutableLiveData: MutableLiveData<String>) {
            editText.setOnClickListener {
                Utils.selectDate(
                    editText.context,
                    editText.context.resources.getString(R.string.select_date),
                    mutableLiveData
                )
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["selectDate", "dateFormat"], requireAll = true)
        fun dateWithFormatClicks(
            editText: EditText,
            mutableLiveData: MutableLiveData<String>,
            dateFormat: String
        ) {
            editText.setOnClickListener {
                Utils.selectDateWithFormat(
                    editText.context,
                    editText.context.resources.getString(R.string.select_date),
                    mutableLiveData,
                    dateFormat
                )
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["time"], requireAll = true)
        fun timeClicks(editText: EditText, time: MutableLiveData<String>) {
            editText.setOnClickListener {
                Utils.selectTime(
                    editText.context,
                    editText.context.resources.getString(R.string.select_time),
                    time
                )
            }
        }


        @JvmStatic
        @BindingAdapter("touchListener")
        fun setTouchListener(self: View, value: Boolean): Boolean {
            self.setOnTouchListener { view, event ->
                if (self.hasFocus()) {
                    self.parent.requestDisallowInterceptTouchEvent(true)
                    when (event.action) {
                        MotionEvent.ACTION_SCROLL -> {
                            self.parent.requestDisallowInterceptTouchEvent(false)
                            true
                        }
                    }
                }

                false
            }
            return false
        }

    }
}