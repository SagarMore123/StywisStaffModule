package com.astrika.stywis_staff.models.menu

import com.astrika.stywis_staff.models.ErrorDTO
import com.astrika.stywis_staff.models.SuccessDTO

data class GetDishListResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val productSectionListingDTOs: ArrayList<MenuSectionWithDishDetails>
)