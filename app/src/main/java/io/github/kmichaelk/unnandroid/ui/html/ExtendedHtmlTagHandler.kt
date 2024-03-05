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