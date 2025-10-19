package com.example.contactsapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.GridLayoutManager
import com.example.contactsapp.databinding.ActivityMainBinding
import com.example.contactsapp.databinding.BottomSheetBinding
import com.example.contactsapp.databinding.ItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.toString


class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    private lateinit var adapter: HomeAdapter
    val contacts: MutableList<ContactItem> = mutableListOf() // قائمة مهيأة

    private var startTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        startTime = SystemClock.uptimeMillis()

        splashScreen.setKeepOnScreenCondition {
            val elapsed = SystemClock.uptimeMillis() - startTime

            elapsed < 2000
        }

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // ✅ اعمل inflate للـ BottomSheet مع ViewBinding
        val bottomBinding = BottomSheetBinding.inflate(layoutInflater)

        // ✅ جهز الـ BottomSheetDialog وخليه ياخد الـ root من الـ binding
        val bottomSheetDialog = BottomSheetDialog(this, R.style.TransparentBottomSheetDialog)
        bottomSheetDialog.setContentView(bottomBinding.root)

        adapter = HomeAdapter(contacts)
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2) // عرض عنصرين في كل صف
        binding.recyclerView.adapter = adapter
        adapter.setContactList(contacts)


        // ✅ افتح الـ BottomSheet لما تدوس على زرار الـ fab
        binding.fab.setOnClickListener {
            bottomSheetDialog.show()
        }




        bottomBinding.EnButton.setOnClickListener {
            validate(bottomBinding, bottomSheetDialog)

        }


    }

    fun updateUi() {
        if (contacts.isEmpty()) {
            binding.lottieView.visibility = View.VISIBLE
        } else
            binding.lottieView.visibility = View.GONE

    }

    fun upDateUiBottomSheet(
        bottomBinding: BottomSheetBinding,
    ) {
        bottomBinding.edName.editText?.text?.clear()
        bottomBinding.edEmail.editText?.text?.clear()
        bottomBinding.edNumber.editText?.text?.clear()
        bottomBinding.edName.editText?.clearFocus()
        bottomBinding.edEmail.editText?.clearFocus()
        bottomBinding.edNumber.editText?.clearFocus()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun validate(
        bottomBinding: BottomSheetBinding,
        bottomSheetDialog: BottomSheetDialog
    ) {
        val name = bottomBinding.editName.text.toString().trim()
        val email = bottomBinding.edEmail.editText?.text.toString().trim()
        val number = bottomBinding.edNumber.editText?.text.toString().trim()

        var isValid = true


        // validate name
        if (name.isEmpty()) {
            bottomBinding.edName.error = "Name required"
            isValid = false
        } else if (name.length < 3) {
            bottomBinding.edName.error = "The name is less than three letters"
            isValid = false
        } else {
            bottomBinding.edName.error = null

        }


        // validate email

        if (email.isEmpty()) {
            bottomBinding.edEmail.error = "Email required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            bottomBinding.edEmail.error = "Invalid email"
            isValid = false
        } else {
            bottomBinding.edEmail.error = null

        }

        // validate number

        if (number.isEmpty()) {
            bottomBinding.edNumber.error = "Number required"
            isValid = false
        } else if (number.length < 11) {
            bottomBinding.edNumber.error = "Invalid number"
            isValid = false
        } else
            bottomBinding.edNumber.error = null

        if (isValid) {
            val contact = ContactItem(
                name = name,
                email = email,
                phone = number
            )
            adapter.addNewItem(contact)
            adapter.notifyDataSetChanged()
            updateUi()
            upDateUiBottomSheet(bottomBinding)
            Toast.makeText(this, "Data entered successfully ✅", Toast.LENGTH_SHORT).show()
            bottomSheetDialog.dismiss()

        }
    }


}


