package com.limbergdv.vivia_mobile.features.properties.remote.navigation

object MyPropertiesRoutes {
    const val MY_PROPERTIES_GRAPH = "my_properties_graph"
    const val MY_PROPERTIES_LIST  = "my_properties_list"
    const val PROPERTY_DETAIL     = "property_detail/{propertyId}"
    
    fun createPropertyDetailRoute(propertyId: String): String {
        return "property_detail/$propertyId"
    }
}
