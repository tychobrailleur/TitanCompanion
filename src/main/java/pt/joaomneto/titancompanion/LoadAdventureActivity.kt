package pt.joaomneto.titancompanion

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import pt.joaomneto.titancompanion.adapter.Savegame
import pt.joaomneto.titancompanion.adapter.SavegameListAdapter
import pt.joaomneto.titancompanion.consts.Constants
import pt.joaomneto.titancompanion.consts.FightingFantasyGamebook
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

class LoadAdventureActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_adventure)
        val listview = findViewById<ListView>(R.id.adventureListView)

        val baseDir = File(filesDir, "ffgbutil")

        var fileList: Array<String>? = baseDir.list()

        if (fileList == null)
            fileList = arrayOf()

        val files = ArrayList<Savegame>()

        for (string in fileList) {
            if (string.startsWith("save")) {
                val f = File(baseDir, string)
                files.add(Savegame(string, Date(f.lastModified())))
            }
        }

        val adapter = SavegameListAdapter(this, files)
        listview.adapter = adapter

        listview.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val dir = File(baseDir, files[position].filename)

            val f = File(dir, "temp.xml")
            if (f.exists())
                f.delete()

            val initialSavePoints = dir
                .listFiles { _, filename -> filename.startsWith("initial") }
                ?: emptyArray()

            val numericSavePoints = dir
                .listFiles { _, filename ->
                    !filename.startsWith("initial") && !filename.startsWith("exception")
                }
                ?: emptyArray()

            val savepointFiles = numericSavePoints
                .sortedBy { it.lastModified() }
                .reversed()
                .plus(
                    initialSavePoints.sortedBy { it.lastModified() }
                )

            val names = savepointFiles
                .map { it.name.dropLast(4) }
                .toTypedArray()

            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.chooseSavePoint)
            builder.setItems(names) { _, which ->
                var gamebook: FightingFantasyGamebook? = null

                try {
                    val bufferedReader = BufferedReader(
                        FileReader(savepointFiles[which])
                    )

                    while (bufferedReader.ready()) {
                        val line = bufferedReader.readLine()
                        if (line.startsWith("gamebook=")) {
                            val gbs =
                                line.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                            val numbericGbs = gbs.toIntOrNull()
                            gamebook = if (numbericGbs != null)
                                FightingFantasyGamebook.values()[numbericGbs]
                            else
                                FightingFantasyGamebook.valueOf(gbs)
                            break
                        }
                    }

                    bufferedReader.close()

                    val intent = Intent(
                        this,
                        Constants
                            .getRunActivity(this, gamebook!!)
                    )

                    intent.putExtra(
                        ADVENTURE_SAVEGAME_CONTENT,
                        File(File(File(filesDir, "ffgbutil"), dir.name), savepointFiles[which].name).readText()
                    )
                    intent.putExtra(
                        ADVENTURE_NAME,
                        dir.name
                    )
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            val alert = builder.create()
            alert.show()
        }

        listview.onItemLongClickListener = OnItemLongClickListener { _, _, position, _ ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.deleteSavegame)
                .setCancelable(false)
                .setNegativeButton(
                    R.string.close
                ) { dialog, _ -> dialog.cancel() }
            builder.setPositiveButton(
                R.string.ok
            ) { _, _ ->
                val file = files[position].filename
                val f = File(baseDir, file)
                if (deleteDirectory(f)) {
                    files.removeAt(position)
                    (
                        listview
                            .adapter as ArrayAdapter<String>
                        )
                        .notifyDataSetChanged()
                }
            }

            val alert = builder.create()
            alert.show()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.gamebook_list, menu)
        return true
    }

    companion object {

        val ADVENTURE_SAVEGAME_CONTENT = "ADVENTURE_SAVEGAME_CONTENT"
        val ADVENTURE_NAME = "ADVENTURE_NAME"

        val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm")

        fun deleteDirectory(path: File): Boolean {
            if (path.exists()) {
                val files = path.listFiles()
                for (i in files.indices) {
                    if (files[i].isDirectory) {
                        deleteDirectory(files[i])
                    } else {
                        files[i].delete()
                    }
                }
            }
            return path.delete()
        }
    }
}
