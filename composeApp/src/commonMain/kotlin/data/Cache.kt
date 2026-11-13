package data

/** Simple in-memory cache of raw response bodies, keyed by request URL. Nothing is persisted. */
class Cache {
    private val map = mutableMapOf<String, String>()

    fun get(key: String): String? = map[key]

    fun put(key: String, value: String) {
        map[key] = value
    }
}
