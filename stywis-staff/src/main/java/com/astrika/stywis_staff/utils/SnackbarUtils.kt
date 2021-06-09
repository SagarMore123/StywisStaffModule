package com.astrika.stywis_staff.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackbarUtils {
    @JvmStatic
    fun showSnackbar(v: View?, snackbarText: String?) {
        if (v == null || snackbarText == null) {
            return
        }
        Snackbar.make(v, snackbarText, Snackbar.LENGTH_LONG).show()
    }


/*

    //show snack bar when connectivity changes
    @JvmStatic
    fun notifyInternet(state: Int, context: Context, v: View?) {
        if (state > 0) {
            if (Constants.internetNotConnected) { */
/*Snackbar.make(v, "Connected", Snackbar.LENGTH_LONG)
                        .show();*//*

                val snackbar = Snackbar.make(v!!, "Connected", Snackbar.LENGTH_SHORT)
                val snackBarView = snackbar.view
                */
/*CoordinatorLayout.LayoutParams parentParams = (CoordinatorLayout.LayoutParams) snackBarView.getLayoutParams();
                parentParams.height = Constants.SNACKBAR_HEIGHT;
                snackBarView.setLayoutParams(parentParams);*//*
snackBarView.setBackgroundColor(context.getColor(R.color.colorSnackBarGreen))
                snackBarView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                val txtv = snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                txtv.textAlignment = View.TEXT_ALIGNMENT_CENTER
                snackbar.show()
                Constants.internetNotConnected = false
            } else {
                Constants.internetNotConnected = false
            }
        } else { */
/*Snackbar.make(v, "Not Connected", Snackbar.LENGTH_LONG)
                    .show();*//*

            val snackbar = Snackbar.make(v!!, "Not Connected", Snackbar.LENGTH_INDEFINITE)
            val snackBarView = snackbar.view
            */
/*CoordinatorLayout.LayoutParams parentParams = (CoordinatorLayout.LayoutParams) snackBarView.getLayoutParams();
            parentParams.height = Constants.SNACKBAR_HEIGHT;
            snackBarView.setLayoutParams(parentParams);*//*
snackBarView.setBackgroundColor(context.getColor(R.color.colorSnackBarRed))
            snackBarView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            val txtv = snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            txtv.textAlignment = View.TEXT_ALIGNMENT_CENTER
            snackbar.show()
        }
    }

    @JvmStatic
    fun showDeviceRootedMessage(message: String?, context: Context, v: View?) {
        val snackbar = Snackbar.make(v!!, message!!, Snackbar.LENGTH_INDEFINITE)
        val snackBarView = snackbar.view
        */
/*CoordinatorLayout.LayoutParams parentParams = (CoordinatorLayout.LayoutParams) snackBarView.getLayoutParams();
            parentParams.height = Constants.SNACKBAR_HEIGHT;
            snackBarView.setLayoutParams(parentParams);*//*
snackBarView.setBackgroundColor(context.getColor(R.color.colorSnackBarRed))
        snackBarView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        val txtv = snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        txtv.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackbar.show()
    }
*/


}