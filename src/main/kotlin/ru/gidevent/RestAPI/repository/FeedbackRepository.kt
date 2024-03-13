package ru.gidevent.RestAPI.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import ru.gidevent.RestAPI.model.db.Advertisement
import ru.gidevent.RestAPI.model.db.Feedback
import ru.gidevent.RestAPI.model.db.FeedbackId
import java.awt.print.Book


interface FeedbackRepository: CrudRepository<Feedback, FeedbackId> {


    fun findByFeedbackIdAdvertisement(advertisement: Advertisement): Iterable<Feedback>
}