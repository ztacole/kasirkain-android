package com.takumi.kasirkain.di

import com.takumi.kasirkain.domain.usecase.AddCartItemUseCase
import com.takumi.kasirkain.domain.usecase.GetCategoriesUseCase
import com.takumi.kasirkain.domain.usecase.GetProductUseCase
import com.takumi.kasirkain.domain.usecase.GetProductVariantByBarcodeUseCase
import com.takumi.kasirkain.domain.usecase.GetProductVariantsUseCase
import com.takumi.kasirkain.domain.usecase.GetUserProfileUseCase
import com.takumi.kasirkain.domain.usecase.RemoveTokenUseCase
import com.takumi.kasirkain.presentation.features.home.HomeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object ViewModelModule {
    @Provides
    @ActivityScoped
    fun provideYourViewModel(
        getProductUseCase: GetProductUseCase,
        getCategoriesUseCase: GetCategoriesUseCase,
        getProductVariantsUseCase: GetProductVariantsUseCase,
        getProductVariantByBarcodeUseCase: GetProductVariantByBarcodeUseCase,
        addCartItemUseCase: AddCartItemUseCase,
        getUserProfileUseCase: GetUserProfileUseCase,
        removeTokenUseCase: RemoveTokenUseCase
    ): HomeViewModel = HomeViewModel(
        getProductUseCase,
        getCategoriesUseCase,
        getProductVariantsUseCase,
        getProductVariantByBarcodeUseCase,
        addCartItemUseCase,
        getUserProfileUseCase,
        removeTokenUseCase
    )
}