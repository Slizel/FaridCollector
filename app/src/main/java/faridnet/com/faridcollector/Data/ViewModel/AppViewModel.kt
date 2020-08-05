package faridnet.com.faridcollector.Data.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import faridnet.com.faridcollector.Data.ContagensData.Contagens
import faridnet.com.faridcollector.Data.ContagensData.ContagensRepository
import faridnet.com.faridcollector.Data.Database.AppDatabase
import faridnet.com.faridcollector.Data.ProdutosData.Produtos
import faridnet.com.faridcollector.Data.ProdutosData.ProdutosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    val Cont_readAllData: LiveData<List<Contagens>>
    private val Cont_repository: ContagensRepository

    val Prod_readAllData: LiveData<List<Produtos>>
    private val Prod_repository: ProdutosRepository

    init {

        val contagensDao = AppDatabase.getDatabase(application).contagensDao()

        Cont_repository =
            ContagensRepository(
                contagensDao
            )
        Cont_readAllData = Cont_repository.readAllData
    }

    init {
        val produtosDao = AppDatabase.getDatabase(application).produtosDao()

        Prod_repository =
            ProdutosRepository(
                produtosDao
            )
        Prod_readAllData = Prod_repository.readAllData
    }

    fun addContagens(contagens: Contagens) {
        viewModelScope.launch(Dispatchers.IO) {
            Cont_repository.addContagem(contagens)
        }
    }

    fun addProdutos(produtos: Produtos) {
        viewModelScope.launch(Dispatchers.IO) {
            Prod_repository.addProduto(produtos)
        }
    }


    fun deleteAllContagens() {
        viewModelScope.launch(Dispatchers.IO) {
            Cont_repository.deleteAllContagens()
        }
    }

    fun deleteAllProdutos() {
        viewModelScope.launch(Dispatchers.IO) {
            Prod_repository.deleteAllProdutos()
        }
    }

    //    fun deleteContagens(contagens: Contagens){
//        viewModelScope.launch(Dispatchers.IO){
//            Cont_repository.deleteContagens(contagens)
//        }
//    }

//        fun deleteProdutos(produtos: Produtos){
//        viewModelScope.launch(Dispatchers.IO){
//            Prod_repository.deleteProdutos(produtos)
//        }
//    }

}