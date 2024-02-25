package ru.gidevent.RestAPI.repository

import org.springframework.data.repository.CrudRepository
import ru.gidevent.RestAPI.model.db.CustomerCategory

interface CustomerCategoryRepository: CrudRepository<CustomerCategory, Long> {
}