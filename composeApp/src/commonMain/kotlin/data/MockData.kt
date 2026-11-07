package data

import kotlinx.coroutines.delay
import model.Crate
import model.CrateDetail
import model.CrateVersion
import model.Owner

/**
 * Hand-written sample data shaped exactly like the crates.io API fields, so the UI does not
 * change when the repository switches to the network. Versions, download counts and owners
 * are illustrative, not live values.
 */
object MockData {

    val crates: List<Crate> = listOf(
        Crate(
            name = "serde", version = "1.0.228", downloads = 850_000_000,
            description = "A generic serialization/deserialization framework",
            repository = "https://github.com/serde-rs/serde", documentation = "https://docs.rs/serde",
            homepage = "https://serde.rs",
            dependencies = listOf("serde_derive", "serde_core"), owners = listOf("dtolnay"),
        ),
        Crate(
            name = "tokio", version = "1.47.1", downloads = 520_000_000,
            description = "An event-driven, non-blocking I/O platform for writing asynchronous applications",
            repository = "https://github.com/tokio-rs/tokio", documentation = "https://docs.rs/tokio",
            homepage = "https://tokio.rs",
            dependencies = listOf("bytes", "mio", "parking_lot", "tracing"), owners = listOf("carllerche", "Darksonn"),
        ),
        Crate(
            name = "reqwest", version = "0.12.23", downloads = 280_000_000,
            description = "Higher level HTTP client library",
            repository = "https://github.com/seanmonstar/reqwest", documentation = "https://docs.rs/reqwest",
            homepage = "https://github.com/seanmonstar/reqwest",
            dependencies = listOf("hyper", "tokio", "serde", "serde_json", "url", "base64"), owners = listOf("seanmonstar"),
        ),
        Crate(
            name = "clap", version = "4.5.48", downloads = 450_000_000,
            description = "A simple to use, efficient, and full-featured command line argument parser",
            repository = "https://github.com/clap-rs/clap", documentation = "https://docs.rs/clap",
            homepage = "https://github.com/clap-rs/clap",
            dependencies = listOf("anstyle", "strsim", "clap_builder"), owners = listOf("epage"),
        ),
        Crate(
            name = "anyhow", version = "1.0.100", downloads = 480_000_000,
            description = "Flexible concrete Error type built on std::error::Error",
            repository = "https://github.com/dtolnay/anyhow", documentation = "https://docs.rs/anyhow",
            homepage = "https://github.com/dtolnay/anyhow",
            dependencies = listOf("backtrace"), owners = listOf("dtolnay"),
        ),
        Crate(
            name = "thiserror", version = "2.0.17", downloads = 560_000_000,
            description = "derive(Error) for custom error types",
            repository = "https://github.com/dtolnay/thiserror", documentation = "https://docs.rs/thiserror",
            homepage = "https://github.com/dtolnay/thiserror",
            dependencies = listOf("thiserror-impl"), owners = listOf("dtolnay"),
        ),
        Crate(
            name = "rayon", version = "1.11.0", downloads = 330_000_000,
            description = "Simple work-stealing parallelism for Rust",
            repository = "https://github.com/rayon-rs/rayon", documentation = "https://docs.rs/rayon",
            homepage = "https://github.com/rayon-rs/rayon",
            dependencies = listOf("rayon-core", "either"), owners = listOf("cuviper"),
        ),
        Crate(
            name = "serde_json", version = "1.0.145", downloads = 620_000_000,
            description = "A JSON file format implementation for Rust",
            repository = "https://github.com/serde-rs/json", documentation = "https://docs.rs/serde_json",
            homepage = "https://github.com/serde-rs/json",
            dependencies = listOf("serde", "itoa", "ryu", "memchr"), owners = listOf("dtolnay"),
        ),
        Crate(
            name = "hyper", version = "1.7.0", downloads = 400_000_000,
            description = "A protective and efficient HTTP library for all",
            repository = "https://github.com/hyperium/hyper", documentation = "https://docs.rs/hyper",
            homepage = "https://hyper.rs",
            dependencies = listOf("tokio", "futures", "http", "bytes"), owners = listOf("seanmonstar"),
        ),
        Crate(
            name = "axum", version = "0.8.6", downloads = 130_000_000,
            description = "Web framework that focuses on ergonomics and modularity",
            repository = "https://github.com/tokio-rs/axum", documentation = "https://docs.rs/axum",
            homepage = "https://github.com/tokio-rs/axum",
            dependencies = listOf("hyper", "tokio", "tracing", "serde", "serde_json"), owners = listOf("davidpdrsn"),
        ),
        Crate(
            name = "tracing", version = "0.1.41", downloads = 360_000_000,
            description = "Application-level tracing for Rust",
            repository = "https://github.com/tokio-rs/tracing", documentation = "https://docs.rs/tracing",
            homepage = "https://tokio.rs",
            dependencies = listOf("log", "once_cell", "pin-project-lite"), owners = listOf("hawkw"),
        ),
        Crate(
            name = "log", version = "0.4.28", downloads = 700_000_000,
            description = "A lightweight logging facade for Rust",
            repository = "https://github.com/rust-lang/log", documentation = "https://docs.rs/log",
            homepage = "https://github.com/rust-lang/log",
            dependencies = listOf("serde", "value-bag"), owners = listOf("alexcrichton"),
        ),
        Crate(
            name = "rand", version = "0.9.2", downloads = 600_000_000,
            description = "Random number generators and other randomness functionality",
            repository = "https://github.com/rust-random/rand", documentation = "https://docs.rs/rand",
            homepage = "https://rust-random.github.io/book",
            dependencies = listOf("rand_core", "rand_chacha", "serde"), owners = listOf("dhardy"),
        ),
        Crate(
            name = "regex", version = "1.11.3", downloads = 650_000_000,
            description = "An implementation of regular expressions for Rust with linear time matching",
            repository = "https://github.com/rust-lang/regex", documentation = "https://docs.rs/regex",
            homepage = "https://github.com/rust-lang/regex",
            dependencies = listOf("regex-syntax", "regex-automata", "memchr", "aho-corasick"), owners = listOf("BurntSushi"),
        ),
        Crate(
            name = "chrono", version = "0.4.42", downloads = 380_000_000,
            description = "Date and time library for Rust",
            repository = "https://github.com/chronotope/chrono", documentation = "https://docs.rs/chrono",
            homepage = "https://github.com/chronotope/chrono",
            dependencies = listOf("num-traits", "serde", "iana-time-zone"), owners = listOf("djc"),
        ),
        Crate(
            name = "uuid", version = "1.18.1", downloads = 340_000_000,
            description = "A library to generate and parse UUIDs",
            repository = "https://github.com/uuid-rs/uuid", documentation = "https://docs.rs/uuid",
            homepage = "https://github.com/uuid-rs/uuid",
            dependencies = listOf("rand", "serde", "getrandom"), owners = listOf("KodrAus"),
        ),
        Crate(
            name = "url", version = "2.5.7", downloads = 420_000_000,
            description = "URL library for Rust, based on the WHATWG URL Standard",
            repository = "https://github.com/servo/rust-url", documentation = "https://docs.rs/url",
            homepage = "https://github.com/servo/rust-url",
            dependencies = listOf("percent-encoding", "idna", "serde"), owners = listOf("SimonSapin"),
        ),
        Crate(
            name = "base64", version = "0.22.1", downloads = 500_000_000,
            description = "Encodes and decodes base64 as bytes or utf8",
            repository = "https://github.com/marshallpierce/rust-base64", documentation = "https://docs.rs/base64",
            homepage = "https://github.com/marshallpierce/rust-base64",
            dependencies = listOf("alloc"), owners = listOf("marshallpierce"),
        ),
        Crate(
            name = "futures", version = "0.3.31", downloads = 460_000_000,
            description = "An implementation of futures and streams featuring zero allocations and composability",
            repository = "https://github.com/rust-lang/futures-rs", documentation = "https://docs.rs/futures",
            homepage = "https://rust-lang.github.io/futures-rs",
            dependencies = listOf("futures-core", "futures-util", "futures-channel"), owners = listOf("taiki-e"),
        ),
        Crate(
            name = "once_cell", version = "1.21.3", downloads = 640_000_000,
            description = "Single assignment cells and lazy values",
            repository = "https://github.com/matklad/once_cell", documentation = "https://docs.rs/once_cell",
            homepage = "https://github.com/matklad/once_cell",
            dependencies = listOf("critical-section", "portable-atomic"), owners = listOf("matklad"),
        ),
        Crate(
            name = "parking_lot", version = "0.12.5", downloads = 410_000_000,
            description = "More compact and efficient implementations of the standard synchronization primitives",
            repository = "https://github.com/Amanieu/parking_lot", documentation = "https://docs.rs/parking_lot",
            homepage = "https://github.com/Amanieu/parking_lot",
            dependencies = listOf("lock_api", "parking_lot_core"), owners = listOf("Amanieu"),
        ),
        Crate(
            name = "itertools", version = "0.14.0", downloads = 430_000_000,
            description = "Extra iterator adaptors, iterator methods, free functions, and macros",
            repository = "https://github.com/rust-itertools/itertools", documentation = "https://docs.rs/itertools",
            homepage = "https://github.com/rust-itertools/itertools",
            dependencies = listOf("either"), owners = listOf("bluss", "jswrenn"),
        ),
        Crate(
            name = "syn", version = "2.0.106", downloads = 900_000_000,
            description = "Parser for Rust source code",
            repository = "https://github.com/dtolnay/syn", documentation = "https://docs.rs/syn",
            homepage = "https://github.com/dtolnay/syn",
            dependencies = listOf("proc-macro2", "quote", "unicode-ident"), owners = listOf("dtolnay"),
        ),
        Crate(
            name = "bytes", version = "1.10.1", downloads = 580_000_000,
            description = "Types and traits for working with bytes",
            repository = "https://github.com/tokio-rs/bytes", documentation = "https://docs.rs/bytes",
            homepage = "https://github.com/tokio-rs/bytes",
            dependencies = listOf("serde"), owners = listOf("carllerche", "Darksonn"),
        ),
    )

    private val ownerNames: Map<String, String?> = mapOf(
        "dtolnay" to "David Tolnay",
        "carllerche" to "Carl Lerche",
        "Darksonn" to "Alice Ryhl",
        "seanmonstar" to "Sean McArthur",
        "BurntSushi" to "Andrew Gallant",
        "matklad" to "Aleksey Kladov",
        "Amanieu" to "Amanieu d'Antras",
        "cuviper" to "Josh Stone",
    )

    val owners: List<Owner> = crates.flatMap { it.owners }.distinct().map { login ->
        Owner(login = login, name = ownerNames[login], avatar = "https://github.com/$login.png")
    }
}

/** [CratesDataSource] backed by [MockData]; mimics API latency and 404s. */
class MockCratesSource(private val latencyMs: Long = 250) : CratesDataSource {

    override suspend fun search(query: String, page: Int, perPage: Int): List<Crate> {
        delay(latencyMs)
        val q = query.trim()
        val matches = MockData.crates.filter {
            q.isEmpty() || it.name.contains(q, ignoreCase = true) ||
                it.description.orEmpty().contains(q, ignoreCase = true)
        }
        val size = perPage.coerceIn(1, MAX_PER_PAGE)
        return matches.drop((page.coerceAtLeast(1) - 1) * size).take(size)
    }

    override suspend fun getCrate(name: String): CrateDetail {
        delay(latencyMs)
        val crate = find(name)
        return CrateDetail(
            crate = crate,
            versions = fakeVersions(crate),
            owners = ownersOf(crate),
            dependencies = crate.dependencies,
        )
    }

    override suspend fun getDependencies(name: String): List<String> {
        delay(latencyMs)
        return find(name).dependencies
    }

    override suspend fun getOwners(name: String): List<Owner> {
        delay(latencyMs)
        return ownersOf(find(name))
    }

    override suspend fun getUser(login: String): Owner {
        delay(latencyMs)
        return MockData.owners.firstOrNull { it.login == login } ?: throw ApiError.NotFound(login)
    }

    private fun find(name: String): Crate =
        MockData.crates.firstOrNull { it.name == name } ?: throw ApiError.NotFound(name)

    private fun ownersOf(crate: Crate): List<Owner> =
        crate.owners.map { login -> MockData.owners.firstOrNull { it.login == login } ?: Owner(login) }

    /** Derives a short version history from max_version ("1.2.3" -> 1.2.3, 1.2.2, 1.1.0). */
    private fun fakeVersions(crate: Crate): List<CrateVersion> {
        val parts = crate.version.split('.').mapNotNull { it.toIntOrNull() }
        if (parts.size < 3) return listOf(CrateVersion(crate.version, crate.downloads, "2025-01-01T00:00:00Z"))
        val (major, minor, patch) = parts
        return listOf(
            CrateVersion("$major.$minor.$patch", crate.downloads / 10, "2025-09-01T00:00:00Z"),
            CrateVersion("$major.$minor.${(patch - 1).coerceAtLeast(0)}", crate.downloads / 8, "2025-07-15T00:00:00Z"),
            CrateVersion("$major.${(minor - 1).coerceAtLeast(0)}.0", crate.downloads / 5, "2025-03-01T00:00:00Z"),
        )
    }
}
