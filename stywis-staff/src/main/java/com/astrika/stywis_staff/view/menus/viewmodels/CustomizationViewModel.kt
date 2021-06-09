package com.astrika.stywis_staff.view.menus.viewmodels

import android.app.Activity
import android.app.Application
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.master_controller.source.MasterRepository
import com.astrika.stywis_staff.models.*
import com.astrika.stywis_staff.models.menu.DishDetailsDTO
import com.astrika.stywis_staff.models.menu.ProductAddonsCustomizationDTO
import com.astrika.stywis_staff.models.menu.ProductOptionsCustomizationDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.source.dashboard.DashboardRepository
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.GenericBaseObservable
import com.astrika.stywis_staff.utils.SingleLiveEvent

class CustomizationViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var masterRepository: MasterRepository,
    private var dashboardRepository: DashboardRepository

) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()
    var customizationOptionsArrayList = ArrayList<CustomizationOptionDTOGroupRow>()
    var customizationOptionsLiveList = MutableLiveData<ArrayList<CustomizationOptionDTOGroupRow>>()
    var customizationOptionsList = ArrayList<CustomizationOptionDTO>()
    var customizationAddOnsLiveList = MutableLiveData<ArrayList<CustomizationAddOnDTO>>()
    var customizationAddOnsList = ArrayList<CustomizationAddOnDTO>()
    var addRowEvent = SingleLiveEvent<Void>()
    var addGroupEvent = SingleLiveEvent<Void>()
    var saveEvent = SingleLiveEvent<Void>()
    var repeatEvent = SingleLiveEvent<Void>()
    var catalogueCategoryId: Long = -1
    var catalogueSectionId: Long = -1
    var productId: Long = -1
    var customizationDTO = CustomizationDTO()

    var optionsDTOList = ArrayList<CustomizationOptionDTOGroupRow>()
    var addonsDTOList = ArrayList<CustomizationAddOnDTOGroupRow>()
    var menuDishDetails = DishDetailsDTO()
    var customizedPriceMutable = MutableLiveData("")
    var selectedCustomizedItemsMutable = MutableLiveData("No Extras Selected")
    var selectedCustomizedItemsCountsMutable = MutableLiveData(0)
    var displayDishPrice = MutableLiveData(0L)
    var onOptionHeadingClick = MutableLiveData(true)
    var onAddOnsHeadingClick = MutableLiveData(true)
    var isChooseAddonsVisibilityLayout = MutableLiveData(true)
    val optionName = MutableLiveData("")
    private var optionAmt = 0L
    private var addOnAmt = 0L
    var itemsCount = 0

    fun getCustomizationListing() {
        val commonListingDTO = CommonListingDTO()
        val commonSortList = ArrayList<CommonSortDTO>()
        val commonSortDTO = CommonSortDTO()
        commonSortDTO.sortField = Constants.CUSTOMIZATION_PRODUCT_ID_COLUMN
        commonSortDTO.sortOrder = Constants.SORT_ORDER_ASC
        commonSortList.add(commonSortDTO)
        commonListingDTO.sort = commonSortList

        val commonSearchList = ArrayList<CommonSearchDTO>()
        val commonSearchDTO = CommonSearchDTO()
        commonSearchDTO.search = productId.toString()
        commonSearchDTO.searchCol = Constants.CUSTOMIZATION_PRODUCT_ID_COLUMN
        commonSearchList.add(commonSearchDTO)
        commonListingDTO.search = commonSearchList
        dashboardRepository.getCustomizationListing(commonListingDTO, object :
            IDataSourceCallback<CustomizationDTO> {

            override fun onDataFound(data: CustomizationDTO) {

                if (data != null) {

                    // Options
                    if (!data.optionsDTOList.isNullOrEmpty()) {
                        for (options in data.optionsDTOList) {

                            customizationOptionsArrayList =
                                options.customizationOptionValueDTO ?: arrayListOf()

                            if (menuDishDetails.optionValueIds.isNullOrEmpty()) {

                                if (!customizationOptionsArrayList.isNullOrEmpty()) {
                                    customizationOptionsArrayList[0].selected = true
                                    updateOptionPrice(customizationOptionsArrayList[0])
                                } else {
                                    optionAmt =
                                        if (menuDishDetails.productDiscountPrice != null && menuDishDetails.productDiscountPrice?.toLong() ?: 0 > 0) menuDishDetails.productDiscountPrice?.toLong()
                                            ?: 0 else menuDishDetails.productOriPrice?.toLong() ?: 0
                                }


                            } else {
                                loop@ for (item1 in menuDishDetails.optionValueIds
                                    ?: arrayListOf()) {

                                    for (item2 in customizationOptionsArrayList) {

                                        if (item1.id == item2.customizeOptionValueId) {
                                            item2.selected = true

                                            updateOptionPrice(item2)

                                            break@loop
                                        }
                                    }

                                }
                            }

                            customizationOptionsLiveList.value = customizationOptionsArrayList

                            break
                        }
                    } else {
                        customizationOptionsLiveList.value = arrayListOf()
                    }


                    // Addons

                    val itemsNames = arrayListOf<String>()

                    if (!data.addOnDTOList.isNullOrEmpty()) {

                        for (item1 in menuDishDetails.addOnValueIds ?: arrayListOf()) {

                            for (item2 in data.addOnDTOList) {

                                for (item3 in item2.customizationAddOnValueDTO ?: arrayListOf()) {

                                    if (item1.id == item3.customizeAddOnValueId) {
                                        item3.selected = true
                                        itemsNames.add(item3.customizeAddOnValueName ?: "")
                                        addOnAmt += (if (item3.productDisPrice != null && item3.productDisPrice ?: 0 > 0) item3.productDisPrice else item3.productOriPrice
                                            ?: 0) ?: 0
                                        itemsCount = itemsCount.plus(1)
                                        addonsDTOList.add(item3)
                                    }

                                }
                            }

                        }

                        updateOrderDetails(itemsNames)

                        customizationAddOnsLiveList.value = data.addOnDTOList

                    } else {
                        customizationAddOnsLiveList.value = arrayListOf()
                    }

                    optionAmt = optionAmt.plus(addOnAmt)
                    selectedCustomizedItemsCountsMutable.value = itemsCount.minus(3)
                    customizedPriceMutable.value = "Items total | ₹ $optionAmt"
                    menuDishDetails.productCustomizedPrice = optionAmt.toBigDecimal()
                }
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
            }
        })

    }


    fun showTotalOrderDetails(
        isOption: Boolean,
        position: Int,
        optionDTO: CustomizationOptionDTOGroupRow?,
        addOnDTO: CustomizationAddOnDTOGroupRow?
    ) {

        val itemsNames = arrayListOf<String>()

        // Options

        if (isOption) {

            for (dto in customizationOptionsArrayList) {
                dto.selected = dto.customizeOptionValueId == optionDTO?.customizeOptionValueId
            }
            customizationOptionsLiveList.value = customizationOptionsArrayList

            optionDTO?.let { updateOptionPrice(it) }


        } else {

            // Addons

            val amt =
                (if (addOnDTO?.productDisPrice != null && addOnDTO.productDisPrice ?: 0 > 0) addOnDTO.productDisPrice else addOnDTO?.productOriPrice
                    ?: 0) ?: 0
            if (addOnDTO?.selected == true) {
                addonsDTOList.add(addOnDTO)
                addOnAmt += amt
                itemsCount = itemsCount.plus(1)
            } else {
                addonsDTOList.remove(addOnDTO)
                addOnAmt -= amt
                itemsCount = itemsCount.minus(1)

            }


            val addonsArrayList = arrayListOf<ProductAddonsCustomizationDTO>()

            for (item in addonsDTOList) {

                val productAddonsCustomizationDTO = ProductAddonsCustomizationDTO()
                productAddonsCustomizationDTO.id = item.customizeAddOnValueId ?: 0
                productAddonsCustomizationDTO.value = item.customizeAddOnValueName ?: ""
                productAddonsCustomizationDTO.itemId = menuDishDetails.itemId ?: 0
                productAddonsCustomizationDTO.itemId = menuDishDetails.itemId ?: 0
                val amt =
                    (if (item.productDisPrice != null && item.productDisPrice ?: 0 > 0) item.productDisPrice else item.productOriPrice
                        ?: 0) ?: 0
                productAddonsCustomizationDTO.addOnPrice = amt
                addonsArrayList.add(productAddonsCustomizationDTO)

                itemsNames.add(item.customizeAddOnValueName ?: "")

            }

            menuDishDetails.addOnValueIds = addonsArrayList

            updateOrderDetails(itemsNames)

        }


        val dishPrice = optionAmt.plus(addOnAmt)

        selectedCustomizedItemsCountsMutable.value = itemsCount.minus(3)

        customizedPriceMutable.value = "Items total | ₹ $dishPrice"
        menuDishDetails.productCustomizedPrice = dishPrice.toBigDecimal()


    }

    fun updateOrderDetails(itemsNames: ArrayList<String>) {

        when {
            itemsNames.size == 1 -> {
                selectedCustomizedItemsMutable.value = "  |  " + itemsNames[0]
            }
            itemsNames.size > 1 -> {
                selectedCustomizedItemsMutable.value =
                    "  |  " + itemsNames[0] + ", " + itemsNames[1]
            }
            else -> {
                if (optionName.value.isNullOrBlank()) {
                    selectedCustomizedItemsMutable.value = "No Extras Selected"
                } else {
                    selectedCustomizedItemsMutable.value = ""
                }
            }
        }

    }

    fun updateOptionPrice(dto: CustomizationOptionDTOGroupRow) {

        val productCustomizationDTO = ProductOptionsCustomizationDTO()
        productCustomizationDTO.id = dto.customizeOptionValueId ?: 0
        productCustomizationDTO.value = dto.customizeOptionValueName ?: ""
        productCustomizationDTO.itemId = menuDishDetails.itemId ?: 0
        val amt =
            (if (dto.productDisPrice != null && dto.productDisPrice ?: 0 > 0) dto.productDisPrice else dto.productOriPrice
                ?: 0) ?: 0
        productCustomizationDTO.optionPrice = amt

        menuDishDetails.optionValueIds = arrayListOf()
        menuDishDetails.optionValueIds?.add(productCustomizationDTO) // adding only single object in the list

        optionAmt = amt
        optionName.value = dto.customizeOptionValueName ?: ""
        if (itemsCount == 0) {
            itemsCount = 1
        }
    }

    fun onOptionHeadingClick() {
        onOptionHeadingClick.value = onOptionHeadingClick.value?.not()
    }

    fun onAddOnsHeadingClick() {
        onAddOnsHeadingClick.value = onAddOnsHeadingClick.value?.not()
    }

    fun onChooseClick() {
        isChooseAddonsVisibilityLayout.value = isChooseAddonsVisibilityLayout.value?.not()
    }

    fun onRepeatClick() {
        repeatEvent.call()
    }

    fun onConfirmOrderClick() {
        saveEvent.call()
    }


}