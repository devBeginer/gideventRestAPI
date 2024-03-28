package ru.gidevent.RestAPI.model.db

import jakarta.persistence.*
import ru.gidevent.RestAPI.model.db.Advertisement


@Entity
@Table(name="CUSTOMER_CATEGORY")
//@IdClass(CustomerCategoryId::class)
data class CustomerCategory (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "customer_category_id")
        val customerCategoryId: Long,
        /*@Id
        @ManyToOne(targetEntity = CustomerCategory::class,  fetch= FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinColumn(name = "customerCategory_id", insertable = false, updatable = false)*/
        //@OnDelete(action = OnDeleteAction.CASCADE)
        //val customerCategory: CustomerCategory,
        /*@Id
        @ManyToOne(targetEntity = Advertisement::class,  fetch= FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinColumn(name = "advertisement_id", insertable = false, updatable = false)*/
        //@OnDelete(action = OnDeleteAction.CASCADE)
        //val advertisement: Advertisement,
        /*@ManyToOne(targetEntity = Advertisement::class,  fetch= FetchType.LAZY*//*EAGER*//*, cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "advertisement_id",*//*name = "advertisement_id", insertable = false, updatable = false*//*)
        val advertisementId: Advertisement,*/
        val name: String
)

/*
@Embeddable
data class CustomerCategoryId(
        @ManyToOne(targetEntity = CustomerCategory::class,  fetch= FetchType.LAZY*/
/*EAGER*//*
, cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "customerCategory_id",*/
/*name = "customerCategory_id", insertable = false, updatable = false*//*
)
        val customerCategoryId: CustomerCategory,
        @ManyToOne(targetEntity = Advertisement::class,  fetch= FetchType.LAZY*/
/*EAGER*//*
, cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "advertisement_id",*/
/*name = "advertisement_id", insertable = false, updatable = false*//*
)
        val advertisementId: Advertisement
): Serializable {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}*/
