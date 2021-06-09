package com.astrika.stywis_staff.models.menu

import com.astrika.stywis_staff.models.ErrorDTO
import com.astrika.stywis_staff.models.SuccessDTO

data class GetMenuCategoriesResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val activeCatalogueCategoryDTOs: ArrayList<CatalogueCategoryDTO>,
    val inActiveCatalogueCategoryDTOs: ArrayList<CatalogueCategoryDTO>
)