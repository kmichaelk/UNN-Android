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

import io.github.kmichaelk.unnandroid.models.portal.PortalFeedAttachedFile
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedComment
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedPost
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedPostReceiver
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedUser
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.regex.Pattern

class PortalFeedExtractor {

    private val patternAvatar = Pattern.compile("background: url\\('(.*?)'\\)(.*?)")

    fun extractPost(raw: String): List<PortalFeedPost> = Jsoup.parse(raw).select(".feed-post-block").map {
        val id = Integer.parseInt(it.attr("data-livefeed-id"))
        val entityXmlId = it.selectFirst(".feed-comments-block")!!.attr("data-bx-comments-entity-xml-id")

        //

        val commentsCount = it.select(".feed-com-main-content").size +
                (it.selectFirst(".feed-com-all")
                    ?.attr("bx-mpl-comments-count")
                    ?.let { v -> Integer.parseInt(v) }
                    ?: 0)
        val views = Integer.parseInt(it.selectFirst(".feed-content-view-cnt")!!.text())

        val receivers = it.select(".feed-add-post-destination-new").map { dst ->
            PortalFeedPostReceiver(
                id = Integer.parseInt(dst.attr("data-bx-entity-id")),
                name = dst.text(),
                type = dst.attr("data-bx-entity-type")
            )
        }

        //

        val content = it.selectFirst(".feed-post-cont-wrap")!!

        val authorBlock = content.selectFirst(".feed-post-user-name")!!

        val authorUserId = Integer.parseInt(authorBlock.attr("bx-post-author-id"))
        val authorName = authorBlock.text()
        val avatarUrl = extractPostAvatar(it.selectFirst(".feed-user-avatar i")!!)

        val datetime = content.selectFirst(".feed-time")!!.text()

        val url = content.selectFirst(".feed-post-time-wrap > a")!!.attr("href")

        val contentRoot = content.selectFirst(".feed-post-text")!!
        contentRoot.getElementsByTag("script").forEach { script -> contentRoot.children().remove(script) }
        val html = contentRoot.html()

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

        val files = content.select(".feed-com-file-name-wrap").map { file ->
            val fileInfo = file.selectFirst(".feed-com-file-name")!!
            PortalFeedAttachedFile(
                title = fileInfo.attr("title"),
                size = file.selectFirst(".feed-com-file-size")!!.text(),
                url = fileInfo.attr("data-src")
            )
        }

        //

        PortalFeedPost(
            id = id,
            author = PortalFeedUser(
                bxId = authorUserId,
                name = authorName,
                avatarUrl = avatarUrl
            ),
            datetime = datetime,
            html = html,
            attachments = attachmentsUrls,
            commentsCount = commentsCount,
            views = views,
            url = url,
            entityXmlId = entityXmlId,
            receivers = receivers,
            files = files,
        )
    }

    fun extractComments(raw: String): List<PortalFeedComment> = Jsoup.parse(raw).select(".feed-com-block-cover").map {
        val id = Integer.parseInt(it.attr("bx-mpl-entity-id"))

        val datetime = it.selectFirst(".feed-time")!!.text()

        val authorBlock = it.selectFirst(".feed-com-user-box > .feed-author-name")!!
        val authorUserId = Integer.parseInt(authorBlock.attr("bx-tooltip-user-id"))
        val authorName = authorBlock.text()
        val avatarUrl = it.selectFirst(".feed-com-avatar > img")!!.attr("src").run {
            ifEmpty { null }
        }

        val attachmentsUrls = it.select(".feed-com-img-load > img").map { thumb ->
            thumb.attr("data-src")
        }

        val files = it.select(".feed-com-file-name-wrap").map { file ->
            val fileInfo = file.selectFirst(".feed-com-file-name")!!
            PortalFeedAttachedFile(
                title = fileInfo.attr("title"),
                size = file.selectFirst(".feed-com-file-size")!!.text(),
                url = fileInfo.attr("data-src")
            )
        }

        val html = it.selectFirst(".feed-com-text-inner-inner > div")!!.html()

        PortalFeedComment(
            id = id,
            author = PortalFeedUser(
                bxId = authorUserId,
                name = authorName,
                avatarUrl = avatarUrl
            ),
            datetime = datetime,
            html = html,
            attachments = attachmentsUrls,
            files = files,
        )
    }

    private fun extractPostAvatar(el: Element): String? {
        if (!el.hasAttr("style")) return null

        val style = el.attr("style")
        val matcher = patternAvatar.matcher(style)
        if (!matcher.matches()) return null

        return matcher.group(1)
    }
}