package ru.victorpomidor.sitemapgenerator.datastructure

/**
 * Thread-safe implementation of UniqueTree, which uses HashSet to fast checking for duplicates
 */
class UniqueThreadSafeTree<T>(rootValue: T) : UniqueTree<T> {
    private val root: TreeNode<T> = TreeNode(rootValue)
    private val elements = mutableSetOf(root.value)

    override fun getRoot(): TreeNode<T> = root

    override fun putExclusive(parent: TreeNode<T>, value: T): PutResult<TreeNode<T>> {
        if (elements.contains(value)) {
            return PutResult.Duplicated()
        }

        synchronized(this) {
            return if (elements.add(value)) {
                val newNode = TreeNode(value, parent)
                parent.childs.add(newNode)
                PutResult.Ok(newNode)
            } else PutResult.Duplicated()
        }
    }
}
