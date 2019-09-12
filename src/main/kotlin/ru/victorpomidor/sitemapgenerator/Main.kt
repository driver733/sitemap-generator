package ru.victorpomidor.sitemapgenerator

import ru.victorpomidor.sitemapgenerator.datastructure.UniqueThreadSafeTree
import ru.victorpomidor.sitemapgenerator.model.Link
import ru.victorpomidor.sitemapgenerator.page.JsoupLinkParser
import ru.victorpomidor.sitemapgenerator.page.JsoupPageDownloader
import ru.victorpomidor.sitemapgenerator.page.SameDomainFilter
import ru.victorpomidor.sitemapgenerator.print.FileSitemapPrinter
import ru.victorpomidor.sitemapgenerator.print.StdoutSitemapPrinter
import java.util.concurrent.Executors

fun main(args: Array<String>) {
    val url = args[0]

    val siteTree = UniqueThreadSafeTree(Link(url = url, text = url))
    val linkParser = JsoupLinkParser(SameDomainFilter())
    val pageDownloader = JsoupPageDownloader()
    val executorService = Executors.newFixedThreadPool(500)

    val sitemapGenerator = SitemapGenerator(
        siteTree,
        executorService,
        linkParser,
        pageDownloader
    )
    val sitemap = sitemapGenerator.generateSitemap(url)

    StdoutSitemapPrinter().print(sitemap)
    FileSitemapPrinter().print(sitemap)
}
