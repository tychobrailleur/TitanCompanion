package pt.joaomneto.titancompanion.phase1

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.runner.RunWith
import pt.joaomneto.titancompanion.consts.FightingFantasyGamebook.SLAVES_OF_THE_ABYSS
import pt.joaomneto.titancompanion.phase1.TestST

@LargeTest
@RunWith(AndroidJUnit4::class)
class TestSOTA : TestST() {

    override val gamebook = SLAVES_OF_THE_ABYSS
}
