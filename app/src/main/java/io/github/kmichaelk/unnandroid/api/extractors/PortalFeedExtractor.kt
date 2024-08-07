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

package io.github.kmichaelk.unnandroid.api.extractors

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedAttachedFile
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedComment
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedPost
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedPostRecipient
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedReaction
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedUser
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.regex.Pattern

class PortalFeedExtractor {

    private val gson = Gson()

    private val patternVotePost = Pattern.compile("keySigned: 'BLOG_POST-([0-9]+).(.*?)'")
    private val patternVoteComment = Pattern.compile("keySigned: 'BLOG_COMMENT-([0-9]+).(.*?)'")
    //
    private val patternAvatar = Pattern.compile("background: url\\('(.*?)'\\)(.*?)")

    fun extractPost(raw: String): List<PortalFeedPost> = Jsoup.parse(raw).select(".feed-post-block").map {
        val content = it.selectFirst(".feed-post-cont-wrap")!!

        val authorBlock = content.selectFirst(".feed-post-user-name")!!

        val attachmentsUrls = mutableListOf<String>()
        content.select(".disk-ui-file-thumbnails-web-grid-img-item").forEach { thumb ->
            attachmentsUrls.add(thumb.attr("data-bx-src"))
        }
        content.select(".feed-com-img-load > img").forEach { thumb ->
            attachmentsUrls.add(thumb.attr("data-thumb-src"))
        }
        content.select(".disk-ui-file-thumbnails-web-grid-img").forEach { thumb ->
            attachmentsUrls.add(thumb.attr("data-bx-src"))
        }

        val textContentRoot = content.selectFirst(".feed-post-text")!!
        textContentRoot.getElementsByTag("script").forEach { script -> textContentRoot.children().remove(script) }
        val html = textContentRoot.html()

        PortalFeedPost(
            id = Integer.parseInt(it.selectFirst(".feed-post-more-link")!!.attr("data-bx-post-id")),

            entityXmlId = it.selectFirst(".feed-comments-block")!!.attr("data-bx-comments-entity-xml-id"),

            author = PortalFeedUser(
                bxId = Integer.parseInt(authorBlock.attr("bx-post-author-id")),
                name = authorBlock.text(),
                avatarUrl = extractPostAvatar(it.selectFirst(".feed-user-avatar i")!!)
            ),

            datetime = content.selectFirst(".feed-time")!!.text(),

            html = html,

            attachments = attachmentsUrls,

            files = content.select(".feed-com-file-name-wrap").map { file ->
                val fileInfo = file.selectFirst(".feed-com-file-name")!!
                PortalFeedAttachedFile(
                    title = fileInfo.attr("title"),
                    size = file.selectFirst(".feed-com-file-size")!!.text(),
                    url = fileInfo.attr("data-src")
                )
            },

            recipients = it.select(".feed-add-post-destination-new").map { dst ->
                PortalFeedPostRecipient(
                    id = Integer.parseInt(dst.attr("data-bx-entity-id")),
                    name = dst.text(),
                    type = dst.attr("data-bx-entity-type")
                )
            },

            reactions = extractReactions(content.selectFirst(".feed-post-emoji-icon-container")),

            voteKeyPart = extractVoteKey(it.selectFirst(".feed-post-emoji-top-panel-box")
                ?.selectFirst("script"), patternVotePost),

            commentsCount = it.select(".feed-com-main-content").size +
                    (it.selectFirst(".feed-com-all")
                        ?.attr("bx-mpl-comments-count")
                        ?.let { v -> Integer.parseInt(v) }
                        ?: 0),

            views = Integer.parseInt(it.selectFirst(".feed-content-view-cnt")!!.text()),

            url = content.selectFirst(".feed-post-time-wrap > a")!!.attr("href"),
        )
    }

    fun extractComments(raw: String): List<PortalFeedComment> = Jsoup.parse(raw).select(".feed-com-block-cover").map {
        PortalFeedComment(
            id = Integer.parseInt(it.attr("bx-mpl-entity-id")),

            author = it.selectFirst(".feed-com-user-box > .feed-author-name")!!.run {
                PortalFeedUser(
                    bxId = Integer.parseInt(attr("bx-tooltip-user-id")),
                    name = text(),
                    avatarUrl = it.selectFirst(".feed-com-avatar > img")!!.attr("src").run {
                        ifEmpty { null }
                    }
                )
            },

            datetime = it.selectFirst(".feed-time")!!.text(),

            html = it.selectFirst(".feed-com-text-inner-inner > div")!!.html(),

            attachments = it.select(".feed-com-img-load > img").map { thumb ->
                thumb.attr("data-src")
            },

            files = it.select(".feed-com-file-name-wrap").map { file ->
                val fileInfo = file.selectFirst(".feed-com-file-name")!!
                PortalFeedAttachedFile(
                    title = fileInfo.attr("title"),
                    size = file.selectFirst(".feed-com-file-size")!!.text(),
                    url = fileInfo.attr("data-src")
                )
            },

            reactions = extractReactions(it.selectFirst(".feed-post-emoji-icon-container")),

            voteKeyPart = extractVoteKey(it.selectFirst(".bx-ilike-wrap-block ~ script"),
                patternVoteComment)
        )
    }

    private fun extractPostAvatar(el: Element): String? {
        if (!el.hasAttr("style")) return null

        val style = el.attr("style")
        val matcher = patternAvatar.matcher(style)
        if (!matcher.matches()) return null

        return matcher.group(1)
    }

    private fun extractReactions(wrapper: Element?): Map<PortalFeedReaction, Int>
        = wrapper?.let { it.attr("data-reactions-data").let { raw ->
        gson.fromJson(raw, object : TypeToken<Map<PortalFeedReaction, Int>>() {}.type)
    } } ?: mapOf()

    private fun extractVoteKey(scriptEl: Element?, pattern: Pattern): String? {
        if (scriptEl == null) {
            return null
        }

        val matcher = pattern.matcher(scriptEl.data())
        if (!matcher.find()) {
            return null
        }

        return matcher.group(2)
    }
}