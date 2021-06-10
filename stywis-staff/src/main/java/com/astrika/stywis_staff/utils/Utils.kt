package com.astrika.stywis_staff.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.view.login.viewmodels.FirstTimeLoginViewModel
import com.astrika.stywis_staff.master_controller.source.MasterRepository
import com.astrika.stywis_staff.models.LoginDTO
import com.astrika.stywis_staff.models.OrderBundleDTO
import com.astrika.stywis_staff.models.menu.DishDetailsDTO
import com.astrika.stywis_staff.source.dashboard.DashboardRepository
import com.astrika.stywis_staff.source.user.UserRepository
import com.astrika.stywis_staff.view.dashboard.viewmodels.*
import com.astrika.stywis_staff.view.login.viewmodels.LoginViewModel
import com.astrika.stywis_staff.view.login.viewmodels.SetPasswordViewModel
import com.astrika.stywis_staff.view.login.viewmodels.VerifyOtpViewModel
import com.astrika.stywis_staff.view.menus.viewmodels.*
import com.astrika.stywis_staff.view.profile.viewmodels.ProfileViewModel
import com.astrika.stywis_staff.view.stockmanagement.viewmodels.StockCustomizationViewModel
import com.astrika.stywis_staff.view.stockmanagement.viewmodels.StockPagerViewModel
import com.astrika.stywis_staff.view.stockmanagement.viewmodels.StockViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object {

        fun <T : GenericBaseObservable?> obtainBaseObservable(
            activity: Activity,
            modelClass: Class<T>,
            owner: LifecycleOwner?,
            view: View?
        ): T? {

            return when {


                // Login ViewModel
                modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                    LoginViewModel(
                        activity, activity.application, owner, view,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                modelClass.isAssignableFrom(FirstTimeLoginViewModel::class.java) -> {
                    FirstTimeLoginViewModel(
                        activity, activity.application, owner, view,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }
                modelClass.isAssignableFrom(VerifyOtpViewModel::class.java) -> {
                    VerifyOtpViewModel(
                        activity, activity.application, owner, view,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                modelClass.isAssignableFrom(SetPasswordViewModel::class.java) -> {
                    SetPasswordViewModel(
                        activity, activity.application, owner, view,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                    DashboardViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }
                modelClass.isAssignableFrom(DashboardFragmentViewModel::class.java) -> {
                    DashboardFragmentViewModel(
                        activity, activity.application, owner, view
                    ) as T
                }
                modelClass.isAssignableFrom(DashboardSubViewModel::class.java) -> {
                    DashboardSubViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                modelClass.isAssignableFrom(DishAvailabilityPagerViewModel::class.java) -> {
                    DishAvailabilityPagerViewModel(
                        activity, activity.application, owner, view,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                modelClass.isAssignableFrom(DishAvailabilityViewModel::class.java) -> {
                    DishAvailabilityViewModel(
                        activity, activity.application, owner, view,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                modelClass.isAssignableFrom(MenuViewModel::class.java) -> {
                    MenuViewModel(
                        activity, activity.application, owner, view,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                modelClass.isAssignableFrom(MenuPagerViewModel::class.java) -> {
                    MenuPagerViewModel(
                        activity, activity.application, owner, view,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }
                modelClass.isAssignableFrom(OrderReviewViewModel::class.java) -> {
                    OrderReviewViewModel(
                        activity, activity.application, owner, view,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }
//                modelClass.isAssignableFrom(TableConfigViewModel::class.java) -> {
//                    TableConfigViewModel(
//                        activity, activity.application, owner, view,
//                        MasterRepository.getInstance(activity.applicationContext)!!,
//                        DashboardRepository.getInstance(activity.application, true)!!
//                    ) as T
//                }

                // Profile ViewModel
                modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                    ProfileViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        UserRepository.getInstance(activity.application, true)!!,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                modelClass.isAssignableFrom(BottomSheetCheckInViewModel::class.java) -> {
                    BottomSheetCheckInViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                // Bill Generation ViewModel
                modelClass.isAssignableFrom(BillGenerationViewModel::class.java) -> {
                    BillGenerationViewModel(
                        activity, activity.application, owner, view,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }


                // Autocomplete ViewModel
                modelClass.isAssignableFrom(AutocompleteViewModel::class.java) -> {
                    AutocompleteViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!
                    ) as T
                }

                // Discount ViewModel
                modelClass.isAssignableFrom(ApplicableDiscountViewModel::class.java) -> {
                    ApplicableDiscountViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                //Customization Bottom Sheet Viewmodel
                modelClass.isAssignableFrom(CustomizationViewModel::class.java) -> {
                    CustomizationViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }


                //WishList Viewmodel
                modelClass.isAssignableFrom(WishListViewModel::class.java) -> {
                    WishListViewModel(
                        activity, activity.application, owner, view,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                // Stock
                modelClass.isAssignableFrom(StockViewModel::class.java) -> {
                    StockViewModel(
                        activity, activity.application, owner, view,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                modelClass.isAssignableFrom(StockPagerViewModel::class.java) -> {
                    StockPagerViewModel(
                        activity, activity.application, owner, view,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                modelClass.isAssignableFrom(StockCustomizationViewModel::class.java) -> {
                    StockCustomizationViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }


                else -> null
            }

        }

        fun selectDate(
            context: Context,
            message: String,
            mutableLiveData: MutableLiveData<String>
        ) {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val datePickerDialog = DatePickerDialog(
                context,
                { view, year, monthOfYear, dayOfMonth ->
                    mutableLiveData.value =
                        getDatePickerFormattedDate(
                            dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year.toString()
                        )
                }, year, month, day
            )
            datePickerDialog.setTitle(message)
            datePickerDialog.show()

        }

        fun selectDateWithFormat(
            context: Context,
            message: String,
            mutableLiveData: MutableLiveData<String>,
            dateFormat: String
        ) {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val datePickerDialog = DatePickerDialog(
                context,
                { view, year, monthOfYear, dayOfMonth ->
                    mutableLiveData.value =
                        dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year.toString()
                    /* getDatePickerFormattedDate(
                         dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year.toString()
                     )*/
                }, year, month, day
            )
            datePickerDialog.setTitle(message)
            datePickerDialog.show()

        }

        private fun getFormattedDate(DOB: String?, dateFormat: String): String? {
            val format: DateFormat = SimpleDateFormat(dateFormat)
            var date: Date? = null
            try {
                date = format.parse(DOB)
            } catch (e: ParseException) {
            }
            val formatString: DateFormat = SimpleDateFormat("dd-MM-yyyy") // 01-10-2020
            return formatString.format(date) ?: ""
        }

        private fun getDatePickerFormattedDate(DOB: String?): String? {
            val format: DateFormat = SimpleDateFormat("dd-MM-yyyy") // 01-01-2020
            var date: Date? = null
            try {
                date = format.parse(DOB)
            } catch (e: ParseException) {
            }
            val formatString: DateFormat = SimpleDateFormat("dd MMMM yyyy") // 01-Jan-2020
            return formatString.format(date) ?: ""
        }

        fun getDateFormattedTime(DOB: String?): String? {
            val format: DateFormat = SimpleDateFormat("dd-mm-yyyy hh:mm:ss") // 01-01-2020
            var date: Date? = null
            try {
                date = format.parse(DOB)
            } catch (e: ParseException) {
            }
            val formatString: DateFormat = SimpleDateFormat("hh:mm a") // 01-Jan-2020
            return formatString.format(date) ?: ""
        }

        fun getLongFromTime(DOB: String?): Long {
            val format: DateFormat = SimpleDateFormat("dd-mm-yyyy HH:mm:ss") // 01-01-2020
            var date: Date? = null
            try {
                date = format.parse(DOB)
            } catch (e: ParseException) {
            }
//            val formatString: DateFormat = SimpleDateFormat("HH:mm:ss") // 01-Jan-2020
//            return formatString.format(date) ?: ""

            return date?.time ?: 0
        }

        fun getDatePickerFormattedSavingDate(DOB: String?): String? {
            val format: DateFormat = SimpleDateFormat("dd MMMM yyyy") // 01 January 2020
            var date: Date? = null
            try {
                date = format.parse(DOB)
            } catch (e: ParseException) {
            }
            val formatString: DateFormat = SimpleDateFormat("dd-MM-yyyy") // 01-Jan-2020
            return formatString.format(date) ?: ""
        }

        fun getDatePickerFormattedDate3(DOB: String?): String? {
            val format: DateFormat = SimpleDateFormat("dd MMMM yyyy") // 01 January 2020
            var date: Date? = null
            try {
                date = format.parse(DOB)
            } catch (e: ParseException) {
            }
            val formatString: DateFormat = SimpleDateFormat("dd MMMM") // 01 January
            return formatString.format(date) ?: ""
        }

        fun getDatePickerFormattedDate4(DOB: String?): String? {
            val format: DateFormat = SimpleDateFormat("dd-MM-yyyy") // 01-01-2020
            var date: Date? = null
            try {
                date = format.parse(DOB)
            } catch (e: ParseException) {
            }
            val formatString: DateFormat = SimpleDateFormat("dd MMMM") // 01-Jan-2020
            return formatString.format(date) ?: ""
        }


        fun selectTime(
            context: Context,
            message: String,
            startTime: MutableLiveData<String>
        ) {
            val cal = Calendar.getInstance()
            val hours = cal.get(Calendar.HOUR_OF_DAY)
            val minutes = cal.get(Calendar.MINUTE)


            if (!startTime.value.isNullOrEmpty()) {
                startTime.value = ""
            }

            val timePickerDialog = TimePickerDialog(
                context,
                { view, hourOfDay, minute ->

                    var format = ""
                    var hour = hourOfDay
                    when {
                        hour == 0 -> {
                            hour += 12
                            format = "AM"
                        }
                        hour == 12 -> {
                            format = "PM"
                        }
                        hour > 12 -> {
                            hour -= 12
                            format = "PM"
                        }
                        else -> {
                            format = "AM"
                        }
                    }

                    val min = if (minute == 0 || minute == 15 || minute == 30 || minute == 45) {
                        (minute / 15 * 15)
                    } else {
                        (minute / 15 * 15) + 15
                    }
                    startTime.value = getDatePickerFormattedTime("$hourOfDay:$min") + " " + format

                }, hours, minutes, false
            )

            timePickerDialog.setTitle(message)
            timePickerDialog.show()
        }

        private fun getDatePickerFormattedTime(DOB: String?): String? {
            val format: DateFormat = SimpleDateFormat("hh:mm")// 00:00
            var date: Date? = null
            try {
                date = format.parse(DOB)
            } catch (e: ParseException) {
            }
            val formatString: DateFormat = SimpleDateFormat("hh:mm")// 00:00 AM
            return formatString.format(date) ?: ""
        }


        fun getLongToFormattedDate(dateInMillis: Long?): String? {

            var dateString = ""
            if (dateInMillis != null && dateInMillis > 0) {
                dateString =
                    android.text.format.DateFormat.format("dd MMMM", Date(dateInMillis)).toString()
            }

            return dateString
        }

        fun getFormattedDateToLong(DOB: String?): Long {
            val format: DateFormat = SimpleDateFormat("dd MMM yyyy")
            var date: Date? = null
            try {
                date = format.parse(DOB)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
/*
            val timeZone: TimeZone = TimeZone.getTimeZone("GMT+5:30")
            TimeZone.setDefault(timeZone)
*/
            return date?.time ?: 0L
        }


        fun hideKeyboard(activity: Activity?) {
            if (activity != null) {
                val inputMethodManager =
                    activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                if (inputMethodManager.isAcceptingText) {
                    inputMethodManager.hideSoftInputFromWindow(
                        activity.currentFocus
                            ?.windowToken, 0
                    )
                }
            }
        }

        fun showChooseProfileDialog(
            isFragment: Boolean, activity: Activity, context: Context, fragment: Fragment
        ) {

            if (isFragment) {
                CropImage.activity().start(context, fragment)
            } else {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(activity)
            }

        }

        fun getBitmapFromUri(
            context: Context,
            uri: Uri?
        ): Bitmap? {
            var parcelFileDescriptor: ParcelFileDescriptor? = null
            return try {
                parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri!!, "r")
                val fileDescriptor =
                    parcelFileDescriptor!!.fileDescriptor
                val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                parcelFileDescriptor.close()
                image
            } catch (e: java.lang.Exception) {
                Log.e("File Read", "Failed to load image.", e)
                null
            } finally {
                try {
                    parcelFileDescriptor?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e("File Read", "Error closing ParcelFile Descriptor")
                }
            }
        }


        fun convertBitmapToBase64(imgBitmap: Bitmap): String? {
            val byteArrayOutputStream = ByteArrayOutputStream()
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.NO_WRAP)
        }

        fun selectImages(activity: Activity, context: Context, fragment: Fragment) {
            val items =
                arrayOf<CharSequence>("Take Photo", "Choose from Library", "Cancel")
            val builder =
                AlertDialog.Builder(activity)
            builder.setTitle("Add Photo")
            builder.setItems(items) { dialog, item ->
                when {
                    items[item] == "Take Photo" -> {
                        cameraIntent(fragment)
                    }
                    items[item] == "Choose from Library" -> {
                        galleryIntent(fragment)
                    }
                    items[item] == "Cancel" -> {
                        dialog.dismiss()
                    }
                }
            }
            builder.show()
        }

        private fun cameraIntent(fragment: Fragment) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            fragment.startActivityForResult(intent, Constants.CAMERA)
        }

        private fun galleryIntent(fragment: Fragment) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            fragment.startActivityForResult(
                Intent.createChooser(
                    intent,
                    "android.intent.action.SEND_MULTIPLE"
                ), Constants.SELECT_FILE
            )
        }

        fun getImageUri(
            inContext: Context,
            inImage: Bitmap?
        ): Uri? {
            val bytes = ByteArrayOutputStream()
            inImage?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                "Title",
                null
            )
            return Uri.parse(path)
        }


        fun getNameFromURI(activity: Activity, uri: Uri?): String? {
            val c: Cursor? = activity.contentResolver.query(uri!!, null, null, null, null)
            c?.moveToFirst()

            return c?.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }

        fun getData(value: String?): ArrayList<DishDetailsDTO>? {
            val dto = object : TypeToken<ArrayList<DishDetailsDTO>?>() {}.type
            return Gson().fromJson<ArrayList<DishDetailsDTO>>(value, dto)
        }

        fun setData(dto: ArrayList<DishDetailsDTO>?): String? {
            val gSon = Gson()
            return gSon.toJson(dto)
        }

        inline fun <reified T> getGenericData(value: String?): T? {
            val dto = object : TypeToken<T?>() {}.type
            return Gson().fromJson<T>(value, dto)
        }

        fun <T> setGenericData(data: T?): String? {
            val gSon = Gson()
            return gSon.toJson(data)
        }


        fun getOrderBundleDTO(value: String?): OrderBundleDTO? {
            val dto = object : TypeToken<OrderBundleDTO?>() {}.type
            return Gson().fromJson<OrderBundleDTO>(value, dto)
        }

        fun setOrderBundleDTO(dto: OrderBundleDTO?): String? {
            val gSon = Gson()
            return gSon.toJson(dto)
        }

        fun getLoginDTO(value: String?): LoginDTO? {
            val dto = object : TypeToken<LoginDTO?>() {}.type
            return Gson().fromJson<LoginDTO>(value, dto)
        }

        fun setLoginDTO(dto: LoginDTO?): String? {
            val gSon = Gson()
            return gSon.toJson(dto)
        }


/*
        fun <T> getAData(value: String?): T? {
            val dto = object : TypeToken<T?>() {}.type
            return Gson().fromJson<T>(value, dto)
        }

        fun <T> setAData(dto: T?): String? {
            val gSon = Gson()
            return gSon.toJson(dto)
        }
*/
    }

}
