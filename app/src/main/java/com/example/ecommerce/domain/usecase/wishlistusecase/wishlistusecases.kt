package com.example.ecommerce.domain.usecase.wishlistusecase

data class WishlistUseCases(
    val getAllWishlistItems: GetAllWishListItemsUseCase,
    val insertIntoWishlist: InsertIntoWishlistUseCase,
    val deleteFromWishlist: DeleteFromWishlistUseCase,
    val deleteAllWishlistItems: DeleteAllWishlistItemsUseCase,
    val isItemInWishlist: IsItemInWishlistUseCase
)