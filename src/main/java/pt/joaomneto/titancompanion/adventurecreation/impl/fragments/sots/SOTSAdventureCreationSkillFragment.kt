package pt.joaomneto.titancompanion.adventurecreation.impl.fragments.sots

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import java.util.ArrayList
import java.util.HashMap
import pt.joaomneto.titancompanion.R
import pt.joaomneto.titancompanion.adventurecreation.AdventureCreation
import pt.joaomneto.titancompanion.adventurecreation.impl.SOTSAdventureCreation

class SOTSAdventureCreationSkillFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_20sots_adventure_creation_skill, container, false)

        val skillList = ArrayList<Map<String, String>>()

        val stringArray = ArrayList<String>()
        for (art in SOTSMartialArt.values()) {
            stringArray.add(resources.getString(art.nameId))
        }

        for (string in stringArray) {
            val skill = HashMap<String, String>()
            skill["skill"] = string
            skillList.add(skill)
        }

        val mAdapter = SimpleAdapter(
            activity,
            skillList,
            R.layout.potions_item,
            arrayOf("skill"),
            intArrayOf(R.id.potion_name)
        )

        val lView = rootView.findViewById<ListView>(R.id.skillList)
        lView.adapter = mAdapter
        lView.setSelector(R.drawable.row_selector)

        lView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            (activity as SOTSAdventureCreation).setSkill(
                getArtFromString(stringArray[position])!!
            )
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.buttonSaveAdventure).setOnClickListener { v: View -> (this.activity as AdventureCreation).saveAdventure() }
    }

    internal fun getArtFromString(nameString: String): SOTSMartialArt? {
        for (art in SOTSMartialArt.values()) {
            if (nameString == resources.getString(art.nameId))
                return art
        }
        return null
    }
}
