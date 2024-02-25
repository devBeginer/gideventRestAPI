package ru.gidevent.RestAPI.model.db

import jakarta.persistence.*


@Entity
@Table(name="CATEGORY")
data class Category(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "category_id")
        val categoryId: Long = 0,
        val name: String
)
