package com.quetta.touristan.model

import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.gson.annotations.SerializedName

data class Places(
    @SerializedName("next_page_token")
    val nextPageToken: String? = null,
    val results: List<TourPlace> = listOf()
)

data class TourPlace(
    @SerializedName("formatted_address")
    var address: String? = null,
    var name: String? = null,
    @SerializedName("place_id")
    var placeId: String? = null,
    var types: List<String> = listOf(),
    var rating: Float = 0F,
    val photos: List<PlacePhoto> = listOf()
)

data class PlacePhoto(
    val height: Int = 0,
    val width: Int = 0,
    @SerializedName("html_attributions")
    val attribution: List<String> = listOf(),
    @SerializedName("photo_reference")
    val reference: String? = null,
)

//{
//    "business_status": "OPERATIONAL",
//    "formatted_address": "Railway Housing Society Quetta, Balochistan, Pakistan",
//    "geometry": {
//        "location": {
//            "lat": 30.1833998,
//            "lng": 66.99531619999999
//        },
//        "viewport": {
//            "northeast": {
//            "lat": 30.18480992989272,
//            "lng": 66.99672642989272
//        },
//            "southwest": {
//            "lat": 30.18211027010728,
//            "lng": 66.99402677010727
//        }
//    }
//},
//    "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/generic_business-71.png",
//    "name": "Joint Road Square",
//
//    "photos": [
//    {
//        "height": 817,
//        "html_attributions": [
//        "<a href=\"https://maps.google.com/maps/contrib/117249022329383091601\">Jawad Khan</a>"
//        ],
//        "photo_reference": "ATtYBwL8n1NWZezPDY-RXekX2le_kcVMtXq54Uil1W6GmXKPc2zV3MvW7s_YIf1NDhxVYrwgwr4UQC1kW1Z5Z7PaWEvi4vcNG8_B_-jdq3pxEeebAS2z8lZFEdm4bZ8_Uou0pD46Krv-MtmM9hMYur-o30BzEIDaYB9m41WCpciNZz3PB3Se",
//        "width": 1080
//    }
//    ],
//    "place_id": "ChIJpwi5N3Hh0j4RCDx5Tw-Qu0U",
//    "plus_code": {
//    "compound_code": "5XMW+94 Quetta",
//    "global_code": "8J285XMW+94"
//},
//    "rating": 0,
//    "reference": "ChIJpwi5N3Hh0j4RCDx5Tw-Qu0U",
//    "types": [
//    "tourist_attraction",
//    "point_of_interest",
//    "establishment"
//    ],
//    "user_ratings_total": 0
//}
