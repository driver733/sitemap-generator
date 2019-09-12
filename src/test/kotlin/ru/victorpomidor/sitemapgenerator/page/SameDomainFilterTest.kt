package ru.victorpomidor.sitemapgenerator.page

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class SameDomainFilterTest : StringSpec({
    val filter = SameDomainFilter()
    val baseUrl = "http://mysite.com"

    "test filtering" {
        forall(
            row("$baseUrl/", true),
            row("$baseUrl/abc", true),
            row("/abc", true),
            row("abc", false),
            row("/abc/def", true),
            row("abc/def", false),
            row("?page=2", false),
            row("site.com", false),
            row("http://site.com", false),
            row("https://site.com", false)
        ) { url, result ->
            filter.check(url, baseUrl) shouldBe result
        }
    }
})
