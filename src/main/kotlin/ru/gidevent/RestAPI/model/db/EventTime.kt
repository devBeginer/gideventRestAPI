package ru.gidevent.RestAPI.model.db

import jakarta.persistence.*
import ru.gidevent.RestAPI.model.db.Advertisement
import java.util.Calendar

@Entity
@Table(name="EVENT_TIME")
data class EventTime(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "time_id")
        val timeId: Long,
        @ManyToOne(targetEntity = Advertisement::class,  fetch= FetchType.LAZY/*EAGER, cascade = [CascadeType.ALL]*/)
        @JoinColumn(referencedColumnName = "advertisement_id",/*name = "advertisement_id", insertable = false, updatable = false*/)
        //@OnDelete(action = OnDeleteAction.CASCADE)
        val advertisement: Advertisement,
        @Temporal(TemporalType.TIME)
        val time: Calendar,
        val isRepeatable: Boolean,
        val daysOfWeek: String,
        @Temporal(TemporalType.DATE)
        val startDate: Calendar,
        @Temporal(TemporalType.DATE)
        val endDate: Calendar
)
