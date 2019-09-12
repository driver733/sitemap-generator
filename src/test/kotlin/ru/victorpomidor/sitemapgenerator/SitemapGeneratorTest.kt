package ru.victorpomidor.sitemapgenerator

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import ru.victorpomidor.sitemapgenerator.datastructure.UniqueThreadSafeTree
import ru.victorpomidor.sitemapgenerator.model.DownloadResult
import ru.victorpomidor.sitemapgenerator.model.Link
import ru.victorpomidor.sitemapgenerator.page.JsoupLinkParser
import ru.victorpomidor.sitemapgenerator.page.PageDownloader
import ru.victorpomidor.sitemapgenerator.page.SameDomainFilter

const val URL = "http://site.url"

private val simpleSite = object : PageDownloader {
    private val pages = mapOf(
        URL to "<a href='/1'>first</a><a href='/2'>second</a>",
        "http://site.url/1" to "<a href='/1_1'>first_1</a><a href='/1_2'>first_2</a>",
        "http://site.url/1_1" to "<a href='/1_1_1'>first_3</a>",
        "http://site.url/2" to "<a href='/2_1'>second_2</a>"
    )

    override fun downloadPage(url: String, baseUrl: String): DownloadResult {
        val page = pages[url]
        return if (page != null) {
            DownloadResult.Ok(page)
        } else {
            DownloadResult.Error("404")
        }
    }
}

private val cycleSite = object : PageDownloader {
    private val pages = mapOf(
        URL to "<a href='/1'>first</a><a href='/2'>second</a>",
        "http://site.url/1" to "<a href='http://site.url'>first_1</a><a href='http://site.url'>first_2</a>",
        "http://site.url/2" to "<a href='http://site.url'>first_3</a>"
    )

    override fun downloadPage(url: String, baseUrl: String): DownloadResult {
        val page = pages[url]
        return if (page != null) {
            DownloadResult.Ok(page)
        } else {
            DownloadResult.Error("404")
        }
    }
}

val linkParser = JsoupLinkParser(SameDomainFilter())

internal class SitemapGeneratorTest : StringSpec({
    val simpleSitemapGenerator = SitemapGenerator(
        siteTree = UniqueThreadSafeTree(Link(URL, URL)),
        linkParser = linkParser,
        pageDownloader = simpleSite
    )

    val cycleSitemapGenerator = SitemapGenerator(
        siteTree = UniqueThreadSafeTree(Link(URL, URL)),
        linkParser = linkParser,
        pageDownloader = cycleSite
    )

    "should generate sitemap for simple site" {
        val siteMap = simpleSitemapGenerator.generateSitemap(URL)
        siteMap.root.value shouldBe Link(URL, URL)

        siteMap.root.childs.size shouldBe 2
        siteMap.root.childs[0].childs.size shouldBe 2
        siteMap.root.childs[0].childs[0].childs.size shouldBe 1
        siteMap.root.childs[0].childs[1].childs.size shouldBe 0
        siteMap.root.childs[1].childs.size shouldBe 1
        siteMap.root.childs[1].childs[0].childs.size shouldBe 0

        siteMap.root.childs[0].value shouldBe Link("$URL/1", "first")
        siteMap.root.childs[1].value shouldBe Link("$URL/2", "second")
        siteMap.root.childs[0].childs[0].value shouldBe Link("$URL/1_1", "first_1")
        siteMap.root.childs[0].childs[1].value shouldBe Link("$URL/1_2", "first_2")
        siteMap.root.childs[0].childs[0].childs[0].value shouldBe Link("$URL/1_1_1", "first_3")
        siteMap.root.childs[1].value shouldBe Link("$URL/2", "second")
        siteMap.root.childs[1].childs[0].value shouldBe Link("$URL/2_1", "second_1")
    }

    "should avoid the cycles" {
        val siteMap = cycleSitemapGenerator.generateSitemap(URL)

        siteMap.root.value shouldBe Link(URL, URL)

        siteMap.root.childs.size shouldBe 2
        siteMap.root.childs[0].childs.size shouldBe 0
        siteMap.root.childs[1].childs.size shouldBe 0

        siteMap.root.childs[0].value shouldBe Link("$URL/1", URL)
        siteMap.root.childs[1].value shouldBe Link("$URL/2", URL)
    }
})
