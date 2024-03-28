package ru.gidevent.RestAPI.model.db

import jakarta.persistence.*

@Entity
@Table(name="ADVERTISEMENT")
data class Advertisement(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "advertisement_id")
        val id: Long=0,
        val name: String,
        val duration: Int,
        val description: String,
        @ManyToOne(targetEntity = TransportationVariant::class, fetch= FetchType.LAZY/*EAGER*/)
        @JoinColumn(referencedColumnName = "transportation_id",/*name = "transportation_id", insertable = false, updatable = false*/)
        val transportation: TransportationVariant,
        val ageRestrictions: Int,
        val visitorsCount: Int,
        val isIndividual: Boolean,
        val photos: String,
        val rating: Int,
        @ManyToOne(targetEntity = Category::class, fetch= FetchType.LAZY/*EAGER*/)
        @JoinColumn(referencedColumnName = "category_id",/*name = "category_id", insertable = false, updatable = false*/)
        val category: Category,
        @ManyToOne(targetEntity = City::class, fetch= FetchType.LAZY/*EAGER*/)
        @JoinColumn(referencedColumnName = "city_id",/*name = "city_id", insertable = false, updatable = false*/)
        val city: City,
        @ManyToOne(targetEntity = Seller::class,  fetch= FetchType.LAZY/*EAGER*/)
        @JoinColumn(referencedColumnName = "seller_id",/*name = "seller_id", insertable = false, updatable = false*/)
        val seller: Seller/*,
        @OneToMany(mappedBy = "advertisement", cascade = [CascadeType.ALL], orphanRemoval = true)
        val  priceList: MutableList<TicketPrice>*/
)
