package com.duka.phase3.data

/**
 * Phase 3 — Category starter templates.
 *
 * Mechanism, not final content: a lookup keyed by the shop's category. When an
 * owner registers (or opens an empty Products screen) we offer a quick-start:
 * accept -> inserts the pre-seeded products with placeholder pricing the owner
 * can edit, remove, or add to. Declining inserts nothing.
 *
 * HONESTY NOTES (kept with the data on purpose):
 * - Items use generic names ("White bread loaf"), not brands. Real branded-product
 *   photography is a licensing question and is deliberately NOT claimed here —
 *   the UI shows clean category-appropriate icons/placeholders only.
 * - Prices are plausible placeholder defaults in RWF for the owner to edit,
 *   never presented as market data.
 * - These lists are samples. They need validation with actual Rwandan shop
 *   owners before being presented as complete. Extend the maps; don't touch UI.
 */
object CategoryTemplates {

    /** One starter product. iconKey maps to the app's existing icon system. */
    data class StarterProduct(
        val name: String,
        val category: String,   // product category label used by the app
        val defaultPriceRwf: Double,
        val defaultStock: Int = 20,
        val iconKey: String = "generic"
    )

    /** Offer copy shown to the owner. */
    fun offerTitle(category: String): String = "Quick start — add ${starterCount(category)} common $category products?"

    fun offerBody(category: String): String =
        "We'll add a starter list of typical $category products with placeholder prices. " +
        "Edit, remove, or add to them anytime — nothing is final."

    fun starterCount(category: String): Int = templates[category]?.size ?: 0

    fun hasTemplate(category: String): Boolean = templates.containsKey(category)

    fun starterProducts(category: String): List<StarterProduct> =
        templates[category].orEmpty()

    /**
     * Seed data — representative starting sets (samples, not exhaustive).
     * Keyed by the shop category selected at registration.
     */
    private val templates: Map<String, List<StarterProduct>> = mapOf(
        "Supermarket" to listOf(
            StarterProduct("Rice (1kg)", "Groceries", 1200.0, iconKey = "grain"),
            StarterProduct("Sugar (1kg)", "Groceries", 1400.0, iconKey = "grain"),
            StarterProduct("Cooking oil (1L)", "Groceries", 3500.0, iconKey = "bottle"),
            StarterProduct("Salt (500g)", "Groceries", 300.0, iconKey = "grain"),
            StarterProduct("Flour (1kg)", "Groceries", 1100.0, iconKey = "grain"),
            StarterProduct("Bread", "Bakery", 800.0, iconKey = "bread"),
            StarterProduct("Milk (500ml)", "Dairy", 700.0, iconKey = "bottle"),
            StarterProduct("Eggs (tray of 30)", "Dairy", 4500.0, iconKey = "egg"),
            StarterProduct("Tea leaves (100g)", "Beverages", 900.0, iconKey = "packet"),
            StarterProduct("Coffee (100g)", "Beverages", 2500.0, iconKey = "packet"),
            StarterProduct("Soap (bar)", "Household", 500.0, iconKey = "packet"),
            StarterProduct("Laundry detergent (500g)", "Household", 1800.0, iconKey = "packet"),
            StarterProduct("Toothpaste", "Personal Care", 1500.0, iconKey = "tube"),
            StarterProduct("Toilet paper (roll)", "Household", 400.0, iconKey = "packet"),
            StarterProduct("Bottled water (1L)", "Beverages", 500.0, iconKey = "bottle"),
            StarterProduct("Soft drink (300ml)", "Beverages", 400.0, iconKey = "bottle"),
            StarterProduct("Beans (1kg)", "Groceries", 1300.0, iconKey = "grain"),
            StarterProduct("Maize flour (1kg)", "Groceries", 1000.0, iconKey = "grain"),
            StarterProduct("Tomatoes (1kg)", "Produce", 900.0, iconKey = "veg"),
            StarterProduct("Onions (1kg)", "Produce", 800.0, iconKey = "veg"),
            StarterProduct("Potatoes (1kg)", "Produce", 900.0, iconKey = "veg")
        ),
        "Bakery" to listOf(
            StarterProduct("White bread loaf", "Bakery", 800.0, iconKey = "bread"),
            StarterProduct("Brown bread loaf", "Bakery", 900.0, iconKey = "bread"),
            StarterProduct("Buns (pack of 4)", "Bakery", 600.0, iconKey = "bread"),
            StarterProduct("Doughnut", "Bakery", 200.0, iconKey = "sweet"),
            StarterProduct("Cake (slice)", "Bakery", 500.0, iconKey = "sweet"),
            StarterProduct("Biscuits (pack)", "Bakery", 700.0, iconKey = "packet"),
            StarterProduct("Croissant", "Bakery", 600.0, iconKey = "bread"),
            StarterProduct("Meat samosa", "Bakery", 300.0, iconKey = "savoury"),
            StarterProduct("Vegetable samosa", "Bakery", 250.0, iconKey = "savoury"),
            StarterProduct("Mandazi", "Bakery", 150.0, iconKey = "sweet")
        ),
        "Pharmacy" to listOf(
            StarterProduct("Paracetamol (strip)", "Pharmacy", 300.0, iconKey = "packet"),
            StarterProduct("Cough syrup (100ml)", "Pharmacy", 2500.0, iconKey = "bottle"),
            StarterProduct("Bandages (pack)", "Pharmacy", 800.0, iconKey = "packet"),
            StarterProduct("Antiseptic (100ml)", "Pharmacy", 1500.0, iconKey = "bottle"),
            StarterProduct("Vitamin C tablets", "Pharmacy", 2000.0, iconKey = "packet"),
            StarterProduct("Oral rehydration salts", "Pharmacy", 200.0, iconKey = "packet")
        ),
        "Grocery" to listOf(
            StarterProduct("Tomatoes (1kg)", "Produce", 900.0, iconKey = "veg"),
            StarterProduct("Onions (1kg)", "Produce", 800.0, iconKey = "veg"),
            StarterProduct("Bananas (bunch)", "Produce", 1000.0, iconKey = "veg"),
            StarterProduct("Avocados (each)", "Produce", 200.0, iconKey = "veg"),
            StarterProduct("Cabbage (head)", "Produce", 500.0, iconKey = "veg"),
            StarterProduct("Carrots (1kg)", "Produce", 700.0, iconKey = "veg"),
            StarterProduct("Passion fruits (1kg)", "Produce", 1500.0, iconKey = "veg"),
            StarterProduct("Garlic (250g)", "Produce", 900.0, iconKey = "veg")
        )
        // "Other" intentionally has no template — the offer only appears for known categories.
    )
}
