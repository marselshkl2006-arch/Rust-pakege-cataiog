package data

class Cache {
    private val map = mutableMapOf<String, String>()

    fun get(key: String): String? = map[key]

    fun put(key: String, value: String) {
        map[key] = value
    }
}
