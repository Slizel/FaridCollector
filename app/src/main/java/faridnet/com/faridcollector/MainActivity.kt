package faridnet.com.faridcollector

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import faridnet.com.faridcollector.Data.ContagensData.Contagens
import faridnet.com.faridcollector.Data.ProdutosData.Produtos
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import faridnet.com.faridcollector.Data.ViewModel.AppViewModel as AppViewModel


private lateinit var cAppViewModel: AppViewModel
private lateinit var pAppViewModel: AppViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        val FILE_NAME = "Contagem.txt"

        private var CONTAGEM_SHOWN = true

        // Request code for creating a PDF document.
        const val CREATE_FILE = 1

        // Request code for selecting a PDF document.
        const val PICK_TXT_FILE = 2
    }


    private lateinit var appViewModel: AppViewModel
    private lateinit var contagensList: List<Contagens>

    var mEditTextCodBarras: EditText? = null
    var mEditTextQtde: EditText? = null
    //var mEditTextData: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mEditTextCodBarras = findViewById(R.id.codBarrasEditText)
        mEditTextQtde = findViewById(R.id.contagemEditText)
        //mEditTextData = findViewById(R.id.codBarrasEditText)

        pAppViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        cAppViewModel = ViewModelProvider(this).get(AppViewModel::class.java)


        add_btn.setOnClickListener {
            insertDataToDatabase()
            //save()
        }

        add_btn2.setOnClickListener {
            clearDatabase()
        }

        add_btn3.setOnClickListener {
            exportDatabaseToCSVFile()

        }

        add_btn4.setOnClickListener {
            //openFile(PICK_PDF_FILE)
            //openDocumentPicker()
            editDocument()
        }

    }

//    private fun openDocumentPicker() {
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
//            type = "text/plain"
//            addCategory(Intent.CATEGORY_OPENABLE)
//        }
//        startActivityForResult(intent, OPEN_DOCUMENT_REQUEST_CODE)
//    }


    // Import buttom functions ----------------------------------------------------------
    private fun editDocument() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's
        // file browser.
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones).
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        // Filter to show only text files.
        intent.type = "text/plain"
        startActivityForResult(intent, EDIT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        if (requestCode == EDIT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            var uri: Uri? = null
            if (resultData != null) {
                uri = resultData.data


                if (uri != null) {
                    alterDocument(uri)
                }
            }
        }
    }

    fun alterDocument(uri: Uri) {

        try {
            contentResolver.openFileDescriptor(uri, "w")?.use { it ->
                FileOutputStream(it.fileDescriptor).use {

                    it.write(
                        ("000000000000000" +
                                "100151023" +
                                "ARROZ NEGRO LA PASTIN \n"

                                //"${System.currentTimeMillis()}\n"
                                ).toByteArray()
                    )

                    it.write(
                        ("000000000000000" +
                                "500143226" +
                                "JG.PANELAS TRAMONT.7P \n"

                                //"${System.currentTimeMillis()}\n"

                                ).toByteArray()
                    )

//                    val file = File(uri.path)
//                    val line: List<String> = file.readLines()
//                    line.forEach { line ->
//
//                    }

                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Export to CSV ---------------------------------------------------------------------

    private fun getCSVFileName(): String = "Contagens.csv"

    private fun exportDatabaseToCSVFile() {
        val csvFile = generateFile(this, getCSVFileName())
        if (csvFile != null) {

            if (CONTAGEM_SHOWN) {
                exportMoviesWithContentToCSVFile(csvFile)
            }

            Toast.makeText(this, "CSV Gerado!!!", Toast.LENGTH_LONG).show()
            val intent = goToFileIntent(this, csvFile)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Algo deu errado! Tente novamente", Toast.LENGTH_LONG).show()
        }
    }

//    private fun initData() {
//        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
//        appViewModel.Cont_readAllData.observe(this,
//            Observer { contagens: Observable? ->
//                contagensList = contagens
//
//            }
//
//        )
//    }

    fun exportMoviesWithContentToCSVFile(csvFile: File) {
        csvWriter().open(csvFile, append = false) {
            // Header
            writeRow(listOf("[id]", "[${Contagens.TABLE_NAME}]", "[${Produtos.TABLE_NAME}]"))
            contagensList.forEachIndexed { index, contagens ->
                val directorName: String =
                    appViewModel.Cont_readAllData.value?.find { it.contagemId == contagens.produtoId }?.quantidade
                        ?: ""
                writeRow(listOf(index, contagens.quantidade, directorName))
            }
        }
    }


    //Criar novo arquivo na pasta --------------------------------------------------------
    fun saveNewFileOnAppFolder() {
        val text: String = mEditTextCodBarras?.getText().toString()
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            fos.write(text.toByteArray())
            mEditTextCodBarras?.getText()?.clear()
            Toast.makeText(
                this, "Saved to " + filesDir + "/" + FILE_NAME,
                Toast.LENGTH_LONG
            ).show()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


    // Database --------------------------------------------------------------------------

    private fun insertDataToDatabase() {
        val codBarras = codBarrasEditText.text.toString()
        val qtde = contagemEditText.text.toString()
        val descricao = descricaoEditText.text.toString()

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        if (inputCheck(codBarras, qtde, descricao)) {
            //Create Product Object
            val contagem = Contagens(0, qtde.toInt(), qtde, currentDate)

            //Precisa criar váriavel que pega o código do produto
            val produto = Produtos(codBarras, 0, descricao)

            // Add Data to Database
            cAppViewModel.addContagens(contagem)
            pAppViewModel.addProdutos(produto)

            Toast.makeText(this, "Adicionado com sucesso", Toast.LENGTH_LONG).show()

        } else {
            Toast.makeText(this, "Preencha os campos, por gentileza", Toast.LENGTH_LONG).show()

        }
    }

    private fun inputCheck(codBarras: String, qtde: String, descricao: String): Boolean {
        return !(TextUtils.isEmpty(codBarras) && TextUtils.isEmpty(qtde) && TextUtils.isEmpty(
            descricao
        ))
    }

    private fun clearDatabase() {
        //Alert Dialog
        val builder = AlertDialog.Builder(this)
        builder.setPositiveButton("Sim") { _, _ ->
            cAppViewModel.deleteAllContagens()
            pAppViewModel.deleteAllProdutos()


            Toast.makeText(
                this,
                "Banco foi limpo",
                Toast.LENGTH_SHORT
            ).show()

        }
        builder.setNegativeButton("Não") { _, _ -> }
        builder.setTitle("Limpar Banco de Dados")
        builder.setMessage("Tem certeza que deseja limpar o BD?")
        builder.create().show()
    }

}


private const val OPEN_DOCUMENT_REQUEST_CODE = 0x33
private const val EDIT_REQUEST_CODE = 44

