package pt.joaomneto.titancompanion.phase2

import androidx.test.filters.LargeTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import pt.joaomneto.titancompanion.consts.FightingFantasyGamebook.TRIAL_OF_CHAMPIONS
import pt.joaomneto.titancompanion.phase1.TestST

@LargeTest
@RunWith(AndroidJUnit4::class)
class TestTOC : TestST() {

    override val gamebook = TRIAL_OF_CHAMPIONS
}
