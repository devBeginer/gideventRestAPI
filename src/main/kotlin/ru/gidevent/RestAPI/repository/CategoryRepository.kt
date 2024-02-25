package ru.gidevent.RestAPI.repository

import org.springframework.data.repository.CrudRepository
import ru.gidevent.RestAPI.model.db.Category

interface CategoryRepository: CrudRepository<Category, Long> {
}