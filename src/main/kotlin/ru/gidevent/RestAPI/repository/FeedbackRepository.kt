package ru.gidevent.RestAPI.repository

import org.springframework.data.repository.CrudRepository
import ru.gidevent.RestAPI.model.db.Feedback
import ru.gidevent.RestAPI.model.db.FeedbackId

interface FeedbackRepository: CrudRepository<Feedback, FeedbackId> {
}