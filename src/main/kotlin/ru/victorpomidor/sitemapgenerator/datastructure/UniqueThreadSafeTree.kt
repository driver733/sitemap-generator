package ru.victorpomidor.sitemapgenerator.datastructure

/**
 * Thread-safe implementation of UniqueTree, which uses HashSet to fast checking for duplicates
 */
class UniqueThreadSafeTree<T>(rootValue: T) : UniqueTree<T> {
    private val root: TreeNode<T> = TreeNode(rootValue)
    private val elements = mutableSetOf(root.value)

    override fun getRoot(): TreeNode<T> = root

    @Synchronized override fun putExclusive(parent: TreeNode<T>, value: T): TreeNode<T>? {
        return if(elements.add(value)){
            TreeNode(value, parent)
                .also {  parent.childs.add(it) }
        } else null
    }

}
