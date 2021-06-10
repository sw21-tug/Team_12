package com.team12.myopensecret

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*
import kotlin.collections.ArrayList

class DeleteTagActivity : AppCompatActivity() {


    private lateinit var labelsGroup: ChipGroup


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.delete_tag) {
            startSuccessfulIntent()
        }
        else if (item.itemId == R.id.cancel_deleting_tag) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(resources.getString(R.string.description_cancel))
            builder.setPositiveButton(R.string.agree) { dialog, which ->
                var intent = Intent()
                intent.putExtra("ADD_TAG", "test")
                setResult(0, intent)
                finish()
            }
            builder.setNegativeButton(R.string.disagree) { dialog, which ->
            }

            builder.show()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun startSuccessfulIntent() {
        val intent = Intent()

        var hasSelectedChip = false
        labelsGroup.checkedChipIds.forEach {
            hasSelectedChip = true
        }
        val selectedLabels = ArrayList<LabelData>()
        labelsGroup.checkedChipIds.forEach {
            selectedLabels.add(findViewById<Chip>(it).tag as LabelData)
        }

        setResult(1, intent)
        finish()
    }

}





