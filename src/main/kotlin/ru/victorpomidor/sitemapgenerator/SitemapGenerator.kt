package ru.victorpomidor.sitemapgenerator

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import ru.victorpomidor.sitemapgenerator.datastructure.PutResult
import ru.victorpomidor.sitemapgenerator.datastructure.TreeNode
import ru.victorpomidor.sitemapgenerator.datastructure.UniqueTree
import ru.victorpomidor.sitemapgenerator.model.DownloadResult
import ru.victorpomidor.sitemapgenerator.model.Link
import ru.victorpomidor.sitemapgenerator.model.Sitemap
import ru.victorpomidor.sitemapgenerator.page.LinkParser
import ru.victorpomidor.sitemapgenerator.page.PageDownloader
import ru.victorpomidor.sitemapgenerator.utils.Log
import ru.victorpomidor.sitemapgenerator.utils.timed
import java.util.concurrent.atomic.AtomicInteger

class SitemapGenerator(
    private val siteTree: UniqueTree<Link>,
    private val linkParser: LinkParser,
    private val pageDownloader: PageDownloader
) {
    companion object : Log()

    private val tasksCount = AtomicInteger(0)
    private val tasks: MutableList<Deferred<Unit>> = mutableListOf()

    fun generateSitemap(url: String): Sitemap {
        log.info("generate map for $url")
        timed {
            runBlocking {
                generateSitemapTree(siteTree.getRoot(), siteTree, url, 0)
                tasks.awaitAll()
            }
        }.log("map for $url generated for {} millis")

        return Sitemap(siteTree.getRoot())
    }

    private suspend fun generateSitemapTree(
        currentNode: TreeNode<Link>,
        tree: UniqueTree<Link>,
        baseUrl: String,
        depth: Int
    ) {
        val tasksBefore = tasksCount.incrementAndGet()
        log.debug("New task for url {} on level {}. Task count: {}", currentNode.value.url, depth, tasksBefore)
        try {
            when (val page = pageDownloader.downloadPage(currentNode.value.url, baseUrl)) {
                is DownloadResult.Ok -> processPage(page, baseUrl, tree, currentNode, depth)
                is DownloadResult.Error -> currentNode.value = currentNode.value.copy(error = page.errorMessage)
            }
        } catch (e: Exception) {
            log.error("error: $e")
        }

        val tasksAfter = tasksCount.decrementAndGet()
        log.debug(
            "Over executing task for url {} on level {}. Task count: {}",
            currentNode.value.url,
            depth,
            tasksAfter
        )
    }

    private suspend fun processPage(
        page: DownloadResult.Ok,
        baseUrl: String,
        tree: UniqueTree<Link>,
        currentNode: TreeNode<Link>,
        depth: Int
    ) {
        val links = linkParser.parsePage(page.content, baseUrl)
        links.forEach {
            val putResult = tree.putExclusive(currentNode, it)
            if (putResult is PutResult.Ok) {
                log.debug("level $depth: success add ${it.url}")
                tasks.add(GlobalScope.async { generateSitemapTree(putResult.element, tree, baseUrl, depth + 1) })
            }
        }
    }

    private fun waitWhile(condition: () -> Boolean) {
        while (condition.invoke()) {
            Thread.sleep(1000)
        }
    }
}
