package faridnet.com.faridcollector.Data.ProdutosData

import androidx.lifecycle.LiveData

class ProdutosRepository (private val produtosDao: ProdutosDao) {

    val readAllData: LiveData<List<Produtos>> = produtosDao.readAllData()

    suspend fun addProduto(produtos: Produtos){
        produtosDao.addProduto(produtos)
    }

    suspend fun deleteAllProdutos(){
        produtosDao.deleteAllProdutos()
    }

    suspend fun deleteProdutos(produtos: Produtos){
        produtosDao.deleteProdutos(produtos)
    }


}