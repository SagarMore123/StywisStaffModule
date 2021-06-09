package com.astrika.stywis_staff.models


class CommonListingDTO {

    var start: Int = 0

    //    var length: Int = Constants.PAGINATION_PAGE_DATA_COUNT
    var length: Int = 0
    var addtionalSearch = ArrayList<CommonSearchDTO>()
    var search = ArrayList<CommonSearchDTO>()
    var status = true
    var sort: ArrayList<CommonSortDTO>? = null
    var defaultSort = CommonSortDTO()
    var statusCode: Int = -1
    var requestType: Int = -1

}

class CommonSearchDTO {
    var search = ""
    var searchCol = ""
}

class CommonSortDTO {
    var sortField = ""
    var sortOrder = ""
}

