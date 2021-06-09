package com.astrika.stywis_staff.view.stockmanagement.viewmodels

import android.app.Activity
import android.app.Application
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.stywis_staff.master_controller.source.MasterRepository
import com.astrika.stywis_staff.models.CommonListingDTO
import com.astrika.stywis_staff.models.CustomizationOptionDTOGroupRow
import com.astrika.stywis_staff.models.menu.DishDetailsDTO
import com.astrika.stywis_staff.models.stock.StockCustomizationMasterDTO
import com.astrika.stywis_staff.models.stock.StocksCustomizationDTO
import com.astrika.stywis_staff.network.network_utils.IDataSourceCallback
import com.astrika.stywis_staff.source.dashboard.DashboardRepository
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.GenericBaseObservable
import com.astrika.stywis_staff.utils.SingleLiveEvent

class StockCustomizationViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var masterRepository: MasterRepository,
    private var dashboardRepository: DashboardRepository

) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()
    var customizationOptionsLiveList = MutableLiveData<ArrayList<CustomizationOptionDTOGroupRow>>()

    var menuDishDetails = DishDetailsDTO()
    var displayDishPrice = MutableLiveData(0L)
    var isCustomizable = MutableLiveData(true)


    var outletId = 0L
    var productId: Long = 0L

    var stockCustomizationOptionError = MutableLiveData<String>("")
    var quantityError = MutableLiveData<String>("")
    var stockCustomizationOptionId = 0L
    var stockCustomizationOption = MutableLiveData<String>("")

    var stockCustomizationOptionQuantity = MutableLiveData<String>("")

    var stockCustomizationOptionsList = ArrayList<StocksCustomizationDTO>()
    var stockCustomizationOptionsMutableLiveList =
        MutableLiveData<ArrayList<StocksCustomizationDTO>>()

    val onStockCustomizationOptionClickEvent = SingleLiveEvent<Void>()
    var dismissEvent = SingleLiveEvent<Void>()


    init {
        outletId = Constants.getOutletId(activity)
    }

    // Stock Customization Option Click
    fun onStockCustomizationOptionClick() {
        onStockCustomizationOptionClickEvent.call()
    }

    fun getStockCustomizationListing() {

        showProgressBar.value = true


        try {

            val commonListingDTO = CommonListingDTO()

            commonListingDTO.defaultSort.sortField = "createdOn"
            commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER_DESC

            masterRepository.fetchStockCustomizationOptionsMasterDataRemote(
                commonListingDTO,
                object :
                    IDataSourceCallback<List<StockCustomizationMasterDTO>> {

                    override fun onDataFound(data: List<StockCustomizationMasterDTO>) {}

                    override fun onError(error: String) {}

                })

            dashboardRepository.fetchAllStockCustomizationsByProductOrDishId(productId,
                object : IDataSourceCallback<ArrayList<StocksCustomizationDTO>> {

                    override fun onDataFound(data: ArrayList<StocksCustomizationDTO>) {
                        if (isCustomizable.value == true) {

                            stockCustomizationOptionsList = data

                        } else {
                            stockCustomizationOptionQuantity.value =
                                data[0].presentQuantity.toString()
                        }

                        stockCustomizationOptionsMutableLiveList.value =
                            stockCustomizationOptionsList

                        showProgressBar.value = false
                    }

                    override fun onError(error: String) {
                        showProgressBar.value = false
                        getmSnackbar().value = error
                        stockCustomizationOptionQuantity.value = ""
                        stockCustomizationOptionsMutableLiveList.value =
                            stockCustomizationOptionsList
                    }
                })


        } catch (e: Exception) {

        }

    }

    fun removeItem(position: Int, dto: StocksCustomizationDTO) {
        stockCustomizationOptionsList[position] = dto

        val arrayList =
            stockCustomizationOptionsList.filter { it.active } as ArrayList<StocksCustomizationDTO>
        stockCustomizationOptionsMutableLiveList.value = arrayList

    }


    fun onCancelClick() {
        dismissEvent.call()
    }

    fun onSaveClick() {

        showProgressBar.value = true
        dashboardRepository.saveStockCustomizations(
            stockCustomizationOptionsList,
            object : IDataSourceCallback<String> {

                override fun onDataFound(data: String) {
                    dismissEvent.call()
                    showProgressBar.value = false
                    getmSnackbar().value = data
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })

    }

    fun onAddClick() {

        if (isCustomizable.value == true) {

            if (stockCustomizationOptionId != null && stockCustomizationOptionId != 0L
                && !stockCustomizationOptionQuantity.value.isNullOrBlank()
            ) {

                val stocksCustomizationDTO = StocksCustomizationDTO()
                stocksCustomizationDTO.customizeOptionId = stockCustomizationOptionId
                stocksCustomizationDTO.outletId = outletId
                stocksCustomizationDTO.productId = productId
                stocksCustomizationDTO.presentQuantity =
                    stockCustomizationOptionQuantity.value?.toLong() ?: 0L

                stockCustomizationOptionsList.add(stocksCustomizationDTO)
                stockCustomizationOptionsMutableLiveList.value = stockCustomizationOptionsList

            } else if (stockCustomizationOptionId != null && stockCustomizationOptionId != 0L
                && stockCustomizationOptionQuantity.value.isNullOrBlank()
            ) {

                stockCustomizationOptionError.value = ""
                quantityError.value = "Please enter quantity"

            } else if (stockCustomizationOptionId == null && stockCustomizationOptionId == 0L
                && stockCustomizationOptionQuantity.value.isNullOrBlank()
            ) {

                stockCustomizationOptionError.value = "Please select options"
                quantityError.value = ""

            } else {
                stockCustomizationOptionError.value = "Please select options"
                quantityError.value = "Please enter quantity"
            }

        } else {


            if (!stockCustomizationOptionQuantity.value.isNullOrBlank()
            ) {

                val stocksCustomizationDTO = StocksCustomizationDTO()
                stocksCustomizationDTO.customizeOptionId = stockCustomizationOptionId
                stocksCustomizationDTO.outletId = outletId
                stocksCustomizationDTO.productId = productId
                stocksCustomizationDTO.presentQuantity =
                    stockCustomizationOptionQuantity.value?.toLong() ?: 0L

                stockCustomizationOptionsList.add(stocksCustomizationDTO)
                stockCustomizationOptionsMutableLiveList.value = stockCustomizationOptionsList

            } else {

                stockCustomizationOptionError.value = ""
                quantityError.value = "Please enter quantity"

            }


        }

    }


}