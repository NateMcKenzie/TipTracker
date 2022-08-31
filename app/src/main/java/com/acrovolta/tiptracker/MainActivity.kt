package com.acrovolta.tiptracker

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    lateinit var outputFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var filename = "shifts.csv"
        val shiftDirectory = File(applicationContext.filesDir, "shifts")
        shiftDirectory.mkdirs()
        outputFile = File(shiftDirectory, filename)
        textTips.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (textTips.text.isEmpty())
                    return

                val hours = textHours.text.toString()
                val tips = textTips.text.toString().toFloat()
                val mileage = textMile.text.toString()
                val runs = textRuns.text.toString()

                if (mileage != "") {
                    if (hours != "") {
                        tipPerHour.text = "Tips/hr:" + tips / hours.toFloat()
                        moneyPerHour.text =
                            "Total money/hr:" + (tips + mileage.toFloat()) / hours.toFloat()
                    }
                    if (runs != "") {
                        tipsPerRun.text = "Tips/Run:" + tips / runs.toFloat()
                        moneyPerRun.text =
                            "Total money/Run:" + (tips + mileage.toFloat()) / runs.toFloat()
                    }
                    if (textTotal.text.isEmpty())
                        textTotal.setText((tips + mileage.toFloat()).toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        textMile.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (textMile.text.isEmpty())
                    return

                val hours = textHours.text.toString()
                val tips = textTips.text.toString()
                val mileage = textMile.text.toString().toFloat()
                val runs = textRuns.text.toString()

                if (tips != "") {
                    var total = mileage + tips.toFloat()
                    if (hours != "")
                        moneyPerHour.text = "Total money/hr:" + total / hours.toFloat()
                    if (runs != "")
                        moneyPerRun.text = "Total money/Run:" + total / runs.toFloat()
                    if (textTotal.text.isEmpty())
                        textTotal.setText(total.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        textHours.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (textHours.text.isEmpty())
                    return

                val hours = textHours.text.toString().toFloat()
                val tips = textTips.text.toString()
                val mileage = textMile.text.toString()
                val runs = textRuns.text.toString()

                if (tips != "") {
                    tipPerHour.text = "Tips/hr:" + tips.toFloat() / hours
                    if (mileage != "")
                        moneyPerHour.text =
                            "Total money/hr:" + (tips.toFloat() + mileage.toFloat()) / hours
                }
                if (runs != "")
                    runsPerHour.text = "Runs/hr:" + runs.toFloat() / hours
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        textRuns.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (textRuns.text.isEmpty())
                    return

                val hours = textHours.text.toString()
                val tips = textTips.text.toString()
                val mileage = textMile.text.toString()
                val runs = textRuns.text.toString().toFloat()

                if (tips != "") {
                    tipsPerRun.text = "Tips/Run:" + tips.toFloat() / runs
                    if (mileage != "")
                        moneyPerRun.text =
                            "Total money/Run:" + (tips.toFloat() + mileage.toFloat()) / runs
                }
                if (hours != "")
                    runsPerHour.text = "Runs/hr:" + runs / hours.toFloat()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        textTotal.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (textMile.text.isNotEmpty() && textTotal.text.isNotEmpty()) {
                    textTips.setText((textTotal.text.toString().toFloat() - textMile.text.toString().toFloat()).toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    fun saveClicked(view: View) {
        Log.d("exists", outputFile.exists().toString())
        if (!outputFile.exists()) {
            Log.d("Creating", "outputfile")
            outputFile.outputStream().use {
                it.bufferedWriter().use {
                    it.write("Date,Tips,Mileage,Hours,Runs,Stiffs,Morning,Afternoon,Evening,Close\n")
                }
            }
        }

        var contents =
            String.format("%02d/%02d/%04d", datePick.month + 1, datePick.dayOfMonth, datePick.year)
        contents += "," + textTips.text.toString() + "," + textMile.text.toString() + "," + textHours.text.toString() + "," + textRuns.text.toString() + "," + textStiffs.text.toString()
        contents += "," + checkBoxMorn.isChecked.toString() + "," + checkBoxAfter.isChecked.toString() + "," + checkBoxEve.isChecked.toString() + "," + checkBoxClose.isChecked.toString()

        outputFile.appendText(contents + "\n")
        Toast.makeText(applicationContext, "Shift saved", Toast.LENGTH_LONG).show()
    }

    fun historyClicked(view: View) {
        startActivity(Intent(this, HistoryActivity::class.java))
    }

    private fun loadData() {
        if (outputFile.exists()) {
            val selectedDate = String.format(
                "%02d/%02d/%04d",
                datePick.month + 1,
                datePick.dayOfMonth,
                datePick.year
            )
            var existingData = ""
            outputFile.forEachLine {
                if (it.substring(0, 10) == selectedDate) {
                    existingData = it;return@forEachLine
                }
            }
            if (existingData != "") {
                val data = existingData.split(',')
                textTips.setText(data[1])
                textMile.setText(data[2])
                textHours.setText(data[3])
                textRuns.setText(data[4])
                textStiffs.setText(data[5])
                checkBoxMorn.isChecked = data[6].toBoolean()
                checkBoxAfter.isChecked = data[7].toBoolean()
                checkBoxEve.isChecked = data[8].toBoolean()
                checkBoxClose.isChecked = data[9].toBoolean()
            }
        }
    }

    fun loadClicked(view: View) {
        Toast.makeText(applicationContext, "click", Toast.LENGTH_LONG)
        loadData()
    }

}
