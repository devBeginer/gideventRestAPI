package ru.gidevent.RestAPI.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.gidevent.RestAPI.model.*
import ru.gidevent.RestAPI.model.db.*
import ru.gidevent.RestAPI.model.request.*
import ru.gidevent.RestAPI.model.response.ResponseMessage
import ru.gidevent.RestAPI.service.AdvertisementService

@RestController
@RequestMapping("/api/")
class AdvertisementController {
    @Autowired
    lateinit var advertisementService: AdvertisementService

    @GetMapping("auth/advertisement/")
    fun advertisement(): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.allAdvertisements())
    }

    @GetMapping("auth/advertisement/top")
    fun topAdvertisement(): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.topAdvertisements())
    }

    @GetMapping("auth/advertisement/{id}")
    fun advertisementById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.getAdvertisementById(id))
    }

    @GetMapping("auth/category/")
    fun category(): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.allCategory())
    }

    @GetMapping("auth/category/{id}")
    fun categoryById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.getCategoryById(id))
    }

    @GetMapping("auth/city/")
    fun city(): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.allCity())
    }

    @GetMapping("auth/city/{id}")
    fun cityById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.getCityById(id))
    }

    @GetMapping("auth/customerCategory/")
    fun customerCategory(): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.allCustomerCategory())
    }

    @GetMapping("auth/customerCategory/{id}")
    fun customerCategoryById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.getCustomerCategoryById(id))
    }

    @GetMapping("auth/eventTime/")
    fun eventTime(): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.allEventTime())
    }

    @GetMapping("auth/eventTime/{id}")
    fun eventTimeById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.getEventTimeById(id))
    }

    @GetMapping("auth/feedback/")
    fun feedback(): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.allFeedback())
    }


    @GetMapping("auth/seller/")
    fun seller(): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.allSeller())
    }

    @GetMapping("auth/seller/{id}")
    fun sellerById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.getSellerById(id))
    }

    @GetMapping("auth/ticketPrice/")
    fun ticketPrice(): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.allTicketPrice())
    }

    @GetMapping("auth/ticketPrice/{id}")
    fun ticketPriceById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.getTicketPriceById(id))
    }

    @GetMapping("auth/transportationVariant/")
    fun transportationVariant(): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.allTransportationVariant())
    }

    @GetMapping("auth/transportationVariant/{id}")
    fun transportationVariantById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.getTransportationVariantById(id))
    }

    //@PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("advertisement/")
    fun postAdvertisement(@RequestBody advertisementRequest: AdvertisementRequest): ResponseEntity<*> {
        val transportation = advertisementService.getTransportationVariantById(advertisementRequest.transportation)
        val category = advertisementService.getCategoryById(advertisementRequest.category)
        val city = advertisementService.getCityById(advertisementRequest.city)
        val seller = advertisementService.getSellerById(advertisementRequest.seller)

        return if (transportation != null && category != null && city != null && seller != null) {
            val newAdvertisement = advertisementService.saveAdvertisement(
                    Advertisement(
                            advertisementRequest.id,
                            advertisementRequest.name,
                            advertisementRequest.duration,
                            advertisementRequest.description,
                            transportation,
                            advertisementRequest.ageRestrictions,
                            advertisementRequest.visitorsCount,
                            advertisementRequest.isIndividual,
                            advertisementRequest.photos,
                            advertisementRequest.rating,
                            category,
                            city,
                            seller
                    )
            )
            ResponseEntity.ok(newAdvertisement)
        } else {
            ResponseEntity(ResponseMessage("transportation or category or city or seller is not exist"), HttpStatus.BAD_REQUEST)
        }
    }


    //@PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("advertisement/")
    fun updateAdvertisement(@RequestBody advertisementRequest: AdvertisementRequest): ResponseEntity<*> {
        val transportation = advertisementService.getTransportationVariantById(advertisementRequest.transportation)
        val category = advertisementService.getCategoryById(advertisementRequest.category)
        val city = advertisementService.getCityById(advertisementRequest.city)
        val seller = advertisementService.getSellerById(advertisementRequest.seller)

        return if (transportation != null && category != null && city != null && seller != null) {
            val newAdvertisement = advertisementService.updateAdvertisement(advertisementRequest.id,
                    Advertisement(
                            advertisementRequest.id,
                            advertisementRequest.name,
                            advertisementRequest.duration,
                            advertisementRequest.description,
                            transportation,
                            advertisementRequest.ageRestrictions,
                            advertisementRequest.visitorsCount,
                            advertisementRequest.isIndividual,
                            advertisementRequest.photos,
                            advertisementRequest.rating,
                            category,
                            city,
                            seller
                    )
            )
            ResponseEntity.ok(newAdvertisement)
        } else {
            ResponseEntity(ResponseMessage("transportation or category or city or seller is not exist"), HttpStatus.BAD_REQUEST)
        }
    }

    //@PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("category/")
    fun postCategory(@RequestBody categoryRequest: CategoryRequest): ResponseEntity<*> {

        val newCategory = advertisementService.saveCategory(Category(
                categoryId = categoryRequest.categoryId,
                name = categoryRequest.name
        ))
        return ResponseEntity.ok(newCategory)

    }


    //@PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("category/")
    fun updateCategory(@RequestBody categoryRequest: CategoryRequest): ResponseEntity<*> {

        val newCategory = advertisementService.updateCategory(categoryRequest.categoryId, Category(
                categoryId = categoryRequest.categoryId,
                name = categoryRequest.name
        ))
        return ResponseEntity.ok(newCategory)

    }

    //@PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("city/")
    fun postCity(@RequestBody cityRequest: CityRequest): ResponseEntity<*> {

        val newCategory = advertisementService.saveCity(City(
                cityId = cityRequest.cityId,
                name = cityRequest.name
        ))
        return ResponseEntity.ok(newCategory)

    }


    //@PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("city/")
    fun updateCity(@RequestBody cityRequest: CityRequest): ResponseEntity<*> {

        val newCategory = advertisementService.updateCity(cityRequest.cityId, City(
                cityId = cityRequest.cityId,
                name = cityRequest.name
        ))
        return ResponseEntity.ok(newCategory)

    }

    //@PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("customerCategory/")
    fun postCustomerCategory(@RequestBody customerCategory: CustomerCategoryRequest): ResponseEntity<*> {
        val advertisement = advertisementService.getAdvertisementById(customerCategory.advertisementId)


        return if (advertisement != null) {
            val newCategory = advertisementService.saveCustomerCategory(CustomerCategory(
                    customerCategoryId = customerCategory.customerCategoryId,
                    advertisementId = advertisement,
                    name = customerCategory.name
            ))
            ResponseEntity.ok(newCategory)
        } else {
            ResponseEntity(ResponseMessage("advertisement is not exist"), HttpStatus.BAD_REQUEST)
        }


    }


    //@PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("customerCategory/")
    fun updateCustomerCategory(@RequestBody customerCategory: CustomerCategoryRequest): ResponseEntity<*> {

        val advertisement = advertisementService.getAdvertisementById(customerCategory.advertisementId)


        return if (advertisement != null) {
            val newCategory = advertisementService.updateCustomerCategory(customerCategory.customerCategoryId, CustomerCategory(
                    customerCategoryId = customerCategory.customerCategoryId,
                    advertisementId = advertisement,
                    name = customerCategory.name
            ))
            ResponseEntity.ok(newCategory)
        } else {
            ResponseEntity(ResponseMessage("advertisement is not exist"), HttpStatus.BAD_REQUEST)
        }

    }

    //@PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("eventTime/")
    fun postEventTime(@RequestBody eventTimeRequest: EventTimeRequest): ResponseEntity<*> {
        val advertisement = advertisementService.getAdvertisementById(eventTimeRequest.advertisement)


        return if (advertisement != null) {
            val newCategory = advertisementService.saveEventTime(EventTime(
                    timeId = eventTimeRequest.timeId,
                    advertisement = advertisement,
                    time = eventTimeRequest.time,
                    isRepeatable = eventTimeRequest.isRepeatable,
                    daysOfWeek = eventTimeRequest.daysOfWeek,
                    date = eventTimeRequest.date,
            ))
            ResponseEntity.ok(newCategory)
        } else {
            ResponseEntity(ResponseMessage("advertisement is not exist"), HttpStatus.BAD_REQUEST)
        }
    }


    //@PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("eventTime/")
    fun updateEventTime(@RequestBody eventTimeRequest: EventTimeRequest): ResponseEntity<*> {

        val advertisement = advertisementService.getAdvertisementById(eventTimeRequest.advertisement)


        return if (advertisement != null) {
            val newCategory = advertisementService.updateEventTime(eventTimeRequest.timeId, EventTime(
                    timeId = eventTimeRequest.timeId,
                    advertisement = advertisement,
                    time = eventTimeRequest.time,
                    isRepeatable = eventTimeRequest.isRepeatable,
                    daysOfWeek = eventTimeRequest.daysOfWeek,
                    date = eventTimeRequest.date,
            ))
            ResponseEntity.ok(newCategory)
        } else {
            ResponseEntity(ResponseMessage("advertisement is not exist"), HttpStatus.BAD_REQUEST)
        }

    }


    //@PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("seller/")
    fun postCategoryRequest(@RequestBody sellerRequest: SellerRequest): ResponseEntity<*> {

        val newCategory = advertisementService.saveSeller(Seller(
                sellerId = sellerRequest.sellerId,
                login = sellerRequest.login,
                password = sellerRequest.password,
                firstName = sellerRequest.firstName,
                lastName = sellerRequest.lastName,
                photo = sellerRequest.photo,
                about = sellerRequest.about
        ))
        return ResponseEntity.ok(newCategory)

    }


    //@PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("seller/")
    fun updateCategoryRequest(@RequestBody sellerRequest: SellerRequest): ResponseEntity<*> {

        val newCategory = advertisementService.updateSeller(sellerRequest.sellerId, Seller(
                sellerId = sellerRequest.sellerId,
                login = sellerRequest.login,
                password = sellerRequest.password,
                firstName = sellerRequest.firstName,
                lastName = sellerRequest.lastName,
                photo = sellerRequest.photo,
                about = sellerRequest.about
        ))
        return ResponseEntity.ok(newCategory)

    }

    //@PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("ticketPrice/")
    fun postTicketPrice(@RequestBody ticketPriceRequest: TicketPriceRequest): ResponseEntity<*> {

        val advertisement = advertisementService.getAdvertisementById(ticketPriceRequest.advertisement)
        val customerCategory = advertisementService.getCustomerCategoryById(ticketPriceRequest.customerCategory)


        return if (advertisement != null && customerCategory != null) {
            val newCategory = advertisementService.saveTicketPrice(TicketPrice(
                    priceId =  ticketPriceRequest.priceId,
                    advertisement = advertisement,
                    customerCategory = customerCategory,
                    price = ticketPriceRequest.price
            ))
            ResponseEntity.ok(newCategory)
        } else {
            ResponseEntity(ResponseMessage("advertisement or customerCategory is not exist"), HttpStatus.BAD_REQUEST)
        }
    }


    //@PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("ticketPrice/")
    fun updateTicketPrice(@RequestBody ticketPriceRequest: TicketPriceRequest): ResponseEntity<*> {

        val advertisement = advertisementService.getAdvertisementById(ticketPriceRequest.advertisement)
        val customerCategory = advertisementService.getCustomerCategoryById(ticketPriceRequest.customerCategory)


        return if (advertisement != null && customerCategory != null) {
            val newCategory = advertisementService.updateTicketPrice(ticketPriceRequest.priceId, TicketPrice(
                    priceId =  ticketPriceRequest.priceId,
                    advertisement = advertisement,
                    customerCategory = customerCategory,
                    price = ticketPriceRequest.price
            ))
            ResponseEntity.ok(newCategory)
        } else {
            ResponseEntity(ResponseMessage("advertisement or customerCategory is not exist"), HttpStatus.BAD_REQUEST)
        }

    }

    //@PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("transportationVariant/")
    fun postTransportationVariantRequest(@RequestBody transportationVariantRequest: TransportationVariantRequest): ResponseEntity<*> {

        val newCategory = advertisementService.saveTransportationVariant(TransportationVariant(
                transportationId = transportationVariantRequest.transportationId,
                name = transportationVariantRequest.name
        ))
        return ResponseEntity.ok(newCategory)

    }


    //@PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("transportationVariant/")
    fun updateTransportationVariantRequest(@RequestBody transportationVariantRequest: TransportationVariantRequest): ResponseEntity<*> {

        val newCategory = advertisementService.updateTransportationVariant(transportationVariantRequest.transportationId, TransportationVariant(
                transportationId = transportationVariantRequest.transportationId,
                name = transportationVariantRequest.name
        ))
        return ResponseEntity.ok(newCategory)

    }


}