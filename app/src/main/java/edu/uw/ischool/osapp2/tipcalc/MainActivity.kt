package edu.uw.ischool.osapp2.tipcalc

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.text.NumberFormat
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private lateinit var tipButton: Button
    private lateinit var amountInput: EditText

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tipButton = findViewById(R.id.button)
        tipButton.isEnabled = false
        amountInput = findViewById(R.id.editTextNumber)

        var current = ""

        amountInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Enable the tip button if the text is not empty
                tipButton.isEnabled = s.toString().isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                //format the currency so it is USD with a $
                if (s.toString() != current) {
                    val cleanString = s.toString().replace("[$,.]".toRegex(), "")
                    val parsed = cleanString.toDoubleOrNull() ?: 0.0
                    val formatted = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(parsed / 100)
                    current = formatted
                    amountInput.setText(formatted)
                    amountInput.setSelection(formatted.length)
                }
            }
        })

        tipButton.setOnClickListener{
            //turn from $ string to int in pennies
            var dollarAmountStr = amountInput.text.toString()
            var removedDollarSign = dollarAmountStr.substring(1)
            var amountPennies = (removedDollarSign.toDouble() * 100).toInt()

            //calculate tip
            val afterTip = amountPennies * 1.15

            //format tip
            val formattedAmount = String.format("Total: $%.2f", afterTip /100)
            toastTip(formattedAmount)
        }
    }

    private fun toastTip(tip: String){
        Toast.makeText(this, tip, Toast.LENGTH_LONG).show()
    }
}