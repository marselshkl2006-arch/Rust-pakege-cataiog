import data.MockData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MockDataTest {
    @Test
    fun hasAtLeastTwentyCrates() {
        assertTrue(MockData.crates.size >= 20, "expected 20+ crates, got ${MockData.crates.size}")
    }

    @Test
    fun containsTheRequiredRealCrates() {
        val required = listOf(
            "serde", "tokio", "reqwest", "clap", "anyhow", "thiserror", "rayon", "serde_json", "hyper",
            "axum", "tracing", "log", "rand", "regex", "chrono", "uuid", "url", "base64", "futures",
            "once_cell", "parking_lot", "itertools",
        )
        val names = MockData.crates.map { it.name }.toSet()
        assertTrue(names.containsAll(required), "missing: ${required - names}")
    }

    @Test
    fun allFieldsAreFilled() {
        MockData.crates.forEach { c ->
            assertTrue(c.name.isNotBlank(), "name")
            assertTrue(c.version.isNotBlank(), "${c.name}: version")
            assertFalse(c.description.isNullOrBlank(), "${c.name}: description")
            assertTrue(c.downloads > 0, "${c.name}: downloads")
            assertFalse(c.repository.isNullOrBlank(), "${c.name}: repository")
            assertFalse(c.documentation.isNullOrBlank(), "${c.name}: documentation")
            assertFalse(c.homepage.isNullOrBlank(), "${c.name}: homepage")
            assertTrue(c.dependencies.isNotEmpty(), "${c.name}: dependencies")
            assertTrue(c.owners.isNotEmpty(), "${c.name}: owners")
        }
    }

    @Test
    fun namesAreUniqueAndEveryOwnerIsKnown() {
        assertEquals(MockData.crates.size, MockData.crates.map { it.name }.toSet().size)
        val logins = MockData.owners.map { it.login }.toSet()
        MockData.crates.flatMap { it.owners }.forEach { assertTrue(it in logins, "unknown owner $it") }
    }
}
