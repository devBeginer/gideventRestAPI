package ru.gidevent.RestAPI.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.gidevent.RestAPI.auth.AuthenticationService
import ru.gidevent.RestAPI.model.db.*
import ru.gidevent.RestAPI.model.request.*
import ru.gidevent.RestAPI.model.response.*
import ru.gidevent.RestAPI.service.AdvertisementService
import java.io.File
import java.io.FileOutputStream
import java.util.*


@RestController
@RequestMapping("/api/")
class AdvertisementController {

    @Value("\${upload.path}")
    var uploadPath: String? = null

    @Autowired
    lateinit var advertisementService: AdvertisementService
    @Autowired
    lateinit var authService: AuthenticationService

    @GetMapping("auth/advertisement/")
    fun advertisement(): ResponseEntity<*> {
        //return ResponseEntity.ok(advertisementService.allAdvertisements())
        return ResponseEntity.ok(advertisementService.allAdvertisements())
    }

    @GetMapping("auth/advertisement/top")
    fun topAdvertisement(): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.topAdvertisements())
    }

    @GetMapping("advertisement/")
    fun advertisementPrivate(): ResponseEntity<*> {
        val profile = authService.getUser()
        return ResponseEntity.ok(advertisementService.allAdvertisements(profile.id))
    }

    @GetMapping("advertisement/top")
    fun topAdvertisementPrivate(): ResponseEntity<*> {
        val profile = authService.getUser()
        return ResponseEntity.ok(advertisementService.topAdvertisements(profile.id))
    }

    @GetMapping("advertisement/favourite")
    fun favouriteAdvertisement(): ResponseEntity<*> {
        val profile = authService.getUser()
        return ResponseEntity.ok(advertisementService.favouriteAdvertisements(profile.id))
    }

    @GetMapping("auth/advertisement/{id}")
    fun advertisementById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.getExpandedAdvertisementById(id))
    }

    @GetMapping("auth/tmp/{id}")
    fun tmpById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.getAdvertisementById(id))
    }

    @GetMapping("advertisement/{id}")
    fun advertisementByIdPrivate(@PathVariable id: Long): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        return ResponseEntity.ok(advertisementService.getExpandedAdvertisementById(id, profile))
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

    @GetMapping("auth/advertisement/eventTime/{id}")
    fun eventTimeByAdvertisement(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.getEventTimeByAdvertisement(id))
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

    @GetMapping("auth/advertisement/ticketPrice/{id}")
    fun ticketPriceByAdvertisement(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.getTicketPriceByAdvertisement(id))
    }

    @GetMapping("auth/advertisement/emptyTime/{id}")
    fun emptyTimeByAdvertisement(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.getEmptyEventTimeByAdvertisement(id, Calendar.getInstance(Locale.getDefault())))
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
        val profile = authService.getUser()
        val transportation = advertisementService.getTransportationVariantById(advertisementRequest.transportation)
        val category = advertisementService.getCategoryById(advertisementRequest.category)
        val city = advertisementService.getCityById(advertisementRequest.city)
        val seller = advertisementService.getSellerById(profile.id)

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
        val profile = authService.getUser()
        val transportation = advertisementService.getTransportationVariantById(advertisementRequest.transportation)
        val category = advertisementService.getCategoryById(advertisementRequest.category)
        val city = advertisementService.getCityById(advertisementRequest.city)
        val seller = advertisementService.getSellerById(profile.id)

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
        /*val advertisement = advertisementService.getAdvertisementById(customerCategory.advertisementId)


        return if (advertisement != null) {
            val newCategory = advertisementService.saveCustomerCategory(CustomerCategory(
                    customerCategoryId = customerCategory.customerCategoryId,
                    advertisementId = advertisement,
                    name = customerCategory.name
            ))
            ResponseEntity.ok(newCategory)
        } else {
            ResponseEntity(ResponseMessage("advertisement is not exist"), HttpStatus.BAD_REQUEST)
        }*/
        val newCategory = advertisementService.saveCustomerCategory(CustomerCategory(
                customerCategoryId = customerCategory.customerCategoryId,
                name = customerCategory.name
        ))
        return ResponseEntity.ok(newCategory)

    }


    //@PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("customerCategory/")
    fun updateCustomerCategory(@RequestBody customerCategory: CustomerCategoryRequest): ResponseEntity<*> {

        /*val advertisement = advertisementService.getAdvertisementById(customerCategory.advertisementId)


        return if (advertisement != null) {
            val newCategory = advertisementService.updateCustomerCategory(customerCategory.customerCategoryId, CustomerCategory(
                    customerCategoryId = customerCategory.customerCategoryId,
                    advertisementId = advertisement,
                    name = customerCategory.name
            ))
            ResponseEntity.ok(newCategory)
        } else {
            ResponseEntity(ResponseMessage("advertisement is not exist"), HttpStatus.BAD_REQUEST)
        }*/
        val newCategory = advertisementService.updateCustomerCategory(customerCategory.customerCategoryId, CustomerCategory(
                customerCategoryId = customerCategory.customerCategoryId,
                name = customerCategory.name
        ))
        return ResponseEntity.ok(newCategory)

    }

    //@PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("eventTime/")
    fun postEventTime(@RequestBody eventTimeRequest: EventTimeRequest): ResponseEntity<*> {
        val advertisement = advertisementService.getAdvertisementById(eventTimeRequest.advertisement)
        val startDate = Calendar.getInstance(Locale.getDefault())
        startDate.timeInMillis = eventTimeRequest.startDate
        val endDate = Calendar.getInstance(Locale.getDefault())
        endDate.timeInMillis = eventTimeRequest.endDate
        val time = Calendar.getInstance(Locale.getDefault())
        time.timeInMillis = eventTimeRequest.time
        //val daysOfWeek = eventTimeRequest.daysOfWeek.split(",")

        return if (advertisement != null) {
            val eventTime = advertisementService.saveEventTime(EventTime(
                        timeId = eventTimeRequest.timeId,
                        advertisement = advertisement,
                        time = time,
                        isRepeatable = eventTimeRequest.isRepeatable,
                        daysOfWeek = eventTimeRequest.daysOfWeek,
                        startDate = startDate,
                        endDate = endDate
                ))

            ResponseEntity.ok(eventTime)
        } else {
            ResponseEntity(ResponseMessage("advertisement is not exist"), HttpStatus.BAD_REQUEST)
        }
    }


    //@PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("eventTime/")
    fun updateEventTime(@RequestBody eventTimeRequest: EventTimeRequest): ResponseEntity<*> {

        val advertisement = advertisementService.getAdvertisementById(eventTimeRequest.advertisement)
        val startDate = Calendar.getInstance(Locale.getDefault())
        startDate.timeInMillis = eventTimeRequest.startDate
        val endDate = Calendar.getInstance(Locale.getDefault())
        endDate.timeInMillis = eventTimeRequest.endDate
        val time = Calendar.getInstance(Locale.getDefault())
        time.timeInMillis = eventTimeRequest.time

        return if (advertisement != null) {
            val newCategory = advertisementService.updateEventTime(eventTimeRequest.timeId, EventTime(
                    timeId = eventTimeRequest.timeId,
                    advertisement = advertisement,
                    time = time,
                    isRepeatable = eventTimeRequest.isRepeatable,
                    daysOfWeek = eventTimeRequest.daysOfWeek,
                    startDate = startDate,
                    endDate = endDate
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


    @PostMapping(path = ["photo/"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun postPoster(@RequestParam("file") file: MultipartFile): ResponseEntity<*> {
        val uploadDir = File(uploadPath)
        if (!uploadDir.exists()) uploadDir.mkdir()

        val fileUUID = UUID.randomUUID().toString() + ".jpeg"
        val image = File(uploadPath, fileUUID)
        image.createNewFile()

        FileOutputStream(image).buffered().use { output->
            output.write(file.bytes)
        }

        return if (true) {
            ResponseEntity.ok(ResponsePoster(fileUUID))
        } else {
            ResponseEntity(ResponseMessage("File is not uploaded"), HttpStatus.BAD_REQUEST)
        }
    }



    @GetMapping("auth/photo/{fileUUID:.+}")
    fun initPoster(@PathVariable fileUUID: String): ResponseEntity<*> {
        var file = File("$uploadPath/$fileUUID")

        return if (file.exists()) {
            ResponseEntity.ok().header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "inline; filename=\"${file.name}\"") //attachment
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(UrlResource(file.toURI()))
            //.body(InputStreamResource(file.inputStream()))
        } else {
            ResponseEntity(ResponseMessage("file is not exist"), HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("favourite/")
    fun postFavouriteRequest(@RequestParam("advertisementId") advertisementId: Long): ResponseEntity<*> {
        val profile = authService.getUserRecord()

        val advertisement = advertisementService.getAdvertisementById(advertisementId)


        return if (advertisement != null) {

            val newFavourite = advertisement.let { advert->
                val favourite = advertisementService.getFavourite(FavouriteId(profile, advert))
                if(favourite==null){
                    advertisementService.saveFavourite(Favourite(FavouriteId(profile, advert)))
                }else{
                    advertisementService.deleteFavourite(Favourite(FavouriteId(profile, advert)))
                    null
                }
            }

            val advert = advertisementService.getAdvertisementById(profile.id, advertisement.id)
            ResponseEntity.ok(advert)


        } else {
            ResponseEntity(ResponseMessage("advertisement is not exist"), HttpStatus.BAD_REQUEST)
        }

    }

    @GetMapping("bookingParams/")
    fun getBookingParamsRequest(@RequestParam("advertisementId") advertisementId: Long, @RequestParam("date") dateParam: Long): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        val date = Calendar.getInstance(Locale.getDefault())
        date.timeInMillis = dateParam
        val ticketPrice = advertisementService.getTicketPriceByAdvertisement(advertisementId)
        val eventTimeList = advertisementService.getEmptyEventTimeByAdvertisement(advertisementId, date)

        return if (ticketPrice != null && eventTimeList != null) {
            ResponseEntity.ok(BookingParamsResponse(eventTimeList, ticketPrice.toList()))
        } else {
            ResponseEntity(ResponseMessage("ticketPrice or eventTimeList is not exist"), HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("booking/")
    fun postBookingRequest(@RequestBody bookingRequest: BookingRequest): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        val advertisement = advertisementService.getAdvertisementById(bookingRequest.advertisement)
        val eventTime = advertisementService.getEventTimeById(bookingRequest.eventTime)
        val bookingTime = Calendar.getInstance(Locale.getDefault())
        val date = Calendar.getInstance(Locale.getDefault())
        date.timeInMillis = bookingRequest.date
        return if (advertisement != null && eventTime != null) {
            val booking = advertisementService.saveBooking(
                    Booking(
                            0,
                            eventTime,
                            profile,
                            advertisement,
                            bookingTime,
                            date,
                            bookingRequest.totalPrice,
                            bookingRequest.idApproved
                    )
            )

            bookingRequest.groups.forEach {
                val customerCategory = advertisementService.getCustomerCategoryById(it.customerCategory)
                if (customerCategory != null) {
                    val visitorsGroup = advertisementService.saveGroup(VisitorsGroup(0, customerCategory, it.count, booking))
                }
            }
            ResponseEntity.ok(
                    BookingResponse(
                            booking.id,
                            booking.eventTime.timeId,
                            booking.user.id,
                            booking.advertisement.id,
                            booking.bookingTime.timeInMillis,
                            booking.date.timeInMillis,
                            booking.totalPrice,
                            booking.idApproved
                    )
            )
        } else {
            ResponseEntity(ResponseMessage("advertisement or eventTimeList is not exist"), HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("favourite/")
    fun deleteFavouriteRequest(@RequestParam("advertisementId") advertisementId: Long): ResponseEntity<*> {
        val profile = authService.getUserRecord()

        val advertisement = advertisementService.getAdvertisementById(advertisementId)


        return if (advertisement != null) {
            advertisement.let { advert->advertisementService.deleteFavourite(
                    Favourite(FavouriteId(profile, advert))
            ) }
            ResponseEntity.ok("Disliked")
        } else {
            ResponseEntity(ResponseMessage("advertisement is not exist"), HttpStatus.BAD_REQUEST)
        }

    }

    @PostMapping("feedback/")
    fun postFeedbackRequest(@RequestBody feedbackRequest: FeedbackRequest): ResponseEntity<*> {
        val profile = authService.getUserRecord()

        val advertisement = advertisementService.getAdvertisementById(feedbackRequest.advertisementId)


        return if (advertisement != null) {
            val newFeedback = advertisement.let { advert->advertisementService.saveFeedback(
                    Feedback(FeedbackId(profile, advert), feedbackRequest.rating, feedbackRequest.text)
            ) }
            ResponseEntity.ok(NewFeedbackResponse(newFeedback.rating, newFeedback.text))
        } else {
            ResponseEntity(ResponseMessage("advertisement is not exist"), HttpStatus.BAD_REQUEST)
        }

    }


    @PutMapping("feedback/")
    fun updateFeedbackRequest(@RequestBody feedbackRequest: FeedbackRequest): ResponseEntity<*> {

        val profile = authService.getUserRecord()

        val advertisement = advertisementService.getAdvertisementById(feedbackRequest.advertisementId)

        return if (advertisement != null) {
            val newFeedback = advertisementService.updateFeedback(FeedbackId(profile, advertisement),
                    Feedback(FeedbackId(profile, advertisement), feedbackRequest.rating, feedbackRequest.text)
            )
            if(newFeedback!=null){
                ResponseEntity.ok(NewFeedbackResponse(newFeedback.rating, newFeedback.text))
            }else{
                ResponseEntity(ResponseMessage("feedback is not exist"), HttpStatus.BAD_REQUEST)
            }
        } else {
            ResponseEntity(ResponseMessage("advertisement is not exist"), HttpStatus.BAD_REQUEST)
        }

    }
}