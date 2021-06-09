package com.astrika.stywis_staff.utils

import androidx.lifecycle.MutableLiveData

class ErrorCheckUtils {

    companion object {
        const val EMAIL_REGEX =
            "[_a-zA-Z1-9]+(\\.[A-Za-z0-9]*)*@[A-Za-z0-9]+\\.[A-Za-z0-9]+(\\.[A-Za-z0-9]*)*"

        fun checkValidEmail(email: String?): String {
            return if (email == null) {
                "Please enter valid email address"
            } else if (email.trim { it <= ' ' }.isEmpty()) {
                "Please enter valid email address"
            } else if (email.trim { it <= ' ' }.length < 6) {
                "Email id is too short"
            } else if (!email.trim { it <= ' ' }.matches(EMAIL_REGEX.toRegex())) {
                "Enter valid email id"
            } else {
                ""
            }
        }

        fun checkValidPassword(password: String?): String {
            return when {
                password == null -> {
                    "Please enter Password"
                }
                password.isEmpty() -> {
                    "Please enter Password"
                }
                password.length < 8 -> {
                    "Password is too short"
                }
                else -> {
                    ""
                }
            }
        }

        fun checkValidOTP(otp: String?): String {
            return when {
                otp == null -> "Please enter OTP"
                otp.isEmpty() -> {
                    "Please enter OTP"
                }
                otp.length < 4 -> {
                    "Enter valid 4 digit OTP"
                }
                else -> {
                    ""
                }
            }
        }

        fun checkValidPincode(pincode: String?): String {
            return when {
                pincode == null -> "Please enter pincode"
                pincode.isEmpty() -> {
                    "Please enter pincode"
                }
                pincode.length < 6 -> {
                    "Enter valid pincode"
                }
                else -> {
                    ""
                }
            }
        }


        fun validateWithCommaNDot(mutableLiveData: MutableLiveData<String>): Boolean {
            var string = ""
            var isValid = false
            string = mutableLiveData.value.toString().trim().replace(",", "")
            string = string.trim().replace(".", "")
            string = string.trim().replace("!", "")
            string = string.trim().replace("@", "")
            string = string.trim().replace("#", "")
            string = string.trim().replace("$", "")
            string = string.trim().replace("%", "")
            string = string.trim().replace("^", "")
            string = string.trim().replace("&", "")
            string = string.trim().replace("*", "")
            string = string.trim().replace("(", "")
            string = string.trim().replace(")", "")
            string = string.trim().replace("_", "")
            string = string.trim().replace("+", "")
            string = string.trim().replace("-", "")
            string = string.trim().replace("=", "")
            string = string.trim().replace("{", "")
            string = string.trim().replace("}", "")
            string = string.trim().replace("[", "")
            string = string.trim().replace("]", "")
            string = string.trim().replace("|", "")
            string = string.trim().replace("\\", "")
            string = string.trim().replace(":", "")
            string = string.trim().replace(";", "")
            string = string.trim().replace("'", "")
            string = string.trim().replace("<", "")
            string = string.trim().replace(">", "")
            string = string.trim().replace("?", "")
            string = string.trim().replace("/", "")

            if (string.isBlank()) {
                isValid = true
            }
            return isValid
        }

        fun checkValidName(name: String?): String {
            return if (name == null) {
                "Please enter valid name"
            } else if (name.trim { it <= ' ' }.length < 3) {
                "Name is too short"
            } else {
                ""
            }
        }

        fun checkValidMobile(number: String?): String {
            return if (number == null)
                "Please enter mobile number"
            else if (number.trim { it <= ' ' }.isEmpty()) {
                "Please enter mobile number"
            } else if (number.trim { it <= ' ' }.length < 10) {
                "Please enter correct mobile number"
            } else if (!number.matches("^[6789]\\d{9}$".toRegex())) {
                "Please enter valid mobile number"
            } else {
                ""
            }

        }

    }
}