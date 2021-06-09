package com.astrika.stywis_staff.models.menu

data class MenuSectionWithDishDetails(
    var catalogueSectionDTO: MenuSectionDTO,
    var activeProductList: ArrayList<DishDetailsDTO>? = null,
    var inActiveProductList: ArrayList<DishDetailsDTO>? = null
)