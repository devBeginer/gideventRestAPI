package ru.gidevent.RestAPI.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import ru.gidevent.RestAPI.model.db.Category
import ru.gidevent.RestAPI.model.dto.CategorySuggestion
import ru.gidevent.RestAPI.model.dto.CitySuggestion

interface CategoryRepository: CrudRepository<Category, Long> {

    @Query(
            "SELECT new ru.gidevent.RestAPI.model.dto.CategorySuggestion(c.id, c.name) " +
                    "FROM Category c WHERE c.name LIKE '%' || :query || '%' " +
                    "ORDER BY c.name ASC " +
                    "LIMIT 15"
    )
    fun getCategorySuggestion(query: String): Iterable<CategorySuggestion>
}