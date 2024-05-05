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
import ru.gidevent.RestAPI.auth.*
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

    @GetMapping("advertisement/purchases")
    fun reservedAdvertisement(): ResponseEntity<*> {
        val profile = authService.getUserRecord()

        val booking = advertisementService.getBookings(profile.id)

        return if (booking != null) {
            val sellerBookingResponse = booking.map {
                val visitorsGroup = advertisementService.getGroupByBooking(it)
                var visitorsCount = 0
                visitorsGroup.forEach { group ->
                    visitorsCount += group.count
                }
                val date = it.date

                SellerBookingResponse(
                        it.id,
                        it.eventTime.time.timeInMillis,
                        it.advertisement.name,
                        it.date.timeInMillis,
                        it.totalPrice,
                        it.isApproved,
                        visitorsCount
                )
            }
            ResponseEntity.ok(
                    sellerBookingResponse
            )
        } else {
            ResponseEntity(ResponseMessage("booking is not exist"), HttpStatus.BAD_REQUEST)
        }
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

    @GetMapping("edit/advertisement/{id}")
    fun getAdvertisementForEdit(@PathVariable id: Long): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        val seller = advertisementService.getSellerById(profile.id)
        val advertisement = advertisementService.getAdvertisementById(id)
        return if(seller!=null && advertisement!=null && advertisement.seller.sellerId == seller.sellerId){
            ResponseEntity.ok(advertisement)
        }else{
            ResponseEntity(ResponseMessage("seller or advertisement is not exist"), HttpStatus.BAD_REQUEST)
        }
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

    @GetMapping("feedback/")
    fun feedback(@RequestParam("advertisementId") advertisementId: Long): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        val advertisement = advertisementService.getAdvertisementById(advertisementId)
        val feedback = advertisement?.let { advertisementService.getFeedbackById(FeedbackId(profile, it)) }
        return if(feedback!=null){
            ResponseEntity.ok(feedback)
        }else{
            ResponseEntity(ResponseMessage("advertisement or feedback is not exist"), HttpStatus.BAD_REQUEST)
        }

    }


    @GetMapping("auth/seller/")
    fun seller(): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.allSeller())
    }

    /*@GetMapping("auth/seller/{id}")
    fun sellerById(@PathVariable id: Long): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.getSellerById(id))
    }*/

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
        val profile = authService.getUserRecord()
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
                            seller,
                            "MODERATION"
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
        val profile = authService.getUserRecord()
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
                    seller,
                        "MODERATION"
                )
            )
            ResponseEntity.ok(newAdvertisement)
        } else {
            ResponseEntity(ResponseMessage("transportation or category or city or seller is not exist"), HttpStatus.BAD_REQUEST)
        }
    }


    //@PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("advertisement/")
    fun deleteAdvertisement(@RequestParam("advertisementId") advertisementId: Long): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        val seller = advertisementService.getSellerById(profile.id)
        val advertisement = advertisementService.getAdvertisementById(advertisementId)

        return if (advertisement != null && seller!=null && seller.sellerId==advertisement.seller.sellerId) {
            ResponseEntity.ok(advertisementService.deleteAdvertisement(advertisementId))
        } else {
            ResponseEntity(ResponseMessage("transportation or category or city or seller is not exist"), HttpStatus.BAD_REQUEST)
        }
    }


    //@PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("eventTime/")
    fun deleteEventTime(@RequestParam("eventTimeId") eventTimeId: Long): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        val seller = advertisementService.getSellerById(profile.id)
        val eventTime = advertisementService.getEventTimeById(eventTimeId)

        return if (eventTime != null && seller!=null && seller.sellerId==eventTime.advertisement.seller.sellerId) {
            ResponseEntity.ok(advertisementService.deleteEventTime(eventTimeId))
        } else {
            ResponseEntity(ResponseMessage("transportation or category or city or seller is not exist"), HttpStatus.BAD_REQUEST)
        }
    }


    //@PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("ticketPrice/")
    fun deleteTicketPrice(@RequestParam("ticketPriceId") ticketPriceId: Long): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        val seller = advertisementService.getSellerById(profile.id)
        val ticketPrice = advertisementService.getTicketPriceById(ticketPriceId)

        return if (ticketPrice != null && seller!=null && seller.sellerId==ticketPrice.advertisement.seller.sellerId) {
            ResponseEntity.ok(advertisementService.deleteTicketPrice(ticketPriceId))
        } else {
            ResponseEntity(ResponseMessage("transportation or category or city or seller is not exist"), HttpStatus.BAD_REQUEST)
        }
    }
    @DeleteMapping("category/")
    fun deleteCategory(@RequestParam("categoryId") id: Long): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        val category = advertisementService.getCategoryById(id)

        return if (category != null) {
            ResponseEntity.ok(advertisementService.deleteCategory(id))
        } else {
            ResponseEntity(ResponseMessage("category is not exist"), HttpStatus.BAD_REQUEST)
        }
    }
    @DeleteMapping("customerCategory/")
    fun deleteCustomerCategory(@RequestParam("customerId") id: Long): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        val category = advertisementService.getCustomerCategoryById(id)

        return if (category != null) {
            ResponseEntity.ok(advertisementService.deleteCustomerCategory(id))
        } else {
            ResponseEntity(ResponseMessage("category is not exist"), HttpStatus.BAD_REQUEST)
        }
    }
    @DeleteMapping("transportation/")
    fun deleteTransportation(@RequestParam("transportationId") id: Long): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        val transportationVariant = advertisementService.getTransportationVariantById(id)

        return if (transportationVariant != null) {
            ResponseEntity.ok(advertisementService.deleteTransport(id))
        } else {
            ResponseEntity(ResponseMessage("transportationVariant is not exist"), HttpStatus.BAD_REQUEST)
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

    @GetMapping("profile")
    fun getProfile(): ResponseEntity<UserDetailsResponse> {
        val profileResponse = authService.getUserDetails()
        val seller = advertisementService.getSellerById(profileResponse.id)
        val bookings = advertisementService.getBookings(profileResponse.id)?: listOf()
        val today = Calendar.getInstance(Locale.getDefault())
        today.set(Calendar.MILLISECOND, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        val todayBookings = bookings.filter { it.date == today }
        var adverts = 0
        var orders = 0
        if(seller!=null){
            adverts = advertisementService.getAdvertisementBySeller(seller).size
            orders = advertisementService.getBookingsBySeller(seller.sellerId)?.size ?: 0
        }
        return ResponseEntity.ok(UserDetailsResponse(profileResponse.id, profileResponse.photo, profileResponse.firstName, profileResponse.lastName, bookings.size, todayBookings.size, adverts, orders, profileResponse.roles))
    }

    @GetMapping("auth/sellerInfo")
    fun getSeller(@RequestParam("sellerId") sellerId: Long): ResponseEntity<*> {
        //val profileResponse = authService.getUser()
        //val user = authService.getUserDetails()
        val seller = advertisementService.getSellerById(sellerId)


        return if (seller != null) {
            val adverts = advertisementService.getAdvertisementBySeller(seller)
            var feedbackCount = 0
            var totalScore = 0
            adverts.forEach { advertisement ->
                val feedbacks = advertisementService.getFeedbackByAdvertisement(advertisement).toList()
                feedbacks.forEach {
                    totalScore += it.rating
                }
                feedbackCount += feedbacks.size
            }
            val averageRating = if(feedbackCount>0) totalScore/(feedbackCount.toFloat()) else 0F

            ResponseEntity.ok(SellerInfo(seller.sellerId, seller.user.photo, seller.user.firstName, seller.user.lastName, seller.about, adverts.size, feedbackCount, averageRating, seller.user.isVerified))
        } else {
            ResponseEntity(ResponseMessage("seller is not exist"), HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("editProfile")
    fun getEditProfile(): ResponseEntity<*> {
        val profileResponse = authService.getUserDetails()
        val seller = advertisementService.getSellerById(profileResponse.id)
        return ResponseEntity.ok(EditProfile(
                profileResponse.id,
                profileResponse.photo,
                profileResponse.firstName,
                profileResponse.lastName,
                seller?.about?:"",
                profileResponse.login,
                "",
                profileResponse.roles,
                profileResponse.vkId!=-1L
        ))
    }

    @PutMapping("profile")
    fun getProfile(@RequestBody editProfile: EditProfile): ResponseEntity<*> {
        val profileResponse = authService.getUserRecord()
        val registeredUser = authService.updateUser(profileResponse.id, RegisterUserDto(
                editProfile.login,
                editProfile.password,
                editProfile.firstName,
                editProfile.lastName,
                editProfile.photo,
                if (editProfile.roles.contains(Role.ADMIN)) "ADMIN" else if (editProfile.roles.contains(Role.SELLER)) "SELLER" else "USER",
                profileResponse.isVerified,
                profileResponse.vkId
        ))

        val newSeller = registeredUser?.let {
            advertisementService.updateSeller(it.id, Seller(it.id,
                    user = it,
                    about = editProfile.about
            ))
        }

        return if (registeredUser != null){
            ResponseEntity.ok(ProfileResponse(
                    registeredUser.id,
                    registeredUser.login,
                    registeredUser.firstName,
                    registeredUser.lastName,
                    registeredUser.roles
            ))
        } else {
            ResponseEntity(ResponseMessage("user is not exist"), HttpStatus.BAD_REQUEST)
        }
    }


    //@PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("seller/")
    fun postCategoryRequest(): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        val registeredUser = authService.updateUser(
                profile.id,
                RegisterUserDto(
                        profile.login,
                        profile.password,
                        profile.firstName,
                        profile.lastName,
                        profile.photo,
                        "SELLER",
                        profile.isVerified,
                        profile.vkId
                )
        )
        /*val registeredUser: User = authService.signup(RegisterUserDto(
                sellerRequest.login,
                sellerRequest.password,
                sellerRequest.firstName,
                sellerRequest.lastName,
                sellerRequest.photo,
                "SELLER"
        ))*/
        return if(registeredUser!=null){
            val newSeller = advertisementService.saveSeller(Seller(registeredUser.id,
                    user = registeredUser,
                    about = ""
            ))
            ResponseEntity.ok(newSeller)
        }else {
            ResponseEntity(ResponseMessage("user is not exist"), HttpStatus.BAD_REQUEST)
        }
    }


    //@PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("auth/seller/")
    fun postSellerRequest(@RequestBody sellerRequest: SellerRequest): ResponseEntity<*> {
        val registeredUser: User = authService.signup(RegisterUserDto(
                sellerRequest.login,
                sellerRequest.password,
                sellerRequest.firstName,
                sellerRequest.lastName,
                sellerRequest.photo,
                "SELLER"
        ))

        val newSeller = advertisementService.saveSeller(Seller(registeredUser.id,
                user = registeredUser,
                about = sellerRequest.about
        ))
        return ResponseEntity.ok(newSeller)

    }


    //@PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("seller/")
    fun updateCategoryRequest(@RequestBody sellerRequest: SellerRequest): ResponseEntity<*> {
        val registeredUser = authService.updateUser(sellerRequest.sellerId, RegisterUserDto(
                sellerRequest.login,
                sellerRequest.password,
                sellerRequest.firstName,
                sellerRequest.lastName,
                sellerRequest.photo,
                "SELLER"
        ))

        val newSeller = registeredUser?.let {
            advertisementService.updateSeller(it.id, Seller(it.id,
                user = it,
                about = sellerRequest.about
        ))
        }
        return ResponseEntity.ok(newSeller)

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
            ResponseEntity.ok(ResponsePoster("http://10.0.2.2:8080/api/auth/photo/$fileUUID"))
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
                            bookingRequest.isApproved
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
                            booking.isApproved
                    )
            )
        } else {
            ResponseEntity(ResponseMessage("advertisement or eventTimeList is not exist"), HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("confirmBooking/")
    fun postConfirmBookingRequest(@RequestParam("bookingId") bookingId: Long): ResponseEntity<*> {
        val profile = authService.getUserRecord()

        val booking = advertisementService.getBookingById(bookingId)

        return if (booking != null) {
            val newBooking = advertisementService.updateBooking(
                    bookingId,
                    Booking(
                            booking.id,
                            booking.eventTime,
                            booking.user,
                            booking.advertisement,
                            booking.bookingTime,
                            booking.date,
                            booking.totalPrice,
                            !booking.isApproved
                    )
            )

            if(newBooking!=null) {
                ResponseEntity.ok(BookingResponse(
                        newBooking.id,
                        newBooking.eventTime.timeId,
                        newBooking.user.id,
                        newBooking.advertisement.id,
                        newBooking.bookingTime.timeInMillis,
                        newBooking.date.timeInMillis,
                        newBooking.totalPrice,
                        newBooking.isApproved
                ))
            }else{
                ResponseEntity(ResponseMessage("booking is not exist"), HttpStatus.BAD_REQUEST)
            }
        } else {
            ResponseEntity(ResponseMessage("advertisement is not exist"), HttpStatus.BAD_REQUEST)
        }

    }


    @GetMapping("sellerBookings/")
    fun postBookingRequest(@RequestParam("advertId") advertId: Long, @RequestParam("date") date: Long?): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        val seller = advertisementService.getSellerById(profile.id)
        //val advertisement = seller?.let { advertisementService.getAdvertisementBySeller(it) }
        //val booking = advertisement?.map { advertisementService.getBookings(it) }
        val calendarDate = if (date != null) {
            val calendar = Calendar.getInstance(Locale.getDefault())
            calendar.timeInMillis = date
            calendar
        }else{
            null
        }
        val booking = seller?.let  {
            //advertisementService.getBookings(it.sellerId)
            if(advertId==-1L){
                advertisementService.getFilteredBookings(it.user.id, null, calendarDate)
            }else{
                advertisementService.getFilteredBookings(it.user.id, advertId, calendarDate)
            }
        }
        return if (booking != null) {
            val sellerBookingResponse = booking.map {
                val visitorsGroup = advertisementService.getGroupByBooking(it)
                var visitorsCount = 0
                visitorsGroup.forEach { group ->
                    visitorsCount += group.count
                }
                val date = it.date

                SellerBookingResponse(
                        it.id,
                        it.eventTime.time.timeInMillis,
                        it.advertisement.name,
                        it.date.timeInMillis,
                        it.totalPrice,
                        it.isApproved,
                        visitorsCount
                )
            }
            ResponseEntity.ok(
                    sellerBookingResponse
            )
        } else {
            ResponseEntity(ResponseMessage("booking or seller is not exist"), HttpStatus.BAD_REQUEST)
        }
    }


    @GetMapping("advertChips/")
    fun getAdvertChipsRequest(): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        val seller = advertisementService.getSellerById(profile.id)
        val advertisement = seller?.let { advertisementService.getAdvertisementBySeller(it) }
        return if (advertisement != null) {
            val advertisementResponse = advertisement.map {
                AdvertChip(it.name,it.id)
            }
            ResponseEntity.ok(
                    advertisementResponse
            )
        } else {
            ResponseEntity(ResponseMessage("advertisement is not exist"), HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("bookingInfo/")
    fun getBookingInfoRequest(@RequestParam("bookingId") bookingId: Long): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        val booking = advertisementService.getBookingById(bookingId)
        val visitorsGroup = booking?.let { advertisementService.getGroupByBooking(it) }
        return if (booking != null) {
            val bookingInfoResponse = BookingInfoResponse(
                    booking.id,
                    booking.advertisement.id,
                    booking.advertisement.name,
                    booking.eventTime.time.timeInMillis,
                    booking.date.timeInMillis,
                    booking.bookingTime.timeInMillis,
                    booking.isApproved,
                    booking.user.id,
                    booking.user.lastName+" "+booking.user.firstName,
                    booking.totalPrice,
                    visitorsGroup?.map {
                        VisitorsGroupResponse(
                                it.id,
                                it.customerCategory.customerCategoryId,
                                it.customerCategory.name,
                                it.count
                        )
                    }?: listOf()
            )
            ResponseEntity.ok(bookingInfoResponse)
        } else {
            ResponseEntity(ResponseMessage("booking or seller is not exist"), HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("sellerAdverts/")
    fun postAdvertsRequest(): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        val seller = advertisementService.getSellerById(profile.id)
        val advertisement = seller?.let { advertisementService.getAdvertisementBySeller(it) }

        return if (advertisement != null) {
            val sellerAdvertResponse = advertisement.map {

                val ticketPrice =  advertisementService.getTicketPriceByAdvert(it.id).toList().sortedBy { ticketPrice -> ticketPrice.price }

                SellerAdvertResponse(
                        it.id,
                        it.name,
                        ticketPrice.firstOrNull()?.price?:0,
                        it.visitorsCount,
                        it.status
                )
            }
            ResponseEntity.ok(
                    sellerAdvertResponse
            )
        } else {
            ResponseEntity(ResponseMessage("booking or seller is not exist"), HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("adminAdverts/")
    fun getAdminAdvertsRequest(): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        val seller = advertisementService.getSellerById(profile.id)
        val advertisement =  advertisementService.getAdvertisementByStatus("MODERATION")

        return if (advertisement != null) {
            val sellerAdvertResponse = advertisement.map {

                val ticketPrice =  advertisementService.getTicketPriceByAdvert(it.id).toList().sortedBy { ticketPrice -> ticketPrice.price }

                SellerAdvertResponse(
                        it.id,
                        it.name,
                        ticketPrice.firstOrNull()?.price?:0,
                        it.visitorsCount,
                        it.status
                )
            }
            ResponseEntity.ok(
                    sellerAdvertResponse
            )
        } else {
            ResponseEntity(ResponseMessage("booking or seller is not exist"), HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("admin/declineAdvertisement/")
    fun declineAdvertisement(@RequestParam("advertisementId") advertisementId: Long): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        val advertisement = advertisementService.getAdvertisementById(advertisementId)

        return if (advertisement != null) {
            val newAdvertisement = advertisementService.updateAdvertisement(advertisement.id,
                    Advertisement(
                            advertisement.id,
                            advertisement.name,
                            advertisement.duration,
                            advertisement.description,
                            advertisement.transportation,
                            advertisement.ageRestrictions,
                            advertisement.visitorsCount,
                            advertisement.isIndividual,
                            advertisement.photos,
                            advertisement.rating,
                            advertisement.category,
                            advertisement.city,
                            advertisement.seller,
                            "DECLINED"
                    )
            )
            ResponseEntity.ok(true)
        } else {
            ResponseEntity(ResponseMessage("advertisement is not exist"), HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("admin/confirmAdvertisement/")
    fun confirmAdvertisement(@RequestParam("advertisementId") advertisementId: Long): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        val advertisement = advertisementService.getAdvertisementById(advertisementId)

        return if (advertisement != null) {
            val newAdvertisement = advertisementService.updateAdvertisement(advertisement.id,
                    Advertisement(
                            advertisement.id,
                            advertisement.name,
                            advertisement.duration,
                            advertisement.description,
                            advertisement.transportation,
                            advertisement.ageRestrictions,
                            advertisement.visitorsCount,
                            advertisement.isIndividual,
                            advertisement.photos,
                            advertisement.rating,
                            advertisement.category,
                            advertisement.city,
                            advertisement.seller,
                            "ACCEPTED"
                    )
            )
            ResponseEntity.ok(true)
        } else {
            ResponseEntity(ResponseMessage("advertisement is not exist"), HttpStatus.BAD_REQUEST)
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
        val feedbacks = advertisement?.let { advertisementService.getFeedbackByAdvertisement(it) }?.toList()


        return if (advertisement != null) {
            val newFeedback = advertisement.let { advert->advertisementService.saveFeedback(
                    Feedback(FeedbackId(profile, advert), feedbackRequest.rating, feedbackRequest.text)
            ) }
            var newRating = newFeedback.rating
            feedbacks?.let {
                newRating = ((advertisement.rating * it.size) + newRating)/(it.size+1)
            }

            /*feedbacks?.forEach {
                newRating += it.rating
            }*/
            advertisementService.updateAdvertisement(
                    advertisement.id,
                    Advertisement(
                            advertisement.id,
                            advertisement.name,
                            advertisement.duration,
                            advertisement.description,
                            advertisement.transportation,
                            advertisement.ageRestrictions,
                            advertisement.visitorsCount,
                            advertisement.isIndividual,
                            advertisement.photos,
                            newRating,
                            advertisement.category,
                            advertisement.city,
                            advertisement.seller,
                            advertisement.status
                    )
            )
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