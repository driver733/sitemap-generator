package ru.victorpomidor.sitemapgenerator.page

class SameDomainFilter : UrlFilter {
    override fun check(testedUrl: String, baseUrl: String): Boolean {
        return testedUrl.startsWith(baseUrl) || testedUrl.startsWith("/")
    }
}
