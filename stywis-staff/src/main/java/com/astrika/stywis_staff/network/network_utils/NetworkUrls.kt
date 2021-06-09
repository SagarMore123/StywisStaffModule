package com.astrika.stywis_staff.network.network_utils


const val SERVER_URL: String = "https://sit-api.checqk.com/edge-service/"  // sit server

//const val SERVER_IMG_URL: String = "https://uat-api.checqk.com/~cravx/public_html/"
const val SERVER_IMG_URL: String = "https://cravxfiles.s3.amazonaws.com/home/cravx/public_html"

//const val SERVER_URL: String = "http://103.146.177.39:7010/edge-service/"
//const val SERVER_IMG_URL: String = "http://103.146.177.39/~cravx/public_html/"

const val CATALOGUE_URL: String = SERVER_URL + "fashion-catalogue-service/"
const val BOOKING_URL: String = SERVER_URL + "booking-service/"
const val CHECK_IN_URL: String = SERVER_URL + "fashion-checkin-service/"
const val ORDER_URL: String = SERVER_URL + "fashion-order-service/"
const val DISCOUNT_URL: String = SERVER_URL + "fashion-outlet-discount-service/"
const val RETAIL_URL: String = SERVER_URL + "fashion-service/"
const val OUTLET_URL: String = SERVER_URL + "fashion-outlet-service/"
const val SVM_URL: String = SERVER_URL + "system-service/"
const val OTP_URL: String = SERVER_URL + "otp-service/"
const val OAUTH_URL: String = SERVER_URL + "oauth-server/"
const val WISHLIST_URL: String = SERVER_URL + "fashion-wishlist-service/"

const val USER_URL: String = SERVER_URL + "user-service/"
const val STAFF_URL: String = SERVER_URL + "restaurant-staff-service/"

//------------------------------------------------------------------------------------------


const val IS_FIRST_TIME_LOGIN_WITH_LOGIN_ID = OAUTH_URL + "isFirstTimeLoginWithLoginId"

//const val LOGIN_WITH_LOGIN_ID =  OAUTH_URL + "loginWithLoginId"
const val LOGIN_WITH_LOGIN_ID = OAUTH_URL + "login"
const val RESET_PASSWORD = OAUTH_URL + "resetPassword"
const val LOGIN_MASTERS = OAUTH_URL + "loginForMasters"
const val REFRESH_TOKEN = OAUTH_URL + "refreshToken"

// Masters

const val FETCH_SVM = SVM_URL + "listing/SystemValueMasters"
const val FETCH_VISIT_PURPOSE_MASTER = CHECK_IN_URL + "listing/VisitPurpose"
const val DESIGNATION_LISTING = USER_URL + "listing/Designation"

const val LOGOUT_USER_URL = OAUTH_URL + "customLogout"
const val GET_DRAWER_MENU = "get/MenuDrawer"

//const val GET_CHECK_IN_LISTING = CHECK_IN_URL + "listing/InCustomerRequest"
const val GET_CHECK_IN_LISTING = CHECK_IN_URL + "get/AllInCustomerRequest/ById"
const val CHANGE_STATUS_APPROVED = CHECK_IN_URL + "changeStatus/InCustomerRequest/Approved"
const val CHANGE_STATUS_REJECTED = CHECK_IN_URL + "changeStatus/InCustomerRequest/Rejected"
const val CHANGE_DINE_IN_STATUS = CHECK_IN_URL + "changeStatus/InCustomerRequest"
const val CHECK_OUT = CHECK_IN_URL + "changeStatus/InCustomerRequest/Checkedout"
const val VERIFY_GPIN_URL = CHECK_IN_URL + "verifyCheckIn/UniquePin"


const val GET_ALL_MENU_CATEGORIES = CATALOGUE_URL + "get/CatalogueCategory/ByOutletId"
const val GET_ALL_DISHES = CATALOGUE_URL + "get/Product/ByCatalogueCategoryId"
const val SAVE_DISH_AVAILABILITY = CATALOGUE_URL + "save/DishAvailability"
const val GET_CUSTOMIZATION_LISTING = CATALOGUE_URL + "listing/CatalogueCustomization"

const val GET_ORDER_DETAILS = ORDER_URL + "get/Order/ByInCustomerRequestId"
const val SAVE_ORDER_DETAILS = ORDER_URL + "save/Order"


// Profile

//const val GET_PROFILE_DETAILS = "get/User/ById/{userId}"
const val GET_PROFILE_DETAILS = STAFF_URL + "get/StaffUser/userId"

const val UPDATE_PROFILE_DETAILS = "user-service/update/User/ProfilePic"
const val GET_USER_BY_MOBILE = "user-service/get/User/ByMobile"
const val SAVE_VISITOR_DETAILS = "user-service/save/visitor"

const val SEND_OTP_URL = OTP_URL + "sendOtp"
const val VERIFY_OTP_URL = OTP_URL + "verifyOTP"
const val SAVE_STAFF_CHECKIN_VISITOR = CHECK_IN_URL + "save/StaffCheckInRequest"


// Bill Generation
const val GENERATE_BILl = ORDER_URL + "generateBillDetails"
const val GET_BILL_DETAILS_BY_ID = ORDER_URL + "get/BillDetails/ByInCustomerRequestId"
const val CHANGE_BILL_DETAILS_STATUS_PAID = ORDER_URL + "changeStatus/BillDetails/Paid"


// Discount Masters
const val FETCH_DISCOUNT_CATEGORIES = DISCOUNT_URL + "listing/OutletDiscountCategory"
const val FETCH_DISCOUNT_MEMBERSHIP_PLAN = RETAIL_URL + "listing/OutletMembershipPlan"
const val FETCH_CRAVX_CARDS_DISCOUNT_MEMBERSHIP_HOLDER_MASTERS = DISCOUNT_URL + "listing/CravxCard"
const val FETCH_HW_DISCOUNT_MEMBERSHIP_HOLDER_MASTERS = DISCOUNT_URL + "listing/HWCard"
const val FETCH_IN_HOUSE_DISCOUNT_MEMBERSHIP_HOLDER_MASTERS = DISCOUNT_URL + "listing/InHousesCard"

// Discount URLs
const val FETCH_OUTLET_APPLICABLE_DISCOUNTS_LIST =
    DISCOUNT_URL + "get/OutletDiscountDetails/OutletIdAndUserAndWeek"
const val FETCH_OUTLET_DISCOUNT_DETAILS_LIST = DISCOUNT_URL + "get/OutletDiscountDetails/OutletId"
const val FETCH_OUTLET_MEMBERSHIP_PLAN_MAPPING =
    RETAIL_URL + "get/OutletMembershipPlanMapping/ByOutletId"
const val FETCH_OUTLET_CORPORATE_MEMBERSHIP_OR_ONE_DASHBOARD =
    DISCOUNT_URL + "get/CorporateMembership/productIdAndoutletId"

const val FETCH_OUTLET_DETAILS = OUTLET_URL + "get/OutletInfo/Id"


//Wishlist
const val FETCH_WISHLIST_BY_USER_ID = WISHLIST_URL + "listing/Wishlist/ByUserId?"
const val CHANGE_WISHLIST_ITEM_STATUS_BY_ITEM_ID =
    WISHLIST_URL + "changeStatus/Item/ByItemIdAndStatus?"
const val DELETE_WISHLIST_ITEM_STATUS_BY_ITEM_ID = WISHLIST_URL + "changeActiveInactive/ItemId?"

// Stock Management

const val SAVE_STOCK_CUSTOMIZATIONS = CATALOGUE_URL + "save/stock"
const val FETCH_ALL_STOCK_CUSTOMIZATIONS_BY_PRODUCT_OR_DISH_ID =
    CATALOGUE_URL + "get/stock/ByProductIdAndOutletId"
const val FETCH_STOCK_CUSTOMIZATION_OPTION_MASTER =
    CATALOGUE_URL + "listing/CatalogueCustomizationOption"

