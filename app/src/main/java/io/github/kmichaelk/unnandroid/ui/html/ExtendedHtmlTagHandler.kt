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

package io.github.kmichaelk.unnandroid.ui.html

import android.text.Editable
import android.text.Html.TagHandler
import org.xml.sax.XMLReader

class ExtendedHtmlTagHandler : TagHandler {

    override fun handleTag(opening: Boolean, tag: String?, output: Editable?, xmlReader: XMLReader?) {
        if (opening) {
            if ("table".equals(tag, ignoreCase = true)) {
                output?.append("---\n")
            } else if("tr".equals(tag, ignoreCase = true)) {
                output?.append("| ")
            }
        } else {
            if ("table".equals(tag, ignoreCase = true)) {
                output?.append("---")
            } else if("tr".equals(tag, ignoreCase = true)) {
                output?.append("\n")
            } else if ("td".equals(tag, ignoreCase = true) || "th".equals("tag", ignoreCase = true)) {
                output?.append(" | ")
            }
        }
    }
}