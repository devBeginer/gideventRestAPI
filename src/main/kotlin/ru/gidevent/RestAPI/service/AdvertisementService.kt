package ru.gidevent.RestAPI.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.gidevent.RestAPI.model.db.*
import ru.gidevent.RestAPI.repository.*
import ru.gidevent.RestAPI.model.dto.AddInfoSeller
import ru.gidevent.RestAPI.model.dto.AdvertisementWithFavourite
import ru.gidevent.RestAPI.model.dto.CitySuggestion
import ru.gidevent.RestAPI.model.dto.TicketPriceDto
import ru.gidevent.RestAPI.model.request.SearchOptions
import ru.gidevent.RestAPI.model.response.AdvertisementMainInfo
import ru.gidevent.RestAPI.model.response.Suggestions
import ru.gidevent.RestAPI.model.response.TicketPriceResponse
import ru.gidevent.RestAPI.model.response.TopsResponse
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
            if (count>7) count+7 else count
        } else {
            null
        }

        val weekDays = mutableListOf<String?>(null, null, null, null, null, null, null)

        if(dayCount!=null) {
            for (i in 1 until dayCount.toInt()) {
                when (i) {
                    1 -> {weekDays[0]="Monday "}
                    2 -> {weekDays[1]="Tuesday"}
                    3 -> {weekDays[2]="Wednesday"}
                    4 -> {weekDays[3]="Thursday"}
                    5 -> {weekDays[4]="Friday "}
                    6 -> {weekDays[5]="Saturday"}
                    7 -> {weekDays[6]="Sunday "}
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
            if (count>7) count+7 else count
        } else {
            null
        }

        val weekDays = mutableListOf<String?>(null, null, null, null, null, null, null)

        if(dayCount!=null) {
            for (i in 1 until dayCount.toInt()) {
                when (i) {
                    1 -> {weekDays[0]="Monday "}
                    2 -> {weekDays[1]="Tuesday"}
                    3 -> {weekDays[2]="Wednesday"}
                    4 -> {weekDays[3]="Thursday"}
                    5 -> {weekDays[4]="Friday "}
                    6 -> {weekDays[5]="Saturday"}
                    7 -> {weekDays[6]="Sunday "}
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

    fun saveEventTime(eventTime: EventTime): EventTime {
        return eventTimeRepository.save(eventTime)
    }

    fun updateEventTime(id: Long, eventTime: EventTime): EventTime?{
        return if(eventTimeRepository.existsById(id)){
            eventTimeRepository.save(eventTime)
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
}