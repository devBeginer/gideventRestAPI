package ru.gidevent.RestAPI.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.gidevent.RestAPI.model.db.*
import ru.gidevent.RestAPI.repository.*
import ru.gidevent.RestAPI.model.dto.AddInfoSeller
import ru.gidevent.RestAPI.model.dto.TicketPriceDto
import ru.gidevent.RestAPI.model.response.AdvertisementMainInfo
import ru.gidevent.RestAPI.model.response.TopsResponse

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

    fun allAdvertisements(): Iterable<AdvertisementMainInfo>{
        val advertisementList = advertisementRepository.findAll()
        return advertisementList.map { advertisement ->
            val priceList = ticketPriceRepository.getTicketPriceByAdvert(advertisement.id)
            AdvertisementMainInfo(
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
                    AddInfoSeller(
                            advertisement.seller.sellerId,
                            advertisement.seller.firstName,
                            advertisement.seller.lastName,
                            advertisement.seller.photo
                    ),
                    priceList.sortedBy { it.price }
                            .map { price->
                                TicketPriceDto(
                                        price.priceId,
                                        price.advertisement.id,
                                        price.customerCategory.customerCategoryId,
                                        price.customerCategory.name,
                                        price.price)
                            }
            )
        }
    }

    fun topAdvertisements(): TopsResponse {
        val advertisementList = advertisementRepository.findAll()
        //val advertisementList = advertisementRepository.getAdvertWithExtra()
        val categoryList = categoryRepository.findAll()
        val info = TopsResponse(
                advertisementList.map { advertisement ->
                    val priceList = ticketPriceRepository.getTicketPriceByAdvert(advertisement.id)
                    AdvertisementMainInfo(
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
                            AddInfoSeller(
                                    advertisement.seller.sellerId,
                                    advertisement.seller.firstName,
                                    advertisement.seller.lastName,
                                    advertisement.seller.photo
                            ),
                            priceList.sortedBy { it.price }
                                    .map { price->
                                        TicketPriceDto(
                                                price.priceId,
                                                price.advertisement.id,
                                                price.customerCategory.customerCategoryId,
                                                price.customerCategory.name,
                                                price.price)
                                    }
                    )
                },
                categoryList.toList(),
                advertisementList.map { advertisement ->
                    val priceList = ticketPriceRepository.getTicketPriceByAdvert(advertisement.id)
                    AdvertisementMainInfo(
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
                            AddInfoSeller(
                                    advertisement.seller.sellerId,
                                    advertisement.seller.firstName,
                                    advertisement.seller.lastName,
                                    advertisement.seller.photo
                            ),
                            priceList.sortedBy { it.price }
                                    .map { price->
                                        TicketPriceDto(
                                                price.priceId,
                                                price.advertisement.id,
                                                price.customerCategory.customerCategoryId,
                                                price.customerCategory.name,
                                                price.price)
                                    }
                    )
                }
        )
        return info
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