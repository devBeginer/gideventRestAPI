package ru.gidevent.RestAPI.model.db

import jakarta.persistence.*
import ru.gidevent.RestAPI.auth.User
import java.io.Serializable

@Entity
@Table(name="FEEDBACK")
data class Feedback(
        @EmbeddedId
        val feedbackId: FeedbackId,
        val rating: Int,
        val text: String
)

@Embeddable
data class FeedbackId(
        @ManyToOne(targetEntity = User::class,  fetch= FetchType.LAZY/*EAGER, cascade = [CascadeType.ALL]*/)
        @JoinColumn(referencedColumnName = "user_id", insertable = false,/*name = "user_id", updatable = false*/)
        val user: User,
        @ManyToOne(targetEntity = Advertisement::class,  fetch= FetchType.LAZY/*EAGER, cascade = [CascadeType.ALL]*/)
        @JoinColumn(referencedColumnName = "advertisement_id", insertable = false,/*name = "advertisement_id", updatable = false*/)
        val advertisement: Advertisement
): Serializable {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
