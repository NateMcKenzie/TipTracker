package com.acrovolta.tiptracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File


class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
    }

    fun exportClicked(view: View){
        var file = File(File(applicationContext.filesDir,"shifts"), "shifts.csv")
        val intentShareFile = Intent(Intent.ACTION_SEND)
        var uri = FileProvider.getUriForFile(applicationContext, "com.acrovolta.tiptracker.fileprovider", file)
        intentShareFile.setType("text/csv")
        intentShareFile.putExtra(Intent.EXTRA_STREAM,uri);
        intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val chooserIntent = Intent.createChooser(intentShareFile, "Share to")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(chooserIntent)
    }

}
