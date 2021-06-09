package com.astrika.stywis_staff.models

import java.io.Serializable


class DayDTO : Serializable {
    var dayId: Long = 0
    var dayName: String = ""
    var dayIsCheckedOrClosed: Boolean = false
    var dayEnable: Boolean = true
    var timings = ArrayList<TimingDTO>()
    var assuredDiscountString: String = ""
    var assuredDiscountErrorMsg: String = ""
    var discountTimingErrorMsg: String = ""

    var isMarkAsClosedVisibility: Boolean = true

    // Discount
    var allDaySameDiscountFlag = true
    var discountDayAndTimings = ArrayList<DiscountDaysTimingDTO>()

    //
    var staffTimings = ArrayList<StaffTimingDTO>()

    override fun equals(other: Any?): Boolean {

        if (javaClass != other?.javaClass) {
            return false
        }

        other as DayDTO

        if (dayId != other.dayId) {
            return false
        }

        if (dayName != other.dayName) {
            return false
        }

        if (timings != other.timings) {
            return false
        }

        if (allDaySameDiscountFlag != other.allDaySameDiscountFlag) {
            return false
        }

        if (discountDayAndTimings != other.discountDayAndTimings) {
            return false
        }

        if (assuredDiscountErrorMsg != other.assuredDiscountErrorMsg) {
            return false
        }

        if (assuredDiscountString != other.assuredDiscountString) {
            return false
        }

        return true
    }
}

class TimingDTO : Serializable {
    var timingId: Long = 0
    var outletId: Long = 0
    var weekDay: Long = 0
    var opensAt: String = ""
    var closesAt: String = ""
}

class DiscountDaysTimingDTO : Serializable {
    var outletDiscountTimingId: Long = 0
    var assuredDiscount: Long = 0
    var assuredDiscountString: String = ""
    var discountApplicable: Long = 0
    var discountApplicableString: String = ""
    var weekDay: Long = 0
    var startsAt: String = ""
    var endsAt: String = ""
    var terms: String = ""
    var complimentaryApplicable: String = ""
    var discountApplicableErrorMsg: String = ""
    var complimentaryApplicableErrorMsg: String = ""
    var allDaySameDiscountFlag = true

}

enum class DaysEnum(val id: Int, val value: String) {
    MONDAY(0, "Monday"),
    TUESDAY(1, "Tuesday"),
    WEDNESDAY(2, "Wednesday"),
    THURSDAY(3, "Thursday"),
    FRIDAY(4, "Friday"),
    SATURDAY(5, "Saturday"),
    SUNDAY(6, "Sunday");
}

enum class DiscountEnum(val id: Long, val value: String) {
    MEMBERSHIP_DISCOUNTS(1L, "MEMBERSHIP DISCOUNTS"),
    CRAVX_CARD(2L, "CRAVX CARD DISCOUNTS"),
    HW_CARD(3L, "HW CARD DISCOUNTS"),
    ONE_DASHBOARD(4L, "ONE DASHBOARD"),
    INHOUSE_CARD(5L, "In-HOUSE CARDS");
}

class OutletTimingDTO {
    var outletId: Long = 0
    var timings = ArrayList<TimingDTO>()
}

class ClosedDatesDTO {

    var outletDateRestrictionId: Long = 0
    var outletId: Long = 0
    var date: String = ""
    var displayDate: String = ""
    var occasion: String = ""

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as ClosedDatesDTO

        if (outletDateRestrictionId != other.outletDateRestrictionId) {
            return false
        }

        if (outletId != other.outletId) {
            return false
        }

        if (date != other.date) {
            return false
        }

        if (displayDate != other.displayDate) {
            return false
        }

        if (occasion != other.occasion) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = outletDateRestrictionId.hashCode()
        result = 31 * result + outletId.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + displayDate.hashCode()
        result = 31 * result + occasion.hashCode()
        return result
    }
}

class ClosedDatesListingDTO {
    var outletDateRestriction: ArrayList<ClosedDatesDTO>? = null
}
