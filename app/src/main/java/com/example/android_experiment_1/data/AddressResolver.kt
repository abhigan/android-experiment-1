package com.example.android_experiment_1.data

import android.location.Address

class AddressResolver {
    public fun resolve(addresses: List<Address>?): String {
        if (addresses.isNullOrEmpty()) return "-unknown-"

        val countryCodes = mutableSetOf<String>()
        val adminAreas = mutableSetOf<String>()
        val subAdminAreas = mutableSetOf<String>()
        val localitys = mutableSetOf<String>()
        val thoroughfares = mutableSetOf<String>()
        val postalCodes = mutableSetOf<String>()
        val featureNames = mutableSetOf<String>()
        val premisess = mutableSetOf<String>()

        val allOthers = listOf(
            countryCodes,
            adminAreas,
            subAdminAreas,
            localitys,
            thoroughfares,
            postalCodes,
            featureNames
        )

        addresses.forEach { address ->
            addCautious(address.countryCode, countryCodes, allOthers)
            addCautious(address.adminArea, adminAreas, allOthers)
            addCautious(address.subAdminArea, subAdminAreas, allOthers)
            addCautious(address.locality, localitys, allOthers)
            addCautious(address.thoroughfare, thoroughfares, allOthers)
            addCautious(address.postalCode, postalCodes, allOthers)
            addCautious(address.featureName, featureNames, allOthers)
            addCautious(address.premises, premisess, allOthers)
        }

        return listOf(premisess, thoroughfares, localitys, subAdminAreas, adminAreas, countryCodes)
            .flatMap { it.sorted() }
            .joinToString(", ")
    }

    private val except = listOf("Unnamed", "+", "Division")

    private fun addCautious(
        addWhat: String?,
        addTo: MutableSet<String>,
        allOthers: List<Set<String>>
    ) {
        if (addWhat == null || addWhat == "null") return

        val exists = allOthers.find { it.contains(addWhat) }
        if (exists != null) return

        val isExcept = except.find { addWhat.contains(it) }
        if(isExcept != null) return

        addTo.add(addWhat)
    }

    private fun except(vararg except: String): List<String> = except.toList()


}