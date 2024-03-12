package ru.gidevent.RestAPI.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.gidevent.RestAPI.auth.AuthenticationService
import ru.gidevent.RestAPI.model.request.SearchOptions
import ru.gidevent.RestAPI.model.response.OptionsVariants
import ru.gidevent.RestAPI.service.AdvertisementService


@RestController
@RequestMapping("/api/")
class SearchController {
    @Autowired
    lateinit var advertisementService: AdvertisementService
    @Autowired
    lateinit var authService: AuthenticationService


    @GetMapping("auth/search/")
    fun advertisement(): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.allAdvertisements())
    }


    @GetMapping("search/query")
    fun searchByQueryPrivate(@RequestParam query: String): ResponseEntity<*> {
        val profile = authService.getUser()
        return ResponseEntity.ok(advertisementService.getAdvertisementByName(profile.id, query))
    }

    @GetMapping("auth/search/query")
    fun searchByQuery(@RequestParam query: String): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.getAdvertisementByName(query))
    }

    @PostMapping("auth/search/params")
    fun searchByQuery(@RequestBody searchOptions: SearchOptions): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.getAdvertisementsByParams(searchOptions))
    }

    @PostMapping("search/params")
    fun searchByQueryPrivate(@RequestBody searchOptions: SearchOptions): ResponseEntity<*> {
        val profile = authService.getUser()
        return ResponseEntity.ok(advertisementService.getAdvertisementsByParams(profile.id, searchOptions))
    }


    @GetMapping("auth/search/suggestion")
    fun suggestionByQuery(@RequestParam query: String): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.getSuggestionByName(query))
    }


    @GetMapping("auth/search/suggestion/city")
    fun suggestionCityByQuery(@RequestParam query: String): ResponseEntity<*> {
        return ResponseEntity.ok(advertisementService.getCitySuggestionByName(query))
    }


    @GetMapping("auth/search/optionsVariants")
    fun optionVariants(): ResponseEntity<*> {
        val transport = advertisementService.allTransportationVariant()
        val categories = advertisementService.allCategory()
        return ResponseEntity.ok(OptionsVariants(transport.toList(), categories.toList()))
    }
}