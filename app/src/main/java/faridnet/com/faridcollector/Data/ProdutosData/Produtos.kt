package faridnet.com.faridcollector.Data.ProdutosData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class Produtos(
    val codBarras: String,
    @PrimaryKey(autoGenerate = true)
    val produtoId: Int,
    val descricao: String

){
    companion object {
        const val TABLE_NAME = "Produtos"
        const val TITLE = "title"
        const val PRODUTO_ID = "produtoId"
    }

}