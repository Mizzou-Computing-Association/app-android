package tigerhacks.android.tigerhacksapp.shared

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */
abstract class CategoryListAdapter<T>(private val diff: CategoryDiffItemCallback<T>) : ListAdapter<CategoryWrapper<T>, RecyclerView.ViewHolder>(diff) {
    fun submitCategoryList(list: List<T>) {
        if (list.isNotEmpty()) {
            submitListWithHeaders(list)
        } else super.submitList(emptyList())
    }

    fun getCatItem(position: Int): T {
        return (getItem(position) as CategoryWrapper.Container<T>).item
    }

    fun getHeaderTitle(position: Int): String {
        return (getItem(position) as CategoryWrapper.Header<T>).title
    }

    private fun submitListWithHeaders(list: List<T>) {
        CoroutineScope(Dispatchers.IO).launch {
            val wrappedList = mutableListOf<CategoryWrapper<T>>()

            wrappedList.add(CategoryWrapper.Header(diff.findCategory(list[0])))

            if (list.size > 1) {
                for (i in 0..(list.size - 2)) { //Figures out to ranges of the different categories
                    val currentItem = list[i]
                    val nextItem = list[i + 1]
                    wrappedList.add(CategoryWrapper.Container(list[i]))
                    if (!diff.areCategoriesTheSame(currentItem, nextItem)) {
                        wrappedList.add(CategoryWrapper.Header(diff.findCategory(nextItem)))
                    }
                }
                wrappedList.add(CategoryWrapper.Container(list.last()))
            } else wrappedList.add(CategoryWrapper.Container(list[0]))

            CoroutineScope(Dispatchers.Main).launch {
                super.submitList(wrappedList)
            }
        }
    }

    override fun getItemViewType(position: Int) = if (getItem(position) is CategoryWrapper.Header) 1 else 0
}

sealed class CategoryWrapper<T> {
    data class Header<T>(val title: String): CategoryWrapper<T>()
    data class Container<T>(val item: T): CategoryWrapper<T>()
}

abstract class CategoryDiffItemCallback<T> : DiffUtil.ItemCallback<CategoryWrapper<T>>() {
    override fun areItemsTheSame(oldItem: CategoryWrapper<T>, newItem: CategoryWrapper<T>): Boolean {
        if (oldItem is CategoryWrapper.Header && newItem is CategoryWrapper.Header && oldItem.title == newItem.title) return true

        if (oldItem is CategoryWrapper.Container<T> && newItem is CategoryWrapper.Container<T>) {
            val oldCatItem = oldItem.item
            val newCatItem = newItem.item
            return areCategoryItemsTheSame(oldCatItem, newCatItem)
        }
        return false
    }

    override fun areContentsTheSame(oldItem: CategoryWrapper<T>, newItem: CategoryWrapper<T>): Boolean {
        if (oldItem is CategoryWrapper.Header && newItem is CategoryWrapper.Header && oldItem.title == newItem.title) return true

        if (oldItem is CategoryWrapper.Container<T> && newItem is CategoryWrapper.Container<T>) {
            val oldCatItem = oldItem.item
            val newCatItem = newItem.item
            return areCategoryItemContentsTheSame(oldCatItem, newCatItem)
        }
        return false
    }

    fun areCategoriesTheSame(oldItem: T, newItem: T) = findCategory(oldItem) == findCategory(newItem)

    abstract fun areCategoryItemContentsTheSame(oldItem: T, newItem: T): Boolean
    abstract fun areCategoryItemsTheSame(oldItem: T, newItem: T): Boolean
    abstract fun findCategory(item: T): String
}
