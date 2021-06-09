package com.astrika.stywis_staff.utils

import android.app.Activity
import android.app.Application
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.master_controller.source.MasterRepository
import com.astrika.stywis_staff.models.VisitPurposeDTO
import com.astrika.stywis_staff.models.stock.StockCustomizationMasterDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback

class AutocompleteViewModel(
    var activity: Activity,
    application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var masterRepository: MasterRepository
) :
    GenericBaseObservable(
        application,
        owner,
        view
    ) {

    var autocompleteText = MutableLiveData("")
    var noResultsFoundVisible = ObservableBoolean(false)

    /*

        var cuisineArrayList = ArrayList<CuisineMasterDTO>()
        var cuisineListMutableLiveData = MutableLiveData<List<CuisineMasterDTO>>()

        var countryMasterArrayList = ArrayList<CountryMasterDTO>()
        var countryMasterListMutableLiveData = MutableLiveData<List<CountryMasterDTO>>()

        var stateMasterArrayList = ArrayList<StateMasterDTO>()
        var stateMasterListMutableLiveData = MutableLiveData<List<StateMasterDTO>>()

        var cityMasterArrayList = ArrayList<CityMasterDTO>()
        var cityMasterListMutableLiveData = MutableLiveData<List<CityMasterDTO>>()

        var areaMasterArrayList = ArrayList<AreaMasterDTO>()
        var areaMasterListMutableLiveData = MutableLiveData<List<AreaMasterDTO>>()

        var socialMediaMasterArrayList = ArrayList<SocialMediaMasterDTO>()
        var socialMediaMasterListMutableLiveData = MutableLiveData<List<SocialMediaMasterDTO>>()

        var groupRolesArrayList = ArrayList<GroupRolesDTO>()
        var groupRolesListMutableLiveData = MutableLiveData<List<GroupRolesDTO>>()

    */

//    var commonArrayList = ArrayList<CommonDialogDTO>()
//    var commonMutableList = MutableLiveData<List<CommonDialogDTO>>()

//    var knownLanguagesArrayList = ArrayList<KnownLanguagesDTO>()
//    var knownLanguagesListMutableLiveData = MutableLiveData<List<KnownLanguagesDTO>>()

//    var designationArrayList = ArrayList<DesignationDTO>()
//    var designationListMutableLiveData = MutableLiveData<List<DesignationDTO>>()

/*
    var corporateMembershipOneDashboardDiscountMasterArrayList =
        ArrayList<CorporateMembershipOneDashboardDTO>()
    var corporateMembershipOneDashboardDiscountMasterListMutableLiveData =
        MutableLiveData<List<CorporateMembershipOneDashboardDTO>>()
*/

    var visitPurposeArrayList = ArrayList<VisitPurposeDTO>()
    var visitPurposeArrayListMutableLiveData = MutableLiveData<List<VisitPurposeDTO>>()

    var stockCustomizationArrayList = ArrayList<StockCustomizationMasterDTO>()
    var stockCustomizationListMutableLiveData = MutableLiveData<List<StockCustomizationMasterDTO>>()

    init {

//        populateMainList(0)
    }


    fun populateMainList(
        resultCode: Int, productId: Long,
        countryId: Long, stateId: Long, cityId: Long
    ) {


        when (resultCode) {
/*

            Constants.CORPORATE_MEMBERSHIP_ONE_DASHBOARD_CODE -> {
                corporateMembershipOneDashboardDiscountMasterListMutableLiveData.value =
                    corporateMembershipOneDashboardDiscountMasterArrayList ?: arrayListOf()
            }
*/

            Constants.VISIT_PURPOSE_CODE -> {

                masterRepository.fetchAllVisitPurposeMasterDataLocal(object :
                    IDataSourceCallback<List<VisitPurposeDTO>> {

                    override fun onDataFound(data: List<VisitPurposeDTO>) {
                        visitPurposeArrayList.clear()
                        visitPurposeArrayList = data as ArrayList<VisitPurposeDTO>
                        visitPurposeArrayListMutableLiveData.value = visitPurposeArrayList
                    }

                    override fun onError(error: String) {
                        getmSnackbar().value = error
                    }
                })

            }


            Constants.STOCK_CUSTOMIZATION_OPTIONS_CODE -> {

                masterRepository.fetchStockCustomizationOptionsMasterListDataByProductIdLocal(
                    productId,
                    object : IDataSourceCallback<List<StockCustomizationMasterDTO>> {

                        override fun onDataFound(data: List<StockCustomizationMasterDTO>) {
                            stockCustomizationArrayList.clear()
                            stockCustomizationArrayList =
                                data as ArrayList<StockCustomizationMasterDTO>
                            stockCustomizationListMutableLiveData.value =
                                stockCustomizationArrayList
                        }

                        override fun onError(error: String) {
                            getmSnackbar().value = error
                        }
                    })


            }


/*
            Constants.COUNTRY_CODE -> {
                masterRepository.fetchCountryMasterDataLocal(object :
                    IDataSourceCallback<List<CountryMasterDTO>> {

                    override fun onDataFound(data: List<CountryMasterDTO>) {
                        countryMasterArrayList.clear()
                        countryMasterArrayList = data as ArrayList<CountryMasterDTO>
                        countryMasterListMutableLiveData.value = countryMasterArrayList
                    }

                    override fun onError(error: String) {
                        getmSnackbar().value = error
                    }
                })
            }

            Constants.STATE_CODE -> {

                masterRepository.fetchStateMasterByCountryIdLocal(countryId ?: 0, object :
                    IDataSourceCallback<List<StateMasterDTO>> {

                    override fun onDataFound(data: List<StateMasterDTO>) {
                        stateMasterArrayList.clear()
                        stateMasterArrayList = data as ArrayList<StateMasterDTO>
                        stateMasterListMutableLiveData.value = stateMasterArrayList
                    }

                    override fun onError(error: String) {
                        getmSnackbar().value = error
                    }
                })

            }

            Constants.CITY_CODE -> {

                masterRepository.fetchCityMasterByStateIdLocal(stateId ?: 0, object :
                    IDataSourceCallback<List<CityMasterDTO>> {

                    override fun onDataFound(data: List<CityMasterDTO>) {
                        cityMasterArrayList.clear()
                        cityMasterArrayList = data as ArrayList<CityMasterDTO>
                        cityMasterListMutableLiveData.value = cityMasterArrayList
                    }

                    override fun onError(error: String) {
                        getmSnackbar().value = error
                    }
                })


            }

            Constants.AREA_CODE -> {


                masterRepository.fetchAreaMasterByStateIdLocal(cityId ?: 0, object :
                    IDataSourceCallback<List<AreaMasterDTO>> {

                    override fun onDataFound(data: List<AreaMasterDTO>) {
                        areaMasterArrayList.clear()
                        areaMasterArrayList = data as ArrayList<AreaMasterDTO>
                        areaMasterListMutableLiveData.value = areaMasterArrayList
                    }

                    override fun onError(error: String) {
                        getmSnackbar().value = error
                    }
                })


            }

            Constants.SOCIAL_MEDIA_CODE -> {


                masterRepository.fetchSocialMediaMastersLocal(object :
                    IDataSourceCallback<List<SocialMediaMasterDTO>> {

                    override fun onDataFound(data: List<SocialMediaMasterDTO>) {
                        socialMediaMasterArrayList.clear()
                        socialMediaMasterArrayList = data as ArrayList<SocialMediaMasterDTO>
                        socialMediaMasterListMutableLiveData.value = socialMediaMasterArrayList
                    }

                    override fun onError(error: String) {
                        getmSnackbar().value = error
                    }
                })


            }

            Constants.CUISINE_CODE -> {

                // Cuisine
                masterRepository.fetchCuisineMasterDataLocal(object :
                    IDataSourceCallback<List<CuisineMasterDTO>> {

                    override fun onDataFound(data: List<CuisineMasterDTO>) {
                        cuisineArrayList.clear()
                        cuisineArrayList = data as ArrayList<CuisineMasterDTO>
                        cuisineListMutableLiveData.value = cuisineArrayList
                    }

                    override fun onError(error: String) {
                        getmSnackbar().value = error
                    }
                })
            }

            Constants.GROUP_ROLE_CODE -> {

                // Group Role
                masterRepository.fetchGroupRolesMasterDataLocal(object :
                    IDataSourceCallback<List<GroupRolesDTO>> {

                    override fun onDataFound(data: List<GroupRolesDTO>) {
                        groupRolesArrayList.clear()
                        groupRolesArrayList = data as ArrayList<GroupRolesDTO>
                        groupRolesListMutableLiveData.value = groupRolesArrayList
                    }

                    override fun onError(error: String) {
                        getmSnackbar().value = error
                    }
                })
            }

            Constants.KNOWN_LANGUAGES_CODE -> {

                // Group Role
                masterRepository.fetchKnownLanguagesMasterDataLocal(object :
                    IDataSourceCallback<List<KnownLanguagesDTO>> {

                    override fun onDataFound(data: List<KnownLanguagesDTO>) {
                        */
/*knownLanguagesArrayList.clear()
                        knownLanguagesArrayList = data as ArrayList<KnownLanguagesDTO>
                        knownLanguagesListMutableLiveData.value = knownLanguagesArrayList
                        *//*

                        var list = arrayListOf<CommonDialogDTO>()
                        for (language in data){
                            list.add(
                                CommonDialogDTO(
                                    language.languageId,
                                    language.languageName
                                )
                            )
                        }
                        commonArrayList = list
                        commonMutableList.value = list
                    }

                    override fun onError(error: String) {
                        getmSnackbar().value = error
                    }
                })
            }

            Constants.DESIGNATION_CODE -> {

                // Known Languages
                masterRepository.fetchDesignationMasterDataLocal(object :
                    IDataSourceCallback<List<DesignationDTO>> {

                    override fun onDataFound(data: List<DesignationDTO>) {
                        */
/*designationArrayList.clear()
                        designationArrayList = data as ArrayList<DesignationDTO>
                        designationListMutableLiveData.value = designationArrayList*//*

                        var list = arrayListOf<CommonDialogDTO>()
                        for (designation in data){
                            list.add(
                                CommonDialogDTO(
                                    designation.designationId,
                                    designation.designationName
                                )
                            )
                        }
                        commonMutableList.value = list
                    }

                    override fun onError(error: String) {
                        getmSnackbar().value = error
                    }
                })
            }
            Constants.GROUP_ROLE_STAFF_CODE -> {

                // Group Role
                masterRepository.fetchGroupRolesStaffMasterDataLocal(object :
                    IDataSourceCallback<List<GroupRolesStaffDTO>> {

                    override fun onDataFound(data: List<GroupRolesStaffDTO>) {
                        */
/*groupRolesArrayList.clear()
                        groupRolesArrayList = data as ArrayList<GroupRolesDTO>
                        groupRolesListMutableLiveData.value = groupRolesArrayList*//*


                        var list = arrayListOf<CommonDialogDTO>()
                        for (groupRoleStaff in data){
                            list.add(
                                CommonDialogDTO(
                                    groupRoleStaff.id,
                                    groupRoleStaff.name
                                )
                            )
                        }
                        commonMutableList.value = list
                    }

                    override fun onError(error: String) {
                        getmSnackbar().value = error
                    }
                })
            }
*/

        }
    }


    fun searchByText(resultCode: Int, searchQuery: String?) {

        when (resultCode) {

/*

            Constants.CORPORATE_MEMBERSHIP_ONE_DASHBOARD_CODE -> {

                if (!searchQuery.isNullOrEmpty()) {
                    val searchedDataList = ArrayList<CorporateMembershipOneDashboardDTO>()
                    for (item in corporateMembershipOneDashboardDiscountMasterArrayList) {
                        if ((item.corporateName?.contains(searchQuery, true) == true)) {
                            searchedDataList.add(item)
                        }
                    }

                    noResultsFoundVisible.set(searchedDataList.isEmpty())
                    corporateMembershipOneDashboardDiscountMasterListMutableLiveData.value =
                        searchedDataList

                } else {
                    noResultsFoundVisible.set(false)
                    corporateMembershipOneDashboardDiscountMasterListMutableLiveData.value =
                        corporateMembershipOneDashboardDiscountMasterArrayList
                }
            }
*/

            Constants.VISIT_PURPOSE_CODE -> {

                if (!searchQuery.isNullOrEmpty()) {
                    val searchedDataList = ArrayList<VisitPurposeDTO>()
                    for (item in visitPurposeArrayList) {
                        if ((item.name?.contains(searchQuery, true) == true)) {
                            searchedDataList.add(item)
                        }
                    }

                    noResultsFoundVisible.set(searchedDataList.isEmpty())
                    visitPurposeArrayListMutableLiveData.value = searchedDataList

                } else {
                    noResultsFoundVisible.set(false)
                    visitPurposeArrayListMutableLiveData.value = visitPurposeArrayList
                }
            }


            Constants.STOCK_CUSTOMIZATION_OPTIONS_CODE -> {

                if (!searchQuery.isNullOrEmpty()) {
                    val searchedDataList = ArrayList<StockCustomizationMasterDTO>()
                    for (item in stockCustomizationArrayList) {
                        if ((item.customizeOptionName?.contains(searchQuery, true) == true)) {
                            searchedDataList.add(item)
                        }
                    }

                    noResultsFoundVisible.set(searchedDataList.isEmpty())
                    stockCustomizationListMutableLiveData.value = searchedDataList

                } else {
                    noResultsFoundVisible.set(false)
                    stockCustomizationListMutableLiveData.value = stockCustomizationArrayList
                }
            }

/*

            Constants.CUISINE_CODE -> {

                if (!searchQuery.isNullOrEmpty()) {
                    val searchedDataList = ArrayList<CuisineMasterDTO>()
                    for (item in cuisineArrayList) {
                        if ((item.cuisineName.contains(searchQuery, true))) {
                            searchedDataList.add(item)
                        }
                    }

                    noResultsFoundVisible.set(searchedDataList.isEmpty())
                    cuisineListMutableLiveData.value = searchedDataList

                } else {
                    noResultsFoundVisible.set(false)
                    cuisineListMutableLiveData.value = cuisineArrayList
                }
            }

            Constants.COUNTRY_CODE -> {

                if (!searchQuery.isNullOrEmpty()) {
                    val searchedDataList = ArrayList<CountryMasterDTO>()
                    for (item in countryMasterArrayList) {
                        if ((item.countryName.contains(searchQuery, true))) {
                            searchedDataList.add(item)
                        }
                    }

                    noResultsFoundVisible.set(searchedDataList.isEmpty())
                    countryMasterListMutableLiveData.value = searchedDataList

                } else {
                    noResultsFoundVisible.set(false)
                    countryMasterListMutableLiveData.value = countryMasterArrayList
                }
            }

            Constants.STATE_CODE -> {

                if (!searchQuery.isNullOrEmpty()) {
                    val searchedDataList = ArrayList<StateMasterDTO>()
                    for (item in stateMasterArrayList) {
                        if ((item.stateName.contains(searchQuery, true))) {
                            searchedDataList.add(item)
                        }
                    }

                    noResultsFoundVisible.set(searchedDataList.isEmpty())
                    stateMasterListMutableLiveData.value = searchedDataList

                } else {
                    noResultsFoundVisible.set(false)
                    stateMasterListMutableLiveData.value = stateMasterArrayList
                }
            }

            Constants.CITY_CODE -> {

                if (!searchQuery.isNullOrEmpty()) {
                    val searchedDataList = ArrayList<CityMasterDTO>()
                    for (item in cityMasterArrayList) {
                        if ((item.cityName.contains(searchQuery, true))) {
                            searchedDataList.add(item)
                        }
                    }

                    noResultsFoundVisible.set(searchedDataList.isEmpty())
                    cityMasterListMutableLiveData.value = searchedDataList

                } else {
                    noResultsFoundVisible.set(false)
                    cityMasterListMutableLiveData.value = cityMasterArrayList
                }
            }

            Constants.AREA_CODE -> {

                if (!searchQuery.isNullOrEmpty()) {
                    val searchedDataList = ArrayList<AreaMasterDTO>()
                    for (item in areaMasterArrayList) {
                        if ((item.areaName.contains(searchQuery, true))) {
                            searchedDataList.add(item)
                        }
                    }

                    noResultsFoundVisible.set(searchedDataList.isEmpty())
                    areaMasterListMutableLiveData.value = searchedDataList

                } else {
                    noResultsFoundVisible.set(false)
                    areaMasterListMutableLiveData.value = areaMasterArrayList
                }
            }


            Constants.SOCIAL_MEDIA_CODE -> {

                if (!searchQuery.isNullOrEmpty()) {
                    val searchedDataList = ArrayList<SocialMediaMasterDTO>()
                    for (item in socialMediaMasterArrayList) {
                        if ((item.mediumName.contains(searchQuery, true))) {
                            searchedDataList.add(item)
                        }
                    }

                    noResultsFoundVisible.set(searchedDataList.isEmpty())
                    socialMediaMasterListMutableLiveData.value = searchedDataList

                } else {
                    noResultsFoundVisible.set(false)
                    socialMediaMasterListMutableLiveData.value = socialMediaMasterArrayList
                }
            }

            Constants.GROUP_ROLE_CODE -> {

                if (!searchQuery.isNullOrEmpty()) {
                    val searchedDataList = ArrayList<GroupRolesDTO>()
                    for (item in groupRolesArrayList) {
                        if ((item.name.contains(searchQuery, true))) {
                            searchedDataList.add(item)
                        }
                    }

                    noResultsFoundVisible.set(searchedDataList.isEmpty())
                    groupRolesListMutableLiveData.value = searchedDataList

                } else {
                    noResultsFoundVisible.set(false)
                    groupRolesListMutableLiveData.value = groupRolesArrayList
                }
            }
            Constants.KNOWN_LANGUAGES_CODE -> commonSearch(searchQuery!!)

            Constants.DESIGNATION_CODE -> commonSearch(searchQuery!!)

            Constants.GROUP_ROLE_STAFF_CODE -> commonSearch(searchQuery!!)
*/

        }
    }
/*
    fun commonSearch(searchQuery: String) {
        if (!searchQuery.isNullOrEmpty()) {
            val searchedDataList = ArrayList<CommonDialogDTO>()
            for (item in commonArrayList!!) {
                if ((item.name!!.contains(searchQuery, true))) {
                    searchedDataList.add(item)
                }
            }

            noResultsFoundVisible.set(searchedDataList.isEmpty())
            commonMutableList.value = searchedDataList

        } else {
            noResultsFoundVisible.set(false)
            commonMutableList.value = commonArrayList
        }
    }*/


}

