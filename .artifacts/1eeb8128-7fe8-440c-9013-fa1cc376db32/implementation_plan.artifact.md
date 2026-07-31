# Implementation Plan - Fix Price Display Issues

The user reported a bug where "Veg Biryani" shows two dollar signs ("$$7"). This is due to the app hardcoding a "$" symbol while the data from the database might already include a "$" symbol. Additionally, some parts of the code only handle "₹" or "$" but not both, which could lead to crashes or incorrect display.

## User Review Required
> [!NOTE]
> This change will ensure that all prices are displayed in USD (prefixed with a single `$`) by stripping any existing `$` or `₹` symbols from the raw data before formatting.

## Proposed Changes

### Adapters
---
#### [MODIFY] [MenuAdapter.kt](file:///D:/FoodyMind/app/src/main/java/com/example/foodymind/Adapter/MenuAdapter.kt)
- Update `priceMenu.text` to strip both `$` and `₹` from `menuItem.foodPrice`.
- Clean the price passed to `DetailActivity` via intent to avoid redundant symbols in downstream activities.

#### [MODIFY] [PopularAdapter.kt](file:///D:/FoodyMind/app/src/main/java/com/example/foodymind/Adapter/PopularAdapter.kt)
- Update `binding.pricePopular.text` to strip both `$` and `₹` from the price string.

#### [MODIFY] [CartAdapter.kt](file:///D:/FoodyMind/app/src/main/java/com/example/foodymind/Adapter/CartAdapter.kt)
- Fix `decreaseQuantity` function where `₹` was not being stripped, which could lead to a `NumberFormatException` if the price contained a rupee symbol.
- Ensure consistent symbol stripping in `bind` and `increaseQuantity` as well.

## Verification Plan

### Manual Verification
- **Menu List**: Verify "Veg Biryani" shows as "$7" instead of "$$7".
- **Popular List**: Verify popular items have consistent "$[price]" formatting.
- **Cart**:
    - Verify item prices show correctly.
    - Test `+` and `-` buttons in the cart to ensure calculations work and no crashes occur when symbols are present in the data.
- **Detail Page**: Click on an item and verify the data passed to `DetailActivity` doesn't lead to issues (although `DetailActivity` currently doesn't seem to display the price, it uses it for adding to cart).
