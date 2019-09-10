package ru.victorpomidor.sitemapgenerator.model

data class Link(
    val url: String,
    val text: String? = null
) {
    override fun equals(other: Any?): Boolean {
        return (other as? Link)
            ?.url
            ?.equals(this.url)
            ?: false
    }

    override fun hashCode(): Int {
        return url.hashCode()
    }
}
