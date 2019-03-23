package com.csgames.mixparadise.ingredients

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.csgames.mixparadise.R
import com.csgames.mixparadise.model.Ingredient
import com.csgames.mixparadise.model.IngredientsResponse

// Since the adapter is never changing, there's no need to use ListAdapter
class IngredientsAdapter(response: IngredientsResponse) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ingredientList: List<Ingredient>
    private var headerPositions = List(4) { 0 }

    init {
        val lists = listOf(response.juices, response.drinks, response.alcohols, response.ingredients)
        headerPositions = headerPositions.foldIndexed(mutableListOf()) { i, list, _ ->
            if (i == 0) list.add(0)
            else {
                list.add(list.last() + lists.last().size)
            }
            list
        }
        ingredientList = lists.flatten()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.view_section_title_item -> IngredientHeaderVH(view)
            else -> IngredientsViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(this::class.java.simpleName, "position: $position")

        if (holder is IngredientHeaderVH) {
            holder.title.text = when (position) {
                headerPositions[0] -> "Juices"
                headerPositions[1] -> "Drinks"
                headerPositions[2] -> "Ingredients"
                else -> "Alcohol"
            }
        } else if (holder is IngredientsViewHolder) {
            val ingredient = ingredientList[position - headerPositions.filter { it < position }.size]
            with(holder) {
                title.text = ingredient.label
                Glide
                    .with(imageView)
                    .load(ingredient.imageUrl)
                    .into(imageView)
            }
        }
    }

    override fun getItemCount() = ingredientList.size + 4

    override fun getItemViewType(position: Int): Int {
        return if (headerPositions.any { it == position }) {
            R.layout.view_section_title_item
        } else {
            R.layout.view_ingredient_item
        }
    }
}