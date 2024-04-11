package ru.gidevent.RestAPI.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.gidevent.RestAPI.auth.User
import ru.gidevent.RestAPI.model.db.*
import ru.gidevent.RestAPI.repository.*
import ru.gidevent.RestAPI.model.dto.AddInfoSeller
import ru.gidevent.RestAPI.model.dto.AdvertisementWithFavourite
import ru.gidevent.RestAPI.model.dto.CitySuggestion
import ru.gidevent.RestAPI.model.dto.TicketPriceDto
import ru.gidevent.RestAPI.model.request.SearchOptions
import ru.gidevent.RestAPI.model.response.*
import java.util.*

@Service
class AdvertisementService {

    @Autowired
    lateinit var advertisementRepository: AdvertisementRepository

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Autowired
    lateinit var cityRepository: CityRepository

    @Autowired
    lateinit var customerCategoryRepository: CustomerCategoryRepository

    @Autowired
    lateinit var eventTimeRepository: EventTimeRepository

    @Autowired
    lateinit var feedbackRepository: FeedbackRepository

    @Autowired
    lateinit var sellerRepository: SellerRepository

    @Autowired
    lateinit var ticketPriceRepository: TicketPriceRepository

    @Autowired
    lateinit var transportationVariantRepository: TransportationVariantRepository

    @Autowired
    lateinit var favouriteRepository: FavouriteRepository

    @Autowired
    lateinit var groupRepository: GroupRepository

    @Autowired
    lateinit var bookingRepository: BookingRepository

    fun getAdvertisementByName(id: Long, query: String): Iterable<AdvertisementMainInfo> {
        val advertisementList = advertisementRepository.getAdvertWithExtraByName(id, query)

        val groupedList = advertisementList.groupBy { it.advertisement }

        return groupedList.map { advertisement->
            parseAdvertMainInfo(advertisement)
        }
    }

    fun getAdvertisementByName(query: String): Iterable<AdvertisementMainInfo> {
        val advertisementList = advertisementRepository.getAdvertWithExtraByName(query)

        val groupedList = advertisementList.groupBy { it.advertisement }

        return groupedList.map { advertisement->
            parseAdvertMainInfo(advertisement)
        }
    }

    fun getCitySuggestionByName(query: String): Iterable<CitySuggestion> {
        val cityList = cityRepository.getCitySuggestion(query)

        return cityList
    }

    fun getSuggestionByName(query: String): Suggestions{
        val advertisementList = advertisementRepository.getAdvertSuggestion(query)
        val cityList = cityRepository.getCitySuggestion(query)
        val categoryList = categoryRepository.getCategorySuggestion(query)



        return Suggestions(
                advertisementList.toList(),
                cityList.toList(),
                categoryList.toList()
        )
    }

    fun getAdvertisementsByParams(searchOptions: SearchOptions): Iterable<AdvertisementMainInfo>{
        val  dateFrom = searchOptions.dateFrom?.let {
            val date = Calendar.getInstance(Locale.getDefault())
            date.timeInMillis = it
            date
        }
        val  dateTo = searchOptions.dateTo?.let {
            val date = Calendar.getInstance(Locale.getDefault())
            date.timeInMillis = it
            date
        }
        val dayCount = if(dateTo!=null && dateFrom!=null){
            val count = (dateTo.timeInMillis-dateFrom.timeInMillis)/(24 * 60 * 60 * 1000) + 1
            if (count>7) 7 else count
        } else {
            null
        }

        val weekDays = mutableListOf<String?>(null, null, null, null, null, null, null)

        if(dayCount!=null && dateTo!=null && dateFrom!=null) {
            val start = dateFrom.get(Calendar.DAY_OF_WEEK)
            val end = start + dayCount.toInt()
            for (i in start until end) {
                when (i%7) {
                    0 -> {weekDays[5]="Saturday"}
                    1 -> {weekDays[6]="Sunday"}
                    2 -> {weekDays[0]="Monday"}
                    3 -> {weekDays[1]="Tuesday"}
                    4 -> {weekDays[2]="Wednesday"}
                    5 -> {weekDays[3]="Thursday"}
                    6 -> {weekDays[4]="Friday"}
                }
            }
        }

        val isIndividual = if (!searchOptions.isGroup && searchOptions.isIndividual) {
            true
        } else if (searchOptions.isGroup && !searchOptions.isIndividual) {
            false
        } else {
            null
        }


        val advertisementList = advertisementRepository
                .getAdvertWithExtraByParams(
                        weekDays[0],
                        weekDays[1],
                        weekDays[2],
                        weekDays[3],
                        weekDays[4],
                        weekDays[5],
                        weekDays[6],
                        dateFrom,
                        dateTo,
                        searchOptions.ageRestriction,
                        isIndividual,
                        searchOptions.ratingFrom,
                        searchOptions.ratingTo,
                        searchOptions.durationFrom,
                        searchOptions.durationTo,
                        searchOptions.transport,
                        searchOptions.categories,
                        searchOptions.city,
                        searchOptions.priceFrom,
                        searchOptions.priceTo
                )




        val groupedList = advertisementList.groupBy { it.advertisement }

        return groupedList.map { advertisement->
            parseAdvertMainInfo(advertisement)
        }
    }

    fun getAdvertisementsByParams(profileId: Long, searchOptions: SearchOptions): Iterable<AdvertisementMainInfo>{
        val  dateFrom = searchOptions.dateFrom?.let {
            val date = Calendar.getInstance(Locale.getDefault())
            date.timeInMillis = it
            date
        }
        val  dateTo = searchOptions.dateTo?.let {
            val date = Calendar.getInstance(Locale.getDefault())
            date.timeInMillis = it
            date
        }
        val dayCount = if(dateTo!=null && dateFrom!=null){
            val count = (dateTo.timeInMillis-dateFrom.timeInMillis)/(24 * 60 * 60 * 1000) + 1
            if (count>7) 7 else count
        } else {
            null
        }

        val weekDays = mutableListOf<String?>(null, null, null, null, null, null, null)

        if(dayCount!=null && dateTo!=null && dateFrom!=null) {
            val start = dateFrom.get(Calendar.DAY_OF_WEEK)
            val end = start + dayCount.toInt()
            for (i in start until end) {
                when (i%7) {
                    0 -> {weekDays[5]="Saturday"}
                    1 -> {weekDays[6]="Sunday"}
                    2 -> {weekDays[0]="Monday"}
                    3 -> {weekDays[1]="Tuesday"}
                    4 -> {weekDays[2]="Wednesday"}
                    5 -> {weekDays[3]="Thursday"}
                    6 -> {weekDays[4]="Friday"}
                }
            }
        }

        val isIndividual = if (!searchOptions.isGroup && searchOptions.isIndividual) {
            true
        } else if (searchOptions.isGroup && !searchOptions.isIndividual) {
            false
        } else {
            null
        }


        val advertisementList = advertisementRepository
                .getAdvertWithExtraByParams(
                        profileId,
                        weekDays[0],
                        weekDays[1],
                        weekDays[2],
                        weekDays[3],
                        weekDays[4],
                        weekDays[5],
                        weekDays[6],
                        dateFrom,
                        dateTo,
                        searchOptions.ageRestriction,
                        isIndividual,
                        searchOptions.ratingFrom,
                        searchOptions.ratingTo,
                        searchOptions.durationFrom,
                        searchOptions.durationTo,
                        searchOptions.transport,
                        searchOptions.categories,
                        searchOptions.city,
                        searchOptions.priceFrom,
                        searchOptions.priceTo
                )




        val groupedList = advertisementList.groupBy { it.advertisement }

        return groupedList.map { advertisement->
            parseAdvertMainInfo(advertisement)
        }
    }

    fun allAdvertisements(): Iterable<AdvertisementMainInfo>{
        val advertisementList = advertisementRepository.getAdvertWithExtra(/*id*/)
        val groupedList = advertisementList.groupBy { it.advertisement }


        return groupedList.map { advertisement->
            parseAdvertMainInfo(advertisement)
        }
    }

    fun favouriteAdvertisements(id: Long): Iterable<AdvertisementMainInfo>{
        val advertisementList = favouriteRepository.getAdvertWithExtra(id)

        val groupedList = advertisementList.groupBy { it.advertisement }

        return groupedList.map { advertisement->
            parseAdvertMainInfo(advertisement)
        }
    }

    fun reservedAdvertisements(id: Long): Iterable<AdvertisementMainInfo>{
        val advertisementList = bookingRepository.getAdvertWithExtra(id)

        val groupedList = advertisementList.groupBy { it.advertisement }

        return groupedList.map { advertisement->
            parseAdvertMainInfo(advertisement)
        }
    }

    fun allAdvertisements(id: Long): Iterable<AdvertisementMainInfo>{
        val advertisementList = advertisementRepository.getAdvertWithExtra(id)

        val groupedList = advertisementList.groupBy { it.advertisement }

        return groupedList.map { advertisement->
            parseAdvertMainInfo(advertisement)
        }
    }

    fun topAdvertisements(): TopsResponse {
        val advertisementList = advertisementRepository.getAdvertWithExtra()
        val groupedList = advertisementList.groupBy { it.advertisement }
        val categoryList = categoryRepository.findAll()
        val info = TopsResponse(
                groupedList.map { advertisement ->
                    parseAdvertMainInfo(advertisement)
                },
                categoryList.toList(),
                groupedList.map { advertisement ->
                    parseAdvertMainInfo(advertisement)
                }
        )
        return info
    }

    fun topAdvertisements(id: Long): TopsResponse {
        val advertisementList = advertisementRepository.getAdvertWithExtra(id)
        val groupedList = advertisementList.groupBy { it.advertisement }
        val categoryList = categoryRepository.findAll()
        val info = TopsResponse(
                groupedList.map { advertisement ->
                    parseAdvertMainInfo(advertisement)
                },
                categoryList.toList(),
                groupedList.map { advertisement ->
                    parseAdvertMainInfo(advertisement)
                }
        )
        return info
    }

    private fun parseAdvertMainInfo(advertisement: Map.Entry<AdvertisementWithFavourite, List<TicketPriceDto>>): AdvertisementMainInfo{
        return AdvertisementMainInfo(
                advertisement.key.id,
                advertisement.key.name,
                advertisement.key.duration,
                advertisement.key.description,
                advertisement.key.transportation,
                advertisement.key.ageRestrictions,
                advertisement.key.visitorsCount,
                advertisement.key.isIndividual,
                advertisement.key.photos,
                advertisement.key.rating,
                advertisement.key.category,
                advertisement.key.city,
                advertisement.key.favourite!=null,
                AddInfoSeller(
                        advertisement.key.seller.sellerId,
                        advertisement.key.seller.firstName,
                        advertisement.key.seller.lastName,
                        advertisement.key.seller.photo
                ),
                advertisement.value.map { ticketPriceDto ->
                    TicketPriceResponse(
                            ticketPriceDto.priceId,
                            ticketPriceDto.customerCategory.customerCategoryId,
                            ticketPriceDto.customerCategory.name,
                            ticketPriceDto.price
                    )
                }
        )
    }


    fun getAdvertisementById(id: Long): Advertisement?{
        return advertisementRepository.findByIdOrNull(id)
    }

    fun getAdvertisementById(id: Long, advertId: Long): AdvertisementMainInfo?{
        val advertisementList = advertisementRepository.getAdvertWithExtraById(id, advertId)

        val groupedList = advertisementList.groupBy { it.advertisement }

        return parseAdvertMainInfo(groupedList.entries.first())

    }

    fun getAdvertisementBySeller(seller: Seller): List<Advertisement>{
        val advertisementList = advertisementRepository.findBySeller(seller)

        return advertisementList.toList()

    }

    fun getExpandedAdvertisementById(id: Long): AdvertisementExpanded?{
        val advertisement = advertisementRepository.findByIdOrNull(id)
        val ticketPrice = advertisement?.let { ticketPriceRepository.getTicketPriceByAdvert(it.id) }
        val eventTime = advertisement?.let { eventTimeRepository.findByAdvertisement(it) }
        val feedback = advertisement?.let { feedbackRepository.findByFeedbackIdAdvertisement(it) }
        return advertisement?.let {
            AdvertisementExpanded(
                    it.id,
                    it.name,
                    it.duration,
                    it.description,
                    it.transportation,
                    it.ageRestrictions,
                    it.visitorsCount,
                    it.isIndividual,
                    it.photos,
                    it.rating.toFloat(),
                    it.category,
                    it.city,
                    null,
                    it.seller,
                    ticketPrice?.map { ticketPriceDto ->
                        TicketPriceResponse(
                                ticketPriceDto.priceId,
                                ticketPriceDto.customerCategory.customerCategoryId,
                                ticketPriceDto.customerCategory.name,
                                ticketPriceDto.price
                        )
                    },
                    feedback?.map { feedback ->
                                  FeedbackResponse(
                                          "${feedback.feedbackId.user.lastName} ${feedback.feedbackId.user.firstName}",
                                          "",//feedback.feedbackId.userId.photo,
                                          feedback.rating,
                                          feedback.text
                                  )
                    },
                    eventTime?.map{eventTime ->
                        EventTimeResponse(
                                eventTime.timeId,
                                eventTime.time.timeInMillis,
                                eventTime.isRepeatable,
                                eventTime.daysOfWeek,
                                eventTime.startDate.timeInMillis,
                                eventTime.endDate.timeInMillis
                        )
                    }
            )
        }
    }
    fun getExpandedAdvertisementById(id: Long, user: User): AdvertisementExpanded?{
        val advertisement = advertisementRepository.findByIdOrNull(id)
        //val favourite = advertisement?.let { favouriteRepository.findByFavouriteIdAdvertisementIdFavouriteIdUserId(it, user) }
        val favourite = advertisement?.let { favouriteRepository.findByIdOrNull(FavouriteId(user, it)) }
        val ticketPrice = advertisement?.let { ticketPriceRepository.getTicketPriceByAdvert(it.id) }
        val eventTime = advertisement?.let { eventTimeRepository.findByAdvertisement(it) }
        val feedback = advertisement?.let { feedbackRepository.findByFeedbackIdAdvertisement(it) }
        return advertisement?.let {
            AdvertisementExpanded(
                it.id,
                it.name,
                it.duration,
                it.description,
                it.transportation,
                it.ageRestrictions,
                it.visitorsCount,
                it.isIndividual,
                it.photos,
                it.rating.toFloat(),
                it.category,
                it.city,
                favourite!=null,
                it.seller,
                ticketPrice?.map { ticketPriceDto ->
                    TicketPriceResponse(
                            ticketPriceDto.priceId,
                            ticketPriceDto.customerCategory.customerCategoryId,
                            ticketPriceDto.customerCategory.name,
                            ticketPriceDto.price
                    )
                },
                feedback?.map { feedback ->
                    FeedbackResponse(
                            "${feedback.feedbackId.user.lastName} ${feedback.feedbackId.user.firstName}",
                            "",//feedback.feedbackId.userId.photo,
                            feedback.rating,
                            feedback.text
                    )
                },
                eventTime?.map{eventTime ->
                    EventTimeResponse(
                            eventTime.timeId,
                            eventTime.time.timeInMillis,
                            eventTime.isRepeatable,
                            eventTime.daysOfWeek,
                            eventTime.startDate.timeInMillis,
                            eventTime.endDate.timeInMillis
                    )
                }
            )
        }
    }

    fun saveAdvertisement(advertisement: Advertisement): Advertisement {
        return advertisementRepository.save(advertisement)
    }

    fun updateAdvertisement(id: Long, advertisement: Advertisement): Advertisement?{
        return if(advertisementRepository.existsById(id)){
            advertisementRepository.save(advertisement)
        }else{
            null
        }
    }

    fun allCategory(): Iterable<Category>{
        return categoryRepository.findAll()
    }

    fun getCategoryById(id: Long): Category?{
        return categoryRepository.findByIdOrNull(id)
    }

    fun saveCategory(category: Category): Category {
        return categoryRepository.save(category)
    }

    fun updateCategory(id: Long, category: Category): Category?{
        return if(categoryRepository.existsById(id)){
            categoryRepository.save(category)
        }else{
            null
        }
    }

    fun allCity(): Iterable<City>{
        return cityRepository.findAll()
    }

    fun getCityById(id: Long): City?{
        return cityRepository.findByIdOrNull(id)
    }

    fun saveCity(city: City): City {
        return cityRepository.save(city)
    }

    fun updateCity(id: Long, city: City): City?{
        return if(cityRepository.existsById(id)){
            cityRepository.save(city)
        }else{
            null
        }
    }

    fun allCustomerCategory(): Iterable<CustomerCategory>{
        return customerCategoryRepository.findAll()
    }

    fun getCustomerCategoryById(id: Long): CustomerCategory?{
        return customerCategoryRepository.findByIdOrNull(id)
    }

    fun saveCustomerCategory(customerCategory: CustomerCategory): CustomerCategory {
        return customerCategoryRepository.save(customerCategory)
    }

    fun updateCustomerCategory(id: Long, customerCategory: CustomerCategory): CustomerCategory?{
        return if(customerCategoryRepository.existsById(id)){
            customerCategoryRepository.save(customerCategory)
        }else{
            null
        }
    }

    fun allEventTime(): Iterable<EventTime>{
        return eventTimeRepository.findAll()
    }

    fun getEventTimeById(id: Long): EventTime?{
        return eventTimeRepository.findByIdOrNull(id)
    }

    fun getEventTimeByAdvertisement(id: Long): Iterable<EventTimeResponse>?{
        val advertisement = advertisementRepository.findByIdOrNull(id)
        val eventTime = advertisement?.let { eventTimeRepository.findByAdvertisement(it) }
        return eventTime?.map { eventTime ->
            EventTimeResponse(
                    eventTime.timeId,
                    eventTime.time.timeInMillis,
                    eventTime.isRepeatable,
                    eventTime.daysOfWeek,
                    eventTime.startDate.timeInMillis,
                    eventTime.endDate.timeInMillis
            )
        }
    }

    fun getEmptyEventTimeByAdvertisement(id: Long, date: Calendar): List<EventTimeWithCountResponse>?{
        val advertisement = advertisementRepository.findByIdOrNull(id)
        val weekDate = date.get(Calendar.DAY_OF_WEEK)
        val stringWeekDay = when(weekDate){
            1 -> "Sunday"
            2 -> "Monday"
            3 -> "Tuesday"
            4 -> "Wednesday"
            5 -> "Thursday"
            6 -> "Friday"
            7 -> "Saturday"
            else -> ""
        }
        val bookings = advertisement?.let { groupRepository.getByAdvertAndDate(it, date) }
                ?.groupBy { it.booking.eventTime }
        val busyTime = bookings?.entries?.filter {
            var totalCount = 0
            it.value.forEach { group->totalCount += group.count }
            totalCount >= it.key.advertisement.visitorsCount
        }?.map { it.key }

        val emptyCount = bookings?.entries?.filter {
            var totalCount = 0
            it.value.forEach { group->totalCount += group.count }
            totalCount <= it.key.advertisement.visitorsCount
        }?.map {
            var totalCount = 0
            it.value.forEach { group->totalCount += group.count }
            Pair(it.key, it.key.advertisement.visitorsCount - totalCount)
        }
        val eventTime = advertisement?.let { eventTimeRepository.findByAdvertisement(it) }
                ?.toList()
                ?.filter {
                    it.startDate == date
                            || it.endDate == date
                            || (date.before(it.endDate) && date.after(it.startDate) && it.daysOfWeek.contains(stringWeekDay))
                }
                ?.filter {
                    if(busyTime!=null){
                        !busyTime.contains(it)
                    }else{
                        true
                    }
                }
        return eventTime?.map { eventTime ->
            val count = emptyCount?.find { it.first.timeId == eventTime.timeId }?.second ?: eventTime.advertisement.visitorsCount
            EventTimeWithCountResponse(
                    eventTime.timeId,
                    eventTime.time.timeInMillis,
                    eventTime.isRepeatable,
                    eventTime.daysOfWeek,
                    eventTime.startDate.timeInMillis,
                    eventTime.endDate.timeInMillis,
                    count
            )
        }
    }

    fun getBookings(advertisement: Advertisement): List<Booking>?{
        val booking = bookingRepository.findByAdvertisement(advertisement)
        return booking.toList()
    }

    fun getBookings(id: Long): List<Booking>?{
        val booking = bookingRepository.getBySeller(id)
        return booking.toList()
    }

    fun getFilteredBookings(id: Long, advert: Long?, date: Calendar?): List<Booking>?{
        val booking = bookingRepository.getBySellerAndAdvert(id, advert, date)
        return booking.toList()
    }

    fun saveEventTime(eventTime: EventTime): EventTimeResponse {
        val savedEventTime = eventTimeRepository.save(eventTime)
        return EventTimeResponse(
                savedEventTime.timeId,
                savedEventTime.time.timeInMillis,
                savedEventTime.isRepeatable,
                savedEventTime.daysOfWeek,
                savedEventTime.startDate.timeInMillis,
                savedEventTime.endDate.timeInMillis
        )
    }

    fun updateEventTime(id: Long, eventTime: EventTime): EventTimeResponse?{
        return if(eventTimeRepository.existsById(id)){
            val savedEventTime = eventTimeRepository.save(eventTime)
            EventTimeResponse(
                    savedEventTime.timeId,
                    savedEventTime.time.timeInMillis,
                    savedEventTime.isRepeatable,
                    savedEventTime.daysOfWeek,
                    savedEventTime.startDate.timeInMillis,
                    savedEventTime.endDate.timeInMillis
            )
        }else{
            null
        }
    }

    fun allFeedback(): Iterable<Feedback>{
        return feedbackRepository.findAll()
    }

    fun getFeedbackById(id: FeedbackId): Feedback?{
        return feedbackRepository.findByIdOrNull(id)
    }

    fun saveFeedback(feedback: Feedback): Feedback {
        return feedbackRepository.save(feedback)
    }

    fun updateFeedback(id: FeedbackId, feedback: Feedback): Feedback?{
        return if(feedbackRepository.existsById(id)){
            feedbackRepository.save(feedback)
        }else{
            null
        }
    }

    fun getFavourite(favouriteId: FavouriteId): Favourite? {
        return favouriteRepository.findByIdOrNull(favouriteId)
    }

    fun saveFavourite(favourite: Favourite): Favourite {
        return favouriteRepository.save(favourite)
    }

    fun deleteFavourite(favourite: Favourite) {
        return favouriteRepository.delete(favourite)
    }

    fun allSeller(): Iterable<Seller>{
        return sellerRepository.findAll()
    }

    fun getSellerById(id: Long): Seller?{
        return sellerRepository.findByIdOrNull(id)
    }

    fun saveSeller(seller: Seller): Seller {
        return sellerRepository.save(seller)
    }

    fun updateSeller(id: Long, seller: Seller): Seller?{
        return if(sellerRepository.existsById(id)){
            sellerRepository.save(seller)
        }else{
            null
        }
    }

    fun allTicketPrice(): Iterable<TicketPrice>{
        return ticketPriceRepository.findAll()
    }

    fun getTicketPriceByAdvert(id: Long): Iterable<TicketPrice>{
        return ticketPriceRepository.getTicketPriceByAdvert(advertId = id)
    }

    fun getTicketPriceById(id: Long): TicketPrice?{
        return ticketPriceRepository.findByIdOrNull(id)
    }

    fun getTicketPriceByAdvertisement(id: Long): Iterable<TicketPrice>? {
        val advertisement = advertisementRepository.findByIdOrNull(id)
        return advertisement?.let { ticketPriceRepository.getTicketPriceByAdvert(it.id) }
    }

    fun saveTicketPrice(ticketPrice: TicketPrice): TicketPrice {
        return ticketPriceRepository.save(ticketPrice)
    }

    fun updateTicketPrice(id: Long, ticketPrice: TicketPrice): TicketPrice?{
        return if(ticketPriceRepository.existsById(id)){
            ticketPriceRepository.save(ticketPrice)
        }else{
            null
        }
    }

    fun allTransportationVariant(): Iterable<TransportationVariant>{
        return transportationVariantRepository.findAll()
    }

    fun getTransportationVariantById(id: Long): TransportationVariant?{
        return transportationVariantRepository.findByIdOrNull(id)
    }

    fun saveTransportationVariant(transportationVariant: TransportationVariant): TransportationVariant {
        return transportationVariantRepository.save(transportationVariant)
    }

    fun updateTransportationVariant(id: Long, transportationVariant: TransportationVariant): TransportationVariant?{
        return if(transportationVariantRepository.existsById(id)){
            transportationVariantRepository.save(transportationVariant)
        }else{
            null
        }
    }



    fun saveBooking(booking: Booking): Booking {
        return bookingRepository.save(booking)
    }

    fun updateBooking(id: Long, booking: Booking): Booking?{
        return if(bookingRepository.existsById(id)){
            bookingRepository.save(booking)
        }else{
            null
        }
    }

    fun getBookingById(id: Long): Booking?{
        return bookingRepository.findByIdOrNull(id)
    }

    fun getBookingByAdvertisement(advertisement: Advertisement): Iterable<Booking>{
        return bookingRepository.findByAdvertisement(advertisement)
    }



    fun saveGroup(visitorsGroup: VisitorsGroup): VisitorsGroup {
        return groupRepository.save(visitorsGroup)
    }

    fun updateGroup(id: Long, visitorsGroup: VisitorsGroup): VisitorsGroup?{
        return if(bookingRepository.existsById(id)){
            groupRepository.save(visitorsGroup)
        }else{
            null
        }
    }

    fun getGroupById(id: Long): VisitorsGroup?{
        return groupRepository.findByIdOrNull(id)
    }

    fun getGroupByBooking(booking: Booking): Iterable<VisitorsGroup>{
        return groupRepository.findByBooking(booking)
    }
}