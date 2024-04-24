/*
 * This file is part of UNN-Android.
 *
 * UNN-Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UNN-Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with UNN-Android.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.kmichaelk.unnandroid.models.portal

import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName
import io.github.kmichaelk.unnandroid.R

enum class PortalFeedReaction(
    val displayName: String,
    @DrawableRes val icon: Int,
) {

    @SerializedName("like")
    Like("Нравится", R.drawable.emoji_like),

    @SerializedName("kiss")
    Kiss("Восхищаюсь", R.drawable.emoji_kiss),

    @SerializedName("laugh")
    Laugh("Смеюсь", R.drawable.emoji_laugh),

    @SerializedName("wonder")
    Wonder("Удивляюсь", R.drawable.emoji_wonder),

    @SerializedName("cry")
    Cry("Печалюсь", R.drawable.emoji_cry),

    @SerializedName("angry")
    Angry("Злюсь", R.drawable.emoji_angry),

    @SerializedName("facepalm")
    Facepalm("Разочарован", R.drawable.emoji_facepalm);
}