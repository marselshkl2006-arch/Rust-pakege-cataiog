import data.MockData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import ui.list.ListState

class ListStateTest {
    private fun stateWithMocks() = ListState().also { it.crates = MockData.crates }

    @Test
    fun blankQueryKeepsEverything() {
        val state = stateWithMocks()
        state.query = "   "
        assertEquals(MockData.crates.size, state.filtered.size)
    }

    @Test
    fun filtersByNameCaseInsensitively() {
        val state = stateWithMocks()
        state.query = "SERDE"
        assertEquals(setOf("serde", "serde_json"), state.filtered.map { it.name }.toSet())
    }

    @Test
    fun filtersByDescription() {
        val state = stateWithMocks()
        state.query = "work-stealing"
        assertEquals(listOf("rayon"), state.filtered.map { it.name })
    }

    @Test
    fun noMatchGivesEmptyList() {
        val state = stateWithMocks()
        state.query = "definitely-not-a-crate"
        assertTrue(state.filtered.isEmpty())
    }
}
