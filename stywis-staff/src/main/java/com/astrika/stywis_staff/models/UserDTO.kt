package com.astrika.stywis_staff.models

import com.astrika.stywis_staff.utils.Constants
import java.io.Serializable

data class UserDTO(
    var userId: Long?,
    var loginId: String?,
    var emailAddress: String?,
    var fullName: String?,
    var userLastName: String?,
    var userMiddleName: String?,
    var mobileNo: String?,
    var userFirstName: String?,
    var designation: String?,
    var language: String?,
    var timeZone: String?,
    var currency: String?,
    var userDateOfBirth: String? = "",
    var profileImage: ImageDTO?,
    var companyId: Long?,
    var managingOrganizationId: Long?,
    var reportingManagerId: Long?,
    var bio: String?,
    var knownLanguages: ArrayList<Long>?,
    var designationId: Long?,
    var totalExperience: String?,
    var workingSince: String?,
    var accessRoles: ArrayList<Long>?,
    var outletId: Long?,
    var productId: Long?,
    var outletAddress: AddressMasterDTO?,
    var mobileCCode: String?,
    var lastLoginDateTime: String?,
    var createdOn: String?,
    var staffTimingDTO: StaffTimingRequestDTO?,
    var productOutletTypeDTO: ProductOutletTypeDTO = ProductOutletTypeDTO()

) : Serializable


class ProductOutletTypeDTO {
    val productId = Constants.PRODUCT_ID_VALUE
}

class UserDetailsDTO : Serializable {

    var userId: Long? = null
    var userFirstName: String? = ""
    var userLastName: String? = ""
    var fullName: String? = null
    var mobileCode: String? = null
    var mobileNo: String? = ""
    var emailAddress: String? = ""
    var profileImage: ImageDTO? = null
    var sourceId = Constants.SOURCE
    val productId = Constants.PRODUCT_ID_VALUE
    var productOutletTypeDTO: ProductOutletTypeDTO = ProductOutletTypeDTO()

}


class StaffTimingRequestDTO(
    var timings: ArrayList<StaffTimingDTO>
)

class StaffTimingDTO {
    var timingId: Long = 0
    var staffId: Long = 0
    var weekDay: Long = 0
    var weekDayName: String = ""
    var opensAt: String = ""
    var closesAt: String = ""
}

class AddressMasterDTO : Serializable {

    var addressId: Long = 0
    var addressLine1 = ""
    var addressLine2 = ""
    var addressType: Long = 0
    var areaId: Long = 0
    var areaName = ""
    var cityId: Long = 0
    var cityName = ""
    var countryId: Long = 0
    var countryName = ""
    var landlineNo = ""
    var landmark = ""
    var latitude = ""
    var longitude = ""
    var mobileNo = ""
    var pincode: Long = 0
    var pincodeNo = ""
    var stateId: Long = 0
    var stateName = ""

}
