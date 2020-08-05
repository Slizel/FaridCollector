package faridnet.com.faridcollector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import faridnet.com.faridcollector.Data.ContagensData.Contagens
import faridnet.com.faridcollector.Data.ProdutosData.Produtos
import faridnet.com.faridcollector.Data.ViewModel.AppViewModel
import kotlinx.android.synthetic.main.activity_main.*

private lateinit var cAppViewModel: AppViewModel
private lateinit var pAppViewModel: AppViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pAppViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        cAppViewModel = ViewModelProvider(this).get(AppViewModel::class.java)


        add_btn.setOnClickListener {
            insertDataToDatabase()
        }

        add_btn2.setOnClickListener {
            clearDatabase()
        }

    }

    private fun insertDataToDatabase() {
        val codBarras = codBarrasEditText.text.toString()
        val qtde = contagemEditText.text.toString()
        val descricao = descricaoEditText.text.toString()

        if (inputCheck(codBarras, qtde, descricao)){
            //Create Product Object
            val contagem =
                    Contagens(0, 0, qtde)
            val produto = Produtos(
                    0,
                    codBarras,
                    descricao
            )

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

}