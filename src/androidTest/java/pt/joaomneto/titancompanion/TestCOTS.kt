package pt.joaomneto.titancompanion

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.runner.RunWith
import pt.joaomneto.titancompanion.consts.FightingFantasyGamebook.CRYPT_OF_THE_SORCERER

@LargeTest
@RunWith(AndroidJUnit4::class)
class TestCOTS : TestST() {

    override val gamebook = CRYPT_OF_THE_SORCERER
}
