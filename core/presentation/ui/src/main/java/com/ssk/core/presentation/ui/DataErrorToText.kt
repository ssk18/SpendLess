package com.ssk.core.presentation.ui

import com.ssk.core.domain.utils.DataError


fun DataError.asUiText(): UiText {
    return when (this) {
        DataError.Local.DISK_FULL -> UiText.StringResource(
            R.string.failed_to_save_the_item_because_your_disk_is_full
        )

        DataError.Local.INVALID_CREDENTIALS -> UiText.StringResource(
            R.string.username_or_pin_is_incorrect
        )

        DataError.Local.INSERT_USER_ERROR -> UiText.StringResource(
            R.string.insert_user_error
        )
        DataError.Local.DUPLICATE_USER_ERROR -> UiText.StringResource(
            R.string.user_already_exists
        )
        DataError.Local.USER_FETCH_ERROR -> UiText.StringResource(
            R.string.user_fetch_error
        )
        DataError.Local.INSERT_PREFERENCE_ERROR -> UiText.StringResource(
            R.string.insert_preference_error
        )
        DataError.Local.PREFERENCE_FETCH_ERROR -> UiText.StringResource(
            R.string.preference_fetch_error
        )
        DataError.Local.UNKNOWN_DATABASE_ERROR -> UiText.StringResource(
            R.string.unknown_database_error
        )
        DataError.Local.TRANSACTION_FETCH_ERROR -> UiText.StringResource(
            R.string.transaction_fetch_error
        )
        DataError.Local.EXPORT_FAILED -> UiText.StringResource(
            R.string.export_failed
        )
    }
}