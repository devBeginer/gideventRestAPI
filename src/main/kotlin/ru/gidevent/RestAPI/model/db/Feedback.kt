package ru.gidevent.RestAPI.model.db

import jakarta.persistence.*
import ru.gidevent.RestAPI.auth.User
import java.io.Serializable

@Entity
@Table(name="FEEDBACK")
//@IdClass(FeedbackId::class)
data class Feedback(
        /*@Id
        @ManyToOne(targetEntity = User::class,  fetch= FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinColumn(name = "user_id", insertable = false, updatable = false)*/
        //@OnDelete(action = OnDeleteAction.CASCADE)
        //val user: User,
        /*@Id
        @ManyToOne(targetEntity = Advertisement::class,  fetch= FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinColumn(name = "advertisement_id", insertable = false, updatable = false)*/
        //@OnDelete(action = OnDeleteAction.CASCADE)
        //val advertisement: Advertisement,
        @EmbeddedId
        val feedbackId: FeedbackId,
        val rating: Int,
        val text: String
)

@Embeddable
data class FeedbackId(
        @ManyToOne(targetEntity = User::class,  fetch= FetchType.LAZY/*EAGER*/, cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "user_id",/*name = "user_id", insertable = false, updatable = false*/)
        val userId: User,
        @ManyToOne(targetEntity = Advertisement::class,  fetch= FetchType.LAZY/*EAGER*/, cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "advertisement_id",/*name = "advertisement_id", insertable = false, updatable = false*/)
        val advertisementId: Advertisement
): Serializable {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
