package com.sgu.givingsgu.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.quang.lilyshop.activity.BaseActivity
import com.sgu.givingsgu.adapter.TransactionAdapter
import com.sgu.givingsgu.databinding.ActivityDonationHistoryBinding
import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.network.response.TransactionResponse
import com.sgu.givingsgu.viewmodel.DonationHistoryViewModel
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class DonationHistoryActivity : BaseActivity() {
    private lateinit var binding: ActivityDonationHistoryBinding
    private val viewModel = DonationHistoryViewModel()
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var project: Project
    private var transactions : MutableList<TransactionResponse> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDonationHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        settingUpListener()
    }

    private fun init() {

        project = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("project", Project::class.java)!!
        } else {
            intent.getParcelableExtra("project")!!
        }

        transactionAdapter = TransactionAdapter(transactions)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerView.context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.recyclerView.adapter = transactionAdapter

        viewModel.fetchTransaction(project.projectId, 0)
        viewModel.totalPages.observe(this) {
            binding.totalPages.text = "/" + it.toString()
        }
        viewModel.currentPage.observe(this) {
            binding.page.setText((it+1).toString())
            if (it == 0) {
                binding.previousPage.visibility = View.INVISIBLE
            } else {
                binding.previousPage.visibility = View.VISIBLE
            }
            if (it == (viewModel.totalPages.value?.minus(1))) {
                binding.nextPage.visibility = View.INVISIBLE
            } else {
                binding.nextPage.visibility = View.VISIBLE
            }
            viewModel.transaction.observe(this)
            {
                transactions = it as MutableList<TransactionResponse>
                transactionAdapter = TransactionAdapter(transactions)
                binding.recyclerView.adapter = transactionAdapter


            }



        }
    }

    private fun settingUpListener() {

        binding.backBtn.setOnClickListener() {
            finish()
        }

        binding.previousPage.setOnClickListener {
            val currentPage = viewModel.currentPage.value ?: 0
            if (currentPage > 0) {
                viewModel.fetchTransaction(project.projectId, currentPage - 1)
            }
        }

        binding.nextPage.setOnClickListener {
            val currentPage = viewModel.currentPage.value ?: 0
            val totalPages = viewModel.totalPages.value ?: 0
            if (currentPage < totalPages - 1) {
                viewModel.fetchTransaction(project.projectId, currentPage + 1)
            }
        }


        binding.downloadFile.setOnClickListener {
//            viewModel.exportTransactionsToExcel(project.projectId, object :
//                DonationHistoryViewModel.OnExportExcelListener {
//                override fun onExportExcelSuccess(responseBody: ResponseBody) {
//                    saveFileToDevice(responseBody)
//                }
//
//                override fun onExportExcelFailed(message: String) {
//                    Toast.makeText(this@DonationHistoryActivity, message, Toast.LENGTH_SHORT).show()
//                }
//            })

            val apiUrl = "https://givingsguserver-production.up.railway.app/api/transactions/export/excel/${project.projectId}"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(apiUrl))
            startActivity(intent)
        }


        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.page.setOnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                // Ẩn bàn phím khi người dùng nhấn Enter hoặc Next
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                val input = binding.page.text.toString()
                val pageNumber = input.toIntOrNull()

                if (pageNumber != null && pageNumber > 0) {
                    viewModel.fetchTransaction(project.projectId, pageNumber-1)
                } else {
                    Toast.makeText(this, "Please enter a valid page number!", Toast.LENGTH_SHORT)
                        .show()
                }
                true
            }
            else {
                false
            }
        }
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }


    fun saveFileToDevice(responseBody: ResponseBody) {
        val file = File(getExternalFilesDir(null), "transactions.xlsx") // Thực hiện trong Activity

        try {
            val inputStream: InputStream = responseBody.byteStream()
            val outputStream = FileOutputStream(file)
            val buffer = ByteArray(4096)
            var bytesRead: Int

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()

            Toast.makeText(this, "File saved to ${file.absolutePath}", Toast.LENGTH_LONG).show()

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error saving file", Toast.LENGTH_LONG).show()
        }
    }
}
