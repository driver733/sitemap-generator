package ru.victorpomidor.sitemapgenerator.page

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class JsoupLinkParserTest : StringSpec({
    val parser = JsoupLinkParser(SameDomainFilter())
    val baseUrl = "http://mysite.org"

    "should parse url and text" {
        val links = parser.parsePage("<a href='/url'>link text</a>", baseUrl)

        links.size shouldBe 1
        links[0].url shouldBe "$baseUrl/url"
        links[0].text shouldBe "link text"
    }

    "should not parse url without a tag" {
        val links = parser.parsePage("http://example.org or $baseUrl", baseUrl)

        links.size shouldBe 0
    }
})
