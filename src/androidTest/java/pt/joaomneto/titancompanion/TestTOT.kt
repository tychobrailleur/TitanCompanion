package pt.joaomneto.titancompanion

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.runner.RunWith
import pt.joaomneto.titancompanion.consts.FightingFantasyGamebook.TEMPLE_OF_TERROR

@LargeTest
@RunWith(AndroidJUnit4::class)
class TestTOT : TestST() {

    override val gamebook = TEMPLE_OF_TERROR
}
