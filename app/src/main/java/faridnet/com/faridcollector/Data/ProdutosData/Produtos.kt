package faridnet.com.faridcollector.Data.ProdutosData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class Produtos (
    @PrimaryKey(autoGenerate = true)
    val produtoId: Int,
    val codBarras: String,
    val descricao: String

)