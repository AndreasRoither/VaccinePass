import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import java.util.*

// taken from:
// https://stackoverflow.com/a/19897981/9641046
class EditTextDatePicker(context: Context, editTextViewID: Int) :
    View.OnClickListener, OnDateSetListener {

    var _editText: EditText
    private var _day = 0
    private var _month = 0
    private var _birthYear = 0
    private val _context: Context

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _birthYear = year
        _month = monthOfYear
        _day = dayOfMonth
        updateDisplay()
    }

    override fun onClick(v: View?) {
        val calendar: Calendar = Calendar.getInstance(TimeZone.getDefault())
        val dialog = DatePickerDialog(
            _context, this,
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.show()
    }

    // updates the date in the birth date EditText
    private fun updateDisplay() {
        _editText.setText(
            StringBuilder() // Month is 0 based so add 1
                .append(_day).append(".").append(_month + 1).append(".").append(_birthYear)
        )
    }

    init {
        val act = context as Activity
        _editText = act.findViewById<View>(editTextViewID) as EditText
        _editText.setOnClickListener(this)
        _context = context
    }
}