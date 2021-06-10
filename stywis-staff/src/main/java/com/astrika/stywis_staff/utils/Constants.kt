package com.astrika.stywis_staff.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.databinding.AlreadyLoginPopupLayoutStaffBinding
import com.astrika.stywis_staff.models.CommonListingDTO
import com.astrika.stywis_staff.models.CommonSearchDTO
import com.astrika.stywis_staff.models.CommonSortDTO
import com.astrika.stywis_staff.models.UserDTO
import com.astrika.stywis_staff.network.NetworkController
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class Constants {

    companion object {

        const val SPLASH_TIME_OUT: Long = 1000 * 3 // 3 sec
        const val PAGINATION_PAGE_DATA_COUNT: Int = 50
        const val PRODUCT_ID_VALUE = 95L
        private const val SHARED_PREF_NAME = "CravXStaffSharedPreferences"
        private lateinit var sharedPreferences: SharedPreferences
        const val DATABASE_NAME = "CravXStaffRoomDatabase"
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val IS_FIRST_TIME: String = "IS_FIRST_TIME"
        const val CATALOGUE_CATEGORY_TYPE: String = "CRAVX"

        const val DEVICE_KEY = "DEVICE_KEY"

        //        const val SOURCE = "STAFF_ANDROID_APP"
        const val SOURCE = "FASHIONSTAFFANDROIDAPP"
        const val ROLE = "OUTLETSTAFF"
        const val ACCESS_TOKEN_INVALID = "ACCESS_TOKEN_INVALID"

        const val SELECT_FILE = 1
        const val CAMERA = 2

        const val SELECTED_DROPDOWN_ITEM_RESULT_CODE = "selectedItemResultCode"
        const val SELECTED_DROPDOWN_ITEM = "selectedItem"
        const val IS_VIEW_DISCOUNT = "isViewDiscount"

        // Common listing default sort fields

        const val SORT_ORDER_ASC = "asc"
        const val SORT_ORDER_DESC = "desc"

        const val VISIT_PURPOSE_SORT_FIELD = "visitPurposeId"
        const val DESIGNATION_SORT_FIELD = "designationId"
        const val DISCOUNT_CATEGORY_SORT_FIELD = "outletDiscountCategoryId"
        const val DISCOUNT_MEMBERSHIP_PLAN_SORT_FIELD = "membershipId"
        const val CRAVX_CARDS_DISCOUNT_MEMBERSHIP_HOLDER_SORT_FIELD = "cravxDiscountCardId"
        const val HW_DISCOUNT_MEMBERSHIP_HOLDER_SORT_FIELD = "hwDiscountCardId"
        const val IN_HOUSE_DISCOUNT_MEMBERSHIP_HOLDER_SORT_FIELD = "inHouseDiscountCardId"
        const val CUSTOMIZATION_PRODUCT_ID_COLUMN = "productId"


        const val LOG_OUT = "Logout"
        const val LOGIN_USER_ID = "LOGIN_IN_USER_ID"
        const val USER_DTO = "USER_DTO"
        const val LOGIN_ID = "LOGIN_ID"
        const val EMAIL_ID = "EMAIL_ID"
        const val FIRST_NAME = "FIRST_NAME"
        const val LAST_NAME = "LAST_NAME"
        const val MOBILE_NO = "MOBILE_NO"
        const val OUTLET_ID = "OUTLET_ID"
        const val OUTLET_NAME = "OUTLET_NAME"
        const val PRODUCT_ID = "PRODUCT_ID"
        const val IS_FORGOT_PASS = "IS_FORGOT_PASS"
        const val LOGIN_RESPONSE = "LOGIN_RESPONSE"
        const val IS_SPLASH_MASTER = "IS_SPLASH_MASTER"
        const val IS_SAVE_ORDER_SUCCESS = "IS_ORDER_BACK_PRESSED"
        const val IS_MY_TABLES = "IS_MY_TABLES"

        const val DISH_FLAG_ID_COLUMN = "dishFlagId"
        const val TABLE_ID_COLUMN = "tableId"
        const val OUTLET_ID_COLUMN = "outletId"

        const val OUTLET_DASHBOARD = "Outlet Dashboard"
        const val ORDER_DISH_LIST = "ORDER_DISH_LIST"
        const val LOGIN_DTO = "LOGIN_DTO"
        const val ORDER_BUNDLE_DTO = "ORDER_BUNDLE_DTO"
        const val TAKE_ORDER = "Take Order"
        const val STOCK_MANAGEMENT = "Stock Management"
        const val TABLE_MANAGEMENT = "Table Management"

        const val OUTLET_DISCOUNT_DETAILS_DTO_KEY = "OUTLET_DISCOUNT_DETAILS_DTO_KEY"
        const val DISCOUNT_TIMING_DTO_KEY = "DISCOUNT_TIMING_DTO_KEY"
        const val DISCOUNT_PLAN_ELIGIBLE_NAME_KEY = "DISCOUNT_PLAN_ELIGIBLE_NAME_KEY"
        const val WEEK_NAME_KEY = "WEEK_NAME_KEY"
        const val CORPORATE_MEMBERSHIPS_ONE_DASHBOARD_MASTERS_KEY = "ONE_DASHBOARD_MASTER_KEY"

        // Status Code
        const val STATUS_CODE_PENDING = 0
        const val STATUS_CODE_APPROVED = 1
        const val STATUS_CODE_CANCELLED = 2
        const val STATUS_CODE_REJECTED = 3
        const val STATUS_CODE_COMPLETED = 4
        const val STATUS_CODE_BILL_PAID = 5
        const val STATUS_CODE_CHECKEDOUT = 6


        const val APPLICABLE_DISCOUNT_CODE = 2001
        const val CORPORATE_MEMBERSHIP_ONE_DASHBOARD_CODE = 2002
        const val VISIT_PURPOSE_CODE = 2003
        const val STOCK_CUSTOMIZATION_OPTIONS_CODE = 2004

        const val ACTION_APPROVE_CHECKIN = "ACTION_APPROVE_CHECKIN"
        const val ACTION_CONFIRM_CHECKIN = "ACTION_CONFIRM_CHECKIN"
        const val ACTION_REJECT_CHECKIN = "ACTION_REJECT_CHECKIN"
        const val ACTION_DONE = "ACTION_DONE"
        const val ACTION_CHECKEDOUT = "ACTION_CHECKEDOUT"
        const val ACTION_TAKE_ORDER = "ACTION_TAKE_ORDER"
        const val ACTION_GENERATE_BILL = "ACTION_GENERATE_BILL"
        const val ACTION_RE_ASSIGN = "ACTION_RE_ASSIGN"

        const val ORDER_PLACED = "PLACED"
        const val ORDER_IN_PROGRESS = "IN_PROGRESS"
        const val DINE_IN_CUST_REQ_ID = "inCustomerRequestId"
        const val PROFILE_IMAGE_PATH = "PROFILE_IMAGE_PATH"
        var PROFILE_IMAGE_URI: Uri? = null

        var isTableManagement: Boolean? = false
        var userId: Long? = 0
        const val CHECK_IN_DTO = "CHECK_IN_DTO"
        const val CHECK_IN_OTP = "CheckInOtp"
        const val CHECK_IN_USER_ID = "CHECK_IN_USER_ID"


        inline fun <reified T : Activity> changeActivity(activity: Activity) {
            val intent = Intent(activity, T::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        }

        inline fun <reified T : Activity> changeActivityAndFinish(activity: Activity) {
            val intent = Intent(activity, T::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
            activity.finish()
        }


        fun redirectToAutocompleteViewActivity(activity: Activity, requestCode: Int) {
            val intent = Intent(activity, AutocompleteViewActivity::class.java)
            intent.putExtra(SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            activity.startActivityForResult(intent, requestCode)
        }

        fun getCommonListingDTO(context: Context, requestType: Int): CommonListingDTO {

            val commonListingDTO = CommonListingDTO()
            val commonSortList = ArrayList<CommonSortDTO>()
            val commonSortDTO = CommonSortDTO()
            commonSortDTO.sortField = DINE_IN_CUST_REQ_ID
            commonSortDTO.sortOrder = SORT_ORDER_DESC
            commonSortList.add(commonSortDTO)
            commonListingDTO.sort = commonSortList
            commonListingDTO.length = PAGINATION_PAGE_DATA_COUNT

            // Search
            val commonSearchDTOList = ArrayList<CommonSearchDTO>()
            val commonSearchDTO = CommonSearchDTO()
            commonSearchDTO.search = getOutletId(context).toString()
            commonSearchDTO.searchCol = OUTLET_ID_COLUMN
            commonSearchDTOList.add(commonSearchDTO)
            commonListingDTO.search = commonSearchDTOList

            commonListingDTO.requestType = requestType


            return commonListingDTO
        }


        fun showToastMessage(context: Context?, message: String?) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        }

        fun getAccessToken(context: Context?): String? {
            sharedPreferences = getSharedPreferences(context!!)
            return try {
                var accessToken = "${decrypt(sharedPreferences.getString(ACCESS_TOKEN, ""))}"
                accessToken = if (accessToken.contains("Bearer")) {
                    "${decrypt(sharedPreferences.getString(ACCESS_TOKEN, ""))}"
                } else {
                    "Bearer ${decrypt(sharedPreferences.getString(ACCESS_TOKEN, ""))}"
                }
                accessToken
            } catch (e: Exception) {
                ""
            }
        }

        fun getRefreshToken(context: Context?): String? {
            sharedPreferences = getSharedPreferences(context!!)
            return try {
                "${decrypt(sharedPreferences.getString(REFRESH_TOKEN, ""))}"

            } catch (e: Exception) {
                ""
            }
        }

        fun getIsFirstTime(context: Context?): Boolean {
            sharedPreferences = getSharedPreferences(context!!)
            return try {
                decrypt(sharedPreferences.getString(IS_FIRST_TIME, ""))?.toBoolean() ?: true
            } catch (e: Exception) {
                true
            }
        }

        fun getOutletId(context: Context): Long {
            sharedPreferences = getSharedPreferences(context)
            return try {
                decrypt(sharedPreferences.getString(OUTLET_ID, ""))?.toLong() ?: 0L
            } catch (e: Exception) {
                0L
            }
        }


        fun setUserDTOInSharePreferences(context: Context, userDTO: UserDTO?) {

            try {

                sharedPreferences = getSharedPreferences(context)

                userDTO?.loginId?.let {
                    sharedPreferences.edit().putString(
                        LOGIN_ID,
                        encrypt(userDTO.loginId)
                    ).apply()
                }
                userDTO?.emailAddress?.let {
                    sharedPreferences.edit().putString(
                        EMAIL_ID,
                        encrypt(userDTO.emailAddress)
                    ).apply()
                }
                userDTO?.userFirstName?.let {
                    sharedPreferences.edit().putString(
                        FIRST_NAME,
                        encrypt(userDTO.userFirstName)
                    ).apply()
                }
                userDTO?.userLastName?.let {
                    sharedPreferences.edit().putString(
                        LAST_NAME,
                        encrypt(userDTO.userLastName)
                    ).apply()
                }
                userDTO?.mobileNo?.let {
                    sharedPreferences.edit().putString(
                        MOBILE_NO,
                        encrypt(userDTO.mobileNo)
                    ).apply()
                }
                userDTO?.outletId?.let {
                    NetworkController.outletId = it
                    sharedPreferences.edit().putString(
                        OUTLET_ID,
                        encrypt(userDTO.outletId.toString())
                    ).apply()
                }
                userDTO?.productId?.let {
                    sharedPreferences.edit().putString(
                        PRODUCT_ID,
                        encrypt(userDTO.productId.toString())
                    ).apply()
                }

                //Profile
                userDTO?.profileImage?.base64 = ""
                userDTO?.profileImage?.path.let {
                    sharedPreferences.edit().putString(
                        PROFILE_IMAGE_PATH,
                        encrypt(userDTO?.profileImage?.path)
                    ).apply()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        private const val secretKey = "3f26328d2d014be5"
        private const val salt = "a805e0093325872e"
        private const val ivKey = "0123456789ABCDEF"

        fun passwordEncrypt(strToEncrypt: String): String? {
            Log.i("START method encrypt", "")
            var encryptedText = ""
            try {
//                val iv = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
//                val ivspec = IvParameterSpec(iv)
                val iv: ByteArray = ivKey.toByteArray()
                val ivspec = IvParameterSpec(iv)

                val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                val spec: KeySpec = PBEKeySpec(
                    secretKey.toCharArray(),
                    salt.toByteArray(),
                    65536,
                    256
                )
                // spec = new PBEKeySpec(secretKey.toCharArray());
                val tmp = factory.generateSecret(spec)
                val secretKey = SecretKeySpec(tmp.encoded, "AES")
                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec)

                val encryptedByte: ByteArray =
                    cipher.doFinal(strToEncrypt.toByteArray(charset("UTF-8")))
                encryptedText = Base64.encodeToString(encryptedByte, Base64.NO_WRAP)

                return encryptedText

            } catch (e: Exception) {
                Log.i("Error while encrypting:", e.message ?: "")
            }
            Log.i("START method encrypt", "")

            return encryptedText
        }

        @Throws(Exception::class)
        fun encrypt(plainText: String?): String? {
            var encryptedText = ""
            if (plainText != null) {
                val secretKey: SecretKey = getKey()
                val plainTextByte = plainText.toByteArray()
                val cipher: Cipher = Cipher.getInstance("AES")
                cipher.init(Cipher.ENCRYPT_MODE, secretKey)
                val encryptedByte: ByteArray = cipher.doFinal(plainTextByte)
                //Base64.Encoder encoder = Base64.getEncoder();
                encryptedText = Base64.encodeToString(encryptedByte, Base64.URL_SAFE)
            }
            return encryptedText
        }

        @Throws(Exception::class)
        fun decrypt(encryptedText: String?): String? {
            var decryptedText: String?
            if (encryptedText != null && !encryptedText.equals("", ignoreCase = true)) {
                val secretKey: SecretKey = getKey()
                val cipher: Cipher = Cipher.getInstance("AES")
                // Base64.Decoder decoder = Base64.getDecoder();
                val encryptedTextByte: ByteArray =
                    Base64.decode(encryptedText, Base64.URL_SAFE)
                cipher.init(Cipher.DECRYPT_MODE, secretKey)
                val decryptedByte: ByteArray = cipher.doFinal(encryptedTextByte)
                decryptedText = String(decryptedByte)
                return decryptedText
            }
            return null
        }

        @Throws(NoSuchAlgorithmException::class)
        private fun getKey(): SecretKey {
            val md: MessageDigest = MessageDigest.getInstance("SHA-256")
            md.update("reliance@123".toByteArray())
            val encryptionKey: ByteArray = md.digest()
            return SecretKeySpec(encryptionKey, "AES")
        }

        fun disableUserInteraction(mActivity: Activity) {
            mActivity.window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }

        fun enableUserInteraction(mActivity: Activity) {
            mActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        //    public static void showLogoutDialog(final Activity activity) {
        //        LayoutInflater inflater = activity.getLayoutInflater();
        //        View alertLayout = inflater.inflate(R.layout.logout_dialog, null);
        //        Button cancelBtn = alertLayout.findViewById(R.id.cancel_button);
        //        Button confirmBtn = alertLayout.findViewById(R.id.confirm_button);
        //        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        //        // this is set the view from XML inside AlertDialog
        //        alert.setView(alertLayout);
        //        // disallow cancel of AlertDialog on click of back button and outside touch
        //        alert.setCancelable(false);
        //
        //        final AlertDialog dialog = alert.create();
        //        cancelBtn.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                dialog.dismiss();
        //            }
        //        });
        //
        //        confirmBtn.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                Intent intent = new Intent(activity, EmailVerificationActivity.class);
        //                activity.startActivity(intent);
        //                dialog.dismiss();
        //                activity.finish();
        //                //shared preferences to be cleared once confirmed
        //                clearSharedPrefs(activity);
        //
        //            }
        //        });
        //        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //        dialog.show();
        //    }
        fun clearSharedPrefs(context: Context) {
            try {
                context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit().clear()
                    .apply()
                getSharedPreferences(context).edit()
                    ?.putString(IS_FIRST_TIME, encrypt(false.toString()))?.apply()
            } catch (e: Exception) {
            }
        }

        fun clearAllPrefsExceptKey(context: Context, requiredKey: String) {
            val prefs: Map<String?, *> =
                context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).all
            for ((key) in prefs) {
                if (!key.equals(requiredKey, true)) {
                    context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit()
                        .remove(key).apply()
                }
            }

        }


        fun showCommonDialog(activity: Activity, isYes: MutableLiveData<Boolean>, message: String) {
            val alreadyLoginPopUpLayoutStaffBinding: AlreadyLoginPopupLayoutStaffBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(activity), R.layout.already_login_popup_layout_staff, null, false
                )
            val view: View = alreadyLoginPopUpLayoutStaffBinding.root
            val alert =
                AlertDialog.Builder(activity)
            // this is set the view from XML inside AlertDialog
            alert.setView(view)
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false)
            val dialog = alert.create()
            alreadyLoginPopUpLayoutStaffBinding.titleDescription.text = message
            alreadyLoginPopUpLayoutStaffBinding.yesBtn.setOnClickListener {
                isYes.value = true
                dialog.dismiss()
            }
            alreadyLoginPopUpLayoutStaffBinding.noBtn.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }


        fun getTypeOfSeater(name: String?): String {
            val list: List<String> = name!!.split("_")
            var typeOfSeater = list[0]
            when (typeOfSeater) {
                "TWO" -> typeOfSeater = "2"
                "THREE" -> typeOfSeater = "3"
                "FOUR" -> typeOfSeater = "4"
                "FIVE" -> typeOfSeater = "5"
                "SIX" -> typeOfSeater = "6"
                "SEVEN" -> typeOfSeater = "7"
                "EIGHT" -> typeOfSeater = "8"
                "NINE" -> typeOfSeater = "9"
                "TEN" -> typeOfSeater = "10"
                "ELEVEN" -> typeOfSeater = "11"
                "TWELVE" -> typeOfSeater = "12"
                "THIRTEEN" -> typeOfSeater = "13"
                "FOURTEEN" -> typeOfSeater = "14"
                "FIFTEEN" -> typeOfSeater = "15"
                "SIXTEEN" -> typeOfSeater = "16"
                "SEVENTEEN" -> typeOfSeater = "17"
                "EIGHTTEEN" -> typeOfSeater = "18"
                "NINETEEN" -> typeOfSeater = "19"
                "TWENTY" -> typeOfSeater = "20"
            }
            return typeOfSeater
        }

        fun getTypeOfSeaterInFullName(name: String?): String {
            val list: List<String> = name!!.split("_")
            var typeOfSeater = list[0]
            when (typeOfSeater) {
                "2" -> typeOfSeater = "TWO"
                "3" -> typeOfSeater = "THREE"
                "4" -> typeOfSeater = "FOUR"
                "5" -> typeOfSeater = "FIVE"
                "6" -> typeOfSeater = "SIX"
                "7" -> typeOfSeater = "SEVEN"
                "8" -> typeOfSeater = "EIGHT"
                "9" -> typeOfSeater = "NINE"
                "10" -> typeOfSeater = "TEN"
                "11" -> typeOfSeater = "ELEVEN"
                "12" -> typeOfSeater = "TWELVE"
                "13" -> typeOfSeater = "THIRTEEN"
                "14" -> typeOfSeater = "FOURTEEN"
                "15" -> typeOfSeater = "FIFTEEN"
                "16" -> typeOfSeater = "SIXTEEN"
                "17" -> typeOfSeater = "SEVENTEEN"
                "18" -> typeOfSeater = "EIGHTTEEN"
                "19" -> typeOfSeater = "NINETEEN"
                "20" -> typeOfSeater = "TWENTY"
            }
            return typeOfSeater
        }

        fun getDeviceKey(context: Context?): String {
            sharedPreferences = getSharedPreferences(context!!)
            return try {
                decrypt(sharedPreferences.getString(DEVICE_KEY, "")) ?: ""
            } catch (e: Exception) {
                ""
            }
        }
    }

}