package ru.gidevent.RestAPI.model.db

import jakarta.persistence.*
import ru.gidevent.RestAPI.auth.User
import java.io.Serializable

@Entity
@Table(name="FAVOURITE")
data class Favourite(
        @EmbeddedId
        val favouriteId: FavouriteId
)

@Embeddable
data class FavouriteId(
        @ManyToOne(targetEntity = User::class,  fetch= FetchType.LAZY/*EAGER, cascade = [CascadeType.ALL]*/)
        @JoinColumn(referencedColumnName = "user_id",/*name = "user_id", insertable = false, updatable = false*/)
        val userId: User,
        @ManyToOne(targetEntity = Advertisement::class,  fetch= FetchType.LAZY/*EAGER, cascade = [CascadeType.ALL]*/)
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
