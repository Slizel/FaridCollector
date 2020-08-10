package faridnet.com.faridcollector

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import faridnet.com.faridcollector.Data.ContagensData.Contagens
import faridnet.com.faridcollector.Data.ProdutosData.Produtos
import faridnet.com.faridcollector.Data.ViewModel.AppViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


private lateinit var cAppViewModel: AppViewModel
private lateinit var pAppViewModel: AppViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        val FILE_NAME = "Contagem.txt"

        // Request code for creating a PDF document.
        const val CREATE_FILE = 1

        // Request code for selecting a PDF document.
        const val PICK_TXT_FILE = 2
    }

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

        add_btn4.setOnClickListener {
            //openFile(PICK_PDF_FILE)
            openDocumentPicker()

        }



    }

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

    // Database
    private fun insertDataToDatabase() {
        val codBarras = codBarrasEditText.text.toString()
        val qtde = contagemEditText.text.toString()
        val descricao = descricaoEditText.text.toString()

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        if (inputCheck(codBarras, qtde, descricao)){
            //Create Product Object
            val contagem = Contagens(0, qtde, currentDate)

            //Precisa criar váriavel que pega o código do produto
            val produto = Produtos(codBarras,0 , descricao)

            // Add Data to Database
            cAppViewModel.addContagens(contagem)
            pAppViewModel.addProdutos(produto)

            Toast.makeText(this, "Adicionado com sucesso", Toast.LENGTH_LONG).show()

        }else{
            Toast.makeText(this, "Preencha os campos, por gentileza", Toast.LENGTH_LONG).show()

        }
    }

    private fun inputCheck(codBarras: String, qtde: String, descricao: String): Boolean{
        return !(TextUtils.isEmpty(codBarras) && TextUtils.isEmpty(qtde) && TextUtils.isEmpty(descricao))
    }

    private  fun clearDatabase(){
        //Alert Dialog
        val builder = AlertDialog.Builder(this)
        builder.setPositiveButton("Sim"){ _, _ ->
            cAppViewModel.deleteAllContagens()
            pAppViewModel.deleteAllProdutos()


            Toast.makeText(this,
                    "Banco foi limpo",
                    Toast.LENGTH_SHORT).show()

        }
        builder.setNegativeButton("Não"){ _, _ -> }
        builder.setTitle("Limpar Banco de Dados")
        builder.setMessage("Tem certeza que deseja limpar o BD?")
        builder.create().show()
    }

    private fun openDocumentPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "text/plain"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, OPEN_DOCUMENT_REQUEST_CODE)
    }

    fun openDirectory(pickerInitialUri: Uri) {
        // Choose a directory using the system's file picker.
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            // Provide read access to files and sub-directories in the user-selected
            // directory.
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

            // Optionally, specify a URI for the directory that should be opened in
            // the system file picker when it loads.
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }

        startActivityForResult(intent, OPEN_DOCUMENT_REQUEST_CODE)
    }


    fun openFile(pickerInitialUri: Uri) {
        //Allow the user to choose the file to open by invoking the ACTION_OPEN_DOCUMENT intent
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            // To show only the types of files that your app supports, specify a MIME type
            type = "text/plain"

            // Optionally, specify a URI for the file that should appear in the
            // system file picker when it loads.
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }

        startActivityForResult(intent, Companion.PICK_TXT_FILE)
    }


    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        if (requestCode == OPEN_DOCUMENT_REQUEST_CODE
            && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            resultData?.data?.also { uri ->
                // Perform operations on the document using its URI.

                val contentResolver = applicationContext.contentResolver

                fun alterDocument(uri: Uri) {
                    try {
                        contentResolver.openFileDescriptor(uri, "w")?.use {
                            FileOutputStream(it.fileDescriptor).use {
                                it.write(
                                    ("Overwritten at ${System.currentTimeMillis()}\n")
                                        .toByteArray()
                                )
                                Toast.makeText(applicationContext, "passou", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            }
        }
    }
}

private const val OPEN_DOCUMENT_REQUEST_CODE = 0x33
//private const val TAG = "MainActivity"
//private const val LAST_OPENED_URI_KEY =
//    "com.example.android.actionopendocument.pref.LAST_OPENED_URI_KEY"