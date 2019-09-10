package ru.victorpomidor.sitemapgenerator

import org.jsoup.Jsoup
import ru.victorpomidor.sitemapgenerator.datastructure.TreeNode
import ru.victorpomidor.sitemapgenerator.datastructure.UniqueTree
import ru.victorpomidor.sitemapgenerator.model.Link
import ru.victorpomidor.sitemapgenerator.model.Sitemap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SitemapGenerator(
    private val url: String,
    private val siteTree: UniqueTree<Link>,
    executorService: ExecutorService = Executors.newWorkStealingPool()
    ) {
    private val executor = executorService

    fun generateSitemap(): Sitemap {
        val rootLink = Link(url)
        val siteTree = siteTree
        generateSitemapTree(siteTree.getRoot(), siteTree, 0)
        return Sitemap(rootLink, siteTree.getRoot())
    }

    private fun generateSitemapTree(currentNode: TreeNode<Link>, tree: UniqueTree<Link>, depth: Int) {
        val links = getLinks(currentNode.value.url)
        links.forEach {
            val childNode = tree.putExclusive(currentNode, it)
            if (childNode != null) {
                println("level $depth: success add ${it.url}")
                executor.submit{ generateSitemapTree(childNode, tree, depth + 1) }.get()
            }
        }
    }

    private fun getLinks(url: String): List<Link> {
        return Jsoup
            .connect(url)
            .get()
            .select("a")
            .filter { it.attr("href").startsWith(url)  }
            .map { Link(it.attr("href"), it.text()) }
    }
}
